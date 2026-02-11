package ru.wert.normic.excel.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import ru.wert.normic.excel.model.enums.EColName;
import ru.wert.normic.excel.model.enums.EColor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static ru.wert.normic.excel.model.enums.EColName.*;

@Slf4j
@Getter
public class POIReader {

    static private int MAX_LINES = 2000;
    private final File file;
    private final Workbook book;
    private final Sheet sheet;
    private final List<String> colNamesList;
    private final int headRowIndex;
    private final int firstRowIndex;
    private int lastRowIndex;

    private HashMap<Integer, String> hashMapHeader;
    private final Set<Integer> setOfColIndexes;
    private List<String> setOfColNames;
    private boolean colLaсquerExist;
    private boolean colZpcExist;
    private boolean colAmountPerAssembleExist;
    private HashMap<Integer, Integer> executions;
    private HashMap<String, Integer> modelColNames;
    private List<List<String>> data;

    // Окно для вывода этапов парсинга
    private Stage logStage;
    private TextArea logArea;
    private StringBuilder logBuffer = new StringBuilder();

    public POIReader(File file) throws IOException {
        this.file = file;

        createLogWindowSync();

        logInfo("==========================================");
        logInfo("НАЧАЛО ПАРСИНГА ФАЙЛА: " + file.getName());
        logInfo("==========================================");

        try {
            logInfo("[ЭТАП 1] Открытие Excel файла...");
            this.book = WorkbookFactory.create(file);
            logInfo("  ✓ Файл успешно открыт");

            logInfo("[ЭТАП 2] Получение первого листа...");
            this.sheet = book.getSheetAt(0);
            logInfo("  ✓ Лист получен, название: " + sheet.getSheetName());

            logInfo("[ЭТАП 3] Поиск строки заголовков...");
            this.headRowIndex = findHeadRowIndex(sheet);
            if (headRowIndex == -1) {
                throw new IOException("Строка заголовков не найдена");
            }
            logInfo("  ✓ Строка заголовков найдена, индекс: " + headRowIndex);

            this.firstRowIndex = headRowIndex + 1;
            logInfo("[ЭТАП 4] Первая строка с данными: " + firstRowIndex);

            logInfo("[ЭТАП 5] Поиск последней строки таблицы...");
            this.lastRowIndex = findLastRowInTable();
            logInfo("  ✓ Последняя строка таблицы: " + lastRowIndex);
            logInfo("  → Всего строк с данными: " + (lastRowIndex - firstRowIndex));

            this.colNamesList = EColName.getColNamesList();
            logInfo("[ЭТАП 6] Загружено " + colNamesList.size() + " допустимых имен колонок");

            logInfo("[ЭТАП 7] Поиск колонок в заголовке...");
            this.hashMapHeader = findHashMapHeader();
            logInfo("  ✓ Найдено колонок: " + hashMapHeader.size());
            this.setOfColIndexes = hashMapHeader.keySet();

            logInfo("[ЭТАП 8] Формирование списка имен колонок...");
            this.setOfColNames = findSetOfColNames();
            logInfo("  ✓ Список колонок: " + String.join(", ", setOfColNames));

            logInfo("[ЭТАП 9] Проверка наличия специальных колонок...");
            this.colLaсquerExist = setOfColNames.contains(LACQUER.toString());
            logInfo("  - Колонка 'Лак': " + (colLaсquerExist ? "ЕСТЬ" : "ОТСУТСТВУЕТ"));
            this.colZpcExist = setOfColNames.contains(ZCP.toString());
            logInfo("  - Колонка 'ЦСГ': " + (colZpcExist ? "ЕСТЬ" : "ОТСУТСТВУЕТ"));
            this.colAmountPerAssembleExist = setOfColNames.contains(AMOUNT.toString());
            logInfo("  - Колонка '(кол)': " + (colAmountPerAssembleExist ? "ЕСТЬ" : "ОТСУТСТВУЕТ"));

            logInfo("[ЭТАП 10] Формирование карты колонок модели...");
            this.modelColNames = findModelColNames();
            logInfo("  ✓ Карта колонок сформирована");

            logInfo("[ЭТАП 11] Поиск колонок исполнений...");
            this.executions = findExecutions();
            logInfo("  ✓ Найдено исполнений: " + executions.size());

            logInfo("[ЭТАП 12] Чтение данных таблицы...");
            this.data = findData();
            logInfo("  ✓ Прочитано строк данных: " + data.size());

            logInfo("==========================================");
            logInfo("ПАРСИНГ УСПЕШНО ЗАВЕРШЕН!");
            logInfo("==========================================");

        } catch (Exception e) {
            logError("!!! ОШИБКА ПРИ ПАРСИНГЕ !!!");
            logError("Тип ошибки: " + e.getClass().getSimpleName());
            logError("Сообщение: " + e.getMessage());
            logError("Стек вызовов:");
            for (StackTraceElement element : e.getStackTrace()) {
                logError("  at " + element.toString());
            }
            throw e;
        }
    }

