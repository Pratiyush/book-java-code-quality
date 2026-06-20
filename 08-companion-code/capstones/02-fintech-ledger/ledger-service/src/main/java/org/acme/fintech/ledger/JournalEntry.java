package org.acme.fintech.ledger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An immutable, balanced double-entry journal entry (Part: enforcing invariants in the type). The
 * compact constructor enforces the defining rule of double-entry bookkeeping: the lines must sum to
 * zero, so money is never created or destroyed by a posting. It also requires at least two lines and
 * a copied, immutable line list. An unbalanced set of lines cannot be represented as a
 * {@code JournalEntry} at all.
 *
 * @param id                    the entry id
 * @param reference             the idempotency reference (a retry with the same reference is a no-op)
 * @param currency              the entry currency
 * @param lines                 the posting lines; copied defensively, must sum to zero
 * @param occurredAtEpochMillis when the entry was posted
 */
public record JournalEntry(String id, String reference, String currency, List<PostingLine> lines,
                           long occurredAtEpochMillis) {

    public JournalEntry {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(currency, "currency");
        lines = List.copyOf(lines);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("a journal entry needs at least two lines");
        }
        long sum = 0L;
        for (PostingLine line : lines) {
            sum += line.amountMinor();
        }
        if (sum != 0L) {
            throw new IllegalArgumentException("journal entry does not balance: lines sum to " + sum);
        }
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("reference", reference);
        body.put("currency", currency);
        body.put("lines", lines.stream().map(PostingLine::toBody).toList());
        return body;
    }
}
