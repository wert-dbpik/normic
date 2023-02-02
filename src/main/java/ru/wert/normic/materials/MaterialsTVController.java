package ru.wert.normic.materials;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.wert.normic.entities.db_connection.Material;

import java.util.List;

import static ru.wert.normic.ChogoriServices.CH_QUICK_MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialsTVController {

    @FXML
    private TableView<Material> tableView;

    @FXML
    private TableColumn<Material, String> tcName;

    @FXML
    private TableColumn<Material, String> tcType;

    @FXML
    private TableColumn<Material, String> tcParamS;

    @FXML
    private TableColumn<Material, String> tcParamX;

    @FXML
    private TableColumn<Material, String> tcNote;

    @FXML
    void initialize(){

        initializeColumns();

        List<Material> materials = FXCollections.observableArrayList(CH_QUICK_MATERIALS.findAll());
        tableView.getItems().addAll(FXCollections.observableArrayList(materials));
    }

    private void initializeColumns() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tcType.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getMatType().getName()));
        tcType.setStyle("-fx-alignment: CENTER;");

        tcParamS.setCellValueFactory(cd -> new ReadOnlyStringWrapper(DECIMAL_FORMAT.format(cd.getValue().getParamS()).trim()));
        tcParamS.setStyle("-fx-alignment: CENTER;");

        tcParamX.setCellValueFactory(cd -> new ReadOnlyStringWrapper(DECIMAL_FORMAT.format(cd.getValue().getParamX()).trim()));
        tcParamX.setStyle("-fx-alignment: CENTER;");

        tcNote.setCellValueFactory(new PropertyValueFactory<>("note"));
    }
}
