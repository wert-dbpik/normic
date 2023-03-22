package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.controllers.singlePlates.PlatePackController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpAssm;
import ru.wert.normic.entities.ops.opAssembling.OpDetail;
import ru.wert.normic.entities.ops.opPack.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.io.IOException;
import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class ProductTreeController {

    @FXML
    private TreeView<OpData> treeView;
    private String initStyle;

    public void create(OpAssm opRoot){

        TreeItem<OpData> rootItem = new TreeItem<>(opRoot);
        createLeaf(rootItem);

        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        treeView.setCellFactory(param -> new TreeCell<OpData>(){
            @Override
            protected void updateItem(OpData opData, boolean empty) {
                super.updateItem(opData, empty);

                if (empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    List<OpData> operations = ((IOpWithOperations)opData).getOperations();
                    String name = ((IOpWithOperations)opData).getName();
                    int quantity = opData.getQuantity();
                    EOpType type = opData.getOpType();


                    HBox hbTitle = new HBox();
                    hbTitle.setSpacing(5.0);
                    initStyle = hbTitle.getStyle();
                    //Лого
                    ImageView logo = new ImageView(type.getLogo());
                    logo.setFitWidth(16);
                    logo.setFitHeight(16);
                    //Номер с наименованием
                    TextField tfName = new TextField();
                    Text txtName = new Text();
                    txtName.textProperty().bind(tfName.textProperty());
                    tfName.setText(((IOpWithOperations) opData).getName());


                    //Количество
                    TextField tfN = new TextField();
                    Text txtN = new Text();
                    txtN.textProperty().bind(tfN.textProperty());
                    tfN.setText(String.valueOf(quantity));

                    //Все вместе
                    hbTitle.getChildren().add(logo);
                    hbTitle.getChildren().add(txtName);

                    if(quantity > 1) hbTitle.getChildren().addAll(new Text("("), txtN, new Text(" шт.)"));

                    setGraphic(hbTitle);

                    selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue){
                            hbTitle.setStyle("-fx-background-color: #f1e2af");
                            setStyle("-fx-background-color: #f1e2af");
                        } else {
                            hbTitle.setStyle(initStyle);
                            setStyle(initStyle);
                        }
                    });

                    setOnMouseClicked(e->{
                        if(e.isControlDown() && e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                            if (opData instanceof OpDetail) {
                                openFormEditor(EOpType.DETAIL, "ДЕТАЛЬ", "/fxml/formDetail.fxml", opData, tfName, tfN);
                            } else if (opData instanceof OpAssm) {
                                openFormEditor(EOpType.ASSM, "СБОРКА", "/fxml/formAssm.fxml", opData, tfName, tfN);
                            } else if (opData instanceof OpPack) {
                                openFormEditor(EOpType.PACK, "УПАКОВКА", "/fxml/formPack.fxml", opData, tfName, tfN);
                            }
                        }
                    });
                }
            }


        });

    }

    /**
     * Открыть форму редактирования сборки
     */
    private void openFormEditor(EOpType type, String title, String path, OpData opData, TextField tfName, TextField tfN) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent parent = loader.load();
            AbstractFormController formController = loader.getController();
            formController.init(MAIN_CONTROLLER, tfName, opData);
            Decoration windowDecoration = new Decoration(
                    title,
                    parent,
                    false,
                    MAIN_STAGE,
                    "decoration-assm",
                    true,
                    false);
            ImageView closer = windowDecoration.getImgCloseWindow();
            closer.setOnMousePressed(ev -> {
                switch(type){
                    case DETAIL:    PlateDetailController.collectOpData((OpDetail) opData, formController, tfName, tfN); break;
                    case ASSM:      PlateAssmController.collectOpData((OpAssm) opData, formController, tfName, tfN); break;
                    case PACK:      PlatePackController.collectOpData((OpPack) opData, tfName); break;
                }
                MAIN_CONTROLLER.countSumNormTimeByShops();
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createLeaf(TreeItem<OpData> treeItem){
        OpData opData = treeItem.getValue();
        List<OpData> operations = ((IOpWithOperations)opData).getOperations();
        for(OpData op : operations){
            if(op instanceof IOpWithOperations){
                TreeItem<OpData> newTreeItem = new TreeItem<>(op);
                treeItem.getChildren().add(newTreeItem);
                createLeaf(newTreeItem);
            }
        }
    }

    private double countPrefWidth(TextField tf){
        Text text = new Text(tf.getText());
        text.setFont(tf.getFont());
        return text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                + tf.getPadding().getLeft() + tf.getPadding().getRight() // Add the padding of the TextField
                + 2d;
    }
}
