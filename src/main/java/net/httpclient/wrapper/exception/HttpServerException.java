package net.httpclient.wrapper.exception;

import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.HttpResponse;

public class HttpServerException extends HttpException {

    /**
     * This exception is throw when the code of the response
     * is between 500 and 599 included.
     * @param httpResponse The response from the server.
     */
    public HttpServerException(RequestResponse httpResponse) {
        super(httpResponse);
    }

}
