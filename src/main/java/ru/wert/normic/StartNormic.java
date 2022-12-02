package ru.wert.normic;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.decoration.DecorationController;
import ru.wert.normic.decoration.DecorationStatic;
import ru.wert.normic.decoration.warnings.Warning1;

import java.io.IOException;

import static ru.wert.normic.ChogoriServices.initQuickServices;
import static ru.wert.normic.ChogoriServices.initServices;

@Slf4j
public class StartNormic extends Application {

    private boolean initStatus = true;

    @Override
    public void init(){
        try {
            initServices();
            initQuickServices();
            log.debug("init : DATA from server got well!");
        } catch (Exception e) {
            log.error("init : couldn't get DATA from server");
            initStatus = false;

        }
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(StartNormic.class, args);
    }

    public void start(Stage stage) throws Exception {
        if (!initStatus) {
            Warning1.create("Внимание!", "Не удалось загрузить чертежи с сервера", "Работа программы будет прекращена" +
                    "\nдля перезагрузки сервера обратитесь к администратору");
            System.exit(0);
        }
        DecorationStatic.WF_MAIN_STAGE = stage;

        try {
            //Загружаем WindowDecoration
            FXMLLoader decorationLoader = new FXMLLoader(Decoration.class.getResource("/fxml/decoration/decoration.fxml"));
            Parent decoration = decorationLoader.load();
            DecorationController controller = decorationLoader.getController();

            //Загружаем loginWindow
            FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent mainWindow = mainWindowLoader.load();

            //loginWindow помещаем в WindowDecoration
            DecorationStatic.CH_DECORATION_ROOT_PANEL = (StackPane)decoration.lookup("#mainPane");
            DecorationStatic.CH_DECORATION_ROOT_PANEL.getChildren().add(mainWindow);


            //Меняем заголовок окна
            Label windowName = (Label)decoration.lookup("#windowName");
            String headerName = "";
            windowName.setText(headerName);

            Scene scene = new Scene(decoration);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            scene.getStylesheets().add(this.getClass().getResource("/css/pik-dark.css").toString());


            stage.sizeToScene();
            stage.setResizable(true);
            stage.getIcons().add(new Image("/pics/logo.png"));
            stage.setTitle("Нормик");

            stage.show();
            controller.centerInitialWindow(stage, false, 0);


        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
