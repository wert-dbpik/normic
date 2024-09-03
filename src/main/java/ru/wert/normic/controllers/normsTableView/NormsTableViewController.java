package ru.wert.normic.controllers.normsTableView;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.wert.normic.components.BXMaterialForTableView;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EMatType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ru.wert.normic.NormicServices.MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;

public class NormsTableViewController {

    @FXML
    private HBox materials;

    @FXML@Getter
    private ComboBox<String> bxMaterials;

    @FXML
    private TableView<DetailTableRow> tableView;

    @FXML
    private TableColumn<DetailTableRow, String> tcName;

    @FXML
    private TableColumn<DetailTableRow, Integer> tcQuantity;

    @FXML
    private TableColumn<DetailTableRow, String> tcMaterial;

    @FXML
    private TableColumn<DetailTableRow, String> tcWeight;

    @FXML
    private TableColumn<DetailTableRow, String> tcSumWeight;

    @FXML
    private TableColumn<DetailTableRow, String> tcParamA;

    @FXML
    private TableColumn<DetailTableRow, String> tcParamB;

    @FXML
    private TableColumn<DetailTableRow, String> tcParamC;

    @FXML
    private Button btn; //НЕ ИСПОЛЬЗУЕТСЯ
    @Getter
    private final ObservableList<DetailTableRow> details = FXCollections.observableArrayList();
    @Getter
    private final Set<Material> usedMaterials = new HashSet<>();


    public void init(OpAssm assm, int amount){

        getListOfDetails(assm, amount);
        ObservableList<DetailTableRow> data = FXCollections.observableArrayList(details);

        initColumns();
        tableView.setItems(data);

        new BXMaterialForTableView().create(this);

    }

    private void getListOfDetails(OpAssm assm, int amount) {
        List<OpData> ops = assm.getOperations();

        for(OpData op : ops){
            if(op instanceof OpAssm) getListOfDetails((OpAssm) op, ((OpData)op).getQuantity() * amount);
            if(op instanceof OpDetail){
                DetailTableRow row = new DetailTableRow();

                row.name = ((OpDetail) op).getName();
                row.amount = op.getQuantity() * amount;
                row.material = ((OpDetail) op).getMaterial();
                row.weight = ((OpDetail) op).getWeight();
                row.sumWeight = row.weight * row.amount ;
                row.paramA = ((OpDetail) op).getParamA();
                row.paramB = ((OpDetail) op).getParamB();
                row.paramC = ((OpDetail) op).getParamC();

                details.add(row);

                usedMaterials.add(((OpDetail) op).getMaterial());
            }
        }

    }

    private void initColumns(){

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tcQuantity.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcQuantity.setStyle("-fx-alignment: CENTER;");

        tcMaterial.setCellValueFactory(cd->{
            String name = cd.getValue().material.getName();
            return new ReadOnlyStringWrapper(name);
        });

        tcWeight.setCellValueFactory(cd->{
            double val = cd.getValue().weight;
            return new ReadOnlyStringWrapper(format(DOUBLE_FORMAT, val));
        });
        tcWeight.setStyle("-fx-alignment: CENTER;");

        tcSumWeight.setCellValueFactory(new PropertyValueFactory<>("sumWeight"));
        tcSumWeight.setCellValueFactory(cd->{
            double val = cd.getValue().sumWeight;
            return new ReadOnlyStringWrapper(format(DOUBLE_FORMAT, val));
        });
        tcSumWeight.setStyle("-fx-alignment: CENTER;");

        tcParamA.setCellValueFactory(cd->{
            int val = cd.getValue().paramA;
            String str = val == 0 ? "" : String.valueOf(val);
            return new ReadOnlyStringWrapper(str);
        });
        tcParamA.setStyle("-fx-alignment: CENTER;");

        tcParamB.setCellValueFactory(cd->{
            int val = cd.getValue().paramB;
            String str = val == 0 ? "" : String.valueOf(val);
            return new ReadOnlyStringWrapper(str);
        });
        tcParamB.setStyle("-fx-alignment: CENTER;");

        tcParamC.setCellValueFactory(cd->{
            int val = cd.getValue().paramC;
            String str = val == 0 ? "" : String.valueOf(val);
            return new ReadOnlyStringWrapper(str);
        });
        tcParamC.setStyle("-fx-alignment: CENTER;");

    }

    private void updateTableView(ObservableList<DetailTableRow> showingItems){
        tableView.getItems().clear();
        tableView.setItems(showingItems);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailTableRow{

        private String name;
        private int amount;
        private Material material;
        private double sumWeight;
        private double weight;
        private int paramA;
        private int paramB;
        private int paramC;

        private OpDetail opData;

    }

    public void showAll(){
        updateTableView(details);
    }

    public void showLists(){
        ObservableList<DetailTableRow> showingItems = FXCollections.observableArrayList();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.LIST.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    public void showRounds(){
        ObservableList<DetailTableRow> showingItems = FXCollections.observableArrayList();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.ROUND.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    public void showProfiles(){
        ObservableList<DetailTableRow> showingItems = FXCollections.observableArrayList();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.PROFILE.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    public void showPieces(){
        ObservableList<DetailTableRow> showingItems = FXCollections.observableArrayList();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.PIECE.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    public void showExactMaterial(String materialName){
        Material exactMaterial = MATERIALS.findByName(materialName);
        if(exactMaterial == null) return;
        ObservableList<DetailTableRow> showingItems = FXCollections.observableArrayList();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getName().equals(exactMaterial.getName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }




}
