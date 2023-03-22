package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.wert.normic.controllers._forms.AbstractFormController;
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

                    setOnMouseClicked(e->{
                        if(opData instanceof OpDetail){
                            openFormEditor("ДЕТАЛЬ","/fxml/formDetail.fxml", opData, tfName);
                        } else if(opData instanceof OpAssm){
                            openFormEditor("СБОРКА","/fxml/formAssm.fxml", opData, tfName);
                        } else  if(opData instanceof OpPack){
                            openFormEditor("УПАКОВКА","/fxml/formPack.fxml", opData, tfName);
                        }
                    });
                }
            }


        });

    }

    /**
     * Открыть форму редактирования сборки
     */
    private void openFormEditor(String title, String path, OpData opData, AbstractFormController clazz, TextField tfName) {
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
            closer.setOnMousePressed(ev -> clazz.collectOpData(opData));
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
}
