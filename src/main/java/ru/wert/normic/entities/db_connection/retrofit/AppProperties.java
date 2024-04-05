package ru.wert.normic.entities.db_connection.retrofit;

import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.AppStatics;
import ru.wert.normic.decoration.warnings.Warning1;

import java.io.*;
import java.util.Properties;

import static ru.wert.normic.AppStatics.*;


@Slf4j
public class AppProperties {


    static AppProperties instance;

    public static AppProperties getInstance(){
        if(instance == null)
            return new AppProperties();
        else
            return instance;
    }

    private int attempt = 0;
    private Properties connectionProps;
    private final String appConfigPath;

    /**
     * Конструктор
     * Загружаем имеющиеся настройки доступа к серверу из файла connectionSettings.properties
     * если файла не существует, то он создается и в файл записываются данные по умолчанию
     */
    private AppProperties() {
        log.debug("AppProperties : propsFile создается  ...");
        appConfigPath =
                AppStatics.TEST_VERSION ?
                        NORMIC_HOME_PATH + File.separator + "settingsTest.properties" :
                        NORMIC_HOME_PATH + File.separator + "settings.properties";

        File propsFile = new File(appConfigPath);
        if (!propsFile.exists())
            createFileOfConnectionSettings(appConfigPath);
        else {
            try {
                connectionProps = new Properties();
                connectionProps.load(new FileInputStream(appConfigPath));
            } catch (IOException e) {
                Warning1.create("Ошибка!",
                        "Не удалось загрузить настройки доступа к серверу",
                        "Возможно, файл настроек поврежден");
                e.printStackTrace();
            }
        }
        log.debug("AppProperties : propsFile успешно создан");


    }

    /**
     * Создаем файл connectionSettings.properties, если он отсутствует каталоге программы
     * @param appConfigPath String, путь к файлу connectionSettings.properties
     */
    private void createFileOfConnectionSettings(String appConfigPath) {
        log.debug("createFileOfSettings : settings.properties создается  ...");
        try {
            log.debug("File of application settings is creating...");
            File dir = new File(NORMIC_HOME_PATH);
            dir.mkdirs();

            File props = new File(appConfigPath);
            props.createNewFile();
            log.info("File of application settings is created: {}", props.toString());

            FileWriter writer = new FileWriter (props);
            writer.write("IP_ADDRESS=" + SERVER_IP + "\n");
            writer.write("PORT =" + SERVER_PORT  + "\n");
            writer.write("SAVES_DIR=C:/\n");
            writer.write("IMPORT_DIR=C:/\n");
            writer.write("USER=1\n");
            writer.close();
        } catch (IOException e) {
            if(++attempt < 3) new AppProperties();
            else{

                Warning1.create("Ошибка!",
                        "Не удалось создать файл настроек доступа к серверу",
                        "Возможно, стоит защита от создания файлов,\n обратитесь к разработчику");
                e.printStackTrace();
            }

        }
        log.debug("createFileOfConnectionSettings : connectionSettings.properties успешно создан");
    }

    public String getIpAddress(){
        log.debug("IP_ADDRESS returns...{}", connectionProps.getProperty("IP_ADDRESS"));
        return connectionProps.getProperty("IP_ADDRESS");
    }

    public String getPort(){
        log.debug("PORT returns... {}", connectionProps.getProperty("PORT"));
        return connectionProps.getProperty("PORT");
    }

    public String getSavesDir(){
        log.debug("SAVES_DIR returns...{}", connectionProps.getProperty("SAVES_DIR"));
        return connectionProps.getProperty("SAVES_DIR") == null ?
                "C:\\" : connectionProps.getProperty("SAVES_DIR");
    }

    public String getImportDir(){
        log.debug("IMPORT_DIR returns...{}", connectionProps.getProperty("IMPORT_DIR"));
        return connectionProps.getProperty("IMPORT_DIR") == null ?
                "C:\\" : connectionProps.getProperty("IMPORT_DIR");
    }

    public String getUser(){
        log.debug("USER returns... {}", connectionProps.getProperty("USER"));
        return connectionProps.getProperty("USER");
    }

    public void setIpAddress(final String ip){
        try {
            FileOutputStream fos = new FileOutputStream(appConfigPath);
            connectionProps.setProperty("IP_ADDRESS", ip);
            connectionProps.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPort(final String port){
        try {
            FileOutputStream fos = new FileOutputStream(appConfigPath);
            connectionProps.setProperty("PORT", port);
            connectionProps.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSavesDirectory(final String dir){
        try {
            FileOutputStream fos = new FileOutputStream(appConfigPath);
            connectionProps.setProperty("SAVES_DIR", dir);
            connectionProps.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImportDirectory(final String dir){
        try {
            FileOutputStream fos = new FileOutputStream(appConfigPath);
            connectionProps.setProperty("IMPORT_DIR", dir);
            connectionProps.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(final String user){
        try {
            FileOutputStream fos = new FileOutputStream(appConfigPath);
            connectionProps.setProperty("USER", user);
            connectionProps.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
