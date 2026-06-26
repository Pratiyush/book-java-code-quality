package org.acme.storefront.money;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Realization five: the shared select&rarr;predicate&rarr;report&rarr;gate shape written by hand over the
 * reflection artifact, standing in for a Checkstyle / PMD / SpotBugs custom check.
 *
 * <p>The authoring SDKs for those three analyzers ({@code checkstyle}, {@code pmd-core},
 * {@code spotbugs} plus its archetype) are not SOURCE-PIN rows, so a real plugin for each is described in
 * the chapter prose rather than built here. This inspector keeps the chapter's point runnable with a
 * pinned-free artifact: it <em>selects</em> a type's public members, <em>predicates</em> that a member's
 * type is floating-point money, and <em>reports</em> a {@link MoneyViolation} — the same four steps every
 * analyzer custom rule realizes, just over reflection instead of a source AST or bytecode.
 *
 * <p>It also makes the chapter's honest limit concrete: reflection is its artifact, so it sees declared
 * public members and nothing else. A {@code double} smuggled through a generic erased at run time, or
 * reached only by reflection, is invisible to it — which is exactly why no single rule is a complete
 * fence and Chapter 17's layering matters.
 */
public final class MoneyApiInspector {

    private final MoneyPolicy policy;

    /**
     * @param policy the externalized policy supplying the report severity, never {@code null}
     */
    public MoneyApiInspector(MoneyPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Inspects one type's public API for floating-point money members.
     *
     * @param type the type to inspect, never {@code null}
     * @return the breaches found, empty if the type conforms
     */
    // tag::reflective-inspector[]
    public List<MoneyViolation> inspect(Class<?> type) {
        List<MoneyViolation> violations = new ArrayList<>();
        for (Method method : type.getDeclaredMethods()) {                       // select
            if (isPublicMoneyAccessor(method)) {                                // predicate
                violations.add(report(type.getSimpleName() + "." + method.getName() + "()"));  // report
            }
        }
        return violations;
    }
    // end::reflective-inspector[]

    private static boolean isPublicMoneyAccessor(Method method) {
        return Modifier.isPublic(method.getModifiers())
            && MoneyGuards.isFloatingPointMoney(method.getReturnType());
    }

    /**
     * Inspects a type's declared fields too, so a public {@code double} money field is caught alongside a
     * money accessor. Kept out of the displayed snippet to keep that region within the bounded cap.
     *
     * @param type the type to inspect, never {@code null}
     * @return the field breaches found, empty if the type conforms
     */
    public List<MoneyViolation> inspectFields(Class<?> type) {
        List<MoneyViolation> violations = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers())
                && MoneyGuards.isFloatingPointMoney(field.getType())) {
                violations.add(report(type.getSimpleName() + "." + field.getName()));
            }
        }
        return violations;
    }

    private MoneyViolation report(String where) {
        return new MoneyViolation(where,
            "exposes floating-point money; use the Money value type", policy.severity());
    }
}
