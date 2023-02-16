package net.httpclient.wrapper.headers;

import net.httpclient.wrapper.headers.enums.AcceptLanguage;
import net.httpclient.wrapper.headers.FakeHeaders;
import org.apache.http.Header;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FakeHeadersTest {

    @Test
    void getAcceptLanguageOne() {

        final AcceptLanguage primary = AcceptLanguage.EN;
        FakeHeaders fakeHeaders = new FakeHeaders.Builder(primary).build();

        String acceptLanguage = fakeHeaders.getAcceptLanguage(primary, null);
        assertEquals("en", acceptLanguage);
    }

    @Test
    void getAcceptLanguageTwo() {

        final AcceptLanguage primary = AcceptLanguage.FR_FR;
        FakeHeaders fakeHeaders = new FakeHeaders.Builder(primary).build();

        Map<AcceptLanguage, Float> secondary = new HashMap<>();
        secondary.put(AcceptLanguage.FR, 0.9f);
        secondary.put(AcceptLanguage.EN_US, 0.8f);
        secondary.put(AcceptLanguage.EN, 0.7f);

        String acceptLanguage = fakeHeaders.getAcceptLanguage(primary, secondary);

        assertEquals("fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7", acceptLanguage);
    }

    @Test
    void testConstructFakeHeader() {
        final AcceptLanguage primary = AcceptLanguage.EN;
        Map<AcceptLanguage, Float> secondary = new HashMap<>();
        secondary.put(AcceptLanguage.FR, 0.9f);
        secondary.put(AcceptLanguage.EN_US, 0.8f);
        secondary.put(AcceptLanguage.EN, 0.7f);

        FakeHeaders fakeHeaders = new FakeHeaders.Builder(primary)
                .setAcceptSecondaryLanguages(secondary)
                .build();
        assertNotNull(fakeHeaders.getHeaders());
        assertNotEquals(0, fakeHeaders.getHeaders().size());
    }

}