/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

/**
 *
 * @author Gui and Rodrigo
 */
/**
 * A classe Miner é responsável por realizar a mineração de um bloco, isto é,
 * encontrar o valor de nonce que, juntamente com os dados fornecidos, gera um
 * hash que cumpre o critério de dificuldade especificado. O processo de
 * mineração é realizado através de um algoritmo de Proof of Work (PoW).
 */
public class Miner extends Thread {

    /**
     * Valor máximo do nonce, definido como 1 bilhão.
     */
    public static int Max_Nonce = (int) 1E9;
    String data;
    int difficulty;
    int startNonce;
    int endNonce;
    static volatile boolean found = false;  // Controla a parada das threads
    static String resultHash;
    static int resultNonce;

    public Miner(int startNonce, int endNonce) {
        this.startNonce = startNonce;
        this.endNonce = endNonce;
        this.found = false;
    }

     @Override
    public void run() {
        String zeros = String.format("%0" + difficulty + "d", 0);

        for (int nonce = startNonce; nonce <= endNonce && !found; nonce++) {
            String hash = Hash.getHash(nonce + data);

            if (hash.endsWith(zeros)) {
                synchronized (Miner.class) {
                    if (!found) {
                        found = true;
                        resultHash = hash;
                        resultNonce = nonce;
                        System.out.println("Nonce encontrado: " + nonce);
                    }
                }
                break;
            }
        }
    }

    /**
     * Método que encontra o valor de nonce que, quando combinado com os dados
     * fornecidos, gera um hash que termina com um número de zeros equivalente à
     * dificuldade.
     *
     * @param data Dados (evento) a serem incluídos no processo de mineração.
     * @param dificulty O nível de dificuldade da mineração, que determina o
     * número de zeros com os quais o hash resultante deve terminar.
     * @return O valor de nonce que gera um hash válido de acordo com a
     * dificuldade.
     */
    public static int Nonce() {
        int procs = Runtime.getRuntime().availableProcessors();
        int range = Max_Nonce/procs;
        
        Miner miner[] = new Miner[procs];
        
        for(int i = 0; i<miner.length; i++){
            int startNonce = range * i;
            int endNonce = range * (i+1);
            miner[i] = new Miner(startNonce,endNonce);
            miner[i].start();
        }
        
        // Não chamamos join(), pois queremos que o método termine assim que `found` for `true`
        while (!found) {
            // Loop vazio para aguardar até que `found` seja `true`
        }
        
        

        return resultNonce;
    }
}
