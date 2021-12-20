
package com.kth.id1212.task4;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Base64;
/**
* To send email
*/
public class Smtp {
    static private PrintWriter print;
    static private BufferedReader buffer;
    static private BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "smtp.kth.se";
        int port = 587;
        
        String mail = setValue("Enter your KTH mail:");
        String user = getUser(mail);
        
        String pass = setValue("Enter your password:");
        String RCPT = setValue("Enter the recipient:");

        String encodedUser = Base64.getEncoder().encodeToString(user.getBytes());
        String encodedPass = Base64.getEncoder().encodeToString(pass.getBytes());

        //Create insecure connection and encrypt by STARTTLS
        try (Socket socket = new Socket(host, port)) {
            print = new PrintWriter(socket.getOutputStream());
            buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("---------------------------------------------");
            printToOutputStream("EHLO " + host, true); //"gives back" 220: service is ready
            
            //tells email server that email client want to upgrade to secure connection
            printToOutputStream("STARTTLS", true); //"gives back" 250: Requested mail action okay, completed
            
            readFromInputStream(); //read until 220: Ready to start TLS
            
            //set up ssl sockets when TLS service is ready
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) ssf.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
            print = new PrintWriter(sslSocket.getOutputStream(), true);
            buffer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            
            //The client sends the EHLO command again to the e-mail server and 
            //starts the communication from the beginning, but this time the 
            //communication will be encrypted until the QUIT command is sent.
            //"gives back" 250: Requested mail action okay, completed
            printToOutputStream("EHLO " + host, true);
            
            //"gives back" 250: Requested mail action okay, completed
            printToOutputStream("AUTH LOGIN", true);
            
            printToOutputStream(encodedUser, true);
            printToOutputStream(encodedPass, true);
            
            sendMail(mail, RCPT);
            
            print.close();
            buffer.close();
            sslSocket.close();
        }
    }
    
    static String setValue(String message) throws IOException{
        System.out.println(message);
        return reader.readLine();
    }
    
    static String getUser(String mail) throws IOException{
        return mail.split("@")[0];
    }
    
    static void readFromInputStream() throws IOException{
        //Reads until 220, meaning: Service ready
        System.out.println("READING FROM INPUT STREAM");
        String msg;
        while((msg = buffer.readLine()) != null){
            System.out.println(msg);
            if(msg.contains("220"))
                break;
        }
    }
    
    static void sendMail(String mail, String RCPT) throws IOException{
        System.out.println("SENDING MAIL");
        String msg;
        while((msg = buffer.readLine()) != null){
            System.out.println(msg);
            //reads until 235: Authentication successful, then send mail
            if(msg.contains("235")){
                printToOutputStream("MAIL FROM:<"+mail+">", true);
                printToOutputStream("RCPT TO:<"+RCPT+">", true);
                printToOutputStream("DATA", true);
                printToOutputStream("Date: "+ LocalTime.now(), false);
                printToOutputStream("From: " + getUser(mail) + " <"+mail+">", false);
                printToOutputStream("Subject: Task4 SMTP", false);
                printToOutputStream("To: " + RCPT, false);
                printToOutputStream("Merry Christmas! \nBest wishes,\nfrom " + getUser(mail) + "\r\n.", true); //dot must be sent on single line after DATA
                printToOutputStream("QUIT", true);
            }
        }
    }
    
    static void printToOutputStream(String msg, boolean printToConsole) throws IOException {
        print.println(msg);
        print.flush();
        System.out.println(msg);
        if(printToConsole)
            System.out.println(buffer.readLine());
    }
}
