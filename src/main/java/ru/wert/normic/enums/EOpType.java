package ru.wert.normic.enums;

import javafx.scene.image.Image;
import lombok.Getter;

import java.util.NoSuchElementException;

public enum EOpType {


    //ОТДЕЛЬНЫЕ ЭЛЕМЕНТЫ
    DETAIL                      ("Деталь",                          new Image("/pics/opLogos/detail.png")),        //ДЕТАЛЬ
    ASSM                        ("Сборка",                          new Image("/pics/opLogos/assemble.png")),      //СБОРКА
    PACK                        ("Упаковка",                        new Image("/pics/opLogos/pack.png")),          //УПАКОВКА

    //ОПЕРАЦИИ С ЛИСТОМ
    CUTTING                     ("Резка и зачистка",                new Image("/pics/opLogos/list_cutting.png")),   //ВЫРЕЗАНИЕ ЛИСТОВОЙ ДЕТАЛИ
    BENDING                     ("Гибка",                           new Image("/pics/opLogos/bend.png")),           //ГИБКА ЛИСТОВОЙ ДЕТАЛИ

    //ОКРАШИВАНИЕ
    PAINTING                    ("Покраска детали",                 new Image("/pics/opLogos/paint.png")),         //ОКРАШИВАНИЕ ДЕТАЛИ
    PAINT_ASSM                  ("Покраска сборки",                 new Image("/pics/opLogos/paint.png")),         //ОКРАШИВАНИЕ СБОРКИ

    //СБОРОЧНЫЕ ОПЕРАЦИИ
    ASSM_CUTTINGS               ("Сборка раскройного материала",    new Image("/pics/opLogos/assm_cutting.png")),  //СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    ASSM_NODES                  ("Сборка стандартных узлов",        new Image("/pics/opLogos/cutting.png")),       //СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    ASSM_NUTS                   ("Сборка крепежа",                  new Image("/pics/opLogos/nuts.png")),          //СБОРКА КРЕПЕЖА
    LEVELING_SEALER             ("Наливной уплотнитель",            new Image("/pics/opLogos/sealer.png")),        //НАЛИВКА УПЛОТНИТЕЛЯ

    //СЛЕСАРНЫЕ ОПЕРАЦИИ
    LOCKSMITH                   ("Слесарные операции",              new Image("/pics/opLogos/locksmith.png")),     //СЛЕАРНЫЕ РАБОТЫ
    ASSM_NUTS_MK                ("Крепеж (Учаток МК)",              new Image("/pics/opLogos/nuts.png")),          //КРЕПЕЖ (УЧАСТОК МК)
    CHOP_OFF                    ("Рубка в размер",                  new Image("/pics/opLogos/guillotine.png")),    //ОТРУБКА ЗАГОТОВКИ НА ГЕКЕ
    DRILLING_BY_MARKING         ("Сверление по разметке",           new Image("/pics/opLogos/drill.png")),         //СВЕРЛЕНИЕ ПО РАЗМЕТКЕ
    CUT_OFF_ON_SAW              ("Отрезание на пиле",               new Image("/pics/opLogos/saw.png")),           //ОТРЕЗАНИЕ НА ПИЛЕ

    //ТОКАРНЫЕ ОПЕРАЦИИ
    LATHE_MOUNT_DISMOUNT        ("Установка и снятие детали",       new Image("/pics/opLogos/lathe.png")),         //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    LATHE_TURNING               ("Точение и растачивание",          new Image("/pics/opLogos/lathe.png")),         //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    LATHE_CUT_GROOVE            ("Точение канавки",                 new Image("/pics/opLogos/lathe.png")),         //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    LATHE_THREADING             ("Нарезание резьбы",                new Image("/pics/opLogos/threading.png")),     //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    LATHE_DRILLING              ("Сверление",                       new Image("/pics/opLogos/lathe_drill.png")),   //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    LATHE_ROLLING               ("Накатывание рифления",            new Image("/pics/opLogos/lathe.png")),         //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    LATHE_CUT_OFF               ("Отрезание",                       new Image("/pics/opLogos/lathe.png")),         //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS             ("Сварка непрерывная",              new Image("/pics/opLogos/weld.png")),          //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED                 ("Сварка точечная",                 new Image("/pics/opLogos/weld.png")),          //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ

    //УПАКОВКА
    PACK_ON_PALLET              ("Монтаж на поддон",                new Image("/pics/opLogos/pallet.png")),        //УПАКОВКА НА ПАЛЛЕТ (Установка и закрепление)
    PACK_IN_MACHINE_STRETCH_WRAP("Упаковка в машинную стрейч-пленку",new Image("/pics/opLogos/wrap.png")),          //УПАКОВКА В КАРТОН (КРЫШКИ И УГОЛКИ)
    PACK_IN_HAND_STRETCH_WRAP   ("Упаковка в ручную стрейч-пленку", new Image("/pics/opLogos/wrap.png")),          //УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    PACK_IN_CARTOON_BOX         ("Упаковка в картонную коробку",    new Image("/pics/opLogos/cartoon.png")),       //УПАКОВКА В КАРТОННУЮ КОРОБКУ
    PACK_IN_BUBBLE_WRAP         ("Упаковка в пузырьковую пленку",   new Image("/pics/opLogos/wrap.png")),          //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ

    ERROR_OP_DATA               ("Error",                           new Image("/pics/opLogos/error.png"));

    @Getter
    String opName;
    @Getter
    Image logo;

    EOpType(String opName, Image logo) {
        this.opName = opName;
        this.logo = logo;
    }

    public static EOpType findOpTypeByName(String name){
        for(EOpType op : EOpType.values()){
            if(op.name().equals(name))
                   return op;
        }
        throw new NoSuchElementException("No such type in EOpType found!");
    }
}
