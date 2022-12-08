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

import static ru.wert.normic.AppStatics.PROJECT_VERSION;
import static ru.wert.normic.AppStatics.THEME_STYLE;
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
            Warning1.create("Внимание!", "Не удалось загрузить данные с сервера", "Работа программы будет прекращена");
            System.exit(0);
        }
        DecorationStatic.WF_MAIN_STAGE = stage;

        try {
            //Загружаем WindowDecoration
            FXMLLoader decorationLoader = new FXMLLoader(Decoration.class.getResource("/fxml/decoration/decoration.fxml"));
            Parent decoration = decorationLoader.load();
            decoration.setId("decoration-main");
            DecorationController controller = decorationLoader.getController();

            //Загружаем loginWindow
            FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = mainWindowLoader.load();

            //loginWindow помещаем в WindowDecoration
            DecorationStatic.CH_DECORATION_ROOT_PANEL = (StackPane)decoration.lookup("#mainPane");
            DecorationStatic.CH_DECORATION_ROOT_PANEL.getChildren().add(root);


            //Меняем заголовок окна
            Label windowName = (Label)decoration.lookup("#windowName");

            windowName.setText("НормИК-" + PROJECT_VERSION);

            Scene scene = new Scene(decoration);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            scene.getStylesheets().add(this.getClass().getResource(THEME_STYLE).toString());


            stage.sizeToScene();
            stage.setResizable(true);
            stage.getIcons().add(new Image("/pics/logo.png"));
            stage.setTitle("НормИК-" + PROJECT_VERSION);

            stage.show();
            controller.centerInitialWindow(stage, false, 0);


        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
