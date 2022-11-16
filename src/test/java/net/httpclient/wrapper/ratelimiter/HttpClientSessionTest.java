package net.httpclient.wrapper.ratelimiter;

import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import net.httpclient.wrapper.session.HttpClientSession;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientSessionTest {

    @Test
    public void testRedirectN() throws IOException, HttpServerException {
        try {
            HttpClientSession httpClientSession = new HttpClientSession();
            String url = "http://t.co/I5YYd9tddw";
            RequestConfig requestConfig = RequestConfig.custom()
                    .setRedirectsEnabled(true)
                    .build();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("url", "https://www.wikipedia.net/"));
            RequestResponse requestResponse = httpClientSession.sendForm(url, params, requestConfig);
            assertEquals(200, requestResponse.getStatusCode());
        } catch (HttpException e) {
            System.out.println(e.getRequestResponse().getRawResponse());
            Assertions.fail();
        }
    }

}
