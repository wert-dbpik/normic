package ru.wert.normic.entities.extra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
public class AppColor {

    private String colorName;
    private String ral;
    private int consumption;

}
