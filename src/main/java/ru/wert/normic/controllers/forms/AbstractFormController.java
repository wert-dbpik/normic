package ru.wert.normic.controllers.forms;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.interfaces.IForm;

import java.util.List;


public abstract class AbstractFormController implements IForm {


    @Getter protected OpData opData;
    @Getter protected ObservableList<AbstractOpPlate> addedPlates;
    @Getter protected List<OpData> addedOperations;

    @FXML
    @Getter
    private ListView<VBox> listViewTechOperations;


    public abstract ListView<VBox> getListViewTechOperations();

    public abstract void countSumNormTimeByShops();

    public abstract void fillOpData();

    public void deleteOperation(OpData operation){
        int index = addedOperations.indexOf(operation);
        addedOperations.remove(operation);
        addedPlates.remove(index);
        listViewTechOperations.getItems().remove(index);

        countSumNormTimeByShops();

    }

}
