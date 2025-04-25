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
    @FXML private Slider scaleSlider;
    @FXML private Button btnPrint;

    private Printer currentPrinter;
    private TreeView<OpData> treeView = new TreeView<>();
    private StackPane previewContainer;
    private ScrollPane scrollPane;
    private VBox contentContainer;

    private VBox treeViewContainer; // Контейнер для TreeView

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

    private void setupScaleSlider() {
        scaleSlider.setMin(0.5);
        scaleSlider.setMax(2.0);
        scaleSlider.setValue(1.0);
        scaleSlider.setBlockIncrement(0.1);

        scaleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyScaleToTreeView(newVal.doubleValue());
        });
    }

    private void applyScaleToTreeView(double scaleValue) {
        Pane contentWrapper = (Pane) scrollPane.getContent();

        // Удаляем предыдущие трансформации
        contentWrapper.getTransforms().clear();

        // Применяем масштабирование к содержимому
        Scale scale = new Scale(scaleValue, scaleValue);
        contentWrapper.getTransforms().add(scale);

        // Корректируем размеры содержимого
        treeView.setScaleX(1.0);
        treeView.setScaleY(1.0);
        treeView.setPrefSize(
                scrollPane.getWidth() / scaleValue,
                calculateContentHeight() / scaleValue
        );

        contentWrapper.setPrefSize(
                scrollPane.getWidth() / scaleValue,
                calculateContentHeight() / scaleValue
        );
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

        // Контейнер для масштабирования содержимого
        Pane contentWrapper = new Pane();
        contentWrapper.getChildren().add(treeView);

        scrollPane.setContent(contentWrapper);
        previewContainer.getChildren().add(scrollPane);
        apPaper.getChildren().add(previewContainer);

        // Фиксируем размеры ScrollPane
        AnchorPane.setTopAnchor(previewContainer, 0.0);
        AnchorPane.setRightAnchor(previewContainer, 0.0);
        AnchorPane.setBottomAnchor(previewContainer, 0.0);
        AnchorPane.setLeftAnchor(previewContainer, 0.0);

        setupScaleSlider();
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
        double contentHeight = calculateContentHeight();

        // Фиксируем размеры ScrollPane
        scrollPane.setPrefSize(apPaper.getWidth(), apPaper.getHeight());

        // Обновляем масштаб
        applyScaleToTreeView(scaleSlider.getValue());

        Platform.runLater(() -> {
            ScrollBar verticalScrollBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
            if (verticalScrollBar != null) {
                verticalScrollBar.setUnitIncrement(20);
                verticalScrollBar.setBlockIncrement(100);
            }
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

        PageLayout pageLayout = getCurrentPageLayout();
        double pagePrintableHeight = pageLayout.getPrintableHeight();

        // Рассчитываем полную высоту содержимого
        double totalHeight = calculateContentHeight();
        int totalPages = (int) Math.ceil(totalHeight / pagePrintableHeight);

        // Сохраняем текущие трансформации
        List<Transform> originalTransforms = FXCollections.observableArrayList(treeView.getTransforms());

        try {
            // Масштабируем по ширине страницы
            double scaleX = pageLayout.getPrintableWidth() / treeView.getBoundsInParent().getWidth();
            treeView.getTransforms().add(new Scale(scaleX, 1.0));

            // Устанавливаем высоту TreeView для корректного расчета страниц
            treeView.setPrefHeight(totalHeight);

            // Добавляем трансформацию для постраничной печати
            Translate pageTransform = new Translate();
            treeView.getTransforms().add(pageTransform);

            boolean success = true;
            for (int i = 0; i < totalPages; i++) {
                pageTransform.setY(-i * pagePrintableHeight);
                success = success && job.printPage(pageLayout, treeView);
            }

            if (success) {
                job.endJob();
            }
        } finally {
            // Восстанавливаем исходные трансформации
            treeView.getTransforms().setAll(originalTransforms);
            treeView.setPrefHeight(-1); // Сбрасываем фиксированную высоту
        }
    }

    private double calculateContentHeight() {
        // Более точный расчет высоты содержимого
        int rowCount = treeView.getExpandedItemCount();
        double rowHeight = treeView.getFixedCellSize() > 0 ?
                treeView.getFixedCellSize() : 24;
        return rowCount * rowHeight;
    }
}
