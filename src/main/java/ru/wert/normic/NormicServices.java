package ru.wert.normic;


import ru.wert.normic.entities.db_connection.anyPart.AnyPartQuickService;
import ru.wert.normic.entities.db_connection.anyPart.AnyPartService;
import ru.wert.normic.entities.db_connection.anyPartGroup.AnyPartGroupService;
import ru.wert.normic.entities.db_connection.density.DensityService;
import ru.wert.normic.entities.db_connection.density.IDensityService;
import ru.wert.normic.entities.db_connection.matType.IMatTypeService;
import ru.wert.normic.entities.db_connection.matType.MatTypeService;
import ru.wert.normic.entities.db_connection.material.IMaterialService;
import ru.wert.normic.entities.db_connection.material.MaterialQuickService;
import ru.wert.normic.entities.db_connection.material.MaterialService;
import ru.wert.normic.entities.db_connection.material_group.IMaterialGroupService;
import ru.wert.normic.entities.db_connection.material_group.MaterialGroupService;

public class NormicServices {

    public static IMaterialService MATERIALS;
    public static IDensityService DENSITIES;
    public static IMatTypeService MAT_TYPES;
    public static IMaterialGroupService MATERIAL_GROUPS;
    public static AnyPartGroupService ANY_PART_GROUPS;
    public static AnyPartService ANY_PART;

    public static MaterialQuickService QUICK_MATERIALS;
    public static AnyPartQuickService QUICK_ANY_PARTS;

    public static void initServices(){

        MATERIALS = MaterialService.getInstance();
        DENSITIES = DensityService.getInstance();
        MAT_TYPES = MatTypeService.getInstance();
        MATERIAL_GROUPS = MaterialGroupService.getInstance();
        ANY_PART = AnyPartService.getInstance();
        ANY_PART_GROUPS = AnyPartGroupService.getInstance();


    }

    public static void initQuickServices(){
        QUICK_MATERIALS = MaterialQuickService.getInstance();
        QUICK_ANY_PARTS = AnyPartQuickService.getInstance();
    }


}
