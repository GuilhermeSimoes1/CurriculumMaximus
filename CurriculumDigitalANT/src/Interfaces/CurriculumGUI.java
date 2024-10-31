/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaces;

import com.mycompany.curriculumdigital.Block;
import com.mycompany.curriculumdigital.BlockChain;
import com.mycompany.curriculumdigital.Certification;
import com.mycompany.curriculumdigital.CurriculumMaximus;
import com.mycompany.curriculumdigital.MerkleTree;
import com.mycompany.curriculumdigital.User;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Gui and Rodrigo
 */
public class CurriculumGUI extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public static String fileCurriculumMaximus = "curriculumMaximus.obj";
    //CurriculumMaximus curriculum;
    User user = null;
    MerkleTree temp;
    DefaultListModel<String> listModel;
    boolean flag; //para parar multiplas entradas
    BlockChain blockChain1 = new BlockChain();
    public static String fileTempTree = "temptree.mkt";
    public static String fileBlockChain = "blockchain.mkt";

    public CurriculumGUI() {
        initComponents();

        try {
            //curriculum = CurriculumMaximus.load(fileCurriculumMaximus); 
            temp = MerkleTree.loadFromFile(fileTempTree);
            updateMerkleTreeList();
            //txtLedge.setText(curriculum.toString());
        } catch (Exception e) {
            temp = new MerkleTree();
        }

    }

    public CurriculumGUI(User u) throws Exception {

        this();
        this.user = u;
        this.textFrom.setText(u.getName());
        listModel = new DefaultListModel<>();
        flag = true;
        this.blockChain1 = loadBlockchain(); // Carrega a Blockchain a partir do ficheiro
        updateBlockList();
    }

    private void updateBlockList() {
        DefaultListModel<String> blockListModel = new DefaultListModel<>();
        for (Block block : blockChain1.getChain()) {
            blockListModel.addElement(block.toString());
        }
        listBlockChain.setModel(blockListModel); // Atualiza o JList3 com os blocos
        jListBlockChain.setModel(blockListModel);

    }

    private void updateMerkleTreeList() {
        System.out.println("asdadfads" + temp);
        DefaultListModel model = new DefaultListModel();
        for (Certification tempC : (List<Certification>) temp.getElements()) {
            model.addElement(tempC.toString());
        }
        ListTempTree.setModel(model);
    }

    private void saveBlockchain(BlockChain blockchain) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.fileBlockChain))) {
            oos.writeObject(blockchain); // Guarda a blockchain no ficheiro
            System.out.println("Blockchain guardada com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao guardar a blockchain: " + e.getMessage());
        }
    }

    private BlockChain loadBlockchain() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.fileBlockChain))) {
            return (BlockChain) ois.readObject(); // Carrega a blockchain do ficheiro
        } catch (Exception e) {
            System.out.println("Nenhuma blockchain existente encontrada. A criar uma nova blockchain.");
            return new BlockChain(); // Se não houver ficheiro, cria uma nova blockchain
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        textFrom = new javax.swing.JTextField();
        txtTo = new javax.swing.JTextField();
        txtEvento = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        ListTempTree = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        listBlockChain = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtViewCurriculum = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListBlockChain = new javax.swing.JList<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        merkleGraphics1 = new Interfaces.MerkleGraphics();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/adicionar.png"))); // NOI18N
        jButton1.setText("Adicionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Serif", 2, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 255));
        jLabel3.setText("                        Curriculum Digital");

        textFrom.setEditable(false);
        textFrom.setBorder(javax.swing.BorderFactory.createTitledBorder("From"));
        textFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFromActionPerformed(evt);
            }
        });

        txtTo.setBorder(javax.swing.BorderFactory.createTitledBorder("To"));

        txtEvento.setBorder(javax.swing.BorderFactory.createTitledBorder("Evento"));

        ListTempTree.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(ListTempTree);

        listBlockChain.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listBlockChain.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listBlockChainValueChanged(evt);
            }
        });
        jScrollPane7.setViewportView(listBlockChain);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setText("Escolha um bloco e vá para a secção da MerkleTree");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEvento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFrom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(108, 108, 108)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addComponent(jScrollPane7))
                .addGap(50, 50, 50))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(107, 107, 107))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(textFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(txtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Criar Curriculum", new javax.swing.ImageIcon(getClass().getResource("/images/arquivo.png")), jPanel2); // NOI18N

        txtViewCurriculum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtViewCurriculumActionPerformed(evt);
            }
        });

        jList2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jList2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jList2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane5.setViewportView(jList2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eye.png"))); // NOI18N
        jButton3.setText("Visualizar Curriculum");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Email");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtViewCurriculum, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(154, 154, 154))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtViewCurriculum, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(189, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Visualizar curriculum", new javax.swing.ImageIcon(getClass().getResource("/images/visualizar.png")), jPanel3); // NOI18N

        jLabel7.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/blockchain.png"))); // NOI18N
        jLabel7.setText("Blockchain");

        jListBlockChain.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListBlockChainValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(jListBlockChain);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(332, 332, 332)
                        .addComponent(jLabel7))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(376, 376, 376)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 181, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(181, 181, 181))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BlockChain", new javax.swing.ImageIcon(getClass().getResource("/images/blockchain_1.png")), jPanel4); // NOI18N

        jLabel6.setFont(new java.awt.Font("Serif", 2, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 255));
        jLabel6.setText("About us");

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setText("Este trabalho foi desenvolvido por dois estudantes de Engenharia informática\ndo instituto politécnico de Tomar, no âmbito da cadeira de computação distribuida.\n\nRodrigo Calisto Nº 24851\nGuilherme Simões Nº 25259\n");
        jScrollPane3.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(331, 331, 331)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel6)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(270, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("About", new javax.swing.ImageIcon(getClass().getResource("/images/information.png")), jPanel5); // NOI18N

        javax.swing.GroupLayout merkleGraphics1Layout = new javax.swing.GroupLayout(merkleGraphics1);
        merkleGraphics1.setLayout(merkleGraphics1Layout);
        merkleGraphics1Layout.setHorizontalGroup(
            merkleGraphics1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 776, Short.MAX_VALUE)
        );
        merkleGraphics1Layout.setVerticalGroup(
            merkleGraphics1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(merkleGraphics1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(merkleGraphics1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("MerkleTree", new javax.swing.ImageIcon(getClass().getResource("/images/phylogenetics_1855061 (1).png")), jPanel6); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        listModel.clear(); // Limpa a lista antes de mostrar novos eventos
        String email = txtViewCurriculum.getText();
        if (!email.isEmpty()) {
            boolean emailFound = false;

            try (BufferedReader br = new BufferedReader(new FileReader("List.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.equals(email)) {
                        emailFound = true;
                        listModel.addElement(line); // Adiciona o meail á lista

                        // Lê as novas linhas (eventos) até encontrar outro email ou até acabar o ficheiro
                        while ((line = br.readLine()) != null && !line.contains("@")) {
                            listModel.addElement(line); // Adiciona eventos á lista
                        }
                        break; // Para depois de encontrar todos os eventos pertencentes aquele email
                    }
                }

                if (!emailFound) {
                    System.out.println("Email not found");
                }

                jList2.setModel(listModel); // atualiza a lista na UI
                flag = false;
            } catch (Exception e) {
                System.out.println("" + e);
            }
        } else {
            System.out.println("Email field is empty.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jList2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jList2AncestorAdded

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        User toUser = new User(txtTo.getText());
        try {
            toUser.loadPublic();
            Certification c;
            c = new Certification(
                    user,
                    toUser,
                    txtEvento.getText()
            );
            temp = temp.add(c);
            temp.saveToFile(fileTempTree);
            if (temp.getNumberOfElemensts() >= 4) {
                //System.out.println(temp.toString());
                blockChain1.add(temp.getRoot(), (int) 9);
                temp.saveToFile(blockChain1.getLastBlockHash() + ".mkt");
                merkleGraphics1.setMerkle(temp);
                temp = new MerkleTree();
                updateBlockList();
                saveBlockchain(blockChain1);
                DefaultListModel model = new DefaultListModel();
                ListTempTree.setModel(model);
            } else {
                DefaultListModel model = new DefaultListModel();
                for (Certification tempC : (List<Certification>) temp.getElements()) {
                    model.addElement(tempC.toString());
                }
                ListTempTree.setModel(model);
            }
        } catch (Exception ex) {
            Logger.getLogger(CurriculumGUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void textFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFromActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFromActionPerformed

    private void txtViewCurriculumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtViewCurriculumActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtViewCurriculumActionPerformed

    private void listBlockChainValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listBlockChainValueChanged
        // TODO add your handling code here:
        if (listBlockChain.getSelectedIndex() != -1) {
            try {
                MerkleTree mktemp = MerkleTree.loadFromFile(this.blockChain1.get(listBlockChain.getSelectedIndex()).getCurrentHash() + ".mkt");
                merkleGraphics1.setMerkle(mktemp);
            } catch (IOException ex) {
                Logger.getLogger(CurriculumGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CurriculumGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_listBlockChainValueChanged

    private void jListBlockChainValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListBlockChainValueChanged
        // TODO add your handling code here:
        if (jListBlockChain.getSelectedIndex() != -1) {
            try {
                MerkleTree mktemp = MerkleTree.loadFromFile(this.blockChain1.get(jListBlockChain.getSelectedIndex()).getCurrentHash() + ".mkt");
                merkleGraphics1.setMerkle(mktemp);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CurriculumGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CurriculumGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jListBlockChainValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CurriculumGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CurriculumGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CurriculumGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CurriculumGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User u = null;
                try {
                    new CurriculumGUI(u).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(CurriculumGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> ListTempTree;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jListBlockChain;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JList<String> listBlockChain;
    private Interfaces.MerkleGraphics merkleGraphics1;
    private javax.swing.JTextField textFrom;
    private javax.swing.JTextField txtEvento;
    private javax.swing.JTextField txtTo;
    private javax.swing.JTextField txtViewCurriculum;
    // End of variables declaration//GEN-END:variables
}