    private void createLogWindowSync() {
        if (Platform.isFxApplicationThread()) {
            createLogWindow();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    createLogWindow();
                } finally {
                    latch.countDown();
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createLogWindow() {
        logStage = new Stage();
        logStage.setTitle("Процесс парсинга Excel - " + file.getName());
        logStage.setWidth(600);
        logStage.setHeight(600);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        if (logBuffer.length() > 0) {
            logArea.appendText(logBuffer.toString());
            logBuffer.setLength(0);
        }

        StackPane root = new StackPane(logArea);
        Scene scene = new Scene(root);
        logStage.setScene(scene);
        logStage.show();
    }

    private void logInfo(String message) {
        String logMessage = message + "\n";
        System.out.print(logMessage);

        if (logArea != null) {
            if (Platform.isFxApplicationThread()) {
                logArea.appendText(logMessage);
            } else {
                Platform.runLater(() -> {
                    if (logArea != null) {
                        logArea.appendText(logMessage);
                    }
                });
            }
        } else {
            logBuffer.append(logMessage);
        }
    }

    private void logError(String message) {
        String logMessage = "❌ " + message + "\n";
        System.err.print(logMessage);

        if (logArea != null) {
            if (Platform.isFxApplicationThread()) {
                logArea.appendText(logMessage);
            } else {
                Platform.runLater(() -> {
                    if (logArea != null) {
                        logArea.appendText(logMessage);
                    }
                });
            }
        } else {
            logBuffer.append(logMessage);
        }
    }

    private String getDataFromCell(int rowIndex, int cellIndex) {
        try {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return "";
            }

            Cell cell = row.getCell(cellIndex);
            if (cell == null) {
                return "";
            }

            if (cell.getCellType().equals(CellType.NUMERIC)) {
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((int) numericValue);
                }
                return String.valueOf(numericValue);
            } else {
                return cell.getStringCellValue();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private List<String> findSetOfColNames() {
        setOfColNames = new ArrayList<>();
        setOfColNames.add("Цвет");
        setOfColNames.addAll(hashMapHeader.values());
        return setOfColNames;
    }

    private Integer findHeadRowIndex(Sheet sheet) {
        Integer num = null;
        for (int i = 0; i < MAX_LINES; i++) {
            try {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String cellValue1 = "";
                String cellValue2 = "";

                Cell cell1 = row.getCell(0);
                if (cell1 != null) {
                    cellValue1 = cell1.getStringCellValue();
                }

                Cell cell2 = row.getCell(1);
                if (cell2 != null) {
                    cellValue2 = cell2.getStringCellValue();
                }

                if (ROW_NUM.toString().equals(cellValue1) ||
                        KRP.toString().equals(cellValue2)) {
                    num = row.getRowNum();
                    break;
                }
            } catch (Exception e) {
                // Пропускаем ошибки при поиске
            }
        }
        if (num == null) {
            logError("НЕ НАЙДЕНА СТРОКА ЗАГОЛОВКОВ!");
            return -1;
        }
        return num;
    }

    private int findLastRowInTable() {
        int lastRow = headRowIndex;
        for (; lastRow < MAX_LINES; lastRow++) {
            try {
                Row row = sheet.getRow(lastRow);
                if (row == null) {
                    return lastRow;
                }

                Cell cell = row.getCell(0);
                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    return lastRow;
                }

                if (cell.getCellType() == CellType.STRING &&
                        cell.getStringCellValue().trim().isEmpty()) {
                    return lastRow;
                }

            } catch (Exception e) {
                return lastRow;
            }
        }
        return lastRow;
    }

    private HashMap<Integer, String> findHashMapHeader() {
        hashMapHeader = new HashMap<>();

        if (headRowIndex == -1) {
            return hashMapHeader;
        }

        Row headerRow = sheet.getRow(headRowIndex);

        if (headerRow == null) {
            logError("Строка заголовков пуста!");
            return hashMapHeader;
        }

        for (int colNum = 0; colNum < 100; colNum++) {
            try {
                Cell cell = headerRow.getCell(colNum);
                if (cell == null) {
                    break;
                }

                String cellValue = cell.getStringCellValue();
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    if (colNamesList.contains(cellValue.trim())) {
                        hashMapHeader.put(cell.getColumnIndex(), cellValue.trim());
                        logInfo("  → Найдена колонка '" + cellValue.trim() + "' в позиции " + cell.getColumnIndex());
                    }
                }
            } catch (Exception e) {
                break;
            }
        }

        if (hashMapHeader.isEmpty()) {
            logError("НЕ НАЙДЕНО НИ ОДНОЙ КОЛОНКИ ИЗ СПИСКА ДОПУСТИМЫХ!");
        }

        return hashMapHeader;
    }

    private List<List<String>> findData() {
        data = FXCollections.observableArrayList();
        int processedRows = 0;
        int errorRows = 0;

        logInfo("[ЧТЕНИЕ ДАННЫХ] Начинаем чтение " + (lastRowIndex - firstRowIndex) + " строк...");

        for (int rowNum = firstRowIndex; rowNum < lastRowIndex; rowNum++) {
            try {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    errorRows++;
                    continue;
                }

                Iterator<Integer> it = setOfColIndexes.iterator();
                List<String> oneRowData = new ArrayList<>();

                String color = findRowStatus(rowNum);
                oneRowData.add(color);

                while (it.hasNext()) {
                    oneRowData.add(getDataFromCell(rowNum, it.next()));
                }

                data.add(oneRowData);
                processedRows++;

                if (processedRows % 20 == 0 || processedRows == 1) {
                    logInfo("  Прочитано строк: " + processedRows + " / " + (lastRowIndex - firstRowIndex));
                }

            } catch (Exception e) {
                errorRows++;
                logError("Ошибка чтения строки Excel " + rowNum + ": " + e.getMessage());
            }
        }

        logInfo("  ✓ Чтение данных завершено. Успешно: " + processedRows + ", ошибок: " + errorRows);
        return data;
    }

