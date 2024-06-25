package ru.wert.normic.searching;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.controllers._forms.MainController;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.excel.ExcelImporter;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuForm;
import ru.wert.normic.utils.NvrConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class SearchService extends Service<Void> {

    private final String searchText;
    private final OpAssm opAssm;
    private final SearchingFileController mainController;
    private final MenuForm menu;
    private final boolean showEntry;
    private final List<OpData> addedOperations = new ArrayList<>();

    public SearchService(SearchingFileController mainController, OpAssm opAssm, String searchText, boolean showEntry) {
        this.mainController = mainController;
        this.opAssm = opAssm;
        this.searchText = searchText.toLowerCase();
        this.menu = mainController.getMenu();
        this.showEntry = showEntry;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                mainController.getProgressIndicator().setVisible(true);
                List<String> foundNVRFiles = collectFoundNVRFiles();
                if (showEntry) showNVRFiles(foundNVRFiles);
                else
                    for (String path : foundNVRFiles) {
                        OpData opData = new NvrConverter(new File(path)).getConvertedOpData();
                        findSearchedOpData((OpAssm) opData);
                    }
                return null;
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

    private void showNVRFiles(List<String> foundNVRFiles) {
        for(String path : foundNVRFiles){
            OpAssm op = (OpAssm) new NvrConverter(new File(path)).getConvertedOpData();
            Platform.runLater(()->{
                menu.addAssmPlate((OpAssm) op);
            });
        }
    }

    /**
     * В директории поиска собирает все файлы с расширением .nvr, содержащие строку
     */
    private List<String> collectFoundNVRFiles(){
        List<String> foundNVRFiles = new ArrayList<>();
        try{
            Stream<Path> stream = Files.walk(Paths.get(AppProperties.getInstance().getWhereToSearch()));
            Set<String> allFiles = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".nvr"))
                    .collect(Collectors.toSet());
            stream.close();

            for(String path : allFiles){
                String content = new String(Files.readAllBytes(Paths.get(path))).toLowerCase();
                if(content.contains(searchText))
                    foundNVRFiles.add(path);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return foundNVRFiles;
    }

    private void findSearchedOpData(OpAssm opAssm) {
        for(OpData op : opAssm.getOperations()){
            if(op instanceof IOpWithOperations)
                if(((IOpWithOperations) op).getName().toLowerCase().contains(searchText)){
                    if(!doubleOperations(op)){
                        addedOperations.add(op);
                        Platform.runLater(()->{
                            if(op instanceof OpDetail)
                                menu.addDetailPlate((OpDetail) op);
                            if(op instanceof OpAssm)
                                menu.addAssmPlate((OpAssm) op);
                        });
                        if(op instanceof OpAssm)
                            findSearchedOpData((OpAssm) op);
                    }
                }
        }
    }

    /**
     * Проверка на одинаковые операции в списке
     * Если имена разные и сумма норм времении не совпадают,
     * то элементы считаются разными
     */
    private boolean doubleOperations(OpData opData) {
        for (OpData op : addedOperations) {
            if (
                    ((IOpWithOperations) op).getName().toLowerCase().equals(((IOpWithOperations) opData).getName().toLowerCase()) &&
                            (op.getMechTime() + op.getPaintTime() + op.getAssmTime() + op.getPackTime()) ==
                                    (opData.getMechTime() + opData.getPaintTime() + opData.getAssmTime() + opData.getPackTime())
            )
                return true;
        }
        return false;
    }

}

