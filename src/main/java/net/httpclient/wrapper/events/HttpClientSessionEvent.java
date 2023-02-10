package net.httpclient.wrapper.events;

import net.httpclient.wrapper.session.HttpClientSession;
import net.httpclient.wrapper.session.HttpClientSessionBasic;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This static class manage the HttpClientSessionBasic events.
 */
public class HttpClientSessionEvent {

    private static final List<HttpClientSessionListener> httpClientSessionBasics = new ArrayList<>();

    /*
     $      Add and remove listeners
     */

    /**
     * Add a listener to the list of listeners.
     * @param listener The listener to add.
     */
    public static void addHttpClientSessionListener(@NotNull final HttpClientSessionListener listener) {
        synchronized (httpClientSessionBasics) {
            httpClientSessionBasics.add(listener);
        }
    }

    /**
     * Remove a listener from the list of listeners.
     * @param listener The listener to remove.
     */
    public static void removeHttpClientSessionListener(@NotNull final HttpClientSessionListener listener) {
        synchronized (httpClientSessionBasics) {
            httpClientSessionBasics.remove(listener);
        }
    }

    /*
     $      Trigger events
     */

    /**
     * Trigger the event onHttpClientCookiesUpdated in all listeners.
     * @param httpClientSession The HttpClientSessionBasic who has updated his cookies.
     */
    public static void triggerHttpClientCookiesUpdated(@NotNull final HttpClientSession httpClientSession) {
        synchronized (httpClientSessionBasics) {
            httpClientSessionBasics.forEach(listener -> listener.onHttpClientCookiesUpdated(httpClientSession));
        }
    }

    public static int countListeners() {
        synchronized (httpClientSessionBasics) {
            return (httpClientSessionBasics.size());
        }
    }

}
