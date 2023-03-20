package ru.wert.normic.menus;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.ImageView;
import lombok.Getter;
import ru.wert.normic.controllers._forms.MainController;

import static ru.wert.normic.utils.BtnImages.IMG_OPEN;

public class MainMenuController {

    //ФАЙЛ
    @FXML@Getter
    private MenuItem mOpen, mSaveAs, mImportExcel, mExit;
    //ПРАВКА
    @FXML@Getter
    private MenuItem mClearAll;
    //НАСТРОЙКА
    @FXML@Getter
    private MenuItem mColors, mMaterials, mConstants;

    @FXML@Getter
    private RadioMenuItem rbmSeconds, rbmMinutes;
    //ОТЧЕТЫ
    @FXML@Getter
    private MenuItem mRapport1C;
    //ПОМОЩЬ
    @FXML@Getter
    private MenuItem mAbout;


    public void init(MainController controller){
//        mOpen.setGraphic(new ImageView(IMG_OPEN));

    }
}
