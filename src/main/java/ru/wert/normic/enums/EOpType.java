package ru.wert.normic.enums;

public enum EOpType {

    //ОПЕРАЦИИ С ЛИСТОМ
    CUTTING,                //ВЫРЕЗАНИЕ ЛИСТОВОЙ ДЕТАЛИ
    BENDING,                //ГИБКА ЛИСТОВОЙ ДЕТАЛИ

    //СБОРОЧНЫЕ ОПЕРАЦИИ
    DETAIL,                 //ДЕТАЛЬ
    ASSM,                   //СБОРКА
    ASSM_CUTTINGS,          //СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    ASSM_NODES,             //СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    ASSM_NUTS,              //СБОРКА КРЕПЕЖА
    LEVELING_SEALER,        //НАЛИВКА УПЛОТНИТЕЛЯ

    //ОКРАШИВАНИЕ
    PAINTING,               //ОКРАШИВАНИЕ ДЕТАЛИ
    PAINT_ASSM,             //ОКРАШИВАНИЕ СБОРКИ

    //СЛЕСАРНЫЕ ОПЕРАЦИИ
    LOCKSMITH,              //СЛЕАРНЫЕ РАБОТЫ
    CHOP_OFF,               //ОТРУБКА ЗАГОТОВКИ НА ГЕКЕ
    DRILLING_BY_MARKING,    //СВЕРЛЕНИЕ ПО РАЗМЕТКЕ
    CUT_OFF_ON_SAW,         //ОТРЕЗАНИЕ НА ПИЛЕ

    //ТОКАРНЫЕ ОПЕРАЦИИ
    LATHE_MOUNT_DISMOUNT,   //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    LATHE_TURNING,          //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    LATHE_CUT_GROOVE,       //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    LATHE_THREADING,        //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    LATHE_DRILLING,         //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    LATHE_ROLLING,          //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    LATHE_CUT_OFF,          //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS,        //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED,            //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ

    //УПАКОВКА
    PACK,                   //УПАКОВКА
    PACK_ON_PALLET,         //УПАКОВКА НА ПАЛЛЕТ (Установка и закрепление)
    PACK_IN_MACHINE_STRETCH_WRAP,     //УПАКОВКА В КАРТОН (КРЫШКИ И УГОЛКИ)
    PACK_IN_HAND_STRETCH_WRAP,     //УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    PACK_IN_CARTOON_BOX,    //УПАКОВКА В КАРТОННУЮ КОРОБКУ
    PACK_IN_BUBBLE_WRAP,    //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ

    ERROR_OP_DATA



    ;
}
