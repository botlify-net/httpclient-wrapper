package net.httpclient.wrapper.headers.enums;

import net.httpclient.wrapper.headers.enums.AcceptLanguage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AcceptLanguageTest {

    @Test
    void testToString() {
        AcceptLanguage al = AcceptLanguage.FR_FR;
        assertEquals("fr-FR", al.toString());
    }

    @Test
    void getCode() {
        AcceptLanguage al = AcceptLanguage.FR_FR;
        assertEquals("fr-FR", al.getCode());
    }

    @Test
    void getCountry() {
        AcceptLanguage al = AcceptLanguage.FR_FR;
        assertEquals("French (France)", al.getCountry());
    }
}