package ru.wert.normic.decoration;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class DecorationStatic {

    public static Stage MAIN_STAGE;
    public static Label LABEL_PRODUCT_NAME;
    public static String TITLE_SEPARATOR = " : ";

    /**
     * Метод обеспечивает закрытие любого окна
     * В случае, если это окно главного приложения, то осуществляется выход из приложения
     */
    public static void closeWindow(Event event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        if (window.equals(DecorationStatic.MAIN_STAGE)) {
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

    /**
     * ЦЕНТРИРОВАНИЕ ОКНА ОТНОСИТЕЛЬНО ЭКРАНА, КОТОРОЕ РАБОТАЕТ ПРИ СТАРТЕ ПРОГРАММЫ
     */
    public static void centerWindowRelativeToScreen(Stage window, Boolean fullScreen, int mainMonitor, Stage owner){

        List<Screen> screenList = Screen.getScreens();
        //Если всего один монитор, то открываем на нем
        int monitor = Math.min(mainMonitor, screenList.size() - 1);

        if (fullScreen) {
            window.setWidth(screenList.get(monitor).getVisualBounds().getWidth());
            window.setHeight(screenList.get(monitor).getVisualBounds().getHeight());
            window.setX(screenList.get(monitor).getVisualBounds().getMinX());
            window.setY(screenList.get(monitor).getVisualBounds().getMinY());

        } else if (owner == null) { //Окно основное + поиск

            double screenMinX = screenList.get(monitor).getVisualBounds().getMinX();
            double screenMinY = screenList.get(monitor).getVisualBounds().getMinY();
            double screenWidth = screenList.get(monitor).getVisualBounds().getWidth();
            double screenHeight = screenList.get(monitor).getVisualBounds().getHeight();

            window.setX(screenMinX + ((screenWidth - window.getWidth()) / 2));
            window.setY(screenMinY + ((screenHeight - window.getHeight()) / 2));

        } else { //Дополнительные окна

            double mainX = owner.getX();
            double mainY = owner.getY();
            double mainWidth = owner.getWidth();
            double mainHeight = owner.getHeight();
            double winWidth = window.getWidth();
            double winHeight = window.getHeight();

            window.setX(mainX + ((mainWidth - winWidth) / 2));
            window.setY(mainY + ((mainHeight - winHeight) / 2));
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
            if(MAIN_STAGE.isFullScreen()){
                centerWindowRelativeToScreen(window, false, monitor, owner);
            } else {
                if(owner != null) {
                    window.setX(owner.getX() + 40);
                    window.setY(owner.getY() + 40);
                } else
                    centerWindowRelativeToScreen(window, false, monitor, owner);
            }
        }
    }
}
