package ru.wert.normic.entities.settings;

import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;

public class AppSettings {

    //--- РЕЗКА И ЗАЧИСТКА

    public static double CUTTING_SPEED = 1.28; //Скорость резания, зависящая от площади детали, мин
    public static double REVOLVER_SPEED = 0.057; //Скорость вырубки одного элемента револьвером, мин/уд
    public static double PERFORATION_SPEED = 0.007; //Скорость перфорирования, мин/уд
    public static double CUTTING_SERVICE_RATIO = 1.22; //коэфффициент, учитывающий 22% времени на обслуживание при резке
    public static double STRIPING_SPEED = 2.5; //Скорость зачистки резанных кромок, сек

    //--- ГИБКА

    public static double BENDING_SPEED = 0.15; //Скорость гибки, мин/гиб
    public static double BENDING_SERVICE_RATIO = 1.25; //коэфффициент, учитывающий 25% времени на обслуживание при гибке

    //--- СЛЕСАРНЫЕ РАБОТЫ

    public static double CHOP_SPEED = 0.05; //Скорость рубки на Геке, мин/удар
    public static double RIVETS_SPEED = 18; //Скорость установки вытяжной заклепки, сек/закл
    public static double COUNTERSINKING_SPEED = 0.31; //Скорость сверления и зенковки, мин/отв
    public static double THREADING_SPEED = 0.37; //Скорость нарезания резьбы, мин/отв
    public static double SMALL_SAWING_SPEED = 0.2; //Скорость пиления на малой пиле, мин/рез
    public static double BIG_SAWING_SPEED = 1.0; //Скорость пиления на большой пиле, мин/рез

    //--- ПОКРАСКА ДЕТАЛИ

    public static int DETAIL_DELTA = 100; //Расстояние между деталями, мм
    public static double WASHING = 12; //Мойка, сек
    public static double WINDING = 6; //Продувка, сек
    public static double DRYING = 20; //Сушка, сек
    public static double BAKING = 40.0; //Полимеризация, мин

    //--- ПОКРАСКА СБОРКИ

    public static int ASSM_DELTA = 300; //Расстояние между сборками, мм
    public static double HANGING_TIME = 0.34; //Время навески и снятия после полимеризации, мин
    public static double WINDING_MOVING_SPEED = 1.4; //Продувка после промывки и перемещение изделя на штанге, мин/1 м.кв.
    public static double SOLID_BOX_SPEED = 1.686; //Скорость окрашивания глухих шкафов, мин/1 м.кв.
    public static double FRAME_SPEED = 2.4; //Скорость окрашивания открытых рам и кроссов, мин/1 м.кв.

    //--- СВАРКА НЕПРЕРЫВНАЯ

    public static double WELDING_SPEED = 4.0; //Скорость сваркм, мин/м

    //--- СВАРКА ТОЧЕЧНАЯ

    public static double WELDING_PARTS_SPEED = 0.13; //Скорость онденсаторной сварки точкой, мин/элемент
    public static double WELDING_DOTTED_SPEED = 0.3; //Скорость контактной сварки, мин/точку
    public static double WELDING_DROP_SPEED = 0.07; //Скорость сварки прихватками, мин/прихватку

    //--- ЗАЛИВКА УПЛОТНИТЕЛЯ

    public static double LEVELING_PREPARED_TIME = 0.32; //ПЗ время, мин
    public static double LEVELING_SPEED = 0.16; //Скорость нанесения, м/мин


    //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ

    public static double SCREWS_SPEED = 0.25; //Скорость установки винтов, мин
    public static double VSHGS_SPEED = 0.4; //Скорость установки комплектов ВШГ, мин
    public static double RIVET_NUTS_SPEED = 22; //Скорость установки заклепочных гаек, сек
    public static double GROUND_SETS_SPEED = 1.0; //Скорость установки комплекта заземления с этикеткой, мин
    public static double OTHERS_SPEED = 15; //Скорость установки другого крепежа, сек

    //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ

    public static double POST_LOCKS_SPEED = 1.5; //Скорость установки почтового замка с регулировкой, мин
    public static double DOUBLE_LOCKS_SPEED = 3.0; //Скорость установки замка с рычагами, мин
    public static double GLASS_SPEED = 2.0; //Скорость установки стекла на полиуретан, мин
    public static double DETECTORS_SPEED = 2.0; //Скорость установки извещателей (ИО-102), мин
    public static double CONNECTION_BOXES_SPEED = 3.0; //Скорость установки коробки соединительной (КС-4), мин

    //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ

    public static double SEALER_SPEED = 40; //Скорость монтажа уплотнителя на ребро корпуса, сек/м
    public static double SELF_ADH_SEALER_SPEED =  20; //Скорость установки самоклеющегося уплотнителя, сек/м
    public static double INSULATION_SPEED = 5.5; //Скорость разметки, резки и укладки утеплителя, мин/м2

    //--- УПАКОВКА

    public static double CARTOON_BOX_PREPARED_TIME = 10.0; //ПЗ время изготовления коробок, мин
    public static double CARTOON_BOX_SPEED = 2.3; //Скорость изготовления коробки
    public static double STRETCH_MACHINE_WINDING = 2.1; //Скорость наматывания машинной стретч-пленки
    public static double CARTOON_AND_STRETCH_PREPARED_TIME = 12.0; //ПЗ время упаковки изделия в картон и обмотки стречем, мин
    public static double PACK_IN_CARTOON_BOX_SPEED = 1.5; //Скорость упаковки изделя в коробку
    public static double DUCT_TAPE_LENGTH = 66.0; //Длина рулона скотча
    public static double BUBBLE_CUT_AND_DUCT = 0.7; //ПЗ время пузырьковой пленки, мин
    public static double BUBBLE_HAND_WINDING = 0.2; //Скорость оборачивания пузырьковой пленки, мин/м.кв.
    public static double STRETCH_HAND_WINDING = 0.2; //Скорость оборачивания пузырьковой пленки, мин/м

}
