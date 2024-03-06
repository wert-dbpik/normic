package ru.wert.normic.controllers.assembling.countings;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpAssmNode;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.settings.NormConstants.*;

public class OpAssmNodeCounter implements NormCounter {

    public OpData count(OpData data){
        OpAssmNode opData = (OpAssmNode)data;

        int postLocks = opData.getPostLocks(); //Количество почтовых замков
        int doubleLocks = opData.getDoubleLocks(); //Количество замков с рычагами
        int mirrors = opData.getMirrors(); //Количество стекол
        int detectors = opData.getDetectors(); //Количество извещателей ИО-102
        int connectionBoxes = opData.getConnectionBoxes(); //Количество еоробок соединительных КС-4

        //######################################################################

        double time =  postLocks * POST_LOCKS_SPEED
                + doubleLocks * DOUBLE_LOCKS_SPEED
                + mirrors * GLASS_SPEED
                + detectors * DETECTORS_SPEED
                + connectionBoxes * CONNECTION_BOXES_SPEED;   //мин


        opData.setAssmTime(time);
        return opData;
    }

}
