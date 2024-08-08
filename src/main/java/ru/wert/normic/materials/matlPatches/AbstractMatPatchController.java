package ru.wert.normic.materials.matlPatches;

import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.dataBaseEntities.ops.single.OpDetail;

import java.util.List;

/**
 * Абстрактная модель панельки материалов
 */
public abstract class AbstractMatPatchController {

    @Getter@Setter protected double ro; //Плотность
    @Getter@Setter protected double t; //Толщина
    @Getter@Setter protected double wasteRatio; //Коэффициент, учитывающий отход материала
    @Getter@Setter protected int paramA; //параметр А
    @Getter@Setter protected int paramB; //параметр B
    @Getter@Setter protected int paramC; //параметр B

    public abstract TextField getTfA();
    public abstract TextField getTfB();
//    public abstract TextField getTfC();
    public abstract TextField getTfWasteRatio();
    public abstract TextField getTfCoat();
    public abstract TextField getTfWeight();
    public abstract void fillPatchOpData();

    protected FormDetailController detailController;
    protected List<AbstractOpPlate> addedPlates;
    protected OpDetail opData;

    public abstract void countWeightAndArea();

    public void init(OpDetail opData, FormDetailController detailController, List<AbstractOpPlate> addedPlates){
        this.addedPlates = addedPlates;
        this.detailController = detailController;
        this.opData = opData;

        getTfA().textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        getTfB().textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });


        getTfWasteRatio().textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

    };


}
