package ru.wert.normic.print;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;

public class PrintToPDF {

    static void print(AnchorPane apPaper, PrintDialogPreviewer previewer){
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

    static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
