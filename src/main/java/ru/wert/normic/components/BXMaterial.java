package ru.wert.normic.components;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
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
 * Предоставляет функциональность фильтрации материалов по вводу текста,
 * автоматическую конвертацию латинских символов в кириллические и
 * обработку специальных клавиш (Backspace, Escape).
 */
@Slf4j
public class BXMaterial {

    /** Последнее выбранное значение материала */
    private static Material LAST_VAL = null;

    /** Ссылка на комбобокс */
    private ComboBox<Material> cmbx;

    /** Текущий паттерн поиска */
    private String searchingPattern = "";

    /** Таймер для сброса паттерна поиска */
    private PauseTransition searchTimer;

    /** Таймер для отслеживания длительного нажатия Backspace */
    private PauseTransition backspaceTimer;

    /** Список всех материалов */
    private ObservableList<Material> allMaterials;

    /** Отфильтрованный список материалов */
    private FilteredList<Material> filteredMaterials;

    /** Флаг для отслеживания инициализации обработчиков */
    private boolean handlersInitialized = false;

    /** Флаг для отслеживания длительного нажатия Backspace */
    private boolean backspaceLongPressed = false;

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
        filteredMaterials = new FilteredList<>(allMaterials);
        filteredMaterials.setPredicate(m -> true); // Изначально показываем все элементы

        // Инициализация таймера для сброса паттерна поиска
        searchTimer = new PauseTransition(Duration.seconds(2));
        searchTimer.setOnFinished(event -> {
            log.debug("Таймер сброса паттерна сработал. Текущий паттерн сброшен: '{}'", searchingPattern);
            searchingPattern = ""; // Сбрасываем только паттерн, не фильтр
            // Фильтр НЕ сбрасываем, список остается отфильтрованным
        });

        // Инициализация таймера для отслеживания длительного нажатия Backspace
        backspaceTimer = new PauseTransition(Duration.millis(500)); // 0.5 секунды
        backspaceTimer.setOnFinished(event -> {
            backspaceLongPressed = true;
            handleLongBackspacePress();
        });

        // Устанавливаем отфильтрованный список как источник данных
        bxMaterial.setItems(filteredMaterials);

        createCellFactory();
        createConverter();

        addDropDownListener();

