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
package blockchain.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Created on 13/09/2022, 11:42:56
 *
 * @author IPT - Ant0nio M@nso
 * @version 1.0
 * @param <T> type of elements
 */
public final class MerkleTreeBytes<T> implements Serializable {

    // merkle tree hashs
    List<List<byte[]>> hashTree;
    // elements of tree
    List<T> elements;

    /**
     * Builds a merkle tree with an array of data
     *
     * @param arrayOfData list of data
     */
    public MerkleTreeBytes(T[] arrayOfData) {
        this(Arrays.asList(arrayOfData));

    }

    /**
     * Builds a merkle tree with an list of data
     *
     * @param listOfData list of data
     */
    public MerkleTreeBytes(List<T> listOfData) {
        this(); //build lists
        //save data
        elements.addAll(listOfData);
        //calculate list of hash of elements
        List<byte[]> hashT = new ArrayList<>();
        for (T elem : listOfData) {
            //convert T to byte arrays
            //hash byte array
            hashT.add(getHashValue(objectToBytes(elem)));
        }
        //build merkle tree
        makeTree(hashT);
    }

    /**
     * Builds an empty merkle tree
     */
    public MerkleTreeBytes() {
        //build lists
        hashTree = new ArrayList<>();
        elements = new ArrayList<>();
    }

    /**
     * root of tree
     *
     * @return root of tree
     */
    public byte[] getRoot() {
        //top o list
        return hashTree.get(0).get(0);
    }

    /**
     * gets the merkle tree
     *
     * @return tree
     */
    public List<List<byte[]>> getMerkleTree() {
        return hashTree;
    }

    /**
     * gets the elements in the merkle tree
     *
     * @return elements
     */
    public List<T> getElements() {
        return elements;
    }

