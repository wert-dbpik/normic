package ru.wert.normic.menus;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.PlateErrorController;
import ru.wert.normic.controllers.assembling.*;
import ru.wert.normic.controllers.list.PlateBendController;
import ru.wert.normic.controllers.list.PlateCuttingController;
import ru.wert.normic.controllers.locksmith.*;
import ru.wert.normic.controllers.packing.*;
import ru.wert.normic.controllers.paint.PlatePaintAssmController;
import ru.wert.normic.controllers.paint.PlatePaintController;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.controllers.singlePlates.PlatePackController;
import ru.wert.normic.controllers.turning.*;
import ru.wert.normic.controllers.welding.PlateWeldContinuousController;
import ru.wert.normic.controllers.welding.PlateWeldDottedController;
import ru.wert.normic.entities.ops.OpData;
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
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.io.IOException;
import java.util.List;

/**
 * МЕНЮ ДОБАВЛЕНИЯ ОПЕРАЦИЙ
 */
public class MenuForm extends ContextMenu {

    private final AbstractFormController formController;
    private final ListView<VBox> listViewTechOperations;
    private final List<OpData> addedOperations;
    private final IOpWithOperations opData;

    /**
     * Create a new ContextMenu
     */
    public MenuForm(AbstractFormController formController, ListView<VBox> listViewTechOperations, IOpWithOperations opData) {
        this.formController = formController;
        this.listViewTechOperations = listViewTechOperations;
        this.opData = opData;
        this.addedOperations = formController.getAddedOperations();
    }


    //ДОБАВИТЬ ДЕТАЛЬ
    public MenuItem createItemDetail(){
        MenuItem addDetail = new MenuItem("Добавить деталь");
        addDetail.setOnAction(event -> {
            OpDetail opDetail = new OpDetail();
            addDetailPlate(opDetail);
        });
        return addDetail;
    }

    //ДОБАВИТЬ СБОРКУ
    public MenuItem createItemAssm(){
        MenuItem addAssm = new MenuItem("Добавить сборку");
        addAssm.setOnAction(event -> {
            OpAssm opAssm = new OpAssm();
            addAssmPlate(opAssm);
        });
        return addAssm;
    }

    //ДОБАВИТЬ УПАКОВКУ
    public MenuItem createItemPack(){
        MenuItem addPack = new MenuItem("Добавить упаковку");
        addPack.setOnAction(event -> {
            OpPack opPack = new OpPack();
            addPackPlate(opPack);
        });
        return addPack;
    }


    //РАСКРОЙ И ЗАЧИСТКА
    public MenuItem createItemCutting(){
        MenuItem addCutting = new MenuItem("Резка и зачистка");
        addCutting.setOnAction(event -> {
            if(isDuplicate(EOpType.CUTTING)) return ;
            addCattingPlate(new OpCutting());
        });
        return addCutting;
    }

    //ГИБКА
    public MenuItem createItemBending(){
        MenuItem addBending = new MenuItem("Гибка");
        addBending.setOnAction(event -> {
            if(isDuplicate(EOpType.BENDING)) return ;
            addBendingPlate(new OpBending());
        });
        return addBending;
    }


    //СЛЕСАРНЫЕ РАБОТЫ
    public MenuItem createItemLocksmith(){
        MenuItem addLocksmith = new MenuItem("Слесарные операции");
        addLocksmith.setOnAction(event -> {
            if(isDuplicate(EOpType.LOCKSMITH)) return ;
            addLocksmithPlate(new OpLocksmith());
        });
        return addLocksmith;
    }

    //==============================ТОКАРНЫЕ ОПЕРАЦИИ =======================

