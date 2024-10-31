/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

import java.awt.RenderingHints.Key;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;


/**
 *
 * @author Gui
 */

/**
 * A classe User representa um utilizador com chaves de criptografia pública, privada e simétrica.
 * Esta classe permite gerar, guardar e carregar chaves de forma segura, com suporte para 
 * encriptação e desencriptação das chaves privadas e simétricas usando uma password.
 */
public class User {

    /**
     * Nome do utilizador.
     */
    String nome;

    /**
     * Chave pública do utilizador.
     */
    private PublicKey pub;

    /**
     * Chave privada do utilizador.
     */
    private PrivateKey priv;

    /**
     * Chave simétrica (AES) do utilizador.
     */
    private SecretKey sim;

    /**
     * Construtor da classe User. Inicializa o utilizador com o nome fornecido e define as chaves
     * pública, privada e simétrica como null.
     * 
     * @param nome O nome do utilizador.
     */
    public User(String nome) {
        this.nome = nome;
        this.pub = null;
        this.priv = null;
        this.sim = null;
    }

    /**
     * Construtor da classe User. Inicializa o utilizador com o nome "noName" e define as chaves 
     * pública, privada e simétrica como null.
     * 
     * @throws Exception Caso ocorra um erro na inicialização do utilizador.
     */
    public User() throws Exception {
        this.nome = "noName";
        this.pub = null;
        this.priv = null;
        this.sim = null;
    }

    /**
     * Gera um par de chaves (pública e privada) usando o algoritmo ECC e uma chave simétrica AES,
     * com tamanho de 256 bits.
     * 
     * @throws Exception Caso ocorra um erro na geração das chaves.
     */
    public void generateKeys() throws Exception {
        this.sim = (SecretKey) SecurityUtils.generateAESKey(256);
        KeyPair kp = SecurityUtils.generateECKeyPair(256);
        this.pub = kp.getPublic();
        this.priv = kp.getPrivate();
    }

    /**
     * Guarda a chave privada e a chave simétrica encriptadas, assim como a chave pública sem 
     * encriptação, no sistema de ficheiros.
     * 
     * @param password A password utilizada para encriptar as chaves privada e simétrica.
     * @throws Exception Caso ocorra um erro ao guardar as chaves.
     */
    public void save(String password) throws Exception {
        // Encriptar a chave privada
        byte[] secret = SecurityUtils.encrypt(priv.getEncoded(), password);
        Files.write(Path.of(this.nome + ".priv"), secret);
        // Encriptar a chave simétrica
        byte[] simData = SecurityUtils.encrypt(sim.getEncoded(), password);
        Files.write(Path.of(this.nome + ".sim"), simData);
        // Guardar a chave pública
        Files.write(Path.of(this.nome + ".pub"), pub.getEncoded());
    }

    /**
     * Carrega e desencripta a chave privada e a chave simétrica a partir de ficheiros, usando a 
     * password fornecida, e carrega a chave pública sem encriptação.
     * 
     * @param password A password utilizada para desencriptar as chaves.
     * @throws Exception Caso ocorra um erro ao carregar ou desencriptar as chaves.
     */
    public void load(String password) throws Exception {
        // Desencriptar a chave privada
        byte[] privData = Files.readAllBytes(Path.of(this.nome + ".priv"));
        privData = SecurityUtils.decrypt(privData, password);
        // Desencriptar a chave simétrica
        byte[] simData = Files.readAllBytes(Path.of(this.nome + ".sim"));
        simData = SecurityUtils.decrypt(simData, password);
        // Carregar a chave pública
        byte[] pubData = Files.readAllBytes(Path.of(this.nome + ".pub"));
        this.priv = SecurityUtils.getPrivateKey(privData);
        this.pub = SecurityUtils.getPublicKey(pubData);
        this.sim = (SecretKey) SecurityUtils.getAESKey(simData);
    }

    /**
     * Carrega a chave pública do utilizador a partir de um ficheiro, sem encriptação.
     * 
     * @throws Exception Caso ocorra um erro ao carregar a chave pública.
     */
    public void loadPublic() throws Exception {
        try {
            byte[] pubData = Files.readAllBytes(Path.of(this.nome + ".pub"));
            this.pub = SecurityUtils.getPublicKey(pubData);
        } catch (Exception e) {
            throw new Exception("Erro ao carregar usuário: " + e.getMessage());
        }
    }

    /**
     * Retorna o nome do utilizador.
     * 
     * @return O nome do utilizador.
     */
    public String getName() {
        return nome;
    }

    /**
     * Define o nome do utilizador.
     * 
     * @param nome O novo nome do utilizador.
     */
    public void setName(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a chave pública do utilizador.
     * 
     * @return A chave pública.
     */
    public PublicKey getPub() {
        return pub;
    }

    /**
     * Define a chave pública do utilizador.
     * 
     * @param pub A nova chave pública.
     */
    public void setPub(PublicKey pub) {
        this.pub = pub;
    }

    /**
     * Retorna a chave privada do utilizador.
     * 
     * @return A chave privada.
     */
    public PrivateKey getPriv() {
        return priv;
    }

    /**
     * Define a chave privada do utilizador.
     * 
     * @param priv A nova chave privada.
     */
    public void setPriv(PrivateKey priv) {
        this.priv = priv;
    }

    /**
     * Retorna a chave simétrica do utilizador.
     * 
     * @return A chave simétrica.
     */
    public SecretKey getSim() {
        return sim;
    }

    /**
     * Define a chave simétrica do utilizador.
     * 
     * @param sim A nova chave simétrica.
     */
    public void setSim(SecretKey sim) {
        this.sim = sim;
    }
}
