package ru.wert.normik.interfaces;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.wert.normik.AbstractOpPlate;
import ru.wert.normik.enums.ETimeMeasurement;


public interface IFormController extends IForm{

    ObservableList<AbstractOpPlate> getAddedPlates();

    ListView<VBox> getListViewTechOperations();

    void countSumNormTimeByShops();

    ComboBox<ETimeMeasurement> getCmbxTimeMeasurement();

}
