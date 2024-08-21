package ru.wert.normic.report.reports;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.LocksmithOperation;
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
     *
     * @param OpWithOperations IOpWithOperations
     */
    private void countNormsByJobType(IOpWithOperations OpWithOperations, int quantity) {
        List<OpData> ops = OpWithOperations.getOperations();
        for (OpData op : ops) {
            if (op instanceof IOpWithOperations)
                countNormsByJobType((IOpWithOperations) op, op.getQuantity() * quantity);
            else {
                switch (op.getJobType()) {
                    case JOB_CUTTING:
                        cutting += op.getMechTime() * quantity;
                        break;
                    case JOB_BENDING:
                        bending += op.getMechTime() * quantity;
                        break;
                    case JOB_WELDING:
                        if (op instanceof LocksmithOperation) {
                            double strippingTime = ((LocksmithOperation) op).getLocksmithTime();
                            welding += (op.getMechTime() - strippingTime) * quantity;
                            locksmith += strippingTime * quantity;
                        } else
                            welding += op.getMechTime() * quantity;
                        break;
                    case JOB_LOCKSMITH:
                        locksmith += op.getMechTime() * quantity;
                        break;
                    case JOB_MECHANIC:
                        mechanic += op.getMechTime() * quantity;
                        break;
                    default:
                        break;
                }
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
        textReport.append("\n\n").append(String.format("НОРМЫ ВРЕМЕНИ МК ПО ВИДУ РАБОТ  (%s):\n", CURRENT_MEASURE.getMeasure()));

        int cuttingPercent = (int)Math.round(cutting / opAssm.getMechTime() * 100);
        int bendingPercent = (int) Math.round(bending / opAssm.getMechTime() * 100);
        int weldingPercent = (int) Math.round(welding / opAssm.getMechTime() * 100);
        int locksmithPercent = (int) Math.round(locksmith / opAssm.getMechTime() * 100);
        int mechanicPercent = (int) Math.round(mechanic / opAssm.getMechTime() * 100);

        int sum = cuttingPercent + bendingPercent + weldingPercent + locksmithPercent + mechanicPercent;

        if(sum != 100) cuttingPercent = cuttingPercent + 100 - sum;



        if (cutting != 0.0)
            textReport.append("Резка \t\t\t:  ")
                    .append(String.format(DOUBLE_FORMAT, cutting * k))
                    .append("\t\t").append(PERCENTAGE_FORMAT.format(cuttingPercent)).append(" %")
                    .append("\n");
        if (bending != 0.0)
            textReport.append("Гибка \t\t\t:  ")
                    .append(String.format(DOUBLE_FORMAT, bending * k))
                    .append("\t\t").append(PERCENTAGE_FORMAT.format(bendingPercent)).append(" %")
                    .append("\n");
        if (welding != 0.0)
            textReport.append("Сварка \t\t\t:  ")
                    .append(String.format(DOUBLE_FORMAT, welding * k))
                    .append("\t\t").append(PERCENTAGE_FORMAT.format(weldingPercent)).append(" %")
                    .append("\n");
        if (locksmith != 0.0)
            textReport.append("Слесарка \t\t:  "
            ).append(String.format(DOUBLE_FORMAT, locksmith * k))
                    .append("\t\t").append(PERCENTAGE_FORMAT.format(locksmithPercent)).append(" %")
                    .append("\n");
        if (mechanic != 0.0)
            textReport.append("Мехобработка \t:  "
            ).append(String.format(DOUBLE_FORMAT, mechanic * k))
                    .append("\t\t").append(PERCENTAGE_FORMAT.format(mechanicPercent)).append(" %")
                    .append("\n");

        textReport.append("-------------------------------------------------------\n");
        textReport.append("ИТОГО МК\t\t:  ")
                .append(String.format(DOUBLE_FORMAT, opAssm.getMechTime() * k * opAssm.getQuantity()))
                .append("\n");
    }

}
