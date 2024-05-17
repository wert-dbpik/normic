package ru.wert.normic.report.reports;

public class ReportPainting {
//
//    private StringBuilder textReport;
//    private OpAssm opAssm;
//
//    double cutting, bending, welding, locksmith, mechanic; //виды работ МК
//
//
//    public ReportPainting(StringBuilder textReport, OpAssm opAssm) {
//        this.textReport = textReport;
//        this.opAssm = opAssm;
//    }
//
//    public void create() {
//
//        List<Double> ral1 = collectListOfOperationsInOpData(opAssm, opAssm.getQuantity(), EColor.COLOR_I);
//        List<Double> ral2 = collectListOfOperationsInOpData(opAssm, opAssm.getQuantity(), EColor.COLOR_II);
//        List<Double> ral3 = collectListOfOperationsInOpData(opAssm, opAssm.getQuantity(), EColor.COLOR_III);
//
//        if(ral1.get(0) + ral2.get(0) + ral3.get(0) != 0.0)
//            addColorReport(ral1, ral2, ral3);
//
//    }
//
//    /**
//     * Метод рекурсивно перебирает механические операции и, в зависимости от вида работ суммирует
//     * норму времени. Норма времени с типом JOB_NONE не суммируется.
//     * @param OpWithOperations IOpWithOperations
//     */
//    private void countNormsByJobType(IOpWithOperations OpWithOperations, int quantity){
//        List<OpData> ops = OpWithOperations.getOperations();
//        for (OpData op : ops){
//            if(op instanceof IOpWithOperations)
//                countNormsByJobType((IOpWithOperations) op, op.getQuantity() * quantity);
//            else
//                switch(op.getJobType()){
//                    case JOB_CUTTING: cutting += op.getMechTime() * quantity; break;
//                    case JOB_BENDING: bending += op.getMechTime() * quantity; break;
//                    case JOB_WELDING: welding += op.getMechTime() * quantity; break;
//                    case JOB_LOCKSMITH: locksmith += op.getMechTime() * quantity; break;
//                    case JOB_MECHANIC: mechanic += op.getMechTime() * quantity; break;
//                    default: break;
//                }
//
//        }
//    }
//
//    /**
//     * Добавить отчет по РАСХОДУ КРАСКИ (начало)
//     */
//    private void addColorReport(List<Double> ral1, List<Double> ral2, List<Double> ral3) {
//        textReport.append("\n\n").append("ПОКРЫТИЕ :\n");
//        if(ral1.get(0) != 0.0) addRal1Report(ral1, EColor.COLOR_I);
//        if(ral2.get(0) != 0.0) addRal1Report(ral2, EColor.COLOR_II);
//        if(ral3.get(0) != 0.0) addRal1Report(ral3, EColor.COLOR_III);
//    }
//    /**
//     * Добавить отчет по РАСХОДУ КРАСКИ (по конкретной краске)
//     */
//    private void addRal1Report(List<Double> ral1, EColor color) {
//        textReport.append("Краска '")
//                .append(color.getRal())
//                .append("', площадь = ")
//                .append(DECIMAL_FORMAT.format(ral1.get(0)))
//                .append(" м.кв., ")
//                .append("расход = ")
//                .append(DECIMAL_FORMAT.format(ral1.get(1)))
//                .append(" кг.\n");
//    }
//
//    /**
//     * Сосчитать СУММАРНУЮ ПЛОЩАДЬ и РАСХОД краски
//     */
//    private List<Double> collectListOfOperationsInOpData(IOpWithOperations opWithOperations, int quantity, EColor color){
//        double area = 0.0;
//        double weight = 0.0;
//        for(OpData op : opWithOperations.getOperations()){
//            if(op instanceof IOpWithOperations) {
//                List<Double> ress = collectListOfOperationsInOpData((IOpWithOperations) op, op.getQuantity() * quantity, color);
//                area += ress.get(0);
//                weight += ress.get(1);
//            }else{
//                if(op instanceof OpPaint && ((OpPaint)op).getColor().equals(color)) {
//                    area += ((OpPaint) op).getArea() * op.getOpData().getQuantity();
//                    weight += ((OpPaint) op).getDyeWeight() * op.getOpData().getQuantity();
//                }else if(op instanceof OpPaintAssm && ((OpPaintAssm)op).getColor().equals(color)) {
//                    area += ((OpPaintAssm) op).getCountedArea() * op.getOpData().getQuantity();
//                    weight += ((OpPaintAssm) op).getDyeWeight() * op.getOpData().getQuantity();
//                }
//            }
//
//        }
//        return Arrays.asList(area, weight);
//    }

}
