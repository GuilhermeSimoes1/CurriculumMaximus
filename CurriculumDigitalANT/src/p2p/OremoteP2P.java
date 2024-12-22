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
package p2p;

import com.mycompany.curriculumdigital.Block;
import com.mycompany.curriculumdigital.BlockChain;
import com.mycompany.curriculumdigital.Certification;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import com.mycompany.curriculumdigital.Miner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import utils.RMI;

/**
 * Created on 27/11/2024, 17:48:32
 *
 * @author manso - computer
 */
public class OremoteP2P extends UnicastRemoteObject implements IremoteP2P {

    final static String BLOCHAIN_FILENAME = "blockchain.obj";
    private static final String USERS_FILE = "users.dat";
    private static final String INSTITUTIONS_FILE = "institutions.dat";
    private static final String PUBLIC_KEYS_FILE = "publicKeys.dat";

    String address;
    CopyOnWriteArrayList<IremoteP2P> network;
    CopyOnWriteArraySet<String> transactions;
    P2Plistener p2pListener;
    Miner myMiner;
    BlockChain myBlockchain;

    private Map<String, PublicKey> publicKeys = new HashMap<>();
    private Map<String, String> instituicoes = new HashMap<>();
    private Map<String, String> utilizadores = new HashMap<>();

    public OremoteP2P(String address, P2Plistener listener) throws RemoteException {
        super(RMI.getAdressPort(address));
        this.address = address;
        this.network = new CopyOnWriteArrayList<>();
        transactions = new CopyOnWriteArraySet<>();
        this.myMiner = new Miner(listener);
        this.myBlockchain = new BlockChain(BLOCHAIN_FILENAME);
        this.p2pListener = listener;

        listener.onStartRemote("Object " + address + " listening");

        try {
            loadData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oosUsers = new ObjectOutputStream(new FileOutputStream(USERS_FILE)); ObjectOutputStream oosInstitutions = new ObjectOutputStream(new FileOutputStream(INSTITUTIONS_FILE)); ObjectOutputStream oosPublicKeys = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEYS_FILE))) {
            oosUsers.writeObject(utilizadores);
            oosInstitutions.writeObject(instituicoes);
            oosPublicKeys.writeObject(publicKeys);
        }
    }

    private void loadData() throws IOException, ClassNotFoundException {
        try (ObjectInputStream oisUsers = new ObjectInputStream(new FileInputStream(USERS_FILE)); ObjectInputStream oisInstitutions = new ObjectInputStream(new FileInputStream(INSTITUTIONS_FILE)); ObjectInputStream oisPublicKeys = new ObjectInputStream(new FileInputStream(PUBLIC_KEYS_FILE))) {
            utilizadores = (Map<String, String>) oisUsers.readObject();
            instituicoes = (Map<String, String>) oisInstitutions.readObject();
            publicKeys = (Map<String, PublicKey>) oisPublicKeys.readObject();
        } catch (FileNotFoundException e) {
            utilizadores = new HashMap<>();
            instituicoes = new HashMap<>();
            publicKeys = new HashMap<>();
        }
    }

    @Override
    public String getAdress() throws RemoteException {
        return address;
    }

    /**
     * Método que verifica se um no está na rede e elmina os que não responderem
     *
     * @param adress endereço do no
     * @return true se estiver na rede falso caso contrario
     */
    private boolean isInNetwork(String adress) {
        //fazer o acesso iterado pelo fim do array para remover os nos inativos
        for (int i = network.size() - 1; i >= 0; i--) {
            try {
                //se o no responder e o endereço for igaul
                if (network.get(i).getAdress().equals(adress)) {
                    // no esta na rede 
                    return true;
                }
            } catch (RemoteException ex) {
                //remover os nós que não respondem
                network.remove(i);
            }
        }
        return false;
    }

    @Override
    public void addNode(IremoteP2P node) throws RemoteException {
        //se já tiver o nó  ---  não faz nada
        if (isInNetwork(node.getAdress())) {
            return;
        }
        p2pListener.onMessage("Network addNode ", node.getAdress());
        //adicionar o no á rede
        network.add(node);

        p2pListener.onConect(node.getAdress());
        // pedir ao no para nos adicionar
        node.addNode(this);
        //propagar o no na rede
        for (IremoteP2P iremoteP2P : network) {
            iremoteP2P.addNode(node);
        }

        //sicronizar as transaçoes
        synchronizeTransactions(node);
        //sincronizar a blockchain
        synchnonizeBlockchain();
        synchronizeData(node);

    }

    @Override
    public List<IremoteP2P> getNetwork() throws RemoteException {
        return new ArrayList<>(network);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::            T R A N S A C T I O N S       ::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public int getTransactionsSize() throws RemoteException {
        return transactions.size();
    }

    public void addTransaction(Certification certification) throws RemoteException {
        String data = certification.toString();
        if (transactions.contains(data)) {
            p2pListener.onTransaction("Transação repetida: " + data);
            return;
        }
        transactions.add(data);
        for (IremoteP2P node : network) {
            node.addTransaction(certification);
        }
    }

    @Override
    public List<String> getTransactions() throws RemoteException {
        return new ArrayList<>(transactions);
    }

    @Override
    public void synchronizeTransactions(IremoteP2P node) throws RemoteException {
        //tamanho anterior
        int oldsize = transactions.size();
        p2pListener.onMessage("sinchronizeTransactions", node.getAdress());
        // juntar as transacoes todas (SET elimina as repetidas)
        this.transactions.addAll(node.getTransactions());
        int newSize = transactions.size();
        //se o tamanho for incrementado
        if (oldsize < newSize) {
            p2pListener.onMessage("sinchronizeTransactions", "tamanho diferente");
            //pedir ao no para sincronizar com as nossas
            node.synchronizeTransactions(this);
            p2pListener.onTransaction(address);
            p2pListener.onMessage("sinchronizeTransactions", "node.sinchronizeTransactions(this)");
            //pedir á rede para se sincronizar
            for (IremoteP2P iremoteP2P : network) {
                //se o tamanho for menor
                if (iremoteP2P.getTransactionsSize() < newSize) {
                    //cincronizar-se com o no actual
                    p2pListener.onMessage("sinchronizeTransactions", " iremoteP2P.sinchronizeTransactions(this)");
                    iremoteP2P.synchronizeTransactions(this);
                }
            }
        }

    }

    @Override
    public void removeTransactions(List<String> myTransactions) throws RemoteException {
        //remover as transações da lista atural
        transactions.removeAll(myTransactions);
        p2pListener.onTransaction("remove " + myTransactions.size() + "transactions");
        //propagar as remoções
        for (IremoteP2P iremoteP2P : network) {
            //se houver algum elemento em comum nas transações remotas
            if (iremoteP2P.getTransactions().retainAll(transactions)) {
                //remover as transaçoies
                iremoteP2P.removeTransactions(myTransactions);
            }
        }

    }
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::      M I N E R   :::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //////////////////////////////////////////////////////////////////////////////

    @Override
    public void startMining(String msg, int zeros) throws RemoteException {
        try {
            //colocar a mineiro a minar
            myMiner.startMining(msg, zeros);
            p2pListener.onStartMining(msg, zeros);
            //mandar minar a rede
            for (IremoteP2P iremoteP2P : network) {
                //se o nodo nao estiver a minar
                if (!iremoteP2P.isMining()) {
                    p2pListener.onStartMining(iremoteP2P.getAdress() + " mining", zeros);
                    //iniciar a mineracao no nodo
                    iremoteP2P.startMining(msg, zeros);
                }
            }
        } catch (Exception ex) {
            p2pListener.onException(ex, "startMining");
        }

    }

    @Override
    public void stopMining(int nonce) throws RemoteException {
        //parar o mineiro e distribuir o nonce
        myMiner.stopMining(nonce);
        //mandar parar a rede
        for (IremoteP2P iremoteP2P : network) {
            //se o nodo estiver a minar   
            if (iremoteP2P.isMining()) {
                //parar a mineração no nodo 
                iremoteP2P.stopMining(nonce);
            }
        }
    }

    @Override
    public int mine(String msg, int zeros) throws RemoteException {
        try {
            //começar a minar a mensagem
            startMining(msg, zeros);
            //esperar que o nonce seja calculado
            return myMiner.waitToNonce();
        } catch (InterruptedException ex) {
            p2pListener.onException(ex, "Mine");
            return -1;
        }

    }

    @Override
    public boolean isMining() throws RemoteException {
        return myMiner.isMining();
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::: B L O C K C H A I N :::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //////////////////////////////////////////////////////////////////////////////
    @Override
    public void addBlock(Block b) throws RemoteException {
        try {
            //se não for válido
            if (!b.isValid()) {
                throw new RemoteException("invalid block");
            }
            //se encaixar adicionar o bloco
            if (myBlockchain.getLastBlockHash().equals(b.getPreviousHash())) {
                myBlockchain.add(b);
                //guardar a blockchain
                myBlockchain.save(BLOCHAIN_FILENAME);
                p2pListener.onBlockchainUpdate(myBlockchain);
            }
            //propagar o bloco pela rede
            for (IremoteP2P iremoteP2P : network) {
                //se encaixar na blockcahin dos nodos remotos
                if (!iremoteP2P.getBlockchainLastHash().equals(b.getPreviousHash())
                        || //ou o tamanho da remota for menor
                        iremoteP2P.getBlockchainSize() < myBlockchain.getSize()) {
                    //adicionar o bloco ao nodo remoto
                    iremoteP2P.addBlock(b);
                }
            }
            //se não encaixou)
            if (!myBlockchain.getLastBlockHash().equals(b.getCurrentHash())) {
                //sincronizar a blockchain
                synchnonizeBlockchain();
            }
        } catch (Exception ex) {
            p2pListener.onException(ex, "Add bloco " + b);
        }
    }

    @Override
    public int getBlockchainSize() throws RemoteException {
        return myBlockchain.getSize();
    }

    @Override
    public String getBlockchainLastHash() throws RemoteException {
        return myBlockchain.getLastBlockHash();
    }

    @Override
    public BlockChain getBlockchain() throws RemoteException {
        return myBlockchain;
    }

    @Override
    public void synchnonizeBlockchain() throws RemoteException {
        //para todos os nodos da rede
        for (IremoteP2P iremoteP2P : network) {
            //se a blockchain for maior
            if (iremoteP2P.getBlockchainSize() > myBlockchain.getSize()) {
                BlockChain remote = iremoteP2P.getBlockchain();
                //e a blockchain for válida
                if (remote.isValid()) {
                    //atualizar toda a blockchain
                    myBlockchain = remote;
                    //deveria sincronizar apenas os blocos que faltam
                    p2pListener.onBlockchainUpdate(myBlockchain);
                }
            }
        }
    }

    @Override
    public List<String> getBlockchainTransactions() throws RemoteException {
        ArrayList<String> allTransactions = new ArrayList<>();
        for (Block b : myBlockchain.getChain()) {
            allTransactions.addAll(b.transactions());
        }
        return allTransactions;
    }

    @Override
    public void registerUser(String nome, PublicKey publicKey, String password) throws RemoteException {
        if (utilizadores.containsKey(nome)) {
            throw new RemoteException("Nome já registrado.");
        }
        publicKeys.put(nome, publicKey);
        utilizadores.put(nome, password);
        try {
            saveData();
            // Salvar a chave pública no sistema de arquivos
            Files.write(Path.of(nome + ".pub"), publicKey.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerInstituicao(String nome, PublicKey publicKey, String password) throws RemoteException {
        if (instituicoes.containsKey(nome)) {
            throw new RemoteException("Nome de instituição já registrado.");
        }
        publicKeys.put(nome, publicKey);
        instituicoes.put(nome, password);
        try {
            saveData();
            // Salvar a chave pública no sistema de arquivos
            Files.write(Path.of(nome + ".pub"), publicKey.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean loginInstituicao(String nome, String password) throws RemoteException {
        return instituicoes.containsKey(nome) && instituicoes.get(nome).equals(password);
    }

    @Override
    public boolean loginUtilizador(String nome, String password) throws RemoteException {
        return utilizadores.containsKey(nome) && utilizadores.get(nome).equals(password);
    }

    // Métodos de sincronização
    @Override
    public Map<String, String> getUtilizadores() throws RemoteException {
        return new HashMap<>(utilizadores);
    }

    @Override
    public Map<String, String> getInstituicoes() throws RemoteException {
        return new HashMap<>(instituicoes);
    }

    @Override
    public Map<String, PublicKey> getPublicKeys() throws RemoteException {
        return new HashMap<>(publicKeys);
    }

    @Override
    public void synchronizeData(IremoteP2P node) throws RemoteException {
        // Sincronizar utilizadores
        Map<String, String> remoteUtilizadores = node.getUtilizadores();
        utilizadores.putAll(remoteUtilizadores);

        // Sincronizar instituições
        Map<String, String> remoteInstituicoes = node.getInstituicoes();
        instituicoes.putAll(remoteInstituicoes);

        // Sincronizar chaves públicas
        Map<String, PublicKey> remotePublicKeys = node.getPublicKeys();
        publicKeys.putAll(remotePublicKeys);

        // Salvar dados sincronizados
        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
