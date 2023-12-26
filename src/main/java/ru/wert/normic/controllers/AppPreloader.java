package ru.wert.normic.controllers;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import ru.wert.normic.controllers.intro.NoConnection;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;

import java.util.List;

public class AppPreloader extends Preloader {

    private Stage preloaderWindow = null;

    @SneakyThrows
    @Override
    public void start(Stage preloaderWindow){
        this.preloaderWindow = preloaderWindow;
        try {
            RetrofitClient.getInstance();
            if(!RetrofitClient.checkUpConnection())
                showSetConnectionDialog(preloaderWindow);
        } catch (Exception e) {
            showSetConnectionDialog(preloaderWindow);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/intro/preloader.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        preloaderWindow.setScene(scene);

        if(!preloaderWindow.isShowing())
            preloaderWindow.initStyle(StageStyle.TRANSPARENT);
        preloaderWindow.show();
        centerWindow();

    }

    private void centerWindow(){
        List<Screen> screenList = Screen.getScreens();
        double screenMinX = screenList.get(0).getBounds().getMinX(); //0 - основной монитор
        double screenMinY = screenList.get(0).getBounds().getMinY();
        double screenWidth = screenList.get(0).getBounds().getWidth();
        double screenHeight = screenList.get(0).getBounds().getHeight();

        preloaderWindow.setX(screenMinX + ((screenWidth - preloaderWindow.getWidth()) / 2));
        preloaderWindow.setY(screenMinY + ((screenHeight - preloaderWindow.getHeight()) / 2));
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Preloader.StateChangeNotification.Type.BEFORE_START){

            preloaderWindow.hide();
        }
    }

    private void showSetConnectionDialog(Stage preloaderWindow) {
        boolean r = new NoConnection(preloaderWindow).create();
        preloaderWindow.hide();
        if (r) start(preloaderWindow);
        else System.exit(0);
    }


}
