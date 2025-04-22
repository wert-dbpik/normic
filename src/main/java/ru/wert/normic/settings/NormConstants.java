package ru.wert.normic.settings;

import javafx.concurrent.Service;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.files.FilesService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;


@Slf4j
public class NormConstants {

    private static NormConstants instance;

    public static NormConstants getInstance() {
        if (instance == null) {
            instance = new NormConstants();
        }
        return instance;
    }

    private Properties constantsProps;
    @Getter
    private String homeDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "NormIC";
    private final String appConstantsPath = homeDir + File.separator + "constants.properties";
    private File constantsFile;

    public static List<String> constantNames = Arrays.asList(
            "CUTTING_SPEED","REVOLVER_SPEED","PERFORATION_SPEED","CUTTING_SERVICE_RATIO","STRIPING_SPEED",//--- РЕЗКА И ЗАЧИСТКА
            "BENDING_SPEED","BENDING_SERVICE_RATIO",//--- ГИБКА
            "CHOP_SPEED","RIVETS_SPEED","COUNTERSINKING_SPEED","THREADING_SPEED","SMALL_SAWING_SPEED","BIG_SAWING_SPEED",//--- СЛЕСАРНЫЕ РАБОТЫ
            "DETAIL_DELTA","WASHING","WINDING","DRYING","BAKING",//--- ПОКРАСКА ДЕТАЛИ
            "ASSM_DELTA","HANGING_TIME","WINDING_MOVING_SPEED","SOLID_BOX_SPEED","FRAME_SPEED",//--- ПОКРАСКА СБОРКИ
            "WELDING_SPEED",//--- СВАРКА НЕПРЕРЫВНАЯ
            "WELDING_PARTS_SPEED","WELDING_DOTTED_SPEED","WELDING_DROP_SPEED",//--- СВАРКА ТОЧЕЧНАЯ
            "LEVELING_PREPARED_TIME","LEVELING_SPEED",//--- ЗАЛИВКА УПЛОТНИТЕЛЯ
            "SCREWS_SPEED","VSHGS_SPEED","RIVET_NUTS_SPEED","GROUND_SETS_SPEED","OTHERS_SPEED",//--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ
            "POST_LOCKS_SPEED","DOUBLE_LOCKS_SPEED","GLASS_SPEED","DETECTORS_SPEED","CONNECTION_BOXES_SPEED",//--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
            "SEALER_SPEED","SELF_ADH_SEALER_SPEED","INSULATION_SPEED","SCOTCH_SPEED",//--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
            "CARTOON_BOX_PREPARED_TIME","CARTOON_BOX_SPEED","STRETCH_MACHINE_WINDING","CARTOON_BOX_AND_ANGLES_SPEED",//--- УПАКОВКА
            "PACK_IN_CARTOON_BOX_SPEED","DUCT_TAPE_LENGTH","BUBBLE_CUT_AND_DUCT","BUBBLE_HAND_WINDING","STRETCH_HAND_WINDING");

    //--- РЕЗКА И ЗАЧИСТКА
    public static double CUTTING_SPEED; //Скорость резания, зависящая от площади детали, мин
    public static double REVOLVER_SPEED; //Скорость вырубки одного элемента револьвером, мин/уд
    public static double PERFORATION_SPEED; //Скорость перфорирования, мин/уд
    public static double CUTTING_SERVICE_RATIO; //коэфффициент, учитывающий 22% времени на обслуживание при резке
    public static double STRIPING_SPEED; //Скорость зачистки резанных кромок, сек

    //--- ГИБКА
    public static double UNIVERSAL_BENDING_SPEED; //Скорость гибки, мин/гиб
    public static double BENDING_SERVICE_RATIO; //коэфффициент, учитывающий 25% времени на обслуживание при гибке

    //--- СЛЕСАРНЫЕ РАБОТЫ
    public static double CHOP_SPEED; //Скорость рубки на Геке, мин/удар
    public static double RIVETS_SPEED; //Скорость установки вытяжной заклепки, сек/закл
    public static double COUNTERSINKING_SPEED; //Скорость сверления и зенковки, мин/отв
    public static double THREADING_SPEED; //Скорость нарезания резьбы, мин/отв
    public static double SMALL_SAWING_SPEED; //Скорость пиления на малой пиле, мин/рез
    public static double BIG_SAWING_SPEED; //Скорость пиления на большой пиле, мин/рез

