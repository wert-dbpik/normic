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
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.components.TFBatch;
import ru.wert.normic.controllers.extra.ColorsController;
import ru.wert.normic.controllers.intro.NoConnection;
import ru.wert.normic.controllers.normsTableView.NormsTableViewController;
import ru.wert.normic.controllers.structure.StructureController;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;
import ru.wert.normic.entities.saves.SaveNormEntry;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.history.HistoryFile;
import ru.wert.normic.operations.OperationsController;
import ru.wert.normic.report.ReportController;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.decoration.warnings.Warning0;
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
import ru.wert.normic.settings.ProductSettings;
import ru.wert.normic.utils.AppFiles;
import ru.wert.normic.utils.NvrConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.StartNormic.FIRST_PARAMS;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.controllers.extra.SavesHistoryController.getCurrentTime;
import static ru.wert.normic.decoration.DecorationStatic.*;
import static ru.wert.normic.enums.EColor.*;
import static ru.wert.normic.enums.ETimeMeasurement.*;

/**
 * ОСНОВНАЯ ФОРМА - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ГЛАВНОЙ СБОРКИ
 */
@Slf4j
public class MainController extends AbstractFormController {

    @FXML
    @Getter
    private StackPane spMenuBar;

    @FXML
    @Getter
    private StackPane spIconMenu;

    @FXML
    @Getter
    private HBox progressIndicator;

    @FXML
    @Getter
    private HBox vbAboutPane;

    @FXML
    @Getter //AbstractFormController
    private ListView<VBox> listViewTechOperations;

    @FXML
    @Getter
    private Button btnAddOperation;

    @FXML
    private TextField tfMechanicalTime, tfPaintingTime, tfAssemblingTime, tfPackingTime;

    @FXML
    @Getter
    private TextField tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    @FXML
    @Getter
    private TextField tfBatch;

    @FXML
    @Getter
    private HBox hbBatchness;

    public static File savedProductFile = null;

    private MainMenuController mainMenuController;
    private IconMenuController iconMenuController;
    private Parent iconBar;
    private final BooleanProperty showIconMenuProperty = new SimpleBooleanProperty();


