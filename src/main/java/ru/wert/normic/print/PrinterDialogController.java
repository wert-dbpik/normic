package ru.wert.normic.print;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import ru.wert.normic.controllers.extra.tree_view.StructureTreeView;
import ru.wert.normic.controllers.extra.tree_view.TreeViewCell;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;

import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class PrinterDialogController {

    @FXML StackPane spTreeView;

    private TreeView<OpData> treeView = new TreeView<>();

    public void init(OpAssm opRoot) {
        TreeItem<OpData> root = new TreeItem<>(opRoot);
        treeView.setCellFactory(param -> new PrintTreeViewCell());
        StructureTreeView.buildTree(root);
        root.setExpanded(true);
        StructureTreeView.expandTree(treeView, root);
        treeView.setRoot(root);

        spTreeView.getChildren().add(treeView);
    }

    @FXML
    void print(Event event) {


        Printer printer = Printer.getDefaultPrinter(); //get the default printer
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT); //create a pagelayout.  I used Paper.NA_LETTER for a standard 8.5 x 11 in page.
        PrinterJob job = PrinterJob.createPrinterJob();//create a printer job

        if (job.showPrintDialog(MAIN_STAGE))// this is very useful it allows you to save the file as a pdf instead using all of your printer's paper. A dialog box pops up, allowing you to change the "name" option from your default printer to Adobe pdf.
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
    }
}
