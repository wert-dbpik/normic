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
                    updateMaterialComboBox(cmbxMaterial);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Обновляет комбобокс с материалами после закрытия окна редактора материалов
     * @param cmbxMaterial комбобокс для обновления
     */
    private void updateMaterialComboBox(ComboBox<Material> cmbxMaterial) {
        // Сохраняем текущий выбранный материал
        Material chosenMaterial = cmbxMaterial.getValue();

        // Загружаем обновленный список материалов из БД
        ObservableList<Material> newMaterials = FXCollections.observableArrayList(QUICK_MATERIALS.findAll());
        newMaterials.sort(Comparator.comparing(Material::getName));

        // Всегда добавляем NO_MATERIAL в начало (если его нет)
        if (!newMaterials.contains(NO_MATERIAL)) {
            newMaterials.add(0, NO_MATERIAL);
        }

        // Обновляем список в комбобоксе
        updateComboBoxItems(cmbxMaterial, newMaterials);

        // Восстанавливаем выбор материала
        restoreSelectedMaterial(cmbxMaterial, chosenMaterial, newMaterials);

        // Сбрасываем фильтр быстрого поиска если он есть
        resetQuickSearchFilter(cmbxMaterial);
    }

    /**
     * Безопасно обновляет элементы комбобокса с учетом FilteredList
     */
    private void updateComboBoxItems(ComboBox<Material> cmbx, ObservableList<Material> newItems) {
        try {
            // Проверяем, является ли список FilteredList
            if (cmbx.getItems() instanceof javafx.collections.transformation.FilteredList) {
                javafx.collections.transformation.FilteredList<Material> filteredList =
                        (javafx.collections.transformation.FilteredList<Material>) cmbx.getItems();

                // Получаем исходный список (source)
                ObservableList<Material> sourceList = (ObservableList<Material>) filteredList.getSource();

                // Безопасно обновляем исходный список
                if (!sourceList.equals(newItems)) {
                    sourceList.setAll(newItems);
                }
            } else {
                // Просто обновляем обычный список
                cmbx.setItems(newItems);
            }
        } catch (Exception e) {
            // В случае ошибки используем простой способ
            System.err.println("Ошибка при обновлении списка комбобокса: " + e.getMessage());
            cmbx.setItems(newItems);
        }
    }

    /**
     * Восстанавливает выбранный материал в комбобоксе
     */
    private void restoreSelectedMaterial(ComboBox<Material> cmbx, Material chosenMaterial, ObservableList<Material> items) {
        if (chosenMaterial != null && items.contains(chosenMaterial)) {
            cmbx.getSelectionModel().select(chosenMaterial);
        } else {
            // Если ранее выбранный материал не найден, выбираем первый
            cmbx.getSelectionModel().select(0);
        }
    }

    /**
     * Пытается сбросить фильтр быстрого поиска в комбобоксе
     */
    private void resetQuickSearchFilter(ComboBox<Material> cmbx) {
        try {
            // Ищем поле с быстрым поиском через рефлексию
            java.lang.reflect.Field quickSearchField = findQuickSearchField(cmbx);
            if (quickSearchField != null) {
                Object quickSearch = quickSearchField.get(cmbx);
                invokeResetFilterMethod(quickSearch);
            }
        } catch (Exception e) {
            // Не критично, если не удалось сбросить фильтр
            System.err.println("Не удалось сбросить фильтр быстрого поиска: " + e.getMessage());
        }
    }

    /**
     * Ищет поле с типом, содержащим "QuickSearch"
     */
    private java.lang.reflect.Field findQuickSearchField(ComboBox<Material> cmbx) {
        try {
            java.lang.reflect.Field[] fields = cmbx.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.getType().getName().contains("QuickSearch")) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } catch (Exception e) {
            // Игнорируем ошибки при поиске поля
        }
        return null;
    }

    /**
     * Вызывает метод resetFilter() у объекта быстрого поиска
     */
    private void invokeResetFilterMethod(Object quickSearch) {
        try {
            java.lang.reflect.Method resetMethod = quickSearch.getClass().getMethod("resetFilter");
            resetMethod.invoke(quickSearch);
        } catch (NoSuchMethodException e) {
            // Пробуем альтернативные имена методов
            tryAlternativeResetMethods(quickSearch);
        } catch (Exception e) {
            // Игнорируем другие ошибки
        }
    }

    /**
     * Пробует альтернативные методы сброса фильтра
     */
    private void tryAlternativeResetMethods(Object quickSearch) {
        String[] methodNames = {"reset", "clearFilter", "clearSearch"};
        for (String methodName : methodNames) {
            try {
                java.lang.reflect.Method method = quickSearch.getClass().getMethod(methodName);
                method.invoke(quickSearch);
                break;
            } catch (Exception e) {
                // Продолжаем пробовать другие методы
            }
        }
    }
}
