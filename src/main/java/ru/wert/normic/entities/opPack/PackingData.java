package ru.wert.normic.entities.opPack;

public interface PackingData {

    double getCartoon();
    void setCartoon(double cartoon);

    double getStretchMachineWrap();
    void setStretchMachineWrap(double stretchMachineWrap);

    double getStretchHandWrap();
    void setStretchHandWrap(double stretchHandWrap);

    double getBubbleWrap();
    void setBubbleWrap(double bubbleWrap);

    double getPolyWrap();
    void setPolyWrap(double polyWrap);

    double getDuctTape();
    void setDuctTape(double ductTape);

    double getPallet();
    void setPallet(double pallet);

}
