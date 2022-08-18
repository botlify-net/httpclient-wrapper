package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientWrapper;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
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
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The basic class for all the http requests.
 * No security for multithreaded application.
 */
public class HttpClientSessionBasic {

    /*
     $      Variable of the class
     */

    protected HttpClient httpClient;
    protected final BasicCookieStore httpCookieStore = new BasicCookieStore();
    protected RequestConfig.Builder requestConfig;
    protected String userAgent = RandomUserAgent.getRandomUserAgent();

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

    public HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setUserAgent(userAgent);

        /*
         * Create a SSLContext that uses our Trust Strategy to trust all self-signed certificates.
         */
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        SSLContext sslContext = sslContextBuilder.loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();

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
        httpClientBuilder.setDefaultCookieStore(httpCookieStore);
        httpClientBuilder.setSSLContext(sslContext);
        return (httpClientBuilder.build());
    }

    public RequestResponse sendGet(String url) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getRequestConfig().build());
        httpGet.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        HttpResponse httpResponse = getHttpClient().execute(httpGet);
        assertRequest(httpResponse, start);
        return (new RequestResponse(httpResponse, start));
    }

    public RequestResponse sendPost(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getRequestConfig().build());
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
        httpPost.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPost.setEntity(new StringEntity(content));
        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        assertRequest(httpResponse, start);
        return (new RequestResponse(httpResponse, start));
    }

    public RequestResponse sendForm(String url, List<NameValuePair> form) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getRequestConfig().build());
        httpPost.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        assertRequest(httpResponse, start);
        return (new RequestResponse(httpResponse, start));
    }

    public RequestResponse sendDelete(String url) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(getRequestConfig().build());
        httpDelete.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        HttpResponse httpResponse = httpClient.execute(httpDelete);
        assertRequest(httpResponse, start);
        return (new RequestResponse(httpResponse, start));
    }

    public RequestResponse sendPut(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        Date start = new Date();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(getRequestConfig().build());
        httpPut.addHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
        httpPut.addHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPut.setEntity(new StringEntity(content));
        HttpResponse httpResponse = getHttpClient().execute(httpPut);
        assertRequest(httpResponse, start);
        return (new RequestResponse(httpResponse, start));
    }

    private void assertRequest(HttpResponse httpResponse, Date startDate) throws HttpClientException, HttpServerException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 500 && statusCode <= 599)
            throw new HttpServerException(new RequestResponse(httpResponse, startDate));
        else if (statusCode >= 400 && statusCode <= 499)
            throw new HttpClientException(new RequestResponse(httpResponse, startDate));
    }

    /*
     *      Getters and Setters
     */

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public RequestConfig.Builder getRequestConfig() {
        return requestConfig;
    }

    public BasicCookieStore getHttpCookieStore() {
        return httpCookieStore;
    }

    public long getTimeout() {
        return TIMEOUT;
    }

    public void setHttpClient(HttpClient httpClient) {
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

}
