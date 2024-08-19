package ru.wert.normic.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.json.JSONException;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.settings.ColorsSettings;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NvrConverter {

    @Getter private final OpData convertedOpData;
    @Getter private final ColorsSettings colorsSettings;

    public NvrConverter(File file) {
        ArrayList<String> store = getStringsStore(file);
        EOpType opType = getOpDataType(store.get(0));
        colorsSettings = getSettingFromNVRFile(store.get(1));
        convertedOpData = convertOpDataFromNVRFile(store.get(2), opType);
    }

    /**
     * Читает из файла строки и формирует их в одну коллекцию
     * @param file файл .nvr, содержащий json строку
     * @return ArrayList<String> Коллекция строк
     */
    private ArrayList<String> getStringsStore(File file){
        ArrayList<String> store = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                store.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return store;
    }

    /**
     * Определяет тип сохраненных данных  (ДЕТАЛЬ, СБОРКА, УПАКОВКА)
     */
    public EOpType getOpDataType(String jsonString) {
        return EOpType.findOpTypeByName(jsonString);
    }

    /**
     * Определяеет настройки палитры
     */
    public ColorsSettings getSettingFromNVRFile(String jsonString) {
        Gson gson = new Gson();
        Type settingsType = new TypeToken<ColorsSettings>() {
        }.getType();
        return gson.fromJson(jsonString, settingsType);
    }

    /**
     * Выполняет деконвертацию jsonString в объект OpData
     */
    public OpData convertOpDataFromNVRFile(String jsonString, EOpType opType){
        OpData newOpData = null;
        try {
            switch (opType) {
                case DETAIL:
                    newOpData = (OpDetail) OpDataJsonConverter.convert(jsonString);
                    break;
                case ASSM:
                    newOpData = (OpAssm) OpDataJsonConverter.convert(jsonString);
                    break;
                case PACK:
                    newOpData = (OpPack) OpDataJsonConverter.convert(jsonString);
                    break;
            }
        } catch (JSONException ioException) {
            ioException.printStackTrace();
        }
        return newOpData;
    }

}
