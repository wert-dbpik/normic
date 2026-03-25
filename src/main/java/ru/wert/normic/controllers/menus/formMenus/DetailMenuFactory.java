package ru.wert.normic.controllers.menus.formMenus;

import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import ru.wert.normic.AppStatics;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.*;

/**
 * Фабрика для создания меню детали (FormDetailController)
 */
public class DetailMenuFactory implements MenuFactory {

    @Override
    public FormMenuManager createMenu(AbstractFormController controller) {
        FormDetailController detailController = (FormDetailController) controller;
        FormMenuManager menu = new FormMenuManager(controller,
                controller.getListViewTechOperations(),
                (IOpWithOperations) controller.getOpData());

        Material material = detailController.getCmbxMaterial().getValue();
        EMatType type = EMatType.getTypeByName(material.getMatType().getName());

        if (type.equals(EMatType.LIST)) { //ЛИСТЫ
            menu.getItems().add(menu.createItemCutting());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(menu.createItemDrillingByMarking());
            menu.getItems().add(menu.createItemLocksmith());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintDetail());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemWeldAssm());
            menu.getItems().add(menu.createItemWeldContinuousNew());
            menu.getItems().add(menu.createItemWeldingDotted());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if (simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }

//            deleteImproperOperations(detailController, AppStatics.LIST_OPERATIONS);

        } else if (type.equals(EMatType.ROUND)) { //КРУГИ
            menu.getItems().add(menu.createItemMountDismount());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemTurning());
            menu.getItems().add(menu.createItemDrilling());
            menu.getItems().add(menu.createItemCutGroove());
            menu.getItems().add(menu.createItemThreading());
            menu.getItems().add(menu.createItemRolling());
            menu.getItems().add(menu.createItemCutOff());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintingOld());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllLocksmithOperations());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            menu.getItems().add(simpleOperationsMenu);

//            deleteImproperOperations(detailController, AppStatics.ROUND_OPERATIONS);

        } else if (type.equals(EMatType.PROFILE)) { //ПРОФИЛИ
            menu.getItems().add(menu.createItemCutOffOnTheSaw());
            menu.getItems().add(menu.createItemChopOff());
            menu.getItems().add(menu.createItemDrillingByMarking());
            menu.getItems().add(menu.createItemLocksmith());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintingOld());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if (simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }

//            deleteImproperOperations(detailController, AppStatics.PROFILE_OPERATIONS);
        } else { //ШТУЧНЫЕ
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintingOld());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllLatheOperations());
            menu.getItems().add(menu.createAllLocksmithOperations());
            menu.getItems().add(menu.createAllWeldingOperations());
            menu.getItems().add(menu.createAllAssmOperations());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if (simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }
        }

        return menu;
    }

    /**
     * Метод удаляет операции не подходящие под операцию
     */
    private void deleteImproperOperations(FormDetailController controller, List<EOpType> properOperations) {
        //Корректируем список операции, удаляем несовместимые
        List<OpData> operations = new java.util.ArrayList<>(controller.getAddedOperations());
        if (controller.getListViewTechOperations() == null || controller.getListViewTechOperations().getItems().isEmpty()
        ) return;

        for (OpData op : operations) {
            if (!properOperations.contains(op.getOpType())) {
                int index = controller.getAddedOperations().indexOf(op);

                controller.getListViewTechOperations().getItems().remove(index);
                controller.getAddedPlates().remove(index);
                controller.getAddedOperations().remove(index);
            }
        }

        MAIN_CONTROLLER.recountMainOpData();
    }
}
