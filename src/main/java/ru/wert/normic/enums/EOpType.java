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
    MOUNT_DISMOUNT,         //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    TURNING,                //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    CUT_GROOVE,             //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    THREADING,              //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    DRILLING,               //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    ROLLING,                //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    CUT_OFF,                //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS,        //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED,            //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ

    //УПАКОВКА
    PACK_FRAME              //УПАКОВКА КАРКАСА

    ;
}
