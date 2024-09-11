package ru.wert.normic.operations;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;

public class OperationsContextMenu extends ContextMenu {
    final TableRow<SimpleOperation> tableRow;

    public OperationsContextMenu(OperationsController controller, TableRow<SimpleOperation> tableRow) {
        this.tableRow = tableRow;

        final MenuItem addItem = new MenuItem("Добавить");
        addItem.setOnAction(e-> controller.addSimpleOperation(e));

        final MenuItem copyItem = new MenuItem("Добавить копированием");
        copyItem.setOnAction(e-> controller.copySimpleOperation(tableRow));

        final MenuItem changeItem = new MenuItem("Изменить");
        changeItem.setOnAction(e-> controller.changeSimpleOperation(tableRow));

        final MenuItem deleteItem = new MenuItem("Удалить");
        deleteItem.setOnAction(e-> controller.deleteSimpleOperation(e, tableRow));

        if(tableRow.getItem() != null) {
            getItems().addAll(addItem, copyItem, changeItem, deleteItem);
        } else
            getItems().addAll(addItem);



    }

}
