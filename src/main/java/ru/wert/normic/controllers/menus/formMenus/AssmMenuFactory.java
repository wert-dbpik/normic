package ru.wert.normic.controllers.menus.formMenus;

import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Collections;

/**
 * Фабрика для создания меню сборки (FormAssmController)
 */
public class AssmMenuFactory implements MenuFactory {

    @Override
    public FormMenuManager createMenu(AbstractFormController controller) {
        FormMenuManager menu = new FormMenuManager(controller,
                controller.getListViewTechOperations(),
                (IOpWithOperations) controller.getOpData());

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemWeldAssm());
        menu.getItems().add(menu.createItemWeldContinuousNew());
        menu.getItems().add(menu.createItemWeldingDotted());
        menu.getItems().add(menu.createItemWeldDifficulty());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemAssmNuts());
        menu.getItems().add(menu.createItemAssmNutsMK());
        menu.getItems().add(menu.createItemAssmCuttings());
        menu.getItems().add(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());
        menu.getItems().add(new SeparatorMenuItem());//-----------------

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if (simpleOperationsMenu != null)
            menu.getItems().add(simpleOperationsMenu);

        return menu;
    }
}
