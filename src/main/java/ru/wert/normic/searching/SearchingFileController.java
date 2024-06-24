package ru.wert.normic.searching;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;

import java.io.File;

public class SearchingFileController {

    @FXML
    private TextField tfSearchText;

    @FXML
    private Button btnSearchNow;

    @FXML
    private TextField tfWhereSearch;

    @FXML
    private Button btnWhereSearch;

    @FXML
    private ListView<?> listViewFoundOperations;

    public void init(){
        createBtnSearchNow();
        createTfAndBtnWhereSearch();
    }

    /**
     * ИСКАТЬ
     */
    private void createBtnSearchNow(){
        btnSearchNow.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/search.png")), 32,32, true, true)));
        btnSearchNow.setOnAction(e->{

        });
    }

    /**
     * ГДЕ ИСКАТЬ
     */
    private void createTfAndBtnWhereSearch(){
        tfWhereSearch.setText(AppProperties.getInstance().getWhereToSearch());
        btnWhereSearch.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/folder2.png")), 32,32, true, true)));
        btnWhereSearch.setOnAction(e->{
            Stage owner =
                    (Stage) ((Node)e.getSource()).getScene().getWindow();

            DirectoryChooser chooser = new DirectoryChooser();
            File initDir = new File(AppProperties.getInstance().getWhereToSearch());
            chooser.setInitialDirectory(initDir.exists() ? initDir : new File("C:/"));
            File file = chooser.showDialog(owner);
            if(file == null) return;
            AppProperties.getInstance().setWhereToSearch(file.getAbsolutePath());
            Platform.runLater(()->{
                tfWhereSearch.setText(AppProperties.getInstance().getWhereToSearch());
            });
        });
    }

}
