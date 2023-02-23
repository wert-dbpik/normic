package ru.wert.normic.controllers._forms;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import org.json.JSONException;
import ru.wert.normic.AppStatics;
import ru.wert.normic.controllers.extra.ColorsController;
import ru.wert.normic.controllers.extra.ReportController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.settings.AppColor;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuOps;
import ru.wert.normic.components.BXTimeMeasurement;
import ru.wert.normic.controllers.assembling.PlateAssmController;
import ru.wert.normic.controllers.assembling.PlateDetailController;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.settings.ProductSettings;
import ru.wert.normic.utils.OpDataJsonConverter;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.KEYS_NOW_PRESSED;
import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;
import static ru.wert.normic.enums.EColor.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

/**
 * ОСНОВНАЯ ФОРМА - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ГЛАВНОЙ СБОРКИ
 */
@Slf4j
public class MainController extends AbstractFormController {

    @FXML @Getter
    private ComboBox<ETimeMeasurement> cmbxTimeMeasurement;

    @FXML @Getter //AbstractFormController
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private Button btnSave, btnErase, btnOpen, btnReport, btnColors, btnConstants, btnMaterials;

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
        btnSave.setOnAction(this::save);
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


        cmbxTimeMeasurement.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblTimeMeasure.setText(newValue.getName());
            countSumNormTimeByShops();
        });



    }


    /**
     * ОЧИСТИТЬ ВСЕ
     */
    private void clearAll(Event e) {
        ((IOpWithOperations)opData).getOperations().clear();
        addedPlates.clear();
        addedOperations.clear();
        listViewTechOperations.getItems().clear();
        countSumNormTimeByShops();
        PlateDetailController.nameIndex = 0;
        PlateAssmController.nameIndex = 0;
    }

    /**
     * СОХРАНИТЬ ИЗДЕЛИЕ
     */
    private void save(Event e) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы норм времени (.nvr)", "*.nvr"));
        chooser.setInitialDirectory(new File(AppProperties.getInstance().getSavesDir()));
        File file = chooser.showSaveDialog(((Node) e.getSource()).getScene().getWindow());
        if (file == null) return;
        ((OpAssm)opData).setName(file.getName());
        AppProperties.getInstance().setSavesDirectory(file.getParent());
        ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));

        Gson gson = new Gson();
        ProductSettings settings = new ProductSettings();
        settings.setColor1(new AppColor(COLOR_I.getName(), COLOR_I.getRal(), COLOR_I.getConsumption()));
        settings.setColor2(new AppColor(COLOR_II.getName(), COLOR_II.getRal(), COLOR_II.getConsumption()));
        settings.setColor3(new AppColor(COLOR_III.getName(), COLOR_III.getRal(), COLOR_III.getConsumption()));

        //Создаем строки для сохранения в коллекцию
        String productSettings = gson.toJson(settings);
        String productData = gson.toJson(opData);
        List<String> product = Arrays.asList(productSettings, productData);
        saveTextToFile(product, file);

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
                    true);

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
    private void saveTextToFile(List<String> product, File file) {
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

    /**
     * ОТКРЫТЬ СОХРАНЕННОЕ ИЗДЕЛИЕ
     */
    private void open(Event e){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы норм времени (.nvr)", "*.nvr"));
        chooser.setInitialDirectory(new File(AppProperties.getInstance().getSavesDir()));
        File file = chooser.showOpenDialog(((Node)e.getSource()).getScene().getWindow());
        if(file == null) return;
        clearAll(e);
        try {
            //Читаем строки из файла
            BufferedReader reader = new BufferedReader(new FileReader(new File(file.toString())));
            ArrayList<String> store = new ArrayList<>();
            String line;
            while((line = reader.readLine())!= null){
                store.add(line);
            }

            //Настройки
            String settings = store.get(0);
            Gson gson = new Gson();
            Type settingsType = new TypeToken<ProductSettings>(){}.getType();
            ProductSettings productSettings = gson.fromJson(settings, settingsType);
            //Применяем настройки
            deployProductSettings(productSettings);

            //Структура
            String product = store.get(1);
            Type opDataType = new TypeToken<OpAssm>(){}.getType();
            opData = gson.fromJson(product, opDataType);
            //Применяем структуру
            deployJson(product);

            countSumNormTimeByShops();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    @Override
    public void createMenu() {

        menu = new MenuOps(this, listViewTechOperations, (IOpWithOperations) opData);

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemWeldLongSeam(), menu.createItemWeldingDotted());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAssmNuts(), menu.createItemAssmCuttings(), menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());

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

        tfMechanicalTime.setText(String.format(format, mechanicalTime));
        tfPaintingTime.setText(String.format(format, paintingTime));
        tfAssemblingTime.setText(String.format(format, assemblingTime));
        tfPackingTime.setText(String.format(format, packingTime));

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime));

        lblTimeMeasure.setText(measure);

    }

    /**
     * Применение НАСТРОЕК
     */
    private void deployProductSettings(ProductSettings settings) {
        COLOR_I.setRal(settings.getColor1().getRal());
        COLOR_II.setRal(settings.getColor2().getRal());
        COLOR_III.setRal(settings.getColor3().getRal());

        COLOR_I.setConsumption(settings.getColor1().getConsumption());
        COLOR_II.setConsumption(settings.getColor2().getConsumption());
        COLOR_III.setConsumption(settings.getColor3().getConsumption());
    }

    /**
     * Применение СТРУКТУРЫ
     */
    private void deployJson(String jsonString) {
        try {
            opData = (OpAssm) OpDataJsonConverter.convert(jsonString);
            createMenu();
            menu.deployData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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