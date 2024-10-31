/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22/08/2022, 10:09:17
 *
 * @author IPT - computer
 * @version 1.0
 */
/**
 * A classe BlockChain representa uma cadeia de blocos, onde cada bloco contém dados e uma referência
 * à hash do bloco anterior. Esta implementação permite a adição de blocos, validação da cadeia e
 * operações de leitura/escrita para persistência dos dados.
 * A classe implementa Serializable, permitindo a serialização da cadeia de blocos.
 */
public class BlockChain implements Serializable {

    ArrayList<Block> chain = new ArrayList<>();

    /**
     * Obtém a hash do último bloco da cadeia.
     * Se a cadeia estiver vazia, retorna a hash do bloco génesis.
     *
     * @return A hash do último bloco da cadeia, ou "00000000" se a cadeia estiver vazia.
     */
    public String getLastBlockHash() {
        // Bloco génesis
        if (chain.isEmpty()) {
            return String.format("%08d", 0);
        }
        // Hash do último bloco na lista
        return chain.get(chain.size() - 1).currentHash;
    }

    /**
     * Adiciona um novo bloco à cadeia com os dados fornecidos e um nível de dificuldade especificado.
     * O bloco é minado utilizando o mecanismo de Proof of Work (POW).
     *
     * @param data Dados a serem incluídos no novo bloco.
     * @param dificulty Dificuldade para os mineradores encontrarem a hash válida (POW).
     */
    public void add(String data, int dificulty) {
        // Hash do bloco anterior
        String prevHash = getLastBlockHash();
        // Mineração do bloco
        int nonce = Miner.getNonce(prevHash + data, dificulty);
        // Criação do novo bloco
        Block newBlock = new Block(prevHash, data, nonce);
        // Adiciona o novo bloco à cadeia
        chain.add(newBlock);
    }

    /**
     * Obtém o bloco num índice específico da cadeia.
     *
     * @param index Índice do bloco na cadeia.
     * @return O bloco no índice especificado.
     */
    public Block get(int index) {
        return chain.get(index);
    }

    /**
     * Retorna uma representação textual da cadeia de blocos, incluindo o tamanho da cadeia e a
     * descrição de cada bloco.
     *
     * @return String representando a cadeia de blocos.
     */
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("Blockchain size = " + chain.size() + "\n");
        for (Block block : chain) {
            txt.append(block.toString() + "\n");
        }
        return txt.toString();
    }

    /**
     * Obtém a lista de blocos da cadeia.
     *
     * @return Lista de blocos que compõem a cadeia.
     */
    public List<Block> getChain() {
        return chain;
    }

    /**
     * Guarda a cadeia de blocos num ficheiro especificado.
     *
     * @param fileName Nome do ficheiro onde a cadeia será guardada.
     * @throws Exception Se ocorrer um erro ao tentar guardar o ficheiro.
     */
    public void save(String fileName) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(chain);
        }
    }

    /**
     * Carrega uma cadeia de blocos a partir de um ficheiro especificado.
     *
     * @param fileName Nome do ficheiro de onde a cadeia será carregada.
     * @throws Exception Se ocorrer um erro ao tentar carregar o ficheiro.
     */
    public void load(String fileName) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            this.chain = (ArrayList<Block>) in.readObject();
        }
    }

    /**
     * Valida a cadeia de blocos, verificando a integridade de cada bloco e a consistência das ligações
     * entre eles (hashes).
     *
     * @return true se a cadeia for válida, false caso contrário.
     */
    public boolean isValid() {
        // Valida os blocos
        for (Block block : chain) {
            if (!block.isValid()) {
                return false;
            }
        }
        // Valida as ligações entre blocos (começa no segundo bloco)
        for (int i = 1; i < chain.size(); i++) {
            // A hash anterior deve ser igual à hash do bloco anterior
            if (!chain.get(i).previousHash.equals(chain.get(i - 1).currentHash)) {
                return false;
            }
        }
        return true;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202208221009L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  ::::::::::::::::::::
    ////////////////////////////////////////////////////////////////////////////
}