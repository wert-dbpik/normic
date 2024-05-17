package ru.wert.normic.report.reports;

import ru.wert.normic.entities.ops.single.OpAssm;

public class ReportMaterials {

    private StringBuilder textReport;
    private OpAssm opAssm;

    public ReportMaterials(StringBuilder textReport, OpAssm opAssm) {
        this.textReport = textReport;
        this.opAssm = opAssm;
    }

    public void create() {

    }
}
