package ru.wert.normic;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.*;
import ru.wert.normic.controllers.forms.FormAssmController;
import ru.wert.normic.entities.*;
import ru.wert.normic.interfaces.IFormController;


import java.io.IOException;
import java.util.List;

public class MenuCalculator extends ContextMenu {

    private final IFormController calculator;
    private final ObservableList<AbstractOpPlate> addedPlates;

    private final ListView<VBox> listViewTechOperations;
    private final List<OpData> addedOperations;

    /**
     * Create a new ContextMenu
     */
    public MenuCalculator(IFormController calculator, ObservableList<AbstractOpPlate> addedPlates, ListView<VBox> listViewTechOperations, List<OpData> addedOperations) {
        this.calculator = calculator;
        this.addedPlates = addedPlates;
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


    //РАСКРОЙ И ЗАЧИСТКА
    public MenuItem createItemAddCutting(){
        MenuItem addCutting = new MenuItem("Резка и зачистка");
        addCutting.setOnAction(event -> {
            if(isDuplicate(PlateCuttingController.class.getSimpleName())) return ;
            addCattingPlate(new OpCutting());
        });
        return addCutting;
    }

    //ГИБКА
    public MenuItem createItemAddBending(){
        MenuItem addBending = new MenuItem("Гибка");
        addBending.setOnAction(event -> {
            if(isDuplicate(PlateBendController.class.getSimpleName())) return ;
            addBendingPlate(new OpBending());
        });
        return addBending;
    }


    //СЛЕСАРНЫЕ РАБОТЫ
    public MenuItem createItemAddLocksmith(){
        MenuItem addLocksmith = new MenuItem("Слесарные операции");
        addLocksmith.setOnAction(event -> {
            if(isDuplicate(PlateLocksmithController.class.getSimpleName())) return ;
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
            if(isDuplicate(PlateWeldDottedController.class.getSimpleName())) return ;
            addWeldDottedPlate(new OpWeldDotted());
        });
        return addWeldingDotted;
    }


    //=======================================================================

    //ПОКРАСКА
    public MenuItem createItemAddPainting(){
        MenuItem addPainting = new MenuItem("Покраска детали");
        addPainting.setOnAction(event -> {
            if(isDuplicate(PlatePaintController.class.getSimpleName())) return ;
            addPaintPlate(new OpPaint());
        });
        return addPainting;
    }



    //ПОКРАСКА СБОРОЧНОЙ ЕДИНИЦЫ
    public MenuItem createItemAddPaintAssm(){
        MenuItem addPaintAssm = new MenuItem("Покраска сборочной единицы");
        addPaintAssm.setOnAction(event -> {
            if(isDuplicate(PlatePaintAssmController.class.getSimpleName())) return ;
            addPaintAssmPlate(new OpPaintAssm());
        });
        return addPaintAssm;
    }


    //=======================================================================
    //СБОРКА - КРЕПЕЖ
    public MenuItem createItemAddAssmNuts(){
        MenuItem addAssmNuts = new MenuItem("Сборка крепежа");
        addAssmNuts.setOnAction(event -> {
            if(isDuplicate(PlateAssmNutsController.class.getSimpleName())) return ;
            addAssmNutsPlate(new OpAssmNut());
        });
        return addAssmNuts;
    }


    //СБОРКА - РАСКРОЙНЫЙ МАТЕРИАЛ
    public MenuItem createItemAddAssmCuttings(){
        MenuItem addAssmCuttings = new MenuItem("Сборка раскройного материала");
        addAssmCuttings.setOnAction(event -> {
            if(isDuplicate(PlateAssmCuttingsController.class.getSimpleName())) return ;
            addAssmCuttingsPlate(new OpAssmCutting());
        });
        return addAssmCuttings;
    }


    //СБОРКА СТАНДАРТНЫХ УЗЛОВ
    public MenuItem createItemAddAssmNodes(){
        MenuItem addAssmNodes = new MenuItem("Сборка стандартных узлов");
        addAssmNodes.setOnAction(event -> {
            if(isDuplicate(PlateAssmNodesController.class.getSimpleName())) return ;
            addAssmNodesPlate(new OpAssmNode());
        });
        return addAssmNodes;
    }


    //НАНЕСЕНИЕ НАЛИВНОГО УПЛОТНИТЕЛЯ
    public MenuItem createItemAddLevelingSealer(){
        MenuItem addLevelingSealer = new MenuItem("Нанесение наливного утеплителя");
        addLevelingSealer.setOnAction(event -> {
            if(isDuplicate(PlateLevelingSealerController.class.getSimpleName())) return ;
            addLevelingSealerPlate(new OpLevelingSealer());
        });
        return addLevelingSealer;
    }

    /**
     * Ищем дубликат операции в списке addedOperations по clazz
     */
    private boolean isDuplicate(String clazz){
        for(AbstractOpPlate cn: addedPlates){
            if(cn.getClass().getSimpleName().equals(clazz))
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
            detail.setId("calculator");
            PlateDetailController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(detail);
            addedOperations.add(opData);
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
            cutting.setId("calculator");
            PlateCuttingController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(cutting);
            addedOperations.add(opData);
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
            bending.setId("calculator");
            PlateBendController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(bending);
            addedOperations.add(opData);
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
            locksmith.setId("calculator");
            PlateLocksmithController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(locksmith);
            addedOperations.add(opData);
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
            paint.setId("calculator");
            PlatePaintController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(paint);
            addedOperations.add(opData);
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
            paintAssm.setId("calculator");
            PlatePaintAssmController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(paintAssm);
            addedOperations.add(opData);
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
            weldLongSeam.setId("calculator");
            PlateWeldContinuousController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(weldLongSeam);
            addedOperations.add(opData);
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
            weldDotted.setId("calculator");
            PlateWeldDottedController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(weldDotted);
            addedOperations.add(opData);
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
            assmNuts.setId("calculator");
            PlateAssmNutsController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(assmNuts);
            addedOperations.add(opData);
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
            assmCuttings.setId("calculator");
            PlateAssmCuttingsController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(assmCuttings);
            addedOperations.add(opData);
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
            assmNodes.setId("calculator");
            PlateAssmNodesController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(assmNodes);
            addedOperations.add(opData);
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
            levelingSealer.setId("calculator");
            PlateLevelingSealerController controller = loader.getController();
            controller.init(calculator, opData);
            listViewTechOperations.getItems().add(levelingSealer);
            addedOperations.add(opData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
