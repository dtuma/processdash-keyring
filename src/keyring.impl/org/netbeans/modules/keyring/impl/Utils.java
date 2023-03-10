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

package org.netbeans.modules.keyring.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Utils {

    private Utils() {}

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public static byte[] chars2Bytes(char[] chars) {
        byte[] bytes = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            bytes[i * 2] = (byte) (chars[i] >> 8);   // TUMA modified
            bytes[i * 2 + 1] = (byte) (chars[i] & 0xff);    // TUMA modified
        }
        return bytes;
    }

    public static char[] bytes2Chars(byte[] bytes) {
        char[] result = new char[bytes.length / 2];
        for (int i = 0; i < result.length; i++) {
            int high = (int) bytes[i * 2];
            int low  = (int) bytes[i * 2 + 1];
            result[i] = (char) ((high << 8) | (low & 0xff)); // TUMA modified
        }
        return result;
    }

    /** Tries to set permissions on preferences storage file to -rw------- */
    public static void goMinusR(Preferences p) {
        File props = new File(System.getProperty("netbeans.user"), ("config/Preferences" + p.absolutePath()).replace('/', File.separatorChar) + ".properties");
        if (props.isFile()) {
            props.setReadable(false, false); // seems to be necessary, not sure why
            props.setReadable(true, true);
            LOG.log(Level.FINE, "chmod go-r {0}", props);
        } else {
            LOG.log(Level.FINE, "no such file to chmod: {0}", props);
        }
    }

    public static final Charset UTF_8;
    static {
        UTF_8 = Charset.forName("UTF-8");
    }

}
