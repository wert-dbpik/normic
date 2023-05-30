package ru.wert.normic.entities.db_connection.constants;

import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.files.FilesService;

import java.io.File;


@Slf4j
public class DownloadConstantsFileTask extends Task<Void> {
    NormConstants normConstants;

    public DownloadConstantsFileTask(NormConstants normConstants) {
        this.normConstants = normConstants;
    }

    @Override
    protected Void call() throws Exception {

        File file = new File(normConstants.getHomeDir());
        if(!file.exists() && file.mkdir()){
            failed();
        }

        FilesService.getInstance().download("normic",
                "def-constants",
                ".properties",
                normConstants.getHomeDir(),
                "constants");
        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        normConstants.createConstantsProps();
    }

    @Override
    protected void failed() {
        super.failed();
        log.error("Ошибка при скачивании файла с константами с сервера");
        Warning1.create("Ошибка!",
                "Не удалось создать файл  с константами с сервера",
                "Возможно, ошибка на сервере");
    }
}
