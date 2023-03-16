package ru.wert.normic.controllers._forms;


import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import ru.wert.normic.AppStatics;
import ru.wert.normic.controllers.extra.ColorsController;
import ru.wert.normic.controllers.extra.ReportController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpAssm;
import ru.wert.normic.entities.settings.AppColor;
import ru.wert.normic.excel.ExcelImporter;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuForm;
import ru.wert.normic.components.BXTimeMeasurement;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.settings.ProductSettings;
import ru.wert.normic.utils.AppFiles;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.KEYS_NOW_PRESSED;
import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.decoration.DecorationStatic.*;
import static ru.wert.normic.enums.EColor.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

/**
 * ОСНОВНАЯ ФОРМА - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ГЛАВНОЙ СБОРКИ
 */
@Slf4j
public class MainController extends AbstractFormController {


    @FXML@Getter
    private Label lblProductName;

    @FXML @Getter
    private ComboBox<ETimeMeasurement> cmbxTimeMeasurement;

    @FXML @Getter //AbstractFormController
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private Button btnSave, btnErase, btnOpen, btnReport, btnColors, btnConstants, btnMaterials, btnImportExcel;

    @FXML
    private TextField tfMechanicalTime, tfPaintingTime, tfAssemblingTime, tfPackingTime;

    @FXML @Getter
    private TextField tfTotalTime;

    @FXML
    private Label lblTimeMeasure;


