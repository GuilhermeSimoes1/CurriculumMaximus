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

import blockchain.Block;
import blockchain.BlockChain;
import curriculumMaximus.core.Certification;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import blockchain.Miner;
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
    private static final String PENDING_TRANSACTIONS_FILE = "pendingTransactions.obj";
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

    /**
     * Construtor da classe OremoteP2P.
     *
     * <p>
     * Inicializa um nó da rede P2P com um endereço específico e um listener
     * para eventos. Este construtor também carrega dados previamente
     * armazenados, incluindo usuários, instituições e transações pendentes.
     * </p>
     *
     * @param address O endereço do nó no formato "host:porta".
     * @param listener O listener responsável por capturar eventos do nó P2P,
     * como mensagens ou atualizações da blockchain.
     *
     * @throws RemoteException Se ocorrer algum erro remoto durante a criação do
     * nó.
     */
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
            loadPendingTransactions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva os dados de usuários, instituições e chaves públicas no sistema de
     * arquivos.
     *
     * <p>
     * Este método utiliza streams de saída para gravar os mapas de dados em
     * arquivos correspondentes, garantindo a persistência das informações. Se
     * ocorrer algum erro durante a escrita nos arquivos, uma exceção
     * {@link IOException} será lançada.
     * </p>
     *
     * @throws IOException Se ocorrer um erro ao salvar os dados nos arquivos.
     */
    private void saveData() throws IOException {
        try (ObjectOutputStream oosUsers = new ObjectOutputStream(new FileOutputStream(USERS_FILE)); ObjectOutputStream oosInstitutions = new ObjectOutputStream(new FileOutputStream(INSTITUTIONS_FILE)); ObjectOutputStream oosPublicKeys = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEYS_FILE))) {
            oosUsers.writeObject(utilizadores);
            oosInstitutions.writeObject(instituicoes);
            oosPublicKeys.writeObject(publicKeys);
        }
    }

    /**
     * Carrega os dados de usuários, instituições e chaves públicas do sistema
     * de arquivos.
     *
     * <p>
     * Este método utiliza streams de entrada para ler os mapas de dados a
     * partir de arquivos previamente gravados. Caso algum dos arquivos não seja
     * encontrado, inicializa os mapas como vazios. Se ocorrer algum erro de
     * leitura ou deserialização, uma exceção será lançada.
     * </p>
     *
     * @throws IOException Se ocorrer um erro ao acessar ou ler os arquivos.
     * @throws ClassNotFoundException Se o formato do objeto nos arquivos não
     * for compatível.
     */
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

    /**
     * Carrega as transações pendentes do sistema de arquivos.
     *
     * <p>
     * Este método utiliza um stream de entrada para ler o conjunto de
     * transações pendentes a partir de um arquivo previamente gravado. Caso o
     * arquivo não seja encontrado, inicializa o conjunto de transações como
     * vazio.
     * </p>
     *
     * @throws IOException Se ocorrer um erro ao acessar ou ler o arquivo.
     * @throws ClassNotFoundException Se o formato do objeto no arquivo não for
     * compatível.
     */
    private void loadPendingTransactions() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PENDING_TRANSACTIONS_FILE))) {
            transactions.addAll((CopyOnWriteArraySet<String>) ois.readObject());
        } catch (FileNotFoundException e) {
            // Se o ficheiro não existir, inicialize o conjunto vazio
            transactions = new CopyOnWriteArraySet<>();
        }
    }

    /**
     * Salva as transações pendentes no sistema de arquivos.
     *
     * <p>
     * Este método utiliza um stream de saída para gravar o conjunto de
     * transações pendentes em um arquivo, garantindo a persistência das
     * informações.
     * </p>
     *
     * @throws IOException Se ocorrer um erro ao gravar os dados no arquivo.
     */
    private void savePendingTransactions() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PENDING_TRANSACTIONS_FILE))) {
            oos.writeObject(transactions);
        }
    }

    /**
     * Retorna o endereço do nó atual.
     *
     * <p>
     * O endereço é usado para identificar o nó na rede P2P.
     * </p>
     *
     * @return O endereço do nó como uma string no formato "host:porta".
     * @throws RemoteException Se ocorrer um erro remoto ao obter o endereço.
     */
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

    /**
     * Adiciona um nó à rede P2P.
     *
     * <p>
     * Este método verifica se o nó já está presente na rede. Caso contrário,
     * adiciona o nó à lista de nós conectados, propaga a informação para os
     * demais nós da rede, sincroniza as transações e a blockchain, e solicita a
     * sincronização de dados.
     * </p>
     *
     * @param node O nó a ser adicionado à rede.
     * @throws RemoteException Se ocorrer um erro remoto ao interagir com os
     * nós.
     */
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

    /**
     * Retorna a lista de nós conectados à rede P2P.
     *
     * <p>
     * A lista retornada é uma cópia da lista interna, garantindo que alterações
     * externas não afetem a lista original.
     * </p>
     *
     * @return Uma lista de nós conectados à rede.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar os nós.
     */
    @Override
    public List<IremoteP2P> getNetwork() throws RemoteException {
        return new ArrayList<>(network);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::            T R A N S A C T I O N S       ::::::::::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Retorna o número de transações pendentes.
     *
     * <p>
     * Este método obtém o tamanho do conjunto de transações pendentes
     * armazenado localmente.
     * </p>
     *
     * @return O número de transações pendentes.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar o conjunto
     * de transações.
     */
    public int getTransactionsSize() throws RemoteException {
        return transactions.size();
    }

    /**
     * Adiciona uma nova transação à rede P2P.
     *
     * <p>
     * Este método verifica se a transação já existe no conjunto de transações
     * pendentes. Caso contrário, adiciona a transação localmente, salva o
     * estado no arquivo de transações pendentes e propaga a transação para os
     * demais nós da rede.
     * </p>
     *
     * @param certification O objeto {@code Certification} representando a
     * transação a ser adicionada.
     * @throws RemoteException Se ocorrer um erro remoto ao adicionar a
     * transação.
     */
    @Override
    public void addTransaction(Certification certification) throws RemoteException {
        String data = certification.toString();
        if (transactions.contains(data)) {
            p2pListener.onTransaction("Transação repetida: " + data);
            return;
        }
        transactions.add(data);
        try {
            savePendingTransactions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (IremoteP2P node : network) {
            node.addTransaction(certification);
        }
    }

    /**
     * Retorna a lista de transações pendentes no nó atual.
     *
     * <p>
     * Este método cria uma cópia da coleção de transações pendentes armazenadas
     * localmente para evitar alterações externas na lista original.
     * </p>
     *
     * @return Uma lista de strings representando as transações pendentes.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar as
     * transações.
     */
    @Override
    public List<String> getTransactions() throws RemoteException {
        return new ArrayList<>(transactions);
    }

    /**
     * Retorna as transações relacionadas a um usuário específico.
     *
     * <p>
     * Este método busca todas as transações associadas ao nome do usuário na
     * blockchain local e retorna-as numa lista.
     * </p>
     *
     * @param userName O nome do usuário cujas transações serão buscadas.
     * @return Uma lista de strings representando as transações do usuário.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar as
     * transações.
     */
    @Override
    public List<String> getUserTransactions(String userName) throws RemoteException {
        List<String> userTransactions = new ArrayList<>();

        // Buscar transações na blockchain
        for (Block block : myBlockchain.getChain()) {
            for (String transaction : block.transactions()) {
                if (transaction.contains(userName)) {
                    userTransactions.add(transaction);
                }
            }
        }

        return userTransactions;
    }

    /**
     * Retorna as transações relacionadas a um usuário e uma instituição
     * específicos.
     *
     * <p>
     * Este método busca todas as transações que envolvem o nome do usuário e da
     * instituição na blockchain local e as retorna em uma lista.
     * </p>
     *
     * @param userName O nome do usuário cujas transações serão buscadas.
     * @param instName O nome da instituição relacionada às transações.
     * @return Uma lista de strings representando as transações do usuário para
     * a instituição.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar as
     * transações.
     */
    public List<String> getUserTransactionsInstitution(String userName, String instName) throws RemoteException {
        List<String> userTransactionsInst = new ArrayList<>();

        // Buscar transações na blockchain
        for (Block block : myBlockchain.getChain()) {
            for (String transaction : block.transactions()) {
                if (transaction.contains(userName) && transaction.contains(instName)) {
                    userTransactionsInst.add(transaction);
                }
            }
        }

        return userTransactionsInst;
    }

    /**
     * Sincroniza as transações pendentes com outro nó na rede.
     *
     * <p>
     * Este método compara o conjunto de transações pendentes local com as do nó
     * especificado. Caso haja transações novas, estas são adicionadas ao
     * conjunto local e propagadas para outros nós da rede, garantindo a
     * sincronização completa.
     * </p>
     *
     * @param node O nó com o qual as transações serão sincronizadas.
     * @throws RemoteException Se ocorrer um erro remoto durante a
     * sincronização.
     */
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
                    //sincronizar-se com o no actual
                    p2pListener.onMessage("sinchronizeTransactions", " iremoteP2P.sinchronizeTransactions(this)");
                    iremoteP2P.synchronizeTransactions(this);
                }
            }
        }

    }

    /**
     * Remove uma lista de transações do conjunto de transações pendentes.
     *
     * <p>
     * Este método remove as transações especificadas do conjunto local, salva o
     * estado atualizado no arquivo de transações pendentes, e propaga a remoção
     * para os demais nós da rede. Apenas transações que ainda existem são
     * removidas.
     * </p>
     *
     * @param myTransactions A lista de transações a serem removidas.
     * @throws RemoteException Se ocorrer um erro remoto ao sincronizar a
     * remoção com outros nós.
     */
    @Override
    public void removeTransactions(List<String> myTransactions) throws RemoteException {
        transactions.removeAll(myTransactions);
        try {
            savePendingTransactions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        p2pListener.onTransaction("remove " + myTransactions.size() + " transactions");
        for (IremoteP2P iremoteP2P : network) {
            if (iremoteP2P.getTransactions().retainAll(transactions)) {
                iremoteP2P.removeTransactions(myTransactions);
            }
        }
    }
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::      M I N E R   :::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //////////////////////////////////////////////////////////////////////////////

    /**
     * Inicia o processo de mineração de um bloco com uma mensagem e uma
     * dificuldade específica.
     *
     * <p>
     * O método inicia o minerador local e envia comandos para que outros nós na
     * rede também comecem a minerar a mesma mensagem com a mesma dificuldade,
     * caso ainda não estejam minerando.
     * </p>
     *
     * @param msg A mensagem a ser minerada.
     * @param zeros O número de zeros necessários no hash para validar o bloco.
     * @throws RemoteException Se ocorrer um erro remoto durante a comunicação
     * com outros nós.
     */
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

    /**
     * Para o processo de mineração e propaga a interrupção para outros nós da
     * rede.
     *
     * <p>
     * Este método interrompe o minerador local, utilizando o nonce fornecido
     * como resultado, e informa os outros nós para também interromperem suas
     * operações de mineração.
     * </p>
     *
     * @param nonce O nonce calculado para interromper o processo de mineração.
     * @throws RemoteException Se ocorrer um erro remoto ao comunicar a
     * interrupção para outros nós.
     */
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

    /**
     * Realiza a mineração de um bloco com uma mensagem e dificuldade
     * específicas.
     *
     * <p>
     * Este método inicia o processo de mineração localmente e espera até que o
     * nonce válido seja calculado. Retorna o nonce gerado após a conclusão do
     * processo.
     * </p>
     *
     * @param msg A mensagem a ser minerada.
     * @param zeros O número de zeros necessários no hash para validar o bloco.
     * @return O nonce calculado para o bloco minerado.
     * @throws RemoteException Se ocorrer um erro remoto durante a mineração.
     */
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

    /**
     * Verifica se o nó atual está a realizar a mineração.
     *
     * @return {@code true} se o nó está a minerar, {@code false} caso
     * contrário.
     * @throws RemoteException Se ocorrer um erro remoto ao verificar o estado
     * da mineração.
     */
    @Override
    public boolean isMining() throws RemoteException {
        return myMiner.isMining();
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //::::::::::::::::: B L O C K C H A I N :::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //////////////////////////////////////////////////////////////////////////////
    /**
     * Adiciona um bloco à blockchain local e propaga a atualização para os
     * outros nós da rede.
     *
     * <p>
     * Este método verifica a validade do bloco e insere-o na blockchain local
     * se ele for válido e encaixar corretamente na cadeia existente. Também
     * propaga a adição para outros nós e realiza a sincronização da blockchain,
     * caso necessário.
     * </p>
     *
     * @param b O bloco a ser adicionado.
     * @throws RemoteException Se o bloco for inválido ou ocorrer um erro
     * remoto.
     */
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

    /**
     * Retorna o tamanho atual da blockchain local.
     *
     * @return O número de blocos na blockchain.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar a
     * blockchain.
     */
    @Override
    public int getBlockchainSize() throws RemoteException {
        return myBlockchain.getSize();
    }

    /**
     * Retorna o hash do último bloco na blockchain local.
     *
     * @return O hash do último bloco.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar a
     * blockchain.
     */
    @Override
    public String getBlockchainLastHash() throws RemoteException {
        return myBlockchain.getLastBlockHash();
    }

    /**
     * Retorna a blockchain local.
     *
     * @return A blockchain como um objeto {@link BlockChain}.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar a
     * blockchain.
     */
    @Override
    public BlockChain getBlockchain() throws RemoteException {
        return myBlockchain;
    }

    /**
     * Sincroniza a blockchain local com os nós da rede.
     *
     * <p>
     * Este método verifica se há uma blockchain maior e válida em outros nós da
     * rede. Caso encontre, substitui a blockchain local pela versão remota
     * válida.
     * </p>
     *
     * @throws RemoteException Se ocorrer um erro remoto durante a
     * sincronização.
     */
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

    /**
     * Retorna todas as transações na blockchain local.
     *
     * @return Uma lista contendo todas as transações da blockchain.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar a
     * blockchain.
     */
    @Override
    public List<String> getBlockchainTransactions() throws RemoteException {
        ArrayList<String> allTransactions = new ArrayList<>();
        for (Block b : myBlockchain.getChain()) {
            allTransactions.addAll(b.transactions());
        }
        return allTransactions;
    }

    /**
     * Registra um novo usuário na rede P2P.
     *
     * <p>
     * O método verifica se o nome já está registrado. Caso não esteja, adiciona
     * o usuário ao sistema, salva os dados localmente, e grava a chave pública
     * em um arquivo.
     * </p>
     *
     * @param nome O nome do usuário a ser registrado.
     * @param publicKey A chave pública associada ao usuário.
     * @param password A senha do usuário.
     * @throws RemoteException Se o nome já estiver registrado ou ocorrer um
     * erro remoto.
     */
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

    /**
     * Registra uma nova instituição na rede P2P.
     *
     * <p>
     * O método verifica se o nome já está registrado. Caso não esteja, adiciona
     * a instituição ao sistema, salva os dados localmente, e grava a chave
     * pública em um arquivo.
     * </p>
     *
     * @param nome O nome da instituição a ser registrada.
     * @param publicKey A chave pública associada à instituição.
     * @param password A senha da instituição.
     * @throws RemoteException Se o nome já estiver registrado ou ocorrer um
     * erro remoto.
     */
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

    /**
     * Verifica se uma instituição pode fazer login no sistema.
     *
     * @param nome O nome da instituição.
     * @param password A senha da instituição.
     * @return {@code true} se as credenciais forem válidas, {@code false} caso
     * contrário.
     * @throws RemoteException Se ocorrer um erro remoto ao verificar as
     * credenciais.
     */
    @Override
    public boolean loginInstituicao(String nome, String password) throws RemoteException {
        return instituicoes.containsKey(nome) && instituicoes.get(nome).equals(password);
    }

    /**
     * Verifica se um utilizador pode fazer login no sistema.
     *
     * @param nome O nome do utilizador.
     * @param password A senha do utilizador.
     * @return {@code true} se as credenciais forem válidas, {@code false} caso
     * contrário.
     * @throws RemoteException Se ocorrer um erro remoto ao verificar as
     * credenciais.
     */
    @Override
    public boolean loginUtilizador(String nome, String password) throws RemoteException {
        return utilizadores.containsKey(nome) && utilizadores.get(nome).equals(password);
    }

    /**
     * Retorna os usuários registrados no sistema.
     *
     * @return Um mapa contendo os nomes dos usuários e suas respectivas senhas.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar os dados.
     */
    // Métodos de sincronização
    @Override
    public Map<String, String> getUtilizadores() throws RemoteException {
        return new HashMap<>(utilizadores);
    }

    /**
     * Retorna as instituições registradas no sistema.
     *
     * @return Um mapa contendo os nomes das instituições e suas respectivas
     * senhas.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar os dados.
     */
    @Override
    public Map<String, String> getInstituicoes() throws RemoteException {
        return new HashMap<>(instituicoes);
    }

    /**
     * Retorna as chaves públicas registradas no sistema.
     *
     * @return Um mapa contendo os nomes e suas respectivas chaves públicas.
     * @throws RemoteException Se ocorrer um erro remoto ao acessar os dados.
     */
    @Override
    public Map<String, PublicKey> getPublicKeys() throws RemoteException {
        return new HashMap<>(publicKeys);
    }

    /**
     * Sincroniza os dados (usuários, instituições e chaves públicas) com outro
     * nó da rede.
     *
     * <p>
     * Este método mescla os dados locais com os do nó remoto e salva os dados
     * sincronizados localmente.
     * </p>
     *
     * @param node O nó remoto com o qual os dados serão sincronizados.
     * @throws RemoteException Se ocorrer um erro remoto durante a
     * sincronização.
     */
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
