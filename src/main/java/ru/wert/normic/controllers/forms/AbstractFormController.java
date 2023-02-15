package ru.wert.normic.controllers.forms;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.geometry.Side;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import lombok.Getter;
import org.apache.commons.lang3.SerializationUtils;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.interfaces.IForm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;
import ru.wert.normic.menus.MenuPlate;

import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.*;


public abstract class AbstractFormController implements IForm {

    //Коллекции переносимых элементов
    //При переносе какого-либо узла переносится одновременно соответственно элементу OpData = AbstractOpPlate = VBox
    public static AbstractFormController whereFromController;
    public static List<OpData> clipOpDataList = new ArrayList<>(); //Коллекция переносимых операций
    public static List<AbstractOpPlate> clipOpPlateList = new ArrayList<>(); //Коллекция переносимых плашек
    public static List<VBox> clipBoxList = new ArrayList<>(); //Колеекция переносимых Бксов
    public static boolean copy; //true - (КОПИРОВАТЬ) переносимые элементы не удаляются, false - (ВЫРЕЗАТЬ) удаляются
    //------------------------

    protected MenuCalculator menu;
    @Getter protected OpData opData;
    @Getter protected List<AbstractOpPlate> addedPlates = new ArrayList<>();
    @Getter protected List<OpData> addedOperations = new ArrayList<>();

    @Getter protected DoubleProperty formAreaProperty = new SimpleDoubleProperty(0.0);

    private final ObjectProperty<ListCell<VBox>> dragSource = new SimpleObjectProperty<>();
    private final Image imageCopy = new Image(String.valueOf(getClass().getResource("/pics/btns/cursor_copy.png")),
            32, 32, true, true);
    private final Image imageCut = new Image(String.valueOf(getClass().getResource("/pics/btns/cursor_cut.png")),
            32, 32, true, true);


    public abstract void countSumNormTimeByShops();

    public abstract void createMenu();

    public abstract void fillOpData();

    public abstract ListView<VBox> getListViewTechOperations();
    public abstract Button getBtnAddOperation();


    ImageCursor copyCursor;
    ImageCursor cutCursor;

    public AbstractFormController() {
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
        copyCursor = new ImageCursor(imageCopy);
        cutCursor = new ImageCursor(imageCut);

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

//                cell.setOnDragDone(ev -> {
//                    if (ev.isAccepted()) {
//                        finishWithPaste(opData);
//                    }
//
//                    ev.consume();
//                });


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
                    if (e.getButton().equals(MouseButton.SECONDARY)) {
                        boolean cellIsEmpty = cell.isEmpty();
                        new MenuPlate().create(getThisController(), cellIsEmpty).show(
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
                new MenuPlate().create(getThisController(), true).show(
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
                if(RESTRICTED_FOR_DETAILS.contains(op.getOpType())) return false;
            }
        } else if (targetOpData instanceof OpAssm) {
            for(OpData op : clipOpDataList){
                if(RESTRICTED_FOR_ASSM.contains(op.getOpType())) return false;
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
     * ДОБАВИТЬ ОПЕРАЦИЮ (MenuCalculator)
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

        finishWithPaste(targetOpData);

    }

    /**
     * В методе происходит завершение операции вставки
     * При переносе операций происходит их удаление из источника
     */
    private void finishWithPaste(OpData targetOpData) {
        if (!copy) {
            //Если источник совпадает с текущим контроллером
            if(whereFromController.equals(this)) {
                for (int i = 0; i < clipOpDataList.size(); i++) {
                    addedPlates.remove(clipOpPlateList.get(i));
                    getListViewTechOperations().getItems().remove(clipBoxList.get(i));
                    addedOperations.remove(clipOpDataList.get(i));
                }
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

//        clipOpDataList.clear();
//        clipOpPlateList.clear();
//        clipBoxList.clear();
//        copy = false;
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
        clearClipboard();
        List<Integer> selectedIndices = getListViewTechOperations().getSelectionModel().getSelectedIndices();
        for(int index : selectedIndices){
            clipOpDataList.add(getAddedOperations().get(index));
            clipOpPlateList.add(addedPlates.get(index));
            clipBoxList.add(getListViewTechOperations().getItems().get(index));
        }
        whereFromController = this;
        copy = false;
    }

    /**
     * КОПИРОВАТЬ (MenuPlate)
     */
    public void copyOperation(Event e){
        clearClipboard();
        List<Integer> selectedIndices = getListViewTechOperations().getSelectionModel().getSelectedIndices();
        for(int index : selectedIndices){
            clipOpDataList.add(getAddedOperations().get(index));
            clipOpPlateList.add(addedPlates.get(index));
            clipBoxList.add(getListViewTechOperations().getItems().get(index));
        }
        whereFromController = this;
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
        //Исключаем автовставку
        if(clipOpDataList.contains(targetOpData)) return false;

        for(OpData op : clipOpDataList){
            if (targetOpData instanceof OpDetail) {
                if (RESTRICTED_FOR_DETAILS.contains(op.getOpType())) return false;
                if (getAddedOperations().contains(op) && !DUPLICATED_OPERATIONS.contains(op.getOpType())) return false;
            } else if (targetOpData instanceof OpAssm) {
                if (RESTRICTED_FOR_ASSM.contains(op.getOpType())) return false;
                if (getAddedOperations().contains(op) && !DUPLICATED_OPERATIONS.contains(op.getOpType())) return false;
            }
        }

        return true;
    }



    public void deleteSelectedOperation(Event e) {
        List<Integer> selectedIndices = getListViewTechOperations().getSelectionModel().getSelectedIndices();
        for(int index : selectedIndices){
            addedOperations.remove(getAddedOperations().get(index));
            getListViewTechOperations().getItems().remove(getListViewTechOperations().getItems().get(index));
            addedPlates.remove(addedPlates.get(index));
        }

        countSumNormTimeByShops();
    }

}
