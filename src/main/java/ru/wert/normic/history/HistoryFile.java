package ru.wert.normic.history;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HistoryFile {

    final int MAX = 20;
    final String homeDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "NormIC";
    final String historyFilePath = homeDir + File.separator + "history.txt";
    File historyFile = new File(historyFilePath);

    static HistoryFile instance;

    public static HistoryFile getInstance() {
        if (instance == null)
            instance = new HistoryFile();
        return instance;
    }

    private HistoryFile(){
        File h = new File(historyFilePath);
        if(!h.exists()) {
            try {
                h.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //================================================================================================

    public void addFileToHistory(File file){
        String newPath = file.getAbsolutePath();
        List<String> history = loadHistory();
        if(history.contains(newPath)) {
            int pos = history.indexOf(newPath);
            history.add(0, newPath);
            history.remove(pos + 1);
        } else{
            history.add(0, newPath);
        }
        //Укорачивваем список до максимально допустимого размера
        if(history.size() > MAX)
            history = history.subList(0, MAX);
        //Сохраняем полученный список на диск
        saveToFile(createStringToSave(history));
    }

    /**
     * Читает файл и формирует из него список из элементов истории поиска
     * @return ObservableList<String> список элементов поиска
     */
    public List<String> loadHistory(){
        List<String> history = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(historyFilePath), StandardCharsets.UTF_8));
            String st;
            while ((st = br.readLine()) != null)
                history.add(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Загружает данные с элементами поиска в файл
     * @param searchHistoryItems String , элементы истории каждый в отдельной строке
     */
    private void saveToFile(String searchHistoryItems){
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(historyFilePath), StandardCharsets.UTF_8));
            writer.write(searchHistoryItems);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создает строку для последующего сохранения в файле
     * @return String
     */
    private String createStringToSave(List<String> history){
        StringBuilder sb = new StringBuilder();
        for(String str : history)
            sb.append(str).append("\n");
        return sb.toString();
    }

}
