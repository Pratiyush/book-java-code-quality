package org.acme.fintech.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.acme.platform.error.ApiException;
import org.junit.jupiter.api.Test;

class TransferServiceTest {

    private final FakeAccounts accounts = new FakeAccounts(Map.of("acc-alice", "USD", "acc-bob", "USD"));
    private final FakeLedger ledger = new FakeLedger(Map.of("acc-alice", 100_000L, "acc-bob", 0L));

    private TransferService newService() {
        return new TransferService(new InMemoryTransferRepository(), accounts, ledger);
    }

    @Test
    void movesFundsAndRecordsTheTransfer() {
        TransferService service = newService();
        TransferService.TransferResult result = service.transfer(
            new TransferRequest("ref-1", "acc-alice", "acc-bob", 25_000L, "USD"));

        assertThat(result.replayed()).isFalse();
        assertThat(ledger.balanceOf("acc-alice")).isEqualTo(75_000L);
        assertThat(ledger.balanceOf("acc-bob")).isEqualTo(25_000L);
    }

    @Test
    void replaysOnTheSameReferenceWithoutPostingAgain() {
        TransferService service = newService();
        TransferRequest request = new TransferRequest("ref-2", "acc-alice", "acc-bob", 10_000L, "USD");
        service.transfer(request);
        TransferService.TransferResult replay = service.transfer(request);

        assertThat(replay.replayed()).isTrue();
        assertThat(ledger.posts.get()).isEqualTo(1);
        assertThat(ledger.balanceOf("acc-alice")).isEqualTo(90_000L);
    }

    @Test
    void rejectsAnUnknownAccount() {
        TransferService service = newService();
        assertThatThrownBy(() -> service.transfer(
            new TransferRequest("ref-3", "acc-alice", "acc-ghost", 1_000L, "USD")))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(404));
    }

    @Test
    void rejectsAcurrencyMismatch() {
        TransferService service = newService();
        assertThatThrownBy(() -> service.transfer(
            new TransferRequest("ref-4", "acc-alice", "acc-bob", 1_000L, "EUR")))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(409));
    }

    @Test
    void rejectsAnOverdraftWith402() {
        TransferService service = newService();
        assertThatThrownBy(() -> service.transfer(
            new TransferRequest("ref-5", "acc-alice", "acc-bob", 1_000_000L, "USD")))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(402));
    }

    private static final class FakeAccounts implements AccountPort {
        private final Map<String, String> currencyById;

        FakeAccounts(Map<String, String> currencyById) {
            this.currencyById = Map.copyOf(currencyById);
        }

        @Override
        public Optional<AccountInfo> lookup(String accountId) {
            return Optional.ofNullable(currencyById.get(accountId))
                .map(currency -> new AccountInfo(accountId, currency));
        }
    }

    private static final class FakeLedger implements LedgerPort {
        private final ConcurrentHashMap<String, Long> balances = new ConcurrentHashMap<>();
        private final AtomicInteger posts = new AtomicInteger();

        FakeLedger(Map<String, Long> opening) {
            balances.putAll(opening);
        }

        @Override
        public long balanceOf(String accountId) {
            return balances.getOrDefault(accountId, 0L);
        }

        @Override
        public void postTransfer(String reference, String fromAccount, String toAccount, long amountMinor,
                                 String currency) {
            posts.incrementAndGet();
            balances.merge(fromAccount, -amountMinor, Long::sum);
            balances.merge(toAccount, amountMinor, Long::sum);
        }
    }
}
