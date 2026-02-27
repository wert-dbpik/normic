package ru.wert.normic.entities.ops;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers._plates.AbstractOpPlate;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

import java.io.Serializable;

/**
 * Класс наследуется всеми entities
 */
@Getter
@Setter
public class OpData implements Serializable, Cloneable {

    transient protected int total = 1; //Общее количество в изделии
    transient protected AbstractOpPlate plateController; //

    protected ENormType normType; //Тип нормы по цехам (МК, ППК и т.д.)
    protected EJobType jobType = EJobType.JOB_NONE; //Тип операции по участкам (Резка, Гибка, Сварка и т.д)
    protected EOpType opType; //Тип олперации (Гибка, покраска и т.д))

    private int quantity = 1; //количество

    private double mechTime; //МК
    private double paintTime; //ППК
    private double assmTime; //Сборка
    private double packTime; //Упаковка
    private double electricalTime; //Электромонтаж


    private double totalTime; //Общее время

    @Override
    public OpData clone() {
        try {
            OpData clone = (OpData) super.clone();

            // plateController не клонируем, так как это transient и
            // ссылка на контроллер должна быть установлена заново при необходимости
            clone.plateController = null;

            // Клонируем enum поля (они immutable, но глубокое копирование не требуется)
            // normType, jobType, opType - enum, они копируются автоматически

            // quantity и времена копируются автоматически при поверхностном копировании

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("OpData должен поддерживать Cloneable", e);
        }
    }

}
