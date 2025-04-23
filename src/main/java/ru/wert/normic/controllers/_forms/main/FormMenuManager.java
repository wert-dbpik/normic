package ru.wert.normic.controllers._forms.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.wert.normic.controllers.PlateEmptyController;
import ru.wert.normic.controllers.PlateErrorController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.controllers.assembling.*;
import ru.wert.normic.controllers.electricalOperations.*;
import ru.wert.normic.controllers.listOperations.PlateBendController;
import ru.wert.normic.controllers.listOperations.PlateCuttingController;
import ru.wert.normic.controllers.locksmith.*;
import ru.wert.normic.controllers.paint.PlatePaintDetailController;
import ru.wert.normic.controllers.simpleOperations.PlateSimpleOperationController;
import ru.wert.normic.controllers.packing.*;
import ru.wert.normic.controllers.paint.PlatePaintAssmController;
import ru.wert.normic.controllers.paint.PlatePaintOldController;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.controllers.singlePlates.PlatePackController;
import ru.wert.normic.controllers.turning.*;
import ru.wert.normic.controllers.welding.PlateWeldContinuousController;
import ru.wert.normic.controllers.welding.PlateWeldDifficultyController;
import ru.wert.normic.controllers.welding.PlateWeldDottedController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperationServiceImpl;
import ru.wert.normic.entities.ops.OpData;
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
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.entities.ops.opWelding.OpWeldDifficulty;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.entities.ops.simpleOperations.OpSimpleOperation;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.ECommands;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.operations.OperationsACCController;
import ru.wert.normic.operations.OperationsController;

import java.io.IOException;
import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * МЕНЮ ДОБАВЛЕНИЯ ОПЕРАЦИЙ
 */
public class FormMenuManager extends ContextMenu {

    private final AbstractFormController formController;
    private final ListView<VBox> listViewTechOperations;
    private final List<OpData> addedOperations;
    private final IOpWithOperations opData;

    /**
     * Create a new ContextMenu
     */
    public FormMenuManager(AbstractFormController formController, ListView<VBox> listViewTechOperations, IOpWithOperations opData) {
        this.formController = formController;
        this.listViewTechOperations = listViewTechOperations;
        this.opData = opData;
        this.addedOperations = formController.getAddedOperations();
    }

    //===========      ОТДЕЛЬНЫЕ ЭЛЕМЕНТЫ     =========================================

    //ДОБАВИТЬ ДЕТАЛЬ
    public MenuItem createItemDetail(){
        MenuItem addDetail = new MenuItem(EOpType.DETAIL.getOpName());
        addDetail.setOnAction(event -> {
            OpDetail opDetail = new OpDetail();
            addDetailPlate(opDetail);
        });
        return addDetail;
    }

    //ДОБАВИТЬ СБОРКУ
    public MenuItem createItemAssm(){
        MenuItem addAssm = new MenuItem(EOpType.ASSM.getOpName());
        addAssm.setOnAction(event -> {
            OpAssm opAssm = new OpAssm();
            addAssmPlate(opAssm);
        });
        return addAssm;
    }

    //ДОБАВИТЬ УПАКОВКУ
    public MenuItem createItemPack(){
        MenuItem addPack = new MenuItem(EOpType.PACK.getOpName());
        addPack.setOnAction(event -> {
            OpPack opPack = new OpPack();
            addPackPlate(opPack);
        });
        return addPack;
    }

    //===========      ОПЕРАЦИИ С ЛИСТОМ     =========================================

    //РАСКРОЙ И ЗАЧИСТКА
    public MenuItem createItemCutting(){
        MenuItem addCutting = new MenuItem(EOpType.CUTTING.getOpName());
        addCutting.setOnAction(event -> {
            if(isDuplicate(EOpType.CUTTING)) return ;
            addCattingPlate(new OpCutting());
        });
        return addCutting;
    }

    //ГИБКА
    public MenuItem createItemBending(){
        MenuItem addBending = new MenuItem(EOpType.BENDING.getOpName());
        addBending.setOnAction(event -> {
            if(isDuplicate(EOpType.BENDING)) return ;
            addBendingPlate(new OpBending());
        });
        return addBending;
    }

    //===========      ОКРАШИВАНИЕ     =========================================

