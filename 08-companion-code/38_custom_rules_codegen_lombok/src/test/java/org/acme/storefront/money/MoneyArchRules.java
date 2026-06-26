package org.acme.storefront.money;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * Realization four: the house invariant as a custom ArchUnit rule (SOURCE-PIN.md §2:
 * com.tngtech.archunit 1.4.2), expressed through the two extension points ArchUnit documents for a
 * fully custom architectural law — a {@link DescribedPredicate} (the filter) and an
 * {@link ArchCondition} (the constraint).
 *
 * <p>The artifact here is the imported class graph, so this realization sees type-level structure: which
 * domain types <em>expose</em> floating-point money on their public API. That is its strongest case (an
 * architectural "no domain type may do X" is awkward to phrase against source tokens or bytecode) and its
 * limit (it reasons about the public surface of compiled types, not statement-level logic inside a method
 * body). The constants are combined in a test with {@code classes().that(IN_DOMAIN).should(NOT_EXPOSE...)}
 * and run as an ordinary JUnit test, where a violation throws {@code AssertionError} like any failed
 * assertion.
 */
public final class MoneyArchRules {

    private MoneyArchRules() {
    }

    /** Selects the domain types the architectural rule applies to. */
    // tag::archunit-predicate[]
    public static final DescribedPredicate<JavaClass> IN_DOMAIN =
        new DescribedPredicate<>("reside in a domain package") {
            @Override
            public boolean test(JavaClass clazz) {
                String name = clazz.getPackageName();
                return name.startsWith("org.acme.storefront.money");
            }
        };
    // end::archunit-predicate[]

    /** Asserts a domain type exposes no floating-point money on its public methods. */
    public static final ArchCondition<JavaClass> NOT_EXPOSE_FLOATING_POINT_MONEY =
        new ArchCondition<>("not expose floating-point money on the public API") {
            @Override
            // tag::archunit-condition[]
            public void check(JavaClass clazz, ConditionEvents events) {
                for (JavaMethod method : clazz.getMethods()) {
                    if (method.getModifiers().contains(JavaModifier.PUBLIC)
                        && isFloatingPoint(method.getRawReturnType())) {
                        events.add(SimpleConditionEvent.violated(method,
                            method.getFullName() + " returns floating-point money"));
                    }
                }
            }
            // end::archunit-condition[]
        };

    private static boolean isFloatingPoint(JavaClass type) {
        return type.isEquivalentTo(double.class)
            || type.isEquivalentTo(float.class)
            || type.isEquivalentTo(Double.class)
            || type.isEquivalentTo(Float.class);
    }
}
