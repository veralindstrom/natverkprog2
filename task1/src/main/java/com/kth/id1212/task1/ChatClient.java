package com.kth.id1212.task1;

/**
 * ChatClient is the program for the clients to communicate through the server.
 * @author Estelle Hue and Vera Lindström
 */
import java.net.*;
import java.io.*;


public class ChatClient {
    private String serverAddress;
    private int port;
    private String client;
    private Socket socket;
    
    public ChatClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }
    
    /**
     * Main method to run the program, starting a client connection to the server.
     * @param args the serverAddress and the port number
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Syntax: java ChatClient.java localhost <port-number>");
            return;
        }
 
        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);
 
        ChatClient client = new ChatClient(serverAddress, port);
        client.start(client);
    }
 
    /**
     * Method starting the client connection to the server.
     */
    public void start(ChatClient newClient) {
        try {
            socket = new Socket(serverAddress, port);
 
            System.out.println("Connected to the chat server");
 
            // Creating the two threads for one client to read and write to and from server.
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
 
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }
 
    /**
     * Method to set the current client connected to the server.
     * @param name the clients name
     */
    void setClient(String name) {
        this.client = name;
    }
 
    /**
     * Method to get the current client connected to the server.
     * @return the clients name
     */
    String getClient() {
        return this.client;
    }
    
    /**
     * Method to close the current client's server connection.
     */
    void closeClient(){
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error disconneting from server: " + ex.getMessage());
        }
    }
}


/**
 * ReadThread is responsible for reading the server's input and printing it
 * to the client console.
 * It runs in an infinite loop until the client disconnects from the server.
 */
class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    /**
     * Method for the client to constantly "read" from the server, checking for 
     * any new message for the client to receive.
     * @param socket the current connected socket for the client
     * @param client the connected client
     */
    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
        }
    }

    /**
     * Infinite loop method for checking if any new message are sent from the server.
     */
    @Override
    public void run() {
        while (true) {
            try {
                String response = reader.readLine();

                // If the server closes the connection the clients are informed
                if (response == null){
                    System.out.print("Server closed connection");   
                    client.closeClient();
                    break;
                }
                System.out.println("\n" + response);
                
            } catch (IOException ex) {
                System.out.println("Message: " + ex.getMessage());
                break;
            }
        }
    }
}






/**
 * WriteThread is responsible for reading the user's input and sending it
 * to the server.
 * It runs in an infinite loop until the user types 'Bye' to quit.
 */
class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
 
    /**
     * Method for the client to constantly "write" to the server, checking for
     * any new message from the client sent to server.
     * @param socket the current connected socket for the client
     * @param client the connected client
     */
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
 
    /**
     * Method for client to write to server any time during connection.
     */
    @Override
    public void run() {
        Console console = System.console();
 
        String name = console.readLine("\nEnter your name: ");
        client.setClient(name);
        writer.println(name);
 
        String clientInput;
        do {
            clientInput = console.readLine();
            // If the client doesn't enter anything and presses enter 
            if (clientInput.isEmpty() || clientInput.equals(" ") || clientInput.equals("\t")) {
            }
            else {
                writer.println(clientInput);
            }
        } while (!clientInput.equals("Bye") && !clientInput.equals("bye"));
 
        // The client closes the connection before the server
        client.closeClient();
    }
}