package ru.wert.normic.excel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.excel.model.EditorRow;
import ru.wert.normic.excel.model.POIReader;
import ru.wert.normic.excel.model.enums.EColor;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.NormicServices.MATERIALS;

public class ExcelImporter {

    private ObservableList<EditorRow> data;
    private OpData opData;
    private POIReader poi;
    private List<String> patterns = Arrays.asList(DEC_NUMBER, DEC_NUMBER_WITH_EXT, SKETCH_NUMBER, SKETCH_NUM_WITH_EXT);
    private Integer execution = null;
    int level = 0;
    public OpData convertOpAssmFromExcel(File file) throws IOException {
        poi = new POIReader(file);
        opData = null;

        data = poi.findModelData();
        ObservableList<EditorRow.Execution>  executions = FXCollections.observableArrayList(data.get(0).getExecutions());
        if(executions.size() > 1){

            execution = ExecutionDialog.create(executions);
        }

        //Обязательно закрыть поток
        poi.getBook().close();
        if(execution == null) return null;
        return convert();
    }

    private OpData convert(){

        for(EditorRow row : data){
            //Читаем значения строки
            EColor color = EColor.valueOf(row.getColor());
            String dec = row.getDecNumber(); //Ячейка ДЕЦИМАЛЬНЫЙ НОМЕР
            String name = row.getName(); //Ячейка НАИМЕНОВАНИЕ
            int quantity = Integer.parseInt(row.getExecutions().get(0).getTotalAmount());
            String materialName = row.getMaterial();
            //Если строка - предположительно деталь
            if(color.equals(EColor.WHITE)){

                //Проверяем, содержит ли ячейка правильный децимальный номер
                for(String pat : patterns) {
                    //Если содержит, то
                    if (dec.matches(pat)){
                        //Создаем OpDetail и заполняем его поля
                        OpDetail detail = new OpDetail();
                        detail.setName(dec + ", " + name ); //Имя
                        detail.setQuantity(quantity); //Количество
                        //Определяем материал
                        if(!materialName.isEmpty()){
                            EMatType matType = null;
                            Material mat = MATERIALS.findByName(materialName);
                            if(mat != null)
                                matType = EMatType.getTypeByName(mat.getMatType().getName());
                            detail.setMaterial(mat);
                            detail.setParamA(Integer.parseInt(row.getParamA()));
                            if(matType.equals(EMatType.LIST))
                                detail.setParamB(Integer.parseInt(row.getParamB()));

                        }
                        ((IOpWithOperations)opData).getOperations().add(detail);
                    }
                    //Иначе смотрим следующую строку
                }
            } else {
//                if(color.ordinal() > level){
//                    level = color.ordinal();
//                } else {
//                    level = color.ordinal();
//                }

            }

        }
        return  opData;
    }
}
