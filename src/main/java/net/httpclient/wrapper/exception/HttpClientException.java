package net.httpclient.wrapper.exception;

import net.httpclient.wrapper.response.RequestResponse;
import org.apache.http.HttpResponse;

public class HttpClientException extends HttpException {

    /**
     * This exception is throw when the code of the response
     * is between 400 and 499 included.
     * @param httpResponse The response from the server.
     */
    public HttpClientException(RequestResponse httpResponse) {
        super(httpResponse);
    }

}
