/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

import java.io.Serializable;

/**
 *
 * @author Gui and Rodrigo
 */


/**
 * Representa um bloco numa cadeia de blocos (blockchain).
 * Cada bloco contém uma referência à hash do bloco anterior, os dados armazenados, um nonce e a hash atual.
 * A classe implementa Serializable, permitindo que os blocos possam ser serializados.
 */
public class Block implements Serializable {

    // Atributos do bloco
    String previousHash;  // Hash do bloco anterior
    String data;          // Dados armazenados no bloco
    int nonce;            // Valor nonce utilizado na mineração para encontrar a hash
    String currentHash;   // Hash atual do bloco, calculada com base no conteúdo do bloco

    /**
     * Construtor da classe Block.
     * Inicializa os atributos de um bloco e calcula a hash do bloco atual.
     *
     * @param previousHash Hash do bloco anterior na cadeia.
     * @param data Dados armazenados no bloco atual.
     * @param nonce Valor nonce utilizado para encontrar a hash.
     */
    public Block(String previousHash, String data, int nonce) {
        this.previousHash = previousHash;
        this.data = data;
        this.nonce = nonce;
        this.currentHash = calculateHash();
    }

    /**
     * Obtém os dados armazenados no bloco.
     *
     * @return String com os dados do bloco.
     */
    public String getData() {
        return data;
    }

    /**
     * Obtém o valor do nonce.
     *
     * @return Inteiro que representa o nonce do bloco.
     */
    public int getNonce() {
        return nonce;
    }

    /**
     * Calcula a hash do bloco atual com base no nonce, na hash do bloco anterior e nos dados do bloco.
     *
     * @return String representando a hash gerada.
     */
    public String calculateHash() {
        return Hash.getHash(nonce + previousHash + data);
    }

    /**
     * Obtém a hash atual do bloco.
     *
     * @return String representando a hash atual do bloco.
     */
    public String getCurrentHash() {
        return currentHash;
    }

    /**
     * Obtém a hash do bloco anterior.
     *
     * @return String com a hash do bloco anterior.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * Representação textual do bloco.
     * Mostra a hash anterior, os dados, o nonce e a hash atual do bloco.
     *
     * @return String com a representação do bloco.
     */
    public String toString() {
        return String.format("[ %8s", previousHash) + " <- "
                + String.format("%-10s", data) + String.format(" %7d ] = ", nonce)
                + String.format("%8s", currentHash);
    }

    /**
     * Verifica se a hash atual do bloco é válida, comparando com a hash calculada.
     *
     * @return true se a hash atual for válida, false caso contrário.
     */
    public boolean isValid() {
        return currentHash.equals(calculateHash());
    }

}
