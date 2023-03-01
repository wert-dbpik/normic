package ru.wert.normic.utils;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.*;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.OpLocksmith;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class OpDataJsonConverter {

    static Gson gson;

    public static OpData convert(String jsonString) throws JSONException {
        gson = new GsonBuilder().disableHtmlEscaping().create();
        return convertOpData(new JSONObject(jsonString));
    }

    private static OpData convertOpData(JSONObject jsonObject) throws JSONException {
        OpData opData = convertOpType(jsonObject);
        if(opData instanceof OpAssm){
            List<OpData> opDataList = getOperations(jsonObject);
            ((OpAssm)opData).setOperations(opDataList);
        }
        else if(opData instanceof OpDetail){
            List<OpData> opDataList = getOperations(jsonObject);
            ((OpDetail)opData).setOperations(opDataList);
        }

        return opData;
    }

    private static List<OpData> getOperations(JSONObject jsonObject) throws JSONException {
        JSONArray array = (JSONArray) jsonObject.get("operations");
        List<OpData> opDataList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject op = array.getJSONObject(i);
            opDataList.add(convertOpData(op));
        }
        return opDataList;
    }

    private static OpData convertOpType(JSONObject op) throws JSONException {
        String opType = (String) op.get("opType");
        switch (opType) {
            case "DETAIL":
                return gson.fromJson(op.toString(), OpDetail.class);
            case "ASSM":
                return gson.fromJson(op.toString(), OpAssm.class);
            case "CUTTING":
                return gson.fromJson(op.toString(), OpCutting.class);
            case "BENDING":
                return gson.fromJson(op.toString(), OpBending.class);
            case "LOCKSMITH":
                return gson.fromJson(op.toString(), OpLocksmith.class);
            case "PAINTING":
                return gson.fromJson(op.toString(), OpPaint.class);
            case "PAINT_ASSM":
                return gson.fromJson(op.toString(), OpPaintAssm.class);
            case "WELD_CONTINUOUS":
                return gson.fromJson(op.toString(), OpWeldContinuous.class);
            case "WELD_DOTTED":
                return gson.fromJson(op.toString(), OpWeldDotted.class);
            case "ASSM_CUTTINGS":
                return gson.fromJson(op.toString(), OpAssmCutting.class);
            case "ASSM_NUTS":
                return gson.fromJson(op.toString(), OpAssmNut.class);
            case "ASSM_NODES":
                return gson.fromJson(op.toString(), OpAssmNode.class);
            case "LEVELING_SEALER":
                return gson.fromJson(op.toString(), OpLevelingSealer.class);
            default:
                throw new NoSuchElementException(String.format("Используется незарегистрированный тип плашки '%s'", opType));
        }
    }


}
