package ru.wert.normic.entities.ops.opWelding;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EWeldDifficulty;

/**
 * СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ
 */
@Getter
@Setter
public class OpWeldDifficulty extends OpData {

    private EWeldDifficulty difficulty = EWeldDifficulty.MIDDLE; //Средняя сложность по умолчанию

    public OpWeldDifficulty() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_WELDING;
        super.opType = EOpType.WELD_DIFFICULTY;
    }

    @Override
    public String toString() {
        return "сложность сварки = " + difficulty.getDifficultyName();
    }
}
