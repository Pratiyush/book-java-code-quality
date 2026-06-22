package org.acme.fintech.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.acme.platform.error.ApiException;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private final AccountService service = new AccountService(new InMemoryAccountRepository());

    @Test
    void findsAseededAccount() {
        assertThat(service.require("acc-alice").owner()).isEqualTo("Alice");
    }

    @Test
    void opensAnAccountWithAgeneratedId() {
        Account opened = service.open("Carol", "EUR");
        assertThat(opened.id()).startsWith("acc_");
        assertThat(service.require(opened.id()).currency()).isEqualTo("EUR");
    }

    @Test
    void rejectsUnknownAccountAsNotFound() {
        assertThatThrownBy(() -> service.require("acc-nope"))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(404));
    }
}
