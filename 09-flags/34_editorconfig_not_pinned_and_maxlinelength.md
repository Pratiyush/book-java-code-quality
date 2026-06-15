# FLAG — key 34: EditorConfig not in SOURCE-PIN; `max_line_length` not a core-spec property

**Two related atoms.**

## (a) EditorConfig is not a SOURCE-PIN §2 row
`.editorconfig` / `spec.editorconfig.org` is **key 34's primary authority** for every EditorConfig fact
(properties, glob model, precedence) but has **no row in `SOURCE-PIN.md` §2**. Propose adding a row:
EditorConfig spec (`spec.editorconfig.org`) + `editorconfig.org`, pinned to the current spec edition.
(Sibling of the key-24 "JCStress not pinned" gap.)

## (b) `max_line_length` is editor-supported but NOT in the core spec's listed properties
In the key-34 scan, `spec.editorconfig.org`'s listed core properties were `root`, `indent_style`,
`indent_size`, `tab_width`, `end_of_line`, `charset`, `trim_trailing_whitespace`, `insert_final_newline` —
**`max_line_length` was not among them**, though many editor plugins honor it. Never present
`max_line_length` as a guaranteed spec property; describe it as widely-editor-supported but extra to the
core list, and re-confirm against the pinned spec edition.

**Status:** `⚠ verify at pin` / SOURCE-PIN add-row proposed.

**Source:** `spec.editorconfig.org` (live, not pinned).

**Filed by:** key 34 research (2026-06-15).
