package ru.wert.normic.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.json.JSONException;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.entities.saves.SaveNormEntry;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.settings.ProductSettings;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.SAVES_HISTORY;

public class NvrConverter {

    @Getter private final OpData convertedOpData;
    @Getter private final ProductSettings productSettings;

    public NvrConverter(File file) {
        ArrayList<String> store = getStringsStore(file);
        EOpType opType = getOpDataType(store.get(0));
        productSettings = getSettingFromNVRFile(store.get(1));
        convertedOpData = convertOpDataFromNVRFile(store.get(2), opType);

        try {
            String savesHistoryString = store.get(3);
            if (savesHistoryString != null || !savesHistoryString.isEmpty())
                SAVES_HISTORY = convertSavesHistoryFromNVRFile(savesHistoryString);
        } catch (Exception e) {
            SAVES_HISTORY = new ArrayList<>();
        }
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
    public ProductSettings getSettingFromNVRFile(String jsonString) {
        Gson gson = new Gson();
        Type settingsType = new TypeToken<ProductSettings>() {
        }.getType();
        return gson.fromJson(jsonString, settingsType);
    }

    /**
     * Определяеет настройки палитры
     */
    public List<SaveNormEntry> convertSavesHistoryFromNVRFile(String jsonString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<SaveNormEntry>>(){}.getType();
        return gson.fromJson(jsonString, listType);
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
