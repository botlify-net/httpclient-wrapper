package net.httpclient.wrapper.session;

import lombok.Getter;
import net.httpclient.wrapper.HttpClientProxyConfig;
import net.httpclient.wrapper.HttpClientWrapper;
import net.httpclient.wrapper.events.HttpClientSessionEvent;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import net.httpclient.wrapper.utils.BasicCookieStoreSerializerUtils;
import net.httpclient.wrapper.utils.RandomUserAgent;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.httpclient.wrapper.HttpClientWrapper.logger;

/**
 * The basic class for all the http requests.
 * No security for multithreaded application.
 */
public class HttpClientSessionBasic implements HttpClientSession {

    /*
     $      Variable of the class
     */

    protected HttpClient httpClient;

    protected HttpClientProxyConfig config = null;

    protected final BasicCookieStore httpCookieStore = new BasicCookieStore();

    protected RequestConfig.Builder requestConfig;

    protected String userAgent = RandomUserAgent.getRandomUserAgent();

    protected Object metadata = null;

    private final long TIMEOUT = TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS);

    /*
     $      Constructor
     */

    public HttpClientSessionBasic() {
        try {
            this.httpClient = newHttpClient();
            this.requestConfig = setRequestConfig();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpClientSessionBasic(@NotNull final HttpClientProxyConfig proxyConfig) {
        try {
            this.config = proxyConfig;
            this.httpClient = newHttpClient();
            this.requestConfig = setRequestConfig();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestConfig.Builder setRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout((int) TIMEOUT);
        builder.setSocketTimeout((int) TIMEOUT);
        builder.setConnectionRequestTimeout((int) TIMEOUT);
        builder.setRedirectsEnabled(false);
        builder.setCookieSpec(CookieSpecs.STANDARD_STRICT);
        return builder;
    }

    public void resetHttpClient() {
        try {
            this.httpCookieStore.clear();
            this.httpClient = newHttpClient();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setUserAgent(userAgent);

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
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(), config.getPassword());
                credentialsProvider.setCredentials(new AuthScope(brightDataProxy), credentials);
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            httpClientBuilder.setProxy(brightDataProxy);
        }

        /*
         * Build the HttpClient for the client with HttpClientBuilder.
         */
        httpClientBuilder.setDefaultCookieStore(httpCookieStore);
        httpClientBuilder.setSSLContext(sslContext);
        return (httpClientBuilder.build());
    }

    /*
     $      GET
     */

    public @NotNull RequestResponse sendGet(@NotNull final String url) throws IOException, HttpClientException, HttpServerException {
        return (sendGet(url, getRequestConfig().build()));
    }

    public @NotNull RequestResponse sendGet(@NotNull final String url,
                                            @NotNull final RequestConfig requestConfig) throws IOException,
            HttpClientException,
            HttpServerException {
        Date start = new Date();
        String oldCookieStoreSerialized = BasicCookieStoreSerializerUtils.serializableToBase64(httpCookieStore);

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        httpGet.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpGet.addHeader(HttpHeaders.USER_AGENT, userAgent);
        HttpResponse httpResponse = getHttpClient().execute(httpGet);
        assertRequest(httpResponse, start);

        verifyCookiesEvents(oldCookieStoreSerialized);
        return (new RequestResponse(httpResponse, start));
    }

    /*
     $      POST
     */

    public @NotNull RequestResponse sendPost(@NotNull String url, @NotNull String content, @NotNull ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        return (sendPost(url, content, contentType, null));
    }

    public @NotNull RequestResponse sendPost(@NotNull String url,
                                             @NotNull JSONObject content,
                                             @NotNull ContentType contentType) throws HttpClientException, IOException, HttpServerException {
        return (sendPost(url, content.toString(), contentType, null));
    }

    public @NotNull RequestResponse sendPost(@NotNull String url,
                                             @NotNull JSONObject content,
                                             @NotNull ContentType contentType,
                                             @Nullable List<Header> headers) throws HttpClientException, IOException, HttpServerException {
        return (sendPost(url, content.toString(), contentType, headers));
    }

    public @NotNull RequestResponse sendPost(@NotNull String url,
                                             @NotNull String content,
                                             @NotNull ContentType contentType,
                                             @Nullable List<Header> headers) throws IOException,
            HttpClientException, HttpServerException {
        Date start = new Date();
        String oldCookieStoreSerialized = BasicCookieStoreSerializerUtils.serializableToBase64(httpCookieStore);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getRequestConfig().build());
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
        httpPost.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPost.addHeader(HttpHeaders.USER_AGENT, userAgent);
        if (headers != null) {
            for (Header header : headers)
                httpPost.addHeader(header);
        }
        httpPost.setEntity(new StringEntity(content));
        HttpResponse httpResponse = getHttpClient().execute(httpPost);

        assertRequest(httpResponse, start);
        verifyCookiesEvents(oldCookieStoreSerialized);
        return (new RequestResponse(httpResponse, start));
    }

    /*
     $      FORM
     */

    public @NotNull RequestResponse sendForm(@NotNull String url,
                                             @NotNull List<NameValuePair> form) throws IOException,
            HttpClientException,
            HttpServerException {
        return (sendForm(url, form, getRequestConfig().build()));
    }

    public @NotNull RequestResponse sendForm(@NotNull String url,
                                             @NotNull List<NameValuePair> form,
                                             @NotNull RequestConfig requestConfig) throws IOException,
            HttpClientException,
            HttpServerException {
        Date start = new Date();
        String oldCookieStoreSerialized = BasicCookieStoreSerializerUtils.serializableToBase64(httpCookieStore);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPost.addHeader(HttpHeaders.USER_AGENT, userAgent);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);

        assertRequest(httpResponse, start);
        verifyCookiesEvents(oldCookieStoreSerialized);
        return (new RequestResponse(httpResponse, start));
    }

    public @NotNull RequestResponse sendDelete(@NotNull String url) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        String oldCookieStoreSerialized = BasicCookieStoreSerializerUtils.serializableToBase64(httpCookieStore);

        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(getRequestConfig().build());
        httpDelete.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpDelete.addHeader(HttpHeaders.USER_AGENT, userAgent);
        HttpResponse httpResponse = httpClient.execute(httpDelete);

        assertRequest(httpResponse, start);
        verifyCookiesEvents(oldCookieStoreSerialized);
        return (new RequestResponse(httpResponse, start));
    }

    public @NotNull RequestResponse sendPut(@NotNull String url, @NotNull String content, @NotNull ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        String oldCookieStoreSerialized = BasicCookieStoreSerializerUtils.serializableToBase64(httpCookieStore);

        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(getRequestConfig().build());
        httpPut.addHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
        httpPut.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPut.addHeader(HttpHeaders.USER_AGENT, userAgent);
        httpPut.setEntity(new StringEntity(content));
        HttpResponse httpResponse = getHttpClient().execute(httpPut);

        assertRequest(httpResponse, start);
        verifyCookiesEvents(oldCookieStoreSerialized);
        return (new RequestResponse(httpResponse, start));
    }

    /*
     $      Private mehtods
     */

    /**
     * Assert the request and throw an exception if the request is not valid.
     * @param httpResponse The response of the request.
     * @param startDate The start date of the request.
     * @throws HttpClientException If the request is not successful.
     * @throws HttpServerException If the server is not available.
     * @throws IOException If the request is not successful because of an network error.
     */
    private void assertRequest(HttpResponse httpResponse, Date startDate) throws HttpClientException, HttpServerException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 500 && statusCode <= 599)
            throw new HttpServerException(new RequestResponse(httpResponse, startDate));
        else if (statusCode >= 400 && statusCode <= 499)
            throw new HttpClientException(new RequestResponse(httpResponse, startDate));
    }

    /**
     * Verify if the cookie store has been modified.
     * Some requests can modify the cookie store, and this method will verify if the cookie store has been modified.
     * This methods can trigger HttpClientCookiesUpdatedEvent.
     * This methods should be called after a request.
     * @param serializedOldCookieStore The old cookie store before the request serialized as string.
     */
    private void verifyCookiesEvents(String serializedOldCookieStore) {
        if (HttpClientSessionEvent.countListeners() == 0)
            return;
        try {
            String serializedNewCookieStore = BasicCookieStoreSerializerUtils.serializableToBase64(httpCookieStore);
            if (!serializedOldCookieStore.equals(serializedNewCookieStore))
                HttpClientSessionEvent.triggerHttpClientCookiesUpdated(this);
        } catch (Exception e) {
            logger.warn("Error while verifying cookies events", e);
        }
    }

    /*
     *      Getters and Setters
     */

    public @NotNull HttpClient getHttpClient() {
        return httpClient;
    }

    public RequestConfig.@NotNull Builder getRequestConfig() {
        return requestConfig;
    }

    public @NotNull BasicCookieStore getHttpCookieStore() {
        return httpCookieStore;
    }

    public long getTimeout() {
        return TIMEOUT;
    }

    public void setHttpClient(@NotNull final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * This method will clear the cookie store
     * and push the new cookies from the gived cookie store in the cookie store.
     * @param httpCookieStore the new cookie store to use
     */
    public void setHttpCookieStore(BasicCookieStore httpCookieStore) {
        this.httpCookieStore.clear();
        for (Cookie cookie : httpCookieStore.getCookies()) {
            this.httpCookieStore.addCookie(cookie);
        }
    }

    /**
     * This method return the JsonObject from <a href="https://lumtest.com/echo.json">https://lumtest.com/echo.json</a>.
     * @return The JsonObject from <a href="https://lumtest.com/echo.json">https://lumtest.com/echo.json</a>
     */
    public JSONObject getIpInformation() throws HttpClientException, IOException, HttpServerException {
        RequestResponse requestResponse = sendGet("https://lumtest.com/echo.json");
        return (requestResponse.toJSONObject());
    }

    public String getUserAgent() {
        return (userAgent);
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public @Nullable Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}
