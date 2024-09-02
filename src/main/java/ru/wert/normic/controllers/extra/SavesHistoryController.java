package ru.wert.normic.controllers.extra;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.wert.normic.entities.db_connection.user.User;
import ru.wert.normic.entities.saves.SavesHistory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ru.wert.normic.AppStatics.CURRENT_USER;

public class SavesHistoryController {

    @FXML
    private TableColumn<SavesHistory, String> tcDateSave;

    @FXML
    private TableColumn<SavesHistory, String> tcUser;

    @FXML
    private TableView<SavesHistory> tableView;

    @FXML
    void initialize() {

        tcDateSave.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getSaveTime()));
        tcDateSave.setStyle("-fx-alignment: CENTER;");

        tcUser.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getUser().getName()));
        tcUser.setStyle("-fx-alignment: CENTER;");

        ObservableList<SavesHistory> list = FXCollections.observableArrayList(getHistory());
        tableView.setItems(list);

    }

    private List<SavesHistory> getHistory(){
        List<SavesHistory> historyList = new ArrayList<>();

        String d1 = getCurrentTime();
        historyList.add(new SavesHistory(d1, CURRENT_USER));

        String d2 = getCurrentTime();
        historyList.add(new SavesHistory(d2, CURRENT_USER));

        String d3 = getCurrentTime();
        historyList.add(new SavesHistory(d3, CURRENT_USER));

        return historyList;
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
