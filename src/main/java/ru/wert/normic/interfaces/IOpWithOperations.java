package ru.wert.normic.interfaces;

import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.dataBaseEntities.ops.OpData;

import java.util.List;

public interface IOpWithOperations extends IOpPlate{

    List<OpData> getOperations();

    void setOperations(List<OpData> operations);

    double getArea();

    void setArea(double area);

    String getName();

    void setName(String name);

    void setDone(boolean val);

    boolean isDone();

    void setOpPlate(AbstractOpPlate opPlate);

    AbstractOpPlate getOpPlate();

//    BooleanProperty getDoneProperty();
}
