package ru.wert.normic.utils;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.entities.ops.opAssembling.*;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.*;
import ru.wert.normic.entities.ops.opPack.*;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.opTurning.*;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;

import java.util.ArrayList;
import java.util.List;

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
        else if(opData instanceof OpPack){
            List<OpData> opDataList = getOperations(jsonObject);
            ((OpPack)opData).setOperations(opDataList);
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

            //ОПЕРАЦИИ С ЛИСТОМ

            case "CUTTING":
                return gson.fromJson(op.toString(), OpCutting.class);
            case "BENDING":
                return gson.fromJson(op.toString(), OpBending.class);

            //СБОРОЧНЫЕ ОПЕРАЦИИ

            case "DETAIL":
                return gson.fromJson(op.toString(), OpDetail.class);
            case "ASSM":
                return gson.fromJson(op.toString(), OpAssm.class);
            case "ASSM_CUTTINGS":
                return gson.fromJson(op.toString(), OpAssmCutting.class);
            case "ASSM_NODES":
                return gson.fromJson(op.toString(), OpAssmNode.class);
            case "ASSM_NUTS":
                return gson.fromJson(op.toString(), OpAssmNut.class);
            case "LEVELING_SEALER":
                return gson.fromJson(op.toString(), OpLevelingSealer.class);
            case "THERMO_INSULATION":
                return gson.fromJson(op.toString(), OpThermoInsulation.class);
            case "ASSM_CHOP_OFF":
                return gson.fromJson(op.toString(), OpAssmChopOff.class);

            //ОКРАШИВАНИЕ

            case "PAINTING":
                return gson.fromJson(op.toString(), OpPaint.class);
            case "PAINT_ASSM":
                return gson.fromJson(op.toString(), OpPaintAssm.class);

            //СЛЕСАРНЫЕ ОПЕРАЦИИ

            case "LOCKSMITH":
                return gson.fromJson(op.toString(), OpLocksmith.class);
            case "ASSM_NUTS_MK":
                return gson.fromJson(op.toString(), OpAssmNutMK.class);
            case "CHOP_OFF":
                return gson.fromJson(op.toString(), OpChopOff.class);
            case "DRILLING_BY_MARKING":
                return gson.fromJson(op.toString(), OpDrillingByMarking.class);
            case "CUT_OFF_ON_SAW":
                return gson.fromJson(op.toString(), OpCutOffOnTheSaw.class);

            //ТОКАРНЫЕ ОПЕРАЦИИ

            case "LATHE_MOUNT_DISMOUNT":
                return gson.fromJson(op.toString(), OpLatheMountDismount.class);
            case "LATHE_TURNING":
                return gson.fromJson(op.toString(), OpLatheTurning.class);
            case "LATHE_CUT_GROOVE":
                return gson.fromJson(op.toString(), OpLatheCutGroove.class);
            case "LATHE_THREADING":
                return gson.fromJson(op.toString(), OpLatheThreading.class);
            case "LATHE_DRILLING":
                return gson.fromJson(op.toString(), OpLatheDrilling.class);
            case "LATHE_ROLLING":
                return gson.fromJson(op.toString(), OpLatheRolling.class);
            case "LATHE_CUT_OFF":
                return gson.fromJson(op.toString(), OpLatheCutOff.class);

            //СВАРОЧНЫЕ ОПЕРАЦИИ

            case "WELD_CONTINUOUS":
                return gson.fromJson(op.toString(), OpWeldContinuous.class);
            case "WELD_DOTTED":
                return gson.fromJson(op.toString(), OpWeldDotted.class);

            //УПАКОВКА

            case "PACK":
                return gson.fromJson(op.toString(), OpPack.class);
            case "PACK_ON_PALLET":
                return gson.fromJson(op.toString(), OpPackOnPallet.class);
            case "PACK_IN_MACHINE_STRETCH_WRAP":
                return gson.fromJson(op.toString(), OpPackInMachineStretchWrap.class);
            case "PACK_IN_HAND_STRETCH_WRAP":
                return gson.fromJson(op.toString(), OpPackInHandStretchWrap.class);
            case "PACK_IN_CARTOON_BOX":
                return gson.fromJson(op.toString(), OpPackInCartoonBox.class);
            case "PACK_IN_BUBBLE_WRAP":
                return gson.fromJson(op.toString(), OpPackInBubbleWrap.class);

            default:
                return new OpErrorData(opType);
        }
    }


}
