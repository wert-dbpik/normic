package ru.wert.normic.decoration;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class DecorationStatic {

    public static Stage WF_MAIN_STAGE;

    /**
     * Метод обеспечивает закрытие любого окна
     * В случае, если это окно главного приложения, то осуществляется выход из приложения
     */
    public static void closeWindow(Event event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        if (window.equals(DecorationStatic.WF_MAIN_STAGE)) {
            exitApplication(event);
        } else
            window.hide();
    }

    /**
     * Метод обеспечивает выход из приложения
     */
    public static void exitApplication(Event event){

        System.exit(0);

    }


    public static void centerWindow(Stage window, Boolean fullScreen, int mainMonitor){

        List<Screen> screenList = Screen.getScreens();
        //Если всего один монитор, то открываем на нем
        int monitor = Math.min(mainMonitor, screenList.size() - 1);

        if(fullScreen) {
            window.setWidth(screenList.get(monitor).getVisualBounds().getWidth());
            window.setHeight(screenList.get(monitor).getVisualBounds().getHeight());
            window.setX(screenList.get(monitor).getVisualBounds().getMinX());
            window.setY(screenList.get(monitor).getVisualBounds().getMinY());
        } else {
            double screenMinX = screenList.get(monitor).getVisualBounds().getMinX();
            double screenMinY = screenList.get(monitor).getVisualBounds().getMinY();
            double screenWidth = screenList.get(monitor).getVisualBounds().getWidth();
            double screenHeight = screenList.get(monitor).getVisualBounds().getHeight();

            window.setX(screenMinX + ((screenWidth - window.getWidth()) / 2));
            window.setY(screenMinY + ((screenHeight - window.getHeight()) / 2));
        }

    }

    public static void centerShiftedWindow(Stage window, Boolean fullScreen, int mainMonitor, Stage owner){

        List<Screen> screenList = Screen.getScreens();
        //Если всего один монитор, то открываем на нем
        int monitor = Math.min(mainMonitor, screenList.size() - 1);

        if(fullScreen) {
            window.setWidth(screenList.get(monitor).getVisualBounds().getWidth());
            window.setHeight(screenList.get(monitor).getVisualBounds().getHeight());
            window.setX(screenList.get(monitor).getVisualBounds().getMinX());
            window.setY(screenList.get(monitor).getVisualBounds().getMinY());
        } else {
            window.setX(owner.getX() + 40);
            window.setY(owner.getY() + 40);
        }

    }

}
