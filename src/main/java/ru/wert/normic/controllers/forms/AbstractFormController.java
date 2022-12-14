package ru.wert.normic.controllers.forms;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.interfaces.IForm;

import java.util.List;


public abstract class AbstractFormController implements IForm {

    public abstract ObservableList<AbstractOpPlate> getAddedPlates();

    public abstract ListView<VBox> getListViewTechOperations();

    public abstract List<OpData> getAddedOperations();

    public abstract void countSumNormTimeByShops();

    public abstract OpData getOpData();


}
