/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

import com.mycompany.curriculumdigital.ObjecUtils.ObjectUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gui
 */
public class CurriculumMaximus implements Serializable {
    
       private ArrayList<Certification> ledger;
       BlockChain bc ;
       public static int DIFICULTY = 2;

    public CurriculumMaximus() throws Exception {
        ledger = new ArrayList<>();
        bc = new BlockChain();

    }

    public List<Certification> getLedger() {
        return ledger;
    }

public String toString() {
    StringBuilder txt = new StringBuilder();
    for (Block b : bc.getChain()) {
        Certification c = null;
        try {
            c = (Certification) ObjectUtils.convertBase64ToObject(b.getData());
        } catch (Exception e) {
            System.err.println("Erro ao converter dados do bloco: " + e.getMessage());
        }

        // Verifica se 'c' não é null antes de chamar toString()
        if (c != null) {
            txt.append(b.getPreviousHash()).append(" ")
               .append(c.toString()).append(" ")
               .append(b.getNonce()).append(" ")
               .append(b.getCurrentHash()).append("\n");
        } else {
            txt.append(b.getPreviousHash()).append(" ")
               .append("Certificação não disponível").append(" ")
               .append(b.getNonce()).append(" ")
               .append(b.getCurrentHash()).append("\n");
        }
    }
    return txt.toString();
}


    public void save(String fileName) throws IOException {
        try ( ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    public static CurriculumMaximus load(String fileName) throws IOException, ClassNotFoundException {
        try ( ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fileName))) {
            return (CurriculumMaximus) in.readObject();
        }
    }



   //public boolean isValid(Transaction t) throws Exception {
        
    //}

    public void add(Certification c) throws Exception {
        
            ledger.add(c);  
            String txtTransaction = ObjectUtils.convertObjectToBase64(c);
            bc.add(txtTransaction, DIFICULTY);
 
    }
    
    public List<Certification> getTransactionBlockchain() throws Exception{
        List<Certification> lst = new ArrayList<>();
        for(Block b : bc.getChain()){
            Certification c = (Certification) ObjectUtils.convertBase64ToObject(b.getData());
            lst.add(c);
        }
        return lst;
    }

    public List<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        //get Users
        for (Certification certification : ledger) {
            if (!users.contains(certification.getFrom())) {
                users.add(certification.getFrom());
            }
            if (!users.contains(certification.getTo())) {
                users.add(certification.getTo());
            }
        }
        //get ampunt of users
        ArrayList<String> User = new ArrayList<>();
        for (String user : users) {
            User.add( String.format(user));
        }
        return User;
    }

    
}
