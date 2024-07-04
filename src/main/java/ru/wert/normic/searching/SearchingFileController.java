package ru.wert.normic.searching;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Getter;
import ru.wert.normic.AppStatics;
import ru.wert.normic.components.BtnDouble;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuForm;

import java.io.File;

public class SearchingFileController extends AbstractFormController {

    @FXML
    @Getter
    private HBox progressIndicator;

    @FXML
    private TextField tfSearchText;

    @FXML
    private Button btnSearchNow;

    @FXML
    private Button btnShowResult;
    private BtnDouble showResult;

    @FXML
    private TextField tfWhereSearch;

    @FXML
    private Button btnWhereSearch;

    @FXML
    private ListView<VBox> listViewFoundOperations;

    private boolean allTextIsSelected;

    public void init() {
        progressIndicator.setVisible(false);

        createTfSearchText();
        createBtnSearchNow();
        createTfAndBtnWhereSearch();
        createBtnShowNVRFiles();

        opData = new OpAssm();
        menu = new MenuForm(this, listViewFoundOperations, (IOpWithOperations) opData);

        listViewFoundOperations.setStyle("-fx-border-style: solid; -fx-border-width: 1; -fx-border-color: #6e4909;" );

        setDragAndDropCellFactory();
    }

    private void createBtnShowNVRFiles() {
        Image imgClockOFF =  new Image("/pics/btns/show_norm.png", 32, 32, true, true);
        Image imgClockONN =  new Image("/pics/btns/show_nvr.png", 32, 32, true, true);

        showResult = new BtnDouble(btnShowResult,
                imgClockOFF, "Показывает норму",
                imgClockONN, "Показывает входимость нормы");

        showResult.getStateProperty().addListener((observable, oldValue, newValue) -> {
            searchNow();
        });
    }


    private void createTfSearchText() {
        tfSearchText.setStyle("-fx-font-size: 16;");

        //При нажатии на ентер происходит принудительный поиск
        tfSearchText.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                searchNow();
                listViewFoundOperations.requestFocus();
            }
        });

        //Управляет выделением строки поиска при клике на поле
        tfSearchText.setOnMouseClicked(e -> {
            tfSearchText.requestFocus();
            if (tfSearchText.getSelection().getLength() == 0) {
                if (allTextIsSelected) {
                    tfSearchText.deselect();
                    allTextIsSelected = false;
                } else {
                    tfSearchText.selectAll();
                    allTextIsSelected = true;
                }
            }
        });

        //При потере фокуса выделение с поля поиска снимается
        tfSearchText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                tfSearchText.deselect();
                allTextIsSelected = false;
            }
        });
    }

    /**
     * ИСКАТЬ
     */
    private void createBtnSearchNow() {
        btnSearchNow.setTooltip(new Tooltip("Искать!"));
        btnSearchNow.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/search.png")), 56, 56, true, true)));
        btnSearchNow.setStyle("-fx-border-color: #6e4909; -fx-border-style: solid; -fx-border-radius: 5;-fx-border-width: 2;");
        btnSearchNow.setOnAction(e -> {
            searchNow();
        });
    }

    private void searchNow() {
        String searchText = normalizeSearchedText(tfSearchText.getText());
        if(searchText.isEmpty()) return;
        clearAll(null, false);
        SearchService searchService = new SearchService(this, (OpAssm) opData, searchText, showResult.getStateProperty().get());
        searchService.start();
    }


    /**
     * Позволяет опускать точку при поиске,
     * вставлять строку с номером с пробелами из 1С
     * Если набранных символов меньше равно 6, то ничего не делает
     */
    private String normalizeSearchedText(String text) {
        String newText = text.replaceAll("\\s+", "");
        if (newText.matches("[a-zA-ZА-яа-я-]+"))
            return text;

        if (newText.length() <= 6)
            return newText;

        else if (newText.length() < 10) {
            if (newText.charAt(6) == '/' && newText.substring(0, 5).matches("[0-9]+")) {
                newText = newText.replaceAll("/", ".");
            }
            if (newText.charAt(6) == '.') {
                return newText;
            }
            StringBuilder sbText = new StringBuilder(newText);
            return
                    sbText.insert(6, ".").toString();
        }

        StringBuilder sbText = new StringBuilder(newText.replaceAll("[^\\d]", ""));
        if (sbText.length() >= 9) {
            sbText.delete(9, sbText.length());
            sbText.insert(6, ".");
            return sbText.toString();
        } else
            return newText;
    }

    /**
     * ГДЕ ИСКАТЬ
     */
    private void createTfAndBtnWhereSearch() {
        tfWhereSearch.setText(AppProperties.getInstance().getWhereToSearch());

        btnWhereSearch.setTooltip(new Tooltip("Выбрать директорию"));
        btnWhereSearch.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/folder2.png")), 32, 32, true, true)));
        btnWhereSearch.setOnAction(e -> {
            Stage owner =
                    (Stage) ((Node) e.getSource()).getScene().getWindow();

            DirectoryChooser chooser = new DirectoryChooser();
            File initDir = new File(AppProperties.getInstance().getWhereToSearch());
            chooser.setInitialDirectory(initDir.exists() ? initDir : new File("C:/"));
            File file = chooser.showDialog(owner);
            if (file == null) return;
            AppProperties.getInstance().setWhereToSearch(file.getAbsolutePath());
            Platform.runLater(() -> {
                tfWhereSearch.setText(AppProperties.getInstance().getWhereToSearch());
            });
        });
    }

    @Override
    public void countSumNormTimeByShops() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public MenuForm createMenu() {
        return menu;
    }

    @Override
    public void fillOpData() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override
    public ListView<VBox> getListViewTechOperations() {
        return listViewFoundOperations;
    }

    @Override
    public Button getBtnAddOperation() {
        return null;
    }

    @Override
    public void init(AbstractFormController controller, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }
}
