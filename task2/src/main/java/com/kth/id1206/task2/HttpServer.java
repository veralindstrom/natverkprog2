/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.kth.id1206.task2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Vera
 */
public class HttpServer{
    private final int port;
    private static final HashMap<String, GuessObject> games = new HashMap<String, GuessObject>();

    public HttpServer(int port){
        this.port = port;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO code application logic here

        HttpServer server = new HttpServer(8888);
        server.start();
    }
    
    void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("waiting for connection");
                Socket socket = serverSocket.accept();
                System.out.println("New connection");
                
                InputStream input = socket.getInputStream(); 
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
               
                StringBuilder headers = new StringBuilder();
                GuessObject game;
                String str; 
                String method = "/";
                int contentLength = 0;
                boolean won = false;
                String body = null;
                //read req-header
                while((str = reader.readLine()) != null && str.length() > 0){headers.append(str).append("\r\n");
                    if(str.contains("POST")){
                        method = str;
                    }
                    else if(str.contains("guess")){
                        body = str.split(" ")[1];
                        System.out.println(body);
                    }
                    else if(str.contains("Content-Length")){
                        contentLength = Integer.parseInt(splitString(str, " ", 1));
                    }
                    headers.append(str).append("\r\n");
                }
                
                method = splitString(method, " ", 1);
                
                //stream to send response to
                PrintStream response = new PrintStream(socket.getOutputStream());
                response.print("HTTP/1.1 200 OK\r\n");
                
                //set cookie
                String idCookie = findCookie(headers.toString());
 
                if(idCookie == null){
                    idCookie = UUID.randomUUID().toString();
                    game = new GuessObject();
                    games.put(idCookie, game);
                }else{
                    game = games.get(idCookie);
                    if(game == null){
                        game = new GuessObject();
                        games.put(idCookie, game);
                    }
                        
                }
                
                String setCookieHeader = String.format("Set-Cookie: gameId=%s",idCookie);
               
                //get body, if body
                if(contentLength > 0){
                    System.out.println("goes in content");
                    body = getBody(contentLength, reader);
                    int g = Integer.parseInt(splitString(body, "=", 1));
                    game.setGuess(g);
                    game.incrementNumberOfsGuesses();
                    won = game.result();
                }
                /*
                if(body.contains("guess")){
                    int g = Integer.parseInt(splitString(body, "=", 1));
                    game.setGuess(g);
                    game.incrementNumberOfsGuesses();
                    won = game.result();
                    System.out.println("guess is " + g);
                }*/
                
                //decide what page to display
                if(method.equals("/guess") || game.getNumberOfGuesses() > 0) {
                    response.print(setCookieHeader + "\r\n");
                    response.print("Content-Length: " + game.getPage().length() + "\r\n");
                    response.print("Content-Type: text/html \r\n\r\n");
                    response.println(game.getPage());
                    if(won){
                        game = new GuessObject();
                        games.put(idCookie, game);
                        setCookieHeader = String.format("Set-Cookie: gameId=%s",idCookie);
                    }
                }  
                else {
                    String fileString = readFile("index.html");
                    response.print(setCookieHeader + "\r\n");
                    response.print("Content-Length: " + fileString.length() + "\r\n");
                    response.print("Content-Type: text/html \r\n\r\n");
                    response.println(fileString);
                }
                //socket.shutdownOutput();
                socket.close();
                
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
        }
    }
    
    private String getBody(int contentLength, BufferedReader reader) throws IOException{
        StringBuilder b = new StringBuilder();
        if(contentLength > 0) {
            char buffer[] = new char[contentLength];
            for (int i = 0; i < contentLength; i++) {
                int c = reader.read(); 
                if (c == -1)  //läser tills end of stream
                    break;
                buffer[i] = (char) c; //konvertera till character och lägg i array
            }
            String body = new String(buffer);
            for(String bodyLine : body.split("\r\n")){ //om body innehåller flea rader
                if(bodyLine.equals("")) //när man läst hela body
                    break;
                b.append(bodyLine);
            }
        }
        return b.toString();
    }
    
    private String readFile(String file) throws IOException{
        File newFile = new File(file);
        String str;
        StringBuilder fileString = new StringBuilder();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));
        while((str = fileReader.readLine()) != null && str.length() > 0){
            fileString.append(str);
        }
        return fileString.toString();
    }
    
    private String splitString(String toBeSplit, String spitter, int indexToGet){
        String h[] = toBeSplit.split(spitter);
            if(h.length>indexToGet)
                toBeSplit = h[indexToGet];
        return toBeSplit;
    }
    
    private String findCookie(String headers){
        String [] arr1 = headers.split("\r\n"); //ta in hela header
        for (int i = 0; i < arr1.length; i++) {
            String elem = arr1[i];
            String [] arr2 = elem.split(":"); //splitta mellan header:value
            if(arr2[0].equals("Cookie")){
                if(arr2[1].contains("gameId")){
                String [] arr3 = arr2[1].split("gameId="); // kolla efter gameId-cookien
                    return arr3[1];
                }
            }
        }
        return null;
    }
}

class GuessObject {
    int guesses = 0;
    String cookie;
    int guess;
    int rndNum;
    boolean correct = false;
    
    String page;
    
    public GuessObject() {
        this.rndNum = generateRandomNumber();
        System.out.println(rndNum);
    }
    
    private int generateRandomNumber(){
        return (int) (Math.random() * (101 - 1)) + 1;
    }
    
    private String compareGuess(){
        if(guess < rndNum)
            return "<p>Guess higher.</p>";
        else
            return "<p>Guess lower.</p>";
    }
    
    public String getPage(){
        page = "<html>\n" +
                "    <head>\n" +
                "        <title>Task 2</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div>\n";
        if(correct){
            page +=     " <p>Congratulations!</p>\n" +
                        " <p>You made it in "+ getNumberOfGuesses() +"guess(es)</p>\n" +
                        " <label>Press button to try again</label>\n" +
                        " <a href=\"/again\">New game</a>";
        }
        else{
            page +=
                    "           <p>You have guessed " + guesses + " times</p>" +
                    compareGuess() +
                    "            <p> Last guess: " + guess +
                    "            <p>I'm thinking of a number between 1 and 100.</p>\n" +
                    "            <form method=\"POST\" action=\"/guess\">\n" +
                    "            <label>What's your guess?</label>\n" +
                    "            <input placeholder=\"guess a number\" name=\"guess\"/>\n" +
                    "            <button type=\"submit\">Submit</button>\n" +
                    "            </form>\n";
        }
        
        page +=     "        </div>\n" +
                    "    </body>\n" +
                    "</html>\n";
        
        return page;
    }
    
    public void setGuess(int guess){
        this.guess = guess;
    }
    
    public int getGuess(){
        return guess;
    }
    
    public boolean result(){
        return correct = (guess == rndNum);
    }
    
    public void incrementNumberOfsGuesses(){
        this.guesses += 1;
    }
    
    public int getNumberOfGuesses(){
        return guesses;
    }
}
