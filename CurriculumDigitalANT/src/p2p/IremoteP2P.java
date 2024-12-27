//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
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
package p2p;

import blockchain.utils.Block;
import blockchain.utils.BlockChain;
import curriculumMaximus.core.Certification;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;

/**
 * Created on 27/11/2024, 17:42:15
 *
 * @author manso - computer
 */
public interface IremoteP2P extends Remote {

    //:::: N E T WO R K  :::::::::::
    public String getAdress() throws RemoteException;

    public void addNode(IremoteP2P node) throws RemoteException;

    public List<IremoteP2P> getNetwork() throws RemoteException;

    //::::::::::: T R A N S A C T IO N S  :::::::::::
    public int getTransactionsSize() throws RemoteException;

    public void addTransaction(Certification certification) throws RemoteException;

    public List<String> getTransactions() throws RemoteException;
    
    List<String> getUserTransactions(String userName) throws RemoteException;
    
    List<String> getUserTransactionsInstitution(String userName, String instName) throws RemoteException;
    
    public void removeTransactions(List<String> transactions) throws RemoteException;

    public void synchronizeTransactions(IremoteP2P node) throws RemoteException;

    //::::::::::::::::: M I N E R :::::::::::::::::::::::::::::::::::::::::::
    public void startMining(String msg, int zeros) throws RemoteException;

    public void stopMining(int nonce) throws RemoteException;

    public boolean isMining() throws RemoteException;

    public int mine(String msg, int zeros) throws RemoteException;

    //::::::::::::::::: B L O C K C H A I N :::::::::::::::::::::::::::::::::::::::::::
    public void addBlock(Block b) throws RemoteException;

    public int getBlockchainSize() throws RemoteException;

    public String getBlockchainLastHash() throws RemoteException;
    
    public BlockChain getBlockchain() throws RemoteException;

    public void synchnonizeBlockchain() throws RemoteException;
    
    public List<String> getBlockchainTransactions()throws RemoteException;
    
    //::::::::::::: U S E R  M A N A G E M E N T :::::::::::::::::::
    void registerUser(String nome, PublicKey publicKey, String password) throws RemoteException;
    void registerInstituicao(String nome, PublicKey publicKey, String password) throws RemoteException;
    boolean loginInstituicao(String nome, String password) throws RemoteException;
    boolean loginUtilizador(String nome, String password) throws RemoteException;
    
    
    // Métodos de sincronização
    Map<String, String> getUtilizadores() throws RemoteException;
    Map<String, String> getInstituicoes() throws RemoteException;
    Map<String, PublicKey> getPublicKeys() throws RemoteException;
    void synchronizeData(IremoteP2P node) throws RemoteException;

}
