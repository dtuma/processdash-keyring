package org.openide.util;

/**
 * Stub implementation of the org.openide.util.Utilities class, used to break
 * transitive dependencies upon an extensive list of classes in the Netbeans
 * platform.
 */
public class Utilities {

    public static boolean isWindows() {
        return System.getProperty("os.name").indexOf("Windows") != -1;
    }

    public static boolean isMac() {
        return  System.getProperty("os.name").indexOf("OS X") != -1;
    }

}
