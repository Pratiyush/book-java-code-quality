/**
 * The seed domain for the book's companion reference project: a minimal storefront cart and its priced
 * lines, sized so the assembled local quality toolchain has real source, bytecode, and branches to read.
 *
 * <p>This Chapter 3 module is a <em>map</em> made concrete. Its load-bearing artifact is the build
 * itself — {@code pom.xml} wires the layered local toolchain the chapter describes (the compiler held to
 * {@code -Xlint:all -Werror}, then Checkstyle on source, SpotBugs on bytecode, and JaCoCo over the test
 * run), each tagged region a snippet the chapter displays. The Java here exists so those layers gate
 * something real and {@code mvn -Pquality verify} runs the whole stack green.
 */
package org.acme.toolchain;
