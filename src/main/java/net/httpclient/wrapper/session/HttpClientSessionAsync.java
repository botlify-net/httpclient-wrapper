package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientProxyConfig;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class HttpClientSessionAsync extends HttpClientSessionBasic {

    /*
     $      Variable of the class
     */

    private final Semaphore semaphore;

    private final int maxSynchronousRequest;

    /*
     $      Constructor
     */

    public HttpClientSessionAsync(final int maxSynchronousRequest) {
        try {
            if (maxSynchronousRequest <= 0)
                throw (new IllegalArgumentException("Max synchronous request cannot be 0 or negative"));
            this.maxSynchronousRequest = maxSynchronousRequest;
            this.semaphore = new Semaphore(maxSynchronousRequest);
            this.httpClient = newHttpClient();
            this.requestConfig = setRequestConfig();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpClientSessionAsync(@NotNull final HttpClientProxyConfig config,
                                  final int maxSynchronousRequest) {
        try {
            if (maxSynchronousRequest <= 0)
                throw (new IllegalArgumentException("Max synchronous request cannot be 0 or negative"));
            this.maxSynchronousRequest = maxSynchronousRequest;
            this.semaphore = new Semaphore(maxSynchronousRequest);
            this.config = config;
            this.httpClient = newHttpClient();
            this.requestConfig = setRequestConfig();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     $      Override methods
     */

    @Override
    public @NotNull HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setUserAgent(getUserAgent());

        /*
         * This line below allow redirection (301, 302, 303, 307, 308) to be followed automatically
         * in the case of a POST request. By default, the HttpClient does not follow redirections for POST requests.
         * This is specified in the HTTP specification. (HTTP RFC 2616)
         */
        httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());

        /*
         * Create a SSLContext that uses our Trust Strategy to trust all self-signed certificates.
         */
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        SSLContext sslContext = sslContextBuilder.loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);

        RegistryBuilder<ConnectionSocketFactory> regBuilder = RegistryBuilder.create();
        regBuilder.register("https", sslConnectionSocketFactory);
        regBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = regBuilder.build();

        /*
         * PoolingHttpClientConnectionManager allow to reuse connections
         * and avoid the creation of new connections when the HttpClient is used in multiple threads.
         * This is useful when the HttpClient is used in a multithreaded environment.
         */
        PoolingHttpClientConnectionManager poolingClientConnectionManager = null;
        if (maxSynchronousRequest > 0) {
            poolingClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolingClientConnectionManager.setValidateAfterInactivity((int) (getTimeout() / 2));
            poolingClientConnectionManager.setMaxTotal(maxSynchronousRequest);
            poolingClientConnectionManager.setDefaultMaxPerRoute(maxSynchronousRequest);
        }

        /*
         * Verify if the Bright data proxy is enabled.
         * If it is enabled, the HttpClient will use the proxy.
         */
        if (config != null) {
            HttpHost brightDataProxy = new HttpHost(config.getHost(), config.getPort());
            if (config.hasValidCredentials()) {
                assert config.getUsername() != null;
                assert config.getPassword() != null;
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(),
                        config.getPassword());
                credentialsProvider.setCredentials(new AuthScope(brightDataProxy), credentials);
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            httpClientBuilder.setProxy(brightDataProxy);
        }

        /*
         * Build the HttpClient for the client with HttpClientBuilder.
         */
        httpClientBuilder.setDefaultCookieStore(getHttpCookieStore());
        if (poolingClientConnectionManager != null) {
            httpClientBuilder.setConnectionManager(poolingClientConnectionManager);
            httpClientBuilder.setMaxConnTotal(maxSynchronousRequest);
            httpClientBuilder.setMaxConnPerRoute(maxSynchronousRequest);
        }
        return (httpClientBuilder.build());
    }

    /*
     $      Requests
     */

    // GET

    @Override
    public @NotNull RequestResponse sendGet(@NotNull final String url,
                                            @NotNull final List<Header> headers,
                                            @NotNull final RequestConfig requestConfig) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            try {
                return (super.sendGet(url, headers, requestConfig));
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // POST

    @Override
    public @NotNull RequestResponse sendPost(@NotNull final String url,
                                             @NotNull final String content,
                                             @NotNull final ContentType contentType,
                                             @Nullable final List<Header> headers) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            try {
                return (super.sendPost(url, content, contentType, headers));
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // FORM

    @Override
    public @NotNull RequestResponse sendForm(@NotNull final String url,
                                             @NotNull final List<NameValuePair> form,
                                             @NotNull final RequestConfig requestConfig) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            try {
                return (super.sendForm(url, form, requestConfig));
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // PUT

    @Override
    public @NotNull RequestResponse sendPut(@NotNull final String url,
                                            @NotNull final String content,
                                            @NotNull final ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            try {
                return (super.sendPut(url, content, contentType));
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
