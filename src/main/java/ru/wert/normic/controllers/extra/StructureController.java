package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import ru.wert.normic.components.BtnDouble;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers.extra.tree_view.StructureTreeView;
import ru.wert.normic.controllers.extra.tree_view.TreeViewCell;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.controllers.singlePlates.PlatePackController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class StructureController {

    @FXML
    private TreeView<OpData> treeView;

    @FXML
    private Button btnFolding;

    @FXML
    private Button btnOperations;

    private BtnDouble folding;
    private BtnDouble operations;



    public void create(OpAssm opRoot){

        StructureTreeView structureTreeView = new StructureTreeView(this, treeView, opRoot);

        Image imgUnfold =  new Image("/pics/btns/unfold.png", 16, 16, true, true);
        Image imgFold =  new Image("/pics/btns/fold.png", 16, 16, true, true);

        folding = new BtnDouble(btnFolding,
                imgUnfold, "Развернуть",
                imgFold, "Свернуть");

        folding.getStateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) structureTreeView.unfoldTree();
            else structureTreeView.foldTree();
        });

        Image imgDescriptionOFF =  new Image("/pics/btns/description_off.png", 24, 24, true, true);
        Image imgDescriptionONN =  new Image("/pics/btns/description_on.png", 24, 24, true, true);

        operations = new BtnDouble(btnOperations,
                imgDescriptionOFF, "Скрыть операции",
                imgDescriptionONN, "Показать операции");

        operations.getStateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) structureTreeView.unfoldTree();
            else structureTreeView.foldTree();
        });

    }

    /**
     * Открыть форму редактирования сборки
     */
    public void openFormEditor(EOpType type, String title, String path, OpData opData, TextField tfName, TextField tfN) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent parent = loader.load();
            AbstractFormController formController = loader.getController();
            formController.init(MAIN_CONTROLLER, tfName, tfN, opData);
            Decoration windowDecoration = new Decoration(
                    title,
                    parent,
                    false,
                    MAIN_STAGE,
                    "decoration-assm",
                    true,
                    false);
            ImageView closer = windowDecoration.getImgCloseWindow();
            closer.setOnMousePressed(ev -> {
                switch(type){
                    case DETAIL:    PlateDetailController.collectOpData((OpDetail) opData, formController, tfName, tfN); break;
                    case ASSM:      PlateAssmController.collectOpData((OpAssm) opData, formController, tfName, tfN); break;
                    case PACK:      PlatePackController.collectOpData((OpPack) opData, tfName, tfN); break;
                }
                MAIN_CONTROLLER.countSumNormTimeByShops();
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
