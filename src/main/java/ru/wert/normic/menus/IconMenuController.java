package ru.wert.normic.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.Getter;

public class IconMenuController {

    //ФАЙЛ
    @FXML@Getter
    private Button
            btnOpen,
            btnSave,
            btnImportExcel;

    //ПРАВКА
    @FXML@Getter
    private Button
            btnClearAll;

    //НАСТРОЙКА
    @FXML@Getter
    private Button
            btnColors,
            btnMaterials,
            btnConstants;

    //ОТЧЕТЫ
    @FXML@Getter
    private Button
            btnReport1C,
            btnProductTree;

    //ПОИСК
    @FXML@Getter
    private Button
            btnSearch;

}
