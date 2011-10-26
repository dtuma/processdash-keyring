/**
 * The Netbeans Keyring API is a very useful library which could provide
 * benefit to many applications.  Unfortunately, it has transitive
 * dependencies upon <b>many</b> other classes in the Netbeans platform.
 * 
 * Breaking those transitive dependencies could be accomplished in various
 * ways.  One obvious approach would be to edit the API and implementation
 * classes in the keyring library so the transitive dependencies are removed.
 * However, when the Netbeans keyring library is updated or enhanced, we may 
 * want to incorporate those new changes, and we would prefer not to perform
 * tedious merging operations.
 * 
 * So instead, this package provides stub implementations of certain Netbeans
 * classes. The keyring implementation classes remain unmodified, but they
 * are linked against these stub classes instead of the original Netbeans
 * classes.  These simple stub classes break the transitive dependency chain.
 */
package org.openide.util;

