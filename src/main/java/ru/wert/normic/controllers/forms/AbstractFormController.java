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
import ru.wert.normic.settings.ProductSettings;

import java.lang.reflect.Type;
import java.util.List;

import static ru.wert.normic.AppStatics.KEYS_NOW_PRESSED;


public abstract class AbstractFormController implements IForm {

    public static OpData clipOpData;
    public static AbstractOpPlate clipOpPlate;
    public static VBox clipBox;
    public static boolean copy;

    protected MenuCalculator menu;
    @Getter protected OpData opData;
    @Getter protected ObservableList<AbstractOpPlate> addedPlates;
    @Getter protected List<OpData> addedOperations;

    @Getter protected DoubleProperty formAreaProperty = new SimpleDoubleProperty(0.0);

    private final ObjectProperty<ListCell<VBox>> dragSource = new SimpleObjectProperty<>();
    private final Image imageCopy = new Image(String.valueOf(getClass().getResource("/pics/btns/copy.png")), 32, 32, true, true);
    private final Image imageErase = new Image(String.valueOf(getClass().getResource("/pics/btns/erase.png")), 32, 32, true, true);

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private Button btnAddOperation;

    public abstract void countSumNormTimeByShops();

    public abstract void fillOpData();

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
                    if (! cell.isEmpty()) {
                        Dragboard db = cell.startDragAndDrop(TransferMode.ANY);
                        ClipboardContent cc = new ClipboardContent();
                        clipOpData = addedOperations.get(cell.getIndex());
                        clipOpPlate = addedPlates.get(cell.getIndex());
                        clipBox = cell.getItem();
                        copy = e.isControlDown();
                        cc.putString(clipOpData.getOpType().name());
                        db.setContent(cc);
                        db.setDragView(copy ? imageCopy : imageErase);
                    }
                });

                cell.setOnDragOver(e-> {
                    listViewTechOperations.getSelectionModel().clearSelection();
                    listViewTechOperations.getSelectionModel().select(cell.getItem());
                    Dragboard db = e.getDragboard();
                    copy = KEYS_NOW_PRESSED.contains(KeyCode.CONTROL);
                    db.setDragView(copy ? imageCopy : imageErase);

//                    if (db.hasString() && //
//                            !event.getSource().equals(cell) &&  //перенос не происходит сам в себя
//                            dropIsPossible(db.getString())) {  //Если вставка возможна
                        e.acceptTransferModes(TransferMode.MOVE);

//                    }
                });

                cell.setOnDragDone(event -> {
                    if(!copy) {
                        addedOperations.remove(clipOpData);
                        addedPlates.remove(clipOpPlate);
                        listViewTechOperations.getItems().remove(clipBox);
                    }

                    clipOpData = null;
                    clipOpPlate = null;
                    clipBox = null;
                    copy = false;

                    countSumNormTimeByShops();
                });

                cell.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    if (db.hasString()) {
                        addOperation(addedOperations.get(cell.getIndex()));
                        event.setDropCompleted(true);
                    } else {
                        event.setDropCompleted(false);
                    }
                });

                return cell;
            }

            boolean dropIsPossible(String json){
                Gson gson = new Gson();
                Type opDataType = new TypeToken<OpData>(){}.getType();
                OpData opData = gson.fromJson(json, opDataType);
                return opData.getOpType().equals(EOpType.ASSM) ||
                        opData.getOpType().equals(EOpType.DETAIL);
            }
        });
    }

    protected void tyeMenuToButton(){
        btnAddOperation.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/add.png")), 44,44, true, true)));
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
        int index = 0;
        if (clipOpData instanceof OpDetail) {
            for (OpData op : targetOperations) {
                if (op instanceof OpDetail) index++;
                else break;
            }
            targetOperations.add(index, clipOpData);

        }
        else if(clipOpData instanceof OpAssm){
            for (OpData op : targetOperations) {
                if (op instanceof OpDetail || op instanceof OpAssm) index++;
                else break;
            }
            targetOperations.add(index, clipOpData);
        }

        else {
            targetOperations.add(clipOpData);
        }

        countSumNormTimeByShops();
    }

}
