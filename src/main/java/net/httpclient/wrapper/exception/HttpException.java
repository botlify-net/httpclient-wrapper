package net.httpclient.wrapper.exception;

import org.apache.http.HttpResponse;

class HttpException extends Exception {

    private final HttpResponse httpResponse;

    public HttpException(HttpResponse httpResponse) {
        super("http request return status code : " + httpResponse.getStatusLine().getStatusCode());
        this.httpResponse = httpResponse;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public int getStatusCode() {
        return (getHttpResponse().getStatusLine().getStatusCode());
    }

}