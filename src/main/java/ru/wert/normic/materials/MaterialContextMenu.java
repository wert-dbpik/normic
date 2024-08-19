package ru.wert.normic.materials;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import ru.wert.normic.entities.db_connection.material.Material;

public class MaterialContextMenu extends ContextMenu {
    final TableRow<Material> tableRow;

    public MaterialContextMenu(MaterialsTVController controller, TableRow<Material> tableRow) {
        this.tableRow = tableRow;

        final MenuItem copyItem = new MenuItem("Добавить копированием");
        copyItem.setOnAction(e-> controller.copyMaterial(tableRow));

        final MenuItem changeItem = new MenuItem("Изменить");
        changeItem.setOnAction(e-> controller.changeMaterial(tableRow));

        final MenuItem deleteItem = new MenuItem("Удалить");
        deleteItem.setOnAction(e-> controller.deleteMaterial(e, tableRow));

        getItems().addAll(copyItem, changeItem, deleteItem);

    }

}
