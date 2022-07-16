package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class FileExplorer {
    private File currentDirectory;

    public boolean goToParent() {
        if (currentDirectory == null) return false;
        currentDirectory = currentDirectory.getParentFile();
        return true;
    }

    public boolean goToDirectory(String absolutePath) {
        File file = new File(absolutePath);
        boolean directoryExist = file.exists() && file.isDirectory();
        if (directoryExist) currentDirectory = file;
        return directoryExist;
    }

    public OngoingDirectory getOngoingDirectory() {
        OngoingDirectory ongoingDirectory = new OngoingDirectory();
        if (currentDirectory == null) {
            ongoingDirectory.setCurrentDirectory("");
            ArrayList<String> arrayList = new ArrayList<>();
            File[] roots = File.listRoots();
            for (File root : roots) {
                arrayList.add(root.getAbsolutePath());
            }
            ongoingDirectory.setDirectories(arrayList);
            ongoingDirectory.setFiles(new HashMap<>());
        } else {
            ongoingDirectory.setCurrentDirectory(currentDirectory.getAbsolutePath());
            ArrayList<String> directoriesList = new ArrayList<>();
            HashMap<String, Integer> filesCollection = new HashMap<>();
            File[] files = currentDirectory.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) directoriesList.add(file.getName());
                if (file.isFile()) {
                    try {
                        filesCollection.put(file.getName(), (int) Files.size(file.toPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        filesCollection.put(file.getName(), 0);
                    }
                }
            }
            ongoingDirectory.setDirectories(directoriesList);
            ongoingDirectory.setFiles(filesCollection);
        }
        return ongoingDirectory;
    }

    public boolean deleteFile(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists()) return file.delete();
        return false;
    }

    public boolean createDirectory(String absolutePath) {
        File file = new File(absolutePath);
        return file.mkdir();
    }

    public boolean copyFile(String sourceAbsolutePath, String targetAbsolutePath) {
        File source = new File(sourceAbsolutePath);
        File target = new File(targetAbsolutePath);
        Path path = null;
        if (source.exists() && !target.exists()) {
            try {
                path = Files.copy(source.toPath(), target.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return target.toPath().equals(path);
    }

    public boolean rename(String oldName, String newName) {
        File oldNameFile = new File(oldName);
        File newNameFile= new File(newName);
        if (oldNameFile.exists() && !newNameFile.exists() && oldNameFile.getParent().equals(newNameFile.getParent())) {
            return oldNameFile.renameTo(newNameFile);
        }
        return false;
    }

    public String getOngoingDirectoryJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        OngoingDirectory ongoingDirectory = getOngoingDirectory();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(ongoingDirectory);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
