package ru.wert.normic.menus;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import ru.wert.normic.controllers._forms.MainController;

import static ru.wert.normic.utils.BtnImages.IMG_OPEN;

public class MainMenuController {

    //ФАЙЛ
    @FXML
    private MenuItem mOpen, mSaveAs, mImportExcel, mExit;
    //ПРАВКА
    @FXML
    private MenuItem mClearAll;
    //НАСТРОЙКА
    @FXML
    private MenuItem mColors, mMaterials;
    //ОТЧЕТЫ
    @FXML
    private MenuItem mRapport1C;
    //ПОМОЩЬ
    @FXML
    private MenuItem mAbout;


    public void init(MainController controller){
//        mOpen.setGraphic(new ImageView(IMG_OPEN));

    }
}
