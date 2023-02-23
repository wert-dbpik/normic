package ru.wert.normic.enums;

import lombok.Getter;

public enum EPacks {

    CARTOON         ("Гофрокартон 1050х2000_____________________", "м2"),
    STRETCH_MACHINE ("Пленка-стрейч (машинная)__________________", "м"),
    STRETCH_HAND    ("Пленка-стрейч 35 мкм (ручная)_____________", "м"),
    POLY            ("Лента полипропиленовая 12х0,6____________", "м"),
    BUBBLE          ("Пленка воздушно-пузырчатая_______________", "м"),
    DUCT            ("Скотч 72х66 Unibob____________________________", "шт"),
    PALLET          ("Европоддон деревянный 1200х800х150____", "шт");


    @Getter String name;
    @Getter String measuring;

    EPacks(String name, String measuring) {
        this.name = name;
        this.measuring = measuring;
    }
}
