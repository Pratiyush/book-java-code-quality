package org.acme.platform;

import static org.assertj.core.api.Assertions.assertThat;

import org.acme.platform.result.Result;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void mapsOnlyTheSuccessCase() {
        Result<Integer, String> ok = Result.ok(21);
        Result<Integer, String> err = Result.err("nope");
        assertThat(ok.map(n -> n * 2).orElse(-1)).isEqualTo(42);
        assertThat(err.map(n -> n * 2).orElse(-1)).isEqualTo(-1);
    }

    @Test
    void flatMapShortCircuitsOnFirstFailure() {
        Result<Integer, String> result = Result.<Integer, String>ok(10)
            .<Integer>flatMap(n -> Result.err("boom"))
            .<Integer>flatMap(n -> Result.ok(n + 1));
        assertThat(result.isOk()).isFalse();
        assertThat(result).isEqualTo(Result.err("boom"));
    }

    @Test
    void convertsToOptional() {
        assertThat(Result.ok("v").toOptional()).contains("v");
        assertThat(Result.err("e").toOptional()).isEmpty();
    }
}
