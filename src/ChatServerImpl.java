
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private final Map<Integer, ClientInterface> clients = new ConcurrentHashMap<>();
    private int nextId = 1;
    public ChatServerImpl() throws RemoteException {
        super();
    }
    @Override
    public synchronized int registerClient(ClientInterface client) throws RemoteException {
        int id = nextId++;
        client.setClientId(id);
        clients.put(id, client);
        System.out.println("[Serveur] Client #" + id + " connecté");
        client.receiveMessage("Bienvenue ! Votre identifiant est #" + id);
        broadcastMessage("Le client #" + id + " a rejoint le chat.", id);
        return id;}
    @Override
    public void broadcastMessage(String message, int senderId) throws RemoteException {
        System.out.println("[Serveur] Message broadcast de #" + senderId + " : " + message);
        for (Map.Entry<Integer, ClientInterface> entry : clients.entrySet()) {
            if (entry.getKey() != senderId) {
                try {
                    entry.getValue().receiveMessage("Client #" + senderId + " : " + message);
                } catch (RemoteException e) {
                    System.out.println("[Serveur] Client #" + entry.getKey() + " inaccessible");}
            }}}
    @Override
    public void directMessage(int targetId, String message, int senderId) throws RemoteException {
        ClientInterface target = clients.get(targetId);
        if (target == null) {
            ClientInterface sender = clients.get(senderId);
            if (sender != null) {
                sender.receiveMessage("[Erreur] Client #" + targetId + " introuvable.");
            }
            return;
        }
        try {
            target.receiveMessage("[Privé] Client #" + senderId + " : " + message);
            System.out.println("[Serveur] Message privé de #" + senderId + " vers #" + targetId);
        } catch (RemoteException e) {
            System.out.println("[Serveur] Impossible de contacter le client #" + targetId);
        }}
    @Override
    public void unregisterClient(int clientId) throws RemoteException {
        ClientInterface removed = clients.remove(clientId);
        if (removed != null) {
            System.out.println("[Serveur] Client #" + clientId + " déconnecté");
            broadcastMessage("Le client #" + clientId + " a quitté le chat.", clientId);
        }
    }
}
