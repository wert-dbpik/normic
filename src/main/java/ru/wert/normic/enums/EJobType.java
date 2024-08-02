package ru.wert.normic.enums;

import lombok.Getter;

/**
 * Описывает выиды работ по участкам
 */
public enum EJobType {

    JOB_NONE("Нет"),       //НИКАКИЕ
    JOB_CUTTING("Резка лазером"),    //Резание лазером
    JOB_BENDING("Гибка"),    //Гибочные
    JOB_WELDING("Сварка"),    //Сварочные
    JOB_LOCKSMITH("Слесарка"),  //Слесарные
    JOB_MECHANIC("Мехобработка");    //Механические

    @Getter
    private String jobName;

    EJobType(String jobName) {
        this.jobName = jobName;
    }
}
