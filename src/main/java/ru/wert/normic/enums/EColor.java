package ru.wert.normic.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.*;

public enum EColor{

    COLOR_NO("б/п", "", 0),
    COLOR_I("I", "RAL7035", 150),
    COLOR_II("II", "RAL9006", 300),
    COLOR_III("III", "черный", 200);

    @Getter@Setter String name;
    @Getter@Setter String ral;
    @Getter@Setter int consumption;

    EColor(String name, String ral, int consumption) {
        this.name = name;
        this.ral = ral;
        this.consumption = consumption;
    }

//    public static double getConsumption(EColor color){
//        switch(color){
//            case COLOR_NO: return 0.0;
//            case COLOR_I: return CONSUMPTION_I * 0.001;
//            case COLOR_II: return CONSUMPTION_II * 0.001;
//            case COLOR_III: return CONSUMPTION_III * 0.001;
//            default: throw new NoSuchElementException(String.format("Not found such EColor in the list %s", color.getName()));
//        }
//    }
}
