package ru.wert.normic.report.reports;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.entities.ops.single.OpAssm;

import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class ReportLevelingSealer {

    //Расход на наливное уплотнение
    private double componentA; //Компонент полиэфирный А
    private double componentB; //Компонент изоцинат Б

    private StringBuilder textReport;
    private OpAssm opAssm;

    public ReportLevelingSealer(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {
        //Сбрасываем данные преред создание отчета
        componentA = 0.0;
        componentB = 0.0;

        collectComponentsABByOpData(opAssm.getOperations());
        if(componentA != 0.0)
            addLevelingSealerReport();
    }

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
}
