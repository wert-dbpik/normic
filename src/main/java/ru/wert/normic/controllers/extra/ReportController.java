package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;

public class ReportController {

    @FXML
    private TextArea taReport;

    private OpAssm opAssm;
    private Map<Material, Double> materials;

    //Расход на наливное уплотнение
    private double componentA; //Компонент полиэфирный А
    private double componentB; //Компонент изоцинат Б

    public void init(OpAssm opAssm){
        this.opAssm = opAssm;

        //Сбрасываем данные преред создание отчета
        componentA = 0.0;
        componentB = 0.0;


        StringBuilder report = new StringBuilder();

        //Наименование изделия
        String name = opAssm.getName();
        report.append("ИЗДЕЛИЕ : ").append(name == null? "< без наименования >" : name);

        //Материалы
        materials = new HashMap<>();
        List<OpData> ops = opAssm.getOperations();
        collectMaterialsByOpData(ops);
        report.append("\n\n").append("МАТЕРИАЛЫ :\n");
        for(Material m : materials.keySet()){
            report.append(m.getName()).append("\t: ").append(String.format(DOUBLE_FORMAT, materials.get(m))).append(" кг.\n");
        }

        //Наливной уплотнитель
        report.append("\n\n").append("НАЛИВНОЙ УПЛОТНИТЕЛЬ :\n");
        collectComponentsABByOpData(ops);
        report.append("Компонент полиэфирный А = ").append(componentA).append(" кг.\n");;
        report.append("Компонент изцинат     Б = ").append(componentB).append(" кг.\n");;

        //Покрытие
        report.append("\n\n").append("ПОКРЫТИЕ :\n");
        List<Double> ral1 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_I);
        report.append("Краска '").append(EColor.COLOR_I.getRal()).append("', площадь = ").append(ral1.get(0)).append(" м2, ").append("расход = ").append(ral1.get(1)).append(" кг.\n");

        List<Double> ral2 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_II);
        report.append("Краска '").append(EColor.COLOR_II.getRal()).append("', площадь = ").append(ral2.get(0)).append(" м2, ").append("расход = ").append(ral2.get(1)).append(" кг.\n");

        List<Double> ral3 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_III);
        report.append("Краска '").append(EColor.COLOR_III.getRal()).append("', площадь = ").append(ral3.get(0)).append(" м2, ").append("расход = ").append(ral3.get(1)).append(" кг.\n");

        //НОРМЫ ВРЕМЕНИ
        ETimeMeasurement tm = ETimeMeasurement.MIN;
        double k = 1.0;
        report.append("\n\n").append("НОРМЫ ВРЕМЕНИ :\n");

        report.append("Изготовление : ").append(String.format(DOUBLE_FORMAT, opAssm.getMechTime() * k)).append(" ").append(tm.getName()).append("\n");
        report.append("Покраска \t: ").append(String.format(DOUBLE_FORMAT, opAssm.getPaintTime() * k)).append(" ").append(tm.getName()).append("\n");
        report.append("Сборка \t\t: ").append(String.format(DOUBLE_FORMAT, opAssm.getAssmTime() * k)).append(" ").append(tm.getName()).append("\n");
        report.append("Упаковка \t: ").append(String.format(DOUBLE_FORMAT, opAssm.getPackTime() * k)).append(" ").append(tm.getName()).append("\n");

        taReport.setText(report.toString());

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
