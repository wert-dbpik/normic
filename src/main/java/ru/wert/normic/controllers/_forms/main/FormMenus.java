package ru.wert.normic.controllers._forms.main;

import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Collections;
import java.util.NoSuchElementException;

public class FormMenus {

    public enum EMenuType {
        MAIN_TYPE,
        ASSM_TYPE,
        DETAIL_TYPE,
        ELECTRICAL_TYPE;
    }

    private final AbstractFormController controller;

    public FormMenus(AbstractFormController controller) {
        this.controller = controller;
    }

    public FormMenuManager create(EMenuType type){
        FormMenuManager menu = new FormMenuManager(controller, controller.getListViewTechOperations(), (IOpWithOperations) controller.getOpData());
        switch(type){
            case MAIN_TYPE : return createMainTypeMenu(menu);
            case ASSM_TYPE : return createAssmTypeMenu(menu);
            case DETAIL_TYPE : return createDetailTypeMenu(menu);
            case ELECTRICAL_TYPE : return createElectricalTypeMenu(menu);
        }
        throw new NoSuchElementException("Неожиданный тип меню");
    }

    /**
     * МЕНЮ ГЛАВНОЙ ФОРМЫ (MAIN_TYPE)
     */
    public FormMenuManager createMainTypeMenu(FormMenuManager menu){

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemWeldLongSeam());
        menu.getItems().addAll(menu.createItemWeldingDotted());
        menu.getItems().addAll(menu.createItemWeldDifficulty());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAssmNuts());
        menu.getItems().addAll(menu.createItemAssmNutsMK());
        menu.getItems().addAll(menu.createItemAssmCuttings());
        menu.getItems().addAll(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(menu.createItemThermoInsulation());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if(simpleOperationsMenu != null) {
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(simpleOperationsMenu);
        }

        return menu;
    }

    /**
     * МЕНЮ СБОРКИ (ASSM_TYPE)
     */
    public FormMenuManager createAssmTypeMenu(FormMenuManager menu){

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemWeldLongSeam());
        menu.getItems().add(menu.createItemWeldingDotted());
        menu.getItems().add(menu.createItemWeldDifficulty());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAssmNuts());
        menu.getItems().add(menu.createItemAssmNutsMK());
        menu.getItems().add(menu.createItemAssmCuttings());
        menu.getItems().add(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());
        menu.getItems().add(new SeparatorMenuItem());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if(simpleOperationsMenu != null)
            menu.getItems().add(simpleOperationsMenu);

        return menu;
    }

    /**
     * МЕНЮ ДЕТАЛИ
     */
    public FormMenuManager createDetailTypeMenu(FormMenuManager menu){
        return menu;
    }

    /**
     * МЕНЮ ЭЛЕКТРОМОНТАЖА (ELECTRICAL_TYPE)
     */
    public FormMenuManager createElectricalTypeMenu(FormMenuManager menu){
        menu.getItems().add(menu.createItemAssm());

        menu.getItems().add(new SeparatorMenuItem());
        //==============================================================================
        Menu mountOnDinMenu = new Menu("Установка на динрейку");
        mountOnDinMenu.getItems().add(menu.createItem_MountOnDinAutomats());
        mountOnDinMenu.getItems().add(menu.createItem_MountOnDinHeaters());
        menu.getItems().add(mountOnDinMenu);
        //===============================================================================
        menu.getItems().add(menu.createItem_MountOnScrewsNoDisAssm());
        menu.getItems().add(menu.createItem_MountOnScrewsWithDisAssm());
        menu.getItems().add(menu.createItem_MountOnVSHG());

        menu.getItems().add(new SeparatorMenuItem());
        //================================================================================
        Menu connectDeviceMenu = new Menu("Подключение эелектроустройства");
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceMortiseContact());
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceSpringClamp());
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceClampingScrew());
        connectDeviceMenu.getItems().add(menu.createItem_ConnectDeviceVSHG());
        menu.getItems().add(connectDeviceMenu);
        //=============================================================================
        menu.getItems().add(new SeparatorMenuItem());

        menu.getItems().add(menu.createItem_CutCableHandly());
        menu.getItems().add(menu.createItem_CutCableOnMachine());
        menu.getItems().add(menu.createItem_CutMetalSleeve());
        menu.getItems().add(menu.createItem_CutCableChannel());

        menu.getItems().add(new SeparatorMenuItem());

        menu.getItems().add(menu.createItem_Tinning());
        menu.getItems().add(menu.createItem_TinningInBathe());
        menu.getItems().add(menu.createItem_Soldering());

        menu.getItems().add(new SeparatorMenuItem());

        menu.getItems().add(menu.createItem_MountTipOnCable());
        menu.getItems().add(menu.createItem_MountTipOnPowerCable());
        menu.getItems().add(menu.createItem_IsolateWithThermotube10());
        menu.getItems().add(menu.createItem_IsolateWithThermotube30());

        menu.getItems().add(new SeparatorMenuItem());

        menu.getItems().add(menu.createItem_MountOfSignalEquip());
        menu.getItems().add(menu.createItem_MountOfCableEntries());
        menu.getItems().add(menu.createItem_FixOfCables());
        menu.getItems().add(menu.createItem_Marking());



        return menu;
    }
}
