
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package curriculumMaximus.core;

import utils.SecurityUtils;
import java.awt.RenderingHints.Key;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;

/**
 *
 * @author Gui and Rodrigo
 */
/**
 * A classe User representa um utilizador com chaves de criptografia pública,
 * privada e simétrica. Esta classe permite gerar, guardar e carregar chaves de
 * forma segura, com suporte para encriptação e desencriptação das chaves
 * privadas e simétricas usando uma password.
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
     * Construtor da classe User. Inicializa o utilizador com o nome fornecido e
     * define as chaves pública, privada e simétrica como null.
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
     * Construtor da classe User. Inicializa o utilizador com o nome "noName" e
     * define as chaves pública, privada e simétrica como null.
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
     * Gera um par de chaves (pública e privada) usando o algoritmo ECC e uma
     * chave simétrica AES, com tamanho de 256 bits.
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
     * Guarda a chave privada e a chave simétrica encriptadas, assim como a
     * chave pública sem encriptação, no sistema de ficheiros.
     *
     * @param password A password utilizada para encriptar as chaves privada e
     * simétrica.
     * @throws Exception Caso ocorra um erro ao guardar as chaves.
     */
    public void save(String password) throws Exception {
        // Encriptar a chave privada
        byte[] secret = SecurityUtils.encrypt(priv.getEncoded(), password);
        Path privPath = Path.of(this.nome + ".priv");
        Files.write(privPath, secret);
        System.out.println("Chave privada salva em: " + privPath.toString());

        // Encriptar a chave simétrica
        byte[] simData = SecurityUtils.encrypt(sim.getEncoded(), password);
        Path simPath = Path.of(this.nome + ".sim");
        Files.write(simPath, simData);
        System.out.println("Chave simétrica salva em: " + simPath.toString());

        // Guardar a chave pública
        Path pubPath = Path.of(this.nome + ".pub");
        Files.write(pubPath, pub.getEncoded());
        System.out.println("Chave pública salva em: " + pubPath.toString());
    }

    /**
     * Carrega e desencripta a chave privada e a chave simétrica a partir de
     * ficheiros, usando a password fornecida, e carrega a chave pública sem
     * encriptação.
     *
     * @param password A password utilizada para desencriptar as chaves.
     * @throws Exception Caso ocorra um erro ao carregar ou desencriptar as
     * chaves.
     */
public void load(String password) throws Exception {
    try {
        // Desencriptar a chave privada
        Path privPath = Path.of(this.nome + ".priv");
        if (Files.exists(privPath)) {
            byte[] privData = Files.readAllBytes(privPath);
            privData = SecurityUtils.decrypt(privData, password);
            this.priv = SecurityUtils.getPrivateKey(privData);
            System.out.println("Chave privada carregada para o usuário: " + this.nome);
        } else {
            throw new FileNotFoundException("Chave privada não encontrada para o usuário: " + this.nome);
        }

        // Desencriptar a chave simétrica
        Path simPath = Path.of(this.nome + ".sim");
        if (Files.exists(simPath)) {
            byte[] simData = Files.readAllBytes(simPath);
            simData = SecurityUtils.decrypt(simData, password);
            this.sim = (SecretKey) SecurityUtils.getAESKey(simData);
            System.out.println("Chave simétrica carregada para o usuário: " + this.nome);
        } else {
            throw new FileNotFoundException("Chave simétrica não encontrada para o usuário: " + this.nome);
        }

        // Carregar a chave pública
        Path pubPath = Path.of(this.nome + ".pub");
        if (Files.exists(pubPath)) {
            byte[] pubData = Files.readAllBytes(pubPath);
            this.pub = SecurityUtils.getPublicKey(pubData);
            System.out.println("Chave pública carregada para o usuário: " + this.nome);
        } else {
            throw new FileNotFoundException("Chave pública não encontrada para o usuário: " + this.nome);
        }
    } catch (Exception e) {
        throw new Exception("Erro ao carregar usuário: " + e.getMessage());
    }
}

    /**
     * Carrega a chave pública do utilizador a partir de um ficheiro, sem
     * encriptação.
     *
     * @throws Exception Caso ocorra um erro ao carregar a chave pública.
     */
    public void loadPublic() throws Exception {
        try {
            Path pubPath = Path.of(this.nome + ".pub");
            if (Files.exists(pubPath)) {
                byte[] pubData = Files.readAllBytes(pubPath);
                this.pub = SecurityUtils.getPublicKey(pubData);
                System.out.println("Chave pública carregada para o usuário: " + this.nome);
            } else {
                throw new FileNotFoundException("Chave pública não encontrada para o usuário: " + this.nome);
            }
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
