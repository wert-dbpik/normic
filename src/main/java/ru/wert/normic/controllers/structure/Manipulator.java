package ru.wert.normic.controllers.structure;

import javafx.event.Event;
import javafx.scene.control.TreeItem;
import ru.wert.normic.decoration.warnings.Warning0;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import static java.lang.String.format;

public class Manipulator {

    enum InsertError {
        NO_ERROR,
        ERROR_AUTO_INSERT,
        ERROR_INSERT_TO_DETAIL;
    }

    public static TreeItem<OpData> cuttedParent; //Родитель вырезанного объекта
    public static Integer cuttedPosition; //позиция скопированного элемента в списке операций, null - если ничего не вырезано
    public static TreeItem<OpData> clipItem; //Объект в клипборде
    private final StructureController controller;

    public Manipulator(StructureController controller) {
        this.controller = controller;
    }

    /**
     * ВСТАВИТЬ
     * Вставка в определенную позицию только в случае отмены операции ВЫРЕЗАТЬ
     */
    public void pasteOperation(Event e) {
        TreeItem<OpData> targetItem =
                cuttedPosition == null ?
                        controller.getTreeView().getSelectionModel().getSelectedItem() :
                        cuttedParent;

        String targetName = ((IOpWithOperations) targetItem.getValue()).getName();
        String name = ((IOpWithOperations) clipItem.getValue()).getName();
        InsertError answer = pastePossible(targetItem);

        if (answer.equals(InsertError.ERROR_AUTO_INSERT))
            Warning0.create("Внимание!",
                    format("Вставить '%s' в себя невозможно!", name));
        else if (answer.equals(InsertError.ERROR_INSERT_TO_DETAIL))
            Warning0.create("Внимание!",
                    format("Вставить '%s' в '%s' невозможно!", name, targetName));
        else if (answer.equals(InsertError.NO_ERROR)) {
            OpAssm target = (OpAssm) targetItem.getValue();
            if (cuttedPosition != null) {
                target.getOperations().add(cuttedPosition, clipItem.getValue());
                cuttedPosition = null;
                cuttedParent = null;
            } else {
                target.getOperations().add(clipItem.getValue());
            }

            controller.rebuildAll(targetItem);
        }

    }

    /**
     * ВЫРЕЗАТЬ
     */
    public void cutOperation(Event e) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        clipItem = selectedItem;
        cuttedParent = selectedItem.getParent();
        cuttedPosition = ((IOpWithOperations) selectedItem.getParent().getValue()).getOperations().indexOf(clipItem.getValue());
        deleteItem(e);
    }

    /**
     * Проверить, если возможна вставка
     */
    public InsertError pastePossible(TreeItem<OpData> target) {
        if (target.equals(clipItem))
            return InsertError.ERROR_AUTO_INSERT;
        if (target.getValue() instanceof OpDetail || target.getValue() instanceof OpPack)
            return InsertError.ERROR_INSERT_TO_DETAIL;
        return
                InsertError.NO_ERROR;
    }

    /**
     * КОПИРОВАТЬ
     */
    public void copyOperation(Event e) {
        clipItem = controller.getTreeView().getSelectionModel().getSelectedItem();
    }

    /**
     * УДАЛИТЬ
     */
    public void deleteItem(Event e) {
        TreeItem<OpData> selectedItem = controller.getTreeView().getSelectionModel().getSelectedItem();
        TreeItem<OpData> parentItem = selectedItem.getParent();
        //Нельзя удалить корневой элемент
        if (controller.getTreeView().getRoot().equals(selectedItem)) return;

        if (parentItem != null && parentItem.getValue() instanceof IOpWithOperations) {
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
    public void editItem(Event e) {
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

    /**
     * ОТМЕНИТЬ ВСТАВКУ
     */
    public void cancelPasting(Event e) {
        if (clipItem == null) return;
        if (cuttedPosition != null) {
            pasteOperation(e);
//            controller.getTreeView().getSelectionModel().select(clipItem);
            cuttedPosition = null;
            cuttedParent = null;
            clipItem = null;
        }
    }

}
