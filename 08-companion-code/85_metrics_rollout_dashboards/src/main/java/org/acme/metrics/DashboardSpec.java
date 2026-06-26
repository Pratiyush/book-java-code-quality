package org.acme.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * A "good dashboard" specification: the set of tiles a quality dashboard should show, built so the
 * dashboard informs without distorting. A single point-in-time number is noise; the trend is the signal,
 * so each tile is a trend paired with a counter-metric, and the dashboard carries a new-code lens — it
 * leads with the quality of recent work so it rewards "improving from here" rather than punishing
 * inherited legacy.
 *
 * <p>Two rules are enforced in code, not left to discipline:
 *
 * <ul>
 *   <li>A {@link MetricKind#VANITY} tile (lines of code, commit count, raw velocity) is refused — a
 *       dashboard full of activity counts looks busy and means nothing, and choosing the wrong metrics
 *       to display is the main way a dashboard fails.</li>
 *   <li>An <em>individual-scoped</em> tile is refused — a dashboard turned into a personal leaderboard
 *       triggers Goodhart gaming and harms morale. Team and system trends only; never an individual
 *       ranking.</li>
 * </ul>
 *
 * <p>The honest edge the chapter keeps: a green dashboard is not quality. It shows measured proxies
 * trending; the design call and the logic flaw still need human review. The spec makes the proxies
 * visible and actionable — it does not certify the code is good.
 */
public final class DashboardSpec {

    private final List<Tile> tiles = new ArrayList<>();

    /** Adds a tile, refusing the two that weaponize a dashboard: vanity metrics and individual ranking. */
    public DashboardSpec addTile(Tile tile) {
        // tag::dashboard-no-leaderboard[]
        if (!tile.kind().isUsable()) { // vanity (LOC, commits) looks busy, means nothing
            throw new IllegalArgumentException("refused vanity tile: " + tile.label());
        }
        if (tile.individualScoped()) { // a leaderboard triggers Goodhart gaming and harms morale
            throw new IllegalArgumentException("refused individual leaderboard: " + tile.label());
        }
        // end::dashboard-no-leaderboard[]
        tiles.add(tile);
        return this;
    }

    /** A point-in-time view of the configured tiles — the readable surface a dashboard would render. */
    public List<Tile> snapshot() {
        return List.copyOf(tiles);
    }

    /** How many tiles the dashboard carries. */
    public int tileCount() {
        return tiles.size();
    }

    /**
     * One dashboard tile: a metric shown as a trend, paired with its counter-metric, on a new-code lens.
     *
     * @param label            the human label for the tile
     * @param kind             whether the metric is an outcome, a quality trend, or vanity
     * @param counter          the counter-metric the tile is paired with — a tile is never shown alone
     * @param newCodeLens      whether the tile shows recent work prominently (clean-as-you-code)
     * @param individualScoped whether the tile attributes to an individual — always refused if {@code true}
     */
    public record Tile(
            String label,
            MetricKind kind,
            CounterMetric counter,
            boolean newCodeLens,
            boolean individualScoped) {

        /** Compact constructor: a tile needs a label and a counter-metric to be a trustworthy tile. */
        public Tile {
            if (label == null || label.isBlank()) {
                throw new IllegalArgumentException("tile label must not be blank");
            }
            if (counter == null) {
                throw new IllegalArgumentException("every tile must carry a counter-metric: " + label);
            }
        }
    }
}
