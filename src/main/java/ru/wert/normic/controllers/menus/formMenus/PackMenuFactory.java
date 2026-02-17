package ru.wert.normic.controllers.menus.formMenus;

import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Collections;

/**
 * Фабрика для создания меню упаковки (FormPackController)
 */
public class PackMenuFactory implements MenuFactory {

    @Override
    public FormMenuManager createMenu(AbstractFormController controller) {
        FormMenuManager menu = new FormMenuManager(controller,
                controller.getListViewTechOperations(),
                (IOpWithOperations) controller.getOpData());

        menu.getItems().add(menu.createItemPackInCartoonBox());
        menu.getItems().add(menu.createItemPackOnPalletizer());
        menu.getItems().add(menu.createItemPackInHandStretchWrap());
        menu.getItems().add(menu.createItemPackInBubbleWrap());
        menu.getItems().add(menu.createItemPackOnPallet());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_PACKING));
        if (simpleOperationsMenu != null) {
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(simpleOperationsMenu);
        }

        return menu;
    }
}