        if (materialToBeSelected != null)
            bxMaterial.setValue(materialToBeSelected);
        else
            bxMaterial.getSelectionModel().select(NO_MATERIAL);
    }

    /**
     * Добавляет слушатель для отслеживания состояния выпадающего списка.
     * При открытии списка сбрасывает паттерн поиска и инициализирует обработчики клавиш.
     */
    private void addDropDownListener() {
        cmbx.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.debug("Выпадающий список открыт");
                // При открытии списка сбрасываем паттерн и фильтр
                searchingPattern = "";
                filteredMaterials.setPredicate(m -> true);
                backspaceLongPressed = false;

                // Инициализируем обработчики только один раз
                if (!handlersInitialized) {
                    setupKeyboardHandlers();
                    handlersInitialized = true;
                }
            } else {
                log.debug("Выпадающий список закрыт");
                searchTimer.stop(); // Останавливаем таймер при закрытии
                backspaceTimer.stop(); // Останавливаем таймер backspace
            }
        });
    }

    /**
     * Настраивает обработчики клавиатуры для комбобокса.
     * Обрабатывает ввод текста, Backspace, Escape и другие специальные клавиши.
     */
    private void setupKeyboardHandlers() {
        // Обрабатываем все клавиши через KEY_PRESSED
        cmbx.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.BACK_SPACE) {
                handleBackspacePress(event);
                event.consume();
            } else if (code == KeyCode.ESCAPE) {
                // ESC закрывает комбобокс без изменений
                cmbx.hide();
                event.consume();
            } else if (code == KeyCode.ENTER) {
                // ENTER обрабатывается стандартным образом
                // Не потребляем событие, чтобы комбобокс мог его использовать
            } else {
                // Обрабатываем символьные клавиши
                String character = event.getText();
                if (character != null && !character.isEmpty() &&
                        (character.matches("[A-Za-z0-9А-Яа-я,]") || character.equals(" "))) {

                    // Сбрасываем таймер
                    searchTimer.stop();

                    // Конвертируем латиницу в кириллицу при необходимости
                    character = convertLatinToCyrillic(character);

                    // Добавляем символ к паттерну поиска
                    searchingPattern += character;
                    log.debug("Поисковый паттерн: '{}'", searchingPattern);
                    applyFilter();

                    // Запускаем таймер заново
                    searchTimer.playFromStart();

                    event.consume();
                }
            }
        });

        // Обработка отпускания клавиши Backspace
        cmbx.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                handleBackspaceRelease();
            }
        });

        // Отключаем KEY_TYPED, чтобы избежать дублирования
        cmbx.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            // Потребляем все события KEY_TYPED, чтобы они не обрабатывались дважды
            event.consume();
        });
    }

    /**
     * Конвертирует латинские символы в кириллические по стандартной раскладке QWERTY -> ЙЦУКЕН.
     *
     * @param character символ для конвертации
     * @return конвертированный символ или исходный, если символ не требует конвертации
     */
    private String convertLatinToCyrillic(String character) {
        if (character == null || character.isEmpty()) {
            return character;
        }

        // Маппинг латинских символов на кириллические (QWERTY -> ЙЦУКЕН)
        switch (character.toLowerCase()) {
            case "q": return "й";
            case "w": return "ц";
            case "e": return "у";
            case "r": return "к";
            case "t": return "е";
            case "y": return "н";
            case "u": return "г";
            case "i": return "ш";
            case "o": return "щ";
            case "p": return "з";
            case "[": return "х";
            case "]": return "ъ";
            case "a": return "ф";
            case "s": return "ы";
            case "d": return "в";
            case "f": return "а";
            case "g": return "п";
            case "h": return "р";
            case "j": return "о";
            case "k": return "л";
            case "l": return "д";
            case ";": return "ж";
            case "'": return "э";
            case "z": return "я";
            case "x": return "ч";
            case "c": return "с";
            case "v": return "м";
            case "b": return "и";
            case "n": return "т";
            case "m": return "ь";
            case ",": return "б";
            case ".": return "ю";
            case "/": return ".";
            default: return character; // Возвращаем исходный символ, если это не латинская буква
        }
    }

    /**
     * Обрабатывает нажатие клавиши Backspace.
     *
     * @param event событие нажатия клавиши
     */
    private void handleBackspacePress(KeyEvent event) {
        // Сбрасываем флаг длительного нажатия
        backspaceLongPressed = false;

        // Запускаем таймер для отслеживания длительного нажатия
        backspaceTimer.playFromStart();

        // Сбрасываем таймер поиска
        searchTimer.stop();

        // Если это не длительное нажатие (обработаем короткое нажатие позже)
        if (!backspaceLongPressed) {
            // Ждем немного, чтобы определить, это короткое или длительное нажатие
            // Короткое нажатие будет обработано в handleBackspaceRelease()
        }
    }

    /**
     * Обрабатывает отпускание клавиши Backspace.
     * Определяет, было ли это короткое или длительное нажатие.
     */
    private void handleBackspaceRelease() {
        // Останавливаем таймер длительного нажатия
        backspaceTimer.stop();

        // Если это было короткое нажатие (не длительное)
        if (!backspaceLongPressed) {
            handleShortBackspacePress();
        }

        // Сбрасываем флаг
        backspaceLongPressed = false;
    }

    /**
     * Обрабатывает короткое нажатие клавиши Backspace.
     * Удаляет последний символ из паттерна поиска.
     */
    private void handleShortBackspacePress() {
        if (!searchingPattern.isEmpty()) {
            searchingPattern = searchingPattern.substring(0, searchingPattern.length() - 1);
            log.debug("Поисковый паттерн после короткого Backspace: '{}'", searchingPattern);
            applyFilter();
        }
        // Запускаем таймер поиска заново
        searchTimer.playFromStart();
    }

    /**
     * Обрабатывает длительное нажатие клавиши Backspace.
     * Полностью сбрасывает паттерн поиска и фильтр.
     */
    private void handleLongBackspacePress() {
        log.debug("Длительное нажатие Backspace - сброс поискового паттерна");
        searchingPattern = "";
        // Сбрасываем фильтр - показываем все элементы
        filteredMaterials.setPredicate(m -> true);
        // Останавливаем таймер поиска, так как паттерн сброшен
        searchTimer.stop();
    }

    /**
     * Применяет фильтр к списку материалов на основе текущего паттерна поиска.
     */
    private void applyFilter() {
        // Всегда применяем фильтр на основе текущего паттерна
        String pattern = searchingPattern.toLowerCase();

        filteredMaterials.setPredicate(material -> {
            if (material == NO_MATERIAL) {
                return true; // Всегда показываем NO_MATERIAL
            }

            if (pattern.isEmpty()) {
                return true; // Показываем все материалы, если паттерн пуст
            }

            String materialName = material.getName().toLowerCase();
            return materialName.contains(pattern);
        });
    }

    /**
     * Создает компаратор для сортировки материалов по типу и имени.
     *
     * @return компаратор для сортировки материалов
     */
    private Comparator<Material> createComparator() {
        return new Comparator<Material>() {
            @Override
            public int compare(Material m1, Material m2) {
                int m1_type = EMatType.getTypeByName(m1.getMatType().getName()).ordinal();
                int m2_type = EMatType.getTypeByName(m2.getMatType().getName()).ordinal();

                int res1 = Integer.compare(m1_type, m2_type);
                if (res1 == 0)
                    return m1.getName().compareTo(m2.getName());
                else
                    return res1;
            }
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
