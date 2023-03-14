package ru.wert.normic.settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;
import static ru.wert.normic.entities.settings.AppSettings.*;

public class SettingsController {

    //--- РЕЗКА И ЗАЧИСТКА

    @FXML private TextField tfCUTTING_SPEED; //Скорость резания, зависящая от площади детали, мин
    @FXML private TextField tfREVOLVER_SPEED; //Скорость вырубки одного элемента револьвером, мин/уд
    @FXML private TextField tfPERFORATION_SPEED; //Скорость перфорирования, мин/уд
    @FXML private TextField tfCUTTING_SERVICE_RATIO; //коэфффициент, учитывающий 22% времени на обслуживание при резке
    @FXML private TextField tfSTRIPING_SPEED; //Скорость зачистки резанных кромок, сек

    //--- ГИБКА

    @FXML private TextField tfBENDING_SPEED; //Скорость гибки, мин/гиб
    @FXML private TextField tfBENDING_SERVICE_RATIO; //коэфффициент, учитывающий 25% времени на обслуживание при гибке

    //--- СЛЕСАРНЫЕ РАБОТЫ

    @FXML private TextField tfCHOP_SPEED; //Скорость рубки на Геке, мин/удар
    @FXML private TextField tfRIVETS_SPEED; //Скорость установки вытяжной заклепки, сек/закл
    @FXML private TextField tfCOUNTERSINKING_SPEED; //Скорость сверления и зенковки, мин/отв
    @FXML private TextField tfTHREADING_SPEED; //Скорость нарезания резьбы, мин/отв
    @FXML private TextField tfSMALL_SAWING_SPEED; //Скорость пиления на малой пиле, мин/рез
    @FXML private TextField tfBIG_SAWING_SPEED; //Скорость пиления на большой пиле, мин/рез

    //--- ПОКРАСКА ДЕТАЛИ

    @FXML private TextField tfDETAIL_DELTA; //Расстояние между деталями, мм
    @FXML private TextField tfWASHING; //Мойка, сек
    @FXML private TextField tfWINDING; //Продувка, сек
    @FXML private TextField tfDRYING; //Сушка, сек
    @FXML private TextField tfBAKING; //Полимеризация, мин

    //--- ПОКРАСКА СБОРКИ

    @FXML private TextField tfASSM_DELTA; //Расстояние между сборками, мм
    @FXML private TextField tfHANGING_TIME; //Время навески и снятия после полимеризации, мин
    @FXML private TextField tfWINDING_MOVING_SPEED; //Продувка после промывки и перемещение изделя на штанге, мин/1 м.кв.
    @FXML private TextField tfSOLID_BOX_SPEED; //Скорость окрашивания глухих шкафов, мин/1 м.кв.
    @FXML private TextField tfFRAME_SPEED; //Скорость окрашивания открытых рам и кроссов, мин/1 м.кв.

    //--- СВАРКА НЕПРЕРЫВНАЯ

    @FXML private TextField tfWELDING_SPEED; //Скорость сваркм, мин/м

    //--- СВАРКА ТОЧЕЧНАЯ

    @FXML private TextField tfWELDING_PARTS_SPEED; //Скорость онденсаторной сварки точкой, мин/элемент
    @FXML private TextField tfWELDING_DOTTED_SPEED; //Скорость контактной сварки, мин/точку
    @FXML private TextField tfWELDING_DROP_SPEED; //Скорость сварки прихватками, мин/прихватку

    //--- ЗАЛИВКА УПЛОТНИТЕЛЯ

    @FXML private TextField tfPREPARED_TIME; //ПЗ время, мин
    @FXML private TextField tfLEVELING_SPEED; //Скорость нанесения, м/мин


    //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ

