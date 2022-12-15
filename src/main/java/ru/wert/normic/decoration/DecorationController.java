package ru.wert.normic.decoration;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Setter;


import java.util.List;

import static ru.wert.normic.decoration.DecorationStatic.WF_MAIN_STAGE;


//@Slf4j
public class DecorationController {

    @FXML private ImageView imgBtnClose;
    @FXML private ImageView imgBtnMaximize;
    @FXML private ImageView imgBtnMinimize;
    @FXML private Label windowName;
    @FXML private Pane paneR;
    @FXML private Pane paneX;
    @FXML private Pane paneB;

    //Состояние окна
    private int winX;
    private int winY;
    private int winWidth;
    private int winHeight;

    private Stage window;
    private double dragOffsetX;
    private double dragOffsetY;
    @Setter private boolean isExpanded;


//===========================================    НАЧАЛО     ========================================================
    /**
     *  Конструктор без параметров
     *  Обязателен для корректной работы с FXML файлом
     */
    public DecorationController() {

    }

//===========================================    КНОПКИ     ========================================================

    /**
     * Обработка события кнопки "_" на заголовке окна
     * Сворачивание окна при нажатии на кнопку
     */
    @FXML
    void minimizeWindow(Event e){
        window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        saveWindowCoordinates(window);
        ((Stage) ((Node)e.getSource()).getScene().getWindow()).setIconified(true);
    }

    private void saveWindowCoordinates(Stage window) {
        winX = (int) window.getX();
        winY = (int) window.getY();
        winWidth = (int) window.getWidth();
        winHeight = (int) window.getHeight();
    }

    /**
     * Обработка события кнопки "+" на заголовке окна
     * Разворачивание окна на весь экран, если оно еще не развернуто
     * или сворчивание окна до предыдущего состояния
     */
    @FXML
    void maximizeWindow(MouseEvent e) {
        window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        if (!isExpanded) {
            saveWindowCoordinates(window);
            changeSizeOfWindow(window, e);
            //При условии, что на весь экран раскрывается только главное окно
            WF_MAIN_STAGE.setFullScreen(true);
        } else {
            changeSizeOfWindow(window, e);
            //При условии, что на весь экран раскрывается только главное окно
            WF_MAIN_STAGE.setFullScreen(false);
        }

    }

    /**
     * Закрытие окна после ОТПУСКАНИЯ нажатой кнопки Х или кнопки ОТМЕНА
     * Метод закрывает окно принажатии на крестик или на кнопку ОТМЕНА
     * Так же метод может быть вызван из контроллеров
     */
    @FXML
    public void closeWindow(Event event){
        DecorationStatic.closeWindow(event);
    }

//===========================================    СОБЫТИЯ МЫШИ     =====================================================

    /**
     * Обработка двойного нажатия мыши на заголовок окна
     * Метод, необходим для разворачивания окна на весь экран
     * И обратного сворачивания
     * @param mouseEvent MouseEvent
     */
    @FXML
    private void TitleBarOnMouseClicked(MouseEvent mouseEvent){

        Stage window = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        this.window = window;

        if (window.isFocused() &&
                window.isResizable() &&
                mouseEvent.getClickCount() == 2)
                    changeSizeOfWindow(window, mouseEvent);
    }

    /**
     * Разворачивание или сворачивание окна
     * Условием является состояние флага isExpanded (развернут на весь экран)
     */
    private void changeSizeOfWindow(Stage window, MouseEvent e) {

        if (isExpanded) {
            window.setWidth(winWidth);
            window.setHeight(winHeight);
            window.setX(winX);
            window.setY(winY);

            isExpanded = false;
        } else {

            setWindowToFullScreen(window);
            isExpanded = true;
        }
    }

