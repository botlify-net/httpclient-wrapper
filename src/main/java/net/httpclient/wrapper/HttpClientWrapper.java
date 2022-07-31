package net.httpclient.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientWrapper {

    public static Logger logger = LogManager.getLogger(HttpClientWrapper.class);

    /**
     * This function will check if all brightdata configuration are set.
     * @return True if all configuration are set, false otherwise.
     */
    public static boolean hasValidBrightDataProperty() {
        String[] properties = new String[] {
            "httpClientWrapperBrightDataHost",
            "httpClientWrapperBrightDataPort",
            "httpClientWrapperBrightDataUsername",
            "httpClientWrapperBrightDataPassword"
        };
        for (String property : properties) {
            if (System.getProperty(property) == null) {
                return (false);
            }
        }
        return (true);
    }

    /**
     * This function will return the brightdata host property set.
     * @return The brightdata host property set.
     */
    public static String getBrightDataHost() {
        return (System.getProperty("httpClientWrapperBrightDataHost"));
    }

    /**
     * This function will return the brightdata port property set.
     * @return The brightdata port property set.
     */
    public static int getBrightDataPort() {
        return (Integer.parseInt(System.getProperty("httpClientWrapperBrightDataPort")));
    }

    /**
     * This function will return the brightdata username property set.
     * @return The brightdata username property set.
     */
    public static String getBrightDataUsername() {
        return (System.getProperty("httpClientWrapperBrightDataUsername"));
    }

    /**
     * This function will return the brightdata password property set.
     * @return The brightdata password property set.
     */
    public static String getBrightDataPassword() {
        return (System.getProperty("httpClientWrapperBrightDataPassword"));
    }

}
