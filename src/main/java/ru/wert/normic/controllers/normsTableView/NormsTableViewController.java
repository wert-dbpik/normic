package ru.wert.normic.controllers.normsTableView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;

import java.util.ArrayList;
import java.util.List;

public class NormsTableViewController {

    @FXML
    private HBox materials;

    @FXML
    private ComboBox<Material> bxMaterials;

    @FXML
    private TableView<DetailTableRow> tableView;

    @FXML
    private TableColumn<DetailTableRow, String> tcName;

    @FXML
    private TableColumn<DetailTableRow, Integer> tcQuantity;

    @FXML
    private TableColumn<DetailTableRow, String> tcMaterial;

    @FXML
    private TableColumn<DetailTableRow, String> tcParamA;

    @FXML
    private TableColumn<DetailTableRow, String> tcParamB;

    @FXML
    private TableColumn<DetailTableRow, String> tcParamC;

    @FXML
    private Button btn; //НЕ ИСПОЛЬЗУЕТСЯ

    private final List<DetailTableRow> details = new ArrayList<>();



    public void init(OpAssm assm, int amount){

        getListOfDetails(assm, amount);
        ObservableList<DetailTableRow> data = FXCollections.observableArrayList(details);

        initColumns();
        tableView.setItems(data);
    }

    private void getListOfDetails(OpAssm assm, int amount) {
        List<OpData> ops = assm.getOperations();

        for(OpData op : ops){
            if(op instanceof OpAssm) getListOfDetails((OpAssm) op, amount * ((OpData)op).getQuantity());
            if(op instanceof OpDetail){
                DetailTableRow row = new DetailTableRow();
                row.name = ((OpDetail) op).getName();
                row.amount = op.getQuantity() * amount;
                row.material = ((OpDetail) op).getMaterial().getName();
                row.paramA = ((OpDetail) op).getParamA();
                row.paramB = ((OpDetail) op).getParamB();
                row.paramC = ((OpDetail) op).getParamC();


                details.add(row);
            }
        }

    }

    private void initColumns(){

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tcQuantity.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcQuantity.setStyle("-fx-alignment: CENTER;");

        tcMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));

        tcParamA.setCellValueFactory(new PropertyValueFactory<>("paramA"));
        tcParamA.setStyle("-fx-alignment: CENTER;");

        tcParamB.setCellValueFactory(new PropertyValueFactory<>("paramB"));
        tcParamB.setStyle("-fx-alignment: CENTER;");

        tcParamC.setCellValueFactory(new PropertyValueFactory<>("paramC"));
        tcParamC.setStyle("-fx-alignment: CENTER;");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailTableRow{

        private String name;
        private int amount;
        private String material;
        private int paramA;
        private int paramB;
        private int paramC;

    }


}
