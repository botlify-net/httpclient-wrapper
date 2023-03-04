package net.httpclient.wrapper.session;

import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface HttpClientSession {

    @NotNull HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException;

    void resetHttpClient();

    @NotNull RequestConfig.Builder getRequestConfig();

    @NotNull HttpClient getHttpClient();

    @NotNull BasicCookieStore getHttpCookieStore();

    @Nullable Object getMetadata();

    /*
     $      Requests
     */

    // GET

    @NotNull RequestResponse sendGet(@NotNull final String url) throws IOException, HttpClientException, HttpServerException;

    @NotNull RequestResponse sendGet(@NotNull final String url, @NotNull final RequestConfig requestConfig) throws IOException, HttpClientException, HttpServerException;

    // POST

    @NotNull RequestResponse sendPost(@NotNull final String url, @NotNull final String content, @NotNull final ContentType contentType) throws IOException, HttpClientException, HttpServerException;

    @NotNull RequestResponse sendPost(@NotNull final String url, @NotNull final JSONObject content, @NotNull final ContentType contentType) throws HttpClientException, IOException, HttpServerException;

    @NotNull RequestResponse sendPost(@NotNull final String url, @NotNull final JSONObject content, @NotNull final ContentType contentType, @Nullable final List<Header> headers) throws HttpClientException, IOException, HttpServerException;

    @NotNull RequestResponse sendPost(@NotNull final String url, @NotNull final String content, @NotNull final ContentType contentType, @Nullable final List<Header> headers) throws IOException,
            HttpClientException, HttpServerException;

    // FORM

    @NotNull RequestResponse sendForm(@NotNull final String url, @NotNull final List<NameValuePair> form) throws IOException, HttpClientException, HttpServerException;

    @NotNull RequestResponse sendForm(@NotNull final String url, @NotNull final List<NameValuePair> form, @NotNull final RequestConfig requestConfig) throws IOException, HttpClientException, HttpServerException;

    // DELETE

    @NotNull RequestResponse sendDelete(@NotNull final String url) throws IOException, HttpClientException, HttpServerException;

    // PUT

    @NotNull RequestResponse sendPut(@NotNull final String url, @NotNull final String content, @NotNull final ContentType contentType) throws IOException, HttpClientException, HttpServerException;

}
