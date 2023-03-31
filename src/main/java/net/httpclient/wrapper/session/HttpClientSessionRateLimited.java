package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientProxyConfig;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.ratelimiter.RateLimiter;
import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class HttpClientSessionRateLimited extends HttpClientSessionBasic {

    private final RateLimiter rateLimiter;

    /*
     $      Constructors
     */

    public HttpClientSessionRateLimited(@NotNull final Duration duration) {
        super();
        rateLimiter = new RateLimiter(duration);
    }

    public HttpClientSessionRateLimited(@NotNull final Duration duration,
                                        @NotNull final HttpClientProxyConfig config) {
        super(config);
        rateLimiter = new RateLimiter(duration);
    }

    /*
     $      Requests
     */

    @Override
    public @NotNull RequestResponse sendPut(@NotNull String url, @NotNull String content, @NotNull ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return (super.sendPut(url, content, contentType));
    }

    @Override
    public @NotNull RequestResponse sendGet(@NotNull String url, @NotNull List<Header> headers, @NotNull RequestConfig requestConfig) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return super.sendGet(url, headers, requestConfig);
    }

    @Override
    public @NotNull RequestResponse sendPost(@NotNull String url, @NotNull String content, @NotNull ContentType contentType, @Nullable List<Header> headers) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return super.sendPost(url, content, contentType, headers);
    }

    @Override
    public @NotNull RequestResponse sendForm(@NotNull String url, @NotNull List<NameValuePair> form, @NotNull RequestConfig requestConfig) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return super.sendForm(url, form, requestConfig);
    }

    /**
     * Send a form to the server.
     * This method will bypass the rate limiter.
     * @param url The url to send the form to.
     * @return The response from the server.
     * @throws IOException If an error occurs.
     * @throws HttpClientException If an error occurs.
     */
    public RequestResponse forceSendGet(String url) throws IOException, HttpClientException, HttpServerException {
        return (super.sendGet(url));
    }

    /**
     * Send a form to the server.
     * This method will bypass the rate limiter.
     * @param url The url to send the form to.
     * @return The response from the server.
     * @throws IOException If an error occurs.
     * @throws HttpClientException If an error occurs.
     */
    public RequestResponse forceSendPost(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        return (super.sendPost(url, content, contentType));
    }

    /**
     * Send a form to the server.
     * This method will bypass the rate limiter.
     * @param url The url to send the form to.
     * @return The response from the server.
     * @throws IOException If an error occurs.
     * @throws HttpClientException If an error occurs.
     */
    public RequestResponse forceSendDelete(String url) throws IOException, HttpClientException, HttpServerException {
        return (super.sendDelete(url));
    }

    /**
     * Send a form to the server.
     * This method will bypass the rate limiter.
     * @param url The url to send the form to.
     * @return The response from the server.
     * @throws IOException If an error occurs.
     * @throws HttpClientException If an error occurs.
     */
    public RequestResponse forceSendPut(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        return (super.sendPut(url, content, contentType));
    }

    /**
     * Send a form to the server.
     * This method will bypass the rate limiter.
     * @param url The url to send the form to.
     * @return The response from the server.
     * @throws IOException If an error occurs.
     * @throws HttpClientException If an error occurs.
     */
    public RequestResponse forceSendForm(String url, List<NameValuePair> form) throws IOException, HttpClientException, HttpServerException {
        return (super.sendForm(url, form));
    }

    /*
     $      Getters
     */

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }
}
