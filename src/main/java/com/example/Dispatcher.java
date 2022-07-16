package com.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Dispatcher implements Runnable{
    private Socket clientSocket;
    private FileExplorer fileExplorer;
    private String instruction = "";        //  20 bytes
    private String pathOne = "";            //  260 bytes
    private String pathTwo = "";            //  260 bytes
    private InputStream inputStream;

    private boolean confirmation;
    private String json;                    // ongoingDirectoryJSON
    private FileInputStream fileInputStream;        // only in SEND case

    public Dispatcher(Socket connection, FileExplorer fileExplorer) {
        this.clientSocket = connection;
        this.fileExplorer = fileExplorer;
    }

    @Override
    public void run() {
        receive();
        process();
        send();
        close();
    }

    public void receive() {
        try {
            inputStream = new BufferedInputStream(clientSocket.getInputStream());
            byte[] arrayInstruction = new byte[20];
            byte[] arrayPathOne = new byte[260];
            byte[] arrayPathTwo = new byte[260];
            inputStream.read(arrayInstruction);
            inputStream.read(arrayPathOne);
            inputStream.read(arrayPathTwo);
            instruction = new String(arrayInstruction);
            pathOne = new String(arrayPathOne);
            pathTwo = new String(arrayPathTwo);
            instruction = instruction.trim();
            pathOne = pathOne.trim();
            pathTwo = pathTwo.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process() {
        switch (instruction) {
            case "ONGOING_DIRECTORY":
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0;
                break;

            case "PARENT":
                boolean result_parent = fileExplorer.goToParent();
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0 && result_parent;
                break;

            case "DIRECTORY":
                boolean result_directory = fileExplorer.goToDirectory(pathOne);
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0 && result_directory;
                break;

            case "DELETE":
                boolean result_delete = fileExplorer.deleteFile(pathOne);
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0 && result_delete;
                break;

            case "NEW_DIRECTORY":
                boolean result_new = fileExplorer.createDirectory(pathOne);
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0 && result_new;
                break;

            case "RENAME":
                boolean result_rename = fileExplorer.rename(pathOne, pathTwo);
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0 && result_rename;
                break;

            case "COPY":
                boolean result_copy = fileExplorer.copyFile(pathOne, pathTwo);
                json = fileExplorer.getOngoingDirectoryJSON();
                confirmation = json.length() != 0 && result_copy;
                break;

            case "RECEIVE":
                
                break;

            case "SEND":
                break;

            default:


        }







        // *************************************************************************************** //
        /*

        if(messageIn == null) return;
        String resource = messageIn.substring(5, messageIn.length() - 9);


        if(resource.endsWith(".jpg")) {
            System.out.println("jpg");
            file = new File("Resources/" + resource);
            contentType = "image/jpeg";
        } else if (resource.endsWith(".html")) {
            file = new File("Resources/" + resource);
            contentType = "text/html; charset=UTF-8";
        } else {
            file = new File("Resources/file1.html");
            contentType = "text/html; charset=UTF-8";
        }
        System.out.println("Resource: " + resource);

        messageOut = "HTTP/1.0 200 Document Follows\r\nContent-Type: " + contentType + "\r\nContent-Length: <" + file.length() + "> \r\n\r\n";  //text/html; charset=UTF-8  image/jpeg
        System.out.println("THIRD: response ... " + messageOut.substring(0, 54));

        */
    }

    public void send() {
        if (file == null) return;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            System.out.println(file.getName());
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            bufferedOutputStream.write(messageOut.getBytes(StandardCharsets.UTF_8));
            int totalBytes = 0;
            int ch;
            while ((ch = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(ch);
                totalBytes++;
            }
            bufferedOutputStream.flush();
            System.out.println("FOURTH: total bytes sent: " + totalBytes);
        } catch (IOException e){
                e.printStackTrace();
        }
    }

    public void close() {
        try {
            clientSocket.getOutputStream().close();
            clientSocket.close();
            System.out.println("FIFTH: client socket closed, server socket closed...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
