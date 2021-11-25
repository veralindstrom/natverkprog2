/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kth.id1206.task2;
/**
 *
 * @author Sten, leohj, theok
 */
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.HashMap;

public class TheoLeoServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Creating Serversocket");
        ServerSocket ss = new ServerSocket(8081);
        String message = "TODO write";

        GuessHandler guessHandler = new GuessHandler();

        HashMap<Integer, GuessHandler> cookieMap = new HashMap<>();
        int cookieIdNum = 0;

        while (true) {
            System.out.println("Waiting for client...");
            Socket s = ss.accept();
            System.out.println("Client connected");
            BufferedReader request = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String str = request.readLine();
            System.out.println(str);
            StringTokenizer tokens = new StringTokenizer(str, " ?");
            tokens.nextToken(); // The word GET
            String requestedDocument = tokens.nextToken();
            if (requestedDocument.contains("favicon"))
                continue;
            Boolean reggedUser = false;
            boolean tries = false;
            String body = null;
            if (str.contains("guess")) {
                body = str.split(" ")[1];
            }

            while ((str = request.readLine()) != null && str.length() > 0) {
                System.out.println(str);

                if (str.contains("Cookie")) {
                    if (str.contains("clientCookie")
                            && cookieMap.containsKey(Integer.parseInt(str.split("clientCookie=", 2)[1]))) {
                        System.out.println("inhybvytub yguj");
                        reggedUser = true;
                        guessHandler = cookieMap.get(Integer.parseInt(str.split("clientCookie=", 2)[1]));
                        tries = guessHandler.tries();
                    }
                }
            }
            if (!reggedUser) {
                System.out.println("hej");

                guessHandler = new GuessHandler();
                cookieMap.put(++cookieIdNum, guessHandler);
            }
            System.out.println(
                    "Request processed. ------------------------------------------------------------------------");
            s.shutdownInput();

            PrintStream response = new PrintStream(s.getOutputStream());
            response.println("HTTP/1.1 200 OK");
            response.println("Server: Trash 0.1 Beta");
            response.println("Content-Type: text/html");

            response.println("Set-Cookie: clientCookie=" + Integer.toString(cookieIdNum));
            response.println();

            System.out.println("message is: " + message);
            System.out.println("size: " + cookieMap.size());

            String HTMLStr = "<html>\n" + "    <head>\n" + "        <title>TODO supply a title</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    </head>\n" + "    <body>\n" + "        <div>" + message + "</div>\n"
                    + "        <form method=\"POST\">\n" + "            <input type=\"text\" name=\"guess\">\n"
                    + "        </form>\n" + "    </body>\n" + "</html>";

            if (!"\favicon.ico".equals(requestedDocument)) { // Ignore any additional request to retrieve the
                                                             // bookmark-icon.

                if (body == null)
                    body = request.readLine();

                if (reggedUser && (tries || body != null)) {
                    if (body != null) {
                        System.out.println("body: " + body);
                        String[] bodyArray = body.split("=", 2);
                        String guess = bodyArray[1];
                        message = guessHandler.guessNumber(Integer.parseInt(guess));
                    } else {
                        message = guessHandler.getLastGuess();
                    }
                    HTMLStr = "<html>\n" + "    <head>\n" + "        <title>TODO supply a title</title>\n"
                            + "        <meta charset=\"UTF-8\">\n"
                            + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                            + "    </head>\n" + "    <body>\n" + "        <div>" + message + "</div>\n"
                            + "        <form method=\"POST\">\n" + "            <input type=\"text\" name=\"guess\">\n"
                            + "        </form>\n" + "    </body>\n" + "</html>";
                    if (message.contains("You made it")) {
                        HTMLStr = "<html>\n" + "    <head>\n" + "        <title>TODO supply a title</title>\n"
                                + "        <meta charset=\"UTF-8\">\n"
                                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                                + "    </head>\n" + "    <body>\n" + "        <div>" + message + "</div>\n"
                                + "        <form method=\"POST\">\n"
                                + "            <button type=\"submit\" formaction=\"/index.html\">Play again!</button>\n"
                                + "        </form>\n" + "    </body>\n" + "</html>";
                        guessHandler.resetUser();
                        message = "Welcome to the guessing game. I'm thinking of a number between 1 and 100";
                    }
                    response.write(HTMLStr.getBytes(Charset.forName("UTF-8")));
                } else {
                    System.out.println(new File("theoleo.html").getAbsoluteFile());
                    File f = new File("/Users/admin/NetBeansProjects/task2/theoleo.html");
                    FileInputStream infil = new FileInputStream(f);
                    byte[] b = new byte[1024];
                    while (infil.available() > 0) {
                        response.write(b, 0, infil.read(b));
                    }
                }

                s.shutdownOutput();
                s.close();
            }
        }
    }
}

class GuessHandler {
    int correctNumber = (int) (Math.random() * 100);
    int numberOfGuesses = 0;
    int lastGuess = 0;
    boolean win = false;

    public String guessNumber(int nr) {
        lastGuess = nr;
        numberOfGuesses += 1;
        if (nr == correctNumber) {
            win = true;
            return "You made it in " + numberOfGuesses + " guess(es). Press button to try again.";
        }
        String tipStr = nr < correctNumber ? "higher" : "lower";
        return "Nope, guess " + tipStr + ". You have made " + numberOfGuesses + " guess(es).";
    }

    public boolean tries() {
        if (numberOfGuesses > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getLastGuess() {

        String tipStr = this.lastGuess < correctNumber ? "higher" : "lower";
        return "Nope, guess " + tipStr + ". You have made " + numberOfGuesses + " guess(es). Your last guess was "
                + this.lastGuess;
    }

    public boolean userWon() {
        return this.win;
    }

    public void resetUser() {
        this.correctNumber = (int) (Math.random() * 100);
        this.numberOfGuesses = 0;
        this.lastGuess = 0;
        this.win = false;
    }
}