package ru.wert.normic.enums;

import javafx.scene.image.Image;
import lombok.Getter;

public enum EOpType {

    //ОПЕРАЦИИ С ЛИСТОМ
    CUTTING                     (new Image("/pics/opLogos/list_cutting.png")),   //ВЫРЕЗАНИЕ ЛИСТОВОЙ ДЕТАЛИ
    BENDING(new Image           ("/pics/opLogos/bend.png")),                    //ГИБКА ЛИСТОВОЙ ДЕТАЛИ

    //СБОРОЧНЫЕ ОПЕРАЦИИ
    DETAIL                      (new Image("/pics/opLogos/detail.png")),        //ДЕТАЛЬ
    ASSM                        (new Image("/pics/opLogos/assemble.png")),      //СБОРКА
    ASSM_CUTTINGS               (new Image("/pics/opLogos/assm_cutting.png")),  //СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    ASSM_NODES                  (new Image("/pics/opLogos/cutting.png")),       //СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    ASSM_NUTS                   (new Image("/pics/opLogos/nuts.png")),          //СБОРКА КРЕПЕЖА
    LEVELING_SEALER             (new Image("/pics/opLogos/sealer.png")),        //НАЛИВКА УПЛОТНИТЕЛЯ

    //ОКРАШИВАНИЕ
    PAINTING                    (new Image("/pics/opLogos/paint.png")),         //ОКРАШИВАНИЕ ДЕТАЛИ
    PAINT_ASSM                  (new Image("/pics/opLogos/paint.png")),         //ОКРАШИВАНИЕ СБОРКИ

    //СЛЕСАРНЫЕ ОПЕРАЦИИ
    LOCKSMITH                   (new Image("/pics/opLogos/locksmith.png")),     //СЛЕАРНЫЕ РАБОТЫ
    ASSM_NUTS_MK                (new Image("/pics/opLogos/nuts.png")),          //КРЕПЕЖ (УЧАСТОК МК)
    CHOP_OFF                    (new Image("/pics/opLogos/guillotine.png")),    //ОТРУБКА ЗАГОТОВКИ НА ГЕКЕ
    DRILLING_BY_MARKING         (new Image("/pics/opLogos/drill.png")),         //СВЕРЛЕНИЕ ПО РАЗМЕТКЕ
    CUT_OFF_ON_SAW              (new Image("/pics/opLogos/saw.png")),           //ОТРЕЗАНИЕ НА ПИЛЕ

    //ТОКАРНЫЕ ОПЕРАЦИИ
    LATHE_MOUNT_DISMOUNT        (new Image("/pics/opLogos/lathe.png")),         //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    LATHE_TURNING               (new Image("/pics/opLogos/lathe.png")),         //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    LATHE_CUT_GROOVE            (new Image("/pics/opLogos/lathe.png")),         //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    LATHE_THREADING             (new Image("/pics/opLogos/threading.png")),     //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    LATHE_DRILLING              (new Image("/pics/opLogos/lathe_drill.png")),   //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    LATHE_ROLLING               (new Image("/pics/opLogos/lathe.png")),         //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    LATHE_CUT_OFF               (new Image("/pics/opLogos/lathe.png")),         //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS             (new Image("/pics/opLogos/weld.png")),          //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED                 (new Image("/pics/opLogos/weld.png")),          //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ

    //УПАКОВКА
    PACK                        (new Image("/pics/opLogos/pack.png")),          //УПАКОВКА
    PACK_ON_PALLET              (new Image("/pics/opLogos/pallet.png")),        //УПАКОВКА НА ПАЛЛЕТ (Установка и закрепление)
    PACK_IN_MACHINE_STRETCH_WRAP(new Image("/pics/opLogos/wrap.png")),          //УПАКОВКА В КАРТОН (КРЫШКИ И УГОЛКИ)
    PACK_IN_HAND_STRETCH_WRAP   (new Image("/pics/opLogos/wrap.png")),          //УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    PACK_IN_CARTOON_BOX         (new Image("/pics/opLogos/cartoon.png")),       //УПАКОВКА В КАРТОННУЮ КОРОБКУ
    PACK_IN_BUBBLE_WRAP         (new Image("/pics/opLogos/wrap.png")),          //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ

    ERROR_OP_DATA               (new Image("/pics/opLogos/cutting.png"));

    @Getter
    Image logo;

    EOpType(Image logo) {
        this.logo = logo;
    }

    ;
}
