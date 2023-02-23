package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EPacks;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;
import static ru.wert.normic.enums.EPacks.*;

/**
 * ОТЧЕТ
 */
public class ReportController {

    @FXML
    private TextArea taReport;

    private OpAssm opAssm;
    private Map<Material, Double> materials;
    private  StringBuilder report;

    //Расход на наливное уплотнение
    private double componentA; //Компонент полиэфирный А
    private double componentB; //Компонент изоцинат Б

    public void init(OpAssm opAssm){
        this.opAssm = opAssm;

        //Сбрасываем данные преред создание отчета
        componentA = 0.0;
        componentB = 0.0;

        report = new StringBuilder();

        //Наименование изделия
        String name = opAssm.getName();
        report.append("ИЗДЕЛИЕ : ").append(name == null? "< без наименования >" : name);

        //Материалы
        materials = new HashMap<>();
        List<OpData> ops = opAssm.getOperations();
        collectMaterialsByOpData(ops);
        if(!materials.isEmpty())
            addMaterialsReport();

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
        double cartoon = collectPacksOverOperations(ops, CARTOON, 0.0);
        double stretchMachine = collectPacksOverOperations(ops, EPacks.STRETCH_MACHINE, 0.0);
        double stretchHand = collectPacksOverOperations(ops, STRETCH_HAND, 0.0);
        double polyTape = collectPacksOverOperations(ops, EPacks.POLY, 0.0);
        double bubble = collectPacksOverOperations(ops, EPacks.BUBBLE, 0.0);
        double duct = collectPacksOverOperations(ops, EPacks.DUCT, 0.0);
        double pallet = collectPacksOverOperations(ops, EPacks.PALLET, 0.0);

        if(cartoon + stretchMachine + stretchHand + polyTape + bubble + duct + pallet != 0.0)
            addPackReport(cartoon, stretchMachine, stretchHand, polyTape, bubble, duct, pallet);


        //НОРМЫ ВРЕМЕНИ

        if(opAssm.getMechTime() != 0.0 ||
                opAssm.getPaintTime() != 0.0 ||
                opAssm.getAssmTime() != 0.0 ||
                opAssm.getPackTime() != 0.0)
            addNormTimesReport(opAssm);

        taReport.setText(report.toString());

    }

    private double collectPacksOverOperations(List<OpData> ops, EPacks pack, double res) {
        for (OpData op : ops) {
            if (op instanceof PackingData) {
                switch(pack){
                    case CARTOON : res += ((PackingData)op).getCartoon(); break;
                    case STRETCH_MACHINE : res += ((PackingData)op).getStretchMachineWrap(); break;
                    case STRETCH_HAND : res += ((PackingData)op).getStretchHandWrap(); break;
                    case POLY : res += ((PackingData)op).getPolyWrap(); break;
                    case BUBBLE : res += ((PackingData)op).getBubbleWrap(); break;
                    case DUCT : res += ((PackingData)op).getDuctTape(); break;
                    case PALLET : res += ((PackingData)op).getPallet(); break;
                    default: break;
                }
            } else if (op instanceof IOpWithOperations) {
                List<OpData> operations = ((IOpWithOperations) op).getOperations();
                res += collectPacksOverOperations(operations, pack, res);
            }

        }
        return res;
    }

    private void addPackReport(double cartoon, double stretchMachine, double stretchHand, double polyTape, double bubble, double duct, double pallet){
        report.append("\n\n").append("УПАКОВКА :\n");
        if (cartoon != 0.0)
            report.append(CARTOON.getName())
                    .append(DECIMAL_FORMAT.format(cartoon)).append(" ")
                    .append(CARTOON.getMeasuring()).append("\n");
        if (stretchMachine != 0.0)
            report.append(STRETCH_MACHINE.getName())
                    .append(DECIMAL_FORMAT.format(stretchMachine)).append(" ")
                    .append(STRETCH_MACHINE.getMeasuring()).append("\n");
        if (stretchHand != 0.0)
            report.append(STRETCH_HAND.getName())
                    .append(DECIMAL_FORMAT.format(stretchHand)).append(" ")
                    .append(STRETCH_HAND.getMeasuring()).append("\n");
        if (polyTape != 0.0)
            report.append(POLY.getName())
                    .append(DECIMAL_FORMAT.format(polyTape)).append(" ")
                    .append(POLY.getMeasuring()).append("\n");
        if (bubble != 0.0)
            report.append(BUBBLE.getName())
                    .append(DECIMAL_FORMAT.format(bubble)).append(" ")
                    .append(BUBBLE.getMeasuring()).append("\n");
        if (duct != 0.0)
            report.append(DUCT.getName())
                    .append(DECIMAL_FORMAT.format(duct)).append(" ")
                    .append(DUCT.getMeasuring()).append("\n");
        if (pallet != 0.0)
            report.append(PALLET.getName())
                    .append(DECIMAL_FORMAT.format(pallet)).append(" ")
                    .append(PALLET.getMeasuring()).append("\n");
    }

