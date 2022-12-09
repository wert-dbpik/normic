package ru.wert.normic;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.*;
import ru.wert.normic.entities.*;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IFormController;

import java.io.IOException;
import java.util.List;

public class MenuCalculator extends ContextMenu {

    private final IFormController calculator;
    private final ListView<VBox> listViewTechOperations;
    private final List<OpData> addedOperations;

    /**
     * Create a new ContextMenu
     */
    public MenuCalculator(IFormController calculator, ListView<VBox> listViewTechOperations, List<OpData> addedOperations) {
        this.calculator = calculator;
        this.listViewTechOperations = listViewTechOperations;
        this.addedOperations = addedOperations;
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
            if(isDuplicate(EOpType.LEVELING_SEALER)) return ;
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

    /*==================================================================================================================
    *                                                М Е Т О Д Ы
    * ==================================================================================================================*/

    /**
     * ДОБАВЛЕНИЕ ДЕТАЛИ
     */
    public void addDetailPlate(OpDetail opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateDetail.fxml"));
            VBox detail = loader.load();
            PlateDetailController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(detail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ДОБАВЛЕНИЕ СБОРКИ
     */
    public void addAssmPlate(OpAssm opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateAssm.fxml"));
            VBox assm = loader.load();
            PlateAssmController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(assm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * РАСКРОЙ И ЗАЧИСТКА
     */
    public void addCattingPlate(OpCutting opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateCutting.fxml"));
            VBox cutting = loader.load();
            PlateCuttingController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateBend.fxml"));
            VBox bending = loader.load();
            PlateBendController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateLocksmith.fxml"));
            VBox locksmith = loader.load();
            PlateLocksmithController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(locksmith);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/platePaint.fxml"));
            VBox paint = loader.load();
            PlatePaintController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/platePaintAssm.fxml"));
            VBox paintAssm = loader.load();
            PlatePaintAssmController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateWeldContinuous.fxml"));
            VBox weldLongSeam = loader.load();
            PlateWeldContinuousController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateWeldDotted.fxml"));
            VBox weldDotted = loader.load();
            PlateWeldDottedController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateAssmNuts.fxml"));
            VBox assmNuts = loader.load();
            PlateAssmNutsController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateAssmCuttings.fxml"));
            VBox assmCuttings = loader.load();
            PlateAssmCuttingsController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateAssmNodes.fxml"));
            VBox assmNodes = loader.load();
            PlateAssmNodesController controller = loader.getController();
            controller.init(calculator, opData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plates/plateLevelingSealer.fxml"));
            VBox levelingSealer = loader.load();
            PlateLevelingSealerController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(levelingSealer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
