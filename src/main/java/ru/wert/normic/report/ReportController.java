package ru.wert.normic.report;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.entities.ops.opPack.PackingData;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.report.reports.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.wert.normic.NormicServices.DENSITIES;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.EPacks.*;

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

        //МАТЕРИАЛЫ И ЛОМ
        new ReportMaterials(textReport, opAssm).create();

        //НАЛИВНОЙ УПЛОТНИТЕЛЬ
        new ReportLevelingSealer(textReport, opAssm).create();

        //ПОКРЫТИЕ
        new ReportPainting(textReport, opAssm).create();

        //УПАКОВКА
        new ReportPacking(textReport, opAssm).create();

        //НОРМЫ ВРЕМЕНИ ПО ВИДАМ РАБОТ
        new ReportNormsByJobTypes(textReport, opAssm).create();

        //НОРМЫ ВРЕМЕНИ ПО ЦЕХАМ (МК, ППК, СБОРКА и УПАКОВКА)
        new ReportNormsByNormTypes(textReport, opAssm).create();

        taReport.setText(textReport.toString());

    }

}
