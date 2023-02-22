package ru.wert.normic.enums;

import lombok.Getter;

public enum EPacks {

    CARTOON         ("Гофрокартон 1050х2000................", "м2"),
    STRETCH_MACHINE ("Пленка-стрейч (машинная).............", "м"),
    STRETCH_HAND    ("Пленка-стрейч 35 мкм (ручная)........", "м"),
    POLY            ("Лента полипропиленовая 12х0,6........", "м"),
    BUBBLE          ("Пленка воздушно-пузырчатая...........", "м"),
    DUCT            ("Скотч 72х66 Unibob...................", "шт"),
    PALLET          ("Европоддон деревянный 1200х800х150...", "шт");


    @Getter String name;
    @Getter String measuring;

    EPacks(String name, String measuring) {
        this.name = name;
        this.measuring = measuring;
    }
}
