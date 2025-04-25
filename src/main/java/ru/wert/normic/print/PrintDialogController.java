package ru.wert.normic.print;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;

import java.awt.image.BufferedImage;
import java.io.File;

@Getter
public class PrintDialogController {

    @FXML private AnchorPane apPaper; //Width = 592, height = 842
    @FXML private ComboBox<Printer> cmbxPrinters;
    @FXML private ComboBox<Paper> cmbxPapers;
    @FXML private ComboBox<PageOrientation> cmbxOrientations;
    @FXML private Slider scaleSlider;
    @FXML private Button btnPrint;


    private Printer currentPrinter;
    private TreeView<OpData> treeView = new TreeView<>();
    private PrintDialogPreviewer previewer;

    @FXML
    public void initialize() {
        setupPrinterComboBox();
        setupPaperComboBox();
        setupOrientationComboBox();
        previewer = new PrintDialogPreviewer(this);
        previewer.setupPreviewArea();
    }

    public void init(OpAssm opRoot) {
        currentPrinter = Printer.getDefaultPrinter();

        previewer.buildTreeView(opRoot);
        previewer.updatePreview();
    }

    private void setupPrinterComboBox() {
        ObservableList<Printer> printers = FXCollections.observableArrayList(Printer.getAllPrinters());
        cmbxPrinters.setItems(printers);
        cmbxPrinters.getSelectionModel().select(Printer.getDefaultPrinter());
        cmbxPrinters.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentPrinter = newValue;
            previewer.updatePreview();
        });
    }

    private void setupPaperComboBox() {
        ObservableList<Paper> papers = FXCollections.observableArrayList(
                Paper.A4, Paper.A3, Paper.NA_LETTER);
        cmbxPapers.setItems(papers);
        cmbxPapers.getSelectionModel().select(Paper.A4);
        cmbxPapers.valueProperty().addListener((observable, oldValue, newValue) -> previewer.updatePreview());
    }

    private void setupOrientationComboBox() {
        ObservableList<PageOrientation> orientations = FXCollections.observableArrayList(
                PageOrientation.PORTRAIT, PageOrientation.LANDSCAPE);
        cmbxOrientations.setItems(orientations);
        cmbxOrientations.getSelectionModel().select(PageOrientation.PORTRAIT);
        cmbxOrientations.valueProperty().addListener((observable, oldValue, newValue) -> previewer.updatePreview());
    }



    @FXML
    private void handlePrint(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(apPaper.getScene().getWindow());
        if (file == null) return;

        try {
            // Делаем снимок ScrollPane
            ScrollPane scrollPane = previewer.getScrollPane();
            SnapshotParameters params = new SnapshotParameters();
            params.setTransform(Transform.scale(2, 2)); // 2x DPI
            WritableImage image = scrollPane.snapshot(params, null);

            // Конвертируем изображение в PDF (используя Apache PDFBox)
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(new PDRectangle((float)image.getWidth(), (float)image.getHeight()));
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            contentStream.drawImage(pdImage, 0, 0);
            contentStream.close();

            document.save(file);
            document.close();

            showAlert("Готово", "PDF успешно сохранён: " + file.getAbsolutePath());
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось сохранить PDF: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