    //--- ПОКРАСКА ДЕТАЛИ
    public static int    DETAIL_DELTA; //Расстояние между деталями, мм
    public static double WASHING; //Мойка, сек
    public static double WINDING; //Продувка, сек
    public static double DRYING; //Сушка, сек
    public static double BAKING; //Полимеризация, мин

    //--- ПОКРАСКА СБОРКИ
    public static int    ASSM_DELTA; //Расстояние между сборками, мм
    public static double HANGING_TIME; //Время навески и снятия после полимеризации, мин
    public static double WINDING_MOVING_SPEED; //Продувка после промывки и перемещение изделя на штанге, мин/1 м.кв.
    public static double SOLID_BOX_SPEED; //Скорость окрашивания глухих шкафов, мин/1 м.кв.
    public static double FRAME_SPEED; //Скорость окрашивания открытых рам и кроссов, мин/1 м.кв.

    //--- СВАРКА НЕПРЕРЫВНАЯ
    public static double WELDING_SPEED; //Скорость сваркм, мин/м

    //--- СВАРКА ТОЧЕЧНАЯ
    public static double WELDING_PARTS_SPEED; //Скорость онденсаторной сварки точкой, мин/элемент
    public static double WELDING_DOTTED_SPEED; //Скорость контактной сварки, мин/точку
    public static double WELDING_DROP_SPEED; //Скорость сварки прихватками, мин/прихватку

    //--- ЗАЛИВКА УПЛОТНИТЕЛЯ
    public static double LEVELING_PREPARED_TIME; //ПЗ время, мин
    public static double LEVELING_SPEED; //Скорость нанесения, м/мин

    //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ
    public static double SCREWS_SPEED; //Скорость установки винтов, мин
    public static double VSHGS_SPEED; //Скорость установки комплектов ВШГ, мин
    public static double RIVET_NUTS_SPEED; //Скорость установки заклепочных гаек, сек
    public static double GROUND_SETS_SPEED; //Скорость установки комплекта заземления с этикеткой, мин
    public static double OTHERS_SPEED; //Скорость установки другого крепежа, сек

    //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    public static double POST_LOCKS_SPEED; //Скорость установки почтового замка с регулировкой, мин
    public static double DOUBLE_LOCKS_SPEED; //Скорость установки замка с рычагами, мин
    public static double GLASS_SPEED; //Скорость установки стекла на полиуретан, мин
    public static double DETECTORS_SPEED; //Скорость установки извещателей (ИО-102), мин
    public static double CONNECTION_BOXES_SPEED; //Скорость установки коробки соединительной (КС-4), мин

    //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    public static double SEALER_SPEED; //Скорость монтажа уплотнителя на ребро корпуса, сек/м
    public static double SELF_ADH_SEALER_SPEED; //Скорость установки самоклеющегося уплотнителя, сек/м
    public static double INSULATION_SPEED; //Скорость разметки, резки и укладки утеплителя, мин/м2
    public static double SCOTCH_SPEED; //Скорость оклеивания металлизированного скотча, мин/м

    //--- УПАКОВКА
    public static double CARTOON_BOX_PREPARED_TIME; //ПЗ время изготовления коробок, мин
    public static double CARTOON_BOX_SPEED; //Время изготовления коробки, мин
    public static double STRETCH_MACHINE_WINDING; //Время наматывания машинной стретч-пленки, мин
    public static double CARTOON_BOX_AND_ANGLES_SPEED; //Время изготовления крышек и уголков, мин
    public static double PACK_IN_CARTOON_BOX_SPEED; //Время упаковки изделя в коробку, мин
    public static double DUCT_TAPE_LENGTH; //Длина рулона скотча, м
    public static double BUBBLE_CUT_AND_DUCT; //ПЗ время пузырьковой пленки, мин
    public static double BUBBLE_HAND_WINDING; //Скорость оборачивания пузырьковой пленки, мин/м.кв.
    public static double STRETCH_HAND_WINDING; //Скорость оборачивания стретч пленки, мин/м

