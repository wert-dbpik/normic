package ru.wert.normic;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import ru.wert.normic.controllers._forms.MainController;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ETimeMeasurement;

import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.enums.EOpType.*;
import static ru.wert.normic.enums.EOpType.PAINTING;

public class AppStatics {

    public static boolean TEST_VERSION = true; //тестовая версия - работает с тестовым сервером

    public static MainController MAIN_CONTROLLER;

    //Текущая версия программы
    public static final String PROJECT_VERSION = "1.0";

    //Шаблоны для децимальных номеров
    public static final String DEC_NUMBER = "\\d{6}[.]\\d{3}";// XXXXXX.XXX
    public static final String DEC_NUMBER_WITH_EXT = "\\d{6}[.]\\d{3}[-]\\d{2,3}";// XXXXXX.XXX-ХХ(Х)
    public static final String SKETCH_NUMBER = "Э\\d{5}";// ЭХХХХХ
    public static final String SKETCH_NUM_WITH_EXT = "[Э]\\d{5}[-]\\d{2}";// ЭХХХХХ-ХХ

    //Комбобокс, где переключается единица измерения (мин, сек)
    public static ToggleGroup MEASURE;
    public static ETimeMeasurement CURRENT_MEASURE;

    //Используемая схема CSS
    public static String THEME_STYLE = "/css/calculator-sandy.css";

    public static List<KeyCode> KEYS_NOW_PRESSED; //Массив хранения нажатых клавиш

    //Перечень операций, которые можно добавить в Деталь
    public static final List<EOpType> DETAIL_OPERATIONS =
            Arrays.asList(CUTTING, BENDING, WELD_CONTINUOUS, WELD_DOTTED,
                    LATHE_MOUNT_DISMOUNT, LATHE_TURNING, LATHE_CUT_GROOVE, LATHE_THREADING, LATHE_DRILLING, LATHE_ROLLING,
                    DRILLING_BY_MARKING, LOCKSMITH, LATHE_CUT_OFF, CHOP_OFF,
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

    //Перечень операций, которые можно добавить в УПАКОВКУ
    public static final List<EOpType> PACK_OPERATIONS =
            Arrays.asList(
                    PACK_ON_PALLET, PACK_IN_MACHINE_STRETCH_WRAP, PACK_IN_HAND_STRETCH_WRAP,
                    PACK_IN_CARTOON_BOX, PACK_IN_BUBBLE_WRAP
            );

    //Перечень операций, которые м.б. продублированны
    public static final List<EOpType> DUPLICATED_OPERATIONS =
            Arrays.asList(DETAIL, ASSM, WELD_CONTINUOUS, LEVELING_SEALER,
                    LATHE_TURNING, LATHE_CUT_GROOVE, LATHE_THREADING, LATHE_DRILLING, LATHE_ROLLING,
                    DRILLING_BY_MARKING,
                    PACK, PACK_ON_PALLET);

    //Перечень допустимых операций по типу материала
    public static List<EOpType> LIST_OPERATIONS = Arrays.asList(CUTTING, BENDING,
            LOCKSMITH, DRILLING_BY_MARKING,
            PAINTING,
            WELD_CONTINUOUS, WELD_DOTTED);//Листовой
    public static List<EOpType> ROUND_OPERATIONS = Arrays.asList(LATHE_MOUNT_DISMOUNT,
            PAINTING, LATHE_TURNING, LATHE_CUT_GROOVE, LATHE_THREADING, LATHE_DRILLING, LATHE_ROLLING,
            LATHE_CUT_OFF, CHOP_OFF); //Круглый
    public static List<EOpType> PROFILE_OPERATIONS = Arrays.asList(PAINTING,
            LOCKSMITH, DRILLING_BY_MARKING,
            CHOP_OFF, CUT_OFF_ON_SAW); //Профильный

}
