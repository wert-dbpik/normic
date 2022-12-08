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

import static ru.wert.normic.AppStatics.THEME_STYLE;

@Slf4j
public class Decoration {
    private Stage window = null;
    private final String headerName; //название окна
    private FXMLLoader decorationLoader;
    private final Parent rootPane;
    private Parent decoration;
    private Label windowName;
    private final boolean resizable;
    private boolean waiting = false;
    private DecorationController decorationController;
    @Getter private ImageView imgCloseWindow;
    private boolean shift;
    private String decorationId;

//=============================================    НАЧАЛО    =========================================================
    /**
     * Конструктор
     * 1 - С указанием владельца
     */
    public Decoration(String headerName, Parent rootPane, Boolean resizable, Stage owner, String decorationId, boolean shift){
        this.headerName = headerName;
        this.rootPane = rootPane;
        this.resizable = resizable;
        this.decorationId = decorationId;
        this.shift = shift;
        log.debug("{} создан", this.getClass().getSimpleName());
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

            Scene scene = new Scene(decoration);
            scene.getStylesheets().add(this.getClass().getResource(THEME_STYLE).toString());

            window = new Stage();
            window.setScene(scene);

            window.initStyle(StageStyle.UNDECORATED);
            if (owner != null) settingOwner(owner);

            window.sizeToScene();
            window.setResizable(this.resizable);

            mountResizeButtons();

            if(!waiting) window.show();

            window.getIcons().add(new Image("/pics/logo3.png"));

            Platform.runLater(()->{
                window.setMinWidth(window.getWidth());
                window.setMinHeight(window.getHeight());
                int monitor = ModalWindow.findCurrentMonitorByMainStage(owner);
                if (!shift)
                    DecorationStatic.centerWindow(window, false, monitor);
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
}
