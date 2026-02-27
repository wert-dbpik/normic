package ru.wert.normic.enums;

import javafx.scene.image.Image;
import lombok.Getter;
import ru.wert.normic.controllers._plates.assembling.countings.*;
import ru.wert.normic.controllers._plates.electricalOperations.counters.*;
import ru.wert.normic.controllers._plates.listOperations.counters.OpBendingCounter;
import ru.wert.normic.controllers._plates.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers._plates.locksmith.counters.*;
import ru.wert.normic.controllers._plates.paint.counters.OpPaintDetailCounter;
import ru.wert.normic.controllers._plates.simpleOperations.counters.OpSimpleOperationsCounter;
import ru.wert.normic.controllers._plates.packing.counters.*;
import ru.wert.normic.controllers._plates.paint.counters.OpPaintAssmCounter;
import ru.wert.normic.controllers._plates.paint.counters.OpPaintOldCounter;
import ru.wert.normic.controllers._plates.turning.counters.*;
import ru.wert.normic.controllers._plates.welding.counters.*;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.entities.ops.electrical.*;
import ru.wert.normic.entities.ops.opAssembling.*;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.*;
import ru.wert.normic.entities.ops.opPack.*;
import ru.wert.normic.entities.ops.opPaint.OpPaintOld;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.opPaint.OpPaintDetail;
import ru.wert.normic.entities.ops.opTurning.*;
import ru.wert.normic.entities.ops.opWelding.*;
import ru.wert.normic.entities.ops.simpleOperations.OpSimpleOperation;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

public enum EOpType {




    //ОТДЕЛЬНЫЕ ЭЛЕМЕНТЫ
    DETAIL                      ("Деталь",                          null, OpDetail.class,                     new Image("/pics/opLogos/detail.png"),null),                            //ДЕТАЛЬ
    ASSM                        ("Сборка",                          null, OpAssm.class,                       new Image("/pics/opLogos/assemble.png"),null),                          //СБОРКА
    PACK                        ("Упаковка",                        null, OpPack.class,                       new Image("/pics/opLogos/pack.png"), null),                             //УПАКОВКА

    //ОПЕРАЦИИ С ЛИСТОМ
    CUTTING                     ("Резка и зачистка",                null, OpCutting.class,                    new Image("/pics/opLogos/list_cutting.png"),    new OpCuttingCounter()),           //ВЫРЕЗАНИЕ ЛИСТОВОЙ ДЕТАЛИ
    BENDING                     ("Гибка",                           null, OpBending.class,                    new Image("/pics/opLogos/bend.png"),            new OpBendingCounter()),           //ГИБКА ЛИСТОВОЙ ДЕТАЛИ

    //ОКРАШИВАНИЕ
    PAINTING                    ("Покраска",                        null, OpPaintOld.class,                   new Image("/pics/opLogos/paint.png"),           new OpPaintOldCounter()),           //ОКРАШИВАНИЕ СТАРОЕ
    PAINT_DETAIL                ("Покраска детали",                 null, OpPaintDetail.class,                new Image("/pics/opLogos/paint.png"),           new OpPaintDetailCounter()),        //ОКРАШИВАНИЕ ДЕТАЛИ
    PAINT_ASSM                  ("Покраска сборки",                 null, OpPaintAssm.class,                  new Image("/pics/opLogos/paint.png"),           new OpPaintAssmCounter()),          //ОКРАШИВАНИЕ СБОРКИ

