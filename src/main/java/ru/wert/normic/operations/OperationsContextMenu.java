package ru.wert.normic.operations;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import ru.wert.normic.entities.db_connection.othersOps.SimpleOperation;

public class OperationsContextMenu extends ContextMenu {
    final TableRow<SimpleOperation> tableRow;

    public OperationsContextMenu(OperationsController controller, TableRow<SimpleOperation> tableRow) {
        this.tableRow = tableRow;

        final MenuItem copyItem = new MenuItem("Добавить копированием");
        copyItem.setOnAction(e-> controller.copySimpleOperation(tableRow));

        final MenuItem changeItem = new MenuItem("Изменить");
        changeItem.setOnAction(e-> controller.changeSimpleOperation(tableRow));

        final MenuItem deleteItem = new MenuItem("Удалить");
        deleteItem.setOnAction(e-> controller.deleteSimpleOperation(e, tableRow));

        getItems().addAll(copyItem, changeItem, deleteItem);

    }

}
