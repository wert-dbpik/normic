package ru.wert.normic;

import javafx.scene.control.ComboBox;
import ru.wert.normic.controllers.forms.MainController;
import ru.wert.normic.enums.ETimeMeasurement;

public class AppStatics {

    public static MainController MAIN_CONTROLLER;

    //Текущая версия программы
    public static final String PROJECT_VERSION = "1.0";

    //Комбобокс, где переключается единица измерения (мин, сек)
    public static ComboBox<ETimeMeasurement> MEASURE;

    //Используемая схема CSS
    public static String THEME_STYLE = "/css/calculator-sandy.css";
}
