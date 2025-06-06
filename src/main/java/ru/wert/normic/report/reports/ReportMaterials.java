package ru.wert.normic.report.reports;

import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.EPieceMeasurement;

import java.util.*;

import static ru.wert.normic.NormicServices.DENSITIES;
import static ru.wert.normic.NormicServices.QUICK_MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;

public class ReportMaterials {

    private Map<Material, Double> materials;
    private double steelScrap; //переменная для расчета лома

    private StringBuilder textReport;
    private OpAssm opAssm;

    public ReportMaterials(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {
        materials = new HashMap<>();
        List<OpData> ops = opAssm.getOperations();
        collectMaterialsByOpData(ops, opAssm.getQuantity());
        if(!materials.isEmpty())
            addMaterialsReport();

        //Лом стали
        if(steelScrap != 0.0)
            textReport.append("----------------------------------")
                    .append("\nЛОМ СТАЛИ (10%): ").append(String.format(DOUBLE_FORMAT, steelScrap)).append(" кг.");
    }

    /**
     * Добавить отчет по ИСПОЛЬЗУЕМЫМ МАТЕРИАЛАМ
     */
    private void addMaterialsReport() {
        textReport.append("\n\n").append("МАТЕРИАЛЫ :\n")
                .append("----------------\n");
        List<Material> keys = new ArrayList<>(materials.keySet());
        keys.sort(Comparator.comparing(Material::getName));
        for(Material m : keys){
            textReport.append(m.getName()).append("\t: ").append(String.format(DOUBLE_FORMAT, materials.get(m)));
            if(EMatType.getTypeByName(m.getMatType().getName()).equals(EMatType.PIECE))
                textReport.append(" ")
                        .append(EPieceMeasurement.values()[((int)m.getParamX())].getMeasureName())
                        .append("\n");
            else
                textReport.append(" кг.\n");
        }
    }

    /**
     * Сосчитать все МАТЕРИАЛЫ
     */
    private void collectMaterialsByOpData(List<OpData> ops, int quantity) {

        for (OpData op : ops) {
            if (op instanceof OpDetail) {

                Material mat = ((OpDetail) op).getMaterial();
                if(mat == null) continue;
                Material m = QUICK_MATERIALS.findById(mat.getId());
                if(m == null) m = ((OpDetail) op).getMaterial();
                //Детали, если не открывать редактор детали, материала не содержат
//                if (m == null) continue;
                if (materials.containsKey(m)) {
                    //Прибавляем новый вес к полученному ранее материалу
                    double sumWeight = materials.get(m) + ((OpDetail) op).getWeight() * op.getQuantity() * quantity;
                    materials.put(m, sumWeight);
                } else {
                    //Добавляем новый материал и массу
                    materials.put(m, ((OpDetail) op).getWeight() * op.getQuantity() * quantity);
                }
                if(m.isAsScrap())
                    steelScrap += ((OpDetail) op).getWeight() * op.getTotal() * 0.1;

            } else if (op instanceof OpAssm) {
                List<OpData> operations = ((OpAssm) op).getOperations();
                collectMaterialsByOpData(operations, op.getQuantity() * quantity);
            }

        }
    }
}
