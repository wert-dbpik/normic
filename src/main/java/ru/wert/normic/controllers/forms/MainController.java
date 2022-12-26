package ru.wert.normic.controllers.forms;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;
import ru.wert.normic.components.BXTimeMeasurement;
import ru.wert.normic.controllers.PlateAssmController;
import ru.wert.normic.controllers.PlateDetailController;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.utils.OpDataJsonConverter;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

@Slf4j
public class MainController extends AbstractFormController {

    @FXML @Getter
    private ComboBox<ETimeMeasurement> cmbxTimeMeasurement;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private Button btnSave, btnErase, btnOpen, btnReport, btnColors, btnSettings, btnAddOperation;

    @FXML
    private TextField tfMechanicalTime, tfPaintingTime, tfAssemblingTime, tfPackingTime;

    @FXML @Getter
    private TextField tfTotalTime;

    @FXML
    private Label lblTimeMeasure;


    @FXML
    void initialize(){
        MAIN_CONTROLLER = this;

        AppStatics.MEASURE = cmbxTimeMeasurement;
        opData = new OpAssm();

        //Инициализируем список операционных плашек
        addedPlates = FXCollections.observableArrayList();
        addedOperations = new ArrayList<>();

        //Создаем меню
        createMenu();

        initViews();

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
        //НАСТРОЙКИ
        btnSettings.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/settings.png")), 32,32, true, true)));
        btnSettings.setTooltip(new Tooltip("Настройки"));
//        btnSettings.setOnAction(this::settings);


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
        String json = gson.toJson(opData);
        saveTextToFile(json, file);

    }

    /**
     * ОТЧЕТ
     */
    private void report(Event e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/colors.fxml"));
            VBox settings = loader.load();
            ColorsController controller = loader.getController();
            controller.init();
            Decoration decoration = new Decoration(
                    "НАСТРОЙКИ ИЗДЕЛИЯ",
                    settings,
                    false,
                    (Stage) ((Node)e.getSource()).getScene().getWindow(),
                    "decoration-settings",
                    true);

            decoration.getImgCloseWindow().setOnMousePressed(ev->{
                controller.saveSettings();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
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
            String str = new String(Files.readAllBytes(Paths.get(file.toString())));
            Gson gson = new Gson();
            Type listType = new TypeToken<OpAssm>(){}.getType();
            opData = gson.fromJson(str, listType);

            deployJson(str);
            countSumNormTimeByShops();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    private void createMenu() {

        menu = new MenuCalculator(this, listViewTechOperations, (IOpWithOperations) opData);

        menu.getItems().add(menu.createItemAddDetail());
        menu.getItems().add(menu.createItemAddAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddWeldLongSeam(), menu.createItemAddWeldingDotted());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddAssmNuts(), menu.createItemAddAssmCuttings(), menu.createItemAddAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddLevelingSealer());

        tyeMenuToButton();

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

}