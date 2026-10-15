
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {

        ChatServer server = null;
        ChatClient client = null;
        int id = -1;

        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            server = (ChatServer) reg.lookup("ChatService");
            client = new ChatClient("User");
            id = server.registerClient(client);
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== CHAT RMI ===");
            System.out.println("Message public : Bonjour à tous");
            System.out.println("Message privé : 2:salut");
            System.out.println("Quitter : quit");
            while (true) {
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }
                if (input.isEmpty()) {
                    continue;
                }
                int separator = input.indexOf(':');
                if (separator > 0) {

                    String number =
                            input.substring(0, separator).trim();

                    String message =
                            input.substring(separator + 1).trim();

                    try {
                        int targetId = Integer.parseInt(number);

                        if (message.isEmpty()) {
                            System.out.println(
                                    "Le message ne peut pas être vide."
                            );
                            continue;
                        }

                        server.directMessage(
                                targetId, message, id
                        );

                    } catch (NumberFormatException e) {
                        server.broadcastMessage(input, id);
                    }

                } else {
                    server.broadcastMessage(input, id);
                }
            }

        } catch (Exception e) {
            System.err.println(
                    "[Client] Erreur : " + e.getMessage()
            );

        } finally {

            if (server != null && id != -1) {
                try {
                    server.unregisterClient(id);
                } catch (Exception e) {
                    System.err.println(
                            "[Client] Déconnexion impossible : "
                                    + e.getMessage()
                    );
                }
            }

            if (client != null) {
                try {
                    UnicastRemoteObject.unexportObject(
                            client, true
                    );
                } catch (Exception ignored) {
                }
            }

            System.out.println("[Client] Déconnecté.");
        }
    }
}
