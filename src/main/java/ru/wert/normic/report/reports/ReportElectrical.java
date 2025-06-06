package ru.wert.normic.report.reports;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class ReportElectrical {

    private final StringBuilder textReport;
    private final OpAssm opAssm;

    double electricalTime = 0.0;

    public ReportElectrical(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {

        countElectricalTimes(opAssm, 1);
        if(electricalTime != 0.0) {
            textReport
                    .append("\n\n")
                    .append("ЭЛЕКТРОМОНТАЖ : T норм = ")
                    .append(DECIMAL_FORMAT.format(electricalTime * CURRENT_MEASURE.getRate()))
                    .append(" ").append(CURRENT_MEASURE.getMeasure());
        }
    }

    private void countElectricalTimes(IOpWithOperations op, int quantity) {
        for(OpData o : op.getOperations()){
            if(o instanceof IOpWithOperations) {
                countElectricalTimes((IOpWithOperations) o, o.getQuantity() * quantity);
            } else {
                if(o.getNormType().equals(ENormType.NORM_ELECTRICAL))
                    electricalTime += o.getElectricalTime() * quantity;
            }
        }
    }

}
