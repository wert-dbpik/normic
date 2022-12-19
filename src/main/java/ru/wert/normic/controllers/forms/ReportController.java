package ru.wert.normic.controllers.forms;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.entities.db_connection.Material;
import ru.wert.normic.enums.ETimeMeasurement;

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
        String sp = "9.999";
        String ral = "RAL7035";
        String ralw = "0.782";
        report.append("\n\n").append("ПОКРЫТИЕ :\n");
        report.append("Суммарная площадь : ").append(sp).append(" м2\n");
        report.append("Расход краски ").append("'").append(ral).append("'").append(" : ").append(ralw).append(" кг.\n");

        //НОРМЫ ВРЕМЕНИ
        ETimeMeasurement tm = ETimeMeasurement.MIN;
        double k = 1.0;
        report.append("\n\n").append("НОРМЫ ВРЕМЕНИ :\n");

        report.append("Изготовление : ").append(String.format(DOUBLE_FORMAT, opAssm.getMechTime() * k)).append(" ").append(tm.getTimeName()).append("\n");
        report.append("Покраска \t: ").append(String.format(DOUBLE_FORMAT, opAssm.getPaintTime() * k)).append(" ").append(tm.getTimeName()).append("\n");
        report.append("Сборка \t\t: ").append(String.format(DOUBLE_FORMAT, opAssm.getAssmTime() * k)).append(" ").append(tm.getTimeName()).append("\n");
        report.append("Упаковка \t: ").append(String.format(DOUBLE_FORMAT, opAssm.getPackTime() * k)).append(" ").append(tm.getTimeName()).append("\n");

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

}
