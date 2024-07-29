package ru.wert.normic.decoration;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

import static ru.wert.normic.AppStatics.CURRENT_PROJECT_VERSION;
import static ru.wert.normic.AppStatics.THEME_STYLE;

@Slf4j
public class Decoration {
    @Getter private Stage window = null;
    private final String headerName; //название окна
    private FXMLLoader decorationLoader;
    private final Parent rootPane;
    private Parent decoration;
    private Label windowName;
    @Getter private Label lblProductName;
    private final boolean resizable;
    private boolean waiting = false;
    private DecorationController decorationController;
    @Getter private ImageView imgCloseWindow;
    private boolean shift;
    private String decorationId;

    private ImageView imgBtnClose;
    private ImageView imgBtnMaximize;
    private ImageView imgBtnMinimize;

//    /**
//     * Конструктор
//     * 0 - С указанием ожидания
//     */
//    public Decoration(String headerName, Parent rootPane, Boolean resizable, Stage owner, String decorationId, boolean shift, boolean waiting){
//        this(headerName,rootPane, resizable, owner, decorationId, shift);
//        this.waiting = waiting;
//
//    }

//=============================================    НАЧАЛО    =========================================================
    /**
     * Конструктор
     * 1 - С указанием владельца
     */
    public Decoration(String headerName, Parent rootPane, Boolean resizable, Stage owner, String decorationId, boolean shift, boolean waiting){
        this.headerName = headerName; //Заголовок окна
        this.rootPane = rootPane; //Панель, вставливаемая в Decoration (VBox)
        this.resizable = resizable; //Изменяемы размер окна
        this.decorationId = decorationId; //Стиль оформления
        this.shift = shift; //Смещение относительно вызывающего окна
        this.waiting = waiting; //Ожидание реакции от пользвателя
        createWindow(owner);
    }


    /**
     * создание окна
     * Окно имеет переменную главную панель размещенную на StackPane. Подгружаются два fxml файла:
     * 1) - Панель с заголовком окна
     * 2) - Собственно переменная панель
     * Для обращения к элементам панели с заголовкам окна используется метод lookup().
     * Центрирование окна происходит после метода show()
     */
    private void createWindow(Stage owner){
        try {
            decorationLoader = new FXMLLoader(getClass().getResource("/fxml/decoration/decoration.fxml"));
            decoration = decorationLoader.load();
            decoration.setId(decorationId);
            decorationController = decorationLoader.getController();

            StackPane pane = (StackPane)decoration.lookup("#mainPane");
            pane.getChildren().add(rootPane);

            //Меняем заголовок окна
            windowName = (Label)decoration.lookup("#windowName");
            windowName.setText(headerName);

            lblProductName = (Label)decoration.lookup("#lblProductName");
            lblProductName.setStyle("-fx-text-fill: darkblue");

            imgBtnClose = (ImageView) decoration.lookup("#imgBtnClose");
            imgBtnMaximize = (ImageView) decoration.lookup("#imgBtnMaximize");;
            imgBtnMinimize = (ImageView) decoration.lookup("#imgBtnMinimize");;

            Scene scene = new Scene(decoration);
            scene.getStylesheets().add(this.getClass().getResource(THEME_STYLE).toString());

            window = new Stage();
            window.setScene(scene);
            window.initStyle(StageStyle.UNDECORATED);
            if (owner != null) settingOwner(owner);

            window.sizeToScene();
            window.setResizable(this.resizable);
            window.setTitle("НормИК-" + CURRENT_PROJECT_VERSION);

            mountResizeButtons();

            if(!waiting) window.show();

            window.getIcons().add(new Image("/pics/logo3.png"));

            Platform.runLater(()->{
                window.setMinWidth(window.getWidth());
                window.setMinHeight(window.getHeight());
                int monitor = ModalWindow.findCurrentMonitorByMainStage(owner);
                if (!shift)
                    DecorationStatic.centerWindowRelativeToScreen(window,false, monitor, owner);
                else
                    DecorationStatic.centerShiftedWindow(window, false, monitor, owner);
                window.toFront();
            });


            if(waiting) window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void settingOwner(Stage owner){
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(owner);

    }

    private void mountResizeButtons(){
        //Для модальных окон скрываем кнопки свернуть/развернуть кнопки
        ImageView imgMinimizeWindow = (ImageView)decoration.lookup("#imgBtnMinimize");
        imgMinimizeWindow.setVisible(resizable);
        ImageView imgMaximizeWindow = (ImageView)decoration.lookup("#imgBtnMaximize");
        imgMaximizeWindow.setVisible(resizable);
        imgCloseWindow = (ImageView)decoration.lookup("#imgBtnClose");
    }

    public void makeHeaderWhite() {
        windowName.setStyle("-fx-text-fill: white");
        imgBtnClose.setImage(new Image("/pics/decoration/close_white.png"));
        imgBtnMaximize.setImage(new Image("/pics/decoration/maximize_white.png"));
        imgBtnMinimize.setImage(new Image("/pics/decoration/minimize_white.png"));
    }
}
