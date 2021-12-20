
package com.kth.id1212.task4_encyptsockets;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

/**
* Retrieve
*/
public class Imap {
    private static PrintWriter wirteSocket;
    private static BufferedReader readSocket;
    private static BufferedReader readUser;
    private static SSLSocketFactory factorySocket;
    private static SSLSocket connectionSocket;

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "webmail.kth.se";
        int port = 993;

        // Factory as socket proxy for tunneling socket specified by host and port
        factorySocket = (SSLSocketFactory) SSLSocketFactory.getDefault(); 
        connectionSocket = (SSLSocket) factorySocket.createSocket(host, port);

        // Set up input/output to socket and console
        wirteSocket = new PrintWriter(connectionSocket.getOutputStream());
        readSocket = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        readUser = new BufferedReader(new InputStreamReader(System.in));

        connectUser();
        openSession();  
        // a002 select Inbox, a003 fetch 1 full, a004 fetch 1 body[header]  
    }

    private static void connectUser() throws IOException {
        // User details for connection
        System.out.println("Retrieve KTH email");
        System.out.println("Your mail or only username: ");
        String email = readUser.readLine();
        System.out.println("Your password:");
        String password = readUser.readLine();

        // Connect to user
        String username = email.split("@")[0];
        wirteSocket.println("a001 login " + username + " " + password);
        wirteSocket.flush();

        String dialog = "a001";
        socketResponse(dialog);
    }

    private static void openSession() throws IOException {
        // Next command round is a002, hence start iteration on 2
        int i = 2;   
        String dialog = "";
        String command = "";

        while(!command.contains("logout")){
            dialog = "a" + String.format("%03d", i); //format a00i
            i++;

            // Send user request command to socket
            command = readUser.readLine();
            wirteSocket.println(dialog + " " + command);
            wirteSocket.flush();
            System.out.println(dialog + " " + command);
            socketResponse(dialog); 
        }
    }

    private static void socketResponse(String dialog) throws IOException {
        String response = readSocket.readLine();
        while(response != null){
            System.out.println(response);
            if(response.contains(dialog))
                break;
            response = readSocket.readLine();
        } 
    }
}