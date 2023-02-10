package net.httpclient.wrapper;

import net.httpclient.wrapper.events.HttpClientSessionEvent;
import net.httpclient.wrapper.events.HttpClientSessionListener;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import net.httpclient.wrapper.session.HttpClientSessionBasic;
import net.httpclient.wrapper.utils.BasicCookieStoreSerializerUtils;

import java.io.IOException;

public class Test implements HttpClientSessionListener {

    public static void main(String[] args) throws HttpClientException, IOException, HttpServerException {
        System.out.println("Hello World!");
        HttpClientSessionEvent.addHttpClientSessionListener(new Test());


        HttpClientSessionBasic httpClientSessionBasic = new HttpClientSessionBasic();
        RequestResponse requestResponse = httpClientSessionBasic.sendGet("https://lincos.tools/content/11-cookie");
        System.out.println(requestResponse.getStatusCode());
        System.out.println(BasicCookieStoreSerializerUtils.serializableToBase64(httpClientSessionBasic.getHttpCookieStore()));
        requestResponse = httpClientSessionBasic.sendGet("https://www.nautiljon.com/");
        System.out.println(requestResponse.getStatusCode());
        System.out.println(BasicCookieStoreSerializerUtils.serializableToBase64(httpClientSessionBasic.getHttpCookieStore()));
        System.out.println("Finished !\n");

        // print all cookies
        for (org.apache.http.cookie.Cookie cookie : httpClientSessionBasic.getHttpCookieStore().getCookies()) {
            System.out.println(cookie.getDomain() + " : " + cookie.getPath());
            System.out.println("\t => " + cookie.getName() + " : " + cookie.getValue());
        }

    }

    @Override
    public void onHttpClientCookiesUpdated(HttpClientSessionBasic httpClientSessionBasic) {
        System.out.println("Cookies updated");
    }
}
