/**
 * SOLID taken to its over-application trap: an interface for every class and a factory for every
 * interface, so adding one field threads through several layers of indirection.
 *
 * <p>Every type here satisfies the letter of OCP and DIP — collaborators depend on abstractions, and
 * new behaviour could be a new implementation rather than an edit. The cost the chapter names is that
 * none of the abstractions earns its keep: there is exactly one implementation of each interface, and
 * the factories add a layer whose only job is to hand back that one implementation. Read this package
 * beside {@code org.acme.design.balanced}, which reaches the same outcome with far less ceremony.
 */
package org.acme.design.overengineered;
