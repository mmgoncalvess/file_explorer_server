package com.example.mock;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MockClient {
    private String instruction = "";    //  20 bytes
    private String pathOne = "";    //  260 bytes
    private String pathTwo = "";    //  260 bytes

    private boolean confirmation;   // Confirmation from the server
    private String json;       // Received from the server

    public void connectToServer() {
        instruction = String.format("%-20s", instruction);
        pathOne = String.format("%-260s", pathOne);
        pathTwo = String.format("%-260s", pathTwo);

        String hostName = "localhost";
        int port = 9090;
        try {
            Socket connection = new Socket(hostName, port);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(instruction.getBytes(StandardCharsets.UTF_8));
            outputStream.write(pathOne.getBytes(StandardCharsets.UTF_8));
            outputStream.write(pathTwo.getBytes(StandardCharsets.UTF_8));

            InputStream inputStream = connection.getInputStream();
            int input = inputStream.read();
            if (input == 0) confirmation = false;
            if (input == 1) confirmation = true;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int data = 0;
            while ((data = inputStream.read()) != -1) {
                byteArrayOutputStream.write(data);
            }

            json = byteArrayOutputStream.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setPathOne(String pathOne) {
        this.pathOne = pathOne;
    }

    public void setPathTwo(String pathTwo) {
        this.pathTwo = pathTwo;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public String getJson() {
        return json;
    }
}
