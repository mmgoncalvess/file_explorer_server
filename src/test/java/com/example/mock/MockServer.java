package com.example.mock;

import com.example.Server;

public class MockServer {
    public static void main(String[] args) {
        Server server = new Server(9090);
        server.startServer();
    }
}
