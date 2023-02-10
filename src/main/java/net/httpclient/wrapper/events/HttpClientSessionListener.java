package net.httpclient.wrapper.events;

import net.httpclient.wrapper.session.HttpClientSession;
import net.httpclient.wrapper.session.HttpClientSessionBasic;
import org.jetbrains.annotations.NotNull;

/**
 * This class should be implemented and registered to listen events.
 */
public interface HttpClientSessionListener {

    /**
     * This method should be call when a session has updated his cookies.
     * @param httpClientSession The session who has the cookies updated.
     */
    void onHttpClientCookiesUpdated(@NotNull final HttpClientSession httpClientSession);

}
