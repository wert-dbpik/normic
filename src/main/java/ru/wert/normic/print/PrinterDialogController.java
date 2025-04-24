package ru.wert.normic.print;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import ru.wert.normic.controllers.structure.StructureTreeView;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;

import java.util.List;

public class PrinterDialogController {

    @FXML private AnchorPane apPaper;
    @FXML private ComboBox<Printer> cmbxPrinters;
    @FXML private ComboBox<Paper> cmbxPapers;
    @FXML private ComboBox<PageOrientation> cmbxOrientations;
    @FXML private Button btnPrint;

    private Printer currentPrinter;
    private TreeView<OpData> treeView = new TreeView<>();
    private StackPane previewContainer;
    private ScrollPane scrollPane;
    private VBox contentContainer;

    @FXML
    public void initialize() {
        setupPrinterComboBox();
        setupPaperComboBox();
        setupOrientationComboBox();
        setupPreviewArea();
    }

    public void init(OpAssm opRoot) {
        currentPrinter = Printer.getDefaultPrinter();
        buildTreeView(opRoot);
        updatePreview();
    }

    private void setupPrinterComboBox() {
        ObservableList<Printer> printers = FXCollections.observableArrayList(Printer.getAllPrinters());
        cmbxPrinters.setItems(printers);
        cmbxPrinters.getSelectionModel().select(Printer.getDefaultPrinter());
        cmbxPrinters.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentPrinter = newValue;
            updatePreview();
        });
    }

    private void setupPaperComboBox() {
        ObservableList<Paper> papers = FXCollections.observableArrayList(
                Paper.A4, Paper.A3, Paper.NA_LETTER);
        cmbxPapers.setItems(papers);
        cmbxPapers.getSelectionModel().select(Paper.A4);
        cmbxPapers.valueProperty().addListener((observable, oldValue, newValue) -> updatePreview());
    }

    private void setupOrientationComboBox() {
        ObservableList<PageOrientation> orientations = FXCollections.observableArrayList(
                PageOrientation.PORTRAIT, PageOrientation.LANDSCAPE);
        cmbxOrientations.setItems(orientations);
        cmbxOrientations.getSelectionModel().select(PageOrientation.PORTRAIT);
        cmbxOrientations.valueProperty().addListener((observable, oldValue, newValue) -> updatePreview());
    }

    private void setupPreviewArea() {
        previewContainer = new StackPane();
        previewContainer.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1;");

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");

        previewContainer.getChildren().add(scrollPane);
        apPaper.getChildren().add(previewContainer);

        AnchorPane.setTopAnchor(previewContainer, 10.0);
        AnchorPane.setRightAnchor(previewContainer, 10.0);
        AnchorPane.setBottomAnchor(previewContainer, 10.0);
        AnchorPane.setLeftAnchor(previewContainer, 10.0);
    }

    private void buildTreeView(OpAssm opRoot) {
        TreeItem<OpData> root = new TreeItem<>(opRoot);
        treeView.setCellFactory(param -> new PrintTreeViewCell());
        StructureTreeView.buildTree(root);
        root.setExpanded(true);
        StructureTreeView.expandTree(treeView, root);
        treeView.setRoot(root);
    }

    private void updatePreview() {
        if (currentPrinter == null) return;

        PageLayout pageLayout = getCurrentPageLayout();
        double printableWidth = pageLayout.getPrintableWidth();
        double printableHeight = pageLayout.getPrintableHeight();

        // Устанавливаем размеры области предпросмотра
        previewContainer.setPrefSize(printableWidth, printableHeight);

        // Настраиваем TreeView для предпросмотра
        treeView.setPrefWidth(printableWidth);
        treeView.setPrefHeight(calculateContentHeight(treeView));

        // Обновляем скролл
        Platform.runLater(() -> {
            scrollPane.setVvalue(0); // Сбрасываем скролл в начало
            scrollPane.requestLayout();
        });
    }

    private PageLayout getCurrentPageLayout() {
        Paper paper = cmbxPapers.getValue();
        PageOrientation orientation = cmbxOrientations.getValue();
        return currentPrinter.createPageLayout(paper, orientation, Printer.MarginType.DEFAULT);
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        if (currentPrinter == null) return;

        PrinterJob job = PrinterJob.createPrinterJob(currentPrinter);
        if (job == null) return;

        // 1. Создаем копию TreeView без скроллбаров для печати
        TreeView<OpData> printTreeView = createPrintTreeView();

        PageLayout pageLayout = getCurrentPageLayout();
        double pagePrintableHeight = pageLayout.getPrintableHeight();

        // 2. Рассчитываем полную высоту содержимого
        double totalHeight = calculateContentHeight(printTreeView);
        int totalPages = (int) Math.ceil(totalHeight / pagePrintableHeight);

        // 3. Масштабируем по ширине страницы
        double scaleX = pageLayout.getPrintableWidth() / printTreeView.getBoundsInParent().getWidth();
        printTreeView.getTransforms().add(new Scale(scaleX, 1.0));

        // 4. Добавляем трансформацию для постраничной печати
        Translate pageTransform = new Translate();
        printTreeView.getTransforms().add(pageTransform);

        // 5. Печатаем все страницы
        boolean success = true;
        for (int i = 0; i < totalPages; i++) {
            pageTransform.setY(-i * pagePrintableHeight);
            success = success && job.printPage(pageLayout, printTreeView);
        }

        if (success) {
            job.endJob();
        }
    }

    private TreeView<OpData> createPrintTreeView() {
        // Создаем копию TreeView для печати
        TreeView<OpData> printTreeView = new TreeView<>();
        printTreeView.setRoot(treeView.getRoot());
        printTreeView.setCellFactory(treeView.getCellFactory());

        // Копируем все стили
        printTreeView.getStyleClass().addAll(treeView.getStyleClass());
        printTreeView.setStyle(treeView.getStyle());

        // Устанавливаем фиксированную высоту
        printTreeView.setPrefHeight(calculateContentHeight(treeView));
        printTreeView.setPrefWidth(treeView.getBoundsInParent().getWidth());

        return printTreeView;
    }

    private double calculateContentHeight(TreeView<?> tree) {
        int rowCount = tree.getExpandedItemCount();
        double rowHeight = tree.getFixedCellSize() > 0 ?
                tree.getFixedCellSize() : 24;
        return rowCount * rowHeight;
    }
}
