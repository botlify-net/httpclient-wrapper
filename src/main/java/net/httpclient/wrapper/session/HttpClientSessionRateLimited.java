package net.httpclient.wrapper.session;

import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.ratelimiter.RateLimiter;
import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class HttpClientSessionRateLimited extends HttpClientSession {

    private final RateLimiter rateLimiter;

    public HttpClientSessionRateLimited() {
        this(1.0);
    }

    public HttpClientSessionRateLimited(double permitsPerSecond) {
        super();
        rateLimiter = new RateLimiter(Duration.ofSeconds((int)permitsPerSecond));
    }

    @Override
    public RequestResponse sendPost(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return (super.sendPost(url, content, contentType));
    }

    @Override
    public RequestResponse sendGet(String url) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return (super.sendGet(url));
    }

    @Override
    public RequestResponse sendDelete(String url) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
        return (super.sendDelete(url));
    }

    @Override
    public RequestResponse sendPut(String url, String content, ContentType contentType) throws IOException, HttpClientException, HttpServerException {
        rateLimiter.acquire();
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

}
