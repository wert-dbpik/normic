package ru.wert.normic.materials;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import ru.wert.normic.components.BXMaterialGroups;
import ru.wert.normic.components.BXMaterialGroupsWithAll;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.material_group.MaterialGroup;
import ru.wert.normic.enums.EMatOperations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.wert.normic.NormicServices.QUICK_MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialsTVController {

    @FXML
    private ComboBox<MaterialGroup> bxGroups;

    @FXML
    private Button btnAdd;

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

    private MaterialGroup allMaterials = new MaterialGroup(0L, "Все");

    @FXML
    void initialize(){

        new BXMaterialGroupsWithAll().create(bxGroups);
        bxGroups.valueProperty().addListener((observable, oldValue, newGroup) -> {
            updateTableView(newGroup, null);
        });

        btnAdd.setTooltip(new Tooltip("Добавить материал"));
        btnAdd.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/add.png")), 32,32, true, true)));
        btnAdd.setStyle("-fx-padding: 0");
        btnAdd.setOnAction(this::addMaterial);

        initializeColumns();

        updateTableView(null, null);

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
                    changeMaterial(tableRow);
                }
            });

            return tableRow;
        });


    }

    public void updateTableView(MaterialGroup group, Material selectedMaterial){
        List<Material> items;

        tableView.getItems().clear();
        tableView.refresh();
        if(group == null || group.equals(allMaterials)){
            items = new ArrayList<>(QUICK_MATERIALS.findAll());
        } else {
            items = new ArrayList<>(QUICK_MATERIALS.findAllByGroupId(group.getId()));
        }
        Platform.runLater(()->{
            items.sort(Comparator.comparing(Material::getName));
            tableView.setItems(FXCollections.observableArrayList(items));
            if(selectedMaterial != null) {
                tableView.scrollTo(selectedMaterial);
                tableView.getSelectionModel().select(selectedMaterial);
            }
        });
    }

    public void addMaterial(Event event){
        final MaterialACCLoader materialACCLoader = new MaterialACCLoader(EMatOperations.ADD, tableView, null);
        final MaterialsACCController mainController = materialACCLoader.getMainController();
        mainController.init(this, null, null, EMatOperations.ADD);
    }

    public void copyMaterial(TableRow<Material> tableRow){
        final MaterialACCLoader materialACCLoader = new MaterialACCLoader(EMatOperations.COPY, tableView, tableRow);
        final MaterialsACCController mainController = materialACCLoader.getMainController();
        final MatTypeController matTypeController = materialACCLoader.getMatTypeController();
        mainController.init(this, tableRow.getItem(), matTypeController, EMatOperations.COPY);
    }

    public void changeMaterial(TableRow<Material> tableRow){
        final MaterialACCLoader materialACCLoader = new MaterialACCLoader(EMatOperations.CHANGE, tableView, tableRow);
        final MaterialsACCController mainController = materialACCLoader.getMainController();
        final MatTypeController matTypeController = materialACCLoader.getMatTypeController();
        mainController.init(this, tableRow.getItem(), matTypeController, EMatOperations.CHANGE);
    }

    public void deleteMaterial(TableRow<Material> tableRow){
        tableView.getItems().remove(tableRow.getItem());
    }
}