    //СБОРОЧНЫЕ ОПЕРАЦИИ
    ASSM_CUTTINGS               ("Сборка раскройного материала",    null, OpAssmCutting.class,                new Image("/pics/opLogos/assm_cutting.png"),    new OpAssmCattingCounter()),        //СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    ASSM_NODES                  ("Сборка стандартных узлов",        null, OpAssmNode.class,                   new Image("/pics/opLogos/cutting.png"),         new OpAssmNodeCounter()),           //СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    ASSM_NUTS                   ("Сборка крепежа",                  null, OpAssmNut.class,                    new Image("/pics/opLogos/nuts.png"),            new OpAssmNutsCounter()),           //СБОРКА КРЕПЕЖА
    LEVELING_SEALER             ("Наливной уплотнитель",            null, OpLevelingSealer.class,             new Image("/pics/opLogos/sealer.png"),          new OpLevelingSealerCounter()),     //НАЛИВКА УПЛОТНИТЕЛЯ
    THERMO_INSULATION           ("Термоизоляция",                   null, OpThermoInsulation.class,           new Image("/pics/opLogos/thermoinsulation.png"),new OpThermoInsulationCounter()),   //МОНТАЖ ТЕРМОИЗОЛЯЦИИ
    ASSM_CHOP_OFF               ("Рубка в размер",                  null, OpAssmChopOff.class,                new Image("/pics/opLogos/guillotine.png"),      new OpAssmChopOffCounter()),        //ОТРУБКА ЗАГОТОВКИ НА СБОРОЧНОМ УЧАСТКЕ

    //СЛЕСАРНЫЕ ОПЕРАЦИИ
    LOCKSMITH                   ("Слесарные операции",              null, OpLocksmith.class,                  new Image("/pics/opLogos/locksmith.png"),       new OpLocksmithCounter()),          //СЛЕАРНЫЕ РАБОТЫ
    ASSM_NUTS_MK                ("Крепеж (Учаток МК)",              null, OpAssmNutMK.class,                  new Image("/pics/opLogos/nuts.png"),            new OpAssmNutsMKCounter()),         //КРЕПЕЖ (УЧАСТОК МК)
    CHOP_OFF                    ("Рубка в размер",                  null, OpChopOff.class,                    new Image("/pics/opLogos/guillotine.png"),      new OpChopOffCounter()),            //ОТРУБКА ЗАГОТОВКИ НА ГЕКЕ
    DRILLING_BY_MARKING         ("Сверление по разметке",           null, OpDrillingByMarking.class,          new Image("/pics/opLogos/drill.png"),           new OpDrillingByMarkingCounter()),  //СВЕРЛЕНИЕ ПО РАЗМЕТКЕ
    CUT_OFF_ON_SAW              ("Отрезание на пиле",               null, OpCutOffOnTheSaw.class,             new Image("/pics/opLogos/saw.png"),             new OpCutOffOnTheSawCounter()),     //ОТРЕЗАНИЕ НА ПИЛЕ

    //ТОКАРНЫЕ ОПЕРАЦИИ
    LATHE_MOUNT_DISMOUNT        ("Установка/снятие детали, переворот", null, OpLatheMountDismount.class,      new Image("/pics/opLogos/lathe.png"),           new OpLatheMountDismountCounter()),  //УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
    LATHE_TURNING               ("Точение и растачивание",          null, OpLatheTurning.class,               new Image("/pics/opLogos/lathe.png"),           new OpLatheTurningCounter()),        //ТОЧЕНИЕ И РАСТАЧИВАНИЕ
    LATHE_CUT_GROOVE            ("Точение канавки",                 null, OpLatheCutGroove.class,             new Image("/pics/opLogos/lathe.png"),           new OpLatheCutGrooveCounter()),      //ТОЧЕНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
    LATHE_THREADING             ("Нарезание резьбы",                null, OpLatheThreading.class,             new Image("/pics/opLogos/threading.png"),       new OpLatheThreadingCounter()),      //НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
    LATHE_DRILLING              ("Сверление",                       null, OpLatheDrilling.class,              new Image("/pics/opLogos/lathe_drill.png"),     new OpLatheDrillingCounter()),       //СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
    LATHE_ROLLING               ("Накатывание рифления",            null, OpLatheRolling.class,               new Image("/pics/opLogos/lathe.png"),           new OpLatheRollingCounter()),        //НАКАТЫВАНИЕ ПРОФИЛЯ НА ТОАРНОМ СТАНКЕ
    LATHE_CUT_OFF               ("Отрезание резцом",                null, OpLatheCutOff.class,                new Image("/pics/opLogos/lathe.png"),           new OpLatheCutOffCounter()),         //ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ

