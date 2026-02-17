package ru.wert.normic.controllers.menus.formMenus;

import ru.wert.normic.controllers._forms.AbstractFormController;

/**
 * Интерфейс для фабрик меню
 */
public interface MenuFactory {

    /**
     * Создает меню для указанного контроллера
     * @param controller контроллер формы
     * @return созданное меню
     */
    FormMenuManager createMenu(AbstractFormController controller);
}
