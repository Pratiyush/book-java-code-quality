package org.acme.secgate;

/**
 * Whether a security finding lives in code the change <em>introduced or touched</em>, or in code that
 * was already there. This is the distinction the clean-as-you-code policy rests on (Chapter 19): gating
 * whole-repo absolutes on a legacy codebase blocks every pull request on pre-existing findings the
 * change never created — exactly the noise that gets a security gate routed around or disabled. Scoping
 * the block to new and changed code keeps the gate adoptable and assigns ownership to whoever touched
 * the code, while the pre-existing backlog is still triaged rather than ignored.
 */
public enum FindingScope {

    /** In code the change introduced or modified — what the gate is allowed to block a merge on. */
    NEW,

    /** Pre-existing security debt the change did not touch — triaged and tracked, never used to block the change. */
    EXISTING
}
