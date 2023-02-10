package net.httpclient.wrapper;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.Cookie;

public class HttpClientWrapper {

    public static Logger logger = LogManager.getLogger(HttpClientWrapper.class);

    /**
     * This function will check if all brightdata configuration are set.
     * @return True if all configuration are set, false otherwise.
     */
    public static boolean hasValidBrightDataProperty() {
        String[] properties = new String[] {
            "httpclient.wrapper.proxy.bright-data.host",
            "httpclient.wrapper.proxy.bright-data.port",
            "httpclient.wrapper.proxy.bright-data.username",
            "httpclient.wrapper.proxy.bright-data.password"
        };
        for (String property : properties) {
            if (System.getProperty(property) == null) {
                return (false);
            }
        }
        return (true);
    }

    /*
     $      Get BrightData configuration
     */

    /**
     * This function will return the brightdata host property set.
     * @return The brightdata host property set.
     */
    public static String getBrightDataHost() {
        return (System.getProperty("httpclient.wrapper.proxy.bright-data.host"));
    }

    /**
     * This function will return the brightdata port property set.
     * @return The brightdata port property set.
     */
    public static int getBrightDataPort() {
        return (Integer.parseInt(System.getProperty("httpclient.wrapper.proxy.bright-data.port")));
    }

    /**
     * This function will return the brightdata username property set.
     * @return The brightdata username property set.
     */
    public static String getBrightDataUsername() {
        return (System.getProperty("httpclient.wrapper.proxy.bright-data.username"));
    }

    /**
     * This function will return the brightdata password property set.
     * @return The brightdata password property set.
     */
    public static String getBrightDataPassword() {
        return (System.getProperty("httpclient.wrapper.proxy.bright-data.password"));
    }

    /*
     $      Set BrightData configuration
     */

    public static void setBrightDataHost(@NotNull final String host) {
        System.setProperty("httpclient.wrapper.proxy.bright-data.host", host);
    }

    public static void setBrightDataPort(final int port) {
        System.setProperty("httpclient.wrapper.proxy.bright-data.port", String.valueOf(port));
    }

    public static void setBrightDataUsername(@NotNull final String username) {
        System.setProperty("httpclient.wrapper.proxy.bright-data.username", username);
    }

    public static void setBrightDataPassword(@NotNull final String password) {
        System.setProperty("httpclient.wrapper.proxy.bright-data.password", password);
    }

}
