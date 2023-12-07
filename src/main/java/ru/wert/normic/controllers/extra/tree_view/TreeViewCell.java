package ru.wert.normic.controllers.extra.tree_view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ru.wert.normic.components.ImgDone;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.controllers.extra.StructureController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.HOUR;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

public class TreeViewCell extends TreeCell<OpData> {

//    private ImgDouble imgDone;
    private String initTitleStyle;
    private String initLblNormsTimeStyle;
    private final StructureController controller;

    public TreeViewCell(StructureController controller) {
        this.controller = controller;
    }

    @Override
    protected void updateItem(OpData opData, boolean empty) {
        super.updateItem(opData, empty);

        if (empty) {
            setText("");
            setGraphic(null);
        } else {
            List<OpData> operations = ((IOpWithOperations)opData).getOperations();
            int quantity = opData.getQuantity();
            EOpType type = opData.getOpType();


            HBox hbTitle = new HBox();
            hbTitle.setSpacing(5.0);
            initTitleStyle = hbTitle.getStyle();
            //Лого
            ImageView logo = new ImageView(type.getLogo());
            logo.setFitWidth(16);
            logo.setFitHeight(16);
            //Номер с наименованием
            TextField tfName = new TextField();
            Text txtName = new Text();
            txtName.setId("title");
            txtName.textProperty().bind(tfName.textProperty());
            tfName.setText(((IOpWithOperations) opData).getName());

            //Количество
            TextField tfN = new TextField();
            Text txtStart = new Text("(");
            Text txtN = new Text();
            Text txtFinish = new Text(" шт.)");

            txtStart.setId("title");
            txtN.setId("title");
            txtFinish.setId("title");

            txtN.textProperty().bind(tfN.textProperty());
            tfN.setText(String.valueOf(quantity));

            //Вызов редактора
            ImageView ivDone = new ImageView();
            ImgDouble imgDone = new ImgDone(ivDone,18);
            imgDone.getStateProperty().setValue(((IOpWithOperations) opData).isDone());
            imgDone.setOnMouseClicked(e->{
                    if (opData instanceof OpDetail) {
                        controller.openFormEditor(getTreeItem(), EOpType.DETAIL, "ДЕТАЛЬ", "/fxml/formDetail.fxml", opData, tfName, tfN, imgDone);
                    } else if (opData instanceof OpAssm) {
                        controller.openFormEditor(getTreeItem(), EOpType.ASSM, "СБОРКА", "/fxml/formAssm.fxml", opData, tfName, tfN, imgDone);
                    } else if (opData instanceof OpPack) {
                        controller.openFormEditor(getTreeItem(), EOpType.PACK, "УПАКОВКА", "/fxml/formPack.fxml", opData, tfName, tfN, imgDone);
                    }
            });

            //СТрока с заголовком
            hbTitle.getChildren().add(logo);
            hbTitle.getChildren().add(txtName);
            if(quantity > 1)
                hbTitle.getChildren().addAll(txtStart, txtN, txtFinish);
            hbTitle.getChildren().add(ivDone);
            hbTitle.setAlignment(Pos.CENTER_LEFT);

            //Строка с рассчитанными нормами времени
            Label lblNorms = new Label(createStringWithNormsTime(opData));
            lblNorms.setId("normsTime");
            initLblNormsTimeStyle = lblNorms.getStyle();

            //Блок объединяет
            // 1) Наименование узла HBox hbTitle
            // 2) Нормы времени Label lblNorms
            // 3) Операции с описанием (hbOperation/vbOperation = textOpName + textOpDescription)
            VBox vbItemBlock = new VBox();
            vbItemBlock.setId("vbItemBlock");
            vbItemBlock.getChildren().add(hbTitle);

            if(controller != null && controller.isShowNormsTime())
                vbItemBlock.getChildren().add(lblNorms);

            if (controller != null && controller.isShowOperations())
                if (opData instanceof OpDetail) {
                    Text textDetail = new Text("\t" + opData.toString());
                    textDetail.setId("description");
                    vbItemBlock.getChildren().add(textDetail);

                    for (OpData op : operations) {
                        formOperationBlock(vbItemBlock, op);
                    }
                } else {
                    if (opData instanceof OpPack) {
                        Text text = new Text("\t" + opData.toString());
                        text.setId("description");
                        vbItemBlock.getChildren().add(text);
                    }

                    for (OpData op : operations) {
                        if (op instanceof IOpWithOperations) continue;
                        formOperationBlock(vbItemBlock, op);
                    }
                }


            setGraphic(vbItemBlock);

//            selectedProperty().addListener((observable, oldValue, newValue) -> {
//                if(newValue){
//                    lblNorms.setStyle("-fx-background-color: #f1e2af");
//                    hbTitle.setStyle("-fx-background-color: #f1e2af");
//                    vbItemBlock.setStyle("-fx-background-color: #f1e2af");
//
//                } else {
//                    lblNorms.setStyle(initLblNormsTimeStyle);
//                    hbTitle.setStyle(initTitleStyle);
//                    vbItemBlock.setStyle(initTitleStyle);
//
//
//                    setStyle(initTitleStyle);
//                }
//            });

            selectedProperty().addListener((observable, oldValue, newValue) -> {
                imgDone.setVisible(newValue);
            });
        }
    }

    private void formOperationBlock(VBox vbItemBlock, OpData op) {
        //НИМЕНОВАНИЕ ОПЕРАЦИИ
        String opName = op.getOpType().getOpName();
        Text textOpName = new Text("\t \u25CF " + opName + ": ");
        textOpName.setId("operationName");
        Label textOpDescription = new Label(op.toString());
        if (opName.length() < 20) {
            HBox hbOperation = new HBox();
            hbOperation.setStyle("-fx-background-color: transparent");
            textOpDescription.setId("operationDescription");
            hbOperation.getChildren().addAll(textOpName, textOpDescription);
            vbItemBlock.getChildren().add(hbOperation);
        } else {
            VBox vbOperation = new VBox(); //Вертикальная компоновка
            vbOperation.setStyle("-fx-background-color: transparent");
            textOpDescription.setId("operationDescriptionShifted");
            vbOperation.getChildren().addAll(textOpName, textOpDescription);
            vbItemBlock.getChildren().add(vbOperation);
        }
    }

    private String createStringWithNormsTime(OpData opData){

        double k = 1.0;
        if(CURRENT_MEASURE.equals(SEC)) k = MIN_TO_SEC;
        else if(CURRENT_MEASURE.equals(HOUR)) k = MIN_TO_HOUR;

        Norm mechTime = new Norm("МК", opData.getMechTime() * k);
        Norm paintTime = new Norm("ППК", opData.getPaintTime() * k);
        Norm assmTime = new Norm("СБ", opData.getAssmTime() * k);
        Norm packTime = new Norm("УП", opData.getPackTime() * k);

        Norm [] norms = new Norm[]{mechTime, paintTime, assmTime, packTime};

        StringBuilder str = new StringBuilder();
        for (Norm n : norms)
            if (n.value != 0.0) {
                str.append(n.measureName);
                str.append(" = ");
                str.append(DECIMAL_FORMAT.format(n.value));
                str.append("; ");
            }

        str.append(String.format("(%s)", CURRENT_MEASURE.getMeasure()));
        return str.toString();
    }

    private static class Norm{
        final String measureName;
        final double value;

        public Norm(String measureName, double value) {
            this.measureName = measureName;
            this.value = value;
        }
    }
}
