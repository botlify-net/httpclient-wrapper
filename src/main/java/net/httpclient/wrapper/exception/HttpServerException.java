package net.httpclient.wrapper.exception;

import org.apache.http.HttpResponse;

public class HttpServerException extends HttpException {

    public HttpServerException(HttpResponse httpResponse) {
        super(httpResponse);
    }

}
