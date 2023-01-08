package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

import java.io.Serializable;

/**
 * Класс наследуется всеми entities
 */
@Getter
@Setter
public class OpData implements Serializable {

    protected ENormType normType; //Тип нормы по участкам (МК, ППК и т.д.)
    protected EOpType opType; //Тип олперации (Гибка, покраска и т.д))

    private int quantity = 1; //количество

    private double mechTime; //МК
    private double paintTime; //ППК
    private double assmTime; //Сборка
    private double packTime; //Упаковка

    private double totalTime; //Общее время

}
