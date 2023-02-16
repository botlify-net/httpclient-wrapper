package net.httpclient.wrapper.utils;

import net.httpclient.wrapper.enums.AcceptLanguage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FakeHeadersTest {

    @Test
    void getAcceptLanguageOne() {

        final AcceptLanguage primary = AcceptLanguage.ENGLISH;
        FakeHeaders fakeHeaders = new FakeHeaders.Builder(primary).build();

        String acceptLanguage = fakeHeaders.getAcceptLanguage(primary, null);
        assertEquals("en", acceptLanguage);
    }

    @Test
    void getAcceptLanguageTwo() {

        final AcceptLanguage primary = AcceptLanguage.ENGLISH;
        FakeHeaders fakeHeaders = new FakeHeaders.Builder(primary).build();

        Map<AcceptLanguage, Float> secondary = new HashMap<>();
        secondary.put(AcceptLanguage.FRENCH_STANDARD, 0.8f);

        String acceptLanguage = fakeHeaders.getAcceptLanguage(primary, secondary);
        System.out.println(acceptLanguage);
    }

}