    //СВАРОЧНЫЕ ОПЕРАЦИИ
    WELD_CONTINUOUS             ("Сварка непрерывная (old)",        null, OpWeldContinuousOld.class,          new Image("/pics/opLogos/weld.png"),            new OpWeldContinuousCounterOld()),   //СВАРКА НЕПРЕРЫВНЫМ ШВОМ СТАРАЯ
    WELD_CONTINUOUS_NEW         ("Сварка непрерывная",              null, OpWeldContinuousNew.class,          new Image("/pics/opLogos/weld.png"),            new OpWeldContinuousCounterNew()),      //СВАРКА НЕПРЕРЫВНЫМ ШВОМ
    WELD_DOTTED                 ("Сварка точечная",                 null, OpWeldDotted.class,                 new Image("/pics/opLogos/weld.png"),            new OpWeldDottedCounter()),          //СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ
    WELD_DIFFICULTY             ("Сложность сварки",                null, OpWeldDifficulty.class,             new Image("/pics/opLogos/weld.png"),            new OpWeldDifficultyCounter()),      //СЛОЖНОСТЬ (Тпз) СВАРКИ
    WELD_ASSM                   ("Сборка свариваемой конструкции",  null, OpWeldAssm.class,                   new Image("/pics/opLogos/weld.png"),            new OpWeldAssmCounter()),            //СБОРКА СВ. КОНСТРУКЦИИ

    //УПАКОВКА
    PACK_ON_PALLET              ("Монтаж на поддон",                null, OpPackOnPallet.class,               new Image("/pics/opLogos/pallet.png"),          new OpPackOnPalletCounter()),        //УПАКОВКА НА ПАЛЛЕТ (Установка и закрепление)
    PACK_IN_MACHINE_STRETCH_WRAP("Упаковка в машинную стрейч-пленку", null, OpPackInMachineStretchWrap.class, new Image("/pics/opLogos/wrap.png"),            new OpPackInMachineStretchWrapCounter()),//УПАКОВКА В КАРТОН (КРЫШКИ И УГОЛКИ)
    PACK_IN_HAND_STRETCH_WRAP   ("Упаковка в ручную стрейч-пленку", null, OpPackInHandStretchWrap.class,      new Image("/pics/opLogos/wrap.png"),            new OpPackInHandStretchWrapCounter()),//УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    PACK_IN_CARTOON_BOX         ("Упаковка в картонную коробку",    null, OpPackInCartoonBox.class,           new Image("/pics/opLogos/cartoon.png"),         new OpPackInCartoonBoxCounter()),     //УПАКОВКА В КАРТОННУЮ КОРОБКУ
    PACK_IN_BUBBLE_WRAP         ("Упаковка в пузырьковую пленку",   null, OpPackInBubbleWrap.class,           new Image("/pics/opLogos/wrap.png"),            new OpPackInBubbleWrapCounter()),     //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ

    //ПРОЧИЕ ОПЕРАЦИИ
    SIMPLE_OPERATION            ("Прочие простые операции",         null, OpSimpleOperation.class,            new Image("/pics/opLogos/operations.png"),      new OpSimpleOperationsCounter()),     //ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ

    ERROR_OP_DATA               ("Error",                           null, OpErrorData.class,                  new Image("/pics/opLogos/error.png"),null),

    //===================================================================================================================================================================================================
    //                                                                                  ОПЕРАЦИИ ЭЛЕКТРОМОНТАЖА

    EL_MOUNT_ON_DIN_AUTOMATS            ("Автоматы, УЗО, коммутаторы и т.д.",   "Установка на динрейку автоматов",  OpMountOnDinAutomats.class,             new Image("/pics/opLogos/lamp.png"),       new OpMountOnDinAutomatsCounter()),             //Установка на динрейку атоматов и т.д.
    EL_MOUNT_ON_DIN_HEATERS             ("Нагреватели, счетчики и т.д.",        "Установка на динрейку счетчиков",  OpMountOnDinHeaters.class,              new Image("/pics/opLogos/lamp.png"),        new OpMountOnDinHeatersCounter()),             //Установка на динрейку нагревателей и т.д.

