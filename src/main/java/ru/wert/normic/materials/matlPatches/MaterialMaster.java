package ru.wert.normic.materials.matlPatches;

import ru.wert.normic.entities.opAssm.OpDetail;
import ru.wert.normic.entities.db_connection.material.Material;

import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;

public class MaterialMaster {
    //======================     ЛИСТЫ  =========================================
    public static double countListWeight(OpDetail detail, Material material){
        int paramA = detail.getParamA();
        int paramB = detail.getParamB();
        double wasteRatio = detail.getWasteRatio();
        double t = material.getParamS();
        double ro = material.getParamX();
        return paramA * paramB * t * ro * MM2_TO_M2 * wasteRatio;
    }

    public static double countListArea(OpDetail detail){
        int paramA = detail.getParamA();
        int paramB = detail.getParamB();
        return 2 * paramA * paramB * MM2_TO_M2;
    }

    //======================     КРУГИ  =========================================
    public static double countRoundWeight(OpDetail detail, Material material){
        int length = detail.getParamA();
        int cut = detail.getParamB();
        double wasteRatio = detail.getWasteRatio();
        double weightPerMeter = material.getParamX();
        return (length + cut) * MM_TO_M * weightPerMeter * wasteRatio;
    }

    public static double countRoundArea(OpDetail detail, Material material){
        int length = detail.getParamA();
        int cut = detail.getParamB();
        double radius = material.getParamS();
        return 3.14 * radius * (length + cut) * MM2_TO_M2;
    }

    //======================     ПРОФИЛИ  =========================================
    public static double countProfileWeight(OpDetail detail, Material material){
        int length = detail.getParamA();
        int cut = detail.getParamB();
        double wasteRatio = detail.getWasteRatio();
        double weightPerMeter = material.getParamX();
        return (length + cut) * MM_TO_M * weightPerMeter * wasteRatio;
    }

    public static double countProfileArea(OpDetail detail, Material material){
        int length = detail.getParamA();
        int cut = detail.getParamB();
        double perimeter = material.getParamS();
        return perimeter * (length + cut) * MM2_TO_M2;
    }

}