    //УСТАНОВКА / СНЯТИЕ детали
    public MenuItem createItemMountDismount(){
        MenuItem item = new MenuItem("Установка и снятие детали");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.LATHE_CUT_OFF)) return ;
            addMountDismountPlate(new OpLatheMountDismount());
        });
        return item;
    }

    //ТОЧЕНИЕ / РАСТАЧИВАНИЕ (многократное добавление)
    public MenuItem createItemTurning(){
        MenuItem item = new MenuItem("Точение и растачивание");
        item.setOnAction(event -> {
            addTurningPlate(new OpLatheTurning());
        });
        return item;
    }

    //ПРОРЕЗАНИЕ ПАЗА (многократное добавление)
    public MenuItem createItemCutGroove(){
        MenuItem item = new MenuItem("Точение канавки");
        item.setOnAction(event -> {
            addCutGroovePlate(new LatheCutGroove());
        });
        return item;
    }

    //ОТРЕЗАНИЕ
    public MenuItem createItemCutOff(){
        MenuItem item = new MenuItem("Отрезание на токарном станке");
        item.setOnAction(event -> {
            addCutOffPlate(new OpLatheCutOff());
        });
        return item;
    }

    //ОТРЕЗАНИЕ НА ПИЛЕ
    public MenuItem createItemCutOffOnTheSaw(){
        MenuItem item = new MenuItem("Отрезание на пиле");
        item.setOnAction(event -> {
            addCutOffOnTheSawPlate(new OpCutOffOnTheSaw());
        });
        return item;
    }

    //ОТРУБАНИЕ
    public MenuItem createItemChopOff(){
        MenuItem item = new MenuItem("Рубка в размер");
        item.setOnAction(event -> {
            addChopOffPlate(new OpChopOff());
        });
        return item;
    }

    //НАРЕЗАНИЕ РЕЗЬБЫ (многократное добавление)
    public MenuItem createItemThreading(){
        MenuItem item = new MenuItem("Нарезание резьбы");
        item.setOnAction(event -> {
            addThreadingPlate(new OpLatheThreading());
        });
        return item;
    }

    //СВЕРЛЕНИЕ ОТВЕРСТИЯ (многократное добавление)
    public MenuItem createItemDrilling(){
        MenuItem item = new MenuItem("Сверление отверстия");
        item.setOnAction(event -> {
            addDrillingPlate(new OpLatheDrilling());
        });
        return item;
    }

    //СВЕРЛЕНИЕ ОТВЕРСТИЯ ПО РАЗМЕТКЕ (многократное добавление)
    public MenuItem createItemDrillingByMarking(){
        MenuItem item = new MenuItem("Сверление по разметке");
        item.setOnAction(event -> {
            addDrillingByMarkingPlate(new OpDrillingByMarking());
        });
        return item;
    }

    //НАКАТЫВАНИЕ РИФЛЕНИЯ (многократное добавление)
    public MenuItem createItemRolling(){
        MenuItem item = new MenuItem("Накатывание рифления");
        item.setOnAction(event -> {
            addRollingPlate(new OpLatheRolling());
        });
        return item;
    }

    //=======================================================================

    //СВАРКА НЕПРЕРЫВНАЯ
    public MenuItem createItemWeldLongSeam(){
        MenuItem item = new MenuItem("Сварка непрерывная");
        item.setOnAction(event -> {
            addWeldContinuousPlate(new OpWeldContinuous());
        });
        return item;
    }


    //СВАРКА ТОЧЕЧНАЯ
    public MenuItem createItemWeldingDotted(){
        MenuItem item = new MenuItem("Сварка точечная");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.WELD_DOTTED)) return ;
            addWeldDottedPlate(new OpWeldDotted());
        });
        return item;
    }


    //=======================================================================

    //ПОКРАСКА
    public MenuItem createItemPainting(){
        MenuItem item = new MenuItem("Покраска детали");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINTING)) return ;
            addPaintPlate(new OpPaint());
        });
        return item;
    }



    //ПОКРАСКА СБОРОЧНОЙ ЕДИНИЦЫ
    public MenuItem createItemPaintAssm(){
        MenuItem item = new MenuItem("Покраска сборочной единицы");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINT_ASSM)) return ;
            addPaintAssmPlate(new OpPaintAssm());
        });
        return item;
    }


    //=======================================================================
    //СБОРКА - КРЕПЕЖ
    public MenuItem createItemAssmNuts(){
        MenuItem item = new MenuItem("Сборка крепежа");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NUTS)) return ;
            addAssmNutsPlate(new OpAssmNut());
        });
        return item;
    }

    //СБОРКА - КРЕПЕЖ
    public MenuItem createItemAssmNutsMK(){
        MenuItem item = new MenuItem("Крепеж (участок МК)");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NUTS_MK)) return ;
            addAssmNutsMKPlate(new OpAssmNutMK());
        });
        return item;
    }


    //СБОРКА - РАСКРОЙНЫЙ МАТЕРИАЛ
    public MenuItem createItemAssmCuttings(){
        MenuItem item = new MenuItem("Сборка раскройного материала");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_CUTTINGS)) return ;
            addAssmCuttingsPlate(new OpAssmCutting());
        });
        return item;
    }


    //СБОРКА СТАНДАРТНЫХ УЗЛОВ
    public MenuItem createItemAssmNodes(){
        MenuItem item = new MenuItem("Сборка стандартных узлов");
        item.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NODES)) return ;
            addAssmNodesPlate(new OpAssmNode());
        });
        return item;
    }


    //НАНЕСЕНИЕ НАЛИВНОГО УПЛОТНИТЕЛЯ
    public MenuItem createItemLevelingSealer(){
        MenuItem item = new MenuItem("Нанесение наливного утеплителя");
        item.setOnAction(event -> {
            addLevelingSealerPlate(new OpLevelingSealer());
        });
        return item;
    }


    //=========================      УПАКОВКА    =================================

    //КРЕПЛЕНИЕ В КАРТОННУЮ КОРОБКУ
    public MenuItem createItemPackInCartoonBox(){
        MenuItem item = new MenuItem("Упаковка в картонную коробку");
        item.setOnAction(event -> {
            addPackInCartoonBoxPlate(new OpPackInCartoonBox());
        });
        return item;
    }

    //УПАКОВКА В МАШИННУЮ СТРЕЙЧ-ПЛЕНКУ
    public MenuItem createItemPackOnPalletizer(){
        MenuItem item = new MenuItem("Упаковка в машинную стрейч-пленку");
        item.setOnAction(event -> {
            addPackInCartoonAndStretchPlate(new OpPackInMachineStretchWrap());
        });
        return item;
    }

    //УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
    public MenuItem createItemPackInHandStretchWrap(){
        MenuItem item = new MenuItem("Упаковка в ручную стрейч-пленку");
        item.setOnAction(event -> {
            addPackInHandStretchWrapPlate(new OpPackInHandStretchWrap());
        });
        return item;
    }

    //УПАКОВКА В ПУЗЫРЬКОВУЮ ПЛЕНКУ
    public MenuItem createItemPackInBubbleWrap(){
        MenuItem item = new MenuItem("Упаковка в пузырьковую пленку");
        item.setOnAction(event -> {
            addPackInBubbleWrapPlate(new OpPackInBubbleWrap());
        });
        return item;
    }

    //КРЕПЛЕНИЕ К ПОДДОНУ
    public MenuItem createItemPackOnPallet(){
        MenuItem item = new MenuItem("Монтаж на поддон");
        item.setOnAction(event -> {
            addPackOnPalletPlate(new OpPackOnPallet());
        });
        return item;
    }

    //ДОБАВИТЬ ФАЙЛ
    public MenuItem createItemAddFilePallet(){
        MenuItem item = new MenuItem("Добавить файл");
        item.setOnAction(e->formController.open(e, EMenuSource.FORM_MENU));
        return item;
    }


    /*==================================================================================================================
     *
     * ==================================================================================================================*/

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
    public void deployData() {
        List<OpData> operations = ((IOpWithOperations)opData).getOperations();
        for (OpData op : operations) {
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
                    addCutGroovePlate((LatheCutGroove) op);
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
                    addPaintPlate((OpPaint) op);
                    break;
                case PAINT_ASSM:
                    addPaintAssmPlate((OpPaintAssm) op);
                    break;
                case WELD_CONTINUOUS:
                    addWeldContinuousPlate((OpWeldContinuous) op);
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
                case PACK_IN_CARTOON_BOX:
                    addPackInCartoonBoxPlate((OpPackInCartoonBox) op);
                    break;
                case PACK_IN_MACHINE_STRETCH_WRAP:
                    addPackInCartoonAndStretchPlate((OpPackInMachineStretchWrap) op);
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
                case ERROR_OP_DATA:
                    addErrorPlate((OpErrorData) op);
                    break;
            }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateDetail.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/plateAssm.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/assembling/platePack.fxml"));
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            PlateTurningController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ТОЧЕНИЕ ИЛИ РАСТАЧИВАНИЕ");
            listViewTechOperations.getItems().add(vBox);
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
            PlateCutOffController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ОТРЕЗАНИЕ НА ТОКАРНОМ СТАНКЕ");
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ТОЧЕНИЕ ПАЗА
     */
    public void addCutGroovePlate(LatheCutGroove opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning/plateCutGroove.fxml"));
            VBox vBox = loader.load();
            PlateCutGrooveController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ТОЧЕНИЕ ПАЗА НА ТОКАРНОМ СТАНКЕ");
            listViewTechOperations.getItems().add(vBox);
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
            PlateThreadingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ");
            listViewTechOperations.getItems().add(vBox);
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
            PlateDrillingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ");
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            PlateRollingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "НАКАТЫВАНИЕ РИФЛЕНИЯ НА ТОКАРНОМ СТАНКЕ");
            listViewTechOperations.getItems().add(vBox);
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
            PlateMountDismountController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УСТАНОВКА И СНЯТИЕ ДЕТАЛИ");
            listViewTechOperations.getItems().add(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //==================================================================================================================

    /**
     * ПОКРАСКА ДЕТАЛИ
     */
    public void addPaintPlate(OpPaint opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/paint/platePaint.fxml"));
            VBox vBox = loader.load();
            PlatePaintController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "ПОКРАСКА ДЕТАЛИ");
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УПАКОВКА В МАШИННУЮ СТРЕЙЧ-ПЛЕНКУ
     */
    public void addPackInCartoonAndStretchPlate(OpPackInMachineStretchWrap opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/pack/platePackInMachineStretchWrap.fxml"));
            VBox vBox = loader.load();
            PlatePackInMachineStretchWrapController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size(), "УПАКОВКА В МАШИННУЮ СТРЕЙЧ-ПЛЕНКУ");
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
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
            listViewTechOperations.getItems().add(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
