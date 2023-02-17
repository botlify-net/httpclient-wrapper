package net.httpclient.wrapper.headers;

import lombok.Getter;
import net.httpclient.wrapper.headers.enums.HttpHeaders;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FakeHeadersRequest {

    @Getter @NotNull
    private final List<Header> headers;

    public FakeHeadersRequest(@NotNull final Builder builder) {
        headers = new ArrayList<>(builder.fakeHeaders.getHeaders());
        if (builder.referer != null)
            headers.add(new BasicHeader(HttpHeaders.REFERER.getHeaderName(), builder.referer));
        if (builder.host != null)
            headers.add(new BasicHeader(HttpHeaders.HOST.getHeaderName(), builder.host));
        if (builder.accept != null)
            headers.add(new BasicHeader(HttpHeaders.ACCEPT.getHeaderName(), builder.accept));
        if (builder.contentType != null)
            headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE.getHeaderName(), builder.contentType.toString()));
        // sort list alphabetically
        headers.sort(Comparator.comparing(NameValuePair::getName));
    }

    static class Builder {

        private final FakeHeaders fakeHeaders;

        private String referer = null;

        private String host = null;

        @Deprecated
        private String accept = null;

        private ContentType contentType = null;

        public Builder(@NotNull final FakeHeaders fakeHeaders) {
            this.fakeHeaders = fakeHeaders;
        }

        public @NotNull Builder setReferer(@NotNull final String referer) {
            this.referer = referer;
            return (this);
        }

        public @NotNull Builder setHost(@NotNull final String host) {
            this.host = host;
            return (this);
        }

        @Deprecated
        public @NotNull Builder setAccept(@NotNull final String accept) {
            this.accept = accept;
            return (this);
        }

        public @NotNull Builder setContentType(@NotNull final ContentType contentType) {
            this.contentType = contentType;
            return (this);
        }

        public @NotNull FakeHeadersRequest build() {
            return (new FakeHeadersRequest(this));
        }

    }

}
