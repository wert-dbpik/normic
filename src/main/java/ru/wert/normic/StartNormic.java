package ru.wert.normic;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.controllers.intro.ConnectionParams;
import ru.wert.normic.controllers.intro.NetConnection;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
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
import static ru.wert.normic.decoration.DecorationStatic.LABEL_PRODUCT_NAME;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

@Slf4j
public class StartNormic extends Application {

    private static final AppProperties normicProps = AppProperties.getInstance();
    private static final File propsFile = new File(PROPS_PATH);

    private void initUser() {
//        File propsFile = new File(propsPath);
        if (propsFile.exists()){
            log.info("Tubus props path : " + PROPS_PATH );
            getUserFromConnectionSettings(propsFile);
        } else{
            log.info("Current user is not identified : propsFile does not exist.");
            //Создаем файл connectionSettings.properties
        }

    }

    private void getUserFromConnectionSettings(File propsFile) {
        try {
//            Properties tubusProps = new Properties();
            Long userId = Long.parseLong(normicProps.getUser());
            CURRENT_USER = UserService.getInstance().findById(userId);
            CURRENT_USER_GROUP = CURRENT_USER.getUserGroup();
            log.info("Current user is identified as " + CURRENT_USER.getName());
            AppStatics.createLog(true, "Зашел в  приложение NormIC");

        } catch (Exception e) {
            CURRENT_USER = null;
            CURRENT_USER_GROUP = null;
            log.error("Current user is not identified with exception " + e.getMessage());
            //Проверяем настройки подключения к серверу
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru"));
        LauncherImpl.launchApplication(StartNormic.class, args);
    }

    public void start(Stage stage){

        try {
            FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = mainWindowLoader.load();
            Decoration windowDecoration = new Decoration(
                    "НормИК-" + CURRENT_PROJECT_VERSION,
                    root,
                    true,
                    null,
                    "decoration-main",
                    false,
                    false);

            MAIN_STAGE = windowDecoration.getWindow();
            LABEL_PRODUCT_NAME = windowDecoration.getLblProductName();

//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Platform.runLater(() -> {
//                        //Определяем последнюю доступную версию программы в Базе данных
//                        List<VersionNormic> allVersions = VersionNormicService.getInstance().findAll();
//                        LAST_VERSION_IN_DB = allVersions.get(allVersions.size() - 1).getName();
//                        if (CURRENT_PROJECT_VERSION.compareTo(LAST_VERSION_IN_DB) < 0)
//                            Warning1.create("Внимание!", "Доступна новая версия программы " + LAST_VERSION_IN_DB,
//                                    "Скачайте самостоятельно\nили обратитесь к администратору");
//                    });
//                }
//            }, 2000);

            initData();

        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void initData(){
        try {
            ConnectionParams initConnectionParams = new ConnectionParams(normicProps.getIpAddress(), normicProps.getPort());

            if(!NetConnection.checkUp(initConnectionParams)){
                Warning1.create("ПРОБЛЕМА!", "Не удалось подключиться к серверу",
                        "Возможно, он отключен или отсутствует сеть\n" +
                                "повторите попытку подключения позже");
                System.exit(0);
            } else {
                initServices();
                initQuickServices();
                initUser();
                NormConstants.getInstance();
                log.debug("init : DATA from server got well!");
            }

        } catch (Exception e) {
            log.error("init : couldn't get DATA from server");
            e.printStackTrace();
        }
    }

}
