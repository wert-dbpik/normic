package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import ru.wert.normic.controllers.normsTableView.NormsTableViewController;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;


public class BXMaterialForTableView {

    final static String ALL = "Все";
    final static String LIST = "Листовой";
    final static String ROUND = "Круглый";
    final static String PROFILE = "Профильный";
    final static String PIECE = "Штучный";

    private NormsTableViewController controller;
    private ComboBox<String> bxMaterials;

    public void create(NormsTableViewController controller){
        this.controller = controller;
        this.bxMaterials = controller.getBxMaterials();

        updateItems();

        bxMaterials.getSelectionModel().select(ALL);

        bxMaterials.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) return;
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

    public void updateItems() {
        String selectedItem = bxMaterials.getValue();
        controller.getUsedMaterials().clear();
        controller.collectUsedMaterials(MAIN_OP_DATA);
        ObservableList items = FXCollections.observableArrayList();
        items.add(ALL);
        items.addAll(Arrays.stream(EMatType.values()).map(EMatType::getMatTypeName).collect(Collectors.toList()));

        items.add(new Separator());
        items.addAll(controller.getUsedMaterials().stream().map(Material::getName).sorted().collect(Collectors.toList()));


        bxMaterials.getItems().clear();
        bxMaterials.getItems().addAll(items);
        if(selectedItem != null) bxMaterials.setValue(selectedItem);
    }

}
