package org.acme.orders;

/**
 * A command port: charges a payment for a placed order.
 *
 * <p>This is the chapter's example of a <em>command</em> collaborator — the side effect (money is
 * charged) is the point of calling it. In a unit test the right double is a <em>mock</em>: the test
 * exercises the unit and then verifies the interaction with
 * {@code verify(gateway).charge(eq(orderId), any())}. Verifying the interaction is behaviour
 * verification, and the chapter is explicit about its cost — it couples the test to how the unit
 * collaborates, so an over-specified version breaks on a refactor that changes nothing observable.
 */
public interface PaymentGateway {

    /**
     * Charges an amount against an order.
     *
     * @param orderId the order being paid for, never {@code null}
     * @param amount  the amount to charge, never {@code null}
     * @throws PaymentDeclinedException if the gateway declines the charge
     */
    void charge(String orderId, Money amount);
}
