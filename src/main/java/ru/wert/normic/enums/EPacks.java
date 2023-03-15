package ru.wert.normic.enums;

import lombok.Getter;

public enum EPacks {

    CARTOON         ("Гофрокартон 1050х2000\t\t\t\t", "м.кв."),
    CARTOON_ANGLE   ("Картонный уголок (1350 мм)\t\t\t", "м"),
    STRETCH_MACHINE ("Пленка-стрейч (машинная)\t\t\t\t", "м"),
    STRETCH_HAND    ("Пленка-стрейч 35 мкм (ручная)\t\t\t", "м"),
    POLY            ("Лента полипропиленовая 12х0,6\t\t\t", "м"),
    BUBBLE          ("Пленка воздушно-пузырчатая\t\t\t", "м"),
    DUCT            ("Скотч 72х66 Unibob\t\t\t\t\t", "шт"),
    PALLET          ("Европоддон деревянный 1200х800х150\t", "шт");


    @Getter String name;
    @Getter String measuring;

    EPacks(String name, String measuring) {
        this.name = name;
        this.measuring = measuring;
    }
}
