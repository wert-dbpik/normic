package ru.wert.normic.materials;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import ru.wert.datapik.chogori.common.commands.ItemCommands;
import ru.wert.datapik.chogori.common.components.BXMatType;
import ru.wert.datapik.chogori.common.contextMenuACC.FormView_ACCController;
import ru.wert.datapik.chogori.common.interfaces.IFormView;
import ru.wert.datapik.chogori.common.tableView.CatalogTableView;
import ru.wert.datapik.chogori.statics.AppStatic;
import ru.wert.datapik.client.entity.models.*;
import ru.wert.datapik.winform.enums.EOperation;

import java.io.IOException;
import java.util.ArrayList;

import static ru.wert.datapik.chogori.application.services.ChogoriServices.*;

public class Material_ACCController extends FormView_ACCController<Material> {

    @FXML
    private StackPane spForCalculation;

    @FXML
    private TextField tfMaterialName;

    @FXML
    private ComboBox<MatType> bxMatType;

    @FXML
    private TextArea taMaterialNote;

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private StackPane spIndicator;

    private MaterialGroup defaultGroup;
    private MaterialGroup group;


    private static final String list = "/chogori-fxml/materials/material_list.fxml";
    private static final String round = "/chogori-fxml/materials/material_round.fxml";
    private static final String profile = "/chogori-fxml/materials/material_profile.fxml";

    private MaterialCommonTypeController matComTypeController;


    @FXML
    void cancel(ActionEvent event) {
        super.cancelPressed(event);
    }

    @FXML
    void ok(ActionEvent event) {
        super.okPressed(event, spIndicator, btnOk);
    }

    @FXML
    void initialize() {
        AppStatic.createSpIndicator(spIndicator);
    }

    @Override
    public void init(EOperation operation, IFormView<Material> formView, ItemCommands<Material> commands){
        super.initSuper(operation, formView, commands, CH_MATERIALS);

        //Find default group for later
        defaultGroup = CH_MATERIAL_GROUPS.findByName("Разное");

        //Определяем нулевую группу в корне дерева, т.к. она нам нужна при нажатии на кнопку GLOBE
        MaterialGroup rootGroup = ((Material_TableView) formView).getRootItem().getValue();
        bxMatType = new BXMatType().create(bxMatType);

        //Инициализация комбобокса
        bxMatType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch(newValue.getName()){
                case  "Листовой"   : createMatTypePane(list); break;
                case  "Круглый"    : createMatTypePane(round); break;
                case  "Профильный" : createMatTypePane(profile); break;
                default: break;
            }
            matComTypeController.init(this, tfMaterialName, bxMatType, taMaterialNote);
        });

        //Устанавливаем начальные значения полей в зависимости от operation
        createMatTypePane(list);
        defaultGroup = CH_MATERIAL_GROUPS.findByName("Разное");
        group = ((CatalogTableView<Material, MaterialGroup>)formView).
                findChosenGroup(operation, (CatalogTableView<Material, MaterialGroup>) formView, defaultGroup);
        matComTypeController.setCatalogGroup(group);

        setInitialValues();

    }

    /**
     * Создание и вставка панели с расчетом норм материала
     * @param matType - ссылка на FXML файл
     */
    public void createMatTypePane(String matType){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(matType));
            Parent matTypePane = loader.load();
            matComTypeController = loader.getController();
            matComTypeController.init(
                    this,
                    tfMaterialName,
                    bxMatType,
                    taMaterialNote);
            spForCalculation.getChildren().clear();
            spForCalculation.getChildren().add(matTypePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getNotNullFields() {
        return matComTypeController.getNotNullFields();
    }

    @Override
    public Material getNewItem() {

        return matComTypeController.getNewItem();
    }

    @Override
    public Material getOldItem() {
        return formView.getAllSelectedItems().get(0);
    }

    @Override
    public void fillFieldsOnTheForm(Material oldItem) {
        tfMaterialName.setText(oldItem.getName());
        bxMatType.getSelectionModel().select(oldItem.getMatType());
        System.out.println(bxMatType.getSelectionModel().getSelectedItem().getName());
        taMaterialNote.setText(oldItem.getNote());

        //Оставшиеся поля заполняем в зависимости от расчетного типа материала
        matComTypeController.fillFieldsOnTheForm(oldItem);
    }

    @Override
    public void changeOldItemFields(Material oldItem) {
        oldItem.setName(tfMaterialName.getText());
        oldItem.setMatType(bxMatType.getValue());
        oldItem.setNote(taMaterialNote.getText());

        //Оставшиеся поля заполняем в зависимости от расчетного типа материала
        matComTypeController.changeOldItemFields(oldItem);
    }

    @Override
    public void showEmptyForm() {
        tfMaterialName.setText("");
        //Изменить на последний используемый!!!!
        bxMatType.setValue(CH_MAT_TYPES.findByName("Листовой"));
        taMaterialNote.setText("");

        //Оставшиеся поля заполняем в зависимости от расчетного типа материала
        matComTypeController.showEmptyForm();
    }

    @Override
    public boolean enteredDataCorrect() {
        return true;
    }


}
