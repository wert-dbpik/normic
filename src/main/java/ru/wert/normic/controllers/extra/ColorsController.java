package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.*;

public class ColorsController {

    @FXML
    private TextField tfRAL1, tfRAL2, tfRAL3;

    @FXML
    private TextField tfConsumption1, tfConsumption2, tfConsumption3;

    String ral1, ral2, ral3;
    int consumption1, consumption2, consumption3;


    public void init(){

        new TFColoredInteger(tfConsumption1, null);
        new TFColoredInteger(tfConsumption2, null);
        new TFColoredInteger(tfConsumption3, null);

        ral1 = EColor.COLOR_I.getRal();
        ral2 = EColor.COLOR_II.getRal();
        ral3 = EColor.COLOR_III.getRal();
        consumption1 = EColor.COLOR_I.getConsumption();
        consumption2 = EColor.COLOR_II.getConsumption();
        consumption3 = EColor.COLOR_III.getConsumption();

        tfRAL1.setText(ral1);
        tfConsumption1.setText(String.valueOf(consumption1));

        tfRAL2.setText(ral2);
        tfConsumption2.setText(String.valueOf(consumption2));

        tfRAL3.setText(ral3);
        tfConsumption3.setText(String.valueOf(consumption3));

        tfRAL1.requestFocus();
    }

    public void saveSettings(){
        String r1 = tfRAL1.getText().trim();
        EColor.COLOR_I.setRal(r1.equals("") ? ral1 : r1);

        String r2 = tfRAL2.getText().trim();
        EColor.COLOR_II.setRal(r2.equals("") ? ral2 : r2);

        String r3 = tfRAL3.getText().trim();
        EColor.COLOR_III.setRal(r3.equals("") ? ral3 : r3);

        int cons1 = IntegerParser.getValue(tfConsumption1);
        EColor.COLOR_I.setConsumption((cons1 < 100 || cons1 > 500 ) ? consumption1 : cons1);

        int cons2 = IntegerParser.getValue(tfConsumption2);
        EColor.COLOR_II.setConsumption(( cons2 < 100 || cons2 > 500 ) ? consumption2 : cons2);

        int cons3 = IntegerParser.getValue(tfConsumption3);
        EColor.COLOR_III.setConsumption(( cons3 < 100 || cons3 > 500 ) ? consumption3 : cons3);

    }
}
