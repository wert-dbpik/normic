package ru.wert.normic.excel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.wert.normic.entities.ops.opAssembling.OpAssm;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opAssembling.OpDetail;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.excel.model.EditorRow;
import ru.wert.normic.excel.model.POIReader;
import ru.wert.normic.excel.model.enums.EColor;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.materials.matlPatches.MaterialMaster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.NormicServices.MATERIALS;

public class ExcelImporter {

    private ObservableList<EditorRow> data;
    private OpAssm startOpData; //Начальная сборка
    private OpAssm currentOpData = new OpAssm(); //Текущая сборка
    private List<LevelAssemble> assembles = new ArrayList<>(); //Список сборок

    private POIReader poi;
    private List<String> patterns = Arrays.asList(DEC_NUMBER, DEC_NUMBER_WITH_EXT, SKETCH_NUMBER, SKETCH_NUM_WITH_EXT);
    private Integer execution = 0;
    int level = 0;

    public OpAssm convertOpAssmFromExcel(File file) throws IOException {
        poi = new POIReader(file);
        startOpData = currentOpData;
        assembles.add(new LevelAssemble(0, startOpData));

        data = poi.findModelData();
        poi.getBook().close(); //Закрываем книгу

        ObservableList<EditorRow.Execution>  executions = FXCollections.observableArrayList(data.get(0).getExecutions());
        if(executions.size() > 1){
            execution = ExecutionDialog.create(executions);
        }
        if(execution == null) return null;
        return convert();
    }

    private OpAssm convert(){

        for(EditorRow row : data){
            //Проверяем исполнение
            String quantityStr = row.getExecutions().get(execution).getTotalAmount();
            Integer quantity = quantityStr.isEmpty() ? null : Integer.parseInt(quantityStr);
            if(quantity == null) continue;
            //Читаем значения строки
            EColor color = EColor.valueOf(row.getColor());
            String dec = row.getDecNumber(); //Ячейка ДЕЦИМАЛЬНЫЙ НОМЕР
            String name = row.getName(); //Ячейка НАИМЕНОВАНИЕ

            String materialName = row.getMaterial().isEmpty() ?
                    "" :
                    row.getMaterial();
            //Если строка - деталь
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
                            if(mat != null) {
                                matType = EMatType.getTypeByName(mat.getMatType().getName());
                                detail.setMaterial(mat);
                                detail.setParamA(Integer.parseInt(row.getParamA()));
                                if(matType.equals(EMatType.LIST)) {
                                    detail.setParamB(Integer.parseInt(row.getParamB()));
                                    detail.setArea(MaterialMaster.countListArea(detail));
                                    detail.setWeight(MaterialMaster.countListWeight(detail, mat));
                                    //Вырезание детали
                                    OpCutting opCutting = new OpCutting();
                                    detail.getOperations().add(opCutting);

                                } else if (matType.equals(EMatType.ROUND)){
                                    detail.setArea(MaterialMaster.countRoundArea(detail, mat));
                                    detail.setWeight(MaterialMaster.countRoundWeight(detail, mat));
                                } else if (matType.equals(EMatType.PROFILE)){
                                    detail.setArea(MaterialMaster.countProfileArea(detail, mat));
                                    detail.setWeight(MaterialMaster.countProfileWeight(detail, mat));
                                }
                            }
                        }
                        ((IOpWithOperations) currentOpData).getOperations().add(detail);
                        break;
                    }
                    //Иначе смотрим следующую строку
                }
            //Если строка - сборка
            } else {
                OpAssm newOpData = new OpAssm(); //Новая сборка
                int newLevel = EColor.valueOf(row.getColor()).ordinal(); //Уровень новой сборки
                //Заполняем поля новой сборки
                newOpData.setName(dec + ", " + name );
                newOpData.setQuantity(quantity);

                //Перебираем список сборок с конца к началу,
                // ищем вышестоящую сборку и в нее добавлем новую сборку
                for(int i = assembles.size() - 1 ; i >= 0; i--){
                    LevelAssemble assemble = assembles.get(i);
                    //Если предыдущий уровень выше
                    if(assemble.getLevel() < newLevel){
                        //Добавляем текущую сборку в вышестоящую
                        assemble.getAssm().getOperations().add(newOpData);
                        //Новую сборку делаем текущей
                        currentOpData = newOpData;
                        break;
                    }
                }
                //Добавляем новую сборку в список сборок
                assembles.add(new LevelAssemble(newLevel, newOpData));
            }

        }
        return  startOpData;
    }

    private void recountAll(){

    }
}

@Getter
@AllArgsConstructor
class LevelAssemble{
    private int level;
    private OpAssm assm;
}