    @FXML
    void initialize(){
        MAIN_CONTROLLER = this;

        //Запускаем  перехват нажатых клавишь
        Platform.runLater(()->createButtonInterceptor());

        AppStatics.MEASURE = cmbxTimeMeasurement;
        opData = new OpAssm();

        //Создаем меню
        createMenu();

        initViews();

        setDragAndDropCellFactory();

        //Инициализируем комбобоксы
        new BXTimeMeasurement().create(cmbxTimeMeasurement, MIN);

        //Заполняем поля формы
        fillOpData();

        Platform.runLater(()->LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + "НОВОЕ ИЗДЕЛИЕ"));

    }

    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }

    private void initViews() {

        //СОХРАНИТЬ
        btnSave.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/save.png")), 32,32, true, true)));
        btnSave.setTooltip(new Tooltip("Сохранить"));
        btnSave.setOnAction(e->save(opData, addedOperations, "", e));
        //ОТКРЫТЬ
        btnOpen.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/open.png")), 32,32, true, true)));
        btnOpen.setTooltip(new Tooltip("Открыть"));
        btnOpen.setOnAction(this::open);
        //ОЧИСТИТЬ
        btnErase.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/erase.png")), 32,32, true, true)));
        btnErase.setTooltip(new Tooltip("Очистить"));
        btnErase.setOnAction(this::clearAll);
        //ОТЧЕТ
        btnReport.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/report.png")), 32,32, true, true)));
        btnReport.setTooltip(new Tooltip("Отчет"));
        btnReport.setOnAction(this::report);
        //ПОКРЫТИЕ
        btnColors.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/colors.png")), 32,32, true, true)));
        btnColors.setTooltip(new Tooltip("Покрытие"));
        btnColors.setOnAction(this::colors);
        //РАСЧЕТНЫЕ КОНСТАНТЫ
        btnConstants.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/constants.png")), 32,32, true, true)));
        btnConstants.setTooltip(new Tooltip("Расчетные константы"));
        btnConstants.setOnAction(this::constants);
        //МАТЕРИАЛЫ
        btnMaterials.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 32,32, true, true)));
        btnMaterials.setTooltip(new Tooltip("Материалы"));
        btnMaterials.setOnAction(this::materials);

        //ИМПОРТ EXCEL
        btnImportExcel.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/excel.png")), 32,32, true, true)));
        btnImportExcel.setTooltip(new Tooltip("Импорт Excel"));
        btnImportExcel.setOnAction(this::importExcel);


        cmbxTimeMeasurement.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblTimeMeasure.setText(newValue.getName());
            countSumNormTimeByShops();
        });

    }

    private void importExcel(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы EXCEL (.xlsx)", "*.xlsx"));
        chooser.setInitialDirectory(new File(AppProperties.getInstance().getSavesDir()));
        File file = chooser.showOpenDialog(((Node)e.getSource()).getScene().getWindow());
        if(file == null) return;

        File copied = AppFiles.getInstance().createTempCopyOfFile(file);
        clearAll(e);
        try {
            OpAssm newOpData = new  ExcelImporter().convertOpAssmFromExcel(copied);
            if(newOpData != null) opData = newOpData;
            else return;
            createMenu();
            menu.deployData();
            countSumNormTimeByShops();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * СОХРАНИТЬ ИЗДЕЛИЕ
     */
    public static void save(OpData opData, List<OpData> addedOperations, String initialName, Event e) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы норм времени (.nvr)", "*.nvr"));
        chooser.setInitialDirectory(new File(AppProperties.getInstance().getSavesDir()));
        chooser.setInitialFileName(initialName);
        Stage owner = e.getSource() instanceof Node ?
                (Stage) ((Node) e.getSource()).getScene().getWindow() :
                (Stage)((MenuItem)e.getSource()).getParentPopup().getOwnerWindow();
        File file = chooser.showSaveDialog(owner);
        if (file == null) return;

        ((IOpWithOperations)opData).setName(file.getName());
        AppProperties.getInstance().setSavesDirectory(file.getParent());
        ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));

        Gson gson = new Gson();
        ProductSettings settings = new ProductSettings();
        settings.setColor1(new AppColor(COLOR_I.getName(), COLOR_I.getRal(), COLOR_I.getConsumption()));
        settings.setColor2(new AppColor(COLOR_II.getName(), COLOR_II.getRal(), COLOR_II.getConsumption()));
        settings.setColor3(new AppColor(COLOR_III.getName(), COLOR_III.getRal(), COLOR_III.getConsumption()));

        //Создаем строки для сохранения в коллекцию
        String savedOpType = opData.getOpType().name().replace("\"", "");
        String productSettings = gson.toJson(settings);
        String productData = gson.toJson(opData);
        List<String> product = Arrays.asList(savedOpType, productSettings, productData);
        saveTextToFile(product, file);

        LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + file.getName().replace(".nvr", ""));

    }

    /**
     * ОТЧЕТ
     */
    private void report(Event e) {
        //
        ((OpAssm)opData).setOperations(getAddedOperations());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/report.fxml"));
            VBox report = loader.load();
            ReportController controller = loader.getController();
            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));
            controller.init((OpAssm) opData);

            new Decoration(
                    "ОТЧЕТ",
                    report,
                    false,
                    (Stage) ((Node)e.getSource()).getScene().getWindow(),
                    "decoration-report",
                    true,
                    false);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * НАСТРОЙКИ ИЗДЕЛИЯ
     */
    private void colors(Event e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/colors.fxml"));
            VBox settings = loader.load();
            ColorsController controller = loader.getController();
            controller.init();
            Decoration decoration = new Decoration(
                    "НАСТРОЙКИ ИЗДЕЛИЯ",
                    settings,
                    false,
                    (Stage) ((Node)e.getSource()).getScene().getWindow(),
                    "decoration-settings",
                    false,
                    false);

            decoration.getImgCloseWindow().setOnMousePressed(ev->{
                controller.saveSettings();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * РАСЧЕТНЫЕ КОНСТАНТЫ
     */
    private void constants(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/constants.fxml"));
            Parent parent = loader.load();
            Decoration decoration = new Decoration(
                    "РАСЧЕТНЫЕ КОНСТАНТЫ",
                    parent,
                    false,
                    (Stage) ((Node)event.getSource()).getScene().getWindow(),
                    "decoration-settings",
                    false,
                    false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * РАСЧЕТНЫЕ КОНСТАНТЫ
     */
    private void materials(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialsTV.fxml"));
            Parent parent = loader.load();
            Decoration decoration = new Decoration(
                    "МАТЕРИАЛЫ",
                    parent,
                    false,
                    (Stage) ((Node)event.getSource()).getScene().getWindow(),
                    "decoration-settings",
                    false,
                    false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохранение строк в созданный файл
     * @param product, List<String>
     * @param file, File
     */
    private static void saveTextToFile(List<String> product, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            for(String line : product) {
                writer.println(line);
            }
            writer.close();
        } catch (FileNotFoundException exception) {
            log.error(String.format("Не удалось записать файл %s", file.getName()));
            exception.printStackTrace();
        }
    }




    @Override
    public void createMenu() {

        menu = new MenuForm(this, listViewTechOperations, (IOpWithOperations) opData);

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemWeldLongSeam());
        menu.getItems().addAll(menu.createItemWeldingDotted());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAssmNuts());
        menu.getItems().addAll(menu.createItemAssmNutsMK());
        menu.getItems().addAll(menu.createItemAssmCuttings());
        menu.getItems().addAll(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());

        linkMenuToButton();

    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops(){
        String measure = MIN.getName();

        double mechanicalTime = 0.0;
        double paintingTime = 0.0;
        double assemblingTime = 0.0;
        double packingTime = 0.0;

        for(OpData cn: addedOperations){
            mechanicalTime += cn.getMechTime() * cn.getQuantity();
            paintingTime += cn.getPaintTime() * cn.getQuantity();
            assemblingTime += cn.getAssmTime() * cn.getQuantity();
            packingTime += cn.getPackTime() * cn.getQuantity();
        }

        opData.setMechTime(mechanicalTime);
        opData.setPaintTime(paintingTime);
        opData.setAssmTime(assemblingTime);
        opData.setPackTime(packingTime);

        if(cmbxTimeMeasurement.getValue().equals(ETimeMeasurement.SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;
            assemblingTime = assemblingTime * MIN_TO_SEC;
            packingTime = packingTime * MIN_TO_SEC;

            measure = SEC.getName();
        }

        String format = DOUBLE_FORMAT;
        if(cmbxTimeMeasurement.getValue().equals(ETimeMeasurement.SEC)) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime).trim());
        tfPaintingTime.setText(String.format(format, paintingTime).trim());
        tfAssemblingTime.setText(String.format(format, assemblingTime).trim());
        tfPackingTime.setText(String.format(format, packingTime).trim());

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime).trim());

        lblTimeMeasure.setText(measure);

    }



    @Override
    public void init(AbstractFormController controller, TextField tfName, OpData opData) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /**
     * Перехватчик нажатых клавиш
     * При нажатии, клавиша сохраняется в CH_KEYS_NOW_PRESSED,
     * при отпускании, клавиша удаляется из CH_KEYS_NOW_PRESSED
     */
    private void createButtonInterceptor() {

        KEYS_NOW_PRESSED = new ArrayList<>();

        MAIN_STAGE.getScene().setOnKeyPressed((e)->{
            KEYS_NOW_PRESSED.add(e.getCode());
        });

        MAIN_STAGE.getScene().setOnKeyReleased((e)->{
            KEYS_NOW_PRESSED.remove(e.getCode());
        });
    }

}