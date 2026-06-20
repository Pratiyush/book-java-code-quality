package org.acme.fintech.ledger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class LedgerServiceTest {

    private LedgerService newLedger() {
        return new LedgerService(new InMemoryLedgerRepository(), () -> 0L);
    }

    @Test
    void balanceIsTheSumOfPostedLines() {
        LedgerService ledger = newLedger();
        ledger.post("open", "USD", List.of(
            new PostingLine("acc-a", 100_000L), new PostingLine("acc-house", -100_000L)));
        ledger.post("t1", "USD", List.of(
            new PostingLine("acc-a", -25_000L), new PostingLine("acc-b", 25_000L)));

        assertThat(ledger.balance("acc-a")).isEqualTo(75_000L);
        assertThat(ledger.balance("acc-b")).isEqualTo(25_000L);
    }

    @Test
    void rejectsAnUnbalancedEntry() {
        LedgerService ledger = newLedger();
        assertThatThrownBy(() -> ledger.post("bad", "USD", List.of(
            new PostingLine("acc-a", -10L), new PostingLine("acc-b", 9L))))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("does not balance");
    }

    @Test
    void repeatingAreferenceDoesNotPostTwice() {
        LedgerService ledger = newLedger();
        List<PostingLine> lines = List.of(
            new PostingLine("acc-a", -25_000L), new PostingLine("acc-b", 25_000L));
        ledger.post("transfer-9", "USD", lines);
        ledger.post("transfer-9", "USD", lines);

        assertThat(ledger.balance("acc-a")).isEqualTo(-25_000L);
        assertThat(ledger.balance("acc-b")).isEqualTo(25_000L);
    }
}
