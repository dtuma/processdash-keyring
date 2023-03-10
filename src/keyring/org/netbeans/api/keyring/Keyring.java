/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.api.keyring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.keyring.KeyringProvider;
import org.openide.util.Lookup;
import org.openide.util.Parameters;

/**
 * Client class for working with stored keys (such as passwords).
 * <p>The key identifier should be unique for the whole application,
 * so qualify it with any prefixes as needed.
 * <p>Avoid calling methods on this class from the event dispatch thread,
 * as some provider implementations may need to block while displaying a dialog
 * (e.g. prompting for a master password to access the keyring).
 */
public class Keyring {

    private Keyring() {}

    private static final Logger LOG = Logger.getLogger("org.netbeans.modules.keyring");

    private static KeyringProvider PROVIDER;
    private static synchronized KeyringProvider provider() {
        if (PROVIDER == null) {
            for (KeyringProvider p : Lookup.getDefault().lookupAll(KeyringProvider.class)) {
                if (p.enabled()) {
                    PROVIDER = p;
                    break;
                }
            }
            if (PROVIDER == null) {
                PROVIDER = new DummyKeyringProvider();
            }
            LOG.log(Level.FINE, "Using provider: {0}", PROVIDER);
        }
        return PROVIDER;
    }

    /**
     * @return true if the current keyring provider is capable of storing
     *     passwords for future sessions; false if it can only store them
     *     for the duration of the running JVM process.
     */
    public static synchronized boolean isPersistent() {
        if (provider() instanceof DummyKeyringProvider)
            return false;
        else
            return true;
    }

    /**
     * Reads a key from the ring.
     * @param key the identifier of the key
     * @return its value if found (you may null out its elements), else null if not present
     */
    public static synchronized char[] read(String key) {
        Parameters.notNull("key", key);
        LOG.log(Level.FINEST, "reading: {0}", key);
        return provider().read(key);
    }

    /**
     * Saves a key to the ring.
     * If it could not be saved, does nothing.
     * If the key already existed, overwrites the password.
     * @param key a key identifier
     * @param password the password or other sensitive information associated with the key
     *                 (its contents will be nulled out by end of call)
     * @param description a user-visible description of the key (may be null)
     */
    public static synchronized void save(String key, char[] password, String description) {
        Parameters.notNull("key", key);
        Parameters.notNull("password", password);
        LOG.log(Level.FINEST, "saving: {0}", key);
        provider().save(key, password, description);
        Arrays.fill(password, (char) 0);
    }

    /**
     * Deletes a key from the ring.
     * If the key was not in the ring to begin with, does nothing.
     * @param key a key identifier
     */
    public static synchronized void delete(String key) {
        Parameters.notNull("key", key);
        LOG.log(Level.FINEST, "deleting: {0}", key);
        provider().delete(key);
    }

    private static class DummyKeyringProvider implements KeyringProvider {
        public @Override boolean enabled() {
            return true;
        }
        // prefer byte[] to make passwords less readable in heap dumps:
        private final Map<String,byte[]> passwords = new HashMap<String,byte[]>();
        public @Override char[] read(String key) {
            byte[] pwd = passwords.get(key);
            return pwd != null ? bytes2Chars(pwd) : null;
        }
        public @Override void save(String key, char[] password, String description) {
            passwords.put(key, chars2Bytes(password));
        }
        public @Override void delete(String key) {
            passwords.remove(key);
        }
    }

    // XXX copied from org.netbeans.modules.keyring.impl.Utils:
    private static byte[] chars2Bytes(char[] chars) {
        byte[] bytes = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            bytes[i * 2] = (byte) (chars[i] / 256);
            bytes[i * 2 + 1] = (byte) (chars[i] % 256);
        }
        return bytes;
    }
    private static char[] bytes2Chars(byte[] bytes) {
        char[] result = new char[bytes.length / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (char) (((int) bytes[i * 2]) * 256 + (int) bytes[i * 2 + 1]);
        }
        return result;
    }

}
