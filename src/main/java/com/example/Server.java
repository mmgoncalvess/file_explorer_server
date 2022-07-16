package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int portNumber;
    private ServerSocket serverSocket;

    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(portNumber);
            FileExplorer fileExplorer = new FileExplorer();
            while (true) {
                Dispatcher dispatcher = new Dispatcher(connect(), fileExplorer);
                dispatcher.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket connect() {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();    // Block while waiting for client connections
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientSocket;
    }
}