    //ПОКРАСКА СТАРАЯ
    public MenuItem createItemPaintingOld(){
        MenuItem item = new MenuItem(EOpType.PAINTING.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINTING)) return ;
            addPaintOldPlate(new OpPaintOld());
        });
        return item;
    }

    //ПОКРАСКА ДЕТАЛИ
    public MenuItem createItemPaintDetail(){
        MenuItem item = new MenuItem(EOpType.PAINT_DETAIL.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINT_DETAIL)) return ;
            addPaintDetailPlate(new OpPaintDetail());
        });
        return item;
    }

    //ПОКРАСКА СБОРОЧНОЙ ЕДИНИЦЫ
    public MenuItem createItemPaintAssm(){
        MenuItem item = new MenuItem(EOpType.PAINT_ASSM.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINT_ASSM)) return ;
            addPaintAssmPlate(new OpPaintAssm());
        });
        return item;
    }

    //===========      СБОРОЧНЫЕ ОПЕРАЦИИ     =========================================

    //СБОРКА - РАСКРОЙНЫЙ МАТЕРИАЛ
    public MenuItem createItemAssmCuttings(){
        MenuItem item = new MenuItem(EOpType.ASSM_CUTTINGS.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_CUTTINGS)) return ;
            addAssmCuttingsPlate(new OpAssmCutting());
        });
        return item;
    }

    //СБОРКА СТАНДАРТНЫХ УЗЛОВ
    public MenuItem createItemAssmNodes(){
        MenuItem item = new MenuItem(EOpType.ASSM_NODES.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NODES)) return ;
            addAssmNodesPlate(new OpAssmNode());
        });
        return item;
    }

    //СБОРКА - КРЕПЕЖ
    public MenuItem createItemAssmNuts(){
        MenuItem item = new MenuItem(EOpType.ASSM_NUTS.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NUTS)) return ;
            addAssmNutsPlate(new OpAssmNut());
        });
        return item;
    }


    //НАНЕСЕНИЕ НАЛИВНОГО УПЛОТНИТЕЛЯ
    public MenuItem createItemLevelingSealer(){
        MenuItem item = new MenuItem(EOpType.LEVELING_SEALER.getOpName());
        item.setOnAction(event -> {
            addLevelingSealerPlate(new OpLevelingSealer());
        });
        return item;
    }

    //МОНТАЖ ТЕРМОИЗОЛЯЦИИ
    public MenuItem createItemThermoInsulation(){
        MenuItem item = new MenuItem(EOpType.THERMO_INSULATION.getOpName());
        item.setOnAction(event -> {
            addThermoInsulationPlate(new OpThermoInsulation());
        });
        return item;
    }

    //ОТРУБАНИЕ
    public MenuItem createItemAssmChopOff(){
        MenuItem item = new MenuItem(EOpType.ASSM_CHOP_OFF.getOpName());
        item.setOnAction(event -> {
            addAssmChopOffPlate(new OpAssmChopOff());
        });
        return item;
    }

    /**
     * ВСЕ СБОРОЧНЫЕ ОПЕРАЦИИ
     */
    public Menu createAllAssmOperations(){
        Menu menu = new Menu("СБ операции");
        menu.getItems().add(createItemAssmChopOff());

        return menu;
    }

    //===========      СЛЕСАРНЫЕ ОПЕРАЦИИ     =========================================

    //СЛЕСАРНЫЕ РАБОТЫ
    public MenuItem createItemLocksmith(){
        MenuItem addLocksmith = new MenuItem(EOpType.LOCKSMITH.getOpName());
        addLocksmith.setOnAction(event -> {
            if(isDuplicate(EOpType.LOCKSMITH)) return ;
            addLocksmithPlate(new OpLocksmith());
        });
        return addLocksmith;
    }


    //СБОРКА - КРЕПЕЖ МК
    public MenuItem createItemAssmNutsMK(){
        MenuItem item = new MenuItem(EOpType.ASSM_NUTS_MK.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NUTS_MK)) return ;
            addAssmNutsMKPlate(new OpAssmNutMK());
        });
        return item;
    }

    //ОТРУБАНИЕ
    public MenuItem createItemChopOff(){
        MenuItem item = new MenuItem(EOpType.CHOP_OFF.getOpName());
        item.setOnAction(event -> {
            addChopOffPlate(new OpChopOff());
        });
        return item;
    }

    //СВЕРЛЕНИЕ ОТВЕРСТИЯ ПО РАЗМЕТКЕ (многократное добавление)
    public MenuItem createItemDrillingByMarking(){
        MenuItem item = new MenuItem(EOpType.DRILLING_BY_MARKING.getOpName());
        item.setOnAction(event -> {
            addDrillingByMarkingPlate(new OpDrillingByMarking());
        });
        return item;
    }

    //ОТРЕЗАНИЕ НА ПИЛЕ
    public MenuItem createItemCutOffOnTheSaw(){
        MenuItem item = new MenuItem(EOpType.CUT_OFF_ON_SAW.getOpName());
        item.setOnAction(event -> {
            addCutOffOnTheSawPlate(new OpCutOffOnTheSaw());
        });
        return item;
    }

    /**
     * ВСЕ СЛЕСАРНЫЕ ОПЕРАЦИИ
     */
    public Menu createAllLocksmithOperations(){
        Menu menu = new Menu("МК: слесарные операции");
        menu.getItems().add(createItemLocksmith());
        menu.getItems().add(createItemAssmNutsMK());
        menu.getItems().add(createItemChopOff());
        menu.getItems().add(createItemDrillingByMarking());
        menu.getItems().add(createItemCutOffOnTheSaw());


        return menu;
    }

    //===========      ТОКАРНЫЕ ОПЕРАЦИИ     =========================================

    //УСТАНОВКА / СНЯТИЕ детали
    public MenuItem createItemMountDismount(){
        MenuItem item = new MenuItem(EOpType.LATHE_MOUNT_DISMOUNT.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.LATHE_CUT_OFF)) return ;
            addMountDismountPlate(new OpLatheMountDismount());
        });
        return item;
    }

    //ТОЧЕНИЕ / РАСТАЧИВАНИЕ (многократное добавление)
    public MenuItem createItemTurning(){
        MenuItem item = new MenuItem(EOpType.LATHE_TURNING.getOpName());
        item.setOnAction(event -> {
            addTurningPlate(new OpLatheTurning());
        });
        return item;
    }

    //ПРОРЕЗАНИЕ ПАЗА (многократное добавление)
    public MenuItem createItemCutGroove(){
        MenuItem item = new MenuItem(EOpType.LATHE_CUT_GROOVE.getOpName());
        item.setOnAction(event -> {
            addCutGroovePlate(new OpLatheCutGroove());
        });
        return item;
    }

    //НАРЕЗАНИЕ РЕЗЬБЫ (многократное добавление)
    public MenuItem createItemThreading(){
        MenuItem item = new MenuItem(EOpType.LATHE_THREADING.getOpName());
        item.setOnAction(event -> {
            addThreadingPlate(new OpLatheThreading());
        });
        return item;
    }

    //СВЕРЛЕНИЕ ОТВЕРСТИЯ (многократное добавление)
    public MenuItem createItemDrilling(){
        MenuItem item = new MenuItem(EOpType.LATHE_DRILLING.getOpName());
        item.setOnAction(event -> {
            addDrillingPlate(new OpLatheDrilling());
        });
        return item;
    }

    //НАКАТЫВАНИЕ РИФЛЕНИЯ (многократное добавление)
    public MenuItem createItemRolling(){
        MenuItem item = new MenuItem(EOpType.LATHE_ROLLING.getOpName());
        item.setOnAction(event -> {
            addRollingPlate(new OpLatheRolling());
        });
        return item;
    }

    //ОТРЕЗАНИЕ
    public MenuItem createItemCutOff(){
        MenuItem item = new MenuItem(EOpType.LATHE_CUT_OFF.getOpName());
        item.setOnAction(event -> {
            addCutOffPlate(new OpLatheCutOff());
        });
        return item;
    }

    /**
     * ВСЕ ТОКАРНЫЕ ОПЕРАЦИИ
     */
    public Menu createAllLatheOperations(){
        Menu menu = new Menu("МК: токарные операции");
        menu.getItems().add(createItemMountDismount());
        menu.getItems().add(createItemTurning());
        menu.getItems().add(createItemCutGroove());
        menu.getItems().add(createItemThreading());
        menu.getItems().add(createItemDrilling());
        menu.getItems().add(createItemRolling());
        menu.getItems().add(createItemCutOff());

        return menu;
    }

    //===========      СВАРОЧНЫЕ ОПЕРАЦИИ     =========================================

    //СВАРКА НЕПРЕРЫВНАЯ
    public MenuItem createItemWeldLongSeam(){
        MenuItem item = new MenuItem(EOpType.WELD_CONTINUOUS.getOpName());
        item.setOnAction(event -> {
            addWeldContinuousPlate(new OpWeldContinuous());
        });
        return item;
    }

    //СЛОЖНОСТЬ НЕПРЕРЫВНОЙ СВАРКИ
    public MenuItem createItemWeldDifficulty(){
        MenuItem item = new MenuItem(EOpType.WELD_DIFFICULTY.getOpName());
        item.setOnAction(event -> {
            addWeldDifficultyPlate(new OpWeldDifficulty());
        });
        return item;
    }

    //СВАРКА ТОЧЕЧНАЯ
    public MenuItem createItemWeldingDotted(){
        MenuItem item = new MenuItem(EOpType.WELD_DOTTED.getOpName());
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.WELD_DOTTED)) return ;
            addWeldDottedPlate(new OpWeldDotted());
        });
        return item;
    }

    /**
     * ВСЕ СВАРОЧНЫЕ ОПЕРАЦИИ
     */
    public Menu createAllWeldingOperations(){
        Menu menu = new Menu("МК: сварочные операции");
        menu.getItems().add(createItemWeldLongSeam());
        menu.getItems().add(createItemWeldingDotted());

        return menu;
    }

    //===========      УПАКОВКА     =========================================

    //КРЕПЛЕНИЕ К ПОДДОНУ
    public MenuItem createItemPackOnPallet(){
        MenuItem item = new MenuItem(EOpType.PACK_ON_PALLET.getOpName());
        item.setOnAction(event -> {
            addPackOnPalletPlate(new OpPackOnPallet());
        });
        return item;
    }

    //УПАКОВКА В МАШИННУЮ СТРЕЙЧ-ПЛЕНКУ
    public MenuItem createItemPackOnPalletizer(){
        MenuItem item = new MenuItem(EOpType.PACK_IN_MACHINE_STRETCH_WRAP.getOpName());
        item.setOnAction(event -> {
            addPackInMachineStretchWrapPlate(new OpPackInMachineStretchWrap());
        });
        return item;
    }

    //УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    public MenuItem createItemPackInHandStretchWrap(){
        MenuItem item = new MenuItem(EOpType.PACK_IN_HAND_STRETCH_WRAP.getOpName());
        item.setOnAction(event -> {
            addPackInHandStretchWrapPlate(new OpPackInHandStretchWrap());
        });
        return item;
    }

    //КРЕПЛЕНИЕ В КАРТОННУЮ КОРОБКУ
    public MenuItem createItemPackInCartoonBox(){
        MenuItem item = new MenuItem(EOpType.PACK_IN_CARTOON_BOX.getOpName());
        item.setOnAction(event -> {
            addPackInCartoonBoxPlate(new OpPackInCartoonBox());
        });
        return item;
    }

    //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ
    public MenuItem createItemPackInBubbleWrap(){
        MenuItem item = new MenuItem(EOpType.PACK_IN_BUBBLE_WRAP.getOpName());
        item.setOnAction(event -> {
            addPackInBubbleWrapPlate(new OpPackInBubbleWrap());
        });
        return item;
    }

    //===========      ПРОЧИЕ     =========================================

    //ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ
    public MenuItem createItemSimpleOperation(SimpleOperation operation){
        MenuItem item = new MenuItem(operation.getName());
        item.setOnAction(event -> {
            addSimpleOtherPlate(new OpSimpleOperation(operation));
        });
        return item;
    }


    /**
     * ВСЕ ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ
     */
    public Menu createAllSimpleOperations(List<ENormType> normTypes){
        Menu menu = new Menu("Дополнительные операции");
        List<SimpleOperation> ops = SimpleOperationServiceImpl.getInstance().findAll();
        if(ops == null) return null;
        for(SimpleOperation op : ops){
            for(ENormType nt : normTypes)
                if(op.getNormType().equals(nt))
                    menu.getItems().add(createItemSimpleOperation(op));
        }
        MenuItem itemCreateNew = new MenuItem("Создать операцию");
        itemCreateNew.setOnAction(e->{

            Stage owner = (Stage) ((MenuItem)e.getSource()).getParentMenu().getParentPopup().getOwnerWindow();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operations/operationsACC.fxml"));
                Parent parent = loader.load();
                OperationsACCController controller = loader.getController();
                controller.init(normTypes.get(0), new OperationsController(), null, ECommands.ADD);

                new Decoration("ДОБАВИТЬ ОПЕРАЦИЮ",
                        parent,
                        false,
                        owner,
                        "decoration-settings",
                        false,
                        true);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(itemCreateNew);


        return menu;
    }


    //===========      ОСТАЛЬНОЕ     =========================================


    //ДОБАВИТЬ ФАЙЛ
    public MenuItem createItemAddFilePallet(){
        MenuItem item = new MenuItem("Добавить файл");
        item.setOnAction(e->formController.open(e, EMenuSource.FORM_MENU));
        return item;
    }

    //НАЙТИ ФАЙЛ
    public MenuItem createItemSearchFilePallet(){
        MenuItem item = new MenuItem("Найти файл");
        item.setOnAction(e->formController.search(e, EMenuSource.FORM_MENU));
        return item;
    }

    //===========      ЭЛЕКТРОМОНТАЖ     =========================================

    //МОНТАЖ НА ДИНРЕЙКУ
    public MenuItem createItem_MountOnDin(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_ON_DIN.getOpName());
        item.setOnAction(event -> {
            add_MountOnDin_Plate(new OpMountOnDin());
        });
        return item;
    }

    //МОНТАЖ НА ВИНТЫ БЕЗ РАЗБОРКИ КОРПУСА
    public MenuItem createItem_MountOnScrewsNoDisAssm(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_ON_SCREWS_NO_DISASSM.getOpName());
        item.setOnAction(event -> {
            add_MountOnScrewsNoDisAssm_Plate(new OpMountOnScrewsNoDisAssm());
        });
        return item;
    }

    //МОНТАЖ НА ВИНТЫ С РАЗБОРКОЙ КОРПУСА
    public MenuItem createItem_MountOnScrewsWithDisAssm(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_ON_SCREWS_WITH_DISASSM.getOpName());
        item.setOnAction(event -> {
            add_MountOnScrewsWithDisAssm_Plate(new OpMountOnScrewsWithDisAssm());
        });
        return item;
    }

    //МОНТАЖ НА ВШГ(4 шт)
    public MenuItem createItem_MountOnVSHG(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_ON_VSHG.getOpName());
        item.setOnAction(event -> {
            add_MountOnVSHG_Plate(new OpMountOnVSHG());
        });
        return item;
    }

    //ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВРЕЗНОЙ КОНТАКТ
    public MenuItem createItem_ConnectDeviceMortiseContact(){
        MenuItem item = new MenuItem(EOpType.EL_CONNECT_DEVICE_MORTISE_CONTACT.getOpName());
        item.setOnAction(event -> {
            add_ConnectDeviceMortiseContact_Plate(new OpConnectDeviceMortiseContact());
        });
        return item;
    }

    //ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ПРУЖИННЫЙ ЗАЖИМ
    public MenuItem createItem_ConnectDeviceSpringClamp(){
        MenuItem item = new MenuItem(EOpType.EL_CONNECT_DEVICE_SPRING_CLAMP.getOpName());
        item.setOnAction(event -> {
            add_ConnectDeviceSpringClamp_Plate(new OpConnectDeviceSpringClamp());
        });
        return item;
    }

    //ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ЗАЖИМНОЙ ВИНТ
    public MenuItem createItem_ConnectDeviceClampingScrew(){
        MenuItem item = new MenuItem(EOpType.EL_CONNECT_DEVICE_CLAMPING_SCREW.getOpName());
        item.setOnAction(event -> {
            add_ConnectDeviceClampingScrew_Plate(new OpConnectDeviceClampingScrew());
        });
        return item;
    }

    //ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВШГ
    public MenuItem createItem_ConnectDeviceVSHG(){
        MenuItem item = new MenuItem(EOpType.EL_CONNECT_DEVICE_VSHG.getOpName());
        item.setOnAction(event -> {
            add_ConnectDeviceVSHG_Plate(new OpConnectDeviceVSHG());
        });
        return item;
    }

    //РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ
    public MenuItem createItem_CutCableHandly(){
        MenuItem item = new MenuItem(EOpType.EL_CUT_CABLE_HANDLY.getOpName());
        item.setOnAction(event -> {
            add_CutCableHandly_Plate(new OpCutCableHandly());
        });
        return item;
    }

    //РЕЗКА КАБЕЛЯ НА АВТОМАТЕ
    public MenuItem createItem_CutCableOnMachine(){
        MenuItem item = new MenuItem(EOpType.EL_CUT_CABLE_ON_MACHINE.getOpName());
        item.setOnAction(event -> {
            add_CutCableOnMachine_Plate(new OpCutCableOnMachine());
        });
        return item;
    }

    //РЕЗКА МЕТАЛЛОРУКАВА
    public MenuItem createItem_CutMetalSleeve(){
        MenuItem item = new MenuItem(EOpType.EL_CUT_METAL_SLEEVE.getOpName());
        item.setOnAction(event -> {
            add_CutMetalSleeve_Plate(new OpCutMetalSleeve());
        });
        return item;
    }

    //РЕЗКА КАБЕЛЬ-КАНАЛА, ДИНРЕЙКИ
    public MenuItem createItem_CutCableChannel(){
        MenuItem item = new MenuItem(EOpType.EL_CUT_CABLE_CHANNEL.getOpName());
        item.setOnAction(event -> {
            add_CutCableChannel_Plate(new OpCutCableChannel());
        });
        return item;
    }

    //ЛУЖЕНИЕ В ВАННОЧКЕ
    public MenuItem createItem_TinningInBathe(){
        MenuItem item = new MenuItem(EOpType.EL_TINNING_IN_BATHE.getOpName());
        item.setOnAction(event -> {
            add_TinningInBathe_Plate(new OpTinningInBathe());
        });
        return item;
    }

    //ЛУЖЕНИЕ ЭЛЕКТРОПАЯЛЬНИКОМ
    public MenuItem createItem_Tinning(){
        MenuItem item = new MenuItem(EOpType.EL_TINNING.getOpName());
        item.setOnAction(event -> {
            add_Tinning_Plate(new OpTinning());
        });
        return item;
    }

    //ОКОНЦОВКА ПРОВОДА
    public MenuItem createItem_MountTipOnCable(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_TIP_ON_CABLE.getOpName());
        item.setOnAction(event -> {
            add_MountTipOnCable_Plate(new OpMountTipOnCable());
        });
        return item;
    }

    //ОКОНЦОВКА СИЛОВОГО КАБЕЛЯ
    public MenuItem createItem_MountTipOnPowerCable(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_TIP_ON_POWER_CABLE.getOpName());
        item.setOnAction(event -> {
            add_MountTipOnPowerCable_Plate(new OpMountTipOnPowerCable());
        });
        return item;
    }

    //МАРКИРОВКА
    public MenuItem createItem_Marking(){
        MenuItem item = new MenuItem(EOpType.EL_MARKING.getOpName());
        item.setOnAction(event -> {
            add_Marking_Plate(new OpMarking());
        });
        return item;
    }

    //УСТАНОВКА СИГНАЛЬНОЙ АППАРАТУРЫ
    public MenuItem createItem_MountOfSignalEquip(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_OF_SIGNAL_EQUIP.getOpName());
        item.setOnAction(event -> {
            add_MountOfSignalEquip_Plate(new OpMountOfSignalEquip());
        });
        return item;
    }

    //СОЕДИНЕНИЕ ЭЛЕМЕНТОВ ПАЙКОЙ
    public MenuItem createItem_Soldering(){
        MenuItem item = new MenuItem(EOpType.EL_SOLDERING.getOpName());
        item.setOnAction(event -> {
            add_Soldering_Plate(new OpSoldering());
        });
        return item;
    }

    //УСТАНОВКА КАБЕЛЬНЫХ ВВОДОВ
    public MenuItem createItem_MountOfCableEntries(){
        MenuItem item = new MenuItem(EOpType.EL_MOUNT_OF_CABLE_ENTRIES.getOpName());
        item.setOnAction(event -> {
            add_MountOfCableEntries_Plate(new OpMountOfCableEntries());
        });
        return item;
    }

    //УКЛАДКА ЖГУТОВ
    public MenuItem createItem_FixOfCables(){
        MenuItem item = new MenuItem(EOpType.EL_FIX_OF_CABLES.getOpName());
        item.setOnAction(event -> {
            add_FixOfCables_Plate(new OpFixOfCables());
        });
        return item;
    }

    //ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 2-10 ММ
    public MenuItem createItem_IsolateWithThermotube10(){
        MenuItem item = new MenuItem(EOpType.EL_ISOLATE_WITH_THERM_TUBE10.getOpName());
        item.setOnAction(event -> {
            add_IsolateWithThermotube10_Plate(new OpIsolateWithThermTube10());
        });
        return item;
    }

    //ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 10-30 ММ
    public MenuItem createItem_IsolateWithThermotube30(){
        MenuItem item = new MenuItem(EOpType.EL_ISOLATE_WITH_THERM_TUBE30.getOpName());
        item.setOnAction(event -> {
            add_IsolateWithThermotube30_Plate(new OpIsolateWithThermTube30());
        });
        return item;
    }


