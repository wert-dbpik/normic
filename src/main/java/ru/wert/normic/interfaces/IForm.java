package ru.wert.normic.interfaces;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import ru.wert.normic.components.ImgDone;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.entities.ops.OpData;

/**
 * Интерфейс контроллеров FormDetailController и FormAssmController
 * Эти формы вызываются из плашек ДЕТАЛЬ И СБОРКА соответствующе,
 * либо как самостоятельные формы из меню программы
 */
public interface IForm {

    void init(AbstractFormController controller, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone);
}
