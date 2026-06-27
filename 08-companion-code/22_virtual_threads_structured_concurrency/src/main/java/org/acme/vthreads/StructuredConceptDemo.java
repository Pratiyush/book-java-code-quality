package org.acme.vthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The structured-concurrency <em>concept</em> expressed in stable, GA APIs.
 *
 * <p>Structured concurrency treats a group of related subtasks as a single unit of work with a bounded
 * lifetime, so no subtask is leaked and a failure propagates predictably. The dedicated API for it,
 * {@code java.util.concurrent.StructuredTaskScope}, is <em>preview</em> through Java 25 (JEP 453 through
 * JEP 505) and its shape changed across previews — Java 21 opened a scope via constructors with
 * {@code ShutdownOnFailure}/{@code ShutdownOnSuccess}; Java 25 opens it via
 * {@code StructuredTaskScope.open(Joiner...)} static factories. Because that surface has churned every
 * preview, this module depends on none of it and uses {@code --enable-preview} nowhere.
 *
 * <p>What this method shows instead is the same bounded-lifetime <em>idea</em> with the stable
 * {@code newVirtualThreadPerTaskExecutor()}: the try-with-resources block bounds every fork — closing
 * the executor waits for all submitted tasks — and a failed subtask surfaces as an
 * {@link ExecutionException} on {@code Future.get()}, which this method turns into a single failure for
 * the whole unit. The leak-proof structure the preview API makes first-class is approximated here;
 * the chapter teaches the preview API as the direction, and flags it as preview rather than shipping it.
 */
public final class StructuredConceptDemo {

    /**
     * Runs all subtasks as a single unit of work and returns their results, or fails the whole unit if
     * any subtask fails.
     *
     * @param subtasks the subtasks to run together, never {@code null}, no {@code null} element
     * @param <T>      the subtask result type
     * @return the results in submission order, never {@code null}
     * @throws NullPointerException if {@code subtasks} or any element is {@code null}
     * @throws StructuredFailureException if any subtask fails or the unit is interrupted
     */
    public <T> List<T> runAll(List<? extends Callable<T>> subtasks) {
        Objects.requireNonNull(subtasks, "subtasks");
        subtasks.forEach(s -> Objects.requireNonNull(s, "subtask"));
        List<T> results = new ArrayList<>();
        // tag::structured-preview[]
        // StructuredTaskScope is PREVIEW through Java 25 (JEP 453->505); this shows the CONCEPT instead:
        // the try-with-resources block BOUNDS every fork — closing it joins all submitted subtasks.
        try (var scope = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<T>> forks = subtasks.stream().map(scope::submit).toList();
            for (Future<T> fork : forks) {
                results.add(fork.get());          // a failed fork surfaces here as ExecutionException
            }
        } catch (ExecutionException e) {
            throw new StructuredFailureException("subtask-failed", e.getCause());
            // end::structured-preview[]
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StructuredFailureException("interrupted", e);
        }
        return results;
    }
}
