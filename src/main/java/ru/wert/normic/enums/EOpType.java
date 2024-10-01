package ru.wert.normic.enums;

import javafx.scene.image.Image;
import lombok.Getter;
import ru.wert.normic.controllers.assembling.countings.*;
import ru.wert.normic.controllers.listOperations.counters.OpBendingCounter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.locksmith.counters.*;
import ru.wert.normic.controllers.simpleOperations.counters.OpSimpleOperationsCounter;
import ru.wert.normic.controllers.packing.counters.*;
import ru.wert.normic.controllers.paint.counters.OpPaintAssmCounter;
import ru.wert.normic.controllers.paint.counters.OpPaintCounter;
import ru.wert.normic.controllers.turning.counters.*;
import ru.wert.normic.controllers.welding.counters.OpWeldContinuousCounter;
import ru.wert.normic.controllers.welding.counters.OpWeldDifficultyCounter;
import ru.wert.normic.controllers.welding.counters.OpWeldDottedCounter;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.entities.ops.opAssembling.*;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.*;
import ru.wert.normic.entities.ops.opPack.*;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.opTurning.*;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.entities.ops.opWelding.OpWeldDifficulty;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.entities.ops.simpleOperations.OpSimpleOperation;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

public enum EOpType {




    //ОТДЕЛЬНЫЕ ЭЛЕМЕНТЫ
    DETAIL                      ("Деталь",                          OpDetail.class,                     new Image("/pics/opLogos/detail.png"),null),                            //ДЕТАЛЬ
    ASSM                        ("Сборка",                          OpAssm.class,                       new Image("/pics/opLogos/assemble.png"),null),                          //СБОРКА
    PACK                        ("Упаковка",                        OpPack.class,                       new Image("/pics/opLogos/pack.png"), null),                             //УПАКОВКА

    //ОПЕРАЦИИ С ЛИСТОМ
    CUTTING                     ("Резка и зачистка",                OpCutting.class,                    new Image("/pics/opLogos/list_cutting.png"),    new OpCuttingCounter()),           //ВЫРЕЗАНИЕ ЛИСТОВОЙ ДЕТАЛИ
    BENDING                     ("Гибка",                           OpBending.class,                    new Image("/pics/opLogos/bend.png"),            new OpBendingCounter()),           //ГИБКА ЛИСТОВОЙ ДЕТАЛИ

    //ОКРАШИВАНИЕ
    PAINTING                    ("Покраска детали",                 OpPaint.class,                      new Image("/pics/opLogos/paint.png"),           new OpPaintCounter()),              //ОКРАШИВАНИЕ ДЕТАЛИ
    PAINT_ASSM                  ("Покраска сборки",                 OpPaintAssm.class,                  new Image("/pics/opLogos/paint.png"),           new OpPaintAssmCounter()),          //ОКРАШИВАНИЕ СБОРКИ

    //СБОРОЧНЫЕ ОПЕРАЦИИ
    ASSM_CUTTINGS               ("Сборка раскройного материала",    OpAssmCutting.class,                new Image("/pics/opLogos/assm_cutting.png"),    new OpAssmCattingCounter()),        //СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    ASSM_NODES                  ("Сборка стандартных узлов",        OpAssmNode.class,                   new Image("/pics/opLogos/cutting.png"),         new OpAssmNodeCounter()),           //СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    ASSM_NUTS                   ("Сборка крепежа",                  OpAssmNut.class,                    new Image("/pics/opLogos/nuts.png"),            new OpAssmNutsCounter()),           //СБОРКА КРЕПЕЖА
    LEVELING_SEALER             ("Наливной уплотнитель",            OpLevelingSealer.class,             new Image("/pics/opLogos/sealer.png"),          new OpLevelingSealerCounter()),     //НАЛИВКА УПЛОТНИТЕЛЯ
    THERMO_INSULATION           ("Термоизоляция",                   OpThermoInsulation.class,           new Image("/pics/opLogos/thermoinsulation.png"),new OpThermoInsulationCounter()),   //МОНТАЖ ТЕРМОИЗОЛЯЦИИ
    ASSM_CHOP_OFF               ("Рубка в размер",                  OpAssmChopOff.class,                new Image("/pics/opLogos/guillotine.png"),      new OpAssmChopOffCounter()),        //ОТРУБКА ЗАГОТОВКИ НА СБОРОЧНОМ УЧАСТКЕ

    //СЛЕСАРНЫЕ ОПЕРАЦИИ
    LOCKSMITH                   ("Слесарные операции",              OpLocksmith.class,                  new Image("/pics/opLogos/locksmith.png"),       new OpLocksmithCounter()),          //СЛЕАРНЫЕ РАБОТЫ
    ASSM_NUTS_MK                ("Крепеж (Учаток МК)",              OpAssmNutMK.class,                  new Image("/pics/opLogos/nuts.png"),            new OpAssmNutsMKCounter()),         //КРЕПЕЖ (УЧАСТОК МК)
    CHOP_OFF                    ("Рубка в размер",                  OpChopOff.class,                    new Image("/pics/opLogos/guillotine.png"),      new OpChopOffCounter()),            //ОТРУБКА ЗАГОТОВКИ НА ГЕКЕ
    DRILLING_BY_MARKING         ("Сверление по разметке",           OpDrillingByMarking.class,          new Image("/pics/opLogos/drill.png"),           new OpDrillingByMarkingCounter()),  //СВЕРЛЕНИЕ ПО РАЗМЕТКЕ
    CUT_OFF_ON_SAW              ("Отрезание на пиле",               OpCutOffOnTheSaw.class,             new Image("/pics/opLogos/saw.png"),             new OpCutOffOnTheSawCounter()),     //ОТРЕЗАНИЕ НА ПИЛЕ

