package ru.wert.normic.historyChanges;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Класс описывает запись истории измения
 */
@Getter
@Setter
public class HistoryChanges {

    private String user;
    private Date date;
}
