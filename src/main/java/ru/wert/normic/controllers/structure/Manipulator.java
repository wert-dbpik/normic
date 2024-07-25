package ru.wert.normic.controllers.structure;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.TreeItem;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

public class Manipulator {

    public static OpData clipOpData; //Объект в клипборде
    private final StructureController controller;

    public Manipulator(StructureController controller) {
        this.controller = controller;
    }

    /**
     * ВСТАВИТЬ
     */
    public void pasteOperation(Event e) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        OpAssm target = (OpAssm) selectedItem.getValue();
        target.getOperations().add(clipOpData);

        controller.rebuildAll(selectedItem);

    }

    /**
     * ВЫРЕЗАТЬ
     */
    public void cutOperation(Event e) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        clipOpData = selectedItem.getValue();
        deleteItem(e);
    }

    /**
     * Проверить, если возможна вставка
     */
    public boolean ifPastePossible(TreeItem<OpData> item) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        return
                !(selectedItem.getValue() instanceof OpDetail) &&
                        !(selectedItem.getValue() instanceof OpPack);
    }

    /**
     * КОПИРОВАТЬ
     */
    public void copyOperation(Event e) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        clipOpData = selectedItem.getValue();
    }

    /**
     * УДАЛИТЬ
     */
    public void deleteItem(Event e) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        TreeItem<OpData> parentItem = selectedItem.getParent();
        //Нельзя удалить корневой элемент
        if(controller.getTreeView().getRoot().equals(selectedItem)) return;

        if(parentItem != null && parentItem.getValue() instanceof IOpWithOperations) {
            //Получим индекс выше удаляемого элемента
            int indexToSelect = controller.getTreeView().getRow(selectedItem) - 1;
            controller.getTreeView().getSelectionModel().clearSelection();
            ((IOpWithOperations) parentItem.getValue()).getOperations().remove(selectedItem.getValue());
            controller.rebuildAll(parentItem);
            controller.getTreeView().getSelectionModel().select(indexToSelect);
        }
    }

    /**
     * РЕДАКТИРОВАТЬ
     */
    public void editItem(Event e){
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        TreeViewCell cell = TreeViewCell.selectedCell;
        OpData opData = selectedItem.getValue();
        if (opData instanceof OpDetail) {
            controller.openFormEditor(selectedItem, EOpType.DETAIL, "ДЕТАЛЬ", "/fxml/formDetail.fxml", opData, cell);
        } else if (opData instanceof OpAssm) {
            controller.openFormEditor(selectedItem, EOpType.ASSM, "СБОРКА", "/fxml/formAssm.fxml", opData, cell);
        } else if (opData instanceof OpPack) {
            controller.openFormEditor(selectedItem, EOpType.PACK, "УПАКОВКА", "/fxml/formPack.fxml", opData, cell);
        }
    }

}
