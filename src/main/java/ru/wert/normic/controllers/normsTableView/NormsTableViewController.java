package ru.wert.normic.controllers.normsTableView;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.wert.normic.components.BXMaterialForTableView;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EMatType;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.NormicServices.MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class NormsTableViewController {

    @FXML
    private HBox materials;

    @FXML@Getter
    private ComboBox<String> bxMaterials;

    @FXML
    private TextField tfSumWeight;

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
    private final ObservableSet<DetailTableRow> details = FXCollections.observableSet();
    @Getter
    private final Set<Material> usedMaterials = new HashSet<>();

    private BXMaterialForTableView bx;


    public void init(OpAssm assm, int amount){
        initColumns();
        getListOfDetails(assm, amount); //ObservableSet<DetailTableRow> details
        showAll();
        sortTableViewAscending();

        tableView.setRowFactory(param -> {
            final TableRow<DetailTableRow> row = new TableRow<>();
            row.setOnMouseClicked(e->{
                if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                    openFormEditor(row.getItem().opData, row);
                }
            });
            return row;
        });

        bx = new BXMaterialForTableView();
        bx.create(this);

    }

    /**
     * Открыть форму редактирования сборки
     */
    public void openFormEditor(OpDetail opData, TableRow<DetailTableRow> row) {
        int prevAmount = row.getItem().amount / row.getItem().opData.getQuantity();
        int index = row.getIndex();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formDetail.fxml"));
            Parent parent = loader.load();
            FormDetailController formController = loader.getController();
            formController.init(opData);
            Decoration windowDecoration = new Decoration(
                    "ДЕТАЛЬ",
                    parent,
                    true,
                    MAIN_STAGE,
                    "decoration-assm",
                    false,
                    false);
            ImageView closeWindow = windowDecoration.getImgCloseWindow();
            closeWindow.setOnMousePressed(ev -> {
                opData.setName(formController.getTfDetailName().getText().trim());
                opData.setMaterial(formController.getCmbxMaterial().getValue());
                opData.setQuantity(Integer.parseInt(formController.getTfDetailQuantity().getText()));
                opData.setDone(formController.getDone().getStateProperty().getValue());
                opData.setParamA(formController.getMatPatchController().getParamA());
                opData.setParamB(formController.getMatPatchController().getParamB());
                opData.setParamC(formController.getMatPatchController().getParamC());
                opData.setWasteRatio(formController.getMatPatchController().getWasteRatio());

                tableView.getItems().set(index, fillRowWithData(prevAmount, opData));
                tableView.refresh();
                tableView.getSelectionModel().select(index);

                MAIN_CONTROLLER.recountMainOpData();
                MAIN_CONTROLLER.recountPainting(MAIN_OP_DATA, 1);

                //Перезаполняем сет из используемых материалов
                usedMaterials.clear();
                getListOfDetails(MAIN_OP_DATA, 1);
                bx.updateItems();

            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод собирает все изготавливаемые детали
     */
    public void getListOfDetails(OpAssm assm, int amount) {
        List<OpData> ops = assm.getOperations();

        for(OpData op : ops){
            if(op instanceof OpAssm) getListOfDetails((OpAssm) op, ((OpData)op).getQuantity() * amount);
            if(op instanceof OpDetail){
                DetailTableRow row = fillRowWithData(amount, op);
                details.add(row);
            }
        }
    }

    /**
     * Метод собирает материалы, применяемые в изделии
     */
    public void collectUsedMaterials(OpAssm assm) {
        List<OpData> ops = assm.getOperations();

        for(OpData op : ops){
            if(op instanceof OpAssm) collectUsedMaterials((OpAssm) op);
            if(op instanceof OpDetail){
                usedMaterials.add(((OpDetail) op).getMaterial());
            }
        }

    }

    /**
     * Меод возвращает строку таблицы с данными DetailTableRow
     */
    private DetailTableRow fillRowWithData(int amount, OpData op) {
        DetailTableRow row = new DetailTableRow();

        row.name = ((OpDetail) op).getName();
        row.amount = op.getQuantity() * amount;
        row.material = ((OpDetail) op).getMaterial();
        row.weight = ((OpDetail) op).getWeight();
        row.sumWeight = row.weight * row.amount ;
        row.paramA = ((OpDetail) op).getParamA();
        row.paramB = ((OpDetail) op).getParamB();
        row.paramC = ((OpDetail) op).getParamC();
        row.opData = (OpDetail) op;
        return row;
    }

    /**
     * Инициализация колонок
     */
    private void initColumns(){

        //НАИМЕНОВАНИЕ
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        //КОЛИЧЕСТВО
        tcQuantity.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcQuantity.setStyle("-fx-alignment: CENTER;");

        //МАТЕРИАЛ
        tcMaterial.setCellValueFactory(cd->{
            String name = cd.getValue().material.getName();
            return new ReadOnlyStringWrapper(name);
        });
        tcMaterial.sortTypeProperty().addListener(o->{
            if (tcMaterial.getSortType().equals(TableColumn.SortType.DESCENDING)) {
                sortTableViewDescending();
            } else
                sortTableViewAscending();
        });

        //ВЕС ОДНОЙ ДЕТАЛИ
        tcWeight.setCellValueFactory(cd->{
            double val = cd.getValue().weight;
            return new ReadOnlyStringWrapper(format(DOUBLE_FORMAT, val));
        });
        tcWeight.setStyle("-fx-alignment: CENTER;");

        //СУММАРНЫЙ ВЕС
        tcSumWeight.setCellValueFactory(new PropertyValueFactory<>("sumWeight"));
        tcSumWeight.setCellValueFactory(cd->{
            double val = cd.getValue().sumWeight;
            return new ReadOnlyStringWrapper(format(DOUBLE_FORMAT, val));
        });
        tcSumWeight.setStyle("-fx-alignment: CENTER;");

        //ПАРАМЕТР А
        tcParamA.setCellValueFactory(cd->{
            int val = cd.getValue().paramA;
            String str = val == 0 ? "" : String.valueOf(val);
            return new ReadOnlyStringWrapper(str);
        });
        tcParamA.setStyle("-fx-alignment: CENTER;");

        //ПАРАМЕТР В
        tcParamB.setCellValueFactory(cd->{
            int val = cd.getValue().paramB;
            String str = val == 0 ? "" : String.valueOf(val);
            return new ReadOnlyStringWrapper(str);
        });
        tcParamB.setStyle("-fx-alignment: CENTER;");

        //ПАРАМЕТР С
        tcParamC.setCellValueFactory(cd->{
            int val = cd.getValue().paramC;
            String str = val == 0 ? "" : String.valueOf(val);
            return new ReadOnlyStringWrapper(str);
        });
        tcParamC.setStyle("-fx-alignment: CENTER;");

    }

    private void sortTableViewAscending() {
        tableView.getSortOrder().clear();
        tableView.getSortOrder().addAll(tcMaterial,tcName);
        tcMaterial.setSortType(TableColumn.SortType.ASCENDING);
        tcName.setSortType(TableColumn.SortType.ASCENDING);
        tableView.sort();
    }

    private void sortTableViewDescending() {
        tableView.getSortOrder().clear();
        tableView.getSortOrder().addAll(tcMaterial,tcName);
        tcMaterial.setSortType(TableColumn.SortType.DESCENDING);
        tcName.setSortType(TableColumn.SortType.ASCENDING);
        tableView.sort();
    }

    /**
     * Обновление таблицы
     */
    private void updateTableView(ObservableSet<DetailTableRow> showingItems){
        tableView.getItems().clear();
        tableView.getItems().addAll(showingItems);

        double total = 0 ;
        for (DetailTableRow row : tableView.getItems()) {
            total += + row.getSumWeight();
        }
        tfSumWeight.setText(String.format(DOUBLE_FORMAT, total));
    }


    /**
     * Показать ВСЕ
     */
    public void showAll(){
        updateTableView(details);
    }

    /**
     * Показать детали из ЛИСТОВОГО материала
     */
    public void showLists(){
        ObservableSet<DetailTableRow> showingItems = FXCollections.observableSet();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.LIST.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    /**
     * Показать детали из КРУГЛОГО материала
     */
    public void showRounds(){
        ObservableSet<DetailTableRow> showingItems = FXCollections.observableSet();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.ROUND.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    /**
     * Показать детали из ПРОФИЛЬНОГО материала
     */
    public void showProfiles(){
        ObservableSet<DetailTableRow> showingItems = FXCollections.observableSet();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.PROFILE.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    /**
     * Показать детали из ШТУЧНОГО материала
     */
    public void showPieces(){
        ObservableSet<DetailTableRow> showingItems = FXCollections.observableSet();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getMatType().getName().equals(EMatType.PIECE.getMatTypeName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
    }

    /**
     * Показать определенный материал
     */
    public void showExactMaterial(String materialName){
        Material exactMaterial = MATERIALS.findByName(materialName);
        if(exactMaterial == null) return;
        ObservableSet<DetailTableRow> showingItems = FXCollections.observableSet();
        for(DetailTableRow detail : details){
            if(detail.getMaterial().getName().equals(exactMaterial.getName()))
                showingItems.add(detail);
        }
        updateTableView(showingItems);
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


}
