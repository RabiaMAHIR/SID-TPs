
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

    int registerClient(ClientInterface client)throws RemoteException;
    void unregisterClient(int clientId) throws RemoteException;
    void broadcastMessage(String message, int senderId) throws RemoteException;
    void directMessage(int targetId, String message, int senderId) throws RemoteException;
}
