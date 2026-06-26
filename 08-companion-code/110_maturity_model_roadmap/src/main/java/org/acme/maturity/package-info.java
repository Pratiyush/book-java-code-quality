/**
 * Chapter 47 companion — a code-quality maturity model and adoption roadmap (Where to Start, and How to
 * Keep Going). The book's final chapter.
 *
 * <p>The book covers a lot, and no team can adopt all of it at once. This closer turns the whole book into
 * a staged roadmap — a sequence from the first thing on Monday to advanced governance — and this module
 * makes the roadmap runnable: a model that takes where a team honestly stands on each quality dimension
 * and returns its overall maturity level and the one next step worth taking. It is offered, in code as in
 * prose, as a guide to outcomes, not a ladder to climb for its own sake.
 *
 * <p>The model is built on the JDK alone and is composed of:
 * <ul>
 *   <li>{@link org.acme.maturity.Stage} — the five roadmap stages (foundations, gate the basics, deepen,
 *       govern and observe, sustain and evolve), ordered cheapest-first; {@link org.acme.maturity.Roadmap}
 *       is the runnable view.</li>
 *   <li>{@link org.acme.maturity.Dimension} — the book's recurring through-lines a team is assessed on,
 *       rated independently so the model can name the lowest, most painful one.</li>
 *   <li>{@link org.acme.maturity.DimensionRating} — a team's honest rating of one dimension: the stage it
 *       reached, and whether that dimension's outcomes are actually improving.</li>
 *   <li>{@link org.acme.maturity.MaturityAssessment} — the composition: it reduces the ratings to an
 *       overall level (the lowest stage, never an average) and one {@link org.acme.maturity.NextStep}.</li>
 *   <li>{@link org.acme.maturity.RoadmapPolicy} — the externalized {@code dev} / {@code prod} policy
 *       (require-outcomes, sustain-at-stage), so the assessment is tailored, not compiled in.</li>
 * </ul>
 *
 * <p>The chapter's closing honesty is encoded in the model, not only the comments:
 * <ul>
 *   <li>A maturity level is a vanity metric the moment it becomes a goal — so the overall level is the
 *       <em>lowest</em> dimension's stage, never an average that hides a fire, and a dimension whose
 *       outcomes have stalled is discounted rather than rewarded for ticked boxes (Chapters 1, 38).</li>
 *   <li>The roadmap is a default, not your plan — so the next step targets the lowest, most painful
 *       dimension (start where the pain is), not the next rung dogmatically.</li>
 *   <li>More maturity is not more value past a point — so the model recommends sustaining and subtracting
 *       gates once every dimension is past the policy's threshold, rather than always climbing (Chapters
 *       26, 33).</li>
 *   <li>Tools without culture fail — so {@link org.acme.maturity.Dimension#CULTURE_KNOWLEDGE} is a first-
 *       class dimension, the one no plug-in installs and the one that decides whether the rest are used or
 *       gamed (Chapters 1, 37).</li>
 *   <li>Everything specific is dated; the principles outlast it — this model encodes the principles
 *       (measure, start where the pain is, climb for outcomes, never crown), and pins every version it
 *       touches to the book's source pin.</li>
 * </ul>
 */
package org.acme.maturity;
