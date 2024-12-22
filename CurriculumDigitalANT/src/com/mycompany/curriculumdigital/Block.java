//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2022   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package com.mycompany.curriculumdigital;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import com.mycompany.curriculumdigital.Miner;

/**
 * Created on 22/08/2022, 09:23:49
 *
 * Block with consensus of Proof of Work
 *
 * @author IPT - computer
 * @version 1.0
 */
public class Block implements Serializable, Comparable<Block> {

    String previousHash; // link to previous block
    String merkleRoot;   // merkleRoot in the block
    List<String> transactions; // transações do bloco (devem ser guardadas em separado)
    int nonce;           // proof of work 
    String currentHash;  // Hash of block

    public Block(String previousHash, List<String> transactions) {
        this.previousHash = previousHash;
        this.transactions = transactions;
        MerkleTree mkt = new MerkleTree(transactions);
        this.merkleRoot = mkt.getRoot();
    }

    public void setNonce(int nonce, int zeros) throws Exception {
        this.nonce = nonce;
        //calcular o hash
        this.currentHash = calculateHash();
        //calcular o prefixo
        String prefix = String.format("%0" + zeros + "d", 0);
        if (!currentHash.startsWith(prefix)) {
            throw new Exception(nonce + " not valid Hash=" + currentHash);
        }
        
    }

    public String getMinerData() {
        return previousHash + merkleRoot;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public List<String> transactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public int getNonce() {
        return nonce;
    }

    public String calculateHash() {
        return Miner.getHash(getMinerData(), nonce);
    }

    public String getCurrentHash() {
        return currentHash;
    }

    @Override
    public String toString() {
        return // (isValid() ? "OK\t" : "ERROR\t")+
                String.format("[ %8s", previousHash) + " <- "
                + String.format("%-10s", merkleRoot) + String.format(" %7d ] = ", nonce)
                + String.format("%8s", currentHash);

    }

    public String getHeaderString() {
        return    "prev Hash: " + previousHash +
                "\nMkt Root : " + merkleRoot+
                "\nnonce    : " + nonce+
                "\ncurr Hash: " + currentHash;
    }
    public String getTransactionsString() {
        StringBuilder txt = new StringBuilder();
        for (String transaction : transactions) {
            txt.append(transaction+"\n");
        }
        return txt.toString();
    }

    public boolean isValid() {
        return currentHash.equals(calculateHash());
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202208220923L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Block other = (Block) obj;
        if (this.nonce != other.nonce) {
            return false;
        }
        if (!Objects.equals(this.previousHash, other.previousHash)) {
            return false;
        }
        if (!Objects.equals(this.merkleRoot, other.merkleRoot)) {
            return false;
        }
        return Objects.equals(this.currentHash, other.currentHash);
    }

    
    @Override
    public int compareTo(Block o) {
        return this.currentHash.compareTo(o.currentHash);
    }

}
