package net.httpclient.wrapper.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Builder
public class HttpSessionRequest {

    @NotNull @Getter @Setter
    private List<Header> additionalHeaders = new ArrayList<>(0);

    @Nullable @Getter @Setter
    private ContentType contentType;

    @Nullable @Getter @Setter
    private String body;

}
