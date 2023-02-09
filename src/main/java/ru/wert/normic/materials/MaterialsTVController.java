package ru.wert.normic.materials;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatOperations;

import java.util.List;

import static ru.wert.normic.NormicServices.CH_QUICK_MATERIALS;
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

        tableView.setRowFactory(tableView-> {
            final TableRow<Material> tableRow = new TableRow<>();
            final ContextMenu menu = new MaterialContextMenu(this, tableRow);

            tableRow.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(tableRow.itemProperty())).then(menu)
                    .otherwise((ContextMenu) null));

            tableRow.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {

                }
            });

            return tableRow;
        });


    }

    public void addMaterial(){
        final MaterialACCLoader materialACCLoader = new MaterialACCLoader(EMatOperations.ADD, tableView, null);
    }

    public void copyMaterial(TableRow<Material> tableRow){
        final MaterialACCLoader materialACCLoader = new MaterialACCLoader(EMatOperations.COPY, tableView, tableRow);
        final MaterialsACCController mainController = materialACCLoader.getMainController();
        final MatTypeController matTypeController = materialACCLoader.getMatTypeController();
        mainController.init(tableRow.getItem(), matTypeController);
    }

    public void changeMaterial(TableRow<Material> tableRow){
        final MaterialACCLoader materialACCLoader = new MaterialACCLoader(EMatOperations.CHANGE, tableView, tableRow);
        final MaterialsACCController mainController = materialACCLoader.getMainController();
        final MatTypeController matTypeController = materialACCLoader.getMatTypeController();
        mainController.init(tableRow.getItem(), matTypeController);
    }

    public void deleteMaterial(TableRow<Material> tableRow){
        tableView.getItems().remove(tableRow.getItem());
    }
}
