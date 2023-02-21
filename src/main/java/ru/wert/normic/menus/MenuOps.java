package ru.wert.normic.menus;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.assembling.*;
import ru.wert.normic.controllers.list.PlateBendController;
import ru.wert.normic.controllers.list.PlateCuttingController;
import ru.wert.normic.controllers.locksmith.PlateLocksmithController;
import ru.wert.normic.controllers.packing.PlatePackFrameController;
import ru.wert.normic.controllers.paint.PlatePaintAssmController;
import ru.wert.normic.controllers.paint.PlatePaintController;
import ru.wert.normic.controllers.locksmith.PlateChopOffController;
import ru.wert.normic.controllers.locksmith.PlateCutOffOnTheSawController;
import ru.wert.normic.controllers.locksmith.PlateDrillingByMarkingController;
import ru.wert.normic.controllers.turning.*;
import ru.wert.normic.controllers.welding.PlateWeldContinuousController;
import ru.wert.normic.controllers.welding.PlateWeldDottedController;
import ru.wert.normic.entities.*;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.io.IOException;
import java.util.List;

/**
 * МЕНЮ ДОБАВЛЕНИЯ ОПЕРАЦИЙ
 */
public class MenuOps extends ContextMenu {

    private final AbstractFormController formController;
    private final ListView<VBox> listViewTechOperations;
    private final List<OpData> addedOperations;
    private final IOpWithOperations opData;

    /**
     * Create a new ContextMenu
     */
    public MenuOps(AbstractFormController formController, ListView<VBox> listViewTechOperations, IOpWithOperations opData) {
        this.formController = formController;
        this.listViewTechOperations = listViewTechOperations;
        this.opData = opData;
        this.addedOperations = formController.getAddedOperations();
    }


    //ДОБАВИТЬ ДЕТАЛЬ
    public MenuItem createItemAddDetail(){
        MenuItem addDetail = new MenuItem("Добавить деталь");
        addDetail.setOnAction(event -> {
            OpDetail opDetail = new OpDetail();
            addDetailPlate(opDetail);
        });
        return addDetail;
    }

    //ДОБАВИТЬ СБОРКУ
    public MenuItem createItemAddAssm(){
        MenuItem addAssm = new MenuItem("Добавить сборку");
        addAssm.setOnAction(event -> {
            OpAssm opAssm = new OpAssm();
            addAssmPlate(opAssm);
        });
        return addAssm;
    }


    //РАСКРОЙ И ЗАЧИСТКА
    public MenuItem createItemAddCutting(){
        MenuItem addCutting = new MenuItem("Резка и зачистка");
        addCutting.setOnAction(event -> {
            if(isDuplicate(EOpType.CUTTING)) return ;
            addCattingPlate(new OpCutting());
        });
        return addCutting;
    }

    //ГИБКА
    public MenuItem createItemAddBending(){
        MenuItem addBending = new MenuItem("Гибка");
        addBending.setOnAction(event -> {
            if(isDuplicate(EOpType.BENDING)) return ;
            addBendingPlate(new OpBending());
        });
        return addBending;
    }


    //СЛЕСАРНЫЕ РАБОТЫ
    public MenuItem createItemAddLocksmith(){
        MenuItem addLocksmith = new MenuItem("Слесарные операции");
        addLocksmith.setOnAction(event -> {
            if(isDuplicate(EOpType.LOCKSMITH)) return ;
            addLocksmithPlate(new OpLocksmith());
        });
        return addLocksmith;
    }

    //==============================ТОКАРНЫЕ ОПЕРАЦИИ =======================

    //УСТАНОВКА / СНЯТИЕ детали
    public MenuItem createItemAddMountDismount(){
        MenuItem addMountDismount = new MenuItem("Установка и снятие детали");
        addMountDismount.setOnAction(event -> {
            if(isDuplicate(EOpType.CUT_OFF)) return ;
            addMountDismountPlate(new OpMountDismount());
        });
        return addMountDismount;
    }

