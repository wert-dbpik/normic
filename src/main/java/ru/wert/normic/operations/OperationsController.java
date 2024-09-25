package ru.wert.normic.operations;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.decoration.warnings.Warning2;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperationServiceImpl;
import ru.wert.normic.enums.ECommands;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.menus.MenuForm;


import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class OperationsController {

    @FXML
    private VBox vbMainContainer;

    @FXML
    private TableView<SimpleOperation> tableView;

    @FXML
    private TableColumn<SimpleOperation, String> tcName;

    @FXML
    private TableColumn<SimpleOperation, String> tcNormType;

    @FXML
    private TableColumn<SimpleOperation, String> tcJobType;

    @FXML
    private TableColumn<SimpleOperation, String> tcMeasurement;

    @FXML
    private TableColumn<SimpleOperation, String> tcNormTime;

    @FXML
    private TableColumn<SimpleOperation, String> tcCountMaterial;

    @FXML
    private TableColumn<SimpleOperation, String> tcDescription;

    public void init(){
        initializeColumns();

        ObservableList<SimpleOperation> ops = FXCollections.observableArrayList(SimpleOperationServiceImpl.getInstance().findAll());
        tableView.getItems().addAll(ops);

    }

    private void initializeColumns() {
        tcName.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getName().toUpperCase()));
        tcName.setStyle("-fx-alignment: CENTER-LEFT;");

        tcNormType.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getNormType().getNormName()));
        tcNormType.setStyle("-fx-alignment: CENTER;");

        tcJobType.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getJobType().getJobName()));
        tcJobType.setStyle("-fx-alignment: CENTER;");

        tcMeasurement.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getMeasurement().getMeasureName()));
        tcMeasurement.setStyle("-fx-alignment: CENTER;");

        tcNormTime.setCellValueFactory(cd -> new ReadOnlyStringWrapper(DECIMAL_FORMAT.format(cd.getValue().getNorm()).trim()));
        tcNormTime.setStyle("-fx-alignment: CENTER;");

        tcCountMaterial.setCellValueFactory(cd->{
            boolean val = cd.getValue().isCountMaterial();
            return new ReadOnlyStringWrapper(val ? "Считать" : "Не считать");
        });
        tcCountMaterial.setStyle("-fx-alignment: CENTER;");

        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        if(tableView.getItems().isEmpty())
            tableView.setOnMouseClicked(e->{
                if(e.getButton() == MouseButton.SECONDARY)
                    tableView.setContextMenu(new OperationsContextMenu(this, null));
            });

        tableView.setRowFactory(tableView-> {
            final TableRow<SimpleOperation> tableRow = new TableRow<>();

            tableRow.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    changeSimpleOperation(tableRow);
                }
                if(e.getButton() == MouseButton.SECONDARY){
                    if(tableRow.getItem() == null) {
                        tableView.getSelectionModel().clearSelection();
                    }
                    tableRow.setContextMenu(new OperationsContextMenu(this, tableRow));
                }
            });

            return tableRow;
        });

    }

    public void updateTableView(SimpleOperation selectedSimpleOperation){
        if(tableView == null) return;
        tableView.getItems().clear();
        tableView.refresh();

        ObservableList<SimpleOperation> items = FXCollections.observableArrayList(SimpleOperationServiceImpl.getInstance().findAll());
        Platform.runLater(()->{
            tableView.setItems(FXCollections.observableArrayList(items));
            if(selectedSimpleOperation != null) {
                tableView.scrollTo(selectedSimpleOperation);
                tableView.getSelectionModel().select(selectedSimpleOperation);
            }
        });
    }

    public void addSimpleOperation(Event event){
        final OperationsACCLoader operationACCLoader = new OperationsACCLoader(ECommands.ADD, tableView, null);
        final OperationsACCController mainController = operationACCLoader.getMainController();
        mainController.init(ENormType.NORM_ASSEMBLING, this, null, ECommands.ADD);
    }

    public void copySimpleOperation(TableRow<SimpleOperation> tableRow){
        final OperationsACCLoader operationACCLoader = new OperationsACCLoader(ECommands.COPY, tableView, tableRow);
        final OperationsACCController mainController = operationACCLoader.getMainController();
        mainController.init(ENormType.NORM_ASSEMBLING, this, tableRow.getItem(), ECommands.COPY);
    }

    public void changeSimpleOperation(TableRow<SimpleOperation> tableRow){
        final OperationsACCLoader operationACCLoader = new OperationsACCLoader(ECommands.CHANGE, tableView, tableRow);
        final OperationsACCController mainController = operationACCLoader.getMainController();
        mainController.init(ENormType.NORM_ASSEMBLING, this, tableRow.getItem(), ECommands.CHANGE);
    }

    public void deleteSimpleOperation(Event e, TableRow<SimpleOperation> tableRow){
        SimpleOperation deletedSimpleOperation = tableRow.getItem();
        boolean ans = Warning2.create(
                e,
                "Внимание!",
                String.format( "Вы уверены, что нужно удалить '%s'?", deletedSimpleOperation.getName()),
                "Восстановить будет невозможно!");
        if(ans){
            boolean res = SimpleOperationServiceImpl.getInstance().delete(deletedSimpleOperation);
            if(!res)
                Warning1.create(e, "Ошибка!",
                        "Удалить '%s' не получилось!",
                        "Материал используется или сервер не дотупен");
            else
                updateTableView(null);
        }


    }


}
