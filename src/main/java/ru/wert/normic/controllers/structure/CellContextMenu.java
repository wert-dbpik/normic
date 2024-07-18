package ru.wert.normic.controllers.structure;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

public class CellContextMenu extends ContextMenu{

    private final TreeItem<OpData> item;
    private final TreeItem<OpData> parentItem;
    private final StructureController controller;
    private final TextField tfName;
    private final TextField tfN;
    private final ImgDouble imgDone;

    public CellContextMenu(TreeItem<OpData> item, TreeItem<OpData> parentItem, StructureController controller,
                           TextField tfName, TextField tfN, ImgDouble imgDone) {
        this.item = item;
        this.parentItem = parentItem;
        this.controller = controller;
        this.tfName = tfName;
        this.tfN = tfN;
        this.imgDone = imgDone;

        createMenu();
    }

    private void createMenu() {
        MenuItem edit = new MenuItem("Редактировать");
        edit.setOnAction(this::editItem);

        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(this::deleteItem);

        getItems().addAll(edit, delete);
    }

    /**
     * УДАЛИТЬ
     */
    private void deleteItem(ActionEvent actionEvent) {
        //Нельзя удалить корневой элемент
        if(controller.getTreeView().getRoot().equals(item)) return;

        if(parentItem != null && parentItem.getValue() instanceof IOpWithOperations) {
            //Получим индекс выше удаляемого элемента
            int indexToSelect = controller.getTreeView().getRow(item) - 1;
            controller.getTreeView().getSelectionModel().clearSelection();
            ((IOpWithOperations) parentItem.getValue()).getOperations().remove(item.getValue());
            controller.rebuildTree(parentItem, ((IOpWithOperations) parentItem.getValue()).getOpData());
            controller.getTreeView().getSelectionModel().select(indexToSelect);
        }
    }

    /**
     * РЕДАКТИРОВАТЬ
     */
    private void editItem(Event event){
        OpData opData = item.getValue();
        if (opData instanceof OpDetail) {
            controller.openFormEditor(item, EOpType.DETAIL, "ДЕТАЛЬ", "/fxml/formDetail.fxml", opData, tfName, tfN, imgDone);
        } else if (opData instanceof OpAssm) {
            controller.openFormEditor(item, EOpType.ASSM, "СБОРКА", "/fxml/formAssm.fxml", opData, tfName, tfN, imgDone);
        } else if (opData instanceof OpPack) {
            controller.openFormEditor(item, EOpType.PACK, "УПАКОВКА", "/fxml/formPack.fxml", opData, tfName, tfN, imgDone);
        }
    }
}
