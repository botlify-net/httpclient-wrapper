package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientWrapper;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.Semaphore;

import static net.httpclient.wrapper.HttpClientWrapper.logger;

public class HttpClientSessionAsync extends HttpClientSession {

    /*
     $      Variable of the class
     */

    private final Semaphore semaphore;
    private final int MAX_SYNCHRONOUS_REQUEST;

    /*
     $      Constructor
     */

    public HttpClientSessionAsync() {
        this(3);
    }

    public HttpClientSessionAsync(int maxSynchronousRequest) {
        try {
            this.MAX_SYNCHRONOUS_REQUEST = maxSynchronousRequest;
            this.semaphore = new Semaphore(maxSynchronousRequest);
            this.httpClient = newHttpClient();
            this.requestConfig = setRequestConfig();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setUserAgent(getUserAgent());

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
        PoolingHttpClientConnectionManager poolingClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingClientConnectionManager.setValidateAfterInactivity((int) (getTimeout() / 2));
        poolingClientConnectionManager.setMaxTotal(MAX_SYNCHRONOUS_REQUEST == 0 ? 3 : MAX_SYNCHRONOUS_REQUEST);
        poolingClientConnectionManager.setDefaultMaxPerRoute(MAX_SYNCHRONOUS_REQUEST == 0 ? 3 : MAX_SYNCHRONOUS_REQUEST);

        /*
         * Verify if the Bright data proxy is enabled.
         * If it is enabled, the HttpClient will use the proxy.
         */
        if (HttpClientWrapper.hasValidBrightDataProperty()) {
            HttpHost brightDataProxy = new HttpHost(HttpClientWrapper.getBrightDataHost(), HttpClientWrapper.getBrightDataPort());

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(HttpClientWrapper.getBrightDataUsername(), HttpClientWrapper.getBrightDataPassword());
            credentialsProvider.setCredentials(new AuthScope(brightDataProxy), credentials);

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setProxy(brightDataProxy);
            //httpClientBuilder.setProxy(new HttpHost("127.0.0.1", 8888));
        }

        /*
         * Build the HttpClient for the client with HttpClientBuilder.
         */
        httpClientBuilder.setDefaultCookieStore(getHttpCookieStore());
        httpClientBuilder.setConnectionManager(poolingClientConnectionManager);
        httpClientBuilder.setMaxConnTotal(MAX_SYNCHRONOUS_REQUEST);
        httpClientBuilder.setMaxConnPerRoute(MAX_SYNCHRONOUS_REQUEST);
        return (httpClientBuilder.build());
    }

    @Override
    public RequestResponse sendGet(String url) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            RequestResponse result = super.sendGet(url);
            semaphore.release();
            return (result);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return (null);
    }

    @Override
    public RequestResponse sendPost(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            RequestResponse result = super.sendPost(url, content, contentType);
            semaphore.release();
            return (result);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return (null);
    }

    @Override
    public RequestResponse sendForm(String url, List<NameValuePair> form) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            RequestResponse response = super.sendForm(url, form);
            semaphore.release();
            return (response);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return (null);
    }

    @Override
    public RequestResponse sendDelete(String url) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            RequestResponse response = super.sendDelete(url);
            semaphore.release();
            return (response);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return (null);
    }

    @Override
    public RequestResponse sendPut(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        try {
            semaphore.acquire();
            RequestResponse response = super.sendPut(url, content, contentType);
            semaphore.release();
            return (response);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return (null);
    }
}
