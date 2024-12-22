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
//::                                                               (c)2024   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 27/11/2024, 14:14:13
 *
 * @author manso - computer
 */
public class Miner {

    public static void main(String[] args) {
        String msg = "Transaction 7";
        Miner miner = new Miner();
        int n = miner.mine(msg, 4);
        System.out.println("Message = " + msg);
        System.out.println("Hash = " + getHash(msg + n));

    }

    public int mine(String msg, int dificulty) {
        try {
            //shared objects
            AtomicInteger nonce = new AtomicInteger(0); //nonce of message
            AtomicInteger ticket = new AtomicInteger(0); // tickets to numbers
            MinerThr thr[] = new MinerThr[Runtime.getRuntime().availableProcessors()];
            for (int i = 0; i < thr.length; i++) {
                thr[i] = new MinerThr(nonce, ticket, dificulty, msg);
                thr[i].start();
            }
            thr[0].join();
            return nonce.get();
        } catch (InterruptedException ex) {
            return 0;
        }

    }

    private static class MinerThr extends Thread {

        AtomicInteger trueNonce; //nounce found
        AtomicInteger numberTicket; // number to test
        int dificulty; // number of zeros
        String message; // message to mine

        public MinerThr(AtomicInteger nonce, AtomicInteger ticket, int dificulty, String msg) {
            this.trueNonce = nonce;
            this.numberTicket = ticket;
            this.dificulty = dificulty;
            this.message = msg;
        }

        @Override
        public void run() {
            //zeros to find in hash
            String zeros = String.format("%0" + dificulty + "d", 0);
            //nounce not found
            while (trueNonce.get() == 0) {
                //get a number to test
                int n = numberTicket.getAndIncrement();
                //calculate hash with nonce
                String hash = getHash(message + n);
                //starts with zeros
                if (hash.startsWith(zeros)) {
                    System.out.println(Thread.currentThread().getName() + " found nonce " + n);
                    System.out.println(message + " " + hash);
                    //update true nonce
                    trueNonce.set(n);
                }
            }
        }

    }

    public static String getHash(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            md.update(msg.getBytes());
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            return "ERROR";
        }
    }

}
