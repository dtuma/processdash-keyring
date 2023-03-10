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

package org.netbeans.modules.keyring.fallback;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDisplayer;
import org.openide.NotificationLineSupport;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

class MasterPasswordPanel extends JPanel {

    public MasterPasswordPanel() {
        initComponents();
    }

    /**
     * Shows this dialog.
     * @param fresh true if the master password has not yet been set
     * @return master password, and if selected, new master password; or null if canceled
     */
    public char[][] display(boolean fresh) {
        final JButton ok = new JButton(NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.ok"));
        ok.setDefaultCapable(true);
        NotifyDescriptor d = new NotifyDescriptor(this,
                NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.enter_master_password"),
                NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE,
                new Object[] {ok, NotifyDescriptor.CANCEL_OPTION}, ok);
        final NotificationLineSupport notification = d.createNotificationLineSupport();
        setNewBox.setEnabled(!fresh);
        final Runnable update = new Runnable() {
            public void run() {
                if (masterPasswordField.getPassword().length == 0) {
                    notification.setInformationMessage(NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.enter_password"));
                    ok.setEnabled(false);
                    return;
                }
                boolean changing = setNewBox.isSelected();
                newLabel1.setEnabled(changing);
                newField1.setEnabled(changing);
                newLabel2.setEnabled(changing);
                newField2.setEnabled(changing);
                if (changing) {
                    if (newField1.getPassword().length == 0) {
                        notification.setInformationMessage(NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.enter_new_password"));
                        ok.setEnabled(false);
                        return;
                    }
                    // XXX issue warning in case non-ASCII characters encountered
                    if (!Arrays.equals(newField1.getPassword(), newField2.getPassword())) {
                        notification.setInformationMessage(NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.password_mismatch"));
                        ok.setEnabled(false);
                        return;
                    }
                }
                notification.clearMessages();
                ok.setEnabled(true);
            }
        };
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                update.run();
            }
            public void removeUpdate(DocumentEvent e) {
                update.run();
            }
            public void changedUpdate(DocumentEvent e) {}
        };
        update.run();
        masterPasswordField.getDocument().addDocumentListener(listener);
        newField1.getDocument().addDocumentListener(listener);
        newField2.getDocument().addDocumentListener(listener);
        setNewBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update.run();
            }
        });
        if (DialogDisplayer.getDefault().notify(d) != ok) {
            return null;
        }
        char[] masterPassword = masterPasswordField.getPassword();
        return setNewBox.isSelected() ? new char[][] {masterPassword, newField1.getPassword()} : new char[][] {masterPassword};
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        masterPasswordLabel = new javax.swing.JLabel();
        masterPasswordField = new javax.swing.JPasswordField();
        setNewBox = new javax.swing.JCheckBox();
        newLabel1 = new javax.swing.JLabel();
        newField1 = new javax.swing.JPasswordField();
        newLabel2 = new javax.swing.JLabel();
        newField2 = new javax.swing.JPasswordField();
        explanationScroll = new javax.swing.JScrollPane();
        explanation = new javax.swing.JTextArea();

        masterPasswordLabel.setLabelFor(masterPasswordField);
        org.openide.awt.Mnemonics.setLocalizedText(masterPasswordLabel, org.openide.util.NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.masterPasswordLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(setNewBox, org.openide.util.NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.setNewBox.text")); // NOI18N

        newLabel1.setLabelFor(newField1);
        org.openide.awt.Mnemonics.setLocalizedText(newLabel1, org.openide.util.NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.newLabel1.text")); // NOI18N
        newLabel1.setEnabled(false);

        newField1.setEnabled(false);

        newLabel2.setLabelFor(newField2);
        org.openide.awt.Mnemonics.setLocalizedText(newLabel2, org.openide.util.NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.newLabel2.text")); // NOI18N
        newLabel2.setEnabled(false);

        newField2.setEnabled(false);

        explanationScroll.setBorder(null);

        explanation.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        explanation.setEditable(false);
        explanation.setLineWrap(true);
        explanation.setText(NbBundle.getMessage(MasterPasswordPanel.class, "MasterPasswordPanel.explanation.text")); // NOI18N
        explanation.setWrapStyleWord(true);
        explanationScroll.setViewportView(explanation);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(explanationScroll, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newLabel1)
                            .addComponent(newLabel2)
                            .addComponent(masterPasswordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(masterPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(newField2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(newField1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)))
                    .addComponent(setNewBox, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(masterPasswordLabel)
                    .addComponent(masterPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(setNewBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newLabel1)
                    .addComponent(newField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newLabel2)
                    .addComponent(newField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(explanationScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea explanation;
    private javax.swing.JScrollPane explanationScroll;
    private javax.swing.JPasswordField masterPasswordField;
    private javax.swing.JLabel masterPasswordLabel;
    private javax.swing.JPasswordField newField1;
    private javax.swing.JPasswordField newField2;
    private javax.swing.JLabel newLabel1;
    private javax.swing.JLabel newLabel2;
    private javax.swing.JCheckBox setNewBox;
    // End of variables declaration//GEN-END:variables

}