    //ТОКАРНЫЕ ОПЕРАЦИИ
    LATHE_MOUNT_DISMOUNT        ("Установка и снятие детали",       OpLatheMountDismount.class,         new Image("/pics/opLogos/lathe.png"),           new OpLatheMountDismountCounter()),  //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    LATHE_TURNING               ("Точение и растачивание",          OpLatheTurning.class,               new Image("/pics/opLogos/lathe.png"),           new OpLatheTurningCounter()),        //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    LATHE_CUT_GROOVE            ("Точение канавки",                 OpLatheCutGroove.class,             new Image("/pics/opLogos/lathe.png"),           new OpLatheCutGrooveCounter()),      //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    LATHE_THREADING             ("Нарезание резьбы",                OpLatheThreading.class,             new Image("/pics/opLogos/threading.png"),       new OpLatheThreadingCounter()),      //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    LATHE_DRILLING              ("Сверление",                       OpLatheDrilling.class,              new Image("/pics/opLogos/lathe_drill.png"),     new OpLatheDrillingCounter()),       //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    LATHE_ROLLING               ("Накатывание рифления",            OpLatheRolling.class,               new Image("/pics/opLogos/lathe.png"),           new OpLatheRollingCounter()),        //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    LATHE_CUT_OFF               ("Отрезание",                       OpLatheCutOff.class,                new Image("/pics/opLogos/lathe.png"),           new OpLatheCutOffCounter()),         //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS             ("Сварка непрерывная",              OpWeldContinuous.class,             new Image("/pics/opLogos/weld.png"),            new OpWeldContinuousCounter()),      //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED                 ("Сварка точечная",                 OpWeldDotted.class,                 new Image("/pics/opLogos/weld.png"),            new OpWeldDottedCounter()),          //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ
    WELD_DIFFICULTY             ("Сложность сварки",                OpWeldDifficulty.class,             new Image("/pics/opLogos/weld.png"),            new OpWeldDifficultyCounter()),      //СЛОЖНОСТЬ (Тпз) СВАРКИ

    //УПАКОВКА
    PACK_ON_PALLET              ("Монтаж на поддон",                OpPackOnPallet.class,               new Image("/pics/opLogos/pallet.png"),          new OpPackOnPalletCounter()),        //УПАКОВКА НА ПАЛЛЕТ (Установка и закрепление)
    PACK_IN_MACHINE_STRETCH_WRAP("Упаковка в машинную стрейч-пленку", OpPackInMachineStretchWrap.class, new Image("/pics/opLogos/wrap.png"),            new OpPackInMachineStretchWrapCounter()),//УПАКОВКА В КАРТОН (КРЫШКИ И УГОЛКИ)
    PACK_IN_HAND_STRETCH_WRAP   ("Упаковка в ручную стрейч-пленку", OpPackInHandStretchWrap.class,      new Image("/pics/opLogos/wrap.png"),            new OpPackInHandStretchWrapCounter()),//УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    PACK_IN_CARTOON_BOX         ("Упаковка в картонную коробку",    OpPackInCartoonBox.class,           new Image("/pics/opLogos/cartoon.png"),         new OpPackInCartoonBoxCounter()),     //УПАКОВКА В КАРТОННУЮ КОРОБКУ
    PACK_IN_BUBBLE_WRAP         ("Упаковка в пузырьковую пленку",   OpPackInBubbleWrap.class,           new Image("/pics/opLogos/wrap.png"),            new OpPackInBubbleWrapCounter()),     //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ

    //ПРОЧИЕ ОПЕРАЦИИ
    SIMPLE_OPERATION            ("Прочие простые операции",         OpSimpleOperation.class,            new Image("/pics/opLogos/operations.png"),      new OpSimpleOperationsCounter()),     //ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ

    ERROR_OP_DATA               ("Error",                           OpErrorData.class,                  new Image("/pics/opLogos/error.png"),null);

//===================================================================================================================================================================================================

    @Getter String opName;
    @Getter Class<?> clazz;
    @Getter Image logo;
    @Getter NormCounter normCounter;

    EOpType(String opName, Class<?> clazz, Image logo, NormCounter normCounter) {
        this.opName = opName;
        this.clazz = clazz;
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

//    public OpData jsonConverter(String stringType, String json){
//        EOpType type = findOpTypeByName(stringType);
//        return jsonConverter(type, json);
//    }
//
//    public OpData jsonConverter(EOpType type, String json){
//
//        OpData newOpData;
//
//        switch (type) {
//            case "DETAIL": newOpData = (OpDetail) OpDataJsonConverter.convert(json); break;
//            case "ASSM"  : newOpData = (OpAssm) OpDataJsonConverter.convert(json); break;
//            case "PACK"  : newOpData = (OpPack) OpDataJsonConverter.convert(json); break;
//        }
//    }
}
