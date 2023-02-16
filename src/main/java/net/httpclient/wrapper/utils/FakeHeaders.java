package net.httpclient.wrapper.utils;

import lombok.Getter;
import net.httpclient.wrapper.enums.AcceptLanguage;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeHeaders {

    private final List<Header> headers = new ArrayList<>();

    public FakeHeaders(@NotNull final FakeHeaders.Builder builder) {
        headers.add(new BasicHeader(HttpHeaders.USER_AGENT, builder.userAgent));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, getAcceptLanguage(builder.acceptLanguage, builder.secondaryAcceptLanguages)));
    }

    /*
     $      Private methods
     */

    @NotNull String getAcceptLanguage(@NotNull final AcceptLanguage primaryLanguage,
                                      @Nullable final Map<AcceptLanguage, Float> languages) {
        final StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(primaryLanguage.getCode());
        if (languages != null && !languages.isEmpty()) {
            for (final Map.Entry<AcceptLanguage, Float> entry : languages.entrySet()) {
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
