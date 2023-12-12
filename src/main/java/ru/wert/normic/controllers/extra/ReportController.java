package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.entities.ops.opPack.PackingData;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.NormicServices.DENSITIES;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.EPacks.*;

/**
 * ОТЧЕТ
 */
public class ReportController {

    @FXML
    private TextArea taReport;

    private OpAssm opAssm;
    private Map<Material, Double> materials;
    private Density steelDensity;
    private double steelScrap; //переменная для расчета лома
    private  StringBuilder textReport;

    //Расход на наливное уплотнение
    private double componentA; //Компонент полиэфирный А
    private double componentB; //Компонент изоцинат Б

    //Расход на упаковку
    private double cartoon;
    private double cartoonAngle;
    private double stretchMachine;
    private double stretchHand;
    private double polyTape;
    private double bubble;
    private double duct;
    private double pallet;

    public ReportController() {
        steelDensity = DENSITIES.findByName("сталь");
    }

    public void init(OpAssm opAssm){
        this.opAssm = opAssm;

        //Сбрасываем данные преред создание отчета
        componentA = 0.0;
        componentB = 0.0;

        textReport = new StringBuilder();

        //Наименование изделия
        String name = opAssm.getName();
        textReport.append("ИЗДЕЛИЕ : ").append(name == null? "< без наименования >" : name);

        //Материалы
        materials = new HashMap<>();
        List<OpData> ops = opAssm.getOperations();
        collectMaterialsByOpData(ops);
        if(!materials.isEmpty())
            addMaterialsReport();

        //Лом стали
        if(steelScrap != 0.0)
            textReport.append("\nЛОМ СТАЛИ (10%): ").append(String.format(DOUBLE_FORMAT, steelScrap)).append(" кг.");

        //Наливной уплотнитель
        collectComponentsABByOpData(ops);
        if(componentA != 0.0)
            addLevelingSealerReport();

        //Покрытие

        List<Double> ral1 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_I);
        List<Double> ral2 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_II);
        List<Double> ral3 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_III);

        if(ral1.get(0) + ral2.get(0) + ral3.get(0) != 0.0)
            addColorReport(ral1, ral2, ral3);

        //УПАКОВКА
        collectPack(ops);

        if(cartoon + cartoonAngle + stretchMachine + stretchHand + polyTape + bubble + duct + pallet != 0.0)
            addPackReport(cartoon, cartoonAngle, stretchMachine, stretchHand, polyTape, bubble, duct, pallet);


        //НОРМЫ ВРЕМЕНИ

        if(opAssm.getMechTime() != 0.0 ||
                opAssm.getPaintTime() != 0.0 ||
                opAssm.getAssmTime() != 0.0 ||
                opAssm.getPackTime() != 0.0)
            addNormTimesReport(opAssm);

        taReport.setText(textReport.toString());

    }

//==========   МАТЕРИАЛЫ  ===================================================

    /**
     * Добавить отчет по ИСПОЛЬЗУЕМЫМ МАТЕРИАЛАМ
     */
    private void addMaterialsReport() {
        textReport.append("\n\n").append("МАТЕРИАЛЫ :\n");
        for(Material m : materials.keySet()){
            textReport.append(m.getName()).append("\t: ").append(String.format(DOUBLE_FORMAT, materials.get(m))).append(" кг.\n");
        }
    }

    /**
     * Сосчитать все МАТЕРИАЛЫ
     */
    private void collectMaterialsByOpData(List<OpData> ops) {
        Density steelDensity = DENSITIES.findByName("сталь");
        for (OpData op : ops) {
            if (op instanceof OpDetail) {
                Material m = ((OpDetail) op).getMaterial();
                //Детали, если не открывать редактор детали, материала не содержат
                if (m == null) continue;
                if (materials.containsKey(m)) {
                    //Прибавляем новый вес к полученному ранее материалу
                    double sumWeight = materials.get(m) + ((OpDetail) op).getWeight() * op.getQuantity();
                    materials.put(m, sumWeight);
                } else {
                    //Добавляем новый материал и массу
                    materials.put(m, ((OpDetail) op).getWeight() * op.getQuantity());
                }
                if(steelDensity != null && m.getParamX() == steelDensity.getAmount())
                    steelScrap += ((OpDetail) op).getWeight() * op.getQuantity() * 0.1;

            } else if (op instanceof OpAssm) {
                List<OpData> operations = ((OpAssm) op).getOperations();
                collectMaterialsByOpData(operations);
            }

        }
    }

