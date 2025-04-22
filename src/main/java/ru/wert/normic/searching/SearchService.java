package ru.wert.normic.searching;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.controllers._forms.main.FormMenuManager;
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

    private final String searchText; //Искомый текст (номер или наименование)
    private final OpAssm opAssm; //Сюда добавляем найденные элементы
    private final SearchingFileController mainController; //Контроллер из которого запущен поиск
    private final FormMenuManager menu; //Необходим длоя добавления найденных плашек
    private final boolean showEntries; //Показываеть входимости - т.е. изделия куда входит искомый узел
    private final List<OpData> addedOperations = new ArrayList<>(); //Перечень найденных плашек

    public SearchService(SearchingFileController mainController, OpAssm opAssm, String searchText, boolean showEntries) {
        this.mainController = mainController;
        this.opAssm = opAssm;
        this.searchText = searchText.toLowerCase();
        this.menu = mainController.getMenu();
        this.showEntries = showEntries;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                mainController.getProgressIndicator().setVisible(true);
                List<String> allNVRFilesWithSearchedText = collectAllNVRFilesWithSearchedText();
                for (String path : allNVRFilesWithSearchedText) {
                    File file = new File(path);
                    OpData opData = new NvrConverter(file).getConvertedOpData();
                    //Если искомый текст содержится только в названии nvr файла
                    if (showEntries)
                        showFoundOpData(opData);
                    else
                        searchAndShowEveryDataSeparatly(opData);
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

    /**
     * Рекурсивно проходит по файлу и выводит в поле поиска найденные узлы
     */
    private void searchAndShowEveryDataSeparatly(OpData opData){
        if(((IOpWithOperations)opData).getName().toLowerCase().contains(searchText)) {
                showFoundOpData(opData);
        }

        List<OpData> ops = ((IOpWithOperations)opData).getOperations();
        for (OpData op : ops) {
            if (op instanceof IOpWithOperations)
                searchAndShowEveryDataSeparatly(op);

        }
    }

    /**
     * Метод добавляет найденный узел в окно поиска, если он не повторяется
     */
    private void showFoundOpData(OpData opData) {
        if(opDataIsDubbed(opData)) return;
        Platform.runLater(()->{
            addedOperations.add(opData);
            if(opData instanceof OpDetail)
                menu.addDetailPlate((OpDetail) opData);
            if(opData instanceof OpAssm)
                menu.addAssmPlate((OpAssm) opData);
            if(opData instanceof OpPack)
                menu.addPackPlate((OpPack) opData);
        });
    }

    /**
     * В директории поиска собирает все файлы с расширением .nvr, содержащие строку
     */
    private List<String> collectAllNVRFilesWithSearchedText(){
        List<String> foundNVRFiles = new ArrayList<>();
        try{
            //Собираем в дирректории все файлы с расширением nvr
            Stream<Path> stream = Files.walk(Paths.get(AppProperties.getInstance().getWhereToSearch()));
            Set<String> allFiles = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".nvr"))
                    .collect(Collectors.toSet());
            stream.close();

            //В полученном списке находим файлы, где встречается искомый текст
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


    /**
     * Проверка на одинаковые операции в списке
     * Если имена разные и сумма норм времении не совпадают,
     * то элементы считаются разными
     */
    private boolean opDataIsDubbed(OpData opData) {
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

