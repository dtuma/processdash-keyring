package org.openide.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Stub implementation of the org.openide.util.NbBundle class, used to break
 * transitive dependencies upon an extensive list of classes in the Netbeans
 * platform.
 */
public class NbBundle {
    
    private static String APP_NAME = "Java Application";
    
    public static void setAppName(String appName) {
        APP_NAME = appName;
    }

    public static ResourceBundle getBundle(String string) {
        return new DummyResourceBundle();
    }

    private static class DummyResourceBundle extends ResourceBundle {

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        @Override
        protected Object handleGetObject(String key) {
            return APP_NAME;
        }
        
    }
}
