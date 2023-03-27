package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ru.wert.normic.components.BtnDouble;
import ru.wert.normic.controllers._forms.AbstractFormController;
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
    private String initTitleStyle;
    private TreeItem<OpData> rootItem;

    public void create(OpAssm opRoot){

        rootItem = new TreeItem<>(opRoot);
        createLeaf(rootItem);

        Image imgUnfold =  new Image("/pics/btns/unfold.png", 16, 16, true, true);
        Image imgFold =  new Image("/pics/btns/fold.png", 16, 16, true, true);

        folding = new BtnDouble(btnFolding,
                imgUnfold, "Развернуть",
                imgFold, "Свернуть");

        folding.getStateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) unfoldTree();
            else foldTree();
        });

        Image imgDescriptionOFF =  new Image("/pics/btns/description_off.png", 24, 24, true, true);
        Image imgDescriptionONN =  new Image("/pics/btns/description_on.png", 24, 24, true, true);

        operations = new BtnDouble(btnOperations,
                imgDescriptionOFF, "Скрыть операции",
                imgDescriptionONN, "Показать операции");

        operations.getStateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) unfoldTree();
            else foldTree();
        });

        treeView.setRoot(rootItem);
        if(((OpAssm)rootItem.getValue()).getOperations().isEmpty())
            treeView.setShowRoot(false);

        treeView.setCellFactory(param -> new TreeCell<OpData>(){
            @Override
            protected void updateItem(OpData opData, boolean empty) {
                super.updateItem(opData, empty);

                if (empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    List<OpData> operations = ((IOpWithOperations)opData).getOperations();
                    int quantity = opData.getQuantity();
                    EOpType type = opData.getOpType();


                    HBox hbTitle = new HBox();
                    hbTitle.setSpacing(5.0);
                    initTitleStyle = hbTitle.getStyle();
                    //Лого
                    ImageView logo = new ImageView(type.getLogo());
                    logo.setFitWidth(16);
                    logo.setFitHeight(16);
                    //Номер с наименованием
                    TextField tfName = new TextField();
                    Text txtName = new Text();
                    txtName.setId("title");
                    txtName.textProperty().bind(tfName.textProperty());
                    tfName.setText(((IOpWithOperations) opData).getName());

                    //Количество
                    TextField tfN = new TextField();
                    Text txtStart = new Text("(");
                    Text txtN = new Text();
                    Text txtFinish = new Text(" шт.)");

                    txtStart.setId("title");
                    txtN.setId("title");
                    txtFinish.setId("title");

                    txtN.textProperty().bind(tfN.textProperty());
                    tfN.setText(String.valueOf(quantity));

                    //Все вместе
                    hbTitle.getChildren().add(logo);
                    hbTitle.getChildren().add(txtName);

                    if(quantity > 1)
                        hbTitle.getChildren().addAll(txtStart, txtN, txtFinish);

                    VBox vbItemBlock = new VBox();
                    vbItemBlock.getChildren().add(hbTitle);

                    if(opData instanceof OpDetail){
                        Text textDetail = new Text("\t" + opData.toString());
                        textDetail.setId("description");
                        vbItemBlock.getChildren().add(textDetail);
                        for(OpData op : operations){
                            Text textOpName = new Text("\t \u25CF " + op.getOpType().getOpName() + ": ");
                            textOpName.setId("operationName");
                            Text textOpDescription = new Text(op.toString());
                            textOpDescription.setId("operationDescription");

                            HBox hbOperation = new HBox();
                            hbOperation.getChildren().addAll(textOpName, textOpDescription);
                            vbItemBlock.getChildren().add(hbOperation);
                        }
                    } else {
                        if(opData instanceof OpPack) {
                            Text text = new Text("\t" + opData.toString());
                            text.setId("description");
                            vbItemBlock.getChildren().add(text);
                        }

                        for(OpData op : operations){
                            if(op instanceof IOpWithOperations) continue;
                            Text textOpName = new Text("\t \u25CF " + op.getOpType().getOpName() + ": ");
                            textOpName.setId("operationName");
                            Text textOpDescription = new Text(op.toString());
                            textOpDescription.setId("operationDescription");

                            HBox hbOperation = new HBox();
                            hbOperation.getChildren().addAll(textOpName, textOpDescription);
                            vbItemBlock.getChildren().add(hbOperation);
                        }
                    }


                    setGraphic(vbItemBlock);

                    selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue){
                            hbTitle.setStyle("-fx-background-color: #f1e2af");
                            setStyle("-fx-background-color: #f1e2af");
                        } else {
                            hbTitle.setStyle(initTitleStyle);
                            vbItemBlock.setStyle(initTitleStyle);
                            setStyle(initTitleStyle);
                        }
                    });

                    setOnMouseClicked(e->{
                        if(e.isControlDown() && e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                            if (opData instanceof OpDetail) {
                                openFormEditor(EOpType.DETAIL, "ДЕТАЛЬ", "/fxml/formDetail.fxml", opData, tfName, tfN);
                            } else if (opData instanceof OpAssm) {
                                openFormEditor(EOpType.ASSM, "СБОРКА", "/fxml/formAssm.fxml", opData, tfName, tfN);
                            } else if (opData instanceof OpPack) {
                                openFormEditor(EOpType.PACK, "УПАКОВКА", "/fxml/formPack.fxml", opData, tfName, tfN);
                            }
                        }
                    });
                }
            }


        });

    }

    /**
     * Открыть форму редактирования сборки
     */
    private void openFormEditor(EOpType type, String title, String path, OpData opData, TextField tfName, TextField tfN) {
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

    private void createLeaf(TreeItem<OpData> treeItem){
        OpData opData = treeItem.getValue();
        List<OpData> operations = ((IOpWithOperations)opData).getOperations();
        for(OpData op : operations){
            if(op instanceof IOpWithOperations){
                TreeItem<OpData> newTreeItem = new TreeItem<>(op);
                treeItem.getChildren().add(newTreeItem);
                createLeaf(newTreeItem);
            }
        }
    }

    private void unfoldTree(){
        int index = treeView.getFocusModel().getFocusedIndex();
        List<TreeItem<OpData>> allItems = findAllChildren(rootItem);
        for(TreeItem<OpData> item : allItems){
            item.setExpanded(true);
        }
        treeView.getFocusModel().focus(index);
    }

    private void foldTree(){
        List<TreeItem<OpData>> listOfItemsToUnfold = findAllChildren(rootItem);
        for(TreeItem<OpData> ti : listOfItemsToUnfold)
            ti.setExpanded(false);
    }

    /**
     * Определение всех потомков элемента дерева, метод обобщенный, так как исп-ся для TreeTable
     */
    public List<TreeItem<OpData>> findAllChildren(TreeItem<OpData> treeItem){
        //Создаем три листа
        //лист, где будут храниться, найденые потомки в текущей итерации
        List<TreeItem<OpData>> newList = new ArrayList<>();
        //лист со всеми найденными потомками
        List<TreeItem<OpData>> finalList = new ArrayList<>();
        //текущий лист для итерации
        List<TreeItem<OpData>> list = new ArrayList<>();
        //В него добавляем сам узел, от которого будем искать потомков
        list.add(treeItem);

        //В цикле получаем потомков текущего узла, складываем их во временный лист,
        //потом временный лист снова перебираем и для каждого узла находим своих потомков
        //Найденных потомков снова складываем в промежуточный лист, а предыдущий лист
        //суммируем с finalList.
        while(true){
            for(TreeItem<OpData> ti : list)
                newList.addAll(ti.getChildren());
            if(newList.isEmpty()) break;
            else {
                finalList.addAll(newList);
                list.clear();
                list.addAll(newList);
                newList.clear();
            }
        }
        return finalList;
    }



}
