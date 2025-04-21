package ru.wert.normic.controllers._forms.main;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.wert.normic.enums.EMenuSource;

import java.io.IOException;

public class IconMenuCreator {
    private final MainController controller;
    private IconMenuController iconController;

    public IconMenuCreator(MainController controller) {
        this.controller = controller;
        create();
    }

    public void create() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menus/iconMenu.fxml"));
            controller.setIconBar(loader.load());
            iconController = loader.getController();
            controller.getShowIconMenuProperty().set(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //СОХРАНИТЬ КАК
        iconController.getBtnSave().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/save.png")), 32, 32, true, true)));
        iconController.getBtnSave().setTooltip(new Tooltip("Сохранить"));
        iconController.getBtnSave().setOnAction(e -> MainController.save(controller.getOpData(), controller.getAddedOperations(), e));

        //ОТКРЫТЬ
        iconController.getBtnOpen().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/open.png")), 32, 32, true, true)));
        iconController.getBtnOpen().setTooltip(new Tooltip("Открыть"));
        iconController.getBtnOpen().setOnAction(e -> controller.open(e, EMenuSource.ICON_MENU));

        //ОЧИСТИТЬ
        iconController.getBtnClearAll().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/erase.png")), 32, 32, true, true)));
        iconController.getBtnClearAll().setTooltip(new Tooltip("Очистить"));
        iconController.getBtnClearAll().setOnAction(e -> controller.clearAll(e, true, true));

        //ОТЧЕТ
        iconController.getBtnReport1C().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/report.png")), 32, 32, true, true)));
        iconController.getBtnReport1C().setTooltip(new Tooltip("Отчет"));
        iconController.getBtnReport1C().setOnAction(e -> controller.report(e, EMenuSource.ICON_MENU));

        //ПОКРЫТИЕ
        iconController.getBtnColors().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/colors.png")), 32, 32, true, true)));
        iconController.getBtnColors().setTooltip(new Tooltip("Покрытие"));
        iconController.getBtnColors().setOnAction(e -> controller.colors(e, EMenuSource.ICON_MENU));

        //РАСЧЕТНЫЕ КОНСТАНТЫ
        iconController.getBtnConstants().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/constants.png")), 32, 32, true, true)));
        iconController.getBtnConstants().setTooltip(new Tooltip("Расчетные константы"));
        iconController.getBtnConstants().setOnAction(e -> controller.constants(e, EMenuSource.ICON_MENU));

        //МАТЕРИАЛЫ
        iconController.getBtnMaterials().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 32, 32, true, true)));
        iconController.getBtnMaterials().setTooltip(new Tooltip("Материалы"));
        iconController.getBtnMaterials().setOnAction(e -> controller.materials(e, EMenuSource.ICON_MENU));

        //ПРОЧИЕ ОПЕРАЦИИ
        iconController.getBtnOperations().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/operations.png")), 32, 32, true, true)));
        iconController.getBtnOperations().setTooltip(new Tooltip("Свои операции"));
        iconController.getBtnOperations().setOnAction(e -> controller.operations(e, EMenuSource.ICON_MENU));

        //ИМПОРТ EXCEL
        iconController.getBtnImportExcel().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/excel.png")), 32, 32, true, true)));
        iconController.getBtnImportExcel().setTooltip(new Tooltip("Импорт Excel"));
        iconController.getBtnImportExcel().setOnAction(e -> controller.importExcel(e, EMenuSource.ICON_MENU));

        //ОТЧЕТ
        iconController.getBtnProductTree().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/tree_view.png")), 32, 32, true, true)));
        iconController.getBtnProductTree().setTooltip(new Tooltip("Схема изделия"));
        iconController.getBtnProductTree().setOnAction(e -> controller.productTree(e, EMenuSource.ICON_MENU));

        //ПОИСК
        iconController.getBtnSearch().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/search.png")), 32, 32, true, true)));
        iconController.getBtnSearch().setTooltip(new Tooltip("Поиск"));
        iconController.getBtnSearch().setOnAction(e -> controller.search(e, EMenuSource.ICON_MENU));
    }
}
