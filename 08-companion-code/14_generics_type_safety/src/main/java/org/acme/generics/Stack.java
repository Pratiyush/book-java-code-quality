package org.acme.generics;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A type-safe generic stack, and the chapter's worked PECS example (Effective Java Items 29 and 31).
 *
 * <p>The element type {@code E} is parametric, so the compiler inserts and verifies every cast a
 * hand-written {@code Object} stack would leave to convention. The two bulk operations carry the
 * variance lesson — Producer-Extends, Consumer-Super:
 *
 * <ul>
 *   <li>{@code pushAll} takes an {@code Iterable<? extends E>}: the source <em>produces</em> elements
 *       to read in, so an upper-bounded wildcard lets a {@code Stack<Number>} accept a
 *       {@code List<Integer>}.</li>
 *   <li>{@code popAll} takes a {@code Collection<? super E>}: the destination <em>consumes</em> the
 *       elements written out, so a lower-bounded wildcard lets a {@code Stack<Integer>} drain into a
 *       {@code List<Object>}.</li>
 * </ul>
 *
 * <p>Erasure forbids {@code new E[]}, so the backing array is created once as an {@code Object[]} and
 * cast to {@code E[]}. That is the single spot the compiler cannot prove safe; it is discharged with a
 * narrowest-scope {@link SuppressWarnings} and a comment that proves the array is private and only
 * ever holds {@code E} (Item 27), rather than suppressing across the class. Reads come back as {@code
 * E} without a runtime check because nothing of another type was ever stored.
 *
 * <p>Like the {@code java.util} collections, this stack is <em>not</em> thread-safe: a single thread
 * owns an instance, so the size and push counters are plain fields, not atomics. Sharing one across
 * threads would need external synchronization.
 *
 * @param <E> the element type this stack holds
 */
public final class Stack<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private E[] elements;
    private int size;

    /** Observability: how many elements have been pushed since construction (illustrative, Ch 45). */
    private long pushedTotal;

    /**
     * Creates an empty stack.
     *
     * @implSpec Allocates the backing array eagerly at the default capacity; it grows as needed.
     */
    public Stack() {
        // tag::suppress-justified[]
        // The array is created as Object[] (erasure forbids new E[]) and cast to E[]. This is the one
        // unprovable spot, so the suppression is on this single local — not the method or the class.
        // SAFE: `elements` is private, never escapes this instance, and push(E) is the only write, so
        // every slot holds an E; the unchecked reads in pop()/peek() can never see another type.
        @SuppressWarnings("unchecked")
        E[] initial = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
        this.elements = initial;
        // end::suppress-justified[]
    }

    /**
     * Pushes one element onto the top of the stack.
     *
     * @param element the element to push, never {@code null}
     * @throws NullPointerException if {@code element} is {@code null}
     */
    public void push(E element) {
        Objects.requireNonNull(element, "element");
        ensureCapacity();
        elements[size++] = element;
        pushedTotal++;
    }

    /**
     * Pushes every element of a producer onto this stack.
     *
     * @param src the elements to push, never {@code null}; an {@code E} producer (Item 31)
     * @throws NullPointerException if {@code src} or any element is {@code null}
     */
    // tag::pecs-pushall[]
    public void pushAll(Iterable<? extends E> src) {        // PRODUCER → ? extends E
        Objects.requireNonNull(src, "src");
        for (E element : src) {                              // read each E out of the producer
            push(element);
        }
    }
    // end::pecs-pushall[]

    /**
     * Pops every element of this stack into a consumer, leaving the stack empty.
     *
     * @param dst the destination to drain into, never {@code null}; an {@code E} consumer (Item 31)
     * @throws NullPointerException if {@code dst} is {@code null}
     */
    // tag::pecs-popall[]
    public void popAll(Collection<? super E> dst) {         // CONSUMER → ? super E
        Objects.requireNonNull(dst, "dst");
        while (!isEmpty()) {                                 // write each E into the consumer
            dst.add(pop());
        }
    }
    // end::pecs-popall[]

    /**
     * Removes and returns the top element.
     *
     * @return the element that was on top, never {@code null}
     * @throws NoSuchElementException if the stack is empty
     */
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("pop from empty stack");
        }
        E result = elements[--size];
        elements[size] = null;          // null out the popped slot so the stack holds no stale reference
        return result;
    }

    /**
     * Returns the top element without removing it.
     *
     * @return the element on top, never {@code null}
     * @throws NoSuchElementException if the stack is empty
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("peek into empty stack");
        }
        return elements[size - 1];
    }

    /**
     * Reports whether the stack holds no elements.
     *
     * @return {@code true} when the stack is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements currently on the stack.
     *
     * @return the element count, never negative
     */
    public int size() {
        return size;
    }

    /**
     * Health/observability surface: the running count of elements pushed since construction.
     *
     * @return the total number of pushes, never negative
     */
    public long pushedTotalCount() {
        return pushedTotal;
    }

    /** Doubles the backing array when it is full, preserving every stored element. */
    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
