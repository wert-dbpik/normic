package ru.wert.normic.entities.db_connection.simpleOperations;


import ru.wert.normic.entities.db_connection.ItemService;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EPieceMeasurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SimpleOperationTestService implements ISimpleOperationsService, ItemService<SimpleOperation>{


    /**
     * НАЙТИ ПО ID
     */
    @Override
    public SimpleOperation findById(Long id) {
        for(SimpleOperation op : findAll()){
            if(op.getId().equals(id))
                return op;
        }
        return null;
    }

    /**
     * НАЙТИ ВСЕ
     */
    @Override
    public List<SimpleOperation> findAll() {
        return new ArrayList<>(Arrays.asList(
                createOp1(),
                createOp2(),
                createOp3(),
                createOp4()
        ));
    }

    @Override
    public SimpleOperation findByName(String name) {
        return null;
    }

    @Override
    public List<SimpleOperation> findAllByText(String text) {
        return null; //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /**
     * СОХРАНИТЬ
     */
    @Override
    public SimpleOperation save(SimpleOperation entity) {

            return null;

    }

    /**
     * ОБНОВИТЬ
     */
    @Override
    public boolean update(SimpleOperation entity) {

            return false;

    }

    /**
     * УДАЛИТЬ
     */
    @Override
    public boolean delete(SimpleOperation entity) {


            return false;

    }



    //===============================================================

    private SimpleOperation createOp1(){
        SimpleOperation op = new SimpleOperation();
        op.setId(1L);
        op.setName("Изготовление заземляющего кабеля");
        op.setMeasurement(EPieceMeasurement.PIECE);
        op.setNormType(ENormType.NORM_ASSEMBLING);
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