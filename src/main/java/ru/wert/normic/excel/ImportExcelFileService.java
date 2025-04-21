package ru.wert.normic.excel;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.controllers._forms.main.MainController;
import ru.wert.normic.entities.ops.single.OpAssm;

import java.io.File;


@Slf4j
public class ImportExcelFileService extends Service<OpAssm> {

    private final File copied;
    private final MainController mainController;

    public ImportExcelFileService(MainController mainController, File copied) {
        this.mainController = mainController;
        this.copied = copied;
    }

    @Override
    protected Task<OpAssm> createTask() {
        return new Task<OpAssm>() {
            @Override
            protected OpAssm call() throws Exception {
                mainController.getProgressIndicator().setVisible(true);
                return new ExcelImporter().convertOpAssmFromExcel(copied);
            }

            @Override
            protected void done() {
                super.done();
                mainController.getProgressIndicator().setVisible(false);
            }

            @Override
            protected void failed() {
                super.failed();
                mainController.getProgressIndicator().setVisible(false);
            }

        };
    }

}

