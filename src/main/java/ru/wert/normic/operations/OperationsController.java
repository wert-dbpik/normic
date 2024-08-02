package ru.wert.normic.operations;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.decoration.warnings.Warning2;
import ru.wert.normic.entities.db_connection.operation.SimpleOperation;
import ru.wert.normic.entities.db_connection.othersOps.SimpleOperation;
import ru.wert.normic.entities.db_connection.othersOps.SimpleOperationService;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ECommands;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.operations.MatTypeController;
import ru.wert.normic.operations.SimpleOperationACCLoader;
import ru.wert.normic.operations.SimpleOperationsACCController;

import static ru.wert.normic.NormicServices.QUICK_MATERIALS;

public class OperationsController {

    @FXML
    private VBox vbMainContainer;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<SimpleOperation, String> tcName;

    @FXML
    private TableColumn<SimpleOperation, ENormType> tcNormType;

    @FXML
    private TableColumn<SimpleOperation, EJobType> tcJobType;

    @FXML
    private TableColumn<SimpleOperation, EPieceMeasurement> tcMeasurement;

    @FXML
    private TableColumn<SimpleOperation, Boolean> tcNormTime;

    @FXML
    private TableColumn<SimpleOperation, String> tcDescription;

    public void init(){

        ObservableList<SimpleOperation> ops = FXCollections.observableArrayList(SimpleOperationService.getInstance().getAllSimpleOps());
        tableView.getItems().addAll(ops);

    }

    public void updateTableView(SimpleOperation selectedSimpleOperation){

        tableView.getItems().clear();
        tableView.refresh();

        ObservableList<SimpleOperation> items = FXCollections.observableArrayList(SimpleOperationService.getInstance().getAllSimpleOps());
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
        mainController.init(this, null, null, ECommands.ADD);
    }

    public void copySimpleOperation(TableRow<SimpleOperation> tableRow){
        final OperationsACCLoader operationACCLoader = new OperationsACCLoader(ECommands.COPY, tableView, tableRow);
        final OperationsACCController mainController = operationACCLoader.getMainController();
        mainController.init(this, tableRow.getItem(), ECommands.COPY);
    }

    public void changeSimpleOperation(TableRow<SimpleOperation> tableRow){
        final OperationsACCLoader operationACCLoader = new OperationsACCLoader(ECommands.CHANGE, tableView, tableRow);
        final OperationsACCController mainController = operationACCLoader.getMainController();
        mainController.init(this, tableRow.getItem(), ECommands.CHANGE);
    }

    public void deleteSimpleOperation(Event e, TableRow<SimpleOperation> tableRow){
        SimpleOperation deletedSimpleOperation = tableRow.getItem();
        boolean ans = Warning2.create(
                e,
                "Внимание!",
                String.format( "Вы уверены, что нужно удалить '%s'?", deletedSimpleOperation.getName()),
                "Восстановить будет невозможно!");
        if(ans){
            boolean res = SimpleOperationService.getInstance().delete(deletedSimpleOperation);
            if(!res)
                Warning1.create(e, "Ошибка!",
                        "Удалить '%s' не получилось!",
                        "Материал используется или сервер не дотупен");
            else
                updateTableView(null);
        }


    }


}
