package ru.wert.normic;

import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import ru.wert.normic.controllers.forms.MainController;
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

    //Краска и расход г/м2
    public static String RAL_I = "RAL7035"; public static int CONSUMPTION_I = 150;
    public static String RAL_II = "RAL9006"; public static int CONSUMPTION_II = 150;
    public static String RAL_III = "RAL7035"; public static int CONSUMPTION_III = 150;

    public static List<KeyCode> KEYS_NOW_PRESSED; //Массив хранения нажатых клавиш

    public static final List<EOpType> RESTRICTED_FOR_DETAILS =  //Перечень операций, которые нельзя добавить в Деталь
            Arrays.asList(DETAIL, ASSM, ASSM_CUTTINGS, ASSM_NUTS, ASSM_NODES, PAINT_ASSM, LEVELING_SEALER);
    public static final List<EOpType> RESTRICTED_FOR_ASSM = //Перечень операций, которые нельзя добавить в Сборку
            Arrays.asList(CUTTING, BENDING, PAINTING);
    public static final List<EOpType> DUPLICATED_OPERATIONS =  //Перечень операций, которые м.б. проудублированны
            Arrays.asList(DETAIL, ASSM, LEVELING_SEALER);
}
