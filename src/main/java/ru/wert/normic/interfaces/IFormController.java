package ru.wert.normic.interfaces;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.wert.normic.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ETimeMeasurement;

import java.util.List;


public interface IFormController extends IForm{

    ObservableList<AbstractOpPlate> getAddedPlates();

    ListView<VBox> getListViewTechOperations();


    List<OpData> getAddedOperations();


    void countSumNormTimeByShops();

    ComboBox<ETimeMeasurement> getCmbxTimeMeasurement();

    OpData getOpData();

}
