package ru.wert.normic.controllers.menus.formMenus;

import ru.wert.normic.controllers._forms.*;


import java.util.NoSuchElementException;

/**
 * Класс больше не используется, оставлен для обратной совместимости
 * @deprecated Используйте отдельные фабрики меню
 */
@Deprecated
public class FormMenus {

    public enum EMenuType {
        MAIN_TYPE,
        ASSM_TYPE,
        DETAIL_TYPE,
        ELECTRICAL_TYPE;
    }

    private final AbstractFormController controller;

    public FormMenus(AbstractFormController controller) {
        this.controller = controller;
    }

    public FormMenuManager create(EMenuType type){
        switch(type){
            case MAIN_TYPE:
                return new MainMenuFactory().createMenu(controller);
            case ASSM_TYPE:
                return new AssmMenuFactory().createMenu(controller);
            case DETAIL_TYPE:
                return new DetailMenuFactory().createMenu(controller);
            case ELECTRICAL_TYPE:
                return new ElectricalMenuFactory().createMenu(controller);
        }
        throw new NoSuchElementException("Неожиданный тип меню");
    }
}
