package ru.wert.normic.controllers.extra.reports;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.controllers.AbstractOpPlate.*;

public class ReportNormsByJobTypes {

    private StringBuilder textReport;
    private OpAssm opAssm;

    double cutting, bending, welding, locksmith, mechanic; //виды работ МК


    public ReportNormsByJobTypes(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {

        if (opAssm.getMechTime() == 0.0) return;

        countNormsByJobType(opAssm, opAssm.getQuantity());
        addNormTimesReport(opAssm);

    }

    /**
     * Метод рекурсивно перебирает механические операции и, в зависимости от вида работ суммирует
     * норму времени. Норма времени с типом JOB_NONE не суммируется.
     * @param OpWithOperations IOpWithOperations
     */
    private void countNormsByJobType(IOpWithOperations OpWithOperations, int quantity){
        List<OpData> ops = OpWithOperations.getOperations();
        for (OpData op : ops){
            if(op instanceof IOpWithOperations)
                countNormsByJobType((IOpWithOperations) op, op.getQuantity() * quantity);
            else
                switch(op.getJobType()){
                    case JOB_CUTTING: cutting += op.getMechTime() * quantity; break;
                    case JOB_BENDING: bending += op.getMechTime() * quantity; break;
                    case JOB_WELDING: welding += op.getMechTime() * quantity; break;
                    case JOB_LOCKSMITH: locksmith += op.getMechTime() * quantity; break;
                    case JOB_MECHANIC: mechanic += op.getMechTime() * quantity; break;
                    default: break;
                }

        }
    }

    private void addNormTimesReport(OpAssm opAssm) {
        double k = 1.0; //Коэффициент перевода в ед.измерения
        switch (CURRENT_MEASURE.name()) {
            case "SEC":
                k = MIN_TO_SEC;
                break;
            case "HOUR":
                k = MIN_TO_HOUR;
                break;
        }
        textReport.append("\n\n").append(String.format("НОРМЫ ВРЕМЕНИ МК ПО ВИДУ РАБОТ (%s):\n", CURRENT_MEASURE.getMeasure()));

        if (cutting != 0.0)
            textReport.append("Резка \t\t\t:  ")
                    .append(DECIMAL_FORMAT.format(cutting * k)).append("\n");
        if (bending != 0.0)
            textReport.append("Гибка \t\t\t:  ")
                    .append(DECIMAL_FORMAT.format(bending * k)).append("\n");
        if (welding != 0.0)
            textReport.append("Сварка \t\t\t:  ")
                    .append(DECIMAL_FORMAT.format(welding * k)).append("\n");
        if (locksmith != 0.0)
            textReport.append("Слесарка \t\t:  "
            ).append(DECIMAL_FORMAT.format(locksmith * k)).append("\n");
        if (mechanic != 0.0)
            textReport.append("Мехобработка \t:  "
            ).append(DECIMAL_FORMAT.format(mechanic * k)).append(" ");
    }

}