    private void addNormTimesReport(OpAssm opAssm) {
        report.append("\n\n").append("НОРМЫ ВРЕМЕНИ :\n");
        ETimeMeasurement tm = ETimeMeasurement.MIN;
        double k = 1.0;
        if (opAssm.getMechTime() != 0.0)
            report.append("Изготовление : ")
                    .append(String.format(DOUBLE_FORMAT, opAssm.getMechTime() * k)).append(" ")
                    .append(tm.getName()).append("\n");
        if (opAssm.getPaintTime() != 0.0)
            report.append("Покраска \t: ")
                    .append(String.format(DOUBLE_FORMAT, opAssm.getPaintTime() * k)).append(" ")
                    .append(tm.getName()).append("\n");
        if (opAssm.getAssmTime() != 0.0)
            report.append("Сборка \t\t: ")
                    .append(String.format(DOUBLE_FORMAT, opAssm.getAssmTime() * k)).append(" ")
                    .append(tm.getName()).append("\n");
        if (opAssm.getPackTime() != 0.0)
            report.append("Упаковка \t: "
            ).append(String.format(DOUBLE_FORMAT, opAssm.getPackTime() * k)).append(" ")
                    .append(tm.getName()).append("\n");
    }

    private void addColorReport(List<Double> ral1, List<Double> ral2, List<Double> ral3) {
        report.append("\n\n").append("ПОКРЫТИЕ :\n");
        if(ral1.get(0) != 0.0) addRal1Report(ral1, EColor.COLOR_I);
        if(ral2.get(0) != 0.0) addRal1Report(ral1, EColor.COLOR_II);
        if(ral3.get(0) != 0.0) addRal1Report(ral1, EColor.COLOR_III);
    }

    private void addRal1Report(List<Double> ral1, EColor color) {
        report.append("Краска '")
                .append(color.getRal())
                .append("', площадь = ")
                .append(ral1.get(0))
                .append(" м2, ")
                .append("расход = ")
                .append(ral1.get(1))
                .append(" кг.\n");
    }

    private void addMaterialsReport() {
        report.append("\n\n").append("МАТЕРИАЛЫ :\n");
        for(Material m : materials.keySet()){
            report.append(m.getName()).append("\t: ").append(String.format(DOUBLE_FORMAT, materials.get(m))).append(" кг.\n");
        }
    }

    private void addLevelingSealerReport() {
        report.append("\n\n").append("НАЛИВНОЙ УПЛОТНИТЕЛЬ :\n");
        report.append("Компонент полиэфирный\tА = ").append(componentA).append(" кг.\n");
        report.append("Компонент изоцинат\t\tБ = ").append(componentB).append(" кг.\n");
    }

    /**
     * Метод собирает материалы
     * @param ops
     */
    private void collectMaterialsByOpData(List<OpData> ops) {
        for (OpData op : ops) {
            if (op instanceof OpDetail) {
                Material m = ((OpDetail) op).getMaterial();
                //Детали, если не открывать редактор детали, материала не содержат
                if (m == null) continue;
                if (materials.containsKey(m)) {
                    //Прибавляем новый вес к полученному ранее мвтериалу
                    double sumWeight = materials.get(m) + ((OpDetail) op).getWeight() * op.getQuantity();
                    materials.put(m, sumWeight);
                } else {
                    //Добавляем новый материал и массу
                    materials.put(m, ((OpDetail) op).getWeight() * op.getQuantity());
                }
            } else if (op instanceof OpAssm) {
                List<OpData> operations = ((OpAssm) op).getOperations();
                collectMaterialsByOpData(operations);
            }

        }
    }

    /**
     *
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

}
