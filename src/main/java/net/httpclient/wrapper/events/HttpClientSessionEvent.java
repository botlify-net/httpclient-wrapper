package net.httpclient.wrapper.events;

import net.httpclient.wrapper.session.HttpClientSession;

import java.util.ArrayList;

/**
 * This static class manage the HttpClientSession events.
 */
public class HttpClientSessionEvent {

    private static final ArrayList<HttpClientSessionListener> httpClientSessionBasics = new ArrayList<>();

    /*
     $      Add and remove listeners
     */

    /**
     * Add a listener to the list of listeners.
     * @param listener The listener to add.
     */
    public static void addHttpClientSessionListener(HttpClientSessionListener listener) {
        httpClientSessionBasics.add(listener);
    }

    /**
     * Remove a listener from the list of listeners.
     * @param listener The listener to remove.
     */
    public static void removeHttpClientSessionListener(HttpClientSessionListener listener) {
        httpClientSessionBasics.remove(listener);
    }

    /*
     $      Trigger events
     */

    /**
     * Trigger the event onHttpClientCookiesUpdated in all listeners.
     * @param httpClientSession The HttpClientSession who has updated his cookies.
     */
    public static void triggerHttpClientCookiesUpdated(HttpClientSession httpClientSession) {
        httpClientSessionBasics.forEach(listener -> listener.onHttpClientCookiesUpdated(httpClientSession));
    }


}
