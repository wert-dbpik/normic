package ru.wert.normic.controllers.forms;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.*;

public class ColorsController {

    @FXML
    private TextField tfRAL1, tfRAL2, tfRAL3;

    @FXML
    private TextField tfConsumption1, tfConsumption2, tfConsumption3;

    String ral1, ral2, ral3;
    int consumption1, consumption2, consumption3;


    void init(){

        new TFColoredInteger(tfConsumption1, null);
        new TFColoredInteger(tfConsumption2, null);
        new TFColoredInteger(tfConsumption3, null);

        ral1 = RAL_I; ral2 = RAL_II;ral3 = RAL_III;
        consumption1 = CONSUMPTION_I; consumption2 = CONSUMPTION_II; consumption3 = CONSUMPTION_III;

        tfRAL1.setText(ral1); tfConsumption1.setText(String.valueOf(consumption1));
        tfRAL2.setText(ral2); tfConsumption2.setText(String.valueOf(consumption2));
        tfRAL3.setText(ral3); tfConsumption3.setText(String.valueOf(consumption3));

        tfRAL1.requestFocus();
    }

    public void saveSettings(){
        String r1 = tfRAL1.getText().trim();
        RAL_I = r1.equals("") ? ral1 : r1;

        String r2 = tfRAL2.getText().trim();
        RAL_II = r2.equals("") ? ral2 : r2;

        String r3 = tfRAL3.getText().trim();
        RAL_II = r3.equals("") ? ral3 : r3;

        int cons1 = IntegerParser.getValue(tfConsumption1);
        CONSUMPTION_I = ( cons1 < 100 || cons1 > 500 ) ? consumption1 : cons1;

        int cons2 = IntegerParser.getValue(tfConsumption2);
        CONSUMPTION_II = ( cons2 < 100 || cons2 > 500 ) ? consumption2 : cons2;

        int cons3 = IntegerParser.getValue(tfConsumption2);
        CONSUMPTION_III = ( cons3 < 100 || cons3 > 500 ) ? consumption3 : cons3;

    }
}
