package ru.wert.normic.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public enum EColor implements Serializable {

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

}
