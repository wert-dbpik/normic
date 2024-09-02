package ru.wert.normic.entities.saves;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.user.User;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveNormEntry implements Serializable {

    private String saveTime;

    private User user;
}
