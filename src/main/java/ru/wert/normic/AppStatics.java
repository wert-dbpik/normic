package ru.wert.normic;

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import ru.wert.normic.controllers._forms.MainController;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ETimeMeasurement;

import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.enums.EOpType.*;
import static ru.wert.normic.enums.EOpType.PAINTING;

public class AppStatics {

    public static MainController MAIN_CONTROLLER;

    //Текущая версия программы
    public static final String PROJECT_VERSION = "1.0";

    //Шаблоны для децимальных номеров
    public static final String DEC_NUMBER = "\\d{6}[.]\\d{3}";// XXXXXX.XXX
    public static final String DEC_NUMBER_WITH_EXT = "\\d{6}[.]\\d{3}[-]\\d{2,3}";// XXXXXX.XXX-ХХ(Х)
    public static final String SKETCH_NUMBER = "Э\\d{5}";// ЭХХХХХ
    public static final String SKETCH_NUM_WITH_EXT = "[Э]\\d{5}[-]\\d{2}";// ЭХХХХХ-ХХ

    //Комбобокс, где переключается единица измерения (мин, сек)
    public static ComboBox<ETimeMeasurement> MEASURE;

    //Используемая схема CSS
    public static String THEME_STYLE = "/css/calculator-sandy.css";

    public static List<KeyCode> KEYS_NOW_PRESSED; //Массив хранения нажатых клавиш

    //Перечень операций, которые можно добавить в Деталь
    public static final List<EOpType> DETAIL_OPERATIONS =
            Arrays.asList(CUTTING, BENDING, WELD_CONTINUOUS, WELD_DOTTED,
                    MOUNT_DISMOUNT, TURNING, CUT_GROOVE, THREADING, DRILLING, ROLLING,
                    DRILLING_BY_MARKING, LOCKSMITH, CUT_OFF, CHOP_OFF,
                    PAINTING
                   );

    //Перечень операций, которые можно добавить в Сборку
    public static final List<EOpType> ASSM_OPERATIONS =
            Arrays.asList(DETAIL, ASSM, PACK,
                    PAINT_ASSM,
                    WELD_CONTINUOUS, WELD_DOTTED,
                    ASSM_CUTTINGS, ASSM_NUTS, ASSM_NODES,
                    LEVELING_SEALER
                    );

    //Перечень операций, которые м.б. продублированны
    public static final List<EOpType> DUPLICATED_OPERATIONS =
            Arrays.asList(DETAIL, ASSM, WELD_CONTINUOUS, LEVELING_SEALER,
                    TURNING, CUT_GROOVE, THREADING, DRILLING, ROLLING,
                    DRILLING_BY_MARKING,
                    PACK, PACK_CABINET, MOUNT_ON_PALLET);

    //Перечень допустимых операций по типу материала
    public static List<EOpType> LIST_OPERATIONS = Arrays.asList(CUTTING, BENDING,
            LOCKSMITH, DRILLING_BY_MARKING,
            PAINTING,
            WELD_CONTINUOUS, WELD_DOTTED);//Листовой
    public static List<EOpType> ROUND_OPERATIONS = Arrays.asList(MOUNT_DISMOUNT,
            PAINTING, TURNING, CUT_GROOVE, THREADING, DRILLING, ROLLING,
            CUT_OFF, CHOP_OFF); //Круглый
    public static List<EOpType> PROFILE_OPERATIONS = Arrays.asList(PAINTING,
            LOCKSMITH, DRILLING_BY_MARKING,
            CHOP_OFF, CUT_OFF_ON_SAW); //Профильный
}
