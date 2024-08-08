package ru.wert.normic.report.reports;

import ru.wert.normic.dataBaseEntities.ops.single.OpAssm;

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class ReportNormsByNormTypes {

    private StringBuilder textReport;
    private OpAssm opAssm;

    public ReportNormsByNormTypes(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {

        if (opAssm.getMechTime() != 0.0 ||
                opAssm.getPaintTime() != 0.0 ||
                opAssm.getAssmTime() != 0.0 ||
                opAssm.getPackTime() != 0.0)
            addNormTimesReport(opAssm);

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

        String title = opAssm.getMechTime() == 0.0 ? "НОРМЫ ВРЕМЕНИ" : "ОСТАЛЬНЫЕ НОРМЫ ВРЕМЕНИ";
        if(opAssm.getPaintTime() != 0.0 || opAssm.getAssmTime() != 0.0 || opAssm.getPackTime() != 0.0)
            textReport.append("\n\n").append(String.format("%s  (%s):\n", title, CURRENT_MEASURE.getMeasure()));

//        if (opAssm.getMechTime() != 0.0)
//            textReport.append("Изготовление \t:  ")
//                    .append(DECIMAL_FORMAT.format(opAssm.getMechTime() * k)).append("\n");
        if (opAssm.getPaintTime() != 0.0)
            textReport.append("Покраска \t\t:  ")
                    .append(DECIMAL_FORMAT.format(opAssm.getPaintTime() * k)).append("\n");
        if (opAssm.getAssmTime() != 0.0)
            textReport.append("Сборка \t\t\t:  ")
                    .append(DECIMAL_FORMAT.format(opAssm.getAssmTime() * k)).append("\n");
        if (opAssm.getPackTime() != 0.0)
            textReport.append("Упаковка \t\t:  "
            ).append(DECIMAL_FORMAT.format(opAssm.getPackTime() * k)).append("\n");
    }

}
