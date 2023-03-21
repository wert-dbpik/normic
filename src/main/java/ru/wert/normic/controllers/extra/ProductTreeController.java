package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpAssm;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

public class ProductTreeController {

    @FXML
    private TreeView<OpData> treeView;

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
                    //Лого
                    ImageView logo = new ImageView(type.getLogo());
                    logo.setFitWidth(16);
                    logo.setFitHeight(16);
                    //Номер с наименованием
                    TextField tfName = new TextField(((IOpWithOperations) opData).getName());
                    //Количество
                    StringBuilder sb = new StringBuilder();
                    if(quantity > 1) sb.append(" (")
                            .append(quantity)
                            .append(" шт.)");

                    //Все вместе
                    hbTitle.getChildren().add(logo);
                    hbTitle.getChildren().add(tfName);
                    hbTitle.getChildren().add(new Label(sb.toString()));
                    setGraphic(hbTitle);
                }
            }


        });

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
}