    @FXML private TextField tfSCREWS_SPEED; //Скорость установки вытяжных винтов, мин
    @FXML private TextField tfVSHGS_SPEED ; //Скорость установки комплектов ВШГ, мин
    @FXML private TextField tfRIVET_NUTS_SPEED; //Скорость установки заклепочных гаек, сек
    @FXML private TextField tfGROUND_SETS_SPEED; //Скорость установки комплекта заземления с этикеткой, мин
    @FXML private TextField tfOTHERS_SPEED; //Скорость установки другого крепежа, сек

    //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ

    @FXML private TextField tfPOST_LOCKS_SPEED; //Скорость установки почтового замка с регулировкой, мин
    @FXML private TextField tfDOUBLE_LOCKS_SPEED; //Скорость установки замка с рычагами, мин
    @FXML private TextField tfGLASS_SPEED; //Скорость установки стекла на полиуретан, мин
    @FXML private TextField tfDETECTORS_SPEED; //Скорость установки извещателей (ИО-102), мин
    @FXML private TextField tfCONNECTION_BOXES_SPEED; //Скорость установки коробки соединительной (КС-4), мин

    //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ

    @FXML private TextField tfSEALER_SPEED; //Скорость монтажа уплотнителя на ребро корпуса, сек/м
    @FXML private TextField tfSELF_ADH_SEALER_SPEED; //Скорость установки самоклеющегося уплотнителя, сек/м
    @FXML private TextField tfINSULATION_SPEED; //Скорость разметки, резки и укладки утеплителя, мин/м

    //--- УПАКОВКА

    @FXML private TextField tfCARTOON_BOX_PREPARED_TIME; //ПЗ время изготовления коробок, мин
    @FXML private TextField tfCARTOON_BOX_SPEED; //Время изготовления коробки, мин
    @FXML private TextField tfSTRETCH_MACHINE_WINDING; //Время наматывания машинной стретч-пленки, мин
    @FXML private TextField tfCARTOON_BOX_AND_ANGLES_SPEED; //Время изготовления крышек и уголков, мин
    @FXML private TextField tfPACK_IN_CARTOON_BOX_SPEED; //Время упаковки изделя в коробку, мин
    @FXML private TextField tfDUCT_TAPE_LENGTH; //Длина рулона скотча, м
    @FXML private TextField tfBUBBLE_CUT_AND_DUCT; //ПЗ время пузырьковой пленки, мин
    @FXML private TextField tfBUBBLE_HAND_WINDING; //Скорость оборачивания пузырьковой пленки, мин/м.кв.
    @FXML private TextField tfSTRETCH_HAND_WINDING; //Скорость оборачивания стретч пленки, мин/м


