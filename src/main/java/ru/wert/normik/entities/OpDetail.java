package ru.wert.normik.entities;


import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.entities.db_connection.Material;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpDetail extends OpData {

    private String name = null;
    private Integer quantity = 1;
    private Material material = null;
    private Integer paramA = 0;
    private Integer paramB = 0;
    private List<OpData> operations = new ArrayList<>();

    public OpDetail() {
        super.normType = ENormType.NORM_DETAIL;
        super.opType = EOpType.DETAIL;
    }
}
