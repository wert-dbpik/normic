package ru.wert.normic.interfaces;

import ru.wert.normic.entities.OpData;

import java.util.List;

public interface IOpWithOperations {

    List<OpData> getOperations();

    void setOperations(List<OpData> operations);

    double getArea();

    void setArea(double area);
}
