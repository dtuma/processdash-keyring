package org.openide.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stub implementation of the org.openide.util.Lookup class, used to break
 * transitive dependencies upon an extensive list of classes in the Netbeans
 * platform.
 */
public class Lookup {

    private static final Logger LOG = Logger.getLogger("org.openide.util");
    
    private static final Lookup DEFAULT = new Lookup();

    public static synchronized Lookup getDefault() {
        return DEFAULT;
    }

    public <T> Collection<? extends T> lookupAll(Class<T> clazz) {
        List result = new ArrayList();
        
        Properties p = new Properties();
        try {
            p.load(Lookup.class.getResourceAsStream("Lookup.properties"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unable to load lookup properties file", e);
        }

        String searchPrefix = clazz.getName() + "~";
        for (Map.Entry e : p.entrySet()) {
            String key = (String) e.getKey();
            String className = (String) e.getValue();
            if (key.startsWith(searchPrefix)) {
                try {
                    Class oneClass = Lookup.class.getClassLoader().loadClass(className);
                    result.add(oneClass.newInstance());
                    LOG.finer("Created lookup provider " + className);
                } catch (Throwable t) {
                    LOG.log(Level.SEVERE, "Unable to create lookup object", t);
                }
            }
        }

        return result;
    }

}
