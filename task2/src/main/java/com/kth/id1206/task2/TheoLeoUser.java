/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kth.id1206.task2;


import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author theok
 */
public class TheoLeoUser {
    public static void main(String[] args){
        int[] nrOfGuesses = new int[100];
        int i = 0;
        String endPoint = "http://localhost:8081/index.html";
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();
            HttpResponse<String> response = null;
            try{
                
                URI uri = URI.create(endPoint);
                HttpRequest request = HttpRequest.newBuilder().uri(uri).
                        build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());

            }catch(Exception e){
                e.printStackTrace();
            }
        for (int j = 0; j < 100; j++) {
            

            HttpHeaders headers = response.headers();
            String cookie = headers.firstValue("set-cookie").get();

            int low = 0;
            int high = 100;
            int guess = 50;
            
            while(!response.body().contains("You made it")) {
                try{
                    URI uri = URI.create(endPoint+"?guess="+guess);
                    HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Cookie", cookie).build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.body().contains("higher"))
                        low = guess;
                    else
                        high = guess;
                    guess = (low+high)/2;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            nrOfGuesses[i++] = Integer.parseInt(response.body().split(" in ")[1].split(" guess")[0]);
                try{
                    URI uri = URI.create(endPoint);
                    HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Cookie", cookie).build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                 }catch(Exception e){
                    e.printStackTrace();
                }
            
            
        }
        
        double total = 0;
        for (int x : nrOfGuesses)
            total += x;
        System.out.println("Average number of guesses is "+total/100.0);
        
        /*
        System.out.println("status Code: " + response.statusCode());
        headers = response.headers();
        //List<String> str = headers.allValues("clientCookie");
        cookie = headers.firstValue("set-cookie").get();
        System.out.println(cookie);
        System.out.println(headers);
        System.out.println("");
        System.out.println("Body : "+ response.body());
        */
    }
}

