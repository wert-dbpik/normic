package ru.wert.normic.entities.saves;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavesHistory{

    private String saveTime;

    private User user;
}
