package ru.wert.normic.entities;

public interface PackingData {

    double getCartoon();
    void setCartoon(double cartoon);

    double getStretchWrap();
    void setStretchWrap(double stretchWrap);

    double getBubbleWrap();
    void setBubbleWrap(double bubbleWrap);

    double getPolyWrap();
    void setPolyWrap(double polyWrap);

    double getDuctTape();
    void setDuctTape(double ductTape);

}
