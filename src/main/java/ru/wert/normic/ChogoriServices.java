package ru.wert.normic;


import ru.wert.normic.entities.db_connection.density.DensityService;
import ru.wert.normic.entities.db_connection.density.IDensityService;
import ru.wert.normic.entities.db_connection.material.IMaterialService;
import ru.wert.normic.entities.db_connection.material.MaterialQuickService;
import ru.wert.normic.entities.db_connection.material.MaterialService;

public class ChogoriServices {

    public static IMaterialService CH_MATERIALS;
    public static IDensityService CH_DENSITIES;

    public static MaterialQuickService CH_QUICK_MATERIALS;

    public static void initServices(){

        CH_MATERIALS = MaterialService.getInstance();
        CH_DENSITIES = DensityService.getInstance();

    }

    public static void initQuickServices(){
        CH_QUICK_MATERIALS = MaterialQuickService.getInstance();
    }


}
