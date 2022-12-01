package ru.wert.normik.interfaces;

import javafx.scene.control.TextField;
import ru.wert.normik.entities.OpData;

/**
 * Интерфейс контроллеров FormDetailController и FormAssmController
 * Эти формы вызываются из плашек ДЕТАЛЬ И СБОРКА соответствующе,
 * либо как самостоятельные формы из меню программы
 */
public interface IForm {

    void init(IFormController controller, TextField tfName, OpData opData);
}
