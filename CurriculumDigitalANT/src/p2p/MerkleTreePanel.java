/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package p2p;

import blockchain.MerkleTree;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 *
 * @author Trabalho
 */
public class MerkleTreePanel extends javax.swing.JPanel {

    List<String> elements = new ArrayList<>();
    JFileChooser fc = new JFileChooser(new File("."));

    /**
     * Creates new form MerkleTreePanel
     */
    public MerkleTreePanel() {
        initComponents();
     fc.setSelectedFile(new File("defaultTree.mkt"));
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("defaultTree.mkt"))) {
            elements = (List<String>) in.readObject();
            updateGUI();
        } catch (Exception ex) {
        }
        updateGUI();
    }

    public void saveFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()))) {
            out.writeObject(elements);
        } catch (Exception ex) {
        }
    }

    public void updateGUI() {
        DefaultListModel model = new DefaultListModel();
        for (String elem : elements) {
            model.addElement(elem);
        }

        lst.setModel(model);
        updateMerkle();
        btSearch.setBackground(Color.LIGHT_GRAY);
        saveFile();
    }

    public void updateMerkle() {
        MerkleTree tree = new MerkleTree(elements);
        txt.setText(tree.toTree());
        List<String> proof = tree.getProof(txtElem.getText());
        txtProof.setText(proof.toString());
        merklePanel.setProof(txtElem.getText(), proof);

        merklePanel.setMerkle(tree);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lst = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        txtElem = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btSave = new javax.swing.JButton();
        btLoad = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtProof = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btSearch = new javax.swing.JButton();
        txtSearchElem = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        merklePanel = new p2p.MerkleGraphics();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Elementos"));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 167));

        lst.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lst.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lst.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lst);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.WEST);

        txtElem.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        txtElem.setBorder(javax.swing.BorderFactory.createTitledBorder("Elemento"));

        jPanel7.setLayout(new java.awt.GridLayout(1, 5, 5, 5));

        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_Add.png"))); // NOI18N
        btAdd.setText("Adicionar");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });
        jPanel7.add(btAdd);

        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_Remove.png"))); // NOI18N
        btRemove.setText("Remover");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });
        jPanel7.add(btRemove);

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_Update.png"))); // NOI18N
        btUpdate.setText("Alterar");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });
        jPanel7.add(btUpdate);

        btSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_save.png"))); // NOI18N
        btSave.setText("Guardar");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });
        jPanel7.add(btSave);

        btLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_load.png"))); // NOI18N
        btLoad.setText("Ler");
        btLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoadActionPerformed(evt);
            }
        });
        jPanel7.add(btLoad);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtElem, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtElem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        txtProof.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtProof.setBorder(javax.swing.BorderFactory.createTitledBorder("Prova"));
        jPanel3.add(txtProof, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        btSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_verify.png"))); // NOI18N
        btSearch.setText("Verificar");
        btSearch.setPreferredSize(new java.awt.Dimension(100, 23));
        btSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchActionPerformed(evt);
            }
        });
        jPanel6.add(btSearch);

        txtSearchElem.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        txtSearchElem.setBorder(javax.swing.BorderFactory.createTitledBorder("Elemento"));
        jPanel6.add(txtSearchElem);

        jPanel3.add(jPanel6, java.awt.BorderLayout.WEST);

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel5.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout merklePanelLayout = new javax.swing.GroupLayout(merklePanel);
        merklePanel.setLayout(merklePanelLayout);
        merklePanelLayout.setHorizontalGroup(
            merklePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1104, Short.MAX_VALUE)
        );
        merklePanelLayout.setVerticalGroup(
            merklePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
        );

        jPanel5.add(merklePanel, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Grafico", new javax.swing.ImageIcon(getClass().getResource("/images/bt_tree.png")), jPanel5); // NOI18N

        txt.setColumns(20);
        txt.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txt.setRows(5);
        jScrollPane2.setViewportView(txt);

        jTabbedPane1.addTab("Texto", new javax.swing.ImageIcon(getClass().getResource("/images/bt_Text.png")), jScrollPane2); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bt_MerkleTree.png"))); // NOI18N
        jTabbedPane1.addTab("Merkle Tree", new javax.swing.ImageIcon(getClass().getResource("/images/bt_About.png")), jLabel1); // NOI18N

        jPanel4.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lstValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstValueChanged
        if (lst.getSelectedIndex() >= 0) {
            txtElem.setText(lst.getSelectedValue());
            txtSearchElem.setText(lst.getSelectedValue());
            btSearch.setBackground(Color.LIGHT_GRAY);
            updateMerkle();
        }

    }//GEN-LAST:event_lstValueChanged

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        //        if (!elements.contains(txtElem.getText())) {
        elements.add(txtElem.getText());
        updateGUI();
        //      }
    }//GEN-LAST:event_btAddActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        elements.remove(txtElem.getText());
        updateGUI();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if (lst.getSelectedIndex() >= 0 && !elements.contains(txtElem.getText())) {
            elements.set(lst.getSelectedIndex(), txtElem.getText());
            updateGUI();
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()))) {
                out.writeObject(elements);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }//GEN-LAST:event_btSaveActionPerformed

    private void btLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoadActionPerformed
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()))) {
                elements = (List<String>) in.readObject();
                updateGUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
               
            }
        }
    }//GEN-LAST:event_btLoadActionPerformed

    private void btSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchActionPerformed
        String proof = this.txtProof.getText();
        proof = proof.replaceAll("\\[", "");
        proof = proof.replaceAll("\\]", "");
        proof = proof.replaceAll(",", " ");
        proof = proof.replaceAll("  ", " ");

        List<String> list = Arrays.asList(proof.split(" "));
        if (MerkleTree.isProofValid(txtSearchElem.getText(), list)) {
            btSearch.setBackground(Color.GREEN);
        } else {
            btSearch.setBackground(Color.RED);
        }
    }//GEN-LAST:event_btSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btLoad;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSearch;
    private javax.swing.JButton btUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<String> lst;
    private p2p.MerkleGraphics merklePanel;
    private javax.swing.JTextArea txt;
    private javax.swing.JTextField txtElem;
    private javax.swing.JTextField txtProof;
    private javax.swing.JTextField txtSearchElem;
    // End of variables declaration//GEN-END:variables
}
