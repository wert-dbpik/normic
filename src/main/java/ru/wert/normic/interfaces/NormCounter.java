package ru.wert.normic.interfaces;

import ru.wert.normic.entities.ops.OpData;

/**
 * Интерфейс отвечает за расчет норм времени
 */
public interface NormCounter {

    OpData count(OpData opdata);
}
