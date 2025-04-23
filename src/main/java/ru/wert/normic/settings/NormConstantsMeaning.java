package ru.wert.normic.settings;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class NormConstantsMeaning implements Serializable {

    //--- РЕЗКА И ЗАЧИСТКА
    private double cuttingSpeed = 1.28; //Скорость резания, зависящая от площади детали, мин
    private double revolverSpeed = 0.057; //Скорость вырубки одного элемента револьвером, мин/уд
    private double perforationSpeed = 0.007; //Скорость перфорирования, мин/уд
    private double cuttingServiceRatio = 1.22; //коэфффициент, учитывающий 22% времени на обслуживание при резке
    private double stripingSpeed = 2.5; //Скорость зачистки резанных кромок, сек

    //--- ГИБКА
    private double bendingSpeed = 0.15; //Скорость гибки, мин/гиб
    private double bendingService_ratio = 1.25; //коэфффициент, учитывающий 25% времени на обслуживание при гибке

    //--- СЛЕСАРНЫЕ РАБОТЫ
    private double chopSpeed = 0.05; //Скорость рубки на Геке, мин/удар
    private double rivetsSpeed = 18; //Скорость установки вытяжной заклепки, сек/закл
    private double countersinkingSpeed = 0.31; //Скорость сверления и зенковки, мин/отв
    private double threadingSpeed = 0.37; //Скорость нарезания резьбы, мин/отв
    private double smallSawingSpeed = 0.2; //Скорость пиления на малой пиле, мин/рез
    private double bigSawingSpeed = 1.0; //Скорость пиления на большой пиле, мин/рез

    //--- ПОКРАСКА ДЕТАЛИ
    private int detailDelta = 100; //Расстояние между деталями, мм
    private double washing = 12; //Мойка, сек
    private double winding = 6; //Продувка, сек
    private double drying = 20; //Сушка, сек
    private double baking = 40.0; //Полимеризация, мин 

    //--- ПОКРАСКА СБОРКИ
    private int assmDelta = 300; //Расстояние между сборками, мм
    private double hangingTime = 0.34; //Время навески и снятия после полимеризации, мин
    private double windingMovingSpeed = 1.4; //Продувка после промывки и перемещение изделя на штанге, мин/1 м.кв.
    private double solidBoxSpeed = 1.686; //Скорость окрашивания глухих шкафов, мин/1 м.кв.
    private double frameSpeed = 2.4; //Скорость окрашивания открытых рам и кроссов, мин/1 м.кв.

    //--- СВАРКА НЕПРЕРЫВНАЯ
    private double weldingSpeed = 4.0; //Скорость сваркм, мин/м

    //--- СВАРКА ТОЧЕЧНАЯ
    private double weldingPartsSpeed = 0.13; //Скорость онденсаторной сварки точкой, мин/элемент
    private double weldingDottedSpeed = 0.3; //Скорость контактной сварки, мин/точку
    private double weldingDropSpeed = 0.07; //Скорость сварки прихватками, мин/прихватку

    //--- ЗАЛИВКА УПЛОТНИТЕЛЯ
    private double levelingPreparedTime = 0.32; //ПЗ время, мин
    private double levelingSpeed = 0.16; //Скорость нанесения, м/мин


    //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ
    private double screwsSpeed = 0.25; //Скорость установки винтов, мин
    private double vshgsSpeed = 0.4; //Скорость установки комплектов ВШГ, мин
    private double rivetNutsSpeed = 22; //Скорость установки заклепочных гаек, сек
    private double groundSetsSpeed = 1.0; //Скорость установки комплекта заземления с этикеткой, мин
    private double othersSpeed = 15; //Скорость установки другого крепежа, сек

    //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
    private double postLocksSpeed = 1.5; //Скорость установки почтового замка с регулировкой, мин
    private double doubleLocksSpeed = 3.0; //Скорость установки замка с рычагами, мин
    private double glassSpeed = 2.0; //Скорость установки стекла на полиуретан, мин
    private double detectorsSpeed = 2.0; //Скорость установки извещателей (ИО-102), мин
    private double connectionBoxesSpeed = 3.0; //Скорость установки коробки соединительной (КС-4), мин

    //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
    private double sealerSpeed = 40; //Скорость монтажа уплотнителя на ребро корпуса, сек/м
    private double selfAdhSealerSpeed =  20; //Скорость установки самоклеющегося уплотнителя, сек/м
    private double insulationSpeed = 5.5; //Скорость разметки, резки и укладки утеплителя, мин/м2

    //--- УПАКОВКА
    private double cartoonBoxPreparedTime = 10.0; //ПЗ время изготовления коробок, мин
    private double cartoonBoxSpeed = 2.3; //Время изготовления коробки, мин
    private double stretchMachineWinding = 2.1; //Время наматывания машинной стретч-пленки, мин
    private double cartoonBoxAndAnglesSpeed = 12.0; //Время изготовления крышек и уголков, мин
    private double packInCartoonBoxSpeed = 1.5; //Время упаковки изделя в коробку, мин
    private double ductTapeLength = 66.0; //Длина рулона скотча, м
    private double bubbleCutAndDuct = 0.7; //ПЗ время пузырьковой пленки, мин
    private double bubbleHandWinding = 0.2; //Скорость оборачивания пузырьковой пленки, мин/м.кв.
    private double stretchHandWinding = 0.2; //Скорость оборачивания стретч пленки, мин/м
    
    //--- ЭЛЕКТРОМОНТАЖ
    private double mountOnDinAutomats = 0.3; //Скорость установки на динрейку автоматов, мин
    private double mountOnDinHeaters = 0.5; //Скорость установки на динрейку нагревателей, мин
    private double mountOn2ScrewsNoDisassm = 1.2; //Скорость установки на 2 винта без разборки, мин
    private double mountOn4ScrewsNoDisassm = 2.0; //Скорость установки на 4 винта без разборки, мин

    private double mountOn2ScrewsWithDisassm = 3.0; //Скорость установки на 2 винта с разборкой, мин
    private double mountOn4ScrewsWithDisassm = 5.0; //Скорость установки на 4 винта с разборкой, мин

    private double mountOnVshg = 2.5; //Скорость установки на ВШГ (4 шт), мин

    private double connectDeviceMortiseContact = 0.12; //Подключение устр. на врезной контакт, мин/контакт
    private double connectDeviceSpringClamp = 0.08; //Подключение устр. на пружинный зажим, мин/контакт
    private double connectDeviceClampingScrew = 0.3; //Подключение устр. на зажимной винт, мин/контакт
    private double connectDeviceVshg = 0.5; //Подключение устр. на ВШГ, мин/контакт

    private double cutCableHandlyMc6 = 0.25; //Резка многожильного кабеля Дн = 6мм, мин/рез
    private double cutCableHandlyMc15 = 0.4; //Резка многожильного кабеля Дн = 11..15мм, мин/рез
    private double cutCableHandlySc = 0.15; //Резка одножильного кабеля, мин/рез

    private double cutCableOnMachine = 0.025; //Резка кабеля на автомате, мин/рез
    private double cutMetalSleeve = 0.2; //Резка металлорукава, мин/рез
    private double cutCableChannel = 0.15; //Резка кабель-канала, дин-рейки, мин/рез

    private double tinningInBathe = 0.071; //Лужение в ванночке, мин/наконечник
    private double tinning = 0.074; //Лужение электропаяльником, мин/наконечник

    private double mountTipOnCable = 0.18; //Оконцовка провода наконечником, мин/наконечник
    private double mountTipOnPowerCable = 0.7; //Оконцовка силового кабеля наконечником, мин/наконечник

    private double marking = 0.3; //Скорость маркировки, мин/элемент

    private double soldering = 0.2; //Скорость пайки, мин/элемент

    private double mountOfSignalEquip = 1.3; //Скорость установки сигнальной аппаратуры, мин/элемент

    private double mountOfCableEntries = 0.15; //Скорость установки кабельных вводов, мин/элемент

    private double fixOfCables = 1.5; //Скорость укладки жгутов, мин/элемент (каждые 0,3 м)

    private double isolateWithThermTube10 = 0.3; //Изоляция термоусадочной трубкой 2-10 мм, мин/элемент
    private double isolateWithThermTube30 = 0.5; //Изоляция термоусадочной трубкой 10-30 мм, мин/элемент
}
