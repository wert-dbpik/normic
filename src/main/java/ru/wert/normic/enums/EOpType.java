package ru.wert.normic.enums;

import javafx.scene.image.Image;
import lombok.Getter;
import ru.wert.normic.controllers.assembling.countings.OpAssmCattingCounter;
import ru.wert.normic.controllers.assembling.countings.OpAssmNodeCounter;
import ru.wert.normic.controllers.assembling.countings.OpAssmNutsCounter;
import ru.wert.normic.controllers.assembling.countings.OpLevelingSealerCounter;
import ru.wert.normic.controllers.listOperations.counters.OpBendingCounter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.locksmith.counters.*;
import ru.wert.normic.controllers.packing.counters.*;
import ru.wert.normic.controllers.paint.counters.OpPaintAssmCounter;
import ru.wert.normic.controllers.paint.counters.OpPaintCounter;
import ru.wert.normic.controllers.turning.counters.*;
import ru.wert.normic.controllers.welding.counters.OpWeldContinuousCounter;
import ru.wert.normic.controllers.welding.counters.OpWeldDottedCounter;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

public enum EOpType {




    //ОТДЕЛЬНЫЕ ЭЛЕМЕНТЫ
    DETAIL                      ("Деталь",                          new Image("/pics/opLogos/detail.png"),null),                            //ДЕТАЛЬ
    ASSM                        ("Сборка",                          new Image("/pics/opLogos/assemble.png"),null),                          //СБОРКА
    PACK                        ("Упаковка",                        new Image("/pics/opLogos/pack.png"), null),                             //УПАКОВКА

    //ОПЕРАЦИИ С ЛИСТОМ
    CUTTING                     ("Резка и зачистка",                new Image("/pics/opLogos/list_cutting.png"),    new OpCuttingCounter()),           //ВЫРЕЗАНИЕ ЛИСТОВОЙ ДЕТАЛИ
    BENDING                     ("Гибка",                           new Image("/pics/opLogos/bend.png"),            new OpBendingCounter()),           //ГИБКА ЛИСТОВОЙ ДЕТАЛИ

    //ОКРАШИВАНИЕ
    PAINTING                    ("Покраска детали",                 new Image("/pics/opLogos/paint.png"),           new OpPaintCounter()),              //ОКРАШИВАНИЕ ДЕТАЛИ
    PAINT_ASSM                  ("Покраска сборки",                 new Image("/pics/opLogos/paint.png"),           new OpPaintAssmCounter()),          //ОКРАШИВАНИЕ СБОРКИ

    //СБОРОЧНЫЕ ОПЕРАЦИИ
    ASSM_CUTTINGS               ("Сборка раскройного материала",    new Image("/pics/opLogos/assm_cutting.png"),    new OpAssmCattingCounter()),        //СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    ASSM_NODES                  ("Сборка стандартных узлов",        new Image("/pics/opLogos/cutting.png"),         new OpAssmNodeCounter()),           //СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    ASSM_NUTS                   ("Сборка крепежа",                  new Image("/pics/opLogos/nuts.png"),            new OpAssmNutsCounter()),           //СБОРКА КРЕПЕЖА
    LEVELING_SEALER             ("Наливной уплотнитель",            new Image("/pics/opLogos/sealer.png"),          new OpLevelingSealerCounter()),     //НАЛИВКА УПЛОТНИТЕЛЯ

