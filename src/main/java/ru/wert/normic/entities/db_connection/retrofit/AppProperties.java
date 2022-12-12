package ru.wert.normic.entities.db_connection.retrofit;

import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.decoration.warnings.Warning1;

import java.io.*;
import java.util.Properties;


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
    private String homeDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "NormIC";
    private String appConfigPath = homeDir + File.separator + "settings.properties";


    /**
     * Конструктор
     * Загружаем имеющиеся настройки доступа к серверу из файла connectionSettings.properties
     * если файла не существует, то он создается и в файл записываются данные по умолчанию
     */
    private AppProperties() {
        log.debug("AppProperties : propsFile создается  ...");
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
            File dir = new File(homeDir);
            dir.mkdirs();

            File props = new File(appConfigPath);
            props.createNewFile();
            log.info("File of application settings is created: {}", props.toString());

            FileWriter writer = new FileWriter (props);
            writer.write("IP_ADDRESS=192.168.2.132\n");
            writer.write("PORT = 8080\n");
            writer.write("SAVES_DIR=C:/\n");
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
        return connectionProps.getProperty("SAVES_DIR");
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

}