    //ТОЧЕНИЕ / РАСТАЧИВАНИЕ (многократное добавление)
    public MenuItem createItemAddTurning(){
        MenuItem addTurning = new MenuItem("Точение/растачивание");
        addTurning.setOnAction(event -> {
            addTurningPlate(new OpTurning());
        });
        return addTurning;
    }

    //ПРОРЕЗАНИЕ ПАЗА (многократное добавление)
    public MenuItem createItemAddCutGroove(){
        MenuItem addCutGroove = new MenuItem("Прорезание паза");
        addCutGroove.setOnAction(event -> {
            addCutGroovePlate(new OpCutGroove());
        });
        return addCutGroove;
    }

    //ОТРЕЗАНИЕ
    public MenuItem createItemAddCutOff(){
        MenuItem addCatOff = new MenuItem("Отрезание на токарном станке");
        addCatOff.setOnAction(event -> {
            addCutOffPlate(new OpCutOff());
        });
        return addCatOff;
    }

    //ОТРЕЗАНИЕ НА ПИЛЕ
    public MenuItem createItemAddCutOffOnTheSaw(){
        MenuItem addCatOffOnTheSaw = new MenuItem("Отрезание на пиле");
        addCatOffOnTheSaw.setOnAction(event -> {
            addCutOffOnTheSawPlate(new OpCutOffOnTheSaw());
        });
        return addCatOffOnTheSaw;
    }

    //ОТРУБАНИЕ
    public MenuItem createItemAddChopOff(){
        MenuItem addChopOff = new MenuItem("Отрубание в размер");
        addChopOff.setOnAction(event -> {
            addChopOffPlate(new OpChopOff());
        });
        return addChopOff;
    }

    //НАРЕЗАНИЕ РЕЗЬБЫ (многократное добавление)
    public MenuItem createItemAddThreading(){
        MenuItem addThreading = new MenuItem("Нарезание резьбы");
        addThreading.setOnAction(event -> {
            addThreadingPlate(new OpThreading());
        });
        return addThreading;
    }

    //СВЕРЛЕНИЕ ОТВЕРСТИЯ (многократное добавление)
    public MenuItem createItemAddDrilling(){
        MenuItem addDrilling = new MenuItem("Сверление отверстия");
        addDrilling.setOnAction(event -> {
            addDrillingPlate(new OpDrilling());
        });
        return addDrilling;
    }

    //СВЕРЛЕНИЕ ОТВЕРСТИЯ ПО РАЗМЕТКЕ (многократное добавление)
    public MenuItem createItemAddDrillingByMarking(){
        MenuItem addDrillingByMarking = new MenuItem("Сверление по разметке");
        addDrillingByMarking.setOnAction(event -> {
            addDrillingByMarkingPlate(new OpDrillingByMarking());
        });
        return addDrillingByMarking;
    }

    //НАКАТЫВАНИЕ РИФЛЕНИЯ (многократное добавление)
    public MenuItem createItemAddRolling(){
        MenuItem addRolling = new MenuItem("Накатывание рифления");
        addRolling.setOnAction(event -> {
            addRollingPlate(new OpRolling());
        });
        return addRolling;
    }

    //=======================================================================

    //СВАРКА НЕПРЕРЫВНАЯ
    public MenuItem createItemAddWeldLongSeam(){
        MenuItem addWeldLongSeam = new MenuItem("Сварка непрерывная");
        addWeldLongSeam.setOnAction(event -> {
            addWeldContinuousPlate(new OpWeldContinuous());
        });
        return addWeldLongSeam;
    }


    //СВАРКА ТОЧЕЧНАЯ
    public MenuItem createItemAddWeldingDotted(){
        MenuItem addWeldingDotted = new MenuItem("Сварка точечная");
        addWeldingDotted.setOnAction(event -> {
            if(isDuplicate(EOpType.WELD_DOTTED)) return ;
            addWeldDottedPlate(new OpWeldDotted());
        });
        return addWeldingDotted;
    }


