package ru.wert.normic.controllers.menus.plateMenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers._forms.MainController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

/**
 * МЕНЮ УДАЛЕНИЯ, КОПИРОВАНИЯ И ПЕРЕНОСА ОПЕРАЦИЙ
 */
public class PlateMenu {

    private static final int ICON_SIZE = 24;

    private ImageView createIcon(String resourcePath) {
        return new ImageView(new Image(
                getClass().getResource(resourcePath).toString(),
                ICON_SIZE, ICON_SIZE, true, true
        ));
    }

    public ContextMenu create(AbstractFormController formController, OpData opData, boolean cellIsEmpty) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");

        // Определяем состояние
        boolean isOpWithOps = opData instanceof IOpWithOperations && !cellIsEmpty;
        boolean isPastePossible = !AbstractFormController.clipOpDataList.isEmpty() &&
                formController.isPastePossible(cellIsEmpty);

        List<VBox> selectedItems = formController.getListViewTechOperations()
                .getSelectionModel().getSelectedItems();
        boolean isSingleSelection = selectedItems.size() == 1;

        // Создаем пункты меню только при необходимости
        if (isOpWithOps) {
            MenuItem done = createDoneMenuItem((IOpWithOperations) opData);
            contextMenu.getItems().add(done);
            contextMenu.getItems().add(new SeparatorMenuItem());
        }

        if (!cellIsEmpty) {
            if (isSingleSelection) {
                contextMenu.getItems().add(createCopyMenuItem(formController));
                contextMenu.getItems().add(createCutMenuItem(formController));
            }

            if (isPastePossible && isSingleSelection) {
                contextMenu.getItems().add(createPasteMenuItem(formController, cellIsEmpty));
            }

            contextMenu.getItems().add(createDeleteMenuItem(formController));
        }

        // Сохранение (создаем копию данных)
        if (isOpWithOps) {
            boolean hasOtherItems = !contextMenu.getItems().isEmpty();
            if (hasOtherItems) {
                contextMenu.getItems().add(new SeparatorMenuItem());
            }
            contextMenu.getItems().add(createSaveMenuItem((IOpWithOperations) opData));
        }

        return contextMenu;
    }

    private MenuItem createDoneMenuItem(IOpWithOperations opData) {
        MenuItem done = new MenuItem();
        if (!opData.isDone()) {
            done.setText("Готово");
            done.setOnAction(e -> opData.setDone(true));
            done.setGraphic(createIcon("/pics/btns/done.png"));
        } else {
            done.setText("НЕ готово");
            done.setOnAction(e -> opData.setDone(false));
            done.setGraphic(createIcon("/pics/btns/edit2.png"));
        }
        return done;
    }

    private MenuItem createCopyMenuItem(AbstractFormController formController) {
        MenuItem copy = new MenuItem("Копировать");
        copy.setOnAction(formController::copyOperation);
        copy.setGraphic(createIcon("/pics/btns/copy.png"));
        return copy;
    }

    private MenuItem createCutMenuItem(AbstractFormController formController) {
        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(formController::cutOperation);
        cut.setGraphic(createIcon("/pics/btns/cut.png"));
        return cut;
    }

    private MenuItem createPasteMenuItem(AbstractFormController formController, boolean cellIsEmpty) {
        MenuItem paste = new MenuItem("Вставить");
        paste.setOnAction(e -> formController.pasteOperation(cellIsEmpty));
        paste.setGraphic(createIcon("/pics/btns/paste.png"));
        return paste;
    }

    private MenuItem createDeleteMenuItem(AbstractFormController formController) {
        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(formController::deleteSelectedOperation);
        delete.setGraphic(createIcon("/pics/btns/close.png"));
        return delete;
    }

    private MenuItem createSaveMenuItem(IOpWithOperations opData) {
        MenuItem save = new MenuItem("Сохранить");
        List<OpData> addedOperations = opData.getOperations();
        String initialName = opData.getName();

        //Создаем копию данных для сохранения, не изменяя оригинал
        save.setOnAction(e -> {
            OpData copyForSave = createCopyForSave(opData);
            MainController.saveAs(copyForSave, addedOperations, initialName, e, EMenuSource.FORM_MENU);
        });

        save.setGraphic(createIcon("/pics/btns/save.png"));
        return save;
    }

    /**
     * Создает копию операции для сохранения, чтобы не изменять оригинал
     */
    private OpData createCopyForSave(IOpWithOperations original) {
        OpData originalOp = (OpData) original;
        OpData copy = originalOp.clone();
        copy.setQuantity(1);
        return copy;
    }
}
