package org.openide.util;

/**
 * Stub implementation of the org.openide.util.Parameters class, used to break
 * transitive dependencies upon an extensive list of classes in the Netbeans
 * platform.
 */
public class Parameters {

    /**
     * Asserts the parameter value is not <code>null</code>.
     *
     * @param  name the parameter name.
     * @param  value the parameter value.
     * @throws NullPointerException if the parameter value is <code>null</code>.
     */
    public static void notNull(CharSequence name, Object value) {
        if (value == null) {
            throw new NullPointerException("The " + name + " parameter cannot be null"); // NOI18N
        }
    }

}
