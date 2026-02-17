package ru.wert.normic.controllers.menus.formMenus;

import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Collections;

/**
 * Фабрика для создания электрического меню
 */
public class ElectricalMenuFactory implements MenuFactory {

    @Override
    public FormMenuManager createMenu(AbstractFormController controller) {
        FormMenuManager menu = new FormMenuManager(controller,
                controller.getListViewTechOperations(),
                (IOpWithOperations) controller.getOpData());

        menu.getItems().add(menu.createItemAssm());

        menu.getItems().add(new SeparatorMenuItem());
        //--------------------------------------------------------------------------------
        Menu mountOnDinMenu = new Menu("Установка на динрейку");
        mountOnDinMenu.getItems().add(menu.createItem_MountOnDinAutomats());
        mountOnDinMenu.getItems().add(menu.createItem_MountOnDinHeaters());
        menu.getItems().add(mountOnDinMenu);
        //--------------------------------------------------------------------------------
        Menu mountOnScrewsNoDisAssmMenu = new Menu("Установка на винты без разборки корпуса");
        mountOnScrewsNoDisAssmMenu.getItems().add(menu.createItem_MountOnScrewsNoDisAssm2());
        mountOnScrewsNoDisAssmMenu.getItems().add(menu.createItem_MountOnScrewsNoDisAssm4());
        menu.getItems().add(mountOnScrewsNoDisAssmMenu);
        //--------------------------------------------------------------------------------
        Menu mountOnScrewsWithDisAssmMenu = new Menu("Установка на винты c разборкой корпуса");
        mountOnScrewsWithDisAssmMenu.getItems().add(menu.createItem_MountOnScrewsWithDisAssm2());
        mountOnScrewsWithDisAssmMenu.getItems().add(menu.createItem_MountOnScrewsWithDisAssm4());
        menu.getItems().add(mountOnScrewsWithDisAssmMenu);
        //--------------------------------------------------------------------------------
        menu.getItems().add(menu.createItem_MountOnVSHG());
        menu.getItems().add(new SeparatorMenuItem());
        //================================================================================
        Menu connectDeviceMenu = new Menu("Подключение эелектроустройств");
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceMortiseContact());
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceSpringClamp());
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceClampingScrew());
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceVSHG());
        menu.getItems().add(connectDeviceMenu);
        //---------------------------------------------------------------------------------
        menu.getItems().add(menu.createItem_MountOfSignalEquip());
        menu.getItems().add(menu.createItem_MountOfCableEntries());
        //=============================================================================
        menu.getItems().add(new SeparatorMenuItem());
        //=============================================================================
        Menu cutCableHandly = new Menu("Резка кабеля вручную");
        cutCableHandly.getItems().add(menu.createItem_CutCableHandlyMC6()); //Многожильный 6мм
        cutCableHandly.getItems().add(menu.createItem_CutCableHandlyMC15());//Многожильный 11-15мм
        cutCableHandly.getItems().add(menu.createItem_CutCableHandlySC()); //Одножильный
        menu.getItems().add(cutCableHandly);
        //--------------------------------------------------------------------------------
        menu.getItems().add(menu.createItem_CutCableOnMachine());
        menu.getItems().add(menu.createItem_CutMetalSleeve());
        menu.getItems().add(menu.createItem_CutCableChannel());

        menu.getItems().add(new SeparatorMenuItem());
        //----------------------------------------------------------------------
        Menu tinning = new Menu("Лужение проводника");
        tinning.getItems().add(menu.createItem_Tinning());
        tinning.getItems().add(menu.createItem_TinningInBathe());
        menu.getItems().add(tinning);
        //----------------------------------------------------------------------
        menu.getItems().add(menu.createItem_Soldering());
        //======================================================================
        menu.getItems().add(new SeparatorMenuItem());
        //======================================================================
        menu.getItems().add(menu.createItem_MountTipOnCable());
        menu.getItems().add(menu.createItem_MountTipOnPowerCable());
        //----------------------------------------------------------------------
        Menu isolateWithThermotube = new Menu("Изоляция термоусадочной трубкой");
        isolateWithThermotube.getItems().add(menu.createItem_IsolateWithThermotube10());
        isolateWithThermotube.getItems().add(menu.createItem_IsolateWithThermotube30());
        menu.getItems().add(isolateWithThermotube);
        //----------------------------------------------------------------------
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItem_FixOfCables());
        menu.getItems().add(menu.createItem_Marking());
        //----------------------------------------------------------------------
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());
        //----------------------------------------------------------------------
        menu.getItems().add(new SeparatorMenuItem());
        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if (simpleOperationsMenu != null)
            menu.getItems().add(simpleOperationsMenu);

        return menu;
    }
}