    private String findRowStatus(int rowNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return "WHITE";
            }

            Cell cell = row.getCell(3);
            if (cell == null) {
                return "WHITE";
            }

            CellStyle style = cell.getCellStyle();
            if (style == null) {
                return "WHITE";
            }

            Color color = style.getFillForegroundColorColor();
            if (color instanceof XSSFColor) {
                XSSFColor xssfColor = (XSSFColor) color;
                String hexColor = xssfColor.getARGBHex();

                if (hexColor == null || EColor.byHEX(hexColor) == null) {
                    return "WHITE";
                } else {
                    return EColor.byHEX(hexColor).toString();
                }
            } else {
                return "WHITE";
            }

        } catch (Exception e) {
            return "WHITE";
        }
    }

    private HashMap<String, Integer> findModelColNames() {
        modelColNames = new HashMap<>();

        for (int i = 0; i < setOfColNames.size(); i++) {
            modelColNames.put(setOfColNames.get(i), i);
        }

        return modelColNames;
    }

    private HashMap<Integer, Integer> findExecutions() {
        executions = new HashMap<>();
        int ex = 0;
        for (int i = 0; i < setOfColNames.size(); i++) {
            if (setOfColNames.get(i).equals(TOTAL_AMOUNT.toString())) {
                executions.put(ex, i);
                ex++;
            }
        }
        return executions;
    }

    public ObservableList<EditorRow> findModelData() {
        logInfo("==========================================");
        logInfo("ФОРМИРОВАНИЕ МОДЕЛИ ДАННЫХ");
        logInfo("==========================================");

        ObservableList<EditorRow> editorRowData = FXCollections.observableArrayList();
        int processedRows = 0;
        int errorRows = 0;

        logInfo("[ОБРАБОТКА] Начинаем формирование " + data.size() + " строк модели...");
        logInfo("------------------------------------------");

        for (int i = 0; i < data.size(); i++) {
            try {
                List<String> row = new ArrayList<>(data.get(i));
                EditorRow editorRow = new EditorRow();

                String excelRowNum = String.valueOf(firstRowIndex + i);

                editorRow.setColor(row.get(0));
                editorRow.setRowNumber(splitDotZero(row.get(modelColNames.get(ROW_NUM.toString()))));
                editorRow.setKrp(row.get(modelColNames.get(KRP.toString())));
                editorRow.setDecNumber(row.get(modelColNames.get(DEC_NUM.toString())));
                editorRow.setName(row.get(modelColNames.get(NAME.toString())));

                if (colLaсquerExist && modelColNames.containsKey(LACQUER.toString()))
                    editorRow.setLacquer(row.get(modelColNames.get(LACQUER.toString())));

                editorRow.setCoat(row.get(modelColNames.get(COAT.toString())));

                if (colZpcExist && modelColNames.containsKey(ZCP.toString()))
                    editorRow.setZcp(row.get(modelColNames.get(ZCP.toString())));

                ArrayList<EditorRow.Execution> exs = new ArrayList<>();
                for (Integer ex : executions.keySet()) {
                    String am = splitDotZero(row.get(executions.get(ex)));
                    String amAs = "";
                    if (colAmountPerAssembleExist) {
                        int amAsIndex = executions.get(ex) + 1;
                        if (amAsIndex < row.size()) {
                            amAs = row.get(amAsIndex);
                        }
                    }
                    String exId = "ex" + ex;
                    EditorRow.Execution exx = new EditorRow.Execution(exId, am, amAs);
                    exs.add(exx);
                }
                editorRow.setExecutions(exs);

                editorRow.setFolder(row.get(modelColNames.get(FOLDER.toString())));
                editorRow.setMaterial(row.get(modelColNames.get(MATERIAL.toString())));
                editorRow.setParamA(splitDotZero(row.get(modelColNames.get(A.toString()))));
                editorRow.setParamB(splitDotZero(row.get(modelColNames.get(B.toString()))));

                editorRowData.add(editorRow);
                processedRows++;

                String rowInfo = String.format("  Строка Excel %4s: %s",
                        excelRowNum,
                        truncateString(editorRow.getName(), 50));
                logInfo(rowInfo);

            } catch (Exception e) {
                errorRows++;
                logError("Ошибка формирования строки Excel " + (firstRowIndex + i) + ": " + e.getMessage());
            }
        }

        logInfo("------------------------------------------");
        logInfo("  ✓ Модель данных сформирована. Строк: " + processedRows + ", ошибок: " + errorRows);
        logInfo("==========================================");

        return editorRowData;
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }

    private String splitDotZero(String initStr) {
        if (initStr == null || initStr.isEmpty()) {
            return "";
        }

        if (initStr.contains(".")) {
            String[] parts = initStr.split("\\.");
            if (parts.length > 0) {
                return parts[0];
            }
        }
        return initStr;
    }

    public String getExecutionName(int ex) {
        try {
            if (headRowIndex == 1) return "";

            int rowExName = 1;
            Integer colIndex = executions.get(ex);

            if (colIndex != null) {
                Row row = sheet.getRow(rowExName);
                if (row != null) {
                    Cell cell = row.getCell(colIndex);
                    if (cell != null) {
                        return cell.getStringCellValue();
                    }
                }
            }
        } catch (Exception e) {
            logError("Ошибка получения имени исполнения " + ex);
        }
        return "";
    }

    public String getExecutionDescription(int ex) {
        try {
            if (headRowIndex == 1 || headRowIndex == 2) return "";

            Integer colIndex = executions.get(ex);
            if (colIndex == null) return "";

            if (headRowIndex == 3) {
                int rowExDesc = 2;
                Row row = sheet.getRow(rowExDesc);
                if (row != null) {
                    Cell cell = row.getCell(colIndex);
                    if (cell != null) {
                        return cell.getStringCellValue();
                    }
                }
            } else {
                int rowExDesc = 2;
                Row row1 = sheet.getRow(rowExDesc);
                Row row2 = sheet.getRow(rowExDesc + 1);

                String desc1 = "";
                String desc2 = "";

                if (row1 != null) {
                    Cell cell1 = row1.getCell(colIndex);
                    if (cell1 != null) desc1 = cell1.getStringCellValue();
                }

                if (row2 != null) {
                    Cell cell2 = row2.getCell(colIndex);
                    if (cell2 != null) desc2 = cell2.getStringCellValue();
                }

                return desc1 + desc2;
            }
        } catch (Exception e) {
            logError("Ошибка получения описания исполнения " + ex);
        }
        return "";
    }

    public void closeLogWindow() {
        if (logStage != null) {
            if (Platform.isFxApplicationThread()) {
                logStage.close();
            } else {
                Platform.runLater(() -> {
                    if (logStage != null) {
                        logStage.close();
                    }
                });
            }
        }
    }
}
