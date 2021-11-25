/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kth.id1206.task2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
/**
 *
 * @author Vera
 */
public class WebClient {
    public static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    public static void main(String[] args) throws InterruptedException{
        try{
            Integer guess = guess(100,1);
            boolean fail = true;
            int total = 0;
            int prev = 100;
            
            String cookie = UUID.randomUUID().toString();
            for(int i = 0; i < 30; i++){
                //HttpClient client = HttpClient.newHttpClient();
                int tries = 0;
                
                while(fail){
                    HttpRequest request = HttpRequest.newBuilder()
                        //.POST(HttpRequest.BodyPublishers.ofString(guess.toString()))
                        //.uri(URI.create("http://localhost:8888/"))
                        .uri(URI.create("http://localhost:8888/?guess=" + guess.toString())) 
                        .header("Cookie", "gameId=" + cookie)
                        .build();
                
                    //doesn't want to send more than 1 time
                    //second time loop works until this line
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
               
                    System.out.println(response.headers().toString());
                    //System.out.println(response.body());
                    tries++;
                
                    if(response.body().contains("Congratulations")){
                        fail = true;
                        total = total + tries;
                        break;
                    }
                    else if(response.body().contains("higher")){
                        int temp = guess;
                        guess += (Math.abs(guess-prev)/2);
                        prev = temp;
                        if(guess==temp)
                            guess++;
                    }else if(response.body().contains("lower")){
                        int temp = guess;
                        guess -= (Math.abs(guess-prev)/2);
                        prev = temp;
                        if(guess==temp)
                            guess--;
                    }
                
                }
            }
            System.out.println("total: " + total);
            System.out.println("mean: " + (double) total/100);
            
        }
        catch(java.io.IOException e){
           e.printStackTrace();
        }
    }
    
    private static int guess(int high, int low){
        return (int) (Math.random() * ((high+1) - low)) + low;
    }
}
