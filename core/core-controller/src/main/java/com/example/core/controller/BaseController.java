package com.example.core.controller;

import com.example.core.base.response.ControllerResponse;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Enterprise-grade base controller utilities.
 * - Success responses use ControllerResponse<T>.
 * - Error responses recommend ProblemDetail (RFC7807). Prefer handling via @ControllerAdvice.
 * - Proper HTTP semantics for 201/204.
 * - Async helpers use a managed Executor.
 * - Pagination returns a stable API DTO, not Spring Data Page directly.
 */
public abstract class BaseController {

    private final Clock clock;
    private final Executor executor;

    /**
     * Preferred constructor: inject a managed Clock and Executor (e.g. TaskExecutor).
     */
    protected BaseController(Clock clock, Executor executor) {
        this.clock = Objects.requireNonNullElse(clock, Clock.systemUTC());
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
    }

    /**
     * Fallback: if you cannot inject yet (e.g., tests), uses system UTC and common pool.
     */
    protected BaseController() {
        this.clock = Clock.systemUTC();
        this.executor = CompletableFuture.delayedExecutor(0, java.util.concurrent.TimeUnit.MILLISECONDS);
    }


    /**
     * 200 Success with body.
     */
    protected <T> ResponseEntity<ControllerResponse<T>> ok(T data, String message) {
        return buildResponse(HttpStatus.OK, message, data, null);
    }

    /**
     * 200 Success with body.
     */
    protected <T> ResponseEntity<ControllerResponse<T>> updated(T data, String message) {
        return buildResponse(HttpStatus.OK, message, data, null);
    }

    /**
     * 200 Success with body and headers.
     */
    protected <T> ResponseEntity<ControllerResponse<T>> ok(T data, String message, HttpHeaders headers) {
        return buildResponse(HttpStatus.OK, message, data, headers);
    }

    /**
     * 201 Created with Location header and body.
     */
    protected <T> ResponseEntity<ControllerResponse<T>> created(URI location, T data, String message) {
        HttpHeaders headers = new HttpHeaders();
        if (location != null) headers.setLocation(location);
        return buildResponse(HttpStatus.CREATED, message, data, headers);
    }


    /**
     * 204 No Content – no "body" per RFC. Use for deletes or idempotent operations.
     */
    protected ResponseEntity<ControllerResponse<Void>> noContent() {
        return buildResponse(HttpStatus.NO_CONTENT, null, null, null);
    }

    /**
     * 204 No Content – no "body" with a message in headers.
     */
    protected ResponseEntity<ControllerResponse<Void>> noContent(String message) {
        HttpHeaders headers = new HttpHeaders();
        if (message != null && !message.isBlank()) {
            headers.add("X-Message", message);
        }
        return buildResponse(HttpStatus.NO_CONTENT, message, null, headers);
    }

    /**
     * 200 Success with paginated body.
     */
    protected <T> ResponseEntity<ControllerResponse<PageEnvelope<T>>> paginated(Page<T> page, String message) {
        PageEnvelope<T> body = PageEnvelope.from(page);
        return buildResponse(HttpStatus.OK, message, body, null);
    }


    /**
     * Build RFC7807 ProblemDetail. Prefer to centralize in an "@ExceptionHandler".
     */
    protected ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail != null ? detail : status.getReasonPhrase());
        if (title != null && !title.isBlank()) {
            pd.setTitle(title);
        }
        pd.setProperty("timestamp", Instant.now(clock).toString());
        return ResponseEntity.status(status).body(pd);
    }

    /**
     * 400 Bad Request with ProblemDetail body.
     */
    protected ResponseEntity<ProblemDetail> badRequest(String detail) {
        return problem(HttpStatus.BAD_REQUEST, "Bad Request", detail);
    }

    /**
     * 401 Unauthorized with ProblemDetail body.
     */
    protected ResponseEntity<ProblemDetail> unauthorized(String detail) {
        return problem(HttpStatus.UNAUTHORIZED, "Unauthorized", detail);
    }

    /**
     * 403 Forbidden with ProblemDetail body.
     */
    protected ResponseEntity<ProblemDetail> forbidden(String detail) {
        return problem(HttpStatus.FORBIDDEN, "Forbidden", detail);
    }

    /**
     * 404 Not Found with ProblemDetail body.
     */
    protected ResponseEntity<ProblemDetail> notFound(String detail) {
        return problem(HttpStatus.NOT_FOUND, "Not Found", detail);
    }

    /**
     * 409 Conflict with ProblemDetail body.
     */
    protected ResponseEntity<ProblemDetail> conflict(String detail) {
        return problem(HttpStatus.CONFLICT, "Conflict", detail);
    }

    /**
     * 500 Internal Server Error with ProblemDetail body.
     */
    protected ResponseEntity<ProblemDetail> internalError(String detail) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", detail);
    }


    /**
     * 200 OK async with Supplier.
     */
    protected <T> CompletableFuture<ResponseEntity<ControllerResponse<T>>> okAsync(Supplier<T> supplier, String message) {
        return CompletableFuture.supplyAsync(() -> ok(supplier.get(), message), executor);
    }

    /**
     * 201 Created async with Location.
     */
    protected <T> CompletableFuture<ResponseEntity<ControllerResponse<T>>> createdAsync(URI location, Supplier<T> supplier, String message) {
        return CompletableFuture.supplyAsync(() -> created(location, supplier.get(), message), executor);
    }

    /**
     * Run-and-forget style async that ends in 204 No Content.
     */
    protected CompletableFuture<ResponseEntity<ControllerResponse<Void>>> noContentAsync(Runnable runnable, String message) {
        return CompletableFuture.runAsync(runnable, executor)
                .thenApply(v -> noContent(message));
    }

    /**
     * If you already have a CompletionStage (e.g., from service layer), map it to an OK envelope.
     */
    protected <T> CompletionStage<ResponseEntity<ControllerResponse<T>>> fromStage(CompletionStage<T> stage, String message) {
        return stage.thenApply(result -> ok(result, message));
    }

    /**
     * Internal helper to build standardized response envelopes.
     */
    private <T> ResponseEntity<ControllerResponse<T>> buildResponse(
            HttpStatus status, String message, T data, HttpHeaders headers) {

        ControllerResponse<T> response = ControllerResponse.<T>builder()
                .success(true)
                .code(status.value())
                .message(message)
                .timestamp(Instant.now(clock))
                .data(data)
                .build();

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status);
        if (headers != null && !headers.isEmpty()) {
            builder.headers(headers);
        }
        return builder.body(response);
    }

    /**
     * A stable pagination contract for API responses.
     */
    @Getter
    public static final class PageEnvelope<T> {
        private final List<T> content;
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final boolean first;
        private final boolean last;
        private final boolean hasNext;
        private final boolean hasPrevious;
        private final String sort;

        private PageEnvelope(List<T> content,
                             int page,
                             int size,
                             long totalElements,
                             int totalPages,
                             boolean first,
                             boolean last,
                             boolean hasNext,
                             boolean hasPrevious,
                             String sort) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.first = first;
            this.last = last;
            this.hasNext = hasNext;
            this.hasPrevious = hasPrevious;
            this.sort = sort;
        }

        public static <T> PageEnvelope<T> from(Page<T> page) {
            String sort = page.getSort().isSorted() ? page.getSort().toString() : null;
            return new PageEnvelope<>(
                    page.getContent(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.isFirst(),
                    page.isLast(),
                    page.hasNext(),
                    page.hasPrevious(),
                    sort
            );
        }

    }
}
