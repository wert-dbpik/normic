package ru.wert.normic.controllers.forms;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.ImageCursor;
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
import ru.wert.normic.controllers.PlateAssmController;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.interfaces.IForm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;

import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.*;


public abstract class AbstractFormController implements IForm {

    //Коллекции переносимых элементов
    //При переносе какого-либо узла переносится одновременно соответственно элементу OpData = AbstractOpPlate = VBox
    public static List<OpData> clipOpDataList = new ArrayList<>(); //Коллекция переносимых операций
    public static List<AbstractOpPlate> clipOpPlateList = new ArrayList<>(); //Коллекция переносимых плашек
    public static List<VBox> clipBoxList = new ArrayList<>(); //Колеекция переносимых Бксов
    public static boolean copy; //true - (КОПИРОВАТЬ) переносимые элементы не удаляются, false - (ВЫРЕЗАТЬ) удаляются
    //------------------------

    protected MenuCalculator menu;
    @Getter protected OpData opData;
    @Getter protected ObservableList<AbstractOpPlate> addedPlates;
    @Getter protected List<OpData> addedOperations;

    @Getter protected DoubleProperty formAreaProperty = new SimpleDoubleProperty(0.0);

    private final ObjectProperty<ListCell<VBox>> dragSource = new SimpleObjectProperty<>();
    private final Image imageCopy = new Image(String.valueOf(getClass().getResource("/pics/btns/cursor_copy.png")),
            32, 32, true, true);
    private final Image imageCut = new Image(String.valueOf(getClass().getResource("/pics/btns/cursor_cut.png")),
            32, 32, true, true);

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private Button btnAddOperation;

    public abstract void countSumNormTimeByShops();

    public abstract void createMenu();

    public abstract void fillOpData();

    ImageCursor copyCursor;
    ImageCursor cutCursor;

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

        listViewTechOperations.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewTechOperations.setCellFactory(new Callback<ListView<VBox>, ListCell<VBox>>() {
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
                        List<Integer> indices = listViewTechOperations.getSelectionModel().getSelectedIndices();
                        if(indices.isEmpty()) return;
                        for(int index : indices){
                            clipOpDataList.add(addedOperations.get(index)); //
                            clipOpPlateList.add(addedPlates.get(index));
                            clipBoxList.add(listViewTechOperations.getItems().get(index));
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
                        listViewTechOperations.getSelectionModel().clearSelection();
                        listViewTechOperations.getSelectionModel().select(cell.getItem());

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

                cell.setOnDragDone(ev -> {
                    if (ev.isAccepted()) {
                        if (!copy) {
                            for (int i = 0; i < clipOpDataList.size(); i++) {
                                addedPlates.remove(clipOpPlateList.get(i));
                                listViewTechOperations.getItems().remove(clipBoxList.get(i));
                                addedOperations.remove(clipOpDataList.get(i));
                            }

                        }

                        ((IOpWithOperations) opData).setOperations(addedOperations);

                        countSumNormTimeByShops();
                    }
                    clipOpDataList.clear();
                    clipOpPlateList.clear();
                    clipBoxList.clear();
                    copy = false;

                    ev.consume();
                });


                cell.setOnDragDropped(e -> {
                    if(cell.getItem() != null) {
                        OpData targetOpData = addedOperations.get(cell.getIndex());
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
                return cell;
            }
        });

        listViewTechOperations.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.SECONDARY)){

            }

        });
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

    protected void tyeMenuToButton(){
        btnAddOperation.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/add.png")),
                44,44, true, true)));
        btnAddOperation.setTooltip(new Tooltip("Добавить операцию"));
        btnAddOperation.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY)){
                menu.show(
                        btnAddOperation,
                        Side.LEFT,
                        0.0,
                        32.0);
            }
        });
    };

    /**
     * Метод добавляет данные "clipOpData" в targetOpData
     * В случае если targetOpData и opData формы совпадают, приходится проходить весь цикл добавления новой операции
     * и не ограничиваться только добавлением clipOpData
     */
    public void addOperation(OpData targetOpData) {
        List<OpData> targetOperations = targetOpData.equals(opData) ?
                addedOperations :
                ((IOpWithOperations)targetOpData).getOperations();
        //Перед добавление новых операций снимаем выделение
        if(targetOpData.equals(opData)) {
            ((IOpWithOperations) opData).setOperations(new ArrayList<>(addedOperations));
            listViewTechOperations.getSelectionModel().clearSelection();
        }

        for(OpData clipOpData : clipOpDataList) {

            int targetIndex = 0; //Индекс позиции, куда происходит добавление clipOpData
            int sourceIndex = clipOpDataList.indexOf(clipOpData); //Индекс clipOpData в списке clip

            if (clipOpData instanceof OpDetail) { //Деталь добавляет после всех деталей
                for (OpData op : targetOperations) {
                    if (op instanceof OpDetail) targetIndex++;
                    else break;
                }
                addToTargetOpDataByIndex(targetOpData, targetOperations, clipOpData, targetIndex, sourceIndex);
            } else if (clipOpData instanceof OpAssm) {
                for (OpData op : targetOperations) { //Сборка добавляет после всех деталей и сборок
                    if (op instanceof OpDetail || op instanceof OpAssm) targetIndex++;
                    else break;
                }
                addToTargetOpDataByIndex(targetOpData, targetOperations, clipOpData, targetIndex, sourceIndex);
            } else { //Оставшиеся операции добавляются в конец списка
                addToTargetOpDataToTheEndOfList(targetOpData, targetOperations, clipOpData, sourceIndex);
            }
        }

    }

    private void addToTargetOpDataToTheEndOfList(OpData targetOpData, List<OpData> targetOperations, OpData clipOpData, int sourceIndex) {
        if(targetOpData.equals(opData)){
            targetOperations.add(opData);

            addedPlates.add(clipOpPlateList.get(sourceIndex));
            listViewTechOperations.getItems().add(clipBoxList.get(sourceIndex));
            listViewTechOperations.getSelectionModel().select(clipBoxList.get(sourceIndex));
        } else
            targetOperations.add(clipOpData);
    }

    private void addToTargetOpDataByIndex(OpData targetOpData, List<OpData> targetOperations, OpData clipOpData, int targetIndex, int sourceIndex) {
        if(targetOpData.equals(opData)){
            OpData addedOpData = SerializationUtils.clone(opData);
            targetOperations.add(targetIndex, addedOpData);
//            ((IOpWithOperations) opData).setOperations(targetOperations);
//            addedPlates.add(targetIndex, clipOpPlateList.get(sourceIndex));
//            listViewTechOperations.getItems().add(targetIndex, clipBoxList.get(sourceIndex));
//            listViewTechOperations.getSelectionModel().select(targetIndex);
        } else
            targetOperations.add(targetIndex, clipOpData);
    }

}
