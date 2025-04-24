package ru.wert.normic.controllers._forms.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.enums.ETimeMeasurement;

import java.io.IOException;
import java.util.function.Function;

import static ru.wert.normic.AppStatics.*;

public class MainMenuCreator {

    private final MainController controller;
    private MainMenuController menuController;

    public MainMenuCreator(MainController controller) {
        this.controller = controller;
        create();
    }

    public void create(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menus/mainMenu.fxml"));
            Parent menuBar = loader.load();
            menuController = loader.getController();
            controller.getSpMenuBar().getChildren().add(menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        menuController.getMSave().setOnAction(e -> MainController.save(controller.getOpData(), controller.getAddedOperations(), e));
        menuController.getMSaveAs().setOnAction(e -> MainController.saveAs(controller.getOpData(), controller.getAddedOperations(),
                CURRENT_PRODUCT_NAME,
                e, EMenuSource.MAIN_MENU));
        menuController.getMSavesHistory().setOnAction(e -> controller.openSavesHistory(e, EMenuSource.MAIN_MENU));
        menuController.getMOpen().setOnAction(e -> controller.open(e, EMenuSource.MAIN_MENU));
        //При нажатии на МЕНЮ готовится список последних открываемых файлов
        menuController.getMFile().setOnShowing(e -> controller.prepareRecentFiles(menuController.getMOpenRecent()));
        menuController.getMClearAll().setOnAction(e -> controller.clearAll(e, true, true));

        menuController.getChmBatchness().setSelected(BATCHNESS.get());
        menuController.getChmBatchness().setOnAction(e-> controller.batchness());
        menuController.getChmBatchness().selectedProperty().bindBidirectional(BATCHNESS);

        menuController.getMRapport1C().setOnAction(e -> controller.report(e, EMenuSource.MAIN_MENU));
        menuController.getMProductTree().setOnAction(e -> controller.productTree(e, EMenuSource.MAIN_MENU));
        menuController.getMTableView().setOnAction(e -> controller.openTableView(e, EMenuSource.MAIN_MENU));
        menuController.getMColors().setOnAction(e -> controller.colors(e, EMenuSource.MAIN_MENU));
        menuController.getMConstants().setOnAction(e -> controller.constants(e, EMenuSource.MAIN_MENU));
        menuController.getMOperations().setOnAction(e -> controller.operations(e, EMenuSource.MAIN_MENU));
        menuController.getMMaterials().setOnAction(e -> controller.materials(e, EMenuSource.MAIN_MENU));
        menuController.getMImportExcel().setOnAction(e -> controller.importExcel(e, EMenuSource.MAIN_MENU));
        menuController.getMChangeUser().setOnAction(e -> controller.changeUser(e, EMenuSource.MAIN_MENU));
        menuController.getMChangeServer().setOnAction(e -> controller.changeServer());
        menuController.getMIconMenu().setOnAction(e -> controller.showIconMenuProperty.set(!controller.getShowIconMenuProperty().get()));
        menuController.getMAbout().setOnAction(e -> controller.getVbAboutPane().setVisible(true));

        Function<Boolean, String> getLabel =
                type -> type
                        ? "Расчет электромонтажа"
                        : "Общий расчет";

        menuController.getMOperationsType().setText(getLabel.apply(USE_ELECTRICAL_MENUS));
        menuController.getMOperationsType().setOnAction(e->{
            USE_ELECTRICAL_MENUS = !USE_ELECTRICAL_MENUS;
            AppProperties.getInstance().setUseElectrical(String.valueOf(USE_ELECTRICAL_MENUS));
            MAIN_CONTROLLER.getImgLamp().setVisible(USE_ELECTRICAL_MENUS);
            menuController.getMOperationsType().setText(getLabel.apply(USE_ELECTRICAL_MENUS));
            controller.createMenu();
        });

        controller.showIconMenuProperty.addListener((observable, oldValue, newValue) -> {
            if (controller.showIconMenuProperty.get()) {
                menuController.getMIconMenu().setText("Скрыть панель управления");
                controller.getSpIconMenu().getChildren().clear();
                controller.getSpIconMenu().getChildren().add(controller.getIconBar());
            } else {
                menuController.getMIconMenu().setText("Показать панель управления");
                controller.getSpIconMenu().getChildren().clear();
            }
        });
    }

    public void initMenuMeasures(){
        menuController.getRbmSeconds().setToggleGroup(MEASURE);
        menuController.getRbmSeconds().setUserData(ETimeMeasurement.SEC.name());
        menuController.getRbmMinutes().setToggleGroup(MEASURE);
        menuController.getRbmMinutes().setUserData(ETimeMeasurement.MIN.name());
        menuController.getRbmHours().setToggleGroup(MEASURE);
        menuController.getRbmHours().setUserData(ETimeMeasurement.HOUR.name());
        menuController.getRbmHours().setSelected(true);
    }

}
