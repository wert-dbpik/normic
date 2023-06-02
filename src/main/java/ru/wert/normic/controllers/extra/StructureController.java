package ru.wert.normic.controllers.extra;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
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
import ru.wert.normic.print.NodePrinter;

import java.io.IOException;
import java.util.LinkedList;

import static java.lang.String.format;
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
//        btnPrint.setOnAction(e->{
//            new AppPrinter().print(treeView);
//        });

//        btnPrint.setOnAction(e->{
//            tv = treeView;
//            new AppPrinter().print();
//            spTreeView.getChildren().clear();
//            spTreeView.getChildren().addAll(tv, vbTools);
//        });

//        btnPrint.setOnAction(e->{
//            PrinterJob job = PrinterJob.createPrinterJob();
//            NodePrinter printer = new NodePrinter();
//
//
//
//            for (int i = 0; i < numberOfPages; i++) {
//                gridTransform.setY(-i * (pagePrintableHeight / localScale));
//                job.printPage(pageLayout, treeView);
//            }
//        });

        btnPrint.setOnAction(e -> {
                    Printer printer = Printer.getDefaultPrinter(); //get the default printer
                    PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT); //create a pagelayout.  I used Paper.NA_LETTER for a standard 8.5 x 11 in page.
                    PrinterJob job = PrinterJob.createPrinterJob();//create a printer job

                    if(job.showPrintDialog(((Node)e.getSource()).getScene().getWindow()))// this is very useful it allows you to save the file as a pdf instead using all of your printer's paper. A dialog box pops up, allowing you to change the "name" option from your default printer to Adobe pdf.
                    {
                        double pagePrintableWidth = pageLayout.getPrintableWidth(); //this should be 8.5 inches for this page layout.
                        double pagePrintableHeight = pageLayout.getPrintableHeight();// this should be 11 inches for this page layout.



//                        treeView.prefHeightProperty().bind(Bindings.size(treeView.get).multiply(35));// If your cells' rows are variable size you add the .multiply and play with the input value until your output is close to what you want. If your cells' rows are the same height, I think you can use .multiply(1). This changes the height of your tableView to show all rows in the table.
                        treeView.prefHeightProperty().set(1500.0);// If your cells' rows are variable size you add the .multiply and play with the input value until your output is close to what you want. If your cells' rows are the same height, I think you can use .multiply(1). This changes the height of your tableView to show all rows in the table.
                        treeView.minHeightProperty().bind(treeView.prefHeightProperty());//You can probably play with this to see if it's really needed.  Comment it out to find out.
                        treeView.maxHeightProperty().bind(treeView.prefHeightProperty());//You can probably play with this to see if it' really needed.  Comment it out to find out.

                        double scaleX = pagePrintableWidth / treeView.getBoundsInParent().getWidth();//scaling down so that the printing width fits within the paper's width bound.
                        double scaleY = scaleX; //scaling the height using the same scale as the width. This allows the writing and the images to maintain their scale, or not look skewed.
                        double localScale = scaleX; //not really needed since everything is scaled down at the same ratio. scaleX is used thoughout the program to scale the print out.

                        double numberOfPages = Math.ceil((treeView.getPrefHeight() * localScale) / pagePrintableHeight);//used to figure out the number of pages that will be printed.


                        //System.out.println("pref Height: " + tblvMain.getPrefHeight());
                        //System.out.println("number of pages: " + numberOfPages);


                        treeView.getTransforms().add(new Scale(scaleX, (scaleY)));//scales the printing. Allowing the width to say within the papers width, and scales the height to do away with skewed letters and images.
                        treeView.getTransforms().add(new Translate(0, 0));// starts the first print at the top left corner of the image that needs to be printed

                        //Since the height of what needs to be printed is longer than the paper's heights we use gridTransfrom to only select the part to be printed for a given page.
                        Translate gridTransform = new Translate();
                        treeView.getTransforms().add(gridTransform);

                        //now we loop though the image that needs to be printed and we only print a subimage of the full image.
                        //for example: In the first loop we only pint the printable image from the top down to the height of a standard piece of paper. Then we print starting from were the last printed page ended down to the height of the next page. This happens until all of the pages are printed.
                        // first page prints from 0 height to -11 inches height, Second page prints from -11 inches height to -22 inches height, etc.
                        for (int i = 0; i < numberOfPages; i++) {
                            gridTransform.setY(-i * (pagePrintableHeight / localScale));
                            job.printPage(pageLayout, treeView);
                        }

                        job.endJob();//finally end the printing job.
                    }
        });

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