    EL_MOUNT_ON_SCREWS_NO_DISASSM_2     ("На 2 винта",                          "Установка на 2 винта",             OpMountOnScrewsNoDisAssm2.class,        new Image("/pics/opLogos/lamp.png"),       new OpMountOnScrewsNoDisAssm2Counter()),        //Установка на 2 винта без разборки корпуса
    EL_MOUNT_ON_SCREWS_NO_DISASSM_4     ("На 4 винта",                          "Установка на 4 винта",             OpMountOnScrewsNoDisAssm4.class,        new Image("/pics/opLogos/lamp.png"),       new OpMountOnScrewsNoDisAssm4Counter()),        //Установка на 4 винта без разборки корпуса
    EL_MOUNT_ON_SCREWS_WITH_DISASSM_2   ("На 2 винта",                          "Установка на 2 винта",             OpMountOnScrewsWithDisAssm2.class,      new Image("/pics/opLogos/lamp.png"),       new OpMountOnScrewsWithDisAssm2Counter()),      //Установка на 2 винта с разборкой корпуса_2
    EL_MOUNT_ON_SCREWS_WITH_DISASSM_4   ("На 4 винта",                          "Установка на 4 винта",             OpMountOnScrewsWithDisAssm4.class,      new Image("/pics/opLogos/lamp.png"),       new OpMountOnScrewsWithDisAssm4Counter()),      //Установка на 2 винта с разборкой корпуса_2
    EL_MOUNT_ON_VSHG                    ("Установка на ВШГ(4шт)",               null,                               OpMountOnVSHG.class,                    new Image("/pics/opLogos/lamp.png"),       new OpMountOnVSHGCounter()),                    //Установка на ВШГ (4шт)

    EL_CONNECT_DEVICE_MORTISE_CONTACT   ("На врезной контакт",                  "Подключение на врезной контакт",   OpConnectDeviceMortiseContact.class,    new Image("/pics/opLogos/lamp.png"),       new OpConnectDeviceMortiseContactCounter()),    //Подключение электроустройств на врезной контакт
    EL_CONNECT_DEVICE_SPRING_CLAMP      ("На пружинный зажим",                  "Подключение на пружинный зажим",   OpConnectDeviceSpringClamp.class,       new Image("/pics/opLogos/lamp.png"),       new OpConnectDeviceSpringClampCounter()),        //Подключение электроустройств на пружинный зажим
    EL_CONNECT_DEVICE_CLAMPING_SCREW    ("На зажимной винт",                    "Подключение на зажимной винт\"",   OpConnectDeviceClampingScrew.class,     new Image("/pics/opLogos/lamp.png"),       new OpConnectDeviceClampingScrewCounter()),     //Подключение электроустройств на зажимной винт
    EL_CONNECT_DEVICE_VSHG              ("На ВШГ (наконечник кольцо)",          "Подключение на ВШГ",               OpConnectDeviceVSHG.class,              new Image("/pics/opLogos/lamp.png"),       new OpConnectDeviceVSHGCounter()),               //Подключение электроустройств на ВШГ


    EL_CUT_CABLE_HANDLY_MC6             ("Многожильный 6 мм",                   "Резка многожильного кабеля 6 мм",  OpCutCableHandlyMC6.class,              new Image("/pics/opLogos/lamp.png"),       new OpCutCableHandlyMC6Counter()),              //Резка кабеля вручную Многожильный 6 мм
    EL_CUT_CABLE_HANDLY_MC15            ("Многожильный 11-15 мм",               "Резка многожильного кабеля  11-15 мм\"", OpCutCableHandlyMC15.class,             new Image("/pics/opLogos/lamp.png"),       new OpCutCableHandlyMC15Counter()),              //Резка кабеля вручную Многожильный 11-15 мм
    EL_CUT_CABLE_HANDLY_SC              ("Одножильный",                         "Резка одножильного кабеля",        OpCutCableHandlySC.class,               new Image("/pics/opLogos/lamp.png"),       new OpCutCableHandlySCCounter()),                //Резка кабеля вручную Одножильный

