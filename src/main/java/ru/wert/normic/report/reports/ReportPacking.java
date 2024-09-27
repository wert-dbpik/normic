package ru.wert.normic.report.reports;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.PackingData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;
import static ru.wert.normic.enums.EPacks.*;

public class ReportPacking {

    //Расход на упаковку
    private double cartoon;
    private double cartoonAngle;
    private double stretchMachine;
    private double stretchHand;
    private double polyTape;
    private double bubble;
    private double duct;
    private double pallet;

    private StringBuilder textReport;
    private OpAssm opAssm;

    public ReportPacking(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {
        collectPack(opAssm.getOperations());

        if(cartoon + cartoonAngle + stretchMachine + stretchHand + polyTape + bubble + duct + pallet != 0.0)
            addPackReport(cartoon, cartoonAngle, stretchMachine, stretchHand, polyTape, bubble, duct, pallet);
    }

    /**
     * Сосчитать всю упаковку
     */
    private void collectPack(List<OpData> ops) {
        for (OpData op : ops) {
            if (op instanceof OpAssm || op instanceof OpPack) {
                List<OpData> opsInAssm = ((IOpWithOperations) op).getOperations();
                collectPack(opsInAssm);
            } else if (op instanceof PackingData) {
                cartoon += ((PackingData) op).getCartoon();
                cartoonAngle += ((PackingData) op).getCartoonAngle();
                stretchMachine += ((PackingData) op).getStretchMachineWrap();
                stretchHand += ((PackingData) op).getStretchHandWrap();
                polyTape += ((PackingData) op).getPolyWrap();
                bubble += ((PackingData) op).getBubbleWrap();
                duct += ((PackingData) op).getDuctTape();
                pallet += ((PackingData) op).getPallet();
            }
        }
    }

    /**
     * Добавить отчет по УПАКОВКЕ
     */
    private void addPackReport(double cartoon, double cartoonAngle, double stretchMachine, double stretchHand, double polyTape,
                               double bubble, double duct, double pallet){
        textReport.append("\n\n").append("УПАКОВКА :\n")
                .append("---------------------------\n");
        if (cartoon != 0.0)
            textReport.append(CARTOON.getName())
                    .append(DECIMAL_FORMAT.format(cartoon)).append(" ")
                    .append(CARTOON.getMeasuring()).append("\n");
        if (cartoonAngle != 0.0)
            textReport.append(CARTOON_ANGLE.getName())
                    .append(DECIMAL_FORMAT.format(cartoonAngle)).append(" ")
                    .append(CARTOON_ANGLE.getMeasuring()).append("\n");
        if (stretchMachine != 0.0)
            textReport.append(STRETCH_MACHINE.getName())
                    .append(DECIMAL_FORMAT.format(stretchMachine)).append(" ")
                    .append(STRETCH_MACHINE.getMeasuring()).append("\n");
        if (stretchHand != 0.0)
            textReport.append(STRETCH_HAND.getName())
                    .append(DECIMAL_FORMAT.format(stretchHand)).append(" ")
                    .append(STRETCH_HAND.getMeasuring()).append("\n");
        if (polyTape != 0.0)
            textReport.append(POLY.getName())
                    .append(DECIMAL_FORMAT.format(polyTape)).append(" ")
                    .append(POLY.getMeasuring()).append("\n");
        if (bubble != 0.0)
            textReport.append(BUBBLE.getName())
                    .append(DECIMAL_FORMAT.format(bubble)).append(" ")
                    .append(BUBBLE.getMeasuring()).append("\n");
        if (duct != 0.0)
            textReport.append(DUCT.getName())
                    .append(DECIMAL_FORMAT.format(duct)).append(" ")
                    .append(DUCT.getMeasuring()).append("\n");
        if (pallet != 0.0)
            textReport.append(PALLET.getName())
                    .append(DECIMAL_FORMAT.format(pallet)).append(" ")
                    .append(PALLET.getMeasuring()).append("\n");
        textReport.append("Этикетка-самоклеящаяся 100х80/500\n");
        textReport.append("Этикетка-самоклеящаяся 58х30/1000 ПП Серебро\n");
        textReport.append("Наклейка с логотипом ПИК, 3 цвета\n");
    }

}
