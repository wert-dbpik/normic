package ru.wert.normic.report.reports;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class ReportPainting {

    private StringBuilder textReport;
    private OpAssm opAssm;

    double area = 0.0;
    double weight = 0.0;


    public ReportPainting(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {

        area = 0.0; weight = 0.0;
        List<Double> ral1 = collectListOfOperationsInOpData(opAssm, opAssm.getQuantity(), EColor.COLOR_I);
        area = 0.0; weight = 0.0;
        List<Double> ral2 = collectListOfOperationsInOpData(opAssm, opAssm.getQuantity(), EColor.COLOR_II);
        area = 0.0; weight = 0.0;
        List<Double> ral3 = collectListOfOperationsInOpData(opAssm, opAssm.getQuantity(), EColor.COLOR_III);

        if(ral1.get(0) + ral2.get(0) + ral3.get(0) != 0.0)
            addColorReport(ral1, ral2, ral3);

    }

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
    private List<Double> collectListOfOperationsInOpData(IOpWithOperations op, int quantity, EColor color){
        for(OpData o : op.getOperations()){
            if(o instanceof IOpWithOperations) {
                collectListOfOperationsInOpData((IOpWithOperations) o, o.getQuantity() * quantity, color);
            }else{
                if(o instanceof OpPaint && ((OpPaint)o).getColor().equals(color)) {
                    area += ((OpPaint) o).getArea() * quantity;
                    weight += ((OpPaint) o).getDyeWeight() * quantity;
                }else if(o instanceof OpPaintAssm && ((OpPaintAssm)o).getColor().equals(color)) {
                    area += ((OpPaintAssm) o).getCountedArea() * quantity;
                    weight += ((OpPaintAssm) o).getDyeWeight() * quantity;
                }
            }

        }
        return Arrays.asList(area, weight);
    }


}
