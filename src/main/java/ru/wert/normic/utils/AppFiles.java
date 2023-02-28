package ru.wert.normic.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AppFiles {

    private static AppFiles instance;
    private static File tempDir;

    private AppFiles() {
        try {
            createTempDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = this;
    }

    public static AppFiles getInstance(){
        if(instance == null)
            return new AppFiles();
        else
            return instance;
    }

    public File createTempCopyOfFile(File oldFile){
        File copied = null;
        try {
            copied = new File(tempDir, oldFile.getName());
            Files.copy(oldFile.toPath(), copied.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copied;
    }

    public static void createTempDir() throws IOException {

        tempDir = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(tempDir.delete()))
            throw new IOException("Could not delete temp file: " + tempDir.getAbsolutePath());

        if (!(tempDir.mkdir()))
            throw new IOException("Could not create temp directory: " + tempDir.getAbsolutePath());


    }
}
