import java.net.*;
import java.io.*;

public class newfriend {
    ServerSocket server;
    Socket client1Socket;
    Socket client2Socket;

    // Constructor
    public newfriend() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");

            System.out.println("Waiting for client 1...");
            client1Socket = server.accept();
            System.out.println("Client 1 connected: " + client1Socket);

            
            System.out.println("Waiting for client 2...");
            client2Socket = server.accept();
            System.out.println("Client 2 connected: " + client2Socket);

            // Start threads to handle communication between clients
            new Thread(new ClientHandler(client1Socket, client2Socket)).start();
            new Thread(new ClientHandler(client2Socket, client1Socket)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ClientHandler class
    class ClientHandler implements Runnable {
        Socket senderSocket;
        Socket receiverSocket;
        BufferedReader reader;
        PrintWriter writer;

        public ClientHandler(Socket senderSocket, Socket receiverSocket) {
            try {
                this.senderSocket = senderSocket;
                this.receiverSocket = receiverSocket;
                reader = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
                writer = new PrintWriter(receiverSocket.getOutputStream(), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg = reader.readLine();
                    if (msg == null || msg.equals("exit")) {
                        System.out.println("Client disconnected: " + senderSocket);
                        senderSocket.close();
                        receiverSocket.close();
                        break; 
                    }
                    System.out.println("Message from client " + senderSocket + ": " + msg);
                    writer.println(msg); // Forward message to the other client
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new newfriend();
    }
}
