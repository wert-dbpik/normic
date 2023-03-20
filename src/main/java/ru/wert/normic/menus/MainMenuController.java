package ru.wert.normic.menus;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import lombok.Getter;

public class MainMenuController {

    //ФАЙЛ ==============================================

    @FXML@Getter
    private MenuItem
            mOpen,
            mSaveAs,
            mImportExcel,
            mExit;

    //ПРАВКА ============================================

    @FXML@Getter
    private MenuItem
            mClearAll;

    //НАСТРОЙКА =========================================

    @FXML@Getter
    private MenuItem
            mColors,
            mMaterials,
            mConstants,
            mIconMenu;

    @FXML@Getter
    private RadioMenuItem
            rbmSeconds,
            rbmMinutes;

    //ОТЧЕТЫ============================================

    @FXML@Getter
    private MenuItem
            mRapport1C;

    //ПОМОЩЬ=============================================

    @FXML@Getter
    private MenuItem
            mAbout;

}