    @FXML
    void initialize() {
        MAIN_CONTROLLER = this;

        progressIndicator.setVisible(false);
        vbAboutPane.setVisible(false);

        //Запускаем  перехват нажатых клавишь
        Platform.runLater(this::createButtonInterceptor);

        AppStatics.MEASURE = new ToggleGroup();

        new TFBatch(tfBatch, this);

        opData = new OpAssm();
        MAIN_OP_DATA = (OpAssm) opData;

        ((IOpWithOperations) opData).setName("Новое изделие");

        //Создаем меню
        createMenu();

        createMainMenu();

        createIconMenu();

        initViews();

        setDragAndDropCellFactory();

        //Заполняем поля формы
        fillOpData();

        menu.addEmptyPlate();

        Platform.runLater(() -> {
            if (FIRST_PARAMS.length > 0) { //Если открывается норма по двойному клику

                File openingFile = new File(FIRST_PARAMS[0]);

                NvrConverter convertor = new NvrConverter(openingFile);
                ProductSettings productSettings = convertor.getProductSettings();
                OpData newOpData = convertor.getConvertedOpData();

                deployOpDataFromFile(null, EMenuSource.ON_START, openingFile, newOpData, productSettings);
                HistoryFile.getInstance().addFileToHistory(openingFile);

            } else
                LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + "НОВОЕ ИЗДЕЛИЕ");
        });

    }

    @Override //AbstractFormController
    public void fillOpData() {
        if (!((IOpWithOperations) opData).getOperations().isEmpty())
            menu.addListOfOperations();

        tfBatch.setText(String.valueOf(CURRENT_BATCH));
    }

    private void createMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menus/mainMenu.fxml"));
            Parent menuBar = loader.load();
            mainMenuController = loader.getController();
            spMenuBar.getChildren().add(menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainMenuController.getMSave().setOnAction(e -> save(opData, addedOperations, e));
        mainMenuController.getMSaveAs().setOnAction(e -> saveAs(opData, addedOperations,
                CURRENT_PRODUCT_NAME,
                e, EMenuSource.MAIN_MENU));
        mainMenuController.getMSavesHistory().setOnAction(e -> openSavesHistory(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMOpen().setOnAction(e -> open(e, EMenuSource.MAIN_MENU));
        //При нажатии на МЕНЮ готовится список последних открываемых файлов
        mainMenuController.getMFile().setOnShowing(e -> prepareRecentFiles(mainMenuController.getMOpenRecent()));
        mainMenuController.getMClearAll().setOnAction(e -> clearAll(e, true, true));

        mainMenuController.getChmBatchness().setSelected(BATCHNESS.get());
        mainMenuController.getChmBatchness().setOnAction(e->batchness());
        mainMenuController.getChmBatchness().selectedProperty().bindBidirectional(BATCHNESS);

        mainMenuController.getMRapport1C().setOnAction(e -> report(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMProductTree().setOnAction(e -> productTree(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMTableView().setOnAction(e -> openTableView(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMColors().setOnAction(e -> colors(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMConstants().setOnAction(e -> constants(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMOperations().setOnAction(e -> operations(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMMaterials().setOnAction(e -> materials(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMImportExcel().setOnAction(e -> importExcel(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMChangeUser().setOnAction(e -> changeUser(e, EMenuSource.MAIN_MENU));
        mainMenuController.getMChangeServer().setOnAction(e -> changeServer());
        mainMenuController.getMIconMenu().setOnAction(e -> showIconMenuProperty.set(!showIconMenuProperty.get()));
        mainMenuController.getMAbout().setOnAction(e -> vbAboutPane.setVisible(true));

        showIconMenuProperty.addListener((observable, oldValue, newValue) -> {
            if (showIconMenuProperty.get()) {
                mainMenuController.getMIconMenu().setText("Скрыть панель управления");
                spIconMenu.getChildren().clear();
                spIconMenu.getChildren().add(iconBar);
            } else {
                mainMenuController.getMIconMenu().setText("Показать панель управления");
                spIconMenu.getChildren().clear();
            }
        });

    }

    /**
     * ТАБЛИЧНОЕ ПРЕДСТАВЛЕНИЕ
     */
    private void openTableView(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/normsTableView/normsTableView.fxml"));
            Parent parent = loader.load();
            NormsTableViewController controller = loader.getController();
            controller.init((OpAssm) opData, 1);
            new Decoration("Табличное представление",
                    parent,
                    true,
                    owner,
                    "decoration-login",
                    false,
                    true);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * ИСТОРИЯ СОХРАНЕНИЙ
     */
    private void openSavesHistory(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/savesHistory.fxml"));
            Parent parent = loader.load();
            new Decoration("История сохранений",
                    parent,
                    true,
                    owner,
                    "decoration-login",
                    false,
                    true);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void batchness() {
        hbBatchness.setVisible(BATCHNESS.get());
        recountMainOpData();
    }

    private void operations(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operations/operations.fxml"));
            Parent parent = loader.load();
            OperationsController controller = loader.getController();
            controller.init();
            new Decoration("Свои операции",
                    parent,
                    true,
                    owner,
                    "decoration-login",
                    false,
                    true);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * СМЕНИТЬ ПОЛЬЗОВАТЕЛЯ
     */
    private void changeUser(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/intro/login.fxml"));
            Parent parent = loader.load();
            new Decoration("Вход пользователя",
                    parent,
                    false,
                    owner,
                    "decoration-login",
                    false,
                    true);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * СМЕНИТЬ СЕРВЕР БАЗ ДАННЫХ
     */
    private void changeServer() {
        NoConnection window = new NoConnection(new Stage());
        boolean r = window.create();
        if (!r)
            window.close(); //Окно просто закрыли
        else
            try { //Нажали кнопку
                RetrofitClient.getInstance();
                if (!RetrofitClient.checkUpConnection()) {
                    window.close();
                    changeServer();
                } else {

                    window.close();
                }
            } catch (Exception e) {
                changeServer();
            }
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

        //СОХРАНИТЬ КАК
        iconMenuController.getBtnSave().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/save.png")), 32, 32, true, true)));
        iconMenuController.getBtnSave().setTooltip(new Tooltip("Сохранить"));
        iconMenuController.getBtnSave().setOnAction(e -> save(opData, addedOperations, e));

        //ОТКРЫТЬ
        iconMenuController.getBtnOpen().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/open.png")), 32, 32, true, true)));
        iconMenuController.getBtnOpen().setTooltip(new Tooltip("Открыть"));
        iconMenuController.getBtnOpen().setOnAction(e -> open(e, EMenuSource.ICON_MENU));

        //ОЧИСТИТЬ
        iconMenuController.getBtnClearAll().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/erase.png")), 32, 32, true, true)));
        iconMenuController.getBtnClearAll().setTooltip(new Tooltip("Очистить"));
        iconMenuController.getBtnClearAll().setOnAction(e -> clearAll(e, true, true));

        //ОТЧЕТ
        iconMenuController.getBtnReport1C().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/report.png")), 32, 32, true, true)));
        iconMenuController.getBtnReport1C().setTooltip(new Tooltip("Отчет"));
        iconMenuController.getBtnReport1C().setOnAction(e -> report(e, EMenuSource.ICON_MENU));

        //ПОКРЫТИЕ
        iconMenuController.getBtnColors().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/colors.png")), 32, 32, true, true)));
        iconMenuController.getBtnColors().setTooltip(new Tooltip("Покрытие"));
        iconMenuController.getBtnColors().setOnAction(e -> colors(e, EMenuSource.ICON_MENU));

        //РАСЧЕТНЫЕ КОНСТАНТЫ
        iconMenuController.getBtnConstants().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/constants.png")), 32, 32, true, true)));
        iconMenuController.getBtnConstants().setTooltip(new Tooltip("Расчетные константы"));
        iconMenuController.getBtnConstants().setOnAction(e -> constants(e, EMenuSource.ICON_MENU));

        //МАТЕРИАЛЫ
        iconMenuController.getBtnMaterials().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 32, 32, true, true)));
        iconMenuController.getBtnMaterials().setTooltip(new Tooltip("Материалы"));
        iconMenuController.getBtnMaterials().setOnAction(e -> materials(e, EMenuSource.ICON_MENU));

        //ПРОЧИЕ ОПЕРАЦИИ
        iconMenuController.getBtnOperations().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/operations.png")), 32, 32, true, true)));
        iconMenuController.getBtnOperations().setTooltip(new Tooltip("Свои операции"));
        iconMenuController.getBtnOperations().setOnAction(e -> operations(e, EMenuSource.ICON_MENU));

        //ИМПОРТ EXCEL
        iconMenuController.getBtnImportExcel().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/excel.png")), 32, 32, true, true)));
        iconMenuController.getBtnImportExcel().setTooltip(new Tooltip("Импорт Excel"));
        iconMenuController.getBtnImportExcel().setOnAction(e -> importExcel(e, EMenuSource.ICON_MENU));

        //ОТЧЕТ
        iconMenuController.getBtnProductTree().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/tree_view.png")), 32, 32, true, true)));
        iconMenuController.getBtnProductTree().setTooltip(new Tooltip("Схема изделия"));
        iconMenuController.getBtnProductTree().setOnAction(e -> productTree(e, EMenuSource.ICON_MENU));

        //ПОИСК
        iconMenuController.getBtnSearch().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/search.png")), 32, 32, true, true)));
        iconMenuController.getBtnSearch().setTooltip(new Tooltip("Поиск"));
        iconMenuController.getBtnSearch().setOnAction(e -> search(e, EMenuSource.ICON_MENU));
    }

    private void initViews() {

        MEASURE.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));

            CURRENT_MEASURE = ETimeMeasurement.findValueOf(AppStatics.MEASURE.getSelectedToggle().getUserData().toString());
            lblTimeMeasure.setText(ETimeMeasurement.findValueOf(newValue.getUserData().toString()).getMeasure());
            //Очистить все
            addedPlates.clear();
            addedOperations.clear();
            getListViewTechOperations().getItems().clear();
            finalCountSumNormTimeByShops();
            PlateDetailController.nameIndex = 0;
            PlateAssmController.nameIndex = 0;

            fillOpData();
            finalCountSumNormTimeByShops();
        });

        //Единицы измерения
        mainMenuController.getRbmSeconds().setToggleGroup(MEASURE);
        mainMenuController.getRbmSeconds().setUserData(SEC.name());
        mainMenuController.getRbmMinutes().setToggleGroup(MEASURE);
        mainMenuController.getRbmMinutes().setUserData(MIN.name());
        mainMenuController.getRbmHours().setToggleGroup(MEASURE);
        mainMenuController.getRbmHours().setUserData(HOUR.name());
        mainMenuController.getRbmHours().setSelected(true);
        finalCountSumNormTimeByShops();


    }

    private void importExcel(ActionEvent e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы EXCEL (.xlsx)", "*.xlsx"));
        File initDir = new File(AppProperties.getInstance().getImportDir());
        chooser.setInitialDirectory(initDir.exists() ? initDir : new File("C:\\"));
        File file = chooser.showOpenDialog(owner);
        if (file == null) return;

        //Сохраняем последнюю директорию
        AppProperties.getInstance().setImportDirectory(file.getParent());

        File copied = AppFiles.getInstance().createTempCopyOfFile(file);
        clearAll(e, true, true);

        ImportExcelFileService service = new ImportExcelFileService(this, copied);
        service.setOnSucceeded(workerStateEvent -> {
            blockUndoListFlag = true;
            OpAssm newOpData = service.getValue();
            if (newOpData != null)
                opData = newOpData;
            else return;
            normalizeQuantity(newOpData, 1);
            createMenu();
            menu.addListOfOperations();
            finalCountSumNormTimeByShops();
            iterateUndoList();
        });
        service.start();

    }

    /**
     * Метод приводящий структуру изделия к виду, в котором количество входящих деталей и сборок указывается
     * не на ИЗДЕЛИЕ, а на СБОРКУ.
     * <p>
     * Происходит перебор входящих в сборку элементов и им присваивается количество, получаемое от деления
     * количества элементов в ИЗДЕЛИИ (q) на количество сборок в изделии(assmQuantity).
     * Если перебираемый элемент ДЕТАЛЬ, то происходит присвоение  нового количества, а если СБОРКА, то происходит
     * рекурсивный вызов с указанием нового количества этой СБОРКИ.
     * <p>
     * В финале: СБОРКЕ присваивается новое количество (newQuantity).
     *
     * @param assmOpData  - сборка
     * @param newQuantity - новое количество сборок в предыдущей сборке
     */
    void normalizeQuantity(OpAssm assmOpData, int newQuantity) {
        int assmQuantity = assmOpData.getQuantity(); //количество сборок в ИЗДЕЛИИ
        for (OpData opData : assmOpData.getOperations()) {
            if (opData instanceof IOpWithOperations) {
                int q = opData.getQuantity(); //количество элементов в ИЗДЕЛИИ
                if (opData instanceof OpAssm)
                    normalizeQuantity((OpAssm) opData, q);
                opData.setQuantity(q / assmQuantity);
            }
        }
        assmOpData.setQuantity(newQuantity);
    }

    /**
     * СОХРАНИТЬ ИЗДЕЛИЕ
     */
    public static void save(OpData opData, List<OpData> addedOperations, Event e) {
        if (savedProductFile == null) saveAs(
                opData,
                addedOperations,
                CURRENT_PRODUCT_NAME,
                e,
                EMenuSource.ICON_MENU);
        else loadProductToFile(savedProductFile, opData, addedOperations);
    }

    /**
     * Выбрать файл для сохранения изделия
     */
    public static void saveAs(OpData opData, List<OpData> addedOperations, String initialName, Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы норм времени (.nvr)", "*.nvr"));
        File initFile = new File(AppProperties.getInstance().getLastDir());
        chooser.setInitialDirectory(initFile.exists() ? initFile : new File("C:\\"));
        chooser.setInitialFileName(initialName);

        File file = chooser.showSaveDialog(owner);
        if (file != null) {
            loadProductToFile(file, opData, addedOperations);

            if (source.equals(EMenuSource.MAIN_MENU) || source.equals(EMenuSource.ICON_MENU)) {
                AppProperties.getInstance().setLastDir(file.getParent());
                savedProductFile = file;
                CURRENT_PRODUCT_NAME = file.getName().replace(".nvr", "");
                LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + CURRENT_PRODUCT_NAME);
            }
        }

    }

    /**
     * Загрузить изделие в файл
     */
    public static void loadProductToFile(File file, OpData opData, List<OpData> addedOperations) {

        ((IOpWithOperations) opData).setName(file.getName());
        AppProperties.getInstance().setLastDir(file.getParent());
        ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));

        Gson gson = new Gson();
        ProductSettings settings = new ProductSettings();
        settings.setBatchness(BATCHNESS.get());
        settings.setBatch(CURRENT_BATCH);
        settings.setColor1(new AppColor(COLOR_I.getName(), COLOR_I.getRal(), COLOR_I.getConsumption()));
        settings.setColor2(new AppColor(COLOR_II.getName(), COLOR_II.getRal(), COLOR_II.getConsumption()));
        settings.setColor3(new AppColor(COLOR_III.getName(), COLOR_III.getRal(), COLOR_III.getConsumption()));

        //Создаем строки для сохранения в коллекцию
        String savedOpType = opData.getOpType().name().replace("\"", "");
        String productSettings = gson.toJson(settings);
        String productData = gson.toJson(opData);

        SAVES_HISTORY.add(new SaveNormEntry(getCurrentTime(), CURRENT_USER));
        String savesHistory = gson.toJson(SAVES_HISTORY);

        List<String> product = Arrays.asList(
                savedOpType, //Тип нормы (ДЕТАЛЬ, СБОРКА)
                productSettings, //Настройки дщля расчета норм
                productData, //Файл с данными НОРМ
                savesHistory); //История сохранений

        saveTextToFile(product, file);

        Warning0.create("Внимание!", "Файл успешно сохранен!");

        HistoryFile.getInstance().addFileToHistory(file);

    }

    /**
     * ОТЧЕТ
     */
    private void report(Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();
        ((OpAssm) opData).setOperations(getAddedOperations());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/extra/report1C.fxml"));
            Parent report = loader.load();
            ReportController controller = loader.getController();
            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));
            controller.init((OpAssm) opData);

            new Decoration(
                    "ОТЧЕТ",
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
     * ДРЕВО
     */
    private void productTree(Event e, EMenuSource source) {
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();
        ((OpAssm) opData).setOperations(getAddedOperations());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/structure/structure.fxml"));
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
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();
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

            decoration.getImgCloseWindow().setOnMousePressed(ev -> {
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
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();
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
                (Stage) ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow() :
                (Stage) ((Node) e.getSource()).getScene().getWindow();

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
     *
     * @param product, List<String>
     * @param file,    File
     */
    private static void saveTextToFile(List<String> product, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file, "UTF-8");
            for (String line : product) {
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
        menu.getItems().addAll(menu.createItemWeldDifficulty());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAssmNuts());
        menu.getItems().addAll(menu.createItemAssmNutsMK());
        menu.getItems().addAll(menu.createItemAssmCuttings());
        menu.getItems().addAll(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(menu.createItemThermoInsulation());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if(simpleOperationsMenu != null) {
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(simpleOperationsMenu);
        }

        linkMenuToButton();

        return menu;

    }

    /**
     * Метод используется для пересчета и вывода норм времени в нижнюю строку программы
     * при открытии готовых норм
     */
    public void finalCountSumNormTimeByShops(){
        double mechanicalTime;
        double paintingTime;
        double assemblingTime;
        double packingTime;

        OpData opData = (OpData) TotalCounter.recountNormTimes((IOpWithOperations) this.opData, 1);

        mechanicalTime = opData.getMechTime();
        paintingTime = opData.getPaintTime();
        assemblingTime = opData.getAssmTime();
        packingTime = opData.getPackTime();

        fillNormsAndMeasurement(mechanicalTime, paintingTime, assemblingTime, packingTime);
    }


    /**
     * Метод вызывается из контроллеров плашек для суммирования норм времени на лету
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops() {

        OpData opData = TotalCounter.countSumNormTimeByShops((IOpWithOperations) getOpData(), null);

        fillNormsAndMeasurement( opData.getMechTime(), opData.getPaintTime(), opData.getAssmTime(), opData.getPackTime());

    }

    private void fillNormsAndMeasurement(double mechanicalTime, double paintingTime, double assemblingTime, double packingTime) {

        mechanicalTime = mechanicalTime * CURRENT_MEASURE.getRate();
        paintingTime = paintingTime * CURRENT_MEASURE.getRate();
        assemblingTime = assemblingTime * CURRENT_MEASURE.getRate();
        packingTime = packingTime * CURRENT_MEASURE.getRate();


        String format = DOUBLE_FORMAT;
        if (CURRENT_MEASURE.equals(ETimeMeasurement.SEC)) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime).trim());
        tfPaintingTime.setText(String.format(format, paintingTime).trim());
        tfAssemblingTime.setText(String.format(format, assemblingTime).trim());
        tfPackingTime.setText(String.format(format, packingTime).trim());

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime).trim());

        lblTimeMeasure.setText(CURRENT_MEASURE.getMeasure());
    }

    /**
     * Метод пересчитывает нормы для главного окна
     * И прописывает их по участкам
     */
    public void recountMainOpData() {
        TotalCounter.recountNormTimes((IOpWithOperations) opData, 1);
        finalCountSumNormTimeByShops();
    }



    /**
     * Метод пересчитывает нормы для главного окна
     * И прописывает их по участкам
     */
    public void recountPaintingMainOpData() {
        MAIN_OP_DATA.setPaintTime(0f);

        recountPainting(MAIN_OP_DATA, 1);
        finalCountSumNormTimeByShops();
    }

    /**
     * Метод пересчитывает нормы времени для указанной сборки
     *
     * @param assm,     IOpWithOperations - пересчитываемая сборка
     * @param quantity, количество сборок (изначально 1)
     */
    public IOpWithOperations recountPainting(IOpWithOperations assm, int quantity) {
        ((OpData) assm).setPaintTime(0f);
        List<OpData> ops = assm.getOperations();
        for (OpData op : ops) {
            if (op instanceof IOpWithOperations) {
                IOpWithOperations opWithOperations = recountPainting((IOpWithOperations) op, quantity * ((OpData) assm).getQuantity());
                ((OpData) assm).setPaintTime(((OpData) assm).getPaintTime() + ((OpData) opWithOperations).getPaintTime() * ((OpData) assm).getQuantity());
            } else {
                OpData opData = op.getOpType().getNormCounter().count(op);
                ((OpData) assm).setPaintTime(((OpData) assm).getPaintTime() + opData.getPaintTime() * ((OpData) assm).getQuantity());
            }
        }
        return assm;
    }

    @Override
    public void init(AbstractFormController controller, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /**
     * Перехватчик нажатых клавиш
     * При нажатии, клавиша сохраняется в CH_KEYS_NOW_PRESSED,
     * при отпускании, клавиша удаляется из CH_KEYS_NOW_PRESSED
     */
    private void createButtonInterceptor() {

        KEYS_NOW_PRESSED = new ArrayList<>();

        MAIN_STAGE.getScene().setOnKeyPressed((e) -> {
            KEYS_NOW_PRESSED.add(e.getCode());
        });

        MAIN_STAGE.getScene().setOnKeyReleased((e) -> {
            KEYS_NOW_PRESSED.remove(e.getCode());
        });
    }

    /**
     * Перестраивает listView, пересчитывает нормы времени и покраску
     * Вызывается из StructureController
     */
    public void rebuildAll() {

        Platform.runLater(() -> {
            //Создаем копию списка ops
            List<OpData> ops = new ArrayList<>(((IOpWithOperations) opData).getOperations());
            listViewTechOperations.getItems().clear();
            addedPlates.clear();
            addedOperations.clear();

            //Возвращаем список оперций
            if (((IOpWithOperations) opData).getOperations().isEmpty())
                ((IOpWithOperations) opData).setOperations(ops);
            menu.addListOfOperations();
            recountPainting(MAIN_OP_DATA, 1);
            finalCountSumNormTimeByShops();
        });

    }

//    public void recountTotals(IOpWithOperations opData, int total){
//        List<OpData> ops = opData.getOperations();
//        for(OpData op : ops){
//            if(op instanceof IOpWithOperations) {
//                int currentTotal = op.getQuantity() * total;
//                op.setTotal(currentTotal);
//                recountTotals((IOpWithOperations) op, currentTotal);
//            } else {
//                int currentTotal = op.getQuantity() * total;
//                op.setTotal(currentTotal);
//            }
//        }
//    }
}