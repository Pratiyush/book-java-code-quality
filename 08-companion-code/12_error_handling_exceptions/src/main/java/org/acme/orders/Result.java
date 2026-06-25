package org.acme.orders;

import java.util.function.Function;

/**
 * A closed set of outcomes modelled as a value: the typed alternative to throwing (a {@code Result}/
 * {@code Either} shape). A {@code sealed} interface (JEP 409) with {@code record} cases (JEP 395) is
 * deconstructed exhaustively by pattern matching for {@code switch} (JEP 441, GA in Java 21); the compiler
 * rejects a {@code switch} that misses a permitted case, so a new outcome cannot be added without every
 * call site being made to account for it.
 *
 * <p>This is presented as an approach alongside exceptions, not a replacement: it puts every failure in the
 * type, which is precise but verbose, and it interoperates awkwardly with the exception-throwing libraries
 * most code crosses. Its costs are the chapter's "Limitations" section; its strength is that the failure
 * cannot be ignored without the compiler noticing.
 *
 * @param <T> the value carried by a successful outcome
 * @param <E> the value carried by a failed outcome
 */
// tag::result-model[]
public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    /** A successful outcome carrying a value. */
    record Ok<T, E>(T value) implements Result<T, E> { }

    /** A failed outcome carrying an error describing why. */
    record Err<T, E>(E error) implements Result<T, E> { }
    // end::result-model[]

    /**
     * Folds this result into a single value by applying the matching mapper.
     *
     * <p>The {@code switch} is exhaustive over the permitted cases, so adding a third outcome would not
     * compile until this method handled it — the exhaustiveness the typed model buys.
     *
     * @param onOk  applied to the value of a successful outcome
     * @param onErr applied to the error of a failed outcome
     * @param <R>   the folded result type
     * @return the value produced by whichever mapper matched
     */
    default <R> R fold(Function<? super T, ? extends R> onOk, Function<? super E, ? extends R> onErr) {
        return switch (this) {
            case Ok<T, E> ok -> onOk.apply(ok.value());
            case Err<T, E> err -> onErr.apply(err.error());
        };
    }

    /**
     * Returns whether this outcome is a success.
     *
     * @return {@code true} for an {@link Ok}, {@code false} for an {@link Err}
     */
    default boolean isOk() {
        return this instanceof Ok<T, E>;
    }
}
