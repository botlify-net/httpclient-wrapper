package net.httpclient.wrapper;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HttpClientProxyConfig {

    @NotNull @Getter
    private final String host;

    @Getter
    private final int port;

    @Nullable @Getter
    private final String username;

    @Nullable @Getter
    private final String password;

    private HttpClientProxyConfig(@NotNull final Builder builder) {
        if (builder.host == null || builder.host.isEmpty())
            throw (new IllegalArgumentException("Host cannot be null or empty"));
        if (builder.port <= 0)
            throw (new IllegalArgumentException("Port cannot be 0 or negative"));
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
    }

    public boolean hasValidCredentials() {
        return (username != null && !username.isEmpty() && password != null && !password.isEmpty());
    }

    static class Builder {

        private String host;

        private int port;

        private String username;

        private String password;

        public @NotNull Builder setHostAndPort(@NotNull final String host, final int port) {
            this.host = host;
            this.port = port;
            return (this);
        }

        public @NotNull Builder setUsernameAndPassword(@NotNull final String username, @NotNull final String password) {
            this.username = username;
            this.password = password;
            return (this);
        }

    }

}
