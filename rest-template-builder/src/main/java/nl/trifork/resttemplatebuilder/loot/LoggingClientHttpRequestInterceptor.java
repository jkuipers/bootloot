package nl.trifork.resttemplatebuilder.loot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Allows logging outgoing requests and the corresponding responses.
 * Requires the use of a {@link org.springframework.http.client.BufferingClientHttpRequestFactory} to log
 * the body of received responses.
 */
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    protected Logger requestLogger;
    protected Logger responseLogger;

    /**
     * Creates an interceptor with request logger {@code spring.web.client.MessageTracing.sent}
     * and response logger {@code spring.web.client.MessageTracing.received}, loosely following
     * Spring-WS logger naming conventions.
     */
    public LoggingClientHttpRequestInterceptor() {
        this(LoggerFactory.getLogger("spring.web.client.MessageTracing.sent"),
             LoggerFactory.getLogger("spring.web.client.MessageTracing.received"));
    }

    /**
     * @param requestLogger the logger used to log sent requests
     * @param responseLogger the logger used to log received responses
     */
    public LoggingClientHttpRequestInterceptor(Logger requestLogger, Logger responseLogger) {
        this.requestLogger = requestLogger;
        this.responseLogger = responseLogger;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(request, response);
        return response;
    }

    protected void logRequest(HttpRequest request, byte[] body) {
        if (requestLogger.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder("Sending ").append(request.getMethod()).append(" request to ").append(request.getURI());
            if (body.length > 0 && hasTextBody(request.getHeaders())) {
                String bodyText = new String(body, determineCharset(request.getHeaders()));
                builder.append(": [").append(bodyText).append("]");
            }
            requestLogger.debug(builder.toString());
        }
    }

    protected void logResponse(HttpRequest request, ClientHttpResponse response) {
        if (responseLogger.isDebugEnabled()) {
            try {
                StringBuilder builder = new StringBuilder("Received \"")
                        .append(response.getRawStatusCode()).append(" ").append(response.getStatusText()).append("\" response for ")
                        .append(request.getMethod()).append(" request to ").append(request.getURI());
                HttpHeaders responseHeaders = response.getHeaders();
                long contentLength = responseHeaders.getContentLength();
                if (contentLength != 0) {
                    if (hasTextBody(responseHeaders) && !isMockedResponse(response)) {
                        String bodyText = StreamUtils.copyToString(response.getBody(), determineCharset(responseHeaders));
                        builder.append(": [").append(bodyText).append("]");
                    } else {
                        if (contentLength == -1) {
                            builder.append(" with content of unknown length");
                        } else {
                            builder.append(" with content of length ").append(contentLength);
                        }
                        MediaType contentType = responseHeaders.getContentType();
                        if (contentType != null) {
                            builder.append(" and content type ").append(contentType);
                        } else {
                            builder.append(" and unknown content type");
                        }
                    }
                }
                responseLogger.debug(builder.toString());
            } catch (IOException e) {
                responseLogger.warn("Failed to log response for {} request to {}", request.getMethod(), request.getURI(), e);
            }
        }
    }

    protected boolean hasTextBody(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            if ("text".equals(contentType.getType())) {
                return true;
            }
            String subtype = contentType.getSubtype();
            if (subtype != null) {
                return "xml".equals(subtype) || "json".equals(subtype) ||
                        subtype.endsWith("+xml") || subtype.endsWith("+json");
            }
        }
        return false;
    }

    protected Charset determineCharset(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            try {
                Charset charSet = contentType.getCharset();
                if (charSet != null) {
                    return charSet;
                }
            } catch (UnsupportedCharsetException e) {
                // ignore
            }
        }
        return StandardCharsets.UTF_8;
    }

    private boolean isMockedResponse(ClientHttpResponse response) {
        return "MockClientHttpResponse".equals(response.getClass().getSimpleName());
    }

}