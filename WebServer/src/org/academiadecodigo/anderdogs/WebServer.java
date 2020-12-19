package org.academiadecodigo.anderdogs;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    ServerSocket serverSocket;

    public void listen(){
            try {
                serverSocket = new ServerSocket(8080);
                System.out.println("Waiting for a client connection");

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void init(){
            listen();
        try {
            while(true) {
                Socket client = serverSocket.accept();

                ExecutorService executorService = Executors.newFixedThreadPool(4);
                executorService.execute(new Client(client));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.init();
    }
}
