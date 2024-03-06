package ru.wert.normic.controllers.assembling.countings;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpAssmNut;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;

public class OpAssmNutsCounter implements NormCounter {

    public OpAssmNut count(OpAssmNut opData){

        int screws = opData.getScrews(); //Количество винтов
        int vshgs = opData.getVshgs(); //Количество комплектов ВШГ
        int rivets = opData.getRivets(); //Количество заклепок
        int rivetNuts = opData.getRivetNuts(); //Количество аклепочных гаек
        int groundSets = opData.getGroundSets(); //Количество комплектов заземления с этикеткой
        int others = opData.getOthers(); //Количество другого крепежа

        //################################################################

        double time =  screws * SCREWS_SPEED
                + vshgs * VSHGS_SPEED
                + rivets * RIVETS_SPEED * SEC_TO_MIN
                + rivetNuts * RIVET_NUTS_SPEED * SEC_TO_MIN
                + groundSets * GROUND_SETS_SPEED
                + others * OTHERS_SPEED * SEC_TO_MIN;   //мин

        opData.setAssmTime(time);
        return opData;
    }

    @Override
    public OpData count(OpData opdata) {
        return null;
    }
}