//*****************************************************************************************************************
//              МЕТОДЫ
//*****************************************************************************************************************
    /**
     * Ищем дубликат операции в списке addedOperations по clazz
     */
    private boolean isDuplicate(EOpType opType){
        for(OpData cn: addedOperations){
            if(cn.getOpType().equals(opType))
                return true;
        }
        return false;
    }



    /*==================================================================================================================
     *                                         В О С С Т А Н О В Л Е Н И Е
     * ==================================================================================================================*/
    public void addListOfOperations() {
        addEmptyPlate();
        List<OpData> operations = ((IOpWithOperations)opData).getOperations();
        for (OpData op : operations) {
            addPlateToForm(op);
        }
        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    public void addPlateToForm(OpData op) {
        switch (op.getOpType()) {
            case DETAIL:
                addDetailPlate((OpDetail) op);
                break;
            case ASSM:
                addAssmPlate((OpAssm) op);
                break;
            case PACK:
                addPackPlate((OpPack) op);
                break;
            case CUTTING:
                addCattingPlate((OpCutting) op);
                break;
            case BENDING:
                addBendingPlate((OpBending) op);
                break;
            case LOCKSMITH:
                addLocksmithPlate((OpLocksmith) op);
                break;
            case LATHE_TURNING:
                addTurningPlate((OpLatheTurning) op);
                break;
            case LATHE_CUT_GROOVE:
                addCutGroovePlate((OpLatheCutGroove) op);
                break;
            case LATHE_THREADING:
                addThreadingPlate((OpLatheThreading) op);
                break;
            case LATHE_DRILLING:
                addDrillingPlate((OpLatheDrilling) op);
                break;
            case DRILLING_BY_MARKING:
                addDrillingByMarkingPlate((OpDrillingByMarking) op);
                break;
            case LATHE_ROLLING:
                addRollingPlate((OpLatheRolling) op);
                break;
            case LATHE_CUT_OFF:
                addCutOffPlate((OpLatheCutOff) op);
                break;
            case CUT_OFF_ON_SAW:
                addCutOffOnTheSawPlate((OpCutOffOnTheSaw) op);
                break;
            case CHOP_OFF:
                addChopOffPlate((OpChopOff) op);
                break;
            case LATHE_MOUNT_DISMOUNT:
                addMountDismountPlate((OpLatheMountDismount) op);
                break;
            case PAINTING:
                addPaintOldPlate((OpPaintOld) op);
                break;
            case PAINT_DETAIL:
                addPaintDetailPlate((OpPaintDetail) op);
                break;
            case PAINT_ASSM:
                addPaintAssmPlate((OpPaintAssm) op);
                break;
            case WELD_CONTINUOUS:
                addWeldContinuousPlate((OpWeldContinuous) op);
                break;
            case WELD_DIFFICULTY:
                addWeldDifficultyPlate((OpWeldDifficulty) op);
                break;
            case WELD_DOTTED:
                addWeldDottedPlate((OpWeldDotted) op);
                break;
            case ASSM_CUTTINGS:
                addAssmCuttingsPlate((OpAssmCutting) op);
                break;
            case ASSM_NUTS:
                addAssmNutsPlate((OpAssmNut) op);
                break;
            case ASSM_NUTS_MK:
                addAssmNutsMKPlate((OpAssmNutMK) op);
                break;
            case ASSM_NODES:
                addAssmNodesPlate((OpAssmNode) op);
                break;
            case LEVELING_SEALER:
                addLevelingSealerPlate((OpLevelingSealer) op);
                break;
            case THERMO_INSULATION:
                addThermoInsulationPlate((OpThermoInsulation) op);
                break;
            case ASSM_CHOP_OFF:
                addAssmChopOffPlate((OpAssmChopOff) op);
                break;
            case PACK_IN_CARTOON_BOX:
                addPackInCartoonBoxPlate((OpPackInCartoonBox) op);
                break;
            case PACK_IN_MACHINE_STRETCH_WRAP:
                addPackInMachineStretchWrapPlate((OpPackInMachineStretchWrap) op);
                break;
            case PACK_IN_HAND_STRETCH_WRAP:
                addPackInHandStretchWrapPlate((OpPackInHandStretchWrap) op);
                break;
            case PACK_IN_BUBBLE_WRAP:
                addPackInBubbleWrapPlate((OpPackInBubbleWrap) op);
                break;
            case PACK_ON_PALLET:
                addPackOnPalletPlate((OpPackOnPallet) op);
                break;
            case SIMPLE_OPERATION:
                addSimpleOtherPlate((OpSimpleOperation) op);
                break;
            case ERROR_OP_DATA:
                addErrorPlate((OpErrorData) op);
                break;

            //ЭЛЕКТРОМОНТАЖ =============================================================
            case EL_MOUNT_ON_DIN:
                add_MountOnDin_Plate((OpMountOnDin) op);
                break;
            case EL_MOUNT_ON_SCREWS_NO_DISASSM:
                add_MountOnScrewsNoDisAssm_Plate((OpMountOnScrewsNoDisAssm) op);
                break;
            case EL_MOUNT_ON_SCREWS_WITH_DISASSM:
                add_MountOnScrewsWithDisAssm_Plate((OpMountOnScrewsWithDisAssm) op);
                break;
            case EL_MOUNT_ON_VSHG:
                add_MountOnVSHG_Plate((OpMountOnVSHG) op);
                break;
            case EL_CONNECT_DEVICE_MORTISE_CONTACT:
                add_ConnectDeviceMortiseContact_Plate((OpConnectDeviceMortiseContact) op);
                break;
            case EL_CONNECT_DEVICE_SPRING_CLAMP:
                add_ConnectDeviceSpringClamp_Plate((OpConnectDeviceSpringClamp) op);
                break;
            case EL_CONNECT_DEVICE_CLAMPING_SCREW:
                add_ConnectDeviceClampingScrew_Plate((OpConnectDeviceClampingScrew) op);
                break;
            case EL_CONNECT_DEVICE_VSHG :
                add_ConnectDeviceVSHG_Plate((OpConnectDeviceVSHG) op);
                break;
            case EL_CUT_CABLE_HANDLY:
                add_CutCableHandly_Plate((OpCutCableHandly) op);
                break;
            case EL_CUT_CABLE_ON_MACHINE:
                add_CutCableOnMachine_Plate((OpCutCableOnMachine) op);
                break;
            case EL_CUT_METAL_SLEEVE:
                add_CutMetalSleeve_Plate((OpCutMetalSleeve) op);
                break;
            case EL_CUT_CABLE_CHANNEL:
                add_CutCableChannel_Plate((OpCutCableChannel) op);
                break;
            case EL_TINNING_IN_BATHE:
                add_TinningInBathe_Plate((OpTinningInBathe) op);
                break;
            case EL_TINNING:
                add_Tinning_Plate((OpTinning) op);
                break;
            case EL_MOUNT_TIP_ON_CABLE:
                add_MountTipOnCable_Plate((OpMountTipOnCable) op);
                break;
            case EL_MOUNT_TIP_ON_POWER_CABLE:
                add_MountTipOnPowerCable_Plate((OpMountTipOnPowerCable) op);
                break;
            case EL_MARKING:
                add_Marking_Plate((OpMarking) op);
                break;
            case EL_MOUNT_OF_SIGNAL_EQUIP:
                add_MountOfSignalEquip_Plate((OpMountOfSignalEquip) op);
                break;
            case EL_SOLDERING:
                add_Soldering_Plate((OpSoldering) op);
                break;
            case EL_MOUNT_OF_CABLE_ENTRIES:
                add_MountOfCableEntries_Plate((OpMountOfCableEntries) op);
                break;
            case EL_FIX_OF_CABLES:
                add_FixOfCables_Plate((OpFixOfCables) op);
                break;
            case EL_ISOLATE_WITH_THERM_TUBE10:
                add_IsolateWithThermotube10_Plate((OpIsolateWithThermTube10) op);
                break;
            case EL_ISOLATE_WITH_THERM_TUBE30:
                add_IsolateWithThermotube30_Plate((OpIsolateWithThermTube30) op);
                break;

        }
    }


    /*==================================================================================================================
    *                                                М Е Т О Д Ы
    * ==================================================================================================================*/

    /**
     * ДОБАВЛЕНИЕ ДЕТАЛИ
     */
    public void addDetailPlate(OpDetail opData) {
        try {
            int index = 0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/single/plateDetail.fxml"));
            VBox box = loader.load();
            PlateDetailController controller = loader.getController();
            for (OpData op : addedOperations) {
                if (op instanceof OpDetail) index ++;
                else break;
            }

            controller.init(formController, opData, index);
            listViewTechOperations.getItems().add(index, box);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ДОБАВЛЕНИЕ СБОРКИ
     */
    public void addAssmPlate(OpAssm opData) {
        try {
            int index = 0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/single/plateAssm.fxml"));
            VBox box = loader.load();
            PlateAssmController controller = loader.getController();
            for (OpData op : addedOperations) {
                if (op instanceof OpDetail || op instanceof OpAssm) index++;
                else break;
            }
            controller.init(formController, opData, index);
            listViewTechOperations.getItems().add(index, box);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ДОБАВЛЕНИЕ УПАКОВКИ
     */
    public void addPackPlate(OpPack opData) {
        try {
            int index = 0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/single/platePack.fxml"));
            VBox box = loader.load();
            PlatePackController controller = loader.getController();
            for (OpData op : addedOperations) {
                if (op instanceof OpDetail || op instanceof OpAssm || op instanceof OpPack) index ++;
                else break;
            }

            controller.init(formController, opData, index);
            listViewTechOperations.getItems().add(index, box);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * РАСКРОЙ И ЗАЧИСТКА
     */
    public void addCattingPlate(OpCutting opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/list/plateCutting.fxml"));
            VBox vBox = loader.load();
            PlateCuttingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "РАСКРОЙ И ЗАЧИСТКА");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * ГИБКА
     */
    public void addBendingPlate(OpBending opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/list/plateBend.fxml"));
            VBox vBox = loader.load();
            PlateBendController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ГИБКА");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СЛЕСАРНЫЕ ОПЕРАЦИИ
     */
    public void addLocksmithPlate(OpLocksmith opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/locksmith/plateLocksmith.fxml"));
            VBox vBox = loader.load();
            PlateLocksmithController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СЛЕСАРНЫЕ ОПЕРАЦИИ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //======================  ТОКАРНЫЕ ОПЕРАЦИИ   ==============================================================
    /**
     * ТОЧЕНИЕ/РАСТАЧИВАНИЕ
     */
    public void addTurningPlate(OpLatheTurning opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateTurning.fxml"));
            VBox vBox = loader.load();
            PlateLatheTurningController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ТОЧЕНИЕ ИЛИ РАСТАЧИВАНИЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ОТРЕЗАНИЕ
     */
    public void addCutOffPlate(OpLatheCutOff opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateCutOff.fxml"));
            VBox vBox = loader.load();
            PlateLatheCutOffController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ОТРЕЗАНИЕ НА ПИЛЕ
     */
    public void addCutOffOnTheSawPlate(OpCutOffOnTheSaw opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/locksmith/plateCutOffOnTheSaw.fxml"));
            VBox vBox = loader.load();
            PlateCutOffOnTheSawController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОТРЕЗАНИЕ НА ПИЛЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ОТРУБАНИЕ
     */
    public void addChopOffPlate(OpChopOff opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/locksmith/plateChopOff.fxml"));
            VBox vBox = loader.load();
            PlateChopOffController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОТРУБАНИЕ НА ГЕКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ТОЧЕНИЕ ПАЗА
     */
    public void addCutGroovePlate(OpLatheCutGroove opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateCutGroove.fxml"));
            VBox vBox = loader.load();
            PlateLatheCutGrooveController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ТОЧЕНИЕ ПАЗА НА ТОКАРНОМ СТАНКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * НАРЕЗАНИЕ РЕЗЬБЫ
     */
    public void addThreadingPlate(OpLatheThreading opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateThreading.fxml"));
            VBox vBox = loader.load();
            PlateLatheThreadingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СВЕРЛЕНИЕ ОТВЕРСТИЯ
     */
    public void addDrillingPlate(OpLatheDrilling opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateDrilling.fxml"));
            VBox vBox = loader.load();
            PlateLatheDrillingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СВЕРЛЕНИЕ ОТВЕРСТИЙ ПО РАЗМЕТКЕ
     */
    public void addDrillingByMarkingPlate(OpDrillingByMarking opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/locksmith/plateDrillingByMarking.fxml"));
            VBox vBox = loader.load();
            PlateDrillingByMarkingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СВЕРЛЕНИЕ ОТВЕРСТИЙ ПО РАЗМЕТКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * НАКАТЫВАНИЕ РИФЛЕНИЯ
     */
    public void addRollingPlate(OpLatheRolling opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateRolling.fxml"));
            VBox vBox = loader.load();
            PlateLatheRollingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "НАКАТЫВАНИЕ РИФЛЕНИЯ НА ТОКАРНОМ СТАНКЕ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА/СНЯТИЕ детали
     */
    public void addMountDismountPlate(OpLatheMountDismount opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateMountDismount.fxml"));
            VBox vBox = loader.load();
            PlateLatheMountDismountController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА И СНЯТИЕ ДЕТАЛИ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //==================================================================================================================

    /**
     * ПОКРАСКА ДЕТАЛИ
     */
    public void addPaintOldPlate(OpPaintOld opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/paint/platePaintOld.fxml"));
            VBox vBox = loader.load();
            PlatePaintOldController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОКРАСКА ДЕТАЛИ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПОКРАСКА ДЕТАЛИ
     */
    public void addPaintDetailPlate(OpPaintDetail opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/paint/platePaintDetail.fxml"));
            VBox vBox = loader.load();
            PlatePaintDetailController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОКРАСКА ДЕТАЛИ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПОКРАСКА СБОРОЧНОЙ ЕДИНИЦЫ
     */
    public void addPaintAssmPlate(OpPaintAssm opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/paint/platePaintAssm.fxml"));
            VBox vBox = loader.load();
            PlatePaintAssmController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОКРАСКА СБОРОЧНОЙ ЕДИНИЦЫ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    /**
     * СВАРКА НЕПРЕРЫВНАЯ
     */
    public void addWeldContinuousPlate(OpWeldContinuous opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/welding/plateWeldContinuous.fxml"));
            VBox vBox = loader.load();
            PlateWeldContinuousController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СВАРКА НЕПРЕРЫВНАЯ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СЛОЖНОСТЬ СВАРИВАЕМОЙ СБОРКИ
     */
    public void addWeldDifficultyPlate(OpWeldDifficulty opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/welding/plateWeldDifficulty.fxml"));
            VBox vBox = loader.load();
            PlateWeldDifficultyController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СЛОЖНОСТЬ СВАРИВАЕМОЙ СБОРКИ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СВАРКА ТОЧЕЧНАЯ
     */
    public void addWeldDottedPlate(OpWeldDotted opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/welding/plateWeldDotted.fxml"));
            VBox vBox = loader.load();
            PlateWeldDottedController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СВАРКА ТОЧЕЧНАЯ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    /**
     * СБОРКА КРЕПЕЖА
     */
    public void addAssmNutsPlate(OpAssmNut opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateAssmNuts.fxml"));
            VBox vBox = loader.load();
            PlateAssmNutsController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СБОРКА КРЕПЕЖА");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СБОРКА КРЕПЕЖА
     */
    public void addAssmNutsMKPlate(OpAssmNutMK opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/locksmith/plateAssmNutsMK.fxml"));
            VBox vBox = loader.load();
            PlateAssmNutsMKController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "КРЕПЕЖ (УЧАСТОК МК)");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СБОРКА РАСКРОЙНОГО МАТЕРИАЛА
     */
    public void addAssmCuttingsPlate(OpAssmCutting opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateAssmCuttings.fxml"));
            VBox vBox = loader.load();
            PlateAssmCuttingsController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СБОРКА РАСКРОЙНОГО МАТЕРИАЛА");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СБОРКА СТАНДАРТНЫХ УЗЛОВ
     */
    public void addAssmNodesPlate(OpAssmNode opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateAssmNodes.fxml"));
            VBox vBox = loader.load();
            PlateAssmNodesController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СБОРКА СТАНДАРТНЫХ УЗЛОВ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * РУБКА НА УЧАСТКЕ СБОРКИ
     */
    public void addAssmChopOffPlate(OpAssmChopOff opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateAssmChopOff.fxml"));
            VBox vBox = loader.load();
            PlateAssmChopOffController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОТРУБАНИЕ НА СБОРКЕ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    /**
     * НАНЕСЕНИЕ НАЛИВНОГО УПЛОТНИТЕЛЯ
     */
    public void addLevelingSealerPlate(OpLevelingSealer opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateLevelingSealer.fxml"));
            VBox vBox = loader.load();
            PlateLevelingSealerController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "НАНЕСЕНИЕ НАЛИВНОГО УПЛОТНИТЕЛЯ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    /**
     * МОНТАЖ ТЕРМОИЗОЛЯЦИИ
     */
    public void addThermoInsulationPlate(OpThermoInsulation opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateThermoInsulation.fxml"));
            VBox vBox = loader.load();
            PlateThermoInsulationController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "МОНТАЖ ТЕРМОИЗОЛЯЦИИ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //===========================================  УПАКОВКА    ===================================================

    /**
     * УПАКОВКА В КАРТОННУЮ КОРОБКУ
     */
    public void addPackInCartoonBoxPlate(OpPackInCartoonBox opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/pack/platePackInCartoonBox.fxml"));
            VBox vBox = loader.load();
            PlatePackInCartoonBoxController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УПАКОВКА В КАРТОННУЮ КОРОБКУ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УПАКОВКА В МАШИННУЮ СТРЕЙЧ-ПЛЕНКУ
     */
    public void addPackInMachineStretchWrapPlate(OpPackInMachineStretchWrap opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/pack/platePackInMachineStretchWrap.fxml"));
            VBox vBox = loader.load();
            PlatePackInMachineStretchWrapController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УПАКОВКА В МАШИННУЮ СТРЕЙЧ-ПЛЕНКУ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УПАКОВКА В РУЧНУЮ СТРЕЙЧ_ПЛЕНКУ
     */
    public void addPackInHandStretchWrapPlate(OpPackInHandStretchWrap opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/pack/platePackInHandStretchWrap.fxml"));
            VBox vBox = loader.load();
            PlatePackInHandStretchWrapController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УПАКОВКА В РУЧНУЮ СТРЕЙЧ_ПЛЕНКУ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ
     */
    public void addPackInBubbleWrapPlate(OpPackInBubbleWrap opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/pack/platePackInBubbleWrap.fxml"));
            VBox vBox = loader.load();
            PlatePackInBubbleController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ
     */
    public void addSimpleOtherPlate(OpSimpleOperation opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/simpleOperations/plateSimpleOperation.fxml"));
            VBox vBox = loader.load();
            PlateSimpleOperationController controller = loader.getController();
            SimpleOperation operation = SimpleOperationServiceImpl.getInstance().findById(opData.getSimpleOtherOpId());
            opData.setOperationPrototype(operation);
            controller.init(formController, opData, addedOperations.size(), operation.getName());
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * УПАКОВКА НА ПОДДОН
     */
    public void addPackOnPalletPlate(OpPackOnPallet opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/pack/platePackOnPallet.fxml"));
            VBox vBox = loader.load();
            PlatePackOnPalletController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УПАКОВКА НА ПОДДОН");
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ERROR
     */
    public void addErrorPlate(OpErrorData opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateError.fxml"));
            VBox vBox = loader.load();
            PlateErrorController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            addVBox(vBox);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //==================================================================================================================

    /**
     * EMPTY PLATE
     */
    public void addEmptyPlate() {

        for(VBox box : listViewTechOperations.getItems()){
            if(box.getId().equals("LAST_LINE"))
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateEmpty.fxml"));
            VBox vBox = loader.load();
            PlateEmptyController controller = loader.getController();
            listViewTechOperations.getItems().add(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление операционной плашки перед пустой плашкой
     * @param vBox - добавляемая плашка
     */
    private void addVBox(VBox vBox) {
        int lastIndex = listViewTechOperations.getItems().size() == 0 ? 0 : listViewTechOperations.getItems().size() - 1;
        listViewTechOperations.getItems().add(lastIndex, vBox);
    }

    //=======================   ЭЛЕКТРОМОНТАЖ =========================================================================

    /**
     * УСТАНОВКА НА ДИНРЕЙКУ
     */
    public void add_MountOnDin_Plate(OpMountOnDin opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountOnDin.fxml"));
            VBox vBox = loader.load();
            Plate_MountOnDin_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА НА ДИРЕЙКУ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА НА ВИНТЫ БЕЗ РАЗБОРКИ КОРПУСА
     */
    public void add_MountOnScrewsNoDisAssm_Plate(OpMountOnScrewsNoDisAssm opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountOnScrewsNoDisAssm.fxml"));
            VBox vBox = loader.load();
            Plate_MountOnScrewsNoDisAssm_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА НА ВИНТЫ БЕЗ РАЗБОРКИ КОРПУСА");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА НА ВИНТЫ С РАЗБОРКОЙ КОРПУСА
     */
    public void add_MountOnScrewsWithDisAssm_Plate(OpMountOnScrewsWithDisAssm opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountOnScrewsWithDisAssm.fxml"));
            VBox vBox = loader.load();
            Plate_MountOnScrewsWithDisAssm_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА НА ВИНТЫ С РАЗБОРКОЙ КОРПУСА");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА НА ВШГ(4 шт)
     */
    public void add_MountOnVSHG_Plate(OpMountOnVSHG opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountOnVSHG.fxml"));
            VBox vBox = loader.load();
            Plate_MountOnVSHG_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА НА ВШГ(4 шт)");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВРЕЗНОЙ КОНТАКТ
     */
    public void add_ConnectDeviceMortiseContact_Plate(OpConnectDeviceMortiseContact opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateConnectDeviceMortiseContact.fxml"));
            VBox vBox = loader.load();
            Plate_ConnectDeviceMortiseContact_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), " ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВРЕЗНОЙ КОНТАКТ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ПРУЖИННЫЙ ЗАЖИМ
     */
    public void add_ConnectDeviceSpringClamp_Plate(OpConnectDeviceSpringClamp opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateConnectDeviceSpringClamp.fxml"));
            VBox vBox = loader.load();
            Plate_ConnectDeviceSpringClamp_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ПРУЖИННЫЙ ЗАЖИМ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ЗАЖИМНОЙ ВИНТ
     */
    public void add_ConnectDeviceClampingScrew_Plate(OpConnectDeviceClampingScrew opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateConnectDeviceClampingScrew.fxml"));
            VBox vBox = loader.load();
            Plate_ConnectDeviceClampingScrew_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ЗАЖИМНОЙ ВИНТ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВШГ
     */
    public void add_ConnectDeviceVSHG_Plate(OpConnectDeviceVSHG opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateConnectDeviceVSHG.fxml"));
            VBox vBox = loader.load();
            Plate_ConnectDeviceVSHG_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВШГ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ
     */
    public void add_CutCableHandly_Plate(OpCutCableHandly opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateCutCableHandly.fxml"));
            VBox vBox = loader.load();
            Plate_CutCableHandly_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * РЕЗКА КАБЕЛЯ НА АВТОМАТЕ
     */
    public void add_CutCableOnMachine_Plate(OpCutCableOnMachine opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateCutCableOnMachine.fxml"));
            VBox vBox = loader.load();
            Plate_CutCableOnMachine_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "РЕЗКА КАБЕЛЯ НА АВТОМАТЕ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  РЕЗКА МЕТАЛЛОРУКАВА
     */
    public void add_CutMetalSleeve_Plate(OpCutMetalSleeve opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateCutMetalSleeve.fxml"));
            VBox vBox = loader.load();
            Plate_CutMetalSleeve_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "РЕЗКА МЕТАЛЛОРУКАВА");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  РЕЗКА КАБЕЛЬКАНАЛА, ДИНРЕЙКИ
     */
    public void add_CutCableChannel_Plate(OpCutCableChannel opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateCutCableChannel.fxml"));
            VBox vBox = loader.load();
            Plate_CutCableChannel_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "РЕЗКА КАБЕЛЬКАНАЛА, ДИНРЕЙКИ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  ЛУЖЕНИЕ В ВАННОЧКЕ
     */
    public void add_TinningInBathe_Plate(OpTinningInBathe opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateTinningInBathe.fxml"));
            VBox vBox = loader.load();
            Plate_TinningInBathe_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ЛУЖЕНИЕ В ВАННОЧКЕ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  ЛУЖЕНИЕ ЭЛЕКТРОПАЯЛЬНИКОМ
     */
    public void add_Tinning_Plate(OpTinning opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateTinning.fxml"));
            VBox vBox = loader.load();
            Plate_Tinning_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ЛУЖЕНИЕ ЭЛЕКТРОПАЯЛЬНИКОМ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  ОКОНЦОВКА ПРОВОДА
     */
    public void add_MountTipOnCable_Plate(OpMountTipOnCable opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountTipOnCable.fxml"));
            VBox vBox = loader.load();
            Plate_MountTipOnCable_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОКОНЦОВКА ПРОВОДА");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  ОКОНЦОВКА СИЛОВОГО КАБЕЛЯ
     */
    public void add_MountTipOnPowerCable_Plate(OpMountTipOnPowerCable opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountTipOnPowerCable.fxml"));
            VBox vBox = loader.load();
            Plate_MountTipOnPowerCable_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОКОНЦОВКА СИЛОВОГО КАБЕЛЯ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * МАРКИРОВКА
     */
    public void add_Marking_Plate(OpMarking opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMarking.fxml"));
            VBox vBox = loader.load();
            Plate_Marking_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "МАРКИРОВКА");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА СИГНАЛЬНОЙ АППАРАТУРЫ
     */
    public void add_MountOfSignalEquip_Plate(OpMountOfSignalEquip opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountOfSignalEquip.fxml"));
            VBox vBox = loader.load();
            Plate_MountOfSignalEquip_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА СИГНАЛЬНОЙ АППАРАТУРЫ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СОЕДИНЕНИЕ ЭЛЕМЕНТОВ ПАЙКОЙ
     */
    public void add_Soldering_Plate(OpSoldering opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateSoldering.fxml"));
            VBox vBox = loader.load();
            Plate_Soldering_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СОЕДИНЕНИЕ ЭЛЕМЕНТОВ ПАЙКОЙ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА КАБЕЛЬНЫХ ВВОДОВ
     */
    public void add_MountOfCableEntries_Plate(OpMountOfCableEntries opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateMountOfCableEntries.fxml"));
            VBox vBox = loader.load();
            Plate_MountOfCableEntries_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА КАБЕЛЬНЫХ ВВОДОВ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УКЛАДКА ЖГУТОВ
     */
    public void add_FixOfCables_Plate(OpFixOfCables opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateFixOfCables.fxml"));
            VBox vBox = loader.load();
            Plate_FixOfCables_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УКЛАДКА ЖГУТОВ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 2-10 ММ
     */
    public void add_IsolateWithThermotube10_Plate(OpIsolateWithThermTube10 opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateIsolateWithThermotube10.fxml"));
            VBox vBox = loader.load();
            Plate_IsolateWithThermotube10_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 2-10 ММ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 10-30 ММ
     */
    public void add_IsolateWithThermotube30_Plate(OpIsolateWithThermTube30 opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/electrical/plateIsolateWithThermotube30.fxml"));
            VBox vBox = loader.load();
            Plate_IsolateWithThermotube30_Controller controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 10-30 ММ");
            addVBox(vBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
