package ru.wert.normic.decoration;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

import static java.lang.String.format;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class ModalWindow {

    protected static double dragOffsetX;
    protected static double dragOffsetY;


// =======================   ПЕРЕМЕЩАЕМ ОКНО  ========================================

    protected static void onMousePressed(MouseEvent mouseEvent){
        Stage window = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        dragOffsetX = mouseEvent.getScreenX() - window.getX();
        dragOffsetY = mouseEvent.getScreenY() - window.getY();
    }

    protected static void onMouseDragged(MouseEvent mouseEvent) {
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setX(mouseEvent.getScreenX() - dragOffsetX);
        window.setY(mouseEvent.getScreenY() - dragOffsetY);
    }
//====================================================================================

    protected static void setMovingPane(Parent parent) {
        AnchorPane anchorPane = (AnchorPane)parent.lookup("#movingPane");
        anchorPane.setOnMousePressed((ModalWindow::onMousePressed));
        anchorPane.setOnMouseDragged((ModalWindow::onMouseDragged));
    }



    /**
     * Метод центрирует модальное окно относительно главного окна приложения
     * @param stage сцена модального окна
     */
    public static void centerModalWindowRelativeToScreen(Stage stage, MouseEvent event){
        int monitor;
        if (event != null) {

            monitor = findCurrentMonitorByMousePointer(event);
        } else {
            monitor = findCurrentMonitorByMainStage(MAIN_STAGE);
        }

        List<Screen> screenList = Screen.getScreens();
        double screenMinX = screenList.get(monitor).getBounds().getMinX();
        double screenMinY = screenList.get(monitor).getBounds().getMinY();
        double screenWidth = screenList.get(monitor).getBounds().getWidth();
        double screenHeight = screenList.get(monitor).getBounds().getHeight();

        stage.setX(screenMinX + ((screenWidth - stage.getWidth()) / 2));
        stage.setY(screenMinY + ((screenHeight - stage.getHeight()) / 2));
    }

    /**
     * Метод задает расположение окна на мониторе относительно главного окна программы
     */
    public static void centerModalWindowRelativeToOwner(Stage window, Event e) {

        Window owner = e == null ? MAIN_STAGE : ((Node)e.getSource()).getScene().getWindow();

        double mainX = owner.getX();
        double mainY = owner.getY();

        double mainWidth = owner.getWidth();
        double mainHeight = owner.getHeight();

        double winWidth = window.getWidth();
        double winHeight = window.getHeight();

        window.setX(mainX + ((mainWidth - winWidth) / 2));
        window.setY(mainY + ((mainHeight - winHeight) / 2));
    }

    /**
     * Метод определяет в каком из мониторов произошел клик мышки
     */
    public static int findCurrentMonitorByMousePointer(MouseEvent event) {
        int monitor = 0;
        List<Screen> screenList = Screen.getScreens();
        double pointerX = event.getScreenX();
        for (int n = 0; n < screenList.size(); n++) {
            Screen screen = screenList.get(n);
            if (screen.getBounds().getMinX() < pointerX &&
                    pointerX < screen.getBounds().getMaxX() ) {
                monitor = n;
                break;
            }
        }
        return monitor;
    }

    /**
     * Метод определяет на каком из мониторов размещается окно программы
     */
    public static int findCurrentMonitorByMainStage(Stage window) {
        int monitor = 0;
        List<Screen> screenList = Screen.getScreens();
        double stageMiddleX = screenList.get(0).getBounds().getWidth()/2.0;
        if(window != null)
            stageMiddleX = window.getX() + window.getWidth() / 2.0;
        for (int n = 0; n < screenList.size(); n++) {
            Screen screen = screenList.get(n);
            if (screen.getBounds().getMinX() < stageMiddleX &&
                    screen.getBounds().getMaxX() > stageMiddleX) {
                monitor = n;
                break;
            }
        }
        return monitor;
    }

}