    @FXML
    void initialize(){
        List<TextField> textFields = Arrays.asList(tfCUTTING_SPEED, tfREVOLVER_SPEED, tfPERFORATION_SPEED, tfCUTTING_SERVICE_RATIO, tfSTRIPING_SPEED,
                tfBENDING_SPEED, tfBENDING_SERVICE_RATIO, tfCHOP_SPEED, tfRIVETS_SPEED, tfCOUNTERSINKING_SPEED, tfTHREADING_SPEED,
                tfSMALL_SAWING_SPEED, tfBIG_SAWING_SPEED, tfDETAIL_DELTA, tfWASHING, tfWINDING, tfBAKING, tfDRYING, tfASSM_DELTA,
                tfHANGING_TIME, tfWINDING_MOVING_SPEED, tfSOLID_BOX_SPEED, tfFRAME_SPEED, tfWELDING_SPEED, tfWELDING_PARTS_SPEED, tfWELDING_DOTTED_SPEED,
                tfWELDING_DROP_SPEED, tfPREPARED_TIME, tfLEVELING_SPEED, tfSCREWS_SPEED, tfVSHGS_SPEED, tfRIVET_NUTS_SPEED,
                tfGROUND_SETS_SPEED, tfOTHERS_SPEED, tfPOST_LOCKS_SPEED, tfDOUBLE_LOCKS_SPEED, tfGLASS_SPEED, tfDETECTORS_SPEED,
                tfCONNECTION_BOXES_SPEED, tfSEALER_SPEED, tfSELF_ADH_SEALER_SPEED, tfINSULATION_SPEED,
                tfCARTOON_BOX_PREPARED_TIME, tfCARTOON_BOX_SPEED, tfSTRETCH_MACHINE_WINDING, tfCARTOON_BOX_AND_ANGLES_SPEED,
                tfPACK_IN_CARTOON_BOX_SPEED, tfDUCT_TAPE_LENGTH, tfBUBBLE_CUT_AND_DUCT, tfBUBBLE_HAND_WINDING, tfSTRETCH_HAND_WINDING);

        for(TextField tf : textFields){
            tf.setEditable(false);
        }

        //--- РЕЗКА И ЗАЧИСТКА
        tfCUTTING_SPEED.setText(DECIMAL_FORMAT.format(CUTTING_SPEED).trim());
        tfREVOLVER_SPEED.setText(DECIMAL_FORMAT.format(REVOLVER_SPEED).trim());
        tfPERFORATION_SPEED.setText(DECIMAL_FORMAT.format(PERFORATION_SPEED).trim());
        tfCUTTING_SERVICE_RATIO.setText(DECIMAL_FORMAT.format(CUTTING_SERVICE_RATIO).trim());
        tfSTRIPING_SPEED.setText(DECIMAL_FORMAT.format(STRIPING_SPEED).trim());

        //--- ГИБКА

        tfBENDING_SPEED.setText(DECIMAL_FORMAT.format(BENDING_SPEED).trim());
        tfBENDING_SERVICE_RATIO.setText(DECIMAL_FORMAT.format(BENDING_SERVICE_RATIO).trim());

        //--- СЛЕСАРНЫЕ РАБОТЫ

        tfCHOP_SPEED.setText(DECIMAL_FORMAT.format(CHOP_SPEED).trim());
        tfRIVETS_SPEED.setText(DECIMAL_FORMAT.format(RIVETS_SPEED).trim());
        tfCOUNTERSINKING_SPEED.setText(DECIMAL_FORMAT.format(COUNTERSINKING_SPEED).trim());
        tfTHREADING_SPEED.setText(DECIMAL_FORMAT.format(THREADING_SPEED).trim());
        tfSMALL_SAWING_SPEED.setText(DECIMAL_FORMAT.format(SMALL_SAWING_SPEED).trim());
        tfBIG_SAWING_SPEED.setText(DECIMAL_FORMAT.format(BIG_SAWING_SPEED).trim());

        //--- ПОКРАСКА ДЕТАЛИ

        tfDETAIL_DELTA.setText(DECIMAL_FORMAT.format(DETAIL_DELTA * 1.0).trim());
        tfWASHING.setText(DECIMAL_FORMAT.format(WASHING).trim());
        tfWINDING.setText(DECIMAL_FORMAT.format(WINDING).trim());
        tfDRYING.setText(DECIMAL_FORMAT.format(DRYING).trim());
        tfBAKING.setText(DECIMAL_FORMAT.format(BAKING).trim());

        //--- ПОКРАСКА СБОРКИ

        tfASSM_DELTA.setText(DECIMAL_FORMAT.format(ASSM_DELTA * 1.0).trim());
        tfHANGING_TIME.setText(DECIMAL_FORMAT.format(HANGING_TIME).trim());
        tfWINDING_MOVING_SPEED.setText(DECIMAL_FORMAT.format(WINDING_MOVING_SPEED).trim());
        tfSOLID_BOX_SPEED.setText(DECIMAL_FORMAT.format(SOLID_BOX_SPEED).trim());
        tfFRAME_SPEED.setText(DECIMAL_FORMAT.format(FRAME_SPEED).trim());

        //--- СВАРКА НЕПРЕРЫВНАЯ

        tfWELDING_SPEED.setText(DECIMAL_FORMAT.format(WELDING_SPEED).trim());

        //--- СВАРКА ТОЧЕЧНАЯ

        tfWELDING_PARTS_SPEED.setText(DECIMAL_FORMAT.format(WELDING_PARTS_SPEED).trim());
        tfWELDING_DOTTED_SPEED.setText(DECIMAL_FORMAT.format(WELDING_DOTTED_SPEED).trim());
        tfWELDING_DROP_SPEED.setText(DECIMAL_FORMAT.format(WELDING_DROP_SPEED).trim());

        //--- ЗАЛИВКА УПЛОТНИТЕЛЯ

        tfPREPARED_TIME.setText(DECIMAL_FORMAT.format(LEVELING_PREPARED_TIME).trim());
        tfLEVELING_SPEED.setText(DECIMAL_FORMAT.format(LEVELING_SPEED).trim());


        //--- СБОРКА КРЕПЕЖНЫХ ЭЛЕМЕНТОВ

        tfSCREWS_SPEED.setText(DECIMAL_FORMAT.format(SCREWS_SPEED).trim());
        tfVSHGS_SPEED.setText(DECIMAL_FORMAT.format(VSHGS_SPEED).trim());
        tfRIVET_NUTS_SPEED.setText(DECIMAL_FORMAT.format(RIVET_NUTS_SPEED).trim());
        tfGROUND_SETS_SPEED.setText(DECIMAL_FORMAT.format(GROUND_SETS_SPEED).trim());
        tfOTHERS_SPEED.setText(DECIMAL_FORMAT.format(OTHERS_SPEED).trim());

        //--- СБОРКА ОТДЕЛЬНЫХ УЗЛОВ

        tfPOST_LOCKS_SPEED.setText(DECIMAL_FORMAT.format(POST_LOCKS_SPEED).trim());
        tfDOUBLE_LOCKS_SPEED.setText(DECIMAL_FORMAT.format(DOUBLE_LOCKS_SPEED).trim());
        tfGLASS_SPEED.setText(DECIMAL_FORMAT.format(GLASS_SPEED).trim());
        tfDETECTORS_SPEED.setText(DECIMAL_FORMAT.format(DETECTORS_SPEED).trim());
        tfCONNECTION_BOXES_SPEED.setText(DECIMAL_FORMAT.format(CONNECTION_BOXES_SPEED).trim());

        //--- СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ

        tfSEALER_SPEED.setText(DECIMAL_FORMAT.format(SEALER_SPEED).trim());
        tfSELF_ADH_SEALER_SPEED.setText(DECIMAL_FORMAT.format(SELF_ADH_SEALER_SPEED).trim());
        tfINSULATION_SPEED.setText(DECIMAL_FORMAT.format(INSULATION_SPEED).trim());

        //--- УПАКОВКА

        tfCARTOON_BOX_PREPARED_TIME.setText(DECIMAL_FORMAT.format(CARTOON_BOX_PREPARED_TIME).trim());
        tfCARTOON_BOX_SPEED.setText(DECIMAL_FORMAT.format(CARTOON_BOX_SPEED).trim());
        tfSTRETCH_MACHINE_WINDING.setText(DECIMAL_FORMAT.format(STRETCH_MACHINE_WINDING).trim());
        tfCARTOON_BOX_AND_ANGLES_SPEED.setText(DECIMAL_FORMAT.format(CARTOON_BOX_AND_ANGLES_SPEED).trim());
        tfPACK_IN_CARTOON_BOX_SPEED.setText(DECIMAL_FORMAT.format(PACK_IN_CARTOON_BOX_SPEED).trim());
        tfDUCT_TAPE_LENGTH.setText(DECIMAL_FORMAT.format(DUCT_TAPE_LENGTH).trim());
        tfBUBBLE_CUT_AND_DUCT.setText(DECIMAL_FORMAT.format(BUBBLE_CUT_AND_DUCT).trim());
        tfBUBBLE_HAND_WINDING.setText(DECIMAL_FORMAT.format(BUBBLE_HAND_WINDING).trim());
        tfSTRETCH_HAND_WINDING.setText(DECIMAL_FORMAT.format(STRETCH_HAND_WINDING).trim());
    }
}

