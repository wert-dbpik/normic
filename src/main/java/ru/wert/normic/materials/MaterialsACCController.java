package ru.wert.normic.materials;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.wert.normic.components.BXMatTypes;
import ru.wert.normic.components.BXMaterialGroups;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.anyPart.AnyPart;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.material_group.MaterialGroup;
import ru.wert.normic.enums.EMatOperations;
import ru.wert.normic.enums.EMatType;

import java.io.IOException;
import java.util.List;

import static ru.wert.normic.NormicServices.*;

public class MaterialsACCController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;

    @FXML
    private StackPane spForCalculation;

    @FXML
    private ComboBox<MaterialGroup> bxMaterialGroup;

    @FXML
    private ComboBox<EMatType> bxMatType;

    @FXML
    private TextField tfMaterialName;

    @FXML
    private TextArea taMaterialNote;

    @FXML
    private StackPane spIndicator;

    @FXML
    private StackPane stackPaneForButtons;

    private Material oldMaterial;
    private MatTypeController matTypeController;
    private MaterialsTVController tableViewController;
    private EMatOperations operation;


    public void init(MaterialsTVController tableViewController, Material material, MatTypeController matTypeController, EMatOperations operation){
        this.tableViewController = tableViewController;
        this.oldMaterial = material;
        this.matTypeController = matTypeController;
        this.operation = operation;

        new BXMatTypes().create(bxMatType);
        bxMatType.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeMatType(newValue);
        });
        bxMatType.setValue(EMatType.LIST);
        new BXMaterialGroups().create(bxMaterialGroup);

        if(operation.equals(EMatOperations.COPY) || operation.equals(EMatOperations.CHANGE)) fillData();

    }

    private void changeMatType(EMatType newValue) {
        try {
            spForCalculation.getChildren().clear();
            FXMLLoader loader = null;
            switch (newValue){
                case LIST:
                    loader = new FXMLLoader(getClass().getResource(EMatType.LIST.getPath())); break;
                case ROUND:
                    loader = new FXMLLoader(getClass().getResource(EMatType.ROUND.getPath())); break;
                case PROFILE:
                    loader = new FXMLLoader(getClass().getResource(EMatType.PROFILE.getPath()));break;
                case PIECE:
                    loader = new FXMLLoader(getClass().getResource(EMatType.PIECE.getPath()));break;
            }
            assert loader != null;
            Parent parent = loader.load();
            matTypeController = loader.getController();
            spForCalculation.getChildren().add(parent);
            ((Stage)parent.getScene().getWindow()).sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void ok(Event event) {
        Material selectedMaterial = null;
        if(operation.equals(EMatOperations.ADD) || operation.equals(EMatOperations.COPY)){
            if(!checkData()) return;
            Material newMaterial = creatNewMaterial();
            if(!isDuplicated(newMaterial, null)){
                AnyPart finalPart = createAnyPart(newMaterial);
                newMaterial.setAnyPart(finalPart);
                selectedMaterial = QUICK_MATERIALS.save(newMaterial);
                if(selectedMaterial == null)
                    Warning1.create(event, "Ошибка!",
                            "Не удалось сохранить материал!",
                            "Возможно, сервер не доступен");
            }

            else Warning1.create(event, "Ошибка!",
                    "Такой материал уже существует!",
                    "Материал должен быть уникальным");

        }
        else if(operation.equals(EMatOperations.CHANGE) ){
            if(!checkData()) return;
            Material newMaterial = creatNewMaterial();
            if(!isDuplicated(newMaterial, oldMaterial)){
                updateOldMaterial(newMaterial);
                boolean res = QUICK_MATERIALS.update(oldMaterial);
                if(!res)
                    Warning1.create(event, "Ошибка!",
                            "Не удалось сохранить материал!",
                            "Возможно, сервер не доступен");
            }
            else Warning1.create(event, "Ошибка!",
                    "Такой материал уже существует!",
                    "Материал должен быть уникальным");

        }
        Material finalSelectedMaterial = selectedMaterial;

        tableViewController.updateTableView(finalSelectedMaterial);
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void updateOldMaterial(Material newMaterial) {
        //Изменяем AnyPart
        AnyPart anyPart = oldMaterial.getAnyPart();
        anyPart.setName(newMaterial.getName());
        ANY_PART.update(anyPart);

        oldMaterial.setName(newMaterial.getName());
        oldMaterial.setNote(newMaterial.getNote());
        oldMaterial.setParamS(matTypeController.readParamS());
        oldMaterial.setParamX(matTypeController.readParamX());

    }


    private void fillData(){
        tfMaterialName.setText(oldMaterial.getName());
        bxMatType.setValue(EMatType.getTypeByName(oldMaterial.getMatType().getName()));
        bxMaterialGroup.setValue(oldMaterial.getCatalogGroup());
        taMaterialNote.setText(oldMaterial.getNote());
        if(matTypeController != null)
            matTypeController.fillData(oldMaterial);
    }

    private boolean checkData(){
        if(tfMaterialName.getText().isEmpty()) return false;
        else if(bxMatType.getValue() == null) return false;
        else if(bxMaterialGroup.getValue() == null) return false;
        else if(!matTypeController.checkData()) return false;
        return true;
    }

    private Material creatNewMaterial(){
        Material newMaterial = new Material();
        newMaterial.setAnyPart(new AnyPart(tfMaterialName.getText().trim(), "", ANY_PART_GROUPS.findById(1L)));//1L - Материалы
        newMaterial.setName(tfMaterialName.getText().trim());
        newMaterial.setMatType(MAT_TYPES.findByName(bxMatType.getValue().getName()));
        newMaterial.setCatalogGroup(MATERIAL_GROUPS.findByName(bxMaterialGroup.getValue().getName()));
        newMaterial.setParamS(matTypeController.readParamS());
        newMaterial.setParamX(matTypeController.readParamX());
        newMaterial.setNote(taMaterialNote.getText().trim());

        return newMaterial;
    }

    /**
     * Проверка на дублирование при изменении записи
     * Запись проверяется со списком уже существующих записей
     * Имя изменяемой записи из проверки исключается
     */
    protected boolean isDuplicated(Material newItem, Material oldItem){

        //Из листа удаляется выделенная запись (старая)
        List<Material> items = QUICK_MATERIALS.findAll();
        if(oldItem != null)items.remove(oldItem);

        //Теперь новая запись сравнивается только с оставшимися записями
        for (Object u : items)
            if (newItem.equals(u)) return true;
        return false;
    }

    /**
     * Если имеющегося пасспорта еще нет в БД, то пасспорт будет сохранен
     */
    private AnyPart createAnyPart(Material newItem) {
        //Окончательный AnyPart
        AnyPart finalPart = null;
        //AnyPart пришедший с изделием
        AnyPart newPart = newItem.getAnyPart();
        //Проверяем наличие newPart в БД
        AnyPart foundPart = QUICK_ANY_PARTS.findByName(newPart.getName());
        if(foundPart == null) { //Если newPart в базе отсутствует
            AnyPart savedPart = QUICK_ANY_PARTS.save(newPart); //Сохраняем в базе
            if (savedPart != null) { //Если сохранение произошло
                finalPart = savedPart;
            } else { //Если сохранение не произошло
                Warning1.create(null, "Ошибка!", String.format("Не удалось создать AnyPart \n%s, %s", newPart.getName(), newPart.getSecondName()),
                        "Возможно, сервер не доступен");
            }
        }else { //Если newPart уже есть в базе
            finalPart = foundPart;
        }
        return finalPart;
    }


}
