package ru.wert.normic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ru.wert.normic.controllers._forms.MainController;
import ru.wert.normic.controllers.intro.ConnectionParams;
import ru.wert.normic.entities.db_connection.UserGroup.UserGroup;
import ru.wert.normic.entities.db_connection.logs.AppLog;
import ru.wert.normic.entities.db_connection.logs.AppLogService;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.user.User;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintOld;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.saves.SaveNormEntry;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.enums.EOpType.*;
import static ru.wert.normic.enums.EOpType.PAINTING;

public class AppStatics {

    //Текущая версия программы
    public static final String CURRENT_PROJECT_VERSION = "2.0";
    //Последняя доступная версия в БД
    public static String LAST_VERSION_IN_DB;
    //тестовая версия - работает с тестовым сервером
    public static boolean TEST_VERSION = false;

    public static List<SaveNormEntry> SAVES_HISTORY = new ArrayList<>(); //История сохранений
    public final static BooleanProperty BATCHNESS = new SimpleBooleanProperty(true); //учет партийности
    public static int DEFAULT_BATCH = 1; //расчетная партия штук по умолчанию
    public static int CURRENT_BATCH = 1; //расчетная партия штук


    //Ползователь
    public static User CURRENT_USER = null;
    //Группа
    public static UserGroup CURRENT_USER_GROUP = null;

    public static MainController MAIN_CONTROLLER;
    public static OpAssm MAIN_OP_DATA;
    public static Stage SEARCH_WINDOW;

    public static String LAUNCH_TIME; //Переменная, по которой происходит идентификация свой/чужой при перетаскивании плашек


    public static final String TEST_SERVER_IP = "192.168.2.175";
    public static final String SERVER_IP = "192.168.2.132";
    public static final String SERVER_PORT = "8080";

    public static ConnectionParams CURRENT_CONNECTION_PARAMS;

    public static final String NORMIC_HOME_PATH = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "NormIC";
    public static final String PROPS_PATH = TEST_VERSION ? NORMIC_HOME_PATH + File.separator + "settingsTest.properties" :
            NORMIC_HOME_PATH + File.separator + "settings.properties";



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

    public static Material NO_MATERIAL = new Material("нет");
    public static String NEW_PRODUCT = "НОВОЕ ИЗДЕЛИЕ";
    public static String CURRENT_PRODUCT_NAME;

    public static List<KeyCode> KEYS_NOW_PRESSED; //Массив хранения нажатых клавиш

    //Перечень операций, которые можно добавить в Деталь
    public static final List<EOpType> DETAIL_OPERATIONS =
            Arrays.asList(CUTTING, BENDING, WELD_CONTINUOUS, WELD_DOTTED,
                    LATHE_MOUNT_DISMOUNT, LATHE_TURNING, LATHE_CUT_GROOVE, LATHE_THREADING, LATHE_DRILLING, LATHE_ROLLING,
                    DRILLING_BY_MARKING, LOCKSMITH, LATHE_CUT_OFF, CHOP_OFF,
                    ASSM_CHOP_OFF,
                    PAINTING, PAINT_NEW
                   );

    //Перечень операций, которые можно добавить в Сборку
    public static final List<EOpType> ASSM_OPERATIONS =
            Arrays.asList(DETAIL, ASSM, PACK,
                    PAINT_ASSM,
                    WELD_CONTINUOUS, WELD_DOTTED,
                    ASSM_CUTTINGS, ASSM_NUTS, ASSM_NODES,
                    LEVELING_SEALER, THERMO_INSULATION
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
                    DRILLING_BY_MARKING, THERMO_INSULATION,
                    PACK, PACK_ON_PALLET);

    //Перечень допустимых операций по типу материала
    public static List<EOpType> LIST_OPERATIONS = Arrays.asList(CUTTING, BENDING,
            LOCKSMITH, DRILLING_BY_MARKING,
            PAINTING, PAINT_NEW,
            WELD_CONTINUOUS, WELD_DOTTED);//Листовой
    public static List<EOpType> ROUND_OPERATIONS = Arrays.asList(LATHE_MOUNT_DISMOUNT,
            PAINTING, PAINT_NEW, LATHE_TURNING, LATHE_CUT_GROOVE, LATHE_THREADING, LATHE_DRILLING, LATHE_ROLLING,
            LATHE_CUT_OFF, CHOP_OFF); //Круглый
    public static List<EOpType> PROFILE_OPERATIONS = Arrays.asList(PAINTING,
            LOCKSMITH, DRILLING_BY_MARKING,
            CHOP_OFF, CUT_OFF_ON_SAW); //Профильный

    /**
     * Метод создает запись лога в базе данных
     */
    public static void createLog(boolean forAdminOnly, String text) {

        if (forAdminOnly && !CURRENT_USER.isLogging()) return;

        AppLogService.getInstance().save(new AppLog(
                LocalDateTime.now().toString(),
                forAdminOnly,
                CURRENT_USER,
                2, //Normic
                CURRENT_PROJECT_VERSION,
                text
        ));

    }

    /**
     * Округляет num до 0.001
     */
    public static double roundTo001(double num){
        num = Math.round(num * 1000);
        num = num/1000;
        return num;
    }

    /**
     * Определяет если деталь или сборка окрашены
     */
    public static boolean checkIfPainted(IOpWithOperations opData){
        boolean painted = false;
        for(OpData op : opData.getOperations()){
            if(op instanceof OpPaintOld || op instanceof OpPaintAssm) {
                painted = true;
                break;
            }
        }
        return painted;
    }
}
