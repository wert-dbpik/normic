package ru.wert.normic.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public enum EColor implements Serializable {

    COLOR_NO("б/п", "", 0),
    COLOR_I("I", "RAL7035", 150),
    COLOR_II("II", "RAL9006", 300),
    COLOR_III("III", "черный", 200);

    @SerializedName("${name}")
    @Getter@Setter String name;

    @SerializedName("${ral}")
    @Getter@Setter String ral;

    @SerializedName("${consumption}")
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
