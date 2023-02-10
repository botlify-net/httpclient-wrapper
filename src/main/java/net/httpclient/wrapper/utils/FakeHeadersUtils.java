package net.httpclient.wrapper.utils;

import org.apache.http.Header;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class FakeHeadersUtils {

    /**
     * This method is deprecated and will be removed in the next version.
     * @return An empty array of Header.
     */
    @Deprecated
    public static @NotNull Header[] getNewFakerHeaders() {
        return new Header[0];
    }

}
