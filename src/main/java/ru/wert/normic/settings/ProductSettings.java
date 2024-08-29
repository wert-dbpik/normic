package ru.wert.normic.settings;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.colors.AppColor;

@Getter
@Setter
public class ProductSettings {

    private boolean batchness; //Использование партийности
    private Integer batch; //Расчет на партию
    private AppColor color1;
    private AppColor color2;
    private AppColor color3;


}
