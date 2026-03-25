package ru.wert.normic.print;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.print.Paper;
import javafx.scene.Node;
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

public class PrintToPdfWorking {

    static void print(AnchorPane apPaper, PrintDialogPreviewer previewer) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(apPaper.getScene().getWindow());
        if (file == null) return;

        try {
            ScrollPane scrollPane = previewer.getScrollPane();
            Node content = scrollPane.getContent();

            // Получаем реальные размеры содержимого
            Bounds contentBounds = content.getBoundsInLocal();
            double contentWidth = contentBounds.getWidth();
            double contentHeight = contentBounds.getHeight();

            // Получаем размер страницы (с учетом масштаба)
            double pageWidth = apPaper.getPrefWidth();
            double pageHeight = apPaper.getPrefHeight();

            // Рассчитываем количество страниц по вертикали
            int totalPages = (int) Math.ceil(contentHeight / pageHeight);

            PDDocument document = new PDDocument();

            // Создаем страницы PDF с правильным размером (A4, A3 и т.д.)
            PDRectangle pageSize = getPDRectangle(previewer.getCurrentPageLayout().getPaper());

            for (int pageNum = 0; pageNum < totalPages; pageNum++) {
                // Создаем снимок для текущей страницы
                WritableImage pageImage = snapshotPage(content, pageNum, pageWidth, pageHeight);

                // Создаем страницу PDF
                PDPage page = new PDPage(pageSize);
                document.addPage(page);

                // Добавляем изображение на страницу
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(pageImage, null);
                PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

                // Масштабируем изображение под размер страницы, сохраняя пропорции
                float imageWidth = pdImage.getWidth();
                float imageHeight = pdImage.getHeight();
                float pageWidthPt = pageSize.getWidth();
                float pageHeightPt = pageSize.getHeight();

                // Рассчитываем масштаб для вписывания в страницу
                float scale = Math.min(pageWidthPt / imageWidth, pageHeightPt / imageHeight);
                float scaledWidth = imageWidth * scale;
                float scaledHeight = imageHeight * scale;

                // Центрируем изображение на странице
                float x = (pageWidthPt - scaledWidth) / 2;
                float y = (pageHeightPt - scaledHeight) / 2;

                contentStream.drawImage(pdImage, x, y, scaledWidth, scaledHeight);
                contentStream.close();
            }

            document.save(file);
            document.close();

            showAlert("Готово",
                    String.format("PDF успешно сохранён: %s\nСоздано страниц: %d",
                            file.getAbsolutePath(), totalPages));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось сохранить PDF: " + e.getMessage());
        }
    }

    private static WritableImage snapshotPage(Node content, int pageNum, double pageWidth, double pageHeight) {
        // Смещение для текущей страницы
        double translateY = -pageNum * pageHeight;

        // Создаем снимок с высоким разрешением (2x)
        SnapshotParameters params = new SnapshotParameters();
        params.setTransform(Transform.scale(2, 2));
        params.setViewport(new javafx.geometry.Rectangle2D(
                0, -translateY, pageWidth, pageHeight
        ));

        return content.snapshot(params, null);
    }

    private static PDRectangle getPDRectangle(Paper paper) {
        if (paper == Paper.A4) {
            return PDRectangle.A4;
        } else if (paper == Paper.A3) {
            return PDRectangle.A3;
        } else if (paper == Paper.NA_LETTER) {
            return PDRectangle.LETTER;
        } else {
            return PDRectangle.A4; // По умолчанию
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
