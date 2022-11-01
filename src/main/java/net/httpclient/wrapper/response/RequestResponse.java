package net.httpclient.wrapper.response;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

public class RequestResponse {

    private final Date start, end;
    private String rawResponse = null;
    private final HttpResponse httpResponse;

    /**
     * This object wrap the original HttpResponse object and provide some additional informations.
     * He also consumes the response body to avoid memory leak (the response body is not consumed by default).
     * The response is stored in the rawResponse field.
     * This object is used to store the response of a request.
     * @param httpResponse The original HttpResponse object.
     * @param start The start date of the request.
     * @throws IOException If an error occurs while reading the response body.
     */
    public RequestResponse(HttpResponse httpResponse, Date start) throws IOException {
        this.httpResponse = httpResponse;
        this.start = start;
        try {
            if (httpResponse.getEntity() != null)
                rawResponse = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            throw new IOException(e);
        }
        this.end = new Date();
    }

    /*
     $      Getter & Setter
     */

    public Header[] getHeaders(String name) {
        return (httpResponse.getHeaders(name));
    }

    public Header getFirstHeader(String headerName) {
        return httpResponse.getFirstHeader(headerName);
    }

    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    public String getRawResponse() {
        return (rawResponse);
    }

    public JSONObject toJSONObject() {
        try {
            return (new JSONObject(rawResponse));
        } catch (Exception e) {
            return (null);
        }
    }

    public JSONArray toJSONArray() {
        try {
            return (new JSONArray(rawResponse));
        } catch (Exception e) {
            return (null);
        }
    }

    public long getTime() {
        if (start == null || end == null)
            return (-1);
        return (end.getTime() - start.getTime());
    }

    public String getReasonPhrase() {
        return (httpResponse.getStatusLine().getReasonPhrase());
    }

}
