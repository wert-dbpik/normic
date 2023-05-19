package ru.wert.normic.controllers._forms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.singlePlates.PlateAssmController;
import ru.wert.normic.controllers.singlePlates.PlateDetailController;
import ru.wert.normic.decoration.warnings.Warning1;
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IForm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuForm;
import ru.wert.normic.menus.MenuPlate;
import ru.wert.normic.settings.ProductSettings;
import ru.wert.normic.utils.OpDataJsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.decoration.DecorationStatic.LABEL_PRODUCT_NAME;
import static ru.wert.normic.decoration.DecorationStatic.TITLE_SEPARATOR;
import static ru.wert.normic.enums.EColor.*;

/**
 * ПРООБРАЗ
 */
public abstract class AbstractFormController implements IForm {

    //Коллекции переносимых элементов
    //При переносе какого-либо узла переносится одновременно соответственно элементу OpData = AbstractOpPlate = VBox
    public static AbstractFormController whereFromController;
    public static List<OpData> clipOpDataList = new ArrayList<>(); //Коллекция переносимых операций
    public static List<AbstractOpPlate> clipOpPlateList = new ArrayList<>(); //Коллекция переносимых плашек
    public static List<VBox> clipBoxList = new ArrayList<>(); //Колеекция переносимых Бксов
    public static boolean copy; //true - (КОПИРОВАТЬ) переносимые элементы не удаляются, false - (ВЫРЕЗАТЬ) удаляются
    //------------------------

    @Getter protected MenuForm menu;
    @Getter protected OpData opData;
    @Getter protected List<AbstractOpPlate> addedPlates = new ArrayList<>();
    @Getter protected ObservableList<OpData> addedOperations = FXCollections.observableArrayList();

    @Getter protected DoubleProperty formAreaProperty = new SimpleDoubleProperty(0.0);

    private final ObjectProperty<ListCell<VBox>> dragSource = new SimpleObjectProperty<>();
    private final Image imageCopy = new Image(String.valueOf(getClass().getResource("/pics/btns/cursor_copy.png")),
            32, 32, true, true);
    private final Image imageCut = new Image(String.valueOf(getClass().getResource("/pics/btns/cursor_cut.png")),
            32, 32, true, true);


    public abstract void countSumNormTimeByShops();
    public abstract MenuForm createMenu();
    public abstract void fillOpData();
    public abstract ListView<VBox> getListViewTechOperations();
    public abstract Button getBtnAddOperation();

    private String opType; //Тип загружаемого json - временная переменная

    private List<List<OpData>> undoList = new ArrayList<>();
    @Getter private int iterator = 0;

    protected boolean blockUndoListFlag; //true - undoList заполняется

    @FXML
    private VBox main;

    protected void iterateUndoList(){
        iterator++;
        if(iterator == undoList.size())
            undoList.add(new ArrayList<>(addedOperations));
        else
            undoList.set(iterator, new ArrayList<>(addedOperations));
        blockUndoListFlag = true;
        for(int i = iterator + 1; i < undoList.size() - 1; i ++ )
        undoList.remove(i);
        blockUndoListFlag = false;
    }


    public AbstractFormController() {
        //Нулевая позиция
        undoList.add(new ArrayList<>(addedOperations));

        addedOperations.addListener((ListChangeListener<OpData>) change -> {
            if(!blockUndoListFlag) {
                iterateUndoList();
            }
        });

        Platform.runLater(()->{
            main.setOnKeyPressed(ke->{
                if (ke.isControlDown() && ke.getCode().equals(KeyCode.Z)) {
                    blockUndoListFlag = true;
                    addedPlates.clear();
                    addedOperations.clear();
                    getListViewTechOperations().getItems().clear();

                    if (ke.isShiftDown()) redoLastOperation();
                    else undoLastOperation();

                    menu.deployData();
                    countSumNormTimeByShops();
                    blockUndoListFlag = false;

                }
            });
        });

    }

