package ru.wert.normic.controllers.intro;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import retrofit2.Call;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;
import ru.wert.normic.entities.db_connection.user.User;
import ru.wert.normic.entities.db_connection.user.UserApiInterface;

import java.io.File;
import java.io.IOException;

import static ru.wert.normic.AppStatics.PROPS_PATH;
import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class NetConnection {

    private static final File propsFile = new File(PROPS_PATH);

    /**
     * Проверяем наличие связи с сервером
     * При введении новых параметров соединения снова проверяем соединение и так по кругу
     */
    public static boolean checkUp(ConnectionParams params) {
        ConnectionParams initParams = params;
        UserApiInterface api = RetrofitClient.getInstance().getRetrofit().create(UserApiInterface.class);
        try {
            Call<User> call = api.getById(1L);
            call.execute();
        } catch (IOException e) {
            ConnectionParams newParams = getConnectionParamsFromDialog(params, null);
            if (newParams != null) {
                //фиксируем полученные данные в файл
                params = newParams;
                writeConnectionParamsToPropsFile(params);
                checkUp(params);
            }
            //восстанавливаем исходные данные в файл
            writeConnectionParamsToPropsFile(initParams);
            return false;
        }
        return true;
    }

    /**
     * Получаем параметры соединения из диалога
     */
    private static ConnectionParams getConnectionParamsFromDialog(ConnectionParams initParams, Stage owner){
        final ConnectionParams[] finalParams = new ConnectionParams[1];

        try {
            FXMLLoader loader = new FXMLLoader(NetConnection.class.getResource("/fxml/intro/connection.fxml"));
            Parent parent = loader.load();
            ConnectionController controller = loader.getController();
            controller.init(initParams);

            Platform.runLater(()->{
                controller.getBtnOK().getScene().getWindow().setOnHiding(e->{
                    finalParams[0] = controller.getFinalParams();

                });
            });

            Decoration decoration = new Decoration(
                    "Внимание!",
                    parent,
                    false,
                    owner == null ? MAIN_STAGE : owner,
                    "decoration-settings",
                    false,
                    true);

            return finalParams[0];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * Фиксируем новые данные в текстовом файле connectionSettings.properties
     * @param params
     */
    private static void writeConnectionParamsToPropsFile(ConnectionParams params){
        AppProperties.getInstance().setIpAddress(params.getIp());
        AppProperties.getInstance().setPort(params.getPort());
    }


}
