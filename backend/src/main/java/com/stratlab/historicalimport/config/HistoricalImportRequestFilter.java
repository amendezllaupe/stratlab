package com.stratlab.historicalimport.config;

import com.stratlab.common.exception.HistoricalImportAccessDeniedException;
import com.stratlab.historicalimport.service.HistoricalImportAccessGuard;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(prefix = "stratlab.historical-import", name = "enabled", havingValue = "true")
public class HistoricalImportRequestFilter extends OncePerRequestFilter {

    private static final String IMPORT_PATH = "/api/historical-imports/";

    private final HistoricalImportAccessGuard accessGuard;
    private final HistoricalImportProperties properties;

    public HistoricalImportRequestFilter(HistoricalImportAccessGuard accessGuard, HistoricalImportProperties properties) {
        this.accessGuard = accessGuard;
        this.properties = properties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(IMPORT_PATH);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            accessGuard.requireAuthorized(request.getHeader("X-StratLab-Import-Key"));
        } catch (HistoricalImportAccessDeniedException exception) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
            return;
        }

        if (request.getContentLengthLong() > properties.maxPayloadBytes()) {
            writeError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Historical import payload exceeds the configured size limit.");
            return;
        }

        try {
            filterChain.doFilter(new LimitedBodyRequest(request, properties.maxPayloadBytes()), response);
        } catch (PayloadLimitExceededIOException exception) {
            if (!response.isCommitted()) {
                writeError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Historical import payload exceeds the configured size limit.");
            }
        }
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message.replace("\"", "\\\"") + "\"}");
    }

    private static final class LimitedBodyRequest extends HttpServletRequestWrapper {
        private final long maxBytes;

        private LimitedBodyRequest(HttpServletRequest request, long maxBytes) {
            super(request);
            this.maxBytes = maxBytes;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new LimitedServletInputStream(super.getInputStream(), maxBytes);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }

    private static final class LimitedServletInputStream extends ServletInputStream {
        private final ServletInputStream delegate;
        private final long maxBytes;
        private long consumedBytes;

        private LimitedServletInputStream(ServletInputStream delegate, long maxBytes) {
            this.delegate = delegate;
            this.maxBytes = maxBytes;
        }

        @Override
        public int read() throws IOException {
            int value = delegate.read();
            if (value != -1) {
                count(1);
            }
            return value;
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            int read = delegate.read(buffer, offset, length);
            if (read > 0) {
                count(read);
            }
            return read;
        }

        @Override
        public boolean isFinished() {
            return delegate.isFinished();
        }

        @Override
        public boolean isReady() {
            return delegate.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            delegate.setReadListener(readListener);
        }

        private void count(long bytes) throws PayloadLimitExceededIOException {
            consumedBytes += bytes;
            if (consumedBytes > maxBytes) {
                throw new PayloadLimitExceededIOException();
            }
        }
    }

    private static final class PayloadLimitExceededIOException extends IOException {
    }
}
