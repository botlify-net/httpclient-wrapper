package net.httpclient.wrapper.exception;

import net.httpclient.wrapper.response.RequestResponse;

public class HttpException extends Exception {

    private final RequestResponse requestResponse;

    public HttpException(RequestResponse requestResponse) {
        super("Http request return status code : " + requestResponse.getStatusCode());
        this.requestResponse = requestResponse;
    }

    public RequestResponse getRequestResponse() {
        return (requestResponse);
    }

}
