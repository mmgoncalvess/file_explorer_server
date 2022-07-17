package com.example.mock;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MockClient {
    private String instruction = "";    //  20 bytes
    private String pathOne = "";    //  260 bytes
    private String pathTwo = "";    //  260 bytes
    private int size = 0;           //  10 bytes

    private boolean confirmation;   // Confirmation from the server
    private String json;       // Received from the server

    public void connectToServer() {
        instruction = String.format("%-20s", instruction);
        pathOne = String.format("%-260s", pathOne);
        pathTwo = String.format("%-260s", pathTwo);
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.putInt(size);
        byteBuffer.rewind();
        byte[] sizeArrayBytes = byteBuffer.array();

        String hostName = "localhost";
        int port = 9090;
        try {
            Socket connection = new Socket(hostName, port);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(instruction.getBytes(StandardCharsets.UTF_8));
            outputStream.write(pathOne.getBytes(StandardCharsets.UTF_8));
            outputStream.write(pathTwo.getBytes(StandardCharsets.UTF_8));
            outputStream.write(sizeArrayBytes);
            if (instruction.trim().equals("RECEIVE")) sendFile(outputStream, pathOne);
            outputStream.flush();

            InputStream inputStream = connection.getInputStream();
            int input = inputStream.read();
            if (input == 0) confirmation = false;
            if (input == 1) confirmation = true;

            if (instruction.trim().equals("SEND")) {
                File file = new File(pathTwo);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                int data;
                int count = 0;
                while ((data = inputStream.read()) != -1) {
                    fileOutputStream.write(data);
                    count++;
                }
                fileOutputStream.close();
                System.out.println(count + " bytes received by client... !!!");
                return;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int data = 0;
            while ((data = inputStream.read()) != -1) {
                byteArrayOutputStream.write(data);
            }
            json = byteArrayOutputStream.toString();

            byteArrayOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean sendFile(OutputStream outputStream, String pathOne) {
        File file = new File(pathOne);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int data;
            while ((data = fileInputStream.read()) != -1) {
                outputStream.write(data);
            }
            fileInputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public String getJson() {
        return json;
    }
}
