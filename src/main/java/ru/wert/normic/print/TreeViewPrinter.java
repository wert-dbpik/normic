package ru.wert.normic.print;

import javafx.print.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class TreeViewPrinter {

    private final TreeView<?> treeView;
    private Printer printer;
    private Paper paper;
    private PageOrientation orientation;

    public TreeViewPrinter(TreeView<?> treeView) {
        this.treeView = treeView;
        this.printer = Printer.getDefaultPrinter();
        this.paper = Paper.A4;
        this.orientation = PageOrientation.PORTRAIT;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public void setOrientation(PageOrientation orientation) {
        this.orientation = orientation;
    }

    public boolean print() {
        // 1. Подготовка дерева к печати
        expandAllNodes(treeView.getRoot());

        // 2. Создание задания на печать
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job == null) return false;

        // 3. Настройка макета страницы
        PageLayout pageLayout = printer.createPageLayout(paper, orientation, Printer.MarginType.DEFAULT);
        double printableWidth = pageLayout.getPrintableWidth();
        double printableHeight = pageLayout.getPrintableHeight();

        // 4. Расчет параметров печати
        double totalHeight = calculateTreeHeight();
        int totalPages = (int) Math.ceil(totalHeight / printableHeight);

        // 5. Сохранение исходного состояния
        List<Transform> originalTransforms = new ArrayList<>(treeView.getTransforms());
        double originalPrefHeight = treeView.getPrefHeight();

        try {
            // 6. Настройка TreeView для печати
            treeView.setPrefHeight(totalHeight);

            // 7. Масштабирование по ширине
            double scaleX = printableWidth / treeView.getBoundsInParent().getWidth();
            treeView.getTransforms().add(new Scale(scaleX, 1.0));

            // 8. Трансформация для постраничного вывода
            Translate pageTransform = new Translate();
            treeView.getTransforms().add(pageTransform);

            // 9. Печать страниц
            boolean success = true;
            for (int page = 0; page < totalPages; page++) {
                pageTransform.setY(-page * printableHeight);
                success &= job.printPage(pageLayout, treeView);
            }

            if (success) {
                job.endJob();
                return true;
            }
            return false;

        } finally {
            // 10. Восстановление исходного состояния
            treeView.getTransforms().setAll(originalTransforms);
            treeView.setPrefHeight(originalPrefHeight);
        }
    }

    private void expandAllNodes(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandAllNodes(child);
            }
        }
    }

    private double calculateTreeHeight() {
        // Более точный расчет высоты с учетом всех строк
        int rowCount = treeView.getExpandedItemCount();
        double rowHeight = treeView.getFixedCellSize() > 0 ?
                treeView.getFixedCellSize() : 24; // fallback
        return rowCount * rowHeight;
    }
}
