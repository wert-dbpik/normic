package ru.wert.normic.components;

import javafx.animation.PauseTransition;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Базовый класс для комбобоксов с функцией быстрого поиска.
 * Предоставляет функциональность фильтрации по вводу текста,
 * автоматическую конвертацию латинских символов в кириллические (по условию)
 * и обработку специальных клавиш (Backspace, Escape).
 *
 * @param <T> тип элементов в комбобоксе
 */
@Slf4j
public class BXQuickSearch<T> {

    /** Ссылка на комбобокс */
    protected ComboBox<T> cmbx;

    /** Отфильтрованный список элементов */
    protected FilteredList<T> filteredItems;

    /** Текущий паттерн поиска */
    protected String searchingPattern = "";

    /** Таймер для сброса паттерна поиска */
    protected PauseTransition searchTimer;

    /** Таймер для отслеживания длительного нажатия Backspace */
    protected PauseTransition backspaceTimer;

    /** Флаг для отслеживания длительного нажатия Backspace */
    protected boolean backspaceLongPressed = false;

    /** Флаг для отслеживания инициализации обработчиков */
    protected boolean handlersInitialized = false;

    /** Флаг для конвертации латиницы в кириллицу */
    protected boolean convertLatinToCyrillic;

    /** Функция для получения строки из элемента для поиска */
    protected Function<T, String> textExtractor;

    /** Предикат для специальных элементов (например, "NO_MATERIAL") */
    protected Predicate<T> specialItemPredicate;

    /**
     * Конструктор
     *
     * @param convertLatinToCyrillic если true, автоматически конвертирует латинские символы в кириллические
     * @param textExtractor функция для получения строки из элемента для поиска
     * @param specialItemPredicate предикат для определения специальных элементов (всегда отображаются)
     */
    public BXQuickSearch(boolean convertLatinToCyrillic,
                         Function<T, String> textExtractor,
                         Predicate<T> specialItemPredicate) {
        this.convertLatinToCyrillic = convertLatinToCyrillic;
        this.textExtractor = textExtractor;
        this.specialItemPredicate = specialItemPredicate;

        initTimers();
    }

    /**
     * Инициализирует таймеры
     */
    private void initTimers() {
        // Инициализация таймера для сброса паттерна поиска
        searchTimer = new PauseTransition(Duration.seconds(2));
        searchTimer.setOnFinished(event -> {
            log.debug("Таймер сброса паттерна сработал. Текущий паттерн сброшен: '{}'", searchingPattern);
            searchingPattern = "";
        });

        // Инициализация таймера для отслеживания длительного нажатия Backspace
        backspaceTimer = new PauseTransition(Duration.millis(500));
        backspaceTimer.setOnFinished(event -> {
            backspaceLongPressed = true;
            handleLongBackspacePress();
        });
    }

    /**
     * Привязывает комбобокс к быстрому поиску
     *
     * @param comboBox комбобокс для привязки
     * @param items отфильтрованный список элементов
     */
    public void bind(ComboBox<T> comboBox, FilteredList<T> items) {
        this.cmbx = comboBox;
        this.filteredItems = items;

        addDropDownListener();
    }

    /**
     * Добавляет слушатель для отслеживания состояния выпадающего списка.
     * При открытии списка сбрасывает паттерн поиска и инициализирует обработчики клавиш.
     */
    protected void addDropDownListener() {
        cmbx.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.debug("Выпадающий список открыт");
                // При открытии списка сбрасываем паттерн и фильтр
                searchingPattern = "";
                filteredItems.setPredicate(m -> true);
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
    protected void setupKeyboardHandlers() {
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
                    if (convertLatinToCyrillic) {
                        character = convertLatinToCyrillic(character);
                    }

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
    protected String convertLatinToCyrillic(String character) {
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
    protected void handleBackspacePress(KeyEvent event) {
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
    protected void handleBackspaceRelease() {
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
    protected void handleShortBackspacePress() {
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
    protected void handleLongBackspacePress() {
        log.debug("Длительное нажатие Backspace - сброс поискового паттерна");
        searchingPattern = "";
        // Сбрасываем фильтр - показываем все элементы
        filteredItems.setPredicate(m -> true);
        // Останавливаем таймер поиска, так как паттерн сброшен
        searchTimer.stop();
    }

    /**
     * Применяет фильтр к списку элементов на основе текущего паттерна поиска.
     */
    protected void applyFilter() {
        // Всегда применяем фильтр на основе текущего паттерна
        String pattern = searchingPattern.toLowerCase();

        filteredItems.setPredicate(item -> {
            // Проверяем, является ли элемент специальным
            if (specialItemPredicate.test(item)) {
                return true; // Всегда показываем специальные элементы
            }

            if (pattern.isEmpty()) {
                return true; // Показываем все элементы, если паттерн пуст
            }

            String itemText = textExtractor.apply(item).toLowerCase();
            return itemText.contains(pattern);
        });
    }
}
