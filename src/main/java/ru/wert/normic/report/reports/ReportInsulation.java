package ru.wert.normic.report.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpThermoInsulation;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.enums.EMaterialMeasurement;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.HashMap;
import java.util.Map;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class ReportInsulation {

    //Расход на наливное уплотнение
    private Map<ThickMeasure, Double> insulations; //Термоизоляция
    private double scotchOutlay = 0.0;

    private StringBuilder textReport;
    private OpAssm opAssm;

    public ReportInsulation(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {
        //Сбрасываем данные преред создание отчета
        insulations = new HashMap<>();

        collectInsulationsByOpData(opAssm, opAssm.getQuantity());
        if(!insulations.isEmpty())
            addThermoInsulationReport();
    }

    /**
     * Добавить отчет по ТЕРМОИЗОЛЯЦИИ
     */
    private void addThermoInsulationReport() {
        textReport.append("\n\n").append("ТЕРМОИЗОЛЯЦИЯ :\n");
        for(ThickMeasure key : insulations.keySet()){
            textReport.append(String.format(
                    "\tТермоизоляция\t\t\t\tS%s = %s %s \n",
                    DECIMAL_FORMAT.format(key.thick),
                    DECIMAL_FORMAT.format(insulations.get(key)),
                    key.measure.getMeasure()));
            if(scotchOutlay != 0.0)
                textReport.append(String.format(
                        "\tМеталлизированный скотч 48х50\t%s шт.\n",
                        DECIMAL_FORMAT.format(scotchOutlay)));
        }
    }

    @Getter
    @AllArgsConstructor
    private static class ThickMeasure{
        private final int thick;
        private final EMaterialMeasurement measure;
    }

    /**
     * Сосчитать матириалы по НАЛИВНОМУ УПЛОТНЕНИЮ
     */
    private void collectInsulationsByOpData(IOpWithOperations op, int quantity){
        for(OpData o : op.getOperations()){
            if(o instanceof IOpWithOperations) {
                collectInsulationsByOpData((IOpWithOperations) o, o.getQuantity() * quantity);
            }else{
                if(o instanceof OpThermoInsulation){
                    ThickMeasure key = new ThickMeasure(
                            ((OpThermoInsulation) o).getThickness(),
                            ((OpThermoInsulation) o).getMeasurement());
                    if(insulations.containsKey(key)){
                        insulations.put(
                                key,
                                insulations.get(key) + ((OpThermoInsulation) o).getOutlay() * quantity);
                    } else {
                            insulations.put(
                                    key,
                                    ((OpThermoInsulation) o).getOutlay() * quantity);
                    }
                    //Сосчитаем расход металлизированного скотча
                    scotchOutlay += ((OpThermoInsulation) o).getScotchOutlay();
                }
            }
        }
    }
}
