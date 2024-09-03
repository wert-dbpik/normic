package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import ru.wert.normic.controllers.normsTableView.NormsTableViewController;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class BXMaterialForTableView {

    final static String ALL = "Все";
    final static String LIST = "Листовой";
    final static String ROUND = "Круглый";
    final static String PROFILE = "Профильный";
    final static String PIECE = "Штучный";

    private Set<Material> usedMaterials;
    private ComboBox<String> bxMaterials;
    private List<NormsTableViewController.DetailTableRow> details;
    private TableView<NormsTableViewController.DetailTableRow> tableView;

    public final static String LISTOK = EMatType.LIST.getMatTypeName();

    public void create(NormsTableViewController controller){
        this.usedMaterials = controller.getUsedMaterials();
        this.bxMaterials = controller.getBxMaterials();
        this.details = controller.getDetails();

        ObservableList items = FXCollections.observableArrayList();

        items.add(ALL);
        items.addAll(Arrays.stream(EMatType.values()).map(EMatType::getMatTypeName).collect(Collectors.toList()));

        items.add(new Separator());
        items.addAll(usedMaterials.stream().map(Material::getName).sorted().collect(Collectors.toList()));


        bxMaterials.getItems().clear();
        bxMaterials.getItems().addAll(items);
        bxMaterials.getSelectionModel().select(ALL);

        bxMaterials.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case ALL:
                    controller.showAll();
                    break;
                case LIST:
                    controller.showLists();
                    break;
                case ROUND:
                    controller.showRounds();
                    break;
                case PROFILE:
                    controller.showProfiles();
                    break;
                case PIECE:
                    controller.showPieces();
                    break;
                default:
                    controller.showExactMaterial(newValue);

            }
        });
    }

    private void addChangeListener(){

    }
}
