package com.kth.id1212.task1_socket;

/**
 * ChatServer is the program for the clients to connect to and communicate through.
 * @author Estelle Hue and Vera Lindström
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private final int port;
    private ArrayList<String> clients = new ArrayList<>();
    private ArrayList<WorkerThread> workerThreads = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Main method to run the program, starting a server connection.
     * @param args the port number
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer.java <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.start();
    }

    /**
     * Method starting the server.
     */
    void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("ChatServer is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                WorkerThread newClient = new WorkerThread(socket, this);
                workerThreads.add(newClient);
                newClient.start();
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
        }
    }

    /**
     * Broadcasts a delivered message to all clients connected to the server.
     * @param message delivered to server to broadcast to all connected clients
     * @param sendingClient the client sending the message to broadcast
     */
    void broadcast(String message, WorkerThread sendingClient) {
        for (WorkerThread client : workerThreads) {
            if (client != sendingClient) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Adds the client to the list of connected clients.
     * @param name the clients name
     */
    void addClient(String name) {
        clients.add(name);
    }

    /**
     * Removes the client from the list of connected clients when the client disconnects.
     * @param name the clients name
     * @param client the clients thread on the server
     */
    void removeClient(String name, WorkerThread client) {
        boolean removed = clients.remove(name);
        if (removed) {
            workerThreads.remove(client);
            System.out.println("Client " + name + " left the chat");
        }
    }

    /**
     * Method to get the list of clients connected to the server.
     * @return the list of connected clients
     */
    ArrayList<String> getClients() {
        return this.clients;
    }

    /**
     * Method to check if there are any connected clients to the server.
     * @return true if there are any connected clients
     */
    boolean hasClients() {
        return !this.clients.isEmpty();
    }
}


/**
 * ServerThread handles all connection for each connected client, the server
 * can handle multiple clients at the same time.
 */
class WorkerThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public WorkerThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Each time a new client wants to connect, a new WorkerThread is created.
     */
    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printClients();

            String currentClient = reader.readLine();
            server.addClient(currentClient);

            String serverMessage = "New client connected: " + currentClient;
            server.broadcast(serverMessage, this);

            String clientMessage;

            // Client's chat connection is open until the client types "Bye"
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + currentClient + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
            } while (!clientMessage.equals("Bye") && !clientMessage.equals("bye"));

            server.removeClient(currentClient, this);
            socket.close();

            serverMessage = currentClient + " has left the chat.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in ServerThread: " + ex.getMessage());
        }
    }

    /**
     * To the newly connected client, it sends a list of the already connected clients.
     */
    private void printClients() {
        if (server.hasClients()) {
            writer.println("Connected clients: " + server.getClients());
        } else {
            writer.println("No other clients connected at the moment.");
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

}