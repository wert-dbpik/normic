package ru.wert.normic.entities.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppColor {

    private String colorName;
    private String ral;
    private int consumption;

}
