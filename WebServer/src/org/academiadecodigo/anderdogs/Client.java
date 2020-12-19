package org.academiadecodigo.anderdogs;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {


    private File home = new File("resources/moths.html");
    private File fileNotFound = new File("resources/FileNotFound.html");
    private File cat = new File("resources/cat2.png");


    private Socket clientSocket;

    private String request = "";

    public Client(Socket clientSocket){
       this.clientSocket = clientSocket;
       System.out.println("Connection accepted!");
    }


    public void processRequest() throws IOException {
        //stream to receive from browser
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        request = in.readLine();
        System.out.println(request);

    }

    public void respond() throws IOException {
        //stream to send to browser
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String headerFound = "HTTP/1.0 200 Document Follows\r\\n\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + home.length() + " \r\n\n" +
                "\r\n";

        String headerNotFound = "HTTP/1.0 404 Not Found\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + fileNotFound.length() + " \r\n" +
                "\r\n";

        String headerImage = "HTTP/1.0 200 Document Follows\r\n"+
                "Content-Type: image/png \r\n" +
                "Content-Length: " + cat.length() + "\r\n";
//                "\r\n";


        if(request.contains("GET") && request.contains("home")){
            out.println(headerFound);


            //stream to read from file
            FileReader HTMLReader = new FileReader("resources/moths.html");
            BufferedReader HTMLBuffer = new BufferedReader(HTMLReader);


            String line = "";
            while((line = HTMLBuffer.readLine()) != null){
                out.println(line);
            }
            HTMLBuffer.close();
        }

        else if(request.contains("GET") && request.contains("cat")){
            out.println(headerImage);
            System.out.println("Image");

            byte[] buffer = new byte[(int) cat.length()];

            FileInputStream inputStream = new FileInputStream("resources/cat2.png");

            int num = inputStream.read(buffer);
            System.out.println(num);

            OutputStream outWithTheCat = clientSocket.getOutputStream();
            outWithTheCat.write(buffer);

        }

        else {
            out.println(headerNotFound);
            System.out.println("File not found");

            FileReader fileNotFound = new FileReader("resources/FileNotFound.html");
            BufferedReader fileNotFoundBuffer = new BufferedReader(fileNotFound);


            String line= "";
            while ((line = fileNotFoundBuffer.readLine()) != null){
                out.println(line);
            }

        }
    }


    @Override
    public void run() {
        try {
            processRequest();
            respond();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
