package ru.wert.normik.interfaces;


import ru.wert.normik.entities.OpData;

/**
 * Интерфейс верхнего уровня, описывает все операции, добавленные в лист операций
 *
 */
public interface IOpPlate {

    /**
     * Метод возвращает данные по операции, в том числе и нормы времени по участкам
     * Используется для восстановления закрытых плашек и сохранения расчетов
     */
    OpData getOpData();


}