    /**
     * builds a merkle tree
     *
     * @param hashList list of hashs
     */
    public void makeTree(List<byte[]> hashList) {
        //add hashlist to the beginning of tree
        hashTree.add(0, hashList);
        //top of tree -> terminate
        if (hashList.size() <= 1) {
            return;
        }
        //new level
        List<byte[]> newLevel = new ArrayList<>();
        //iterate list 2 by 2
        for (int i = 0; i < hashList.size(); i += 2) {
            //first element
            byte[] data = hashList.get(i);
            //if have another element
            if (i + 1 < hashList.size()) {
                //concatenate element    
                data = concatenate(data, hashList.get(i + 1));
            }
            //calculate hash od the elements
            byte[] hash = getHashValue(data);
            //add hash to the new leval
            newLevel.add(hash);
        }
        //call the makeTree with new level
        makeTree(newLevel);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::::::      A D D   E L E M E N T        :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    /**
     * add an element to the merkle tree
     *
     * @param newData new data
     */
    public void add(T newData) {
        //if tree is empty
        if (hashTree.isEmpty()) {
            //create first level
            hashTree.add(new ArrayList<>());
        }
        //save data in elements list
        this.elements.add(newData);

        int level = hashTree.size() - 1;
        //convert new data to byte array
        //calculate hash o byte array
        //add hash of new data to the last level
        hashTree.get(level).add(getHashValue(objectToBytes(newData)));
        //until the top of tree
        while (level > 0) {
            //number of elementos in level
            int sizeOfLevel = hashTree.get(level).size();
            //last element            
            byte[] dataHash = hashTree.get(level).get(sizeOfLevel - 1);
            // if size is even - replace the top element             
            if (sizeOfLevel % 2 == 0) {
                //concatenate the two last hashs
                dataHash = concatenate(hashTree.get(level).get(sizeOfLevel - 2), dataHash);
                //REPLACE the element in top                 
                // top = level.size / 2
                hashTree.get(level - 1).set((sizeOfLevel - 1) / 2, getHashValue(dataHash));
            } //if size is odd and exists the level                           
            else if (hashTree.get(level - 1).size() < sizeOfLevel / 2.0) {
                //ADD the hash element in the top
                hashTree.get(level - 1).add((sizeOfLevel - 1) / 2, getHashValue(dataHash));
            } else {
                //REPLACE the hash element in the top
                hashTree.get(level - 1).set((sizeOfLevel - 1) / 2, getHashValue(dataHash));
            }
            //next level (top)
            level--;
        }

        //create a new level if necessary
        if (hashTree.get(0).size() > 1) { //las level has two elements
            //concatenate elements
            //calculate hash
            byte[] hash = getHashValue(concatenate(hashTree.get(0).get(0), hashTree.get(0).get(1)));
            //create list with the hash
            List<byte[]> lst = new ArrayList<>();
            lst.add(hash);
            //add list to the top
            hashTree.add(0, lst);
        }
    }

    /**
     * calculate the proff of the element
     *
     * @param data element
     * @return list of proofs
     */
    public List<byte[]> getProof(T data) {
        //list of proofs
        List<byte[]> proof = new ArrayList<>();
        //index of element
        int index = elements.indexOf(data);
        if (index < 0) { //element not found
            return proof; // empty proof
        }
        //calculate proof
        return getProof(index, hashTree.size() - 1, proof);
    }

    /**
     * calculate the proff of the element
     *
     * @param index index of element
     * @param level level of the tree
     * @param proof list of proofs
     * @return list of proofs
     */
    private List<byte[]> getProof(int index, int level, List<byte[]> proof) {
        //add  2 elements [ even index , odd index ]  
        if (level > 0) { // not the top
            if (index % 2 == 0) { // is even [ index , index+1]
                proof.add(hashTree.get(level).get(index));
                //if have elements in the right
                if (index + 1 < hashTree.get(level).size()) {
                    proof.add(hashTree.get(level).get(index + 1));
                }
            } else {// is odd [ index - 1 , index]
                proof.add(hashTree.get(level).get(index - 1));
                proof.add(hashTree.get(level).get(index));
            }
            //calculate top level
            return getProof(index / 2, level - 1, proof);
        } else {
            //add root of tree
            proof.add(getRoot());
            return proof;
        }
    }

    /**
     * verify the proof of an element
     *
     * @param data dara
     * @param proof list of proofs
     * @return true if the proof is valid
     */
    public static <T> boolean isProofValid(T data, List<byte[]> proof) {

        if (proof.isEmpty()
                || // proof is empty
                // hash of element is invalid
                !Arrays.equals(proof.get(0), getHashValue(objectToBytes(data)))) {
            return false;
        }
        //index in the proof
        int index = 0;
        //until the top
        while (index < proof.size() - 1) {
            // hash( proof[i] )  = proof[i+1]
            if (Arrays.equals(getHashValue(proof.get(index)), (proof.get(index + 1)))) {
                index++;
            }// hash( proof[i] )  = proof[i+2]
            else if (Arrays.equals(getHashValue(proof.get(index)), (proof.get(index + 2)))) {
                index++;
            }// hash( proof[i] + proof[i+1])  = proof[i+2]
            else if (Arrays.equals(getHashValue(concatenate(proof.get(index), proof.get(index + 1))), (proof.get(index + 2)))) {
                index += 2;
            }// hash( proof[i] + proof[i+1])  = proof[i+3]
            else if (Arrays.equals(getHashValue(concatenate(proof.get(index), proof.get(index + 1))), (proof.get(index + 3)))) {
                index += 2;
            } else {//corrupted
                return false;
            }
        }
        return true;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::   V A L I D A T E    T R E E  ::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    /**
     * verify if the merkle tree is valid
     *
     * @return valid merkle tree
     */
    public boolean isValid() {
        //verify the hash of elements  int the bottom of tree
        for (int i = 0; i < this.elements.size(); i++) {
            if (!Arrays.equals(getHashValue(objectToBytes(this.elements.get(i))), (hashTree.get(hashTree.size() - 1).get(i)))) {
                return false;
            }
        }
        //verify the levels of the tree
        for (int level = 0; level < hashTree.size() - 1; level++) {
            // verify level            
            for (int index = 0; index < hashTree.get(level).size(); index++) {
                //left leaf
                byte[] dataLeafs = hashTree.get(level + 1).get(index * 2);
                //if right leaf exists
                if (index * 2 + 1 < hashTree.get(level + 1).size()) {
                    //concatenate hashs
                    dataLeafs = concatenate(dataLeafs, hashTree.get(level + 1).get(index * 2 + 1));
                }
                //calculate hash of leafs
                byte[] hash = getHashValue(dataLeafs);
                //verify the hash leafs
                if (!Arrays.equals(hashTree.get(level).get(index), hash)) {
                    return false;
                }
            }
        }
        //all is ok
        return true;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        for (List<byte[]> list : hashTree) {
            txt.append(byteArrayListToHex(list)).append("\n");
        }
        for (T elem : elements) {
            txt.append(String.format("[%-8s]", elem.toString()));
        }
        return txt.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::   S A V E   /    L O A D      ::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    public void saveToFile(String fileName) throws FileNotFoundException, IOException {
        try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    public static MerkleTreeBytes loadFromFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        try ( ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (MerkleTreeBytes) in.readObject();
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::                                                           :::::::::
    //::::::                         U T I L S                         :::::::::
    //::::::                                                           :::::::::
    ///////////////////////////////////////////////////////////////////////////
  //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::   TO / FROM BASE 64 STRING    ::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
    public String toBase64() {
        return Base64.getEncoder().encodeToString(objectToBytes(this));
    }

    public static MerkleTreeBytes fromBase64(String b64Merkle) {
        return (MerkleTreeBytes) bytesToObject(Base64.getDecoder().decode(b64Merkle));
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::                                                           :::::::::
    //::::::                         U T I L S                         :::::::::
    //::::::                                                           :::::::::
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Calculates the hash value of data using Arrays.hashCode(data)
     *
     * @param data data
     * @return hash value
     */
    public static byte[] getHashValue(byte[] data) {
        return intToBytes(Arrays.hashCode(data));
    }

    /**
     * calculates the hexadecimal string of byte Array
     *
     * @param val byte array
     * @return hexadecimal string
     */
    public static String byteArrayToHex(byte[] val) {
        return new BigInteger(val).abs().toString(16).toUpperCase();
    }

    /**
     * calculate the hexadecimal string of an list of byte array elements
     *
     * @param list list of byte array elements
     * @return hexadecimal string
     */
    public static String byteArrayListToHex(List<byte[]> list) {
        StringBuilder txt = new StringBuilder();
        for (byte[] bs : list) {
            txt.append("[").append(String.format("%-8s", byteArrayToHex(bs))).append("]");
        }
        return txt.toString();
    }

    /**
     * convert integer to byte array
     *
     * @param x integer value
     * @return byte array
     */
    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    /**
     * concatenate two byte arrays
     *
     * @param array1 first byte array
     * @param array2 second byte array
     * @return array1 + array2
     */
    public static byte[] concatenate(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    /**
     * converts an object to byte array
     *
     * @param object
     * @return byte array
     */
    public static byte[] objectToBytes(Object object) {
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream();  ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        } catch (Exception e) { // not seralizable
            return new byte[]{0};
        }
    }

    /**
     * converts an byte array to byte Array
     *
     * @param data
     * @return byte array
     */
    public static Object bytesToObject(byte[] data) {
        try ( ByteArrayInputStream bin = new ByteArrayInputStream(data);  ObjectInputStream in = new ObjectInputStream(bin)) {
            return in.readObject();
        } catch (Exception e) { // not seralizable
            return null;
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202209131142L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
