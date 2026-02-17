package ru.wert.normic.controllers.menus.formMenus;

import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Collections;

/**
 * Фабрика для создания меню главной формы (MainController)
 */
public class MainMenuFactory implements MenuFactory {

    @Override
    public FormMenuManager createMenu(AbstractFormController controller) {
        FormMenuManager menu = new FormMenuManager(controller,
                controller.getListViewTechOperations(),
                (IOpWithOperations) controller.getOpData());

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().addAll(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemWeldAssm());
        menu.getItems().addAll(menu.createItemWeldContinuousNew());
        menu.getItems().addAll(menu.createItemWeldingDotted());
        menu.getItems().addAll(menu.createItemWeldDifficulty());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().addAll(menu.createItemAssmNuts());
        menu.getItems().addAll(menu.createItemAssmNutsMK());
        menu.getItems().addAll(menu.createItemAssmCuttings());
        menu.getItems().addAll(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(menu.createItemThermoInsulation());
        menu.getItems().add(new SeparatorMenuItem());//-----------------
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if (simpleOperationsMenu != null) {
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(simpleOperationsMenu);
        }

        return menu;
    }
}