    /**
     * Разворачивает окно на весь экран с учетом видимой области
     */
    private void setWindowToFullScreen(Stage window){

        saveWindowCoordinates(window);

        List<Screen> screenList = Screen.getScreens();
        int monitor = ModalWindow.findCurrentMonitorByMainStage(window);

        window.setX(screenList.get(monitor).getVisualBounds().getMinX());
        window.setY(screenList.get(monitor).getVisualBounds().getMinY());
        window.setWidth(screenList.get(monitor).getVisualBounds().getWidth());
        window.setHeight(screenList.get(monitor).getVisualBounds().getHeight());
    }

//===========================================  РАСТЯГИВАЕМ ОКНО   =================================================

    /**
     * Изменить курсор с дефолтного на соответствующий направлению растягивания
     * В модель окна добавлены вспомогательные панели, три из которых имеют названия
     * paneX - панель на перекрестии панель правой(Right) и нижней (BottomLineController)
     * Курсор меняется только для resizable окон
     * @param e MouseEvent
     */
    @FXML
    private void changeCursorOnMouseMoved(MouseEvent e){
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        if (window.isResizable()) {
            switch(((Node)e.getSource()).getId()){
                case ("paneR"): ((Node) e.getSource()).setCursor(Cursor.H_RESIZE); break;
                case ("paneB"): ((Node) e.getSource()).setCursor(Cursor.V_RESIZE); break;
                case ("paneX"): ((Node) e.getSource()).setCursor(Cursor.NW_RESIZE); break;
            }
        }
    }

    /**
     * Изменить размер экрана при перетаскивании его краев
     * Изменение размеров окна возможно только для resizable окон. Размер окна в итоге получается
     * "приблизительно", т.к. толщину вспомогательных панелей и куда ткунались для перетаскивания мышка
     * никто не учитывает. Единственно что, если край таскать за минимальный размер окна, то по факту окно
     * окончательно принимает минимальный размер. Использование метода OnMousePressed дало бы более точный результат
     * но мне кажется, что он работает медленнее.
     * @param e
     */
    @FXML
    private void resizeWindowOnMouseDragged(MouseEvent e){
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        if (window.isResizable()) {
                double newWidth = e.getScreenX()-window.getX();
                double newHeight = e.getScreenY()-window.getY();

                switch(((Node) e.getSource()).getId()){
                    case ("paneR"): {
                        window.setWidth(Math.max(newWidth, window.getMinWidth()));
                    } break;
                    case ("paneB"): {
                        window.setHeight(Math.max(newHeight, window.getMinHeight()));
                    } break;
                    case ("paneX"): {
                        window.setWidth(Math.max(newWidth, window.getMinWidth()));
                        window.setHeight(Math.max(newHeight, window.getMinHeight()));
                    }
                    break;
            }
        }

    }

//==========================================   ПЕРЕМЕЩАЕМ ОКНО  ===================================================
    /**
     * Обработка нажатия мыши на заголовок окна
     * Метод, необходимы для перетаскивания окна за его загловок.
     * Здесь определяется положение мыши в относительных координатах.
     * @param mouseEvent MouseEvent
     */
    @FXML
    private void onMousePressed(MouseEvent mouseEvent){
        Stage window = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        this.dragOffsetX = mouseEvent.getScreenX() - window.getX();
        this.dragOffsetY = mouseEvent.getScreenY() - window.getY();
    }

    /**
     * Обработка движения мыши приперетаскивании окна
     * Метод, необходимы для перетаскивания окна за его загловок.
     * Здесь окну передаются координаты, которые он меняет при перетаскивании
     * @param mouseEvent
     */
    @FXML
    private void onMouseDragged(MouseEvent mouseEvent){
        Stage window = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        window.toFront();
        window.setX(mouseEvent.getScreenX() - this.dragOffsetX);
        window.setY(mouseEvent.getScreenY() - this.dragOffsetY);
    }


//===========================================    СОБЫТИЯ КЛАВИАТУРЫ     ===============================================
    /**
     * Закрытие окна при нажатии на клавишу ESCAPE
     * @param keyEvent
     */
    @FXML
    void OnKeyEscapeTyped(KeyEvent keyEvent){
        Stage window = (Stage) ((Node)keyEvent.getSource()).getScene().getWindow();
        if (window.isFocused() &&
            keyEvent.getCode() == KeyCode.ESCAPE)
            window.hide();
    }

}

