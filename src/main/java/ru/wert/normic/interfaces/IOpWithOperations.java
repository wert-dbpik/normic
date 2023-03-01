package ru.wert.normic.interfaces;

import ru.wert.normic.entities.ops.OpData;

import java.util.List;

public interface IOpWithOperations extends IOpPlate{

    List<OpData> getOperations();

    void setOperations(List<OpData> operations);

    double getArea();

    void setArea(double area);
}
