package ru.wert.normic.excel.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static ru.wert.normic.excel.model.enums.EColName.*;


@Getter
@Setter
public class EditorRow {

    private String color = "";
    private String rowNumber = "";
    private String krp = "";
    private String decNumber = "";
    private String name = "";
    private String lacquer = "";
    private String coat = "";
    private String zcp = "";
    private ArrayList<Execution> executions;
    private String folder = "";
    private String material = "";
    private String paramA = "";
    private String paramB = "";

    public EditorRow(){
    }

    @Getter
    @Setter
    public static class Execution{
        String id;
        String totalAmount = "";
        String amountPerAssemble = "";

        public Execution(String id, String totalAmount, String amountPerAssemble){
            Execution.this.id = id;
            Execution.this.totalAmount = totalAmount;
            Execution.this.amountPerAssemble = amountPerAssemble;
        }
    }

    /**
     * Метод определяет порядковый номер исполнения по id исполнения
     * @param exId
     * @return
     */
    private Execution findExecutionById(String exId) {
        for(Execution ex: executions){
            if(ex.getId().equals(exId))
                return ex;
        }

        throw new NoSuchElementException("There is no execution with id = " + exId);
    }

    /**
     * Быстрая замена значения в строке
     * @param exId Порядковый номер исполнения
     * @param field
     * @param val
     */
    public void changeItemValue(String exId, String field,  String val){
        if(exId != null){
            Execution ex = findExecutionById(exId);
            if(getEColNameByColText(field).equals(TOTAL_AMOUNT)) ex.setTotalAmount(val);
                else if(getEColNameByColText(field).equals(AMOUNT)) ex.setAmountPerAssemble(val);
                else throw new NoSuchElementException("There is no match for field = " + field);
        } else {
            switch(getEColNameByColText(field)){
               case ECOLOR:     setColor(val); break;
               case ROW_NUM:    setRowNumber(val); break;
               case KRP:        setKrp(val); break;
               case DEC_NUM:    setDecNumber(val); break;
               case NAME:       setName(val); break;
               case LACQUER:    setLacquer(val); break;
               case COAT:       setCoat(val); break;
               case ZCP:        setZcp(val); break;
               case FOLDER:     setFolder(val); break;
               case MATERIAL:   setMaterial(val); break;
               case A:          setParamA(val); break;
               case B:          setParamB(val); break;
               default: throw new NoSuchElementException("There is no match for field = " + field);
            }
        }
    }

}


