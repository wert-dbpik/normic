package ru.wert.normic;


import ru.wert.normic.entities.db_connection.IMaterialService;
import ru.wert.normic.entities.db_connection.MaterialQuickService;
import ru.wert.normic.entities.db_connection.MaterialService;

public class ChogoriServices {

    public static IMaterialService CH_MATERIALS;

    public static MaterialQuickService CH_QUICK_MATERIALS;

    public static void initServices(){

        CH_MATERIALS = MaterialService.getInstance();

    }

    public static void initQuickServices(){
        CH_QUICK_MATERIALS = MaterialQuickService.getInstance();
    }


}
