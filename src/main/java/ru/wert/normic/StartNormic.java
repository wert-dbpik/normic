package ru.wert.normic;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.settings.NormConstants;

import java.io.IOException;
import java.util.Locale;

import static ru.wert.normic.AppStatics.PROJECT_VERSION;
import static ru.wert.normic.NormicServices.initQuickServices;
import static ru.wert.normic.NormicServices.initServices;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;
import static ru.wert.normic.decoration.DecorationStatic.LABEL_PRODUCT_NAME;

@Slf4j
public class StartNormic extends Application {

    private boolean initStatus = true;

    @Override
    public void init(){
        try {
            initServices();
            initQuickServices();
            new NormConstants();
            log.debug("init : DATA from server got well!");
        } catch (Exception e) {
            log.error("init : couldn't get DATA from server");
            initStatus = false;

        }
    }

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru"));
        LauncherImpl.launchApplication(StartNormic.class, args);
    }

    public void start(Stage stage){
        if (!initStatus) {
            Warning1.create("Внимание!", "Не удалось загрузить данные с сервера", "Работа программы будет прекращена");
            System.exit(0);
        }

        try {
            FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = mainWindowLoader.load();
            Decoration windowDecoration = new Decoration(
                    "НормИК-" + PROJECT_VERSION,
                    root,
                    true,
                    null,
                    "decoration-main",
                    false,
                    false);

            MAIN_STAGE = windowDecoration.getWindow();
            LABEL_PRODUCT_NAME = windowDecoration.getLblProductName();

        }catch (IOException e) {
            e.printStackTrace();
        }


    }


}
