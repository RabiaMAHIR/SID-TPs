
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServeurMain {

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            ChatServerImpl server = new ChatServerImpl();
            reg.rebind("ChatService", server);
            System.out.println("[Serveur] En attente de clients...");
        } catch (Exception e) {
            System.err.println("[Serveur] Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
