package ru.wert.normic.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/**
 * Класс содержит статические методы оперирования фалами
 * - Создает временную папку
 * - Создает во временной папке копию файла oldFile
 */
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

    /**
     * Создает во временной папке копию файла oldFile
     * Применяется при открытии Excel файлов
     */
    public File createTempCopyOfFile(File oldFile){
        File copied = null;
        try {
//          copied = File.createTempFile(oldFile.getName(), "excel.tmp", tempDir);  //не работает
            copied = new File(tempDir, oldFile.getName() + new Date().getTime());
            Files.copy(oldFile.toPath(), copied.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copied;
    }

    /**
     * Создает временную директорию
     */
    public static void createTempDir() throws IOException {

        tempDir = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(tempDir.delete()))
            throw new IOException("Could not delete temp file: " + tempDir.getAbsolutePath());

        if (!(tempDir.mkdir()))
            throw new IOException("Could not create temp directory: " + tempDir.getAbsolutePath());


    }
}
