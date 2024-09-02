package ru.wert.normic.menus;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import lombok.Getter;

public class MainMenuController {

    //ФАЙЛ ==============================================

    @FXML@Getter
    private MenuItem
            mOpen,
            mSave,
            mSaveAs,
            mSavesHistory,
            mImportExcel,
            mChangeUser,
            mExit;

    @FXML@Getter
    private Menu
            mFile,        //Файл
            mOpenRecent;  //Открыть последнее

    //ПРАВКА ============================================

    @FXML @Getter
    private MenuItem
            mClearAll;

    @FXML @Getter
    private CheckMenuItem
            chmBatchness;

    //НАСТРОЙКА =========================================

    @FXML@Getter
    private MenuItem
            mColors,
            mMaterials,
            mOperations,
            mConstants,
            mIconMenu;

    @FXML@Getter
    private RadioMenuItem
            rbmSeconds,
            rbmMinutes,
            rbmHours;

    //ОТЧЕТЫ============================================

    @FXML@Getter
    private MenuItem
            mRapport1C,
            mProductTree;

    //ПОМОЩЬ=============================================

    @FXML@Getter
    private MenuItem
            mAbout;

}
