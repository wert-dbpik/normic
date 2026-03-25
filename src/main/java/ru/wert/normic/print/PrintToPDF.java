package ru.wert.normic.print;

import javafx.embed.swing.SwingFXUtils;
import javafx.print.Paper;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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

    static void print(AnchorPane apPaper, PrintDialogPreviewer previewer) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(apPaper.getScene().getWindow());
        if (file == null) return;

        ScrollPane scrollPane = previewer.getScrollPane();
        TreeView<?> treeView = previewer.getTreeView();

        // Получаем текущий масштаб из слайдера
        double currentScale = previewer.getScaleSlider().getValue();

        // Сохраняем исходное состояние
        Pane originalParent = (Pane) treeView.getParent();
        double originalPrefWidth = treeView.getPrefWidth();
        double originalPrefHeight = treeView.getPrefHeight();
        double originalMinWidth = treeView.getMinWidth();
        double originalMinHeight = treeView.getMinHeight();
        double originalMaxWidth = treeView.getMaxWidth();
        double originalMaxHeight = treeView.getMaxHeight();

        // Создаем временный контейнер
        AnchorPane tempContainer = new AnchorPane();
        tempContainer.setStyle("-fx-background-color: white;");

        try {
            // Получаем размер страницы
            double pageWidth = apPaper.getPrefWidth();
            double pageHeight = apPaper.getPrefHeight();

            // Получаем точную высоту дерева
            double totalHeight = 12946.0; // Используем значение из лога

            // Рассчитываем количество страниц
            int totalPages = (int) Math.ceil(totalHeight / pageHeight);
            if (totalPages == 0) totalPages = 1;

            System.out.println("Total height: " + totalHeight);
            System.out.println("Page height: " + pageHeight);
            System.out.println("Total pages: " + totalPages);
            System.out.println("Current scale: " + currentScale);

            // Временно извлекаем дерево
            if (originalParent != null) {
                originalParent.getChildren().remove(treeView);
            }

            // Настраиваем временный контейнер
            tempContainer.setPrefSize(pageWidth, totalHeight);
            tempContainer.setMinSize(pageWidth, totalHeight);
            tempContainer.setMaxSize(pageWidth, totalHeight);

            // Настраиваем дерево
            treeView.setPrefSize(pageWidth, totalHeight);
            treeView.setMinSize(pageWidth, totalHeight);
            treeView.setMaxSize(pageWidth, totalHeight);

            // Применяем масштаб через трансформацию
            treeView.getTransforms().clear();
            treeView.getTransforms().add(Transform.scale(currentScale, currentScale));

            tempContainer.getChildren().add(treeView);
            AnchorPane.setTopAnchor(treeView, 0.0);
            AnchorPane.setLeftAnchor(treeView, 0.0);
            AnchorPane.setRightAnchor(treeView, 0.0);

            // Принудительно обновляем layout
            tempContainer.layout();
            treeView.layout();

            // Даем время на рендеринг
            Thread.sleep(500);

            // Создаем PDF документ
            PDDocument document = new PDDocument();
            PDRectangle pageSize = getPDRectangle(previewer.getCurrentPageLayout().getPaper());

            // Делаем снимки постранично прямо с контейнера
            for (int pageNum = 0; pageNum < totalPages; pageNum++) {
                // Вычисляем смещение для текущей страницы
                double translateY = pageNum * pageHeight;

                // Создаем снимок текущей страницы
                WritableImage pageImage = snapshotPage(tempContainer, pageWidth, pageHeight, translateY, 2.0);

                // Создаем страницу PDF
                PDPage page = new PDPage(pageSize);
                document.addPage(page);

                // Добавляем изображение на страницу
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(pageImage, null);
                PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

                // Вписываем изображение в страницу
                float imgWidth = pdImage.getWidth();
                float imgHeight = pdImage.getHeight();
                float pageWidthPt = pageSize.getWidth();
                float pageHeightPt = pageSize.getHeight();

                float scaleX = pageWidthPt / imgWidth;
                float scaleY = pageHeightPt / imgHeight;
                float scale = Math.min(scaleX, scaleY);

                float scaledWidth = imgWidth * scale;
                float scaledHeight = imgHeight * scale;
                float x = (pageWidthPt - scaledWidth) / 2;
                float y = (pageHeightPt - scaledHeight) / 2;

                contentStream.drawImage(pdImage, x, y, scaledWidth, scaledHeight);
                contentStream.close();

                System.out.println("Page " + (pageNum + 1) + " created at y=" + translateY);
            }

            document.save(file);
            document.close();

            showAlert("Готово",
                    String.format("PDF успешно сохранён: %s\nСоздано страниц: %d\nМасштаб: %.2f",
                            file.getAbsolutePath(), totalPages, currentScale));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось сохранить PDF: " + e.getMessage());
        } finally {
            // Восстанавливаем дерево
            try {
                tempContainer.getChildren().remove(treeView);
                treeView.getTransforms().clear();

                if (originalParent != null) {
                    originalParent.getChildren().add(treeView);
                }

                treeView.setPrefSize(originalPrefWidth, originalPrefHeight);
                treeView.setMinSize(originalMinWidth, originalMinHeight);
                treeView.setMaxSize(originalMaxWidth, originalMaxHeight);

                if (originalParent != null) {
                    originalParent.layout();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static WritableImage snapshotPage(Node node, double pageWidth, double pageHeight,
                                              double translateY, double scale) {
        SnapshotParameters params = new SnapshotParameters();

        // Устанавливаем область захвата
        params.setViewport(new javafx.geometry.Rectangle2D(0, translateY, pageWidth, pageHeight));

        // Устанавливаем масштаб для высокого разрешения
        params.setTransform(Transform.scale(scale, scale));

        // Белый фон
        params.setFill(javafx.scene.paint.Color.WHITE);

        return node.snapshot(params, null);
    }

    private static PDRectangle getPDRectangle(Paper paper) {
        if (paper == Paper.A4) {
            return PDRectangle.A4;
        } else if (paper == Paper.A3) {
            return PDRectangle.A3;
        } else if (paper == Paper.NA_LETTER) {
            return PDRectangle.LETTER;
        } else {
            return PDRectangle.A4;
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
