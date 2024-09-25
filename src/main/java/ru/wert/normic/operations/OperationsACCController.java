package ru.wert.normic.operations;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import ru.wert.normic.components.*;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperationServiceImpl;
import ru.wert.normic.enums.ECommands;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.utils.DoubleParser;

import java.util.List;

import static java.lang.String.format;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;

public class OperationsACCController {

    @FXML
    private TextField tfName;

    @FXML
    private ComboBox<ENormType> bxNormType;

    @FXML
    private ComboBox<EJobType> bxJobType;

    @FXML
    private ComboBox<EPieceMeasurement> bxMeasurement;

    @FXML
    private TextField tfNormTime;

    @FXML
    private CheckBox chbxCountMaterial;

    @FXML
    private TextArea taDescription;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOK;
    
    private SimpleOperation oldSimpleOperation;
    private OperationsController tableViewController;
    private ECommands command;


    public void init(ENormType normType, OperationsController tableViewController, SimpleOperation oldSimpleOperation, ECommands command){
        this.tableViewController = tableViewController;
        this.oldSimpleOperation = oldSimpleOperation;
        this.command = command;

        new BXNormType().create(bxNormType, normType);
        new BXJobType().create(bxJobType);
        new BXPieceMeasurement().create(bxMeasurement);
        new TFDouble(tfNormTime);

        bxJobType.disableProperty().bind(bxNormType.valueProperty().isNotEqualTo(ENormType.NORM_MECHANICAL));

        if(command.equals(ECommands.COPY) || command.equals(ECommands.CHANGE)) fillData();

    }

    @FXML
    void cancel(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void ok(Event event) {
        SimpleOperation selectedSimpleOperation = null;
        if(command.equals(ECommands.ADD) || command.equals(ECommands.COPY)){
            if(!checkData()) return;
            SimpleOperation newSimpleOperation = creatNewSimpleOperation();
            if(!isDuplicated(newSimpleOperation, null)){
                selectedSimpleOperation = SimpleOperationServiceImpl.getInstance().save(newSimpleOperation);
                if(selectedSimpleOperation == null)
                    Warning1.create(event, "Ошибка!",
                            "Не удалось сохранить операцию!",
                            "Возможно, сервер не доступен");
            }

            else Warning1.create(event, "Ошибка!",
                    "Такая операция уже существует!",
                    "Операция должна быть уникальной");

        }
        else if(command.equals(ECommands.CHANGE) ){
            if(!checkData()) return;
            SimpleOperation newSimpleOperation = creatNewSimpleOperation();
            if(!isDuplicated(newSimpleOperation, oldSimpleOperation)){
                updateOldSimpleOperation(newSimpleOperation);
                boolean res = SimpleOperationServiceImpl.getInstance().update(oldSimpleOperation);
                if(!res)
                    Warning1.create(event, "Ошибка!",
                            "Не удалось сохранить материал!",
                            "Возможно, сервер не доступен");
            }
            else Warning1.create(event, "Ошибка!",
                    "Такой материал уже существует!",
                    "Материал должен быть уникальным");

        }
        SimpleOperation finalSelectedSimpleOperation = selectedSimpleOperation;

        //Добавление операции возможно и без открытия таблицы
        //Если окно с таблицей создано, то его нужно обновить
        tableViewController.updateTableView(finalSelectedSimpleOperation);
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private boolean checkData(){
        if(tfName.getText().isEmpty()) return false;
        else if(bxNormType.getValue() == null) return false;
        else if(bxNormType.getValue().equals(ENormType.NORM_MECHANICAL) && bxJobType.getValue() == null) return false;
        else if(bxMeasurement.getValue() == null) return false;
        else if(tfNormTime.getText().isEmpty()) return false;
        return true;
    }

    /**
     * Проверка на дублирование при изменении записи
     * Запись проверяется со списком уже существующих записей
     * Имя изменяемой записи из проверки исключается
     */
    protected boolean isDuplicated(SimpleOperation newItem, SimpleOperation oldItem){

        //Из листа удаляется выделенная запись (старая)
        List<SimpleOperation> items = SimpleOperationServiceImpl.getInstance().findAll();
        if(oldItem != null)items.remove(oldItem);

        //Теперь новая запись сравнивается только с оставшимися записями
        for (Object u : items)
            if (newItem.equals(u)) return true;
        return false;
    }

    private SimpleOperation creatNewSimpleOperation(){
        SimpleOperation newSimpleOperation = new SimpleOperation();
        newSimpleOperation.setName(tfName.getText().trim().toUpperCase());
        newSimpleOperation.setNormType(bxNormType.getValue());
        newSimpleOperation.setJobType(bxJobType.isDisabled() ? EJobType.JOB_NONE : bxJobType.getValue());
        newSimpleOperation.setMeasurement(bxMeasurement.getValue());
        newSimpleOperation.setNorm(DoubleParser.getValue(tfNormTime));
        newSimpleOperation.setCountMaterial(chbxCountMaterial.isSelected());
        newSimpleOperation.setDescription(taDescription.getText().trim());

        return newSimpleOperation;
    }

    private void fillData(){
        tfName.setText(oldSimpleOperation.getName().toUpperCase());
        bxNormType.setValue(oldSimpleOperation.getNormType());
        bxJobType.setValue(oldSimpleOperation.getJobType());
        bxMeasurement.setValue(oldSimpleOperation.getMeasurement());
        tfNormTime.setText(format(DOUBLE_FORMAT, oldSimpleOperation.getNorm()));
        chbxCountMaterial.setSelected(oldSimpleOperation.isCountMaterial());
        taDescription.setText(oldSimpleOperation.getDescription());

    }

    private void updateOldSimpleOperation(SimpleOperation newSimpleOperation) {

        oldSimpleOperation.setName(newSimpleOperation.getName());
        oldSimpleOperation.setNormType(newSimpleOperation.getNormType());
        oldSimpleOperation.setJobType(newSimpleOperation.getJobType());
        oldSimpleOperation.setMeasurement(newSimpleOperation.getMeasurement());
        oldSimpleOperation.setNorm(newSimpleOperation.getNorm());
        oldSimpleOperation.setCountMaterial(newSimpleOperation.isCountMaterial());
        oldSimpleOperation.setDescription(newSimpleOperation.getDescription());
    }
}
