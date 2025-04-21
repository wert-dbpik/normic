package ru.wert.normic.report;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.report.reports.*;

/**
 * ОТЧЕТ
 */
public class ReportController {

    @FXML
    private TextArea taReport;

    public void init(OpAssm opAssm){

        StringBuilder textReport = new StringBuilder();

        //Наименование изделия
        String name = opAssm.getName();
        textReport.append("ИЗДЕЛИЕ : ").append(name == null? "< без наименования >" : name);

        //###########################################################################################################

        //НОРМЫ ВРЕМЕНИ ПО ВИДАМ РАБОТ
        new ReportNormsByJobTypes(textReport, opAssm).create();

        //НОРМЫ ВРЕМЕНИ ПО ЦЕХАМ (МК, ППК, СБОРКА и УПАКОВКА)
        new ReportNormsByNormTypes(textReport, opAssm).create();

        //МАТЕРИАЛЫ И ЛОМ
        new ReportMaterials(textReport, opAssm).create();

        //ДОПОЛНИТЕЛЬНЫЕ МАТЕРИАЛЫ
        new ReportSOMaterials(textReport, opAssm).create();

        //НАЛИВНОЙ УПЛОТНИТЕЛЬ
        new ReportLevelingSealer(textReport, opAssm).create();

        //ТЕРМОИОЛЯЦИЯ
        new ReportInsulation(textReport, opAssm).create();

        //ПОКРЫТИЕ
        new ReportPainting(textReport, opAssm).create();

        //УПАКОВКА
        new ReportPacking(textReport, opAssm).create();

        //УПАКОВКА
        new ReportElectrical(textReport, opAssm).create();

        taReport.setText(textReport.toString());

    }

}
