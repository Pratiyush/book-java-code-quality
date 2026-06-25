package org.acme.contracts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.ArrayList;
import java.util.List;
import org.acme.contracts.TransferBatch.TransferInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Exercises the contract surfaces the chapter describes: the happy path, the fail-fast guards
 * (null, range, negative, insufficient funds, unknown account), the empty-{@code Optional} lookup
 * contract, and the defensive-copy isolation. Each test asserts the contract holds at runtime; the
 * {@code -Pquality} build asserts the same kinds of mistake are caught by the analyzers.
 */
class MoneyTransferServiceTest {

    private AccountRepository repository;
    private MoneyTransferService service;

    @BeforeEach
    void seed() {
        repository = new InMemoryAccountRepository();
        repository.save(new Account("alice", new Money(10_000L, "USD")));
        repository.save(new Account("bob", new Money(2_000L, "USD")));
        service = new MoneyTransferService(repository);
    }

    @Test
    void movesMoneyBetweenAccountsOnTheHappyPath() {
        TransferReceipt receipt = service.transfer("alice", "bob", 2_500L, 0);

        assertThat(receipt.amount().minorUnits()).isEqualTo(2_500L);
        assertThat(repository.findById("alice")).get()
            .extracting(a -> a.balance().minorUnits()).isEqualTo(7_500L);
        assertThat(repository.findById("bob")).get()
            .extracting(a -> a.balance().minorUnits()).isEqualTo(4_500L);
    }

    @Test
    void transferRejectsNullAccountFailFast() {
        assertThatNullPointerException()
            .isThrownBy(() -> service.transfer(null, "bob", 100L, 0))
            .withMessageContaining("fromId");
    }

    @Test
    void transferRejectsOutOfRangeAttemptWithCheckIndex() {
        assertThatThrownBy(() -> service.transfer("alice", "bob", 100L, 4))
            .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void transferRejectsNegativeAmountAndCountsIt() {
        assertThatThrownBy(() -> service.transfer("alice", "bob", -1L, 0))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(service.rejectedByContractCount()).isEqualTo(1L);
    }

    @Test
    void transferRejectsInsufficientFundsWithTypedError() {
        TransferRejectedException ex = catchThrowableOfType(
            TransferRejectedException.class, () -> service.transfer("bob", "alice", 9_999L, 0));
        assertThat(ex.code()).isEqualTo("insufficient-funds");
    }

    @Test
    void transferRejectsUnknownAccountWithTypedError() {
        TransferRejectedException ex = catchThrowableOfType(
            TransferRejectedException.class, () -> service.transfer("ghost", "alice", 1L, 0));
        assertThat(ex.code()).isEqualTo("account-unknown");
    }

    @Test
    void lookupReturnsEmptyOptionalWhenAbsent() {
        assertThat(repository.findById("nobody")).isEmpty();
    }

    @Test
    void backoffOptsOutOfNullMarkedForUnknownAttempt() {
        assertThat(service.backoffFor(1)).isEqualTo(100L);
        assertThat(service.backoffFor(99)).isNull();
    }

    @Test
    void readinessProbeReportsReady() {
        assertThat(service.isReady()).isTrue();
    }

    @Test
    void availableBalanceReadsThroughThePort() {
        assertThat(service.availableBalance("alice").minorUnits()).isEqualTo(10_000L);
        assertThatThrownBy(() -> service.availableBalance("ghost"))
            .isInstanceOf(TransferRejectedException.class);
    }

    @Test
    void defensiveCopyIsolatesTheBatchFromCallerMutation() {
        List<TransferInstruction> input = new ArrayList<>();
        input.add(new TransferInstruction("alice", "bob", 100L));
        TransferBatch batch = new TransferBatch(input);

        input.clear();                      // mutate the caller's list after construction
        batch.instructions().clear();       // mutate the returned copy

        assertThat(batch.instructions()).hasSize(1);
    }
}
