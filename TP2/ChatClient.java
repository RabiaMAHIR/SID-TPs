
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements ClientInterface {
    private int clientId;
    private final String name;

    public ChatClient(String name) throws RemoteException {
        super();
        this.name = name;
    }
    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }
    @Override
    public int getClientId() throws RemoteException {
        return clientId;
    }
    @Override
    public void setClientId(int id) throws RemoteException {
        this.clientId = id;
        System.out.println("[Client] Votre identifiant est #" + id);
    }
}
