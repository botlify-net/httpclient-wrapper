package net.httpclient.wrapper.headers;

import lombok.Getter;
import net.httpclient.wrapper.headers.enums.AcceptLanguage;
import net.httpclient.wrapper.headers.enums.HttpHeaders;
import net.httpclient.wrapper.utils.RandomUserAgent;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FakeHeaders {

    @NotNull @Getter
    private final List<Header> headers;

    public FakeHeaders(@NotNull final FakeHeaders.Builder builder) {
        headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.USER_AGENT.getHeaderName(), builder.userAgent));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE.getHeaderName(), getAcceptLanguage(builder.acceptLanguage, builder.secondaryAcceptLanguages)));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING.getHeaderName(), getRandomAcceptEncoding()));
        headers.add(new BasicHeader(HttpHeaders.CACHE_CONTROL.getHeaderName(), getCacheControl()));
        headers.add(new BasicHeader(HttpHeaders.CONNECTION.getHeaderName(), getConnexion()));
        headers.add(new BasicHeader(HttpHeaders.PRAGMA.getHeaderName(), getPragma()));
        headers.add(new BasicHeader(HttpHeaders.SEC_FETCH_DEST.getHeaderName(), getSecFetchDest()));
        headers.add(new BasicHeader(HttpHeaders.SEC_FETCH_MODE.getHeaderName(), getSecFetchMode()));
        headers.add(new BasicHeader(HttpHeaders.SEC_FETCH_SITE.getHeaderName(), getSecFetchSite()));
        // sort list alphabetically
        headers.sort(Comparator.comparing(NameValuePair::getName));
    }

    /*
     $      Private methods
     */

    @NotNull String getAcceptLanguage(@NotNull final AcceptLanguage primaryLanguage,
                                      @Nullable final Map<AcceptLanguage, Float> languages) {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(primaryLanguage.getCode());
        if (languages != null && !languages.isEmpty()) {
            List<Map.Entry<AcceptLanguage, Float>> sortedLanguages = new ArrayList<>(languages.entrySet());
            sortedLanguages.sort((o1, o2) -> (Float.compare(o2.getValue(), o1.getValue())));

            for (final Map.Entry<AcceptLanguage, Float> entry : sortedLanguages) {
                final AcceptLanguage language = entry.getKey();
                final Float weight = entry.getValue();
                strBuilder.append(",").append(language.getCode()).append(";q=").append(weight);
            }
        }

        return (strBuilder.toString());
    }

    private static @NotNull String getRandomAcceptEncoding() {
        return ("gzip, deflate, br");
    }

    private @NotNull String getCacheControl() {
        return ("no-cache");
    }

    private @NotNull String getConnexion() {
        return ("keep-alive");
    }

    public @NotNull String getPragma() {
        return ("no-cache");
    }

    public @NotNull String getSecFetchDest() {
        return ("empty");
    }

    public @NotNull String getSecFetchMode() {
        return ("cors");
    }

    public @NotNull String getSecFetchSite() {
        return ("same-origin");
    }

    static class Builder {

        @Getter @NotNull
        private final AcceptLanguage acceptLanguage;

        @Getter @Nullable
        private Map<AcceptLanguage, Float> secondaryAcceptLanguages = null;

        @Getter @NotNull
        private String userAgent = RandomUserAgent.getRandomUserAgent();

        public Builder(@NotNull final AcceptLanguage acceptLanguage) {
            this.acceptLanguage = acceptLanguage;
        }

        public @NotNull Builder setAcceptSecondaryLanguages(@NotNull final Map<AcceptLanguage, Float> secondaryLanguages) {
            this.secondaryAcceptLanguages = secondaryLanguages;
            return (this);
        }

        public @NotNull Builder setUserAgent(@NotNull final String userAgent) {
            this.userAgent = userAgent;
            return (this);
        }

        /*
         $      Build
         */

        public @NotNull FakeHeaders build() {
            return (new FakeHeaders(this));
        }

    }

}
