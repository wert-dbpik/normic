package ru.wert.normic.print;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import lombok.Getter;
import ru.wert.normic.controllers.structure.StructureTreeView;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;

public class PrintDialogPreviewer {

    private PrintDialogController controller;
    private TreeView<OpData> treeView;
    private AnchorPane apPaper;
    private Slider scaleSlider;
    private ComboBox<Paper> cmbxPapers;
    private ComboBox<PageOrientation> cmbxOrientations;

    @Getter
    private StackPane previewContainer;
    @Getter
    private ScrollPane scrollPane;

    public PrintDialogPreviewer(PrintDialogController controller
    ) {
        this.controller = controller;
        this.treeView = controller.getTreeView();
        this.apPaper = controller.getApPaper();
        this.scaleSlider = controller.getScaleSlider();
        this.cmbxPapers = controller.getCmbxPapers();
        this.cmbxOrientations = controller.getCmbxOrientations();
    }

    private void setupScaleSlider() {
        Slider scaleSlider = controller.getScaleSlider();
        scaleSlider.setMin(0.5);
        scaleSlider.setMax(1.5);
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
        double contentHeight = calculateContentHeight();
        treeView.setPrefSize(
                apPaper.getPrefWidth() / scaleValue,
                Math.max(contentHeight, apPaper.getPrefHeight()) / scaleValue
        );

        contentWrapper.setPrefSize(
                apPaper.getPrefWidth() / scaleValue,
                Math.max(contentHeight, apPaper.getPrefHeight()) / scaleValue
        );
    }

    public void setupPreviewArea() {
        AnchorPane apPaper = controller.getApPaper();

        previewContainer = new StackPane();
        previewContainer.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1;");

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");

        // Контейнер для масштабирования содержимого
        Pane contentWrapper = new Pane();
        contentWrapper.getChildren().add(treeView);

        // Устанавливаем размеры контейнера
        contentWrapper.setMinSize(apPaper.getPrefWidth(), apPaper.getPrefHeight());
        contentWrapper.setPrefSize(apPaper.getPrefWidth(), apPaper.getPrefHeight());

        scrollPane.setContent(contentWrapper);
        previewContainer.getChildren().add(scrollPane);
        apPaper.getChildren().add(previewContainer);

        // Фиксируем размеры ScrollPane и previewContainer
        AnchorPane.setTopAnchor(previewContainer, 0.0);
        AnchorPane.setRightAnchor(previewContainer, 0.0);
        AnchorPane.setBottomAnchor(previewContainer, 0.0);
        AnchorPane.setLeftAnchor(previewContainer, 0.0);

        // Устанавливаем размеры ScrollPane
        scrollPane.setMinSize(apPaper.getPrefWidth(), apPaper.getPrefHeight());
        scrollPane.setPrefSize(apPaper.getPrefWidth(), apPaper.getPrefHeight());

        setupScaleSlider();
    }

    public void buildTreeView(OpAssm opRoot) {
        TreeItem<OpData> root = new TreeItem<>(opRoot);
        treeView.setCellFactory(param -> new PrintTreeViewCell());
        StructureTreeView.buildTree(root);
        root.setExpanded(true);
        StructureTreeView.expandTree(treeView, root);
        treeView.setRoot(root);
        treeView.setId("print-tree-view");
    }

    public void updatePreview() {
        if (controller.getCurrentPrinter() == null) return;

        PageLayout pageLayout = getCurrentPageLayout();

        // Обновляем размеры контейнера
        Pane contentWrapper = (Pane) scrollPane.getContent();
        contentWrapper.setPrefSize(apPaper.getPrefWidth(), apPaper.getPrefHeight());

        // Обновляем масштаб
        applyScaleToTreeView(scaleSlider.getValue());

    }

    public PageLayout getCurrentPageLayout() {
        Paper paper = cmbxPapers.getValue();
        PageOrientation orientation = cmbxOrientations.getValue();
        return controller.getCurrentPrinter().createPageLayout(paper, orientation, Printer.MarginType.DEFAULT);
    }

    public double calculateContentHeight() {
        // Более точный расчет высоты содержимого
        int rowCount = treeView.getExpandedItemCount();
        double rowHeight = treeView.getFixedCellSize() > 0 ?
                treeView.getFixedCellSize() : 24;
        return rowCount * rowHeight;
    }
}
