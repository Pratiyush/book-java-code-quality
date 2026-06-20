package org.acme.platform.result;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * A sealed success-or-failure value (Part: error handling). A method returns {@code Result} when
 * failure is an ordinary, expected outcome the caller must handle — not an exceptional one. The two
 * cases are exhaustive, so a {@code switch} over them needs no default and the compiler enforces
 * that every caller considers failure.
 *
 * @param <T> the success value type
 * @param <E> the failure value type
 */
public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    /** The success case. */
    record Ok<T, E>(T value) implements Result<T, E> { }

    /** The failure case. */
    record Err<T, E>(E error) implements Result<T, E> { }

    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    default boolean isOk() {
        return this instanceof Ok<T, E>;
    }

    /** Maps the success value, leaving a failure untouched. */
    default <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
        return switch (this) {
            case Ok<T, E> ok -> Result.ok(mapper.apply(ok.value()));
            case Err<T, E> err -> Result.err(err.error());
        };
    }

    /** Chains a fallible step, short-circuiting on the first failure. */
    default <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
        return switch (this) {
            case Ok<T, E> ok -> mapper.apply(ok.value());
            case Err<T, E> err -> Result.err(err.error());
        };
    }

    /** The success value, or the supplied fallback on failure. */
    default T orElse(T fallback) {
        return switch (this) {
            case Ok<T, E> ok -> ok.value();
            case Err<T, E> ignored -> fallback;
        };
    }

    /** The success value, or throws — use only where failure has already been ruled out. */
    default T orElseThrow() {
        return switch (this) {
            case Ok<T, E> ok -> ok.value();
            case Err<T, E> err -> throw new NoSuchElementException("Result was Err: " + err.error());
        };
    }

    default Optional<T> toOptional() {
        return this instanceof Ok<T, E> ok ? Optional.of(ok.value()) : Optional.empty();
    }
}
