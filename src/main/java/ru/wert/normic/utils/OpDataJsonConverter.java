package ru.wert.normic.utils;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.entities.ops.electrical.*;
import ru.wert.normic.entities.ops.opAssembling.*;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.*;
import ru.wert.normic.entities.ops.opPack.*;
import ru.wert.normic.entities.ops.opPaint.OpPaintOld;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.opPaint.OpPaintDetail;
import ru.wert.normic.entities.ops.opTurning.*;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.entities.ops.opWelding.OpWeldDifficulty;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.entities.ops.simpleOperations.OpSimpleOperation;
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
                return gson.fromJson(op.toString(), OpPaintOld.class);
            case "PAINT_DETAIL":
                return gson.fromJson(op.toString(), OpPaintDetail.class);
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
            case "WELD_DIFFICULTY":
                return gson.fromJson(op.toString(), OpWeldDifficulty.class);
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

                //ПРОЧИЕ ОПЕРАЦИИ
            case "SIMPLE_OPERATION":
                return gson.fromJson(op.toString(), OpSimpleOperation.class);

            //ЭЛЕКТРОМОНТАЖ
            case "EL_MOUNT_ON_DIN":
                return gson.fromJson(op.toString(), OpMountOnDinAutomats.class);
            case "EL_MOUNT_ON_SCREWS_NO_DISASSM_2":
                return gson.fromJson(op.toString(), OpMountOnScrewsNoDisAssm2.class);
            case "EL_MOUNT_ON_SCREWS_NO_DISASSM_4":
                return gson.fromJson(op.toString(), OpMountOnScrewsNoDisAssm4.class);
            case "EL_MOUNT_ON_SCREWS_WITH_DISASSM_2":
                return gson.fromJson(op.toString(), OpMountOnScrewsWithDisAssm2.class);
            case "EL_MOUNT_ON_SCREWS_WITH_DISASSM_4":
                return gson.fromJson(op.toString(), OpMountOnScrewsWithDisAssm4.class);
            case "EL_MOUNT_ON_VSHG":
                return gson.fromJson(op.toString(), OpMountOnVSHG.class);
            case "EL_CONNECT_DEVICE_MORTISE_CONTACT":
                return gson.fromJson(op.toString(), OpConnectDeviceMortiseContact.class);
            case "EL_CONNECT_DEVICE_SPRING_CLAMP":
                return gson.fromJson(op.toString(), OpConnectDeviceSpringClamp.class);
            case "EL_CONNECT_DEVICE_CLAMPING_SCREW":
                return gson.fromJson(op.toString(), OpConnectDeviceClampingScrew.class);
            case "EL_CONNECT_DEVICE_VSHG":
                return gson.fromJson(op.toString(), OpConnectDeviceVSHG.class);
            case "EL_CUT_CABLE_HANDLY_MC6":
                return gson.fromJson(op.toString(), OpCutCableHandlyMC6.class);
            case "EL_CUT_CABLE_HANDLY_MC15":
                return gson.fromJson(op.toString(), OpCutCableHandlyMC15.class);
            case "EL_CUT_CABLE_HANDLY_SC":
                return gson.fromJson(op.toString(), OpCutCableHandlySC.class);
            case "EL_CUT_CABLE_ON_MACHINE":
                return gson.fromJson(op.toString(), OpCutCableOnMachine.class);
            case "EL_CUT_METAL_SLEEVE":
                return gson.fromJson(op.toString(), OpCutMetalSleeve.class);
            case "EL_CUT_CABLE_CHANNEL":
                return gson.fromJson(op.toString(), OpCutCableChannel.class);
            case "EL_TINNING_IN_BATHE":
                return gson.fromJson(op.toString(), OpTinningInBathe.class);
            case "EL_TINNING":
                return gson.fromJson(op.toString(), OpTinning.class);
            case "EL_MOUNT_TIP_ON_CABLE":
                return gson.fromJson(op.toString(), OpMountTipOnCable.class);
            case "EL_MOUNT_TIP_ON_POWER_CABLE":
                return gson.fromJson(op.toString(), OpMountTipOnPowerCable.class);
            case "EL_MARKING ":
                return gson.fromJson(op.toString(), OpMarking.class);
            case "EL_MOUNT_OF_SIGNAL_EQUIP":
                return gson.fromJson(op.toString(), OpMountOfSignalEquip.class);
            case "EL_SOLDERING":
                return gson.fromJson(op.toString(), OpSoldering.class);
            case "EL_MOUNT_OF_CABLE_ENTRIES":
                return gson.fromJson(op.toString(), OpMountOfCableEntries.class);
            case "EL_FIX_OF_CABLES":
                return gson.fromJson(op.toString(), OpFixOfCables.class);
            case "EL_ISOLATE_WITH_THERM_TUBE10":
                return gson.fromJson(op.toString(), OpIsolateWithThermTube10.class);
            case "EL_ISOLATE_WITH_THERM_TUBE30":
                return gson.fromJson(op.toString(), OpIsolateWithThermTube30.class);



            default:
                return new OpErrorData(opType);
        }
    }


}
