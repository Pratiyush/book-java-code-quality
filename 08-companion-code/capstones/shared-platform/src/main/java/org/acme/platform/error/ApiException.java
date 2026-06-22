package org.acme.platform.error;

/**
 * A failure a handler raises to return a specific HTTP problem (Part: error handling). It carries a
 * fully-formed {@link ProblemDetails}, so the HTTP layer maps the failure to a status and an RFC
 * 7807 body without the handler touching the response stream. Use it for expected, client-visible
 * failures (not found, conflict, validation) — not for bugs, which surface as a generic 500.
 */
public final class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient ProblemDetails problem;

    public ApiException(ProblemDetails problem) {
        super(problem.title() + ": " + problem.detail());
        this.problem = problem;
    }

    public static ApiException notFound(String type, String detail) {
        return new ApiException(ProblemDetails.of(404, type, "Not Found", detail));
    }

    public static ApiException conflict(String type, String detail) {
        return new ApiException(ProblemDetails.of(409, type, "Conflict", detail));
    }

    public static ApiException badRequest(String type, String detail) {
        return new ApiException(ProblemDetails.of(400, type, "Bad Request", detail));
    }

    public static ApiException unprocessable(String type, String detail) {
        return new ApiException(ProblemDetails.of(422, type, "Unprocessable Entity", detail));
    }

    public ProblemDetails problem() {
        return problem;
    }
}