    EL_CUT_CABLE_ON_MACHINE             ("Резка кабеля на автомате",            null,                               OpCutCableOnMachine.class,              new Image("/pics/opLogos/lamp.png"),       new OpCutCableOnMachineCounter()),               //Резка кабеля на автомате
    EL_CUT_METAL_SLEEVE                 ("Резка металлорукава",                 null,                               OpCutMetalSleeve.class,                 new Image("/pics/opLogos/lamp.png"),       new OpCutMetalSleeveCounter()),                  //Резка металлорукава
    EL_CUT_CABLE_CHANNEL                ("Резка кабель-канала, динрейки",       null,                               OpCutCableChannel.class,                new Image("/pics/opLogos/lamp.png"),       new OpCutCableChannelCounter()),                 //Резка кабельканала, динрейки

    EL_TINNING_IN_BATHE                 ("В ванночке",                          "Лужение в ванночке",               OpTinningInBathe.class,                 new Image("/pics/opLogos/lamp.png"),       new OpTinningInBatheCounter()),                  //Лужение в ванночке
    EL_TINNING                          ("Электропаяльником",                   "Лужение электропаяльником",        OpTinning.class,                        new Image("/pics/opLogos/lamp.png"),       new OpTinningCounter()),                         //Лужение электропаяльником

    EL_MOUNT_TIP_ON_CABLE               ("Оконцовка провода",                   null,                               OpMountTipOnCable.class,                new Image("/pics/opLogos/lamp.png"),       new OpMountTipOnCableCounter()),                 //Оконцовка провода наконечником
    EL_MOUNT_TIP_ON_POWER_CABLE         ("Оконцовка силового кабеля",           null,                               OpMountTipOnPowerCable.class,           new Image("/pics/opLogos/lamp.png"),       new OpMountTipOnPowerCableCounter()),            //Оконцовка силового кабеля наконечником

    EL_MARKING                          ("Маркировка",                          null,                               OpMarking.class,                        new Image("/pics/opLogos/lamp.png"),       new OpMarkingCounter()),                         //Маркировка
    EL_MOUNT_OF_SIGNAL_EQUIP            ("Установка сигнальной аппаратуры",     null,                               OpMountOfSignalEquip.class,             new Image("/pics/opLogos/lamp.png"),       new OpMountOfSignalEquipCounter()),              //Установка сигнальной аппаратуры
    EL_SOLDERING                        ("Соединение элементов пайкой",         null,                               OpSoldering.class,                      new Image("/pics/opLogos/lamp.png"),       new OpSolderingCounter()),                       //Соединение элементов пайкой
    EL_MOUNT_OF_CABLE_ENTRIES           ("Установка кабельных вводов",          null,                               OpMountOfCableEntries.class,            new Image("/pics/opLogos/lamp.png"),       new OpMountOfCableEntriesCounter()),             //Установка кабельных вводов
    EL_FIX_OF_CABLES                    ("Укладка жгутов",                      null,                               OpFixOfCables.class,                    new Image("/pics/opLogos/lamp.png"),       new OpFixOfCablesCounter()),                     //Укладка жгутов

    EL_ISOLATE_WITH_THERM_TUBE10        ("2-10мм",                              "Термоусадка трубкой 2-10мм",       OpIsolateWithThermTube10.class,         new Image("/pics/opLogos/lamp.png"),       new OpIsolateWithThermotube10Counter()),         //Изоляция термотрубкой 2-10мм
    EL_ISOLATE_WITH_THERM_TUBE30        ("10-30мм",                             "Термоусадка трубкой 10-30мм",      OpIsolateWithThermTube30.class,         new Image("/pics/opLogos/lamp.png"),       new OpIsolateWithThermotube30Counter());         //Изоляция термотрубкой 10-30мм

    @Getter String menuName;
    @Getter String plateName;
    @Getter Class<?> clazz;
    @Getter Image logo;
    @Getter NormCounter normCounter;

    EOpType(String menuName, String plateName, Class<?> clazz, Image logo, NormCounter normCounter) {
        this.menuName = menuName;
        this.plateName = plateName == null ? menuName : plateName;
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

}
