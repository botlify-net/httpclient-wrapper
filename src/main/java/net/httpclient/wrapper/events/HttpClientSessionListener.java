package net.httpclient.wrapper.events;

import net.httpclient.wrapper.session.HttpClientSessionBasic;

/**
 * This class should be implemented and registered to listen events.
 */
public interface HttpClientSessionListener {

    /**
     * This method should be call when a session has updated his cookies.
     * @param httpClientSessionBasic The session who has the cookies updated.
     */
    void onHttpClientCookiesUpdated(HttpClientSessionBasic httpClientSessionBasic);


}
