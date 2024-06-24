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
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.NvrConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    private String searchText;

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
            searchText = normalizeSearchedText(btnSearchNow.getText());
            List<String> foundNVRFiles = collectFoundNVRFiles();
            for(String path : foundNVRFiles){
                OpData opData = new NvrConverter(new File(path)).getConvertedOpData();

            }
            System.out.println(collectFoundNVRFiles());
        });
    }

    /**
     * В директории поиска собирает все файлы с расширением .nvr, содержащие строку
     */
    private List<String> collectFoundNVRFiles(){
        List<String> foundNVRFiles = new ArrayList<>();
        try{
            Stream<Path> stream = Files.walk(Paths.get(AppProperties.getInstance().getWhereToSearch()));
            Set<String> allFiles = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".nvr"))
                    .collect(Collectors.toSet());
            stream.close();

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
     * Позволяет опускать точку при поиске,
     * вставлять строку с номером с пробелами из 1С
     * Если набранных символов меньше равно 6, то ничего не делает
     */
    private String normalizeSearchedText(String text){
        String newText = text.replaceAll("\\s+", "");
        if(newText.matches("[a-zA-ZА-яа-я-]+"))
            return text;

        if(newText.length() <= 6)
            return newText;

        else if(newText.length() < 10){
            if(newText.charAt(6) == '/' && newText.substring(0, 5).matches("[0-9]+")) {
                newText = newText.replaceAll("/", ".");
            }
            if(newText.charAt(6) == '.'){
                return newText;
            }
            StringBuilder sbText = new StringBuilder(newText);
            return
                    sbText.insert(6, ".").toString();
        }

        StringBuilder sbText = new StringBuilder(newText.replaceAll("[^\\d]", ""));
        if(sbText.length() >= 9){
            sbText.delete(9, sbText.length());
            sbText.insert(6, ".");
            return sbText.toString();
        } else
            return newText;
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
