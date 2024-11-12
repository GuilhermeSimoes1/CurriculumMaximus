/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

import java.util.concurrent.atomic.AtomicInteger;

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
        AtomicInteger ticketNonce;
        AtomicInteger truenonce;
        String data;
        int dificulty;

    public Miner(AtomicInteger nonce, AtomicInteger truenonce, String data, int dificulty) {
            this.ticketNonce = nonce;
            this.truenonce = truenonce;
            this.data = data;
            this.dificulty = dificulty;
    }

     @Override
        public void run() {
            //String of zeros
            String zeros = String.format("%0" + dificulty + "d", 0);
            //starting nonce

            while (truenonce.get() == 0) {
                //calculate hash of block
                int nonce = ticketNonce.getAndIncrement();
                String hash = Hash.getHash(nonce + data);
                //DEBUG .... DEBUG .... DEBUG .... DEBUG .... DEBUG .... DEBUG
                //System.out.println(nonce + " " + hash);
                //Nounce found
                if (hash.endsWith(zeros)) {
                    truenonce.set(nonce);
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
    public static double GetNonce(String data, int dif) {
        int procs = Runtime.getRuntime().availableProcessors();
        Miner thr[]  = new Miner[procs];
        AtomicInteger shNonce = new AtomicInteger(0);
        AtomicInteger trNonce = new AtomicInteger(0);
        
        for(int i = 0; i < procs; i++){
            thr[i] = new Miner(shNonce,trNonce,data,dif);
            thr[i].start();
        }
        
        try{
            
            thr[0].join();
        
        }catch (InterruptedException ex){
                
            System.out.println("Erro");
                  
        }
        
        return trNonce.get();
          
    }
    
    public static void main(String[] args) {
        String texto = "OLa";
        System.out.println(""+ GetNonce(texto,5));
        
        long start = System.currentTimeMillis();
        GetNonce(texto,5);
        long end = System.currentTimeMillis();
        
        long MinerAcel = end - start;
        
        System.out.println(""+ MinerAcel);
        
    }
    
}
