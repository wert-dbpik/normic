package ru.wert.normic.controllers._forms;


import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.AppStatics;
import ru.wert.normic.controllers.extra.ColorsController;
import ru.wert.normic.controllers.extra.StructureController;
import ru.wert.normic.controllers.extra.ReportController;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.colors.AppColor;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.excel.ImportExcelFileService;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.IconMenuController;
import ru.wert.normic.menus.MainMenuController;
import ru.wert.normic.menus.MenuForm;
import ru.wert.normic.settings.ColorsSettings;
import ru.wert.normic.utils.AppFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.decoration.DecorationStatic.*;
import static ru.wert.normic.enums.EColor.*;
import static ru.wert.normic.enums.ETimeMeasurement.*;

/**
 * ОСНОВНАЯ ФОРМА - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ГЛАВНОЙ СБОРКИ
 */
@Slf4j
public class MainController extends AbstractFormController {

    @FXML@Getter
    private StackPane spMenuBar;

    @FXML@Getter
    private StackPane spIconMenu;

    @FXML@Getter
    private HBox progressIndicator;

    @FXML@Getter
    private HBox vbAboutPane;

    @FXML @Getter //AbstractFormController
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private TextField tfMechanicalTime, tfPaintingTime, tfAssemblingTime, tfPackingTime;

    @FXML @Getter
    private TextField tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    private MainMenuController mainMenuController;
    private IconMenuController iconMenuController;
    private Parent iconBar;
    private final BooleanProperty showIconMenuProperty = new SimpleBooleanProperty();


    @FXML
    void initialize(){
        MAIN_CONTROLLER = this;

        progressIndicator.setVisible(false);
        vbAboutPane.setVisible(false);

        //Запускаем  перехват нажатых клавишь
        Platform.runLater(this::createButtonInterceptor);

        AppStatics.MEASURE = new ToggleGroup();
        opData = new OpAssm();
        ((IOpWithOperations)opData).setName("Новое изделие");

        //Создаем меню
        createMenu();

        createMainMenu();

        createIconMenu();

        initViews();

        setDragAndDropCellFactory();

        //Заполняем поля формы
        fillOpData();

        Platform.runLater(()->LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + "НОВОЕ ИЗДЕЛИЕ"));

