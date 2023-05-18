package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.components.BtnDouble;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers.extra.tree_view.StructureTreeView;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.controllers.singlePlates.PlatePackController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.print.AppPrinter;

import java.io.IOException;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class StructureController {

    @FXML
    private TreeView<OpData> treeView;

    @FXML
    private StackPane spTreeView;
    @FXML
    private VBox vbTools;

    @FXML
    private Button btnFolding;

    @FXML
    private Button btnOperations;

    @FXML
    private Button btnNorms;

    @FXML
    private Button btnPrint;

    private StructureTreeView structureTreeView;
    private TreeItem<OpData> root;
    private OpAssm opRoot;
    private BtnDouble folding;
    private BtnDouble operations;
    private BtnDouble norms;

    @Getter@Setter private boolean treeExpanded = true;
    @Getter@Setter private boolean showOperations = true;
    @Getter@Setter private boolean showNormsTime = true;

    public static TreeView<OpData> tv;

    public void create(OpAssm opRoot){
        this.opRoot = opRoot;

        root = new TreeItem<>(opRoot);

        structureTreeView = new StructureTreeView(this, treeView, root, treeExpanded);

        Image imgUnfold =  new Image("/pics/btns/unfold.png", 16, 16, true, true);
        Image imgFold =  new Image("/pics/btns/fold.png", 16, 16, true, true);

        folding = new BtnDouble(btnFolding,
                imgFold, "Свернуть",
                imgUnfold, "Развернуть");

        folding.getStateProperty().addListener((observable, oldValue, newValue) -> {
            treeExpanded = !newValue;
            if(treeExpanded) structureTreeView.expandTree();
            else structureTreeView.foldTree();
        });

        Image imgDescriptionOFF =  new Image("/pics/btns/description_off.png", 24, 24, true, true);
        Image imgDescriptionONN =  new Image("/pics/btns/description_on.png", 24, 24, true, true);

        operations = new BtnDouble(btnOperations,
                imgDescriptionOFF, "Скрыть операции",
                imgDescriptionONN, "Показать операции");

        operations.getStateProperty().addListener((observable, oldValue, newValue) -> {
            showOperations = !newValue;
            treeView.refresh();
        });

        Image imgClockOFF =  new Image("/pics/btns/clock_off.png", 24, 24, true, true);
        Image imgClockONN =  new Image("/pics/btns/clock_on.png", 24, 24, true, true);

        norms = new BtnDouble(btnNorms,
                imgClockOFF, "Скрыть нормы времени",
                imgClockONN, "Показать нормы времени");

        norms.getStateProperty().addListener((observable, oldValue, newValue) -> {
            showNormsTime = !newValue;
            treeView.refresh();
        });

        Image imgPrint =  new Image("/pics/btns/print.png", 24, 24, true, true);
        btnPrint.setGraphic(new ImageView(imgPrint));
        btnPrint.setOnAction(e->{

            print();

        });

//        btnPrint.setOnAction(e->{
//            tv = treeView;
//            new AppPrinter().print();
//            spTreeView.getChildren().clear();
//            spTreeView.getChildren().addAll(tv, vbTools);
//        });

    }

    private void print(){
        TreeView<OpData> printedTreeView = new TreeView<>();
        build

        String initTreeViewStyle = StructureController.tv.getStyle();
        StructureController.tv.setStyle("-fx-font-size: 10;");
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) return;
        boolean proceed = job.showPrintDialog(StructureController.tv.getScene().getWindow());
        double pageWidth = job.getJobSettings().getPageLayout().getPaper().getWidth(); //595 pt
        double pageHeight = job.getJobSettings().getPageLayout().getPaper().getHeight(); //842 pt
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(pageWidth);
        pane.setPrefHeight(pageHeight);
        pane.setStyle("-fx-background-color: #FFFFFC; -fx-text-fill: black");
        if(proceed) {
            pane.getChildren().add(StructureController.tv);
            job.printPage(pane);
            job.endJob();
            StructureController.tv.setStyle(initTreeViewStyle);
        }
    }

    /**
     * Открыть форму редактирования сборки
     */
    public void openFormEditor(TreeItem<OpData> selectedTreeItem, EOpType type, String title, String path, OpData opData, TextField tfName, TextField tfN) {
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
                if(opData instanceof OpAssm) {
                    selectedTreeItem.getChildren().clear();
                    structureTreeView.buildTree(selectedTreeItem);
                } else
                    treeView.refresh();
                MAIN_CONTROLLER.countSumNormTimeByShops();
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
