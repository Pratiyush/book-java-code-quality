package org.acme.cigate;

/**
 * Whether a finding lives in code the change <em>introduced or touched</em>, or in code that was
 * already there. This is the distinction "clean as you code" rests on: gating whole-repo absolutes on
 * a legacy codebase blocks every pull request on debt the change never created, while gating the new
 * and changed code makes the gate adoptable immediately and assigns ownership correctly — a developer
 * owns the quality of what they touch, not the quality of the whole history they inherited.
 */
public enum FindingScope {

    /** In code the change introduced or modified — what a clean-as-you-code gate is allowed to block on. */
    NEW,

    /** Pre-existing debt the change did not touch — tracked and warned, never used to block the change. */
    EXISTING
}