        menu.addEmptyPlate();

    }

    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }

    private void createMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menus/mainMenu.fxml"));
            Parent menuBar = loader.load();
            mainMenuController = loader.getController();
            spMenuBar.getChildren().add(menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainMenuController.getMSaveAs().setOnAction(e->save(opData, addedOperations, "", e, EMenuSource.MAIN_MENU));
        mainMenuController.getMOpen().setOnAction(e->open(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMClearAll().setOnAction(this::clearAll);
        mainMenuController.getMRapport1C().setOnAction(e->report(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMProductTree().setOnAction(e->productTree(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMColors().setOnAction(e->colors(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMConstants().setOnAction(e->constants(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMMaterials().setOnAction(e->materials(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMImportExcel().setOnAction(e->importExcel(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMIconMenu().setOnAction(e->showIconMenuProperty.set(!showIconMenuProperty.get()));
        mainMenuController.getMAbout().setOnAction(e-> vbAboutPane.setVisible(true));

        showIconMenuProperty.addListener((observable, oldValue, newValue) -> {
            if(showIconMenuProperty.get()){
                mainMenuController.getMIconMenu().setText("Скрыть панель управления");
                spIconMenu.getChildren().clear();
                spIconMenu.getChildren().add(iconBar);
            }else{
                mainMenuController.getMIconMenu().setText("Показать панель управления");
                spIconMenu.getChildren().clear();
            }
        });

    }

    private void createIconMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menus/iconMenu.fxml"));
            iconBar = loader.load();
            iconMenuController = loader.getController();
            showIconMenuProperty.set(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //СОХРАНИТЬ
        iconMenuController.getBtnSave().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/save.png")), 32,32, true, true)));
        iconMenuController.getBtnSave().setTooltip(new Tooltip("Сохранить"));
        iconMenuController.getBtnSave().setOnAction(e->save(opData, addedOperations, "", e, EMenuSource.ICON_MENU));

        //ОТКРЫТЬ
        iconMenuController.getBtnOpen().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/open.png")), 32,32, true, true)));
        iconMenuController.getBtnOpen().setTooltip(new Tooltip("Открыть"));
        iconMenuController.getBtnOpen().setOnAction(e->open(e, EMenuSource.ICON_MENU));

        //ОЧИСТИТЬ
        iconMenuController.getBtnClearAll().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/erase.png")), 32,32, true, true)));
        iconMenuController.getBtnClearAll().setTooltip(new Tooltip("Очистить"));
        iconMenuController.getBtnClearAll().setOnAction(this::clearAll);

        //ОТЧЕТ
        iconMenuController.getBtnReport1C().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/report.png")), 32,32, true, true)));
        iconMenuController.getBtnReport1C().setTooltip(new Tooltip("Отчет"));
        iconMenuController.getBtnReport1C().setOnAction(e->report(e, EMenuSource.ICON_MENU));

        //ПОКРЫТИЕ
        iconMenuController.getBtnColors().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/colors.png")), 32,32, true, true)));
        iconMenuController.getBtnColors().setTooltip(new Tooltip("Покрытие"));
        iconMenuController.getBtnColors().setOnAction(e->colors(e, EMenuSource.ICON_MENU));

        //РАСЧЕТНЫЕ КОНСТАНТЫ
        iconMenuController.getBtnConstants().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/constants.png")), 32,32, true, true)));
        iconMenuController.getBtnConstants().setTooltip(new Tooltip("Расчетные константы"));
        iconMenuController.getBtnConstants().setOnAction(e->constants(e, EMenuSource.ICON_MENU));

        //МАТЕРИАЛЫ
        iconMenuController.getBtnMaterials().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 32,32, true, true)));
        iconMenuController.getBtnMaterials().setTooltip(new Tooltip("Материалы"));
        iconMenuController.getBtnMaterials().setOnAction(e->materials(e, EMenuSource.ICON_MENU));

        //ИМПОРТ EXCEL
        iconMenuController.getBtnImportExcel().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/excel.png")), 32,32, true, true)));
        iconMenuController.getBtnImportExcel().setTooltip(new Tooltip("Импорт Excel"));
        iconMenuController.getBtnImportExcel().setOnAction(e->importExcel(e, EMenuSource.ICON_MENU));

        //ОТЧЕТ
        iconMenuController.getBtnProductTree().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/tree_view.png")), 32,32, true, true)));
        iconMenuController.getBtnProductTree().setTooltip(new Tooltip("Схема изделия"));
        iconMenuController.getBtnProductTree().setOnAction(e->productTree(e, EMenuSource.ICON_MENU));
    }

    private void initViews() {

        MEASURE.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ((IOpWithOperations)opData).setOperations(new ArrayList<>(addedOperations));

            CURRENT_MEASURE = ETimeMeasurement.findValueOf(AppStatics.MEASURE.getSelectedToggle().getUserData().toString());
            lblTimeMeasure.setText(ETimeMeasurement.findValueOf(newValue.getUserData().toString()).getMeasure());
            //Очистить все
            addedPlates.clear();
            addedOperations.clear();
            getListViewTechOperations().getItems().clear();
            countSumNormTimeByShops();
            PlateDetailController.nameIndex = 0;
            PlateAssmController.nameIndex = 0;

            fillOpData();
            countSumNormTimeByShops();
        });

        //Единицы измерения
        mainMenuController.getRbmSeconds().setToggleGroup(MEASURE);
        mainMenuController.getRbmSeconds().setUserData(SEC.name());
        mainMenuController.getRbmMinutes().setToggleGroup(MEASURE);
        mainMenuController.getRbmMinutes().setUserData(MIN.name());
        mainMenuController.getRbmHours().setToggleGroup(MEASURE);
        mainMenuController.getRbmHours().setUserData(HOUR.name());
        mainMenuController.getRbmHours().setSelected(true);
        countSumNormTimeByShops();



    }

    private void importExcel(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы EXCEL (.xlsx)", "*.xlsx"));
        File initDir = new File(AppProperties.getInstance().getSavesDir());
        chooser.setInitialDirectory(initDir.exists() ? initDir : new File("C:\\"));
        File file = chooser.showOpenDialog(owner);
        if(file == null) return;

        File copied = AppFiles.getInstance().createTempCopyOfFile(file);
        clearAll(e);

        ImportExcelFileService service = new ImportExcelFileService(this, copied);
        service.setOnSucceeded(workerStateEvent ->{
            blockUndoListFlag = true;
            OpAssm newOpData = service.getValue();
            if(newOpData != null) opData = newOpData;
            else return;
            createMenu();
            menu.deployData();
            countSumNormTimeByShops();
            iterateUndoList();
        });
        service.start();

    }

    /**
     * СОХРАНИТЬ ИЗДЕЛИЕ
     */
    public static void save(OpData opData, List<OpData> addedOperations, String initialName, Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы норм времени (.nvr)", "*.nvr"));
        File initFile = new File(AppProperties.getInstance().getSavesDir());
        chooser.setInitialDirectory(initFile.exists() ? initFile : new File("C:\\"));
        chooser.setInitialFileName(initialName);

        File file = chooser.showSaveDialog(owner);
        if (file == null) return;

        ((IOpWithOperations)opData).setName(file.getName());
        AppProperties.getInstance().setSavesDirectory(file.getParent());
        ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));

        Gson gson = new Gson();
        ColorsSettings settings = new ColorsSettings();
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
    private void report(Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();
        ((OpAssm)opData).setOperations(getAddedOperations());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/report1C.fxml"));
            VBox report = loader.load();
            ReportController controller = loader.getController();
            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));
            controller.init((OpAssm) opData);

            new Decoration(
                    "ОТЧЕТ",
                    report,
                    false,
                    owner,
                    "decoration-report",
                    true,
                    false);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * ДРЕВО
     */
    private void productTree(Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();
        ((OpAssm)opData).setOperations(getAddedOperations());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/structure.fxml"));
            Parent report = loader.load();
            StructureController controller = loader.getController();
            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));
            controller.create((OpAssm) opData);

            new Decoration(
                    "СХЕМА ИЗДЕЛИЯ",
                    report,
                    true,
                    owner,
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
    private void colors(Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/colors.fxml"));
            VBox settings = loader.load();
            ColorsController controller = loader.getController();
            controller.init();
            Decoration decoration = new Decoration(
                    "НАСТРОЙКИ ИЗДЕЛИЯ",
                    settings,
                    false,
                    owner,
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
    private void constants(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/constants.fxml"));
            Parent parent = loader.load();
            Decoration decoration = new Decoration(
                    "РАСЧЕТНЫЕ КОНСТАНТЫ",
                    parent,
                    false,
                    owner,
                    "decoration-settings",
                    false,
                    false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Материалы
     */
    private void materials(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialsTV.fxml"));
            Parent parent = loader.load();
            Decoration decoration = new Decoration(
                    "МАТЕРИАЛЫ",
                    parent,
                    false,
                    owner,
                    "decoration-settings",
                    false,
                    false);
        } catch (IOException ex) {
            ex.printStackTrace();
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
            writer = new PrintWriter(file, "UTF-8");
            for(String line : product) {
                writer.println(line);
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException exception) {
            log.error(String.format("Не удалось записать файл %s", file.getName()));
            exception.printStackTrace();
        }
    }




    @Override
    public MenuForm createMenu() {

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

        return menu;

    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops(){
        String measure = MIN.getMeasure();

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

        //Перевод в секунды
        if(CURRENT_MEASURE.equals(SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;
            assemblingTime = assemblingTime * MIN_TO_SEC;
            packingTime = packingTime * MIN_TO_SEC;

            measure = SEC.getMeasure();
        }

        //Перевод в часы
        if(CURRENT_MEASURE.equals(HOUR)){
            mechanicalTime = mechanicalTime * MIN_TO_HOUR;
            paintingTime = paintingTime * MIN_TO_HOUR;
            assemblingTime = assemblingTime * MIN_TO_HOUR;
            packingTime = packingTime * MIN_TO_HOUR;

            measure = HOUR.getMeasure();
        }

        String format = DOUBLE_FORMAT;
        if(MEASURE.getSelectedToggle().getUserData().equals(ETimeMeasurement.SEC.name())) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime).trim());
        tfPaintingTime.setText(String.format(format, paintingTime).trim());
        tfAssemblingTime.setText(String.format(format, assemblingTime).trim());
        tfPackingTime.setText(String.format(format, packingTime).trim());

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime).trim());

        lblTimeMeasure.setText(measure);

    }



    @Override
    public void init(AbstractFormController controller, TextField tfName, TextField tfQuantity, OpData opData) {
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