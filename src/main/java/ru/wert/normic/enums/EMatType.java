package ru.wert.normic.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

public enum EMatType {
    /**
     * НАЗВАНИЯ НЕ МЕНЯТЬ !!!!!!
     */

    LIST("Листовой", "/fxml/materials/material_list.fxml"),
    ROUND("Круглый", "/fxml/materials/material_round.fxml"),
    PROFILE("Профильный", "/fxml/materials/material_profile.fxml"),
    PIECE("Штучный", "/fxml/materials/material_piece.fxml");

    @Getter private String matTypeName;
    @Getter private String path;

    EMatType(String matTypeName, String path) {
        this.matTypeName = matTypeName;
        this.path = path;
    }

    public static EMatType getTypeByName(String name){
        for(EMatType type : values()){
            if(type.getMatTypeName().equals(name))
                return type;
        }
        throw new NoSuchElementException(String.format("Тип материала %s не найден!", name));
    }

    public static String getPathByName(String name){
        for(EMatType type : values()){
            if(type.getMatTypeName().equals(name))
                return type.getPath();
        }
        throw new NoSuchElementException(String.format("Тип материала '%s' не найден!", name));
    }

}