    //=======================================================================

    //ПОКРАСКА
    public MenuItem createItemAddPainting(){
        MenuItem addPainting = new MenuItem("Покраска детали");
        addPainting.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINTING)) return ;
            addPaintPlate(new OpPaint());
        });
        return addPainting;
    }



    //ПОКРАСКА СБОРОЧНОЙ ЕДИНИЦЫ
    public MenuItem createItemAddPaintAssm(){
        MenuItem addPaintAssm = new MenuItem("Покраска сборочной единицы");
        addPaintAssm.setOnAction(event -> {
            if(isDuplicate(EOpType.PAINT_ASSM)) return ;
            addPaintAssmPlate(new OpPaintAssm());
        });
        return addPaintAssm;
    }


    //=======================================================================
    //СБОРКА - КРЕПЕЖ
    public MenuItem createItemAddAssmNuts(){
        MenuItem addAssmNuts = new MenuItem("Сборка крепежа");
        addAssmNuts.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NUTS)) return ;
            addAssmNutsPlate(new OpAssmNut());
        });
        return addAssmNuts;
    }


    //СБОРКА - РАСКРОЙНЫЙ МАТЕРИАЛ
    public MenuItem createItemAddAssmCuttings(){
        MenuItem addAssmCuttings = new MenuItem("Сборка раскройного материала");
        addAssmCuttings.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_CUTTINGS)) return ;
            addAssmCuttingsPlate(new OpAssmCutting());
        });
        return addAssmCuttings;
    }


    //СБОРКА СТАНДАРТНЫХ УЗЛОВ
    public MenuItem createItemAddAssmNodes(){
        MenuItem addAssmNodes = new MenuItem("Сборка стандартных узлов");
        addAssmNodes.setOnAction(event -> {
            if(isDuplicate(EOpType.ASSM_NODES)) return ;
            addAssmNodesPlate(new OpAssmNode());
        });
        return addAssmNodes;
    }


    //НАНЕСЕНИЕ НАЛИВНОГО УПЛОТНИТЕЛЯ
    public MenuItem createItemAddLevelingSealer(){
        MenuItem addLevelingSealer = new MenuItem("Нанесение наливного утеплителя");
        addLevelingSealer.setOnAction(event -> {
            addLevelingSealerPlate(new OpLevelingSealer());
        });
        return addLevelingSealer;
    }

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
    //=========================      УПАКОВКА    =================================
    //УПАКОВКА КАРКАСА
    public MenuItem createItemAddPackFrame(){
        MenuItem addPackFrame = new MenuItem("Упаковка каркаса");
        addPackFrame.setOnAction(event -> {
            if(isDuplicate(EOpType.PACK_FRAME)) return ;
            addPackFramePlate(new OpPackFrame());
        });
        return addPackFrame;
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
                case CUTTING:
                    addCattingPlate((OpCutting) op);
                    break;
                case BENDING:
                    addBendingPlate((OpBending) op);
                    break;
                case LOCKSMITH:
                    addLocksmithPlate((OpLocksmith) op);
                    break;
                case TURNING:
                    addTurningPlate((OpTurning) op);
                    break;
                case CUT_GROOVE:
                    addCutGroovePlate((OpCutGroove) op);
                    break;
                case THREADING:
                    addThreadingPlate((OpThreading) op);
                    break;
                case DRILLING:
                    addDrillingPlate((OpDrilling) op);
                    break;
                case DRILLING_BY_MARKING:
                    addDrillingByMarkingPlate((OpDrillingByMarking) op);
                    break;
                case ROLLING:
                    addRollingPlate((OpRolling) op);
                    break;
                case CUT_OFF:
                    addCutOffPlate((OpCutOff) op);
                    break;
                case CUT_OFF_ON_SAW:
                    addCutOffOnTheSawPlate((OpCutOffOnTheSaw) op);
                    break;
                case CHOP_OFF:
                    addChopOffPlate((OpChopOff) op);
                    break;
                case MOUNT_DISMOUNT:
                    addMountDismountPlate((OpMountDismount) op);
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
                case ASSM_NODES:
                    addAssmNodesPlate((OpAssmNode) op);
                    break;
                case LEVELING_SEALER:
                    addLevelingSealerPlate((OpLevelingSealer) op);
                    break;
                case PACK_FRAME:
                    addPackFramePlate((OpPackFrame) op);
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
     * РАСКРОЙ И ЗАЧИСТКА
     */
    public void addCattingPlate(OpCutting opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/list/plateCutting.fxml"));
            VBox cutting = loader.load();
            PlateCuttingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(cutting);
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
            VBox bending = loader.load();
            PlateBendController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(bending);
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
            VBox locksmith = loader.load();
            PlateLocksmithController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(locksmith);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //======================  ТОКАРНЫЕ ОПЕРАЦИИ   ==============================================================
    /**
     * ТОЧЕНИЕ/РАСТАЧИВАНИЕ
     */
    public void addTurningPlate(OpTurning opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateTurning.fxml"));
            VBox turning = loader.load();
            PlateTurningController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(turning);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ОТРЕЗАНИЕ
     */
    public void addCutOffPlate(OpCutOff opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateCutOff.fxml"));
            VBox cutOff = loader.load();
            PlateCutOffController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(cutOff);
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
            VBox cutOffOnTheSaw = loader.load();
            PlateCutOffOnTheSawController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(cutOffOnTheSaw);
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
            VBox chopOff = loader.load();
            PlateChopOffController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(chopOff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ПРОРЕЗАНИЕ ПАЗА
     */
    public void addCutGroovePlate(OpCutGroove opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateCutGroove.fxml"));
            VBox cutGroove = loader.load();
            PlateCutGrooveController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(cutGroove);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * НАРЕЗАНИЕ РЕЗЬБЫ
     */
    public void addThreadingPlate(OpThreading opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateThreading.fxml"));
            VBox threading = loader.load();
            PlateThreadingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(threading);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * СВЕРЛЕНИЕ ОТВЕРСТИЯ
     */
    public void addDrillingPlate(OpDrilling opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateDrilling.fxml"));
            VBox drilling = loader.load();
            PlateDrillingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(drilling);
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
            VBox drillingByMarking = loader.load();
            PlateDrillingByMarkingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(drillingByMarking);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * НАКАТЫВАНИЕ РИФЛЕНИЯ
     */
    public void addRollingPlate(OpRolling opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateRolling.fxml"));
            VBox rolling = loader.load();
            PlateRollingController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(rolling);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * УСТАНОВКА/СНЯТИЕ детали
     */
    public void addMountDismountPlate(OpMountDismount opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/turning_operations/plateMountDismount.fxml"));
            VBox mountDismount = loader.load();
            PlateMountDismountController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(mountDismount);
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
            VBox paint = loader.load();
            PlatePaintController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(paint);
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
            VBox paintAssm = loader.load();
            PlatePaintAssmController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(paintAssm);
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
            VBox weldLongSeam = loader.load();
            PlateWeldContinuousController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(weldLongSeam);
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
            VBox weldDotted = loader.load();
            PlateWeldDottedController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(weldDotted);
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
            VBox assmNuts = loader.load();
            PlateAssmNutsController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(assmNuts);
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
            VBox assmCuttings = loader.load();
            PlateAssmCuttingsController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(assmCuttings);
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
            VBox assmNodes = loader.load();
            PlateAssmNodesController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(assmNodes);
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
            VBox levelingSealer = loader.load();
            PlateLevelingSealerController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(levelingSealer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    /**
     * УПАКОВКА КАРКАСА
     */
    public void addPackFramePlate(OpPackFrame opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/packing/platePackFrame.fxml"));
            VBox packFrame = loader.load();
            PlatePackFrameController controller = loader.getController();
            controller.init(formController, opData, addedOperations.size());
            listViewTechOperations.getItems().add(packFrame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
