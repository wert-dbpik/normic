package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.Material;
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

    public void init(OpAssm opAssm){
        this.opAssm = opAssm;

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

        //Покрытие

        report.append("\n\n").append("ПОКРЫТИЕ :\n");
        List<Double> ral1 = collectListOfOperationsInOpData(opAssm, EColor.COLOR_I);
        report.append("Краска '").append(EColor.COLOR_I.getName()).append("'").append(" : площадь = ").append(ral1.get(0)).append(" м2\n");
        report.append("расход = ").append(ral1.get(1)).append(" кг.\n");

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

    private void collectMaterialsByOpData(List<OpData> ops) {
        for(OpData op : ops){
            if(op instanceof OpDetail){
                Material m = ((OpDetail) op).getMaterial();
                if(materials.containsKey(m)){
                    //Прибавляем новый вес к полученному ранее мвтериалу
                    double sumWeight = materials.get(m) + ((OpDetail)op).getWeight() * op.getQuantity();
                    materials.put(m, sumWeight);
                } else {
                    //Добавляем новый материал и массу
                    materials.put(m, ((OpDetail)op).getWeight() * op.getQuantity());
                }
            }
            else if (op instanceof OpAssm){
                List<OpData> operations = ((OpAssm)op).getOperations();
                collectMaterialsByOpData(operations);
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
                    area += ((OpPaint) o).getArea();
                    weight += ((OpPaint) o).getDyeWeight();
                }else if(o instanceof OpPaintAssm && ((OpPaintAssm)o).getColor().equals(color)) {
                    area += ((OpPaintAssm) o).getArea();
                    weight += ((OpPaintAssm) o).getDyeWeight();
                }
            }

        }
        return Arrays.asList(area, weight);
    }

}
