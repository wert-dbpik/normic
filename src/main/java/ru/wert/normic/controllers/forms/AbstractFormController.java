package ru.wert.normic.controllers.forms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IForm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.*;


public abstract class AbstractFormController implements IForm {

    public static List<OpData> clipOpDataList = new ArrayList<>();
    public static List<AbstractOpPlate> clipOpPlateList = new ArrayList<>();
    public static List<VBox> clipBoxList = new ArrayList<>();
    public static boolean copy;
    private ClipboardContent cc;

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

    protected void setCell(){
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

                        cc = new ClipboardContent();
                        List<Integer> indices = listViewTechOperations.getSelectionModel().getSelectedIndices();
                        if(indices.isEmpty()) return;
                        for(int index : indices){
                            clipOpDataList.add(addedOperations.get(index));
                            clipOpPlateList.add(addedPlates.get(index));
                            clipBoxList.add(listViewTechOperations.getItems().get(index));
                        }
                        copy = e.isControlDown();

//                        db.setDragView(cell.getItem().snapshot(null, null));
                        cc.putString("data");
                        db.setContent(cc);
                        e.consume();
                    }
                });

                cell.setOnDragOver(e -> {
                    listViewTechOperations.getSelectionModel().clearSelection();
                    listViewTechOperations.getSelectionModel().select(cell.getItem());

                    Dragboard db = e.getDragboard();
                    copy = KEYS_NOW_PRESSED.contains(KeyCode.CONTROL);

                    OpData targetOpData = addedOperations.get(cell.getIndex());
                    if(db.hasString() && dropIsPossible(targetOpData)) {
                        e.acceptTransferModes(TransferMode.MOVE);
                    } else {
                        e.acceptTransferModes(TransferMode.NONE);

                    }

                    e.consume();
                });

                cell.setOnDragDone(e -> {
                    if(!copy) {
                        for(int i = 0; i < clipOpDataList.size(); i++){
                            addedOperations.remove(clipOpDataList.get(i));
                            addedPlates.remove(clipOpPlateList.get(i));
                            listViewTechOperations.getItems().remove(clipBoxList.get(i));
                        }
                    }

                    clipOpDataList.clear();
                    clipOpPlateList.clear();
                    clipBoxList.clear();
                    copy = false;

                    countSumNormTimeByShops();
                    e.consume();
                });

                cell.setOnDragDropped(e -> {
                    Dragboard db = e.getDragboard();
                    OpData targetOpData = addedOperations.get(cell.getIndex());
                    if (e.getTransferMode().equals(TransferMode.MOVE) && !clipOpDataList.contains(targetOpData)) {
                        addOperation(addedOperations.get(cell.getIndex()));
                        e.setDropCompleted(true);
                    } else {
                        e.setDropCompleted(false);

                    }
                });
                return cell;
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

    public void addOperation(OpData targetOpData) {
        List<OpData> targetOperations = ((IOpWithOperations)targetOpData).getOperations();

        for(OpData clipOpData : clipOpDataList) {
            int index = 0;
            if (clipOpData instanceof OpDetail) {
                for (OpData op : targetOperations) {
                    if (op instanceof OpDetail) index++;
                    else break;
                }
                targetOperations.add(index, clipOpData);

            } else if (clipOpData instanceof OpAssm) {
                for (OpData op : targetOperations) {
                    if (op instanceof OpDetail || op instanceof OpAssm) index++;
                    else break;
                }
                targetOperations.add(index, clipOpData);
            } else {
                targetOperations.add(clipOpData);
            }
        }

        countSumNormTimeByShops();
    }

}
