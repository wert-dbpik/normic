package ru.wert.normic.entities;


import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.Material;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpDetail extends OpData {

    private String name = null;
    private Material material = null;
    private Integer paramA = 0;
    private Integer paramB = 0;
    private List<OpData> operations = new ArrayList<>();

    public OpDetail() {
        super.normType = ENormType.NORM_DETAIL;
        super.opType = EOpType.DETAIL;
    }
}
