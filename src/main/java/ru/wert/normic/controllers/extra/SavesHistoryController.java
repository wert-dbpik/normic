package ru.wert.normic.controllers.extra;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.wert.normic.entities.saves.SaveNormEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static ru.wert.normic.AppStatics.SAVES_HISTORY;

public class SavesHistoryController {

    @FXML
    private TableColumn<SaveNormEntry, String> tcDateSave;

    @FXML
    private TableColumn<SaveNormEntry, String> tcUser;

    @FXML
    private TableView<SaveNormEntry> tableView;

    @FXML
    void initialize() {

        tcDateSave.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getSaveTime()));
        tcDateSave.setStyle("-fx-alignment: CENTER;");

        tcUser.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getUser().getName()));
        tcUser.setStyle("-fx-alignment: CENTER;");

        ObservableList<SaveNormEntry> data = FXCollections.observableArrayList(SAVES_HISTORY);
        tableView.setItems(data);

    }


    /**
     * Возвращает текущее время с миллисекундами
     */
    public static String getCurrentTime(){
        Date date = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
        return df.format(date);
    }

}
