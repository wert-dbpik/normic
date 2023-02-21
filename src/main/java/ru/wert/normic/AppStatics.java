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

    //Комбобокс, где переключается единица измерения (мин, сек)
    public static ComboBox<ETimeMeasurement> MEASURE;

    //Используемая схема CSS
    public static String THEME_STYLE = "/css/calculator-sandy.css";

    public static List<KeyCode> KEYS_NOW_PRESSED; //Массив хранения нажатых клавиш

    //Перечень операций, которые нельзя добавить в Деталь
    public static final List<EOpType> RESTRICTED_FOR_DETAILS =
            Arrays.asList(DETAIL, ASSM, ASSM_CUTTINGS, ASSM_NUTS, ASSM_NODES, PAINT_ASSM, LEVELING_SEALER);

    //Перечень операций, которые нельзя добавить в Сборку
    public static final List<EOpType> RESTRICTED_FOR_ASSM =
            Arrays.asList(CUTTING, BENDING, PAINTING, TURNING, CUT_OFF, THREADING, DRILLING, ROLLING);

    //Перечень операций, которые м.б. продублированны
    public static final List<EOpType> DUPLICATED_OPERATIONS =
            Arrays.asList(DETAIL, ASSM, LEVELING_SEALER, TURNING, CUT_OFF, CUT_GROOVE, THREADING, DRILLING, ROLLING);

    //Перечень допустимых операций по типу материала
    public static List<EOpType> LIST_OPERATIONS = Arrays.asList(CUTTING, BENDING, LOCKSMITH, PAINTING, WELD_CONTINUOUS, WELD_DOTTED);//Листовой
    public static List<EOpType> ROUND_OPERATIONS = Arrays.asList(MOUNT_DISMOUNT, PAINTING, TURNING, CUT_GROOVE, THREADING, DRILLING, ROLLING, CUT_OFF); //Круглый
    public static List<EOpType> PROFILE_OPERATIONS = Arrays.asList(PAINTING); //Профильный
}
