/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package curriculumMaximus.core;

import utils.SecurityUtils;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


/**
 *
 * @author Gui and Rodrigo
 */
/**
 * A classe Certification representa uma certificação de um evento entre dois
 * utilizadores. Cada certificação contém a informação do remetente e
 * destinatário, as chaves públicas dos utilizadores e uma assinatura digital do
 * evento. A assinatura é gerada pelo utilizador remetente e pode ser verificada
 * para garantir a autenticidade da certificação. Esta classe implementa
 * Serializable, permitindo a serialização de certificações.
 */
public class Certification implements Serializable {

    private String from;
    private String to;
    private String fromPub;
    private String toPub;
    private String evento;
    private String signature;

    /**
     * Construtor da classe Certification que inicializa a certificação com as
     * informações do remetente e destinatário, bem como o evento certificado. A
     * assinatura digital é gerada automaticamente.
     *
     * @param from Utilizador remetente da certificação.
     * @param to Utilizador destinatário da certificação.
     * @param evento Descrição do evento que está a ser certificado.
     * @throws Exception Se ocorrer um erro durante a criação da assinatura.
     */
    public Certification(User from, User to, String evento) throws Exception {
        if (from.getPub() == null) {
            throw new NullPointerException("Chave pública do remetente não carregada: " + from.getName());
        }
        if (to.getPub() == null) {
            throw new NullPointerException("Chave pública do destinatário não carregada: " + to.getName());
        }
        this.from = from.getName();
        this.fromPub = Base64.getEncoder().encodeToString(from.getPub().getEncoded());
        this.to = to.getName();
        this.toPub = Base64.getEncoder().encodeToString(to.getPub().getEncoded());
        this.evento = evento;
        sign(from.getPriv());
        System.out.println("Certificação criada com sucesso: " + this.toString());
    }

    /**
     * Gera a assinatura digital para o evento usando a chave privada do
     * remetente.
     *
     * @param priv Chave privada do remetente usada para assinar o evento.
     * @throws Exception Se ocorrer um erro durante o processo de assinatura.
     */
    public void sign(PrivateKey priv) throws Exception {
        byte[] dataSign = SecurityUtils.sign(
                (fromPub + toPub + evento).getBytes(),
                priv);
        this.signature = Base64.getEncoder().encodeToString(dataSign);
    }

    /**
     * Verifica se a assinatura digital da certificação é válida, garantindo que
     * o conteúdo não foi alterado e que foi assinado corretamente.
     *
     * @return true se a certificação for válida, false caso contrário.
     */
    public boolean isValid() {
        try {
            PublicKey pub = SecurityUtils.getPublicKey(Base64.getDecoder().decode(fromPub));
            byte[] data = (fromPub + toPub + evento).getBytes();
            byte[] sign = Base64.getDecoder().decode(signature);
            return SecurityUtils.verifySign(data, sign, pub);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Obtém o evento certificado.
     *
     * @return O evento certificado.
     */
    public String getEvent() {
        return evento;
    }

    /**
     * Define o evento certificado.
     *
     * @param evento Novo evento a ser certificado.
     */
    public void setValue(String evento) {
        this.evento = evento;
    }

    /**
     * Obtém o nome do remetente da certificação.
     *
     * @return Nome do remetente.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Define o nome do remetente da certificação.
     *
     * @param from Novo nome do remetente.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Obtém o nome do destinatário da certificação.
     *
     * @return Nome do destinatário.
     */
    public String getTo() {
        return to;
    }

    /**
     * Define o nome do destinatário da certificação.
     *
     * @param to Novo nome do destinatário.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Retorna uma representação textual da certificação, incluindo o remetente,
     * evento, se a assinatura é válida, e o destinatário.
     *
     * @return String que representa a certificação.
     */
    @Override
    public String toString() {
        return String.format("%-10s -> %s %s -> %s", from, evento, isValid() + "", to);
    }

    /**
     * Obtém a assinatura digital da certificação.
     *
     * @return A assinatura digital em formato Base64.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Define a assinatura digital da certificação.
     *
     * @param signature Nova assinatura digital em formato Base64.
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Obtém a chave pública do remetente da certificação.
     *
     * @return A chave pública do remetente em formato Base64.
     */
    public String getFromPub() {
        return fromPub;
    }

    /**
     * Define a chave pública do remetente da certificação.
     *
     * @param fromPub Nova chave pública do remetente em formato Base64.
     */
    public void setFromPub(String fromPub) {
        this.fromPub = fromPub;
    }

    /**
     * Obtém a chave pública do destinatário da certificação.
     *
     * @return A chave pública do destinatário em formato Base64.
     */
    public String getToPub() {
        return toPub;
    }

    /**
     * Define a chave pública do destinatário da certificação.
     *
     * @param toPub Nova chave pública do destinatário em formato Base64.
     */
    public void setToPub(String toPub) {
        this.toPub = toPub;
    }

    /**
     * Identificador da versão da classe para a serialização.
     */
    public static long serialVersionUID = 123;
}