//==========   НАЛИВНОЙ УПЛОТНИТЕЛЬ  ===================================================

    /**
     * Добавить отчет по НАЛИВНОМУ УПЛОТНЕНИЮ
     */
    private void addLevelingSealerReport() {
        textReport.append("\n\n").append("НАЛИВНОЙ УПЛОТНИТЕЛЬ :\n");
        textReport.append("Компонент полиэфирный\tА = ").append(DECIMAL_FORMAT.format(componentA)).append(" кг.\n");
        textReport.append("Компонент изоцинат\t\tБ = ").append(DECIMAL_FORMAT.format(componentB)).append(" кг.\n");
    }

    /**
     * Сосчитать матириалы по НАЛИВНОМУ УПЛОТНЕНИЮ
     */
    private void collectComponentsABByOpData(List<OpData> ops){
        for (OpData op : ops) {
            if(op instanceof OpAssm){
                List<OpData> opsInAssm = ((OpAssm)op).getOperations();
                collectComponentsABByOpData(opsInAssm);
            }
            else if(op instanceof OpLevelingSealer){
                componentA += ((OpLevelingSealer) op).getCompA();
                componentB += ((OpLevelingSealer) op).getCompB();
            }
        }
    }

//==========   ПОКРАСКА   ===================================================

    /**
     * Добавить отчет по РАСХОДУ КРАСКИ (начало)
     */
    private void addColorReport(List<Double> ral1, List<Double> ral2, List<Double> ral3) {
        textReport.append("\n\n").append("ПОКРЫТИЕ :\n");
        if(ral1.get(0) != 0.0) addRal1Report(ral1, EColor.COLOR_I);
        if(ral2.get(0) != 0.0) addRal1Report(ral2, EColor.COLOR_II);
        if(ral3.get(0) != 0.0) addRal1Report(ral3, EColor.COLOR_III);
    }
    /**
     * Добавить отчет по РАСХОДУ КРАСКИ (по конкретной краске)
     */
    private void addRal1Report(List<Double> ral1, EColor color) {
        textReport.append("Краска '")
                .append(color.getRal())
                .append("', площадь = ")
                .append(DECIMAL_FORMAT.format(ral1.get(0)))
                .append(" м.кв., ")
                .append("расход = ")
                .append(DECIMAL_FORMAT.format(ral1.get(1)))
                .append(" кг.\n");
    }

    /**
     * Сосчитать СУММАРНУЮ ПЛОЩАДЬ и РАСХОД краски
     */
    private List<Double> collectListOfOperationsInOpData(IOpWithOperations op, EColor color){
        double area = 0.0;
        double weight = 0.0;
        for(OpData o : op.getOperations()){
            if(o instanceof IOpWithOperations) {
                List<Double> ress = collectListOfOperationsInOpData((IOpWithOperations) o, color);
                area += ress.get(0);
                weight += ress.get(1);
            }else{
                if(o instanceof OpPaint && ((OpPaint)o).getColor().equals(color)) {
                    area += ((OpPaint) o).getArea() * op.getOpData().getQuantity();
                    weight += ((OpPaint) o).getDyeWeight() * op.getOpData().getQuantity();
                }else if(o instanceof OpPaintAssm && ((OpPaintAssm)o).getColor().equals(color)) {
                    area += ((OpPaintAssm) o).getArea() * op.getOpData().getQuantity();
                    weight += ((OpPaintAssm) o).getDyeWeight() * op.getOpData().getQuantity();
                }
            }

        }
        return Arrays.asList(area, weight);
    }


//==========   УПАКОВКА   ===================================================
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
        textReport.append("\n\n").append("УПАКОВКА :\n");
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

//==========   НОРМЫ ВРЕМЕНИ   ===================================================

    private void addNormTimesReport(OpAssm opAssm) {
        double k = 1.0; //Коэффициент перевода в ед.измерения
        switch(CURRENT_MEASURE.name()){
            case "SEC" : k = MIN_TO_SEC; break;
            case "HOUR" : k = MIN_TO_HOUR; break;
        }
        textReport.append("\n\n").append("НОРМЫ ВРЕМЕНИ :\n");

        if (opAssm.getMechTime() != 0.0)
            textReport.append("Изготовление \t: ")
                    .append(DECIMAL_FORMAT.format(opAssm.getMechTime() * k)).append(" ")
                    .append(CURRENT_MEASURE.getMeasure()).append("\n");
        if (opAssm.getPaintTime() != 0.0)
            textReport.append("Покраска \t\t: ")
                    .append(DECIMAL_FORMAT.format(opAssm.getPaintTime() * k)).append(" ")
                    .append(CURRENT_MEASURE.getMeasure()).append("\n");
        if (opAssm.getAssmTime() != 0.0)
            textReport.append("Сборка \t\t\t: ")
                    .append(DECIMAL_FORMAT.format(opAssm.getAssmTime() * k)).append(" ")
                    .append(CURRENT_MEASURE.getMeasure()).append("\n");
        if (opAssm.getPackTime() != 0.0)
            textReport.append("Упаковка \t\t: "
            ).append(DECIMAL_FORMAT.format(opAssm.getPackTime() * k)).append(" ")
                    .append(CURRENT_MEASURE.getMeasure()).append("\n");
    }
}
