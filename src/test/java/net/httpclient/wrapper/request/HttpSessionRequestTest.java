package net.httpclient.wrapper.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpSessionRequestTest {

    @Test
    public void test() {
        HttpSessionRequest request = HttpSessionRequest.builder()
                .build();
    }

}