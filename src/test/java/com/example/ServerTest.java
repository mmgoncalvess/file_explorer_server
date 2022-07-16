package com.example;

import com.example.mock.MockClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void startServer() {


    }

    @Test
    void caseOngoingDirectory() {
        MockClient mockClient = new MockClient();
        mockClient.setInstruction("ONGOING_DIRECTORY");
        mockClient.connectToServer();
        String json = mockClient.getJson();
        boolean confirmation = mockClient.isConfirmation();
        assertTrue(confirmation);
        assertTrue(json.length() > 10);
        //System.out.println(json);
    }

    @Test
    void caseParent() {
        MockClient mockClient = new MockClient();
        mockClient.setInstruction("ONGOING_DIRECTORY");
        mockClient.connectToServer();
        String json = mockClient.getJson();
        boolean confirmation = mockClient.isConfirmation();
        assertTrue(confirmation);
        ObjectMapper mapper = new ObjectMapper();
        String currentDirectory = "";
        try {
            OngoingDirectory ongoingDirectory = mapper.readValue(json, OngoingDirectory.class);
            currentDirectory = ongoingDirectory.getCurrentDirectory();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        File currentDirectoryFile = new File(currentDirectory);
        File parentDirectory = currentDirectoryFile.getParentFile();
        String expectedParentDirectory = parentDirectory.getAbsolutePath();

        mockClient = new MockClient();
        mockClient.setInstruction("PARENT");
        mockClient.connectToServer();
        json = mockClient.getJson();
        confirmation = mockClient.isConfirmation();
        assertTrue(confirmation);
        try {
            OngoingDirectory ongoingDirectory = mapper.readValue(json, OngoingDirectory.class);
            String actualParentDirectory = ongoingDirectory.getCurrentDirectory();
            assertEquals(expectedParentDirectory, actualParentDirectory);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //System.out.println(json);
    }

    @Test
    void caseDirectory() {
        MockClient mockClient = new MockClient();
        mockClient.setInstruction("DIRECTORY");
        String expectedDirectoryString = System.getProperty("user.dir") + "\\src\\test\\java\\com\\example\\mock";
        mockClient.setPathOne(expectedDirectoryString);
        mockClient.connectToServer();
        String json = mockClient.getJson();
        boolean confirmation = mockClient.isConfirmation();
        assertTrue(confirmation);
        ObjectMapper mapper = new ObjectMapper();
        try {
            OngoingDirectory ongoingDirectory = mapper.readValue(json, OngoingDirectory.class);
            String actualDirectoryString = ongoingDirectory.getCurrentDirectory();
            assertEquals(expectedDirectoryString, actualDirectoryString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //System.out.println(json);
    }






}
