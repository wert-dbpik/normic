package ru.wert.normic.controllers.intro;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import ru.wert.normic.AppStatics;
import ru.wert.normic.components.BXUsers;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.db_connection.user.User;
import ru.wert.normic.entities.db_connection.user.UserService;

import static ru.wert.normic.AppStatics.*;

public class LoginController {

    @FXML
    private ComboBox<User> bxUsers;

    @FXML
    private Button btnOK;

    //Поле ввода пароля
    @FXML
    private PasswordField passwordField;

    @FXML
    void initialize() {

        btnOK.setOnAction(e->{
            User user = bxUsers.getValue();
            String pass = passwordField.getText();

            if (user.getPassword().equals(pass)) {
                if(!CURRENT_USER.equals(user)){
                    AppStatics.createLog(true, CURRENT_USER.getName() + " вошел в программу NormIC");
                }

                CURRENT_USER = user;
                CURRENT_USER_GROUP = CURRENT_USER.getUserGroup();

                AppProperties.getInstance().setUser(String.valueOf(CURRENT_USER.getId()));
                ((Node) e.getSource()).getScene().getWindow().hide();
            } else {
                passwordField.setText("");
                Warning1.create(e, "Внимание!", "Пользователь не найден", "Попробуйте еще раз");
            }
        });

        new BXUsers(bxUsers);
        bxUsers.setStyle("-fx-font-size: 12");

        long userId = Long.parseLong(AppProperties.getInstance().getUser());
        User lastUser = userId == 0L ? null : UserService.getInstance().findById(userId);

        //Если последнего пользователя успели заблокировать
        if (lastUser != null && !lastUser.isActive()) {
            bxUsers.getSelectionModel().select(null);
        } else {
            bxUsers.getSelectionModel().select(lastUser);
        }

        Platform.runLater(()->passwordField.requestFocus());

    }

}
