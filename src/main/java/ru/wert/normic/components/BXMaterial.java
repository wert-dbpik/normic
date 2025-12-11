package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatType;

import java.util.Comparator;
import java.util.List;

import static ru.wert.normic.AppStatics.NO_MATERIAL;
import static ru.wert.normic.NormicServices.QUICK_MATERIALS;

/**
 * Класс для управления комбобоксом выбора материалов.
 * Наследует базовую функциональность быстрого поиска из BXQuickSearch.
 */
@Slf4j
public class BXMaterial extends BXQuickSearch<Material> {

    /** Последнее выбранное значение материала */
    private static Material LAST_VAL = null;

    /** Список всех материалов */
    private ObservableList<Material> allMaterials;

    /**
     * Конструктор
     *
     * @param convertLatinToCyrillic если true, автоматически конвертирует латинские символы в кириллические
     */
    public BXMaterial(boolean convertLatinToCyrillic) {
        super(
                convertLatinToCyrillic,
                Material::getName, // textExtractor - получаем имя материала для поиска
                material -> material == NO_MATERIAL // specialItemPredicate - всегда показывать NO_MATERIAL
        );
    }

    /**
     * Инициализирует комбобокс материалами и настраивает обработчики событий.
     *
     * @param bxMaterial комбобокс для инициализации
     * @param useNoMaterial если true, добавляет элемент "NO_MATERIAL" в начало списка
     * @param materialToBeSelected материал, который должен быть выбран по умолчанию
     */
    public void create(ComboBox<Material> bxMaterial, boolean useNoMaterial, Material materialToBeSelected) {
        this.cmbx = bxMaterial;

        // Загрузка всех материалов в поле класса
        List<Material> materialsFromDB = QUICK_MATERIALS.findAll();
        materialsFromDB.sort(createComparator());

        // Создаем ObservableList для allMaterials
        allMaterials = FXCollections.observableArrayList(materialsFromDB);

        if (useNoMaterial) {
            allMaterials.add(0, NO_MATERIAL);
        }

        // Создаем FilteredList на основе allMaterials
        FilteredList<Material> filteredMaterials = new FilteredList<>(allMaterials);
        filteredMaterials.setPredicate(m -> true); // Изначально показываем все элементы

        // Привязываем комбобокс к быстрому поиску
        bind(bxMaterial, filteredMaterials);

        // Устанавливаем отфильтрованный список как источник данных
        bxMaterial.setItems(filteredMaterials);

        createCellFactory();
        createConverter();

        if (materialToBeSelected != null)
            bxMaterial.setValue(materialToBeSelected);
        else
            bxMaterial.getSelectionModel().select(NO_MATERIAL);
    }

    /**
     * Создает компаратор для сортировки материалов по типу и имени.
     *
     * @return компаратор для сортировки материалов
     */
    private Comparator<Material> createComparator() {
        return (m1, m2) -> {
            int m1_type = EMatType.getTypeByName(m1.getMatType().getName()).ordinal();
            int m2_type = EMatType.getTypeByName(m2.getMatType().getName()).ordinal();

            int res1 = Integer.compare(m1_type, m2_type);
            if (res1 == 0)
                return m1.getName().compareTo(m2.getName());
            else
                return res1;
        };
    }

    /**
     * Создает фабрику ячеек для отображения только имени материала в комбобоксе.
     */
    private void createCellFactory() {
        // CellFactory определяет вид элементов комбобокса - только имя материала
        cmbx.setCellFactory(i -> new ListCell<Material>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    /**
     * Создает конвертер для преобразования материала в строку и обратно.
     */
    private void createConverter() {
        cmbx.setConverter(new StringConverter<Material>() {
            @Override
            public String toString(Material material) {
                LAST_VAL = material;
                if (material == null) return "";
                return material.getName();
            }

            @Override
            public Material fromString(String string) {
                return null;
            }
        });
    }
}