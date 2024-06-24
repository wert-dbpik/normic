package ru.wert.normic.searching;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.util.IOUtils;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ListView<VBox> listViewFoundOperations;

    private List<OpData> addedOperations;

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
            System.out.println(collectFoundNVRFiles());
        });
    }

    private List<String> collectFoundNVRFiles(){
        List<String> foundNVRFiles = new ArrayList<>();
        try{
            Set<String> allFiles = collectAllNVRFiles(AppProperties.getInstance().getWhereToSearch());
            for(String path : allFiles){
                String content = new String(Files.readAllBytes(Paths.get(path)));
                if(content.contains(tfSearchText.getText()))
                    foundNVRFiles.add(path);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return foundNVRFiles;
    }

    /**
     * В директории поиска собирает все файлы с расширением .nvr
     */
    private Set<String> collectAllNVRFiles(String dir) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".nvr"))
                    .collect(Collectors.toSet());
        }
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
