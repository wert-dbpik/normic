package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.material.Material;

import java.io.IOException;
import java.util.Comparator;

import static ru.wert.normic.AppStatics.NO_MATERIAL;
import static ru.wert.normic.NormicServices.QUICK_MATERIALS;

public class BtnAddMaterial{

    public BtnAddMaterial(Button btnAddMaterial, ComboBox<Material> cmbxMaterial) {
        btnAddMaterial.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 18,18, true, true)));
        btnAddMaterial.setTooltip(new Tooltip("Добавить материал"));
        btnAddMaterial.setOnAction(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialsTV.fxml"));
                Parent parent = loader.load();
                Decoration decoration = new Decoration(
                        "МАТЕРИАЛЫ",
                        parent,
                        false,
                        (Stage) ((Node)e.getSource()).getScene().getWindow(),
                        "decoration-settings",
                        false,
                        false);

                decoration.getWindow().setOnHiding(r->{
                    Material chosenMaterial = cmbxMaterial.getValue();

                    // Получаем обновленный список материалов из БД
                    ObservableList<Material> materials = FXCollections.observableArrayList(QUICK_MATERIALS.findAll());
                    materials.sort(Comparator.comparing(Material::getName));

                    // Сохраняем специальные элементы (если есть)
                    ObservableList<Material> currentItems = cmbxMaterial.getItems();

                    // Проверяем, содержит ли текущий список NO_MATERIAL
                    boolean hasNoMaterial = !currentItems.isEmpty() && currentItems.get(0).equals(NO_MATERIAL);

                    // Создаем новый список для обновления
                    ObservableList<Material> newItems = FXCollections.observableArrayList(materials);

                    // Добавляем NO_MATERIAL в начало, если нужно
                    if (chosenMaterial != null && chosenMaterial.equals(NO_MATERIAL) || hasNoMaterial) {
                        if (!newItems.contains(NO_MATERIAL)) {
                            newItems.add(0, NO_MATERIAL);
                        }
                    }

                    // ОБНОВЛЯЕМ БАЗОВЫЙ СПИСОК в FilteredList
                    // Получаем оригинальный список из FilteredList
                    if (cmbxMaterial.getItems() instanceof javafx.collections.transformation.FilteredList) {
                        javafx.collections.transformation.FilteredList<Material> filteredList =
                                (javafx.collections.transformation.FilteredList<Material>) cmbxMaterial.getItems();
                        // Обновляем оригинальный список (source)
                        ObservableList<Material> sourceList = (ObservableList<Material>) filteredList.getSource();
                        if (!(sourceList instanceof javafx.beans.property.Property)) {
                            // Используем setAll для обновления
                            sourceList.setAll(newItems);
                        }
                    } else {
                        // Если это не FilteredList, просто обновляем
                        cmbxMaterial.setItems(newItems);
                    }

                    // Восстанавливаем выбранный элемент
                    if (chosenMaterial != null && newItems.contains(chosenMaterial)) {
                        cmbxMaterial.getSelectionModel().select(chosenMaterial);
                    } else if (!newItems.isEmpty()) {
                        cmbxMaterial.getSelectionModel().select(0);
                    }

                    // Сбрасываем фильтр быстрого поиска (если он есть)
                    // Это можно сделать через рефлексию или добавить метод в BXQuickSearch
                    resetQuickSearchFilter(cmbxMaterial);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Сбрасывает фильтр быстрого поиска в комбобоксе
     * @param cmbx комбобокс с возможным быстрым поиском
     */
    private void resetQuickSearchFilter(ComboBox<Material> cmbx) {
        try {
            // Пытаемся найти поле quickSearch через рефлексию
            java.lang.reflect.Field[] fields = cmbx.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.getType().getName().contains("QuickSearch")) {
                    field.setAccessible(true);
                    Object quickSearch = field.get(cmbx);
                    if (quickSearch != null) {
                        // Вызываем метод сброса фильтра
                        java.lang.reflect.Method resetMethod = quickSearch.getClass().getMethod("resetFilter");
                        resetMethod.invoke(quickSearch);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // Если не удалось сбросить фильтр - это не критично
            System.err.println("Не удалось сбросить фильтр быстрого поиска: " + e.getMessage());
        }
    }
}
