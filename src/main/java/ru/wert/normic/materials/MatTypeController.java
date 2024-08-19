package ru.wert.normic.materials;

import ru.wert.normic.entities.db_connection.material.Material;

public interface MatTypeController {

    /**
     * Метод возвращает конкретный контроллер MatTypeController
     */
    MatTypeController getController();

    /**
     * Метод заполняет поля формы paramS и paramX
     */
    void fillData(Material material);

    /**
     * Метод проверяет введенные paramS и paramX на корректность
     */
    boolean checkData();

    /**
     * Метод считывает paramS с формы
     */
    double readParamS();

    /**
     * Метод считывает paramX с формы
     */
    double readParamX();

}