    //---ЭЛЕКТРИКА
    public static double MOUNT_ON_DIN_AUTOMATS = 0.3; //Скорость установки на динрейку автоматов, мин
    public static double MOUNT_ON_DIN_HEATERS = 0.5; //Скорость установки на динрейку нагревателей, мин
    public static double MOUNT_ON_2_SCREWS_NO_DISASSM = 1.2; //Скорость установки на 2 винта без разборки, мин
    public static double MOUNT_ON_4_SCREWS_NO_DISASSM = 2.0; //Скорость установки на 4 винта без разборки, мин

    public static double MOUNT_ON_2_SCREWS_WITH_DISASSM = 3.0; //Скорость установки на 2 винта с разборкой, мин
    public static double MOUNT_ON_4_SCREWS_WITH_DISASSM = 5.0; //Скорость установки на 4 винта с разборкой, мин

    public static double MOUNT_ON_VSHG = 2.5; //Скорость установки на ВШГ (4 шт), мин

    public static double CONNECTING_DEVICES_WITH_MORTISE_CONTACT = 0.12; //Подключение устр. на врезной контакт, мин/контакт
    public static double CONNECTING_DEVICES_WITH_SPRING_CLAMP = 0.08; //Подключение устр. на пружинный зажим, мин/контакт
    public static double CONNECTING_DEVICES_WITH_CLAMPING_SCREW = 0.3; //Подключение устр. на зажимной винт, мин/контакт
    public static double CONNECTING_DEVICES_WITH_VSHG = 0.5; //Подключение устр. на ВШГ, мин/контакт

    public static double CUTTING_MULTI_CORE_CABLE_6MM = 0.25; //Резка многожильного кабеля Дн = 6мм, мин/рез
    public static double CUTTING_MULTI_CORE_CABLE_11MM = 0.4; //Резка многожильного кабеля Дн = 11..15мм, мин/рез
    public static double CUTTING_SINGLE_CORE_CABLE = 0.15; //Резка одножильного кабеля, мин/рез

    public static double CUTTING_CABLE_ON_MACHINE = 0.25; //Резка кабеля на автомате, мин/рез
    public static double CUTTING_METAL_SLEEVE = 0.2; //Резка металлорукава, мин/рез
    public static double CUTTING_CABLE_CHANNEL = 0.15; //Резка кабель-канала, дин-рейки, мин/рез

    public static double TINNING_IN_BATHE = 0.071; //Лужение в ванночке, мин/наконечник
    public static double TINNING = 0.074; //Лужение электропаяльником, мин/наконечник

    public static double MOUNT_TIP_ON_CABLE = 0.18; //Оконцовка провода наконечником, мин/наконечник
    public static double MOUNT_TIP_ON_POWER_CABLE = 0.7; //Оконцовка силового кабеля наконечником, мин/наконечник





    public static double MARKING_SPEED = 0.3; //Скорость маркировки, мин/элемент

    public static double SOLDERING_SPEED = 0.2; //Скорость пайки, мин/элемент

    public static double MOUNT_OF_SIGNAL_EQUIP_SPEED = 1.3; //Скорость установки сигнальной аппаратуры, мин/элемент

    public static double MOUNT_OF_CABLE_ENTRIES = 0.15; //Скорость установки кабельных вводов, мин/элемент

    public static double FIX_OF_CABLES_SPEED = 1.5; //Скорость укладки жгутов, мин/элемент (каждые 0,3 м)


    private NormConstants() {

        constantsFile = new File(appConstantsPath);

        if (!constantsFile.exists()){
            downloadDefaultConstants();
        } else {
            createConstantsProps();
        }

    }

    /**
     * Создание потока
     */
    public void createConstantsProps() {
        log.debug("createConstantsProps : Properties  constantsProps создается  ...");
        try {
            constantsProps = new Properties();
            constantsProps.load(new FileInputStream(appConstantsPath));
        } catch (IOException ex) {
            Warning1.create(null, "Ошибка!",
                    "Не удалось загрузить настройки доступа к серверу",
                    "Возможно, файл настроек поврежден");
            ex.printStackTrace();
        }

        log.debug("createConstantsProps : Properties  constantsProps  успешно создан");

        loadConstantsFromPropertiesFile();
    }

