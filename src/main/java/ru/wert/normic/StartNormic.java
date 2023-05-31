package ru.wert.normic;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.user.UserService;
import ru.wert.normic.settings.NormConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static ru.wert.normic.AppStatics.*;
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
            initUser();
            NormConstants.getInstance();
            log.debug("init : DATA from server got well!");
        } catch (Exception e) {
            log.error("init : couldn't get DATA from server");
            initStatus = false;

        }
    }

    private void initUser() {
        String bazaPikHomePath = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "BazaPIK";
        String propsPath = TEST_VERSION ? bazaPikHomePath + File.separator + "connectionSettingsTest.properties" :
                bazaPikHomePath + File.separator + "connectionSettingsTest.properties";
        File propsFile = new File(propsPath);
        if (propsFile.exists()){
            log.info("BazaPIK props path : " + propsPath );
            try {
                Properties bazaPikProps = new Properties();
                bazaPikProps.load(new FileInputStream(propsFile));
                Long userId = Long.parseLong(bazaPikProps.getProperty("LAST_USER"));
                CURRENT_USER = UserService.getInstance().findById(userId);
                CURRENT_USER_GROUP = CURRENT_USER.getUserGroup();
                log.info("Current user is identified as " + CURRENT_USER.getName());
            } catch (Exception e) {
                CURRENT_USER = null;
                CURRENT_USER_GROUP = null;
                log.error("Current user is not identified with exception " + e.getMessage());
            }
        } else
            log.info("Current user is not identified : propsFile does not exist.");
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
