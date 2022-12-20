package ru.wert.normic.controllers.forms;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.interfaces.IForm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;

import java.util.List;


public abstract class AbstractFormController implements IForm {

    protected MenuCalculator menu;
    @Getter protected OpData opData;
    @Getter protected ObservableList<AbstractOpPlate> addedPlates;
    @Getter protected List<OpData> addedOperations;

    @Getter protected DoubleProperty formAreaProperty = new SimpleDoubleProperty(0.0);

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private Button btnAddOperation;

    public abstract void countSumNormTimeByShops();

    public abstract void fillOpData();

    public void calculateAreaByDetails(){
        double area = 0.0;
        List<OpData> ops = getAddedOperations();
        for(OpData op : ops){
            if(op instanceof IOpWithOperations)
                area += ((IOpWithOperations) op).getArea() * op.getQuantity();
        }
        formAreaProperty.set(area);
    }

    protected void tyeMenuToButton(){
        btnAddOperation.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/add.png")), 44,44, true, true)));
        btnAddOperation.setTooltip(new Tooltip("Добавить операцию"));
        btnAddOperation.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY)){
                menu.show(
                        btnAddOperation,
                        Side.LEFT,
                        0.0,
                        32.0);
            }
        });
    };

    public void deleteOperation(OpData operation){
        int index = addedOperations.indexOf(operation);
        addedOperations.remove(operation);
        addedPlates.remove(index);
        listViewTechOperations.getItems().remove(index);

        countSumNormTimeByShops();

    }

}