    //СЛЕСАРНЫЕ ОПЕРАЦИИ
    LOCKSMITH                   ("Слесарные операции",              new Image("/pics/opLogos/locksmith.png"),       new OpLocksmithCounter()),          //СЛЕАРНЫЕ РАБОТЫ
    ASSM_NUTS_MK                ("Крепеж (Учаток МК)",              new Image("/pics/opLogos/nuts.png"),            new OpAssmNutsMKCounter()),         //КРЕПЕЖ (УЧАСТОК МК)
    CHOP_OFF                    ("Рубка в размер",                  new Image("/pics/opLogos/guillotine.png"),      new OpChopOffCounter()),            //ОТРУБКА ЗАГОТОВКИ НА ГЕКЕ
    DRILLING_BY_MARKING         ("Сверление по разметке",           new Image("/pics/opLogos/drill.png"),           new OpDrillingByMarkingCounter()),  //СВЕРЛЕНИЕ ПО РАЗМЕТКЕ
    CUT_OFF_ON_SAW              ("Отрезание на пиле",               new Image("/pics/opLogos/saw.png"),             new OpCutOffOnTheSawCounter()),     //ОТРЕЗАНИЕ НА ПИЛЕ

    //ТОКАРНЫЕ ОПЕРАЦИИ
    LATHE_MOUNT_DISMOUNT        ("Установка и снятие детали",       new Image("/pics/opLogos/lathe.png"),           new OpLatheMountDismountCounter()),  //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    LATHE_TURNING               ("Точение и растачивание",          new Image("/pics/opLogos/lathe.png"),           new OpLatheTurningCounter()),        //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    LATHE_CUT_GROOVE            ("Точение канавки",                 new Image("/pics/opLogos/lathe.png"),           new OpLatheCutGrooveCounter()),      //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    LATHE_THREADING             ("Нарезание резьбы",                new Image("/pics/opLogos/threading.png"),       new OpLatheThreadingCounter()),      //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    LATHE_DRILLING              ("Сверление",                       new Image("/pics/opLogos/lathe_drill.png"),     new OpLatheDrillingCounter()),       //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    LATHE_ROLLING               ("Накатывание рифления",            new Image("/pics/opLogos/lathe.png"),           new OpLatheRollingCounter()),        //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    LATHE_CUT_OFF               ("Отрезание",                       new Image("/pics/opLogos/lathe.png"),           new OpLatheCutOffCounter()),         //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS             ("Сварка непрерывная",              new Image("/pics/opLogos/weld.png"),            new OpWeldContinuousCounter()),      //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED                 ("Сварка точечная",                 new Image("/pics/opLogos/weld.png"),            new OpWeldDottedCounter()),          //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ

    //УПАКОВКА
    PACK_ON_PALLET              ("Монтаж на поддон",                new Image("/pics/opLogos/pallet.png"),          new OpPackOnPalletCounter()),        //УПАКОВКА НА ПАЛЛЕТ (Установка и закрепление)
    PACK_IN_MACHINE_STRETCH_WRAP("Упаковка в машинную стрейч-пленку",new Image("/pics/opLogos/wrap.png"),           new OpPackInMachineStretchWrapCounter()),//УПАКОВКА В КАРТОН (КРЫШКИ И УГОЛКИ)
    PACK_IN_HAND_STRETCH_WRAP   ("Упаковка в ручную стрейч-пленку", new Image("/pics/opLogos/wrap.png"),            new OpPackInHandStretchWrapCounter()),//УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    PACK_IN_CARTOON_BOX         ("Упаковка в картонную коробку",    new Image("/pics/opLogos/cartoon.png"),         new OpPackInCartoonBoxCounter()),     //УПАКОВКА В КАРТОННУЮ КОРОБКУ
    PACK_IN_BUBBLE_WRAP         ("Упаковка в пузырьковую пленку",   new Image("/pics/opLogos/wrap.png"),            new OpPackInBubbleWrapCounter()),     //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ

    ERROR_OP_DATA               ("Error",                           new Image("/pics/opLogos/error.png"),null);

//===================================================================================================================================================================================================

    @Getter String opName;
    @Getter Image logo;
    @Getter NormCounter normCounter;

    EOpType(String opName, Image logo, NormCounter normCounter) {
        this.opName = opName;
        this.logo = logo;
        this.normCounter = normCounter;
    }

    public static EOpType findOpTypeByName(String name){
        for(EOpType op : EOpType.values()){
            if(op.name().equals(name))
                   return op;
        }
        throw new NoSuchElementException("No such type in EOpType found!");
    }
}
