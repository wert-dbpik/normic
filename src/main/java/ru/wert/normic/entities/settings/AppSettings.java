package ru.wert.normic.entities.settings;

import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;

public class AppSettings {

    //--- РЕЗКА И ЗАЧИСТКА

    public static double REVOLVER_SPEED = 0.057; //скорость вырубки одного элемента револьвером, мин/уд
    public static double PERFORATION_SPEED = 0.007; //корость перфорирования, мин/уд
    public static double CUTTING_SERVICE_RATIO = 1.22; //коэфффициент, учитывающий 22% времени на обслуживание при резке

    //--- ГИБКА

    public static double BENDING_SERVICE_RATIO = 1.25; //коэфффициент, учитывающий 25% времени на обслуживание при гибке
    public static double BENDING_SPEED = 0.15; //корость гибки, мин/гиб

    //--- СЛЕСАРНЫЕ РАБОТЫ

    public static double RIVETS_SPEED = 18 * SEC_TO_MIN; //скорость установки вытяжной заклепки
    public static double COUNTERSINKING_SPEED = 0.31; //скорость сверления и зенковки
    public static double THREADING_SPEED = 0.37; //скорость нарезания резьбы
    public static double SMALL_SAWING_SPEED = 0.2; //скорость пиления на малой пиле
    public static double BIG_SAWING_SPEED = 1.0; //скорость пиления на большой пиле

    //--- ПОКРАСКА ДЕТАЛИ

    public static int DETAIL_DELTA = 100; //расстояние между деталями
    public static double WASHING = 12/60.0; //мойка, мин
    public static double WINDING = 6/60.0; //продувка, мин
    public static double DRYING = 20/60.0; //сушка, мин

    //--- ПОКРАСКА СБОРКИ

    public static int ASSM_DELTA = 300; //расстояние между сборками
    public static double HANGING_TIME = 0.34; //ремя навески и снятия после полимеризации
    public static double WINDING_MOVING_SPEED = 1.4; //продувка после промывки и перемещение изделя на штанге, мин/1 м.кв.

    //--- СВАРКА НЕПРЕРЫВНАЯ

    public static double WELDING_SPEED = 4.0; //скорость сваркм, мин/гиб

    //--- СВАРКА ТОЧЕЧНАЯ

    public static double WELDING_PARTS_SPEED = 0.13; //скорость онденсаторной сварки точкой, мин/элемент
    public static double WELDING_DOTTED_SPEED = 0.3; //скорость контактной сварки, мин/точку
    public static double WELDING_DROP_SPEED = 0.07; //скорость сварки прихватками, мин/прихватку

    //--- ЗАЛИВКА УПЛОТНИТЕЛЯ

    public static double PREPARED_TIME = 0.32; //ПЗ время, мин
    public static double LEVELING_SPEED = 0.16; //скорость нанесения, м/мин

    //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ

    public static double POST_LOCKS_SPEED = 0.25; //скорость установки вытяжных винтов
    public static double DOUBLE_LOCKS_SPEED = 0.4; //скорость установки комплектов ВШГ
    public static double MIRRORS_SPEED = 18 * SEC_TO_MIN; //скорость установки заклепок
    public static double DETECTORS_SPEED = 22 * SEC_TO_MIN; //скорость установки заклепочных гаек
    public static double CONNECTION_BOXES_SPEED = 1.0; //скорость установки комплекта заземления с этикеткой

    //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ

    public static double SCREWS_SPEED = 0.25; //скорость установки вытяжных винтов
    public static double VSHGS_SPEED = 0.4; //скорость установки комплектов ВШГ
    public static double RIVET_NUTS_SPEED = 22 * SEC_TO_MIN; //скорость установки заклепочных гаек
    public static double GROUND_SETS_SPEED = 1.0; //скорость установки комплекта заземления с этикеткой
    public static double OTHERS_SPEED = 15 * SEC_TO_MIN; //скорость установки другого крепежа

    //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ

    public static double SEALER_SPEED = 40 * SEC_TO_MIN; //скорость монтажа уплотнителя
    public static double SELF_ADH_SEALER_SPEED =  20 * SEC_TO_MIN; //скорость наклейки уплотнителя
    public static double INSULATION_SPEED = 5.5; //скорость разметки, резки и укладки уплотнителя

}
