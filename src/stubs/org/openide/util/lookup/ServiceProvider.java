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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.openide.util.lookup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//import org.openide.util.Lookup;

/**
 * Declarative registration of a singleton service provider.
 * By marking an implementation class with this annotation,
 * you automatically register that implementation, normally in {@link Lookup#getDefault}.
 * The class must be public and have a public no-argument constructor.
 * <p>Example of usage:
 * <pre>
 * package my.module;
 * import org.netbeans.spi.whatever.Thing;
 * import org.openide.util.lookup.ServiceProvider;
 * &#64;ServiceProvider(service=Thing.class)
 * public class MyThing implements Thing {...}
 * </pre>
 * <p>would result in a resource file <code>META-INF/services/org.netbeans.spi.whatever.Thing</code>
 * containing the single line of text: <code>my.module.MyThing</code>
 * @see Lookups#metaInfServices(ClassLoader)
 * @since org.openide.util 7.20
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ServiceProvider {

    /**
     * The interface (or abstract class) to register this implementation under.
     * It is an error if the implementation class is not in fact assignable to the interface.
     * <p>If you need to register one class under multiple interfaces, use {@link ServiceProviders}.
     * <p>Requests to look up the specified interface should result in this implementation.
     * Requests for any other types may or may not result in this implementation even if the
     * implementation is assignable to those types.
     */
    Class<?> service();

    /**
     * An optional position in which to register this service relative to others.
     * Lower-numbered services are returned in the lookup result first.
     * Services with no specified position are returned last.
     */
    int position() default Integer.MAX_VALUE;

    /**
     * An optional list of implementations (given as fully-qualified class names) which this implementation supersedes.
     * If specified, those implementations will not be loaded even if they were registered.
     * Useful on occasion to cancel a generic implementation and replace it with a more advanced one.
     */
    String[] supersedes() default {};

    /**
     * An optional path to register this implementation in.
     * For example, <code>Projects/sometype/Nodes</code> could be used.
     * This style of registration would be recognized by {@link Lookups#forPath}
     * rather than {@link Lookup#getDefault}.
     */
    String path() default "";

}