    private void undoLastOperation() {
        if(iterator == 0) return;
        iterator --;
        ((IOpWithOperations)opData).setOperations(new ArrayList<>(undoList.get(iterator)));
    }

    private void redoLastOperation() {
        if(iterator == undoList.size()-1) return;
        iterator ++;
        ((IOpWithOperations)opData).setOperations(new ArrayList<>(undoList.get(iterator)));
    }

    AbstractFormController getThisController(){
        return this;
    }

    public double calculateAreaByDetails(){
        double area = 0.0;
        List<OpData> ops = getAddedOperations();
        for(OpData op : ops){
            if(op instanceof IOpWithOperations)
                area += ((IOpWithOperations) op).getArea() * op.getQuantity();
        }
        formAreaProperty.set(area);
        return area;
    }

    protected void setDragAndDropCellFactory(){
        getListViewTechOperations().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getListViewTechOperations().setCellFactory(new Callback<ListView<VBox>, ListCell<VBox>>() {
            @Override
            public ListCell<VBox> call(ListView<VBox> operationsListView) {
                ListCell<VBox> cell = new ListCell<VBox>(){
                    @Override
                    protected void updateItem(VBox item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                    }
                };

                cell.setOnDragDetected(e->{
                    if (!cell.isEmpty()) {
                        Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent cc = new ClipboardContent();
                        List<Integer> indices = getListViewTechOperations().getSelectionModel().getSelectedIndices();
                        if(indices.isEmpty()) return;
                        clearClipboard();
                        for(int index : indices){
                            clipOpDataList.add(addedOperations.get(index)); //
                            clipOpPlateList.add(addedPlates.get(index));
                            clipBoxList.add(getListViewTechOperations().getItems().get(index));
                        }
                            copy = e.getButton().equals(MouseButton.SECONDARY);

                        if(copy) {

                            WritableImage image = new Text("Копируем").snapshot(null, null);
                            db.setDragView(image, 5.0 + image.getWidth(), 0.0);
                        }
                        cc.putString("data");
                        db.setContent(cc);
                        e.consume();
                    }
                });

                cell.setOnDragOver(e -> {
                    if(cell.getItem() != null) {
                        getListViewTechOperations().getSelectionModel().clearSelection();
                        getListViewTechOperations().getSelectionModel().select(cell.getItem());

                        Dragboard db = e.getDragboard();

                        OpData targetOpData = addedOperations.get(cell.getIndex());
                        if (clipOpDataList.contains(targetOpData))
                            e.acceptTransferModes(TransferMode.MOVE);
                        else if (db.hasString() && dropIsPossible(targetOpData)) {
                            e.acceptTransferModes(TransferMode.MOVE);
                        } else {
                            e.acceptTransferModes(TransferMode.NONE);
                        }
                    } else{
                        if(copy) e.acceptTransferModes(TransferMode.MOVE);
                        else e.acceptTransferModes(TransferMode.NONE);
                    }

                    e.consume();
                });

                cell.setOnDragDropped(e -> {
                    if(cell.getItem() != null) {
                        OpData targetOpData = addedOperations.get(cell.getIndex());
                        whereFromController = getThisController();
                        if (e.getTransferMode().equals(TransferMode.MOVE) && !clipOpDataList.contains(targetOpData)) {
                            addOperation(addedOperations.get(cell.getIndex()));
                            e.setDropCompleted(true);
                        } else {
                            e.setDropCompleted(false);

                        }
                    } else if(e.getTransferMode().equals(TransferMode.MOVE)){
                        addOperation(opData);
                        e.setDropCompleted(true);
                    }
                });


                cell.setOnMouseClicked(e -> {
                    OpData selectedOpData = null;
                    if (e.getButton().equals(MouseButton.SECONDARY)) {
                        boolean cellIsEmpty = cell.isEmpty();
                        if(cellIsEmpty) {
                            cell.getListView().getSelectionModel().clearSelection();
                            selectedOpData = opData;
                        } else
                            selectedOpData = addedOperations.get(cell.getIndex());
                        new MenuPlate().create(getThisController(), selectedOpData, cellIsEmpty).show(
                                ((Node)e.getSource()).getScene().getWindow(),
                                e.getScreenX(),
                                e.getScreenY());
                    }
                    e.consume(); //Чтобы не срабатывал слушатель ниже
                });

                return cell;
            }
        });

        //Вызывеает меню при пустом списке операций, когда не срабатывает слушатель на пустой ячейке
        getListViewTechOperations().setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.SECONDARY))
                new MenuPlate().create(getThisController(), null, true).show(
                        ((Node)e.getSource()).getScene().getWindow(),
                        e.getScreenX(),
                        e.getScreenY());
        });

    }

    /**
     * Метод обнуляет данные буфера обмена
     */
    private void clearClipboard() {
        clipOpDataList.clear();
        clipOpPlateList.clear();
        clipBoxList.clear();
    }


    public boolean dropIsPossible(OpData targetOpData){
        if(targetOpData instanceof OpDetail){
            for(OpData op : clipOpDataList){
                if(!DETAIL_OPERATIONS.contains(op.getOpType())) return false;
            }
        } else if (targetOpData instanceof OpAssm) {
            for(OpData op : clipOpDataList){
                if(!ASSM_OPERATIONS.contains(op.getOpType())) return false;
            }
        } else
            return false;
        return true;
    }

    protected void linkMenuToButton(){
        getBtnAddOperation().setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/add.png")),
                44,44, true, true)));
        getBtnAddOperation().setTooltip(new Tooltip("Добавить операцию"));
        getBtnAddOperation().setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY)){
                menu.show(
                        getBtnAddOperation(),
                        Side.LEFT,
                        0.0,
                        32.0);
            }
        });
    };

    /**
     * ДОБАВИТЬ ОПЕРАЦИЮ (MenuForm)
     *
     * Метод добавляет данные "clipOpData" в targetOpData
     * В случае если targetOpData и opData формы совпадают, форма перерисовывается
     */
    public void addOperation(OpData targetOpData) {
        //Определяем список Целевых операций, если Целевой OpData яаляется текущим OpData,
        //то есть добавление в корень открытой оперции, то список Целевых операций = текущему списку,
        //иначе - из Целевой OpData находим Список оперций
        List<OpData> targetOperations = targetOpData.equals(opData) ?
                addedOperations :
                ((IOpWithOperations)targetOpData).getOperations();
        //Перед добавление новых операций снимаем выделение
        if(targetOpData.equals(opData)) {
            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));
            getListViewTechOperations().getSelectionModel().clearSelection();
        }

        if(!dropIsPossible(targetOpData)) return;
        blockUndoListFlag = true;
        //Добавление новых операций производится в цикле
        for (OpData clipOpData : clipOpDataList) {

            int targetIndex = 0; //Индекс позиции, куда происходит добавление clipOpData
            int sourceIndex = clipOpDataList.indexOf(clipOpData); //Индекс clipOpData в списке clip

            if (clipOpData instanceof OpDetail) { //Деталь добавляет после всех деталей
                for (OpData op : targetOperations) {
                    if (op instanceof OpDetail) targetIndex++;
                    else break;
                }
                addToTargetOpDataByIndex(targetOpData, targetOperations, clipOpData, targetIndex);
            } else if (clipOpData instanceof OpAssm) {
                for (OpData op : targetOperations) { //Сборка добавляет после всех деталей и сборок
                    if (op instanceof OpDetail || op instanceof OpAssm) targetIndex++;
                    else break;
                }
                addToTargetOpDataByIndex(targetOpData, targetOperations, clipOpData, targetIndex);
            } else { //Оставшиеся операции добавляются в конец списка
                addToTargetOpDataToTheEndOfList(targetOpData, targetOperations, clipOpData);
            }

        }
        iterateUndoList();
        finishWithPaste(targetOpData);

    }

    /**
     * В методе происходит завершение операции вставки
     * При переносе операций происходит их удаление из источника
     */
    private void finishWithPaste(OpData targetOpData) {
        if(whereFromController == null) return;
        if (!copy) {
            //Если источник совпадает с текущим контроллером
            if(whereFromController.equals(this)) {
                blockUndoListFlag = true;
                for (int i = 0; i < clipOpDataList.size(); i++) {
                    addedPlates.remove(clipOpPlateList.get(i));
                    getListViewTechOperations().getItems().remove(clipBoxList.get(i));
                    addedOperations.remove(clipOpDataList.get(i));
                }
                iterateUndoList();
            }
            //Иначе просто меняем список добавленных операций и закрепляем его в opData
            else {

                for (OpData data : clipOpDataList) {
                    whereFromController.getAddedOperations().remove(data);
                }

            }
        }

        ((IOpWithOperations)whereFromController.getOpData())
                .setOperations(new ArrayList<>(whereFromController.getAddedOperations()));

        countSumNormTimeByShops();
    }

    /**
     * ДОБАВИТЬ ОПЕРАЦИЮ В КОНЕЦ СПИСКА
     * @param targetOpData OpData - Целевая OpData, куда происходит добавление
     * @param targetOperations List<OpData> - Список операций в который добавляются новая операция
     * @param clipOpData OpData - Добавляемая OpData
     */
    private void addToTargetOpDataToTheEndOfList(OpData targetOpData, List<OpData> targetOperations, OpData clipOpData) {
        if(!DUPLICATED_OPERATIONS.contains(clipOpData.getOpType()) &&
                targetOperations.contains(clipOpData)) return;
        //Если целевая операция совпадает с ткущей операцией
        if(targetOpData.equals(opData)){
            //Клонируем копируемый OpData, меняем имя +(копия)
            //К текущему OpData добавляем измененный addedOperations
            OpData addedOpData = SerializationUtils.clone(clipOpData);
            if(copy) renameWithCopy(addedOpData);
            addedOperations.add(addedOpData);
            ((IOpWithOperations)opData).setOperations(new ArrayList<>(addedOperations));
            //Перестраиваем список операций
            rebuildListOfOperations();

            getListViewTechOperations().getSelectionModel().select(addedOperations.size()-1);

        } else
            targetOperations.add(clipOpData);
    }

    /**
     * ДОБАВИТЬ ОПЕРАЦИЮ ПО ИНДЕКСУ В СЕРЕДИНУ СПИСКА
     * @param targetOpData
     * @param targetOperations
     * @param clipOpData
     * @param targetIndex
     */
    private void addToTargetOpDataByIndex(OpData targetOpData, List<OpData> targetOperations, OpData clipOpData, int targetIndex) {
        if(!DUPLICATED_OPERATIONS.contains(clipOpData.getOpType()) &&
                targetOperations.contains(clipOpData)) return;
        //Если целевая операция совпадает с ткущей операцией
        if(targetOpData.equals(opData)){
            //Клонируем копируемый OpData, меняем имя +(копия)
            //К текущему OpData добавляем измененный addedOperations
            OpData addedOpData = SerializationUtils.clone(clipOpData);
            if(copy)  renameWithCopy(addedOpData);
            addedOperations.add(targetIndex, addedOpData);
            ((IOpWithOperations)opData).setOperations(new ArrayList<>(addedOperations));
            //Перестраиваем список операций
            rebuildListOfOperations();

            getListViewTechOperations().getSelectionModel().select(targetIndex);
        } else
            targetOperations.add(targetIndex, clipOpData);
    }

    /**
     * Метод перестраивает список операций
     */
    private void rebuildListOfOperations() {
        //Удаляем все данные, чтобы перестроить список операций
        addedPlates.clear();
        getListViewTechOperations().getItems().clear();
        addedOperations.clear();
        //Строим список заново
        menu.deployData();
    }

    /**
     * Метод добавляет -(копия) в конец наименования при копировании
     */
    private void renameWithCopy(OpData addedOpData) {
        if(addedOpData instanceof OpAssm)
            ((OpAssm)addedOpData).setName(((OpAssm)addedOpData).getName() + "(копия)");
        else if(addedOpData instanceof OpDetail)
            ((OpDetail)addedOpData).setName(((OpDetail)addedOpData).getName() + "(копия)");
    }

    /**
     * ВЫРЕЗАТЬ
     */
    public void cutOperation(Event e){
        copyToClipboard();
        copy = false;
    }

    private void copyToClipboard() {
        clearClipboard();
        List<Integer> selectedIndices = getListViewTechOperations().getSelectionModel().getSelectedIndices();
        for(int index : selectedIndices){
            clipOpDataList.add(getAddedOperations().get(index));
            clipOpPlateList.add(addedPlates.get(index));
            clipBoxList.add(getListViewTechOperations().getItems().get(index));
        }
        whereFromController = this;
    }

    /**
     * КОПИРОВАТЬ (MenuPlate)
     */
    public void copyOperation(Event e){
        copyToClipboard();
        copy = true;
    }

    /**
     * ВСТАВИТЬ (MenuPlate)
     */
    public void pasteOperation(boolean cellIsEmpty) {

        int selectedIndex = getListViewTechOperations().getSelectionModel().getSelectedIndex();
        OpData selectedOpData = cellIsEmpty ? opData : getAddedOperations().get(selectedIndex);

        addOperation(selectedOpData);
    }

    public boolean isPastePossible(boolean cellIsEmpty){
        if(clipOpDataList == null) return false;
        //Определяем целевой узел вставки OpData
        int selectedIndex = getListViewTechOperations().getSelectionModel().getSelectedIndex();
        OpData targetOpData = cellIsEmpty ? getOpData() : getAddedOperations().get(selectedIndex);
        if(!(targetOpData instanceof OpDetail) && !(targetOpData instanceof OpAssm) && !(targetOpData instanceof OpPack)) return false;
        List<EOpType> targetOperations = ((IOpWithOperations)targetOpData).getOperations().stream().map(OpData::getOpType).collect(Collectors.toList());
        //Исключаем автовставку
        if(clipOpDataList.contains(targetOpData)) return false;

        for (OpData op : clipOpDataList) {
            if (targetOpData instanceof OpDetail) {
                if (!DETAIL_OPERATIONS.contains(op.getOpType()))
                    return false;
                if (targetOperations.contains(op.getOpType()) &&
                        !DUPLICATED_OPERATIONS.contains(op.getOpType()))
                    return false;
            } else if (targetOpData instanceof OpAssm) {
                if (!ASSM_OPERATIONS.contains(op.getOpType()))
                    return false;
                if (targetOperations.contains(op.getOpType()) &&
                        !DUPLICATED_OPERATIONS.contains(op.getOpType()))
                    return false;
            } else{
                if (!PACK_OPERATIONS.contains(op.getOpType()))
                    return false;
                if (targetOperations.contains(op.getOpType()) &&
                        !DUPLICATED_OPERATIONS.contains(op.getOpType()))
                    return false;
            }
        }

        return true;
    }

    /**
     * УДАЛИТЬ ВЫДЕЛЕННУЮ ОПЕРАЦИЮ
     */
    public void deleteSelectedOperation(Event e) {
        List<Integer> selectedIndices = getListViewTechOperations().getSelectionModel().getSelectedIndices();
        for(int index : selectedIndices){
            addedOperations.remove(getAddedOperations().get(index));
            getListViewTechOperations().getItems().remove(getListViewTechOperations().getItems().get(index));
            addedPlates.remove(addedPlates.get(index));
        }

        countSumNormTimeByShops();
    }

    /**
     * ОТКРЫТЬ СОХРАНЕННОЕ ИЗДЕЛИЕ
     */
    public void open(Event e, EMenuSource source){
        Stage owner = source.equals(EMenuSource.FORM_MENU) || source.equals(EMenuSource.MAIN_MENU) ?
                (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow():
                (Stage) ((Node)e.getSource()).getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы норм времени (.nvr)", "*.nvr"));
        File initDir = new File(AppProperties.getInstance().getSavesDir());
        chooser.setInitialDirectory(initDir.exists() ? initDir : new File("C:/"));
        File file = chooser.showOpenDialog(owner);;
        if(file == null) return;
        try {
            //Читаем строки из файла
            BufferedReader reader = new BufferedReader(new FileReader(new File(file.toString())));
            ArrayList<String> store = new ArrayList<>();
            String line;
            while((line = reader.readLine())!= null){
                store.add(line);
            }

            //Тип сохраненных данных (ДЕТАЛЬ, СБОРКА, УПАКОВКА)
            opType = store.get(0);

            //Настройки
            String settings = store.get(1);
            Gson gson = new Gson();
            Type settingsType = new TypeToken<ProductSettings>(){}.getType();
            ProductSettings productSettings = gson.fromJson(settings, settingsType);

            //Структура
            String jsonString = store.get(2);
            OpData newOpData = null;
            switch (opType) {
                case "DETAIL": newOpData = (OpDetail) OpDataJsonConverter.convert(jsonString); break;
                case "ASSM"  : newOpData = (OpAssm) OpDataJsonConverter.convert(jsonString); break;
                case "PACK"  : newOpData = (OpPack) OpDataJsonConverter.convert(jsonString); break;
            }

            if(newOpData == null) {
                Warning1.create("Ошибка!",
                        "При конвертации файла произошла ошибка",
                        "Возможно, файл поврежден.");
            } else {

                //Убираем ".nvr" в конце наименования
                ((IOpWithOperations) newOpData).setName(((IOpWithOperations) newOpData).getName().replace(".nvr", ""));

                if (source.equals(EMenuSource.FORM_MENU)) {
                    addFromFile(newOpData);
                } else { //Вызов из меню с пиктограммами
                    clearAll(e);
                    if (opType.equals("ASSM")) {
                        LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + file.getName().replace(".nvr", ""));
                        blockUndoListFlag = true;
                        deployFile(productSettings, newOpData);
                        iterateUndoList();
                    } else {
                        LABEL_PRODUCT_NAME.setText(TITLE_SEPARATOR + "НОВОЕ ИЗДЕЛИЕ");
                        addFromFile(newOpData);
                    }
                }

                countSumNormTimeByShops();
            }
        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * В список операций формы добавляется новая деталь, сборка или упаковка
     * Настройки изделия игнорируются
     */
    private void addFromFile(OpData newOpData) {
        clipOpDataList.add(newOpData);
        whereFromController = null;
        addOperation(opData);
        clearClipboard();
    }

    //Сборка разворачивается в главном окне вместе с настройками изделя
    private void deployFile(ProductSettings productSettings, OpData newOpData) {
        opData = newOpData;
        deployProductSettings(productSettings);
        createMenu();
        menu.deployData();
    }

    /**
     * ОЧИСТИТЬ ВСЕ
     */
    public void clearAll(Event e) {
        blockUndoListFlag = true;
        ((IOpWithOperations)opData).getOperations().clear();
        addedPlates.clear();
        addedOperations.clear();
        getListViewTechOperations().getItems().clear();
        countSumNormTimeByShops();
        PlateDetailController.nameIndex = 0;
        PlateAssmController.nameIndex = 0;
        iterateUndoList();
    }

    /**
     * Применение НАСТРОЕК
     */
    private void deployProductSettings(ProductSettings settings) {
        COLOR_I.setRal(settings.getColor1().getRal());
        COLOR_II.setRal(settings.getColor2().getRal());
        COLOR_III.setRal(settings.getColor3().getRal());

        COLOR_I.setConsumption(settings.getColor1().getConsumption());
        COLOR_II.setConsumption(settings.getColor2().getConsumption());
        COLOR_III.setConsumption(settings.getColor3().getConsumption());
    }

    /**
     * Применение СТРУКТУРЫ
     */
    private void createStructureFromJson(String jsonString) {
        try {
            opData = (OpAssm) OpDataJsonConverter.convert(jsonString);
            createMenu();
            menu.deployData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
