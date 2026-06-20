package org.acme.logistics.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.acme.logistics.inventory.InventoryRepository.ReserveOutcome;
import org.junit.jupiter.api.Test;

class InventoryServiceTest {

    private final InventoryService service = new InventoryService(new InMemoryInventoryRepository());

    @Test
    void reservesWhenStockIsAvailable() {
        assertThat(service.reserve("r1", "sku-keyboard", 2)).isEqualTo(ReserveOutcome.RESERVED);
        assertThat(service.available("sku-keyboard")).isEqualTo(3);
    }

    @Test
    void refusesToOversell() {
        assertThat(service.reserve("r1", "sku-monitor", 1)).isEqualTo(ReserveOutcome.RESERVED);
        assertThat(service.reserve("r2", "sku-monitor", 1)).isEqualTo(ReserveOutcome.INSUFFICIENT);
        assertThat(service.available("sku-monitor")).isEqualTo(0);
    }

    @Test
    void reReservingTheSameReferenceDoesNotDoubleDecrement() {
        service.reserve("r1", "sku-keyboard", 2);
        assertThat(service.reserve("r1", "sku-keyboard", 2)).isEqualTo(ReserveOutcome.ALREADY_RESERVED);
        assertThat(service.available("sku-keyboard")).isEqualTo(3);
    }

    @Test
    void releaseReturnsStock() {
        service.reserve("r1", "sku-keyboard", 2);
        assertThat(service.release("r1")).isEqualTo(InventoryRepository.ReleaseOutcome.RELEASED);
        assertThat(service.available("sku-keyboard")).isEqualTo(5);
    }

    @Test
    void concurrentReservationsNeverOversell() throws Exception {
        // 50 threads race for 5 keyboards, each with a distinct reference; exactly 5 may win.
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<ReserveOutcome>> tasks = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                String reference = "rc-" + i;
                tasks.add(() -> service.reserve(reference, "sku-keyboard", 1));
            }
            List<Future<ReserveOutcome>> results = pool.invokeAll(tasks);
            long reserved = 0;
            for (Future<ReserveOutcome> result : results) {
                if (result.get() == ReserveOutcome.RESERVED) {
                    reserved++;
                }
            }
            assertThat(reserved).isEqualTo(5L);
            assertThat(service.available("sku-keyboard")).isEqualTo(0);
        }
    }
}