    /**
     * Загрузка констант в оперативную память
     */
    public void loadConstantsFromPropertiesFile(){

        //--- РЕЗКА И ЗАЧИСТКА
        CUTTING_SPEED = Double.parseDouble(constantsProps.getProperty("CUTTING_SPEED"));
        REVOLVER_SPEED = Double.parseDouble(constantsProps.getProperty("REVOLVER_SPEED"));
        PERFORATION_SPEED = Double.parseDouble(constantsProps.getProperty("PERFORATION_SPEED"));
        CUTTING_SERVICE_RATIO = Double.parseDouble(constantsProps.getProperty("CUTTING_SERVICE_RATIO"));
        STRIPING_SPEED = Double.parseDouble(constantsProps.getProperty("STRIPING_SPEED"));

        //--- ГИБКА
        UNIVERSAL_BENDING_SPEED = Double.parseDouble(constantsProps.getProperty("BENDING_SPEED"));
        BENDING_SERVICE_RATIO = Double.parseDouble(constantsProps.getProperty("BENDING_SERVICE_RATIO"));

        //--- СЛЕСАРНЫЕ РАБОТЫ
        CHOP_SPEED = Double.parseDouble(constantsProps.getProperty("CHOP_SPEED"));
        RIVETS_SPEED = Double.parseDouble(constantsProps.getProperty("RIVETS_SPEED"));
        COUNTERSINKING_SPEED = Double.parseDouble(constantsProps.getProperty("COUNTERSINKING_SPEED"));
        THREADING_SPEED = Double.parseDouble(constantsProps.getProperty("THREADING_SPEED"));
        SMALL_SAWING_SPEED = Double.parseDouble(constantsProps.getProperty("SMALL_SAWING_SPEED"));
        BIG_SAWING_SPEED = Double.parseDouble(constantsProps.getProperty("BIG_SAWING_SPEED"));

        //--- ПОКРАСКА ДЕТАЛИ
        DETAIL_DELTA = Integer.parseInt(constantsProps.getProperty("DETAIL_DELTA"));
        WASHING = Double.parseDouble(constantsProps.getProperty("WASHING"));
        WINDING = Double.parseDouble(constantsProps.getProperty("WINDING"));
        DRYING = Double.parseDouble(constantsProps.getProperty("DRYING"));
        BAKING = Double.parseDouble(constantsProps.getProperty("BAKING"));

        //--- ПОКРАСКА СБОРКИ
        ASSM_DELTA = Integer.parseInt(constantsProps.getProperty("ASSM_DELTA"));
        HANGING_TIME = Double.parseDouble(constantsProps.getProperty("HANGING_TIME"));
        WINDING_MOVING_SPEED = Double.parseDouble(constantsProps.getProperty("WINDING_MOVING_SPEED"));
        SOLID_BOX_SPEED = Double.parseDouble(constantsProps.getProperty("SOLID_BOX_SPEED"));
        FRAME_SPEED = Double.parseDouble(constantsProps.getProperty("FRAME_SPEED"));

        //--- СВАРКА НЕПРЕРЫВНАЯ
        WELDING_SPEED = Double.parseDouble(constantsProps.getProperty("WELDING_SPEED"));

        //--- СВАРКА ТОЧЕЧНАЯ
        WELDING_PARTS_SPEED = Double.parseDouble(constantsProps.getProperty("WELDING_PARTS_SPEED"));
        WELDING_DOTTED_SPEED = Double.parseDouble(constantsProps.getProperty("WELDING_DOTTED_SPEED"));
        WELDING_DROP_SPEED = Double.parseDouble(constantsProps.getProperty("WELDING_DROP_SPEED"));

        //--- ЗАЛИВКА УПЛОТНИТЕЛЯ
        LEVELING_PREPARED_TIME = Double.parseDouble(constantsProps.getProperty("LEVELING_PREPARED_TIME"));
        LEVELING_SPEED = Double.parseDouble(constantsProps.getProperty("LEVELING_SPEED"));


        //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ
        SCREWS_SPEED = Double.parseDouble(constantsProps.getProperty("SCREWS_SPEED"));
        VSHGS_SPEED = Double.parseDouble(constantsProps.getProperty("VSHGS_SPEED"));
        RIVET_NUTS_SPEED = Double.parseDouble(constantsProps.getProperty("RIVET_NUTS_SPEED"));
        GROUND_SETS_SPEED = Double.parseDouble(constantsProps.getProperty("GROUND_SETS_SPEED"));
        OTHERS_SPEED = Double.parseDouble(constantsProps.getProperty("OTHERS_SPEED"));

        //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
        POST_LOCKS_SPEED = Double.parseDouble(constantsProps.getProperty("POST_LOCKS_SPEED"));
        DOUBLE_LOCKS_SPEED = Double.parseDouble(constantsProps.getProperty("DOUBLE_LOCKS_SPEED"));
        GLASS_SPEED = Double.parseDouble(constantsProps.getProperty("GLASS_SPEED"));
        DETECTORS_SPEED = Double.parseDouble(constantsProps.getProperty("DETECTORS_SPEED"));
        CONNECTION_BOXES_SPEED = Double.parseDouble(constantsProps.getProperty("CONNECTION_BOXES_SPEED"));

        //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
        SEALER_SPEED = Double.parseDouble(constantsProps.getProperty("SEALER_SPEED"));
        SELF_ADH_SEALER_SPEED = Double.parseDouble(constantsProps.getProperty("SELF_ADH_SEALER_SPEED"));
        INSULATION_SPEED = Double.parseDouble(constantsProps.getProperty("INSULATION_SPEED"));
        SCOTCH_SPEED = Double.parseDouble(constantsProps.getProperty("SCOTCH_SPEED"));

        //--- УПАКОВКА
        CARTOON_BOX_PREPARED_TIME = Double.parseDouble(constantsProps.getProperty("CARTOON_BOX_PREPARED_TIME"));
        CARTOON_BOX_SPEED = Double.parseDouble(constantsProps.getProperty("CARTOON_BOX_SPEED"));
        STRETCH_MACHINE_WINDING = Double.parseDouble(constantsProps.getProperty("STRETCH_MACHINE_WINDING"));
        CARTOON_BOX_AND_ANGLES_SPEED = Double.parseDouble(constantsProps.getProperty("CARTOON_BOX_AND_ANGLES_SPEED"));
        PACK_IN_CARTOON_BOX_SPEED = Double.parseDouble(constantsProps.getProperty("PACK_IN_CARTOON_BOX_SPEED"));
        DUCT_TAPE_LENGTH = Double.parseDouble(constantsProps.getProperty("DUCT_TAPE_LENGTH"));
        BUBBLE_CUT_AND_DUCT = Double.parseDouble(constantsProps.getProperty("BUBBLE_CUT_AND_DUCT"));
        BUBBLE_HAND_WINDING = Double.parseDouble(constantsProps.getProperty("BUBBLE_HAND_WINDING"));
        STRETCH_HAND_WINDING = Double.parseDouble(constantsProps.getProperty("STRETCH_HAND_WINDING"));
        
    }

    public boolean writeConstant(String name, TextField tf){
        try {
            double val = Double.parseDouble(tf.getText().trim().replace(",", "."));
            FileOutputStream fos = new FileOutputStream(appConstantsPath);
            constantsProps.setProperty(name, DECIMAL_FORMAT.format(val).replace(",", "."));
            constantsProps.store(fos, null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void copyConstantsFileToDB(){
        try {
            FilesService.getInstance().upload("def-constants.properties", "normic", constantsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * После загрузки файла автоматически срабатывают методы createConstantsProps() и loadConstantsFromPropertiesFile()
     */
    public void downloadDefaultConstants(){
        Service<Void> task = new DownloadConstantsFileTask(this, "def-constants");
        task.setOnSucceeded(event -> {
            createConstantsProps();
        });
        task.start();
    }

    /**
     * После загрузки файла автоматически срабатывают методы createConstantsProps() и loadConstantsFromPropertiesFile()
     */
    public void downloadInitConstants(ConstantsController constantsController){
        Service<Void> task = new DownloadConstantsFileTask(this, "init-constants");
        task.setOnSucceeded(event -> {
            createConstantsProps();
            loadConstantsFromPropertiesFile();
            constantsController.fillTextFieldsWithData();
        });
        task.start();
    }

}
