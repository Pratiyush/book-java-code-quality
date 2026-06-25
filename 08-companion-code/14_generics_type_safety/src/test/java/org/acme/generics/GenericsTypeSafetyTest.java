package org.acme.generics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's type-safety claims as runnable assertions: PECS proven by both compilation
 * and behaviour (a {@code List<Integer>} pushed into a {@code Stack<Number>}; a {@code Stack<Integer>}
 * drained into a {@code List<Object>}), the round-trip that shows the justified {@code
 * @SuppressWarnings} is in fact safe, the fail-fast guards, and — the load-bearing failure path — the
 * heap pollution the unsafe varargs method causes, contrasted with the genuinely safe one.
 *
 * <p>That each PECS call compiles is itself part of the proof: the wildcards are what let these
 * argument types bind at all. The {@code -Pquality} build asserts the same kinds of mistake are caught
 * by the analyzers; the compiler's {@code -Xlint} output asserts no unchecked use slipped into the
 * production code.
 */
class GenericsTypeSafetyTest {

    @Test
    void pushAllAcceptsAnIntegerProducerIntoANumberStack() {
        // PECS producer-extends: a Stack<Number> accepts a List<Integer> because pushAll takes
        // Iterable<? extends Number>. Without the wildcard this would not compile.
        Stack<Number> numbers = new Stack<>();
        numbers.pushAll(List.of(1, 2, 3));
        numbers.pushAll(List.of(4.5, 6.5));

        assertThat(numbers.size()).isEqualTo(5);
        assertThat(numbers.peek()).isEqualTo(6.5);
    }

    @Test
    void popAllDrainsAnIntegerStackIntoAnObjectConsumer() {
        // PECS consumer-super: a Stack<Integer> drains into a List<Object> because popAll takes
        // Collection<? super Integer>. Without the wildcard this would not compile.
        Stack<Integer> ints = new Stack<>();
        ints.pushAll(List.of(10, 20, 30));

        List<Object> sink = new ArrayList<>();
        ints.popAll(sink);

        assertThat(ints.isEmpty()).isTrue();
        assertThat(sink).containsExactly(30, 20, 10);   // popped top-first
    }

    @Test
    void pushAndPopRoundTripStayTypeSafe() {
        // The justified @SuppressWarnings holds: every value read back is the E that was written,
        // with no runtime cast failure, across a grow of the backing array.
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < 50; i++) {
            stack.push("item-" + i);
        }

        assertThat(stack.size()).isEqualTo(50);
        assertThat(stack.pop()).isEqualTo("item-49");
        assertThat(stack.pushedTotalCount()).isEqualTo(50L);
    }

    @Test
    void pushRejectsNullFailFast() {
        Stack<String> stack = new Stack<>();
        assertThatNullPointerException()
            .isThrownBy(() -> stack.push(null))
            .withMessageContaining("element");
    }

    @Test
    void popAndPeekOnEmptyStackThrow() {
        Stack<String> stack = new Stack<>();
        assertThatThrownBy(stack::pop).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(stack::peek).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void safeVarargsFlattenReadsEveryInput() {
        List<Integer> flat = VarargsHeapPollution.flatten(List.of(1, 2), List.of(3), List.of(4, 5));
        assertThat(flat).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void unsafeVarargsPoisonsItsArrayAndThrowsClassCastException() {
        // The failure path: dangerous() writes an Integer list into a String-list slot (heap
        // pollution), so reading it back as a String throws — proof that @SafeVarargs would lie here.
        assertThatThrownBy(() -> VarargsHeapPollution.dangerous(List.of("a"), List.of("b")))
            .isInstanceOf(ClassCastException.class);
    }
}
