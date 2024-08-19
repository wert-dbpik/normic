package ru.wert.normic.entities.db_connection.othersOps;

import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EPieceMeasurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleOperationService {

    private static SimpleOperationService instance;

    public static SimpleOperationService getInstance() {
        if (instance == null)
            instance =  new SimpleOperationService();
        return instance;
    }



    /**
     * НАЙТИ ВСЕ
     */
    public List<SimpleOperation> getAllSimpleOps(){
        return getAll();
    }

    /**
     * НАЙТИ ПО ID
     */
    public SimpleOperation getSimpleOpById(Long id){
        return getById(id);
    }

    /**
     * СОХРАНИТЬ
     */
    public SimpleOperation save(SimpleOperation newSimpleOperation) {
        return new SimpleOperation();
    }

    /**
     * ОБНОВИТЬ
     */
    public static boolean update(SimpleOperation oldOperation) {
        return true;
    }

    /**
     * УДАЛИТЬ
     */
    public boolean delete(SimpleOperation deletedSimpleOperation) {
        return true;
    }

    //====================================================================================================

    private SimpleOperation getById(Long id){
        for(SimpleOperation op : getAll()){
            if(op.getId().equals(id))
                return op;
        }
        return null;
    }

    private List<SimpleOperation> getAll() {
        return new ArrayList<>(Arrays.asList(
                createOp1(),
                createOp2(),
                createOp3(),
                createOp4()
        ));
    }



    private SimpleOperation createOp1(){
        SimpleOperation op = new SimpleOperation();
        op.setId(1L);
        op.setName("Изготовление заземляющего кабеля");
        op.setMeasurement(EPieceMeasurement.PIECE);
        op.setNormType(ENormType.NORM_ASSEMBLE);
        op.setJobType(EJobType.JOB_NONE);
        op.setNorm(2); //2 мин на операцию
        op.setDescription("Изготовление заземляющего кабеля включает  в себя:\n" +
                "\t\t1) отрезание кабеля необходимой длины\n" +
                "\t\t2) зачистку концов\n" +
                "\t\t3) обжимку концов соответствующими наконечниками");

        return op;
    }

    private SimpleOperation createOp2(){
        SimpleOperation op = new SimpleOperation();
        op.setId(2L);
        op.setName("Зачистка медной шины");
        op.setMeasurement(EPieceMeasurement.METER);
        op.setNormType(ENormType.NORM_ASSEMBLE);
        op.setJobType(EJobType.JOB_NONE);
        op.setNorm(1); //1 мин на 1 метр
        op.setDescription("");

        return op;
    }

    private SimpleOperation createOp3(){
        SimpleOperation op = new SimpleOperation();
        op.setId(3L);
        op.setName("Зачистка медного листа");
        op.setMeasurement(EPieceMeasurement.SQUARE_METER);
        op.setNormType(ENormType.NORM_ASSEMBLE);
        op.setJobType(EJobType.JOB_NONE);
        op.setNorm(3);
        op.setDescription("");

        return op;
    }

    private SimpleOperation createOp4(){
        SimpleOperation op = new SimpleOperation();
        op.setId(4L);
        op.setName("Укладка паролона");
        op.setMeasurement(EPieceMeasurement.CUBE_METER);
        op.setNormType(ENormType.NORM_ASSEMBLE);
        op.setJobType(EJobType.JOB_NONE);
        op.setNorm(20);
        op.setDescription("");

        return op;
    }


}
