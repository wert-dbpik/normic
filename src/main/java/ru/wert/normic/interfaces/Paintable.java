package ru.wert.normic.interfaces;

import ru.wert.normic.entities.ops.single.OpAssm;

public interface Paintable {

    void setPainter(OpAssm painter);

    OpAssm getPainter();

}
