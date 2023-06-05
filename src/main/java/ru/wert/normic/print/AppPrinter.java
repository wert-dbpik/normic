package ru.wert.normic.print;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import ru.wert.normic.controllers.extra.StructureController;

import java.awt.*;
import java.util.Objects;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Root;

public class AppPrinter {

    public void print(){


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
            job.getJobSettings().setPageRanges(new PageRange(1, 2));

            boolean success = job.printPage(pane);
            if(success) {
                job.endJob();
                StructureController.tv.setStyle(initTreeViewStyle);
            }
        }
    }

    public  void print1(Node node) {

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) return;

        boolean proceed = job.showPrintDialog(node.getScene().getWindow());

        if (proceed) {
            double pageWidth = job.getJobSettings().getPageLayout().getPaper().getWidth(); //595 pt
            double pageHeight = job.getJobSettings().getPageLayout().getPaper().getHeight(); //842 pt

            double topMargin = job.getJobSettings().getPageLayout().getTopMargin(); //54 pt
            double rightMargin = job.getJobSettings().getPageLayout().getRightMargin(); //54 pt
            double bottomMargin = job.getJobSettings().getPageLayout().getBottomMargin(); //54 pt
            double leftMargin = job.getJobSettings().getPageLayout().getLeftMargin(); //54 pt

            int feedResolution = job.getJobSettings().getPrintResolution().getFeedResolution(); //600 dpi


            Stage stage = new Stage();
            AnchorPane pane = new AnchorPane();
            pane.setPrefWidth(pageWidth);
            pane.setPrefHeight(pageHeight);
            pane.getChildren().add(node);


            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.showAndWait();

            boolean success = job.printPage(pane);
            if(success)
                job.endJob();
        }
    }

    public void printScreenshot(Node node) {
        // Select printer
        final PrinterJob job = Objects.requireNonNull(PrinterJob.createPrinterJob(), "Cannot create printer job");
        final Scene scene = Objects.requireNonNull(node.getScene(), "Missing Scene");

        if (!job.showPrintDialog(scene.getWindow()))
            return;

        // Get Screenshot
        final WritableImage screenshot = node.snapshot(null, null);

        // Scale image to full page
        final Printer printer = job.getPrinter();
        final Paper paper = job.getJobSettings().getPageLayout().getPaper();
        final PageLayout pageLayout = printer.createPageLayout(paper,
                PageOrientation.LANDSCAPE,
                Printer.MarginType.DEFAULT);
        final double scaleX = pageLayout.getPrintableWidth() / screenshot.getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / screenshot.getHeight();
        final double scale = Math.min(scaleX, scaleY);
        final ImageView print_node = new ImageView(screenshot);
        print_node.getTransforms().add(new Scale(scale, scale));

        if (job.printPage(print_node))
            job.endJob();

    }

}
