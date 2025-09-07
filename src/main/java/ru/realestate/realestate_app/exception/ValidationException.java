package ru.realestate.realestate_app.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Исключение для ошибок валидации данных
 * Содержит детальную информацию о полях, которые не прошли валидацию
 */
public class ValidationException extends RealEstateException {
    
    // Карта ошибок валидации по полям
    private final Map<String, String> fieldErrors;
    
    /**
     * Конструктор с общим сообщением об ошибке валидации
     * @param message общее сообщение об ошибке валидации
     */
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
        this.fieldErrors = new HashMap<>();
    }
    
    /**
     * Конструктор с ошибкой для конкретного поля
     * @param fieldName название поля
     * @param errorMessage сообщение об ошибке для поля
     */
    public ValidationException(String fieldName, String errorMessage) {
        super("VALIDATION_ERROR", 
              String.format("Ошибка валидации поля '%s': %s", fieldName, errorMessage),
              fieldName, errorMessage);
        this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(fieldName, errorMessage);
    }
    
    /**
     * Конструктор с картой ошибок валидации
     * @param fieldErrors карта ошибок по полям
     */
    public ValidationException(Map<String, String> fieldErrors) {
        super("VALIDATION_ERROR", 
              String.format("Обнаружено %d ошибок валидации", fieldErrors.size()),
              fieldErrors);
        this.fieldErrors = new HashMap<>(fieldErrors);
    }
    
    /**
     * Конструктор с картой ошибок и дополнительным сообщением
     * @param message общее сообщение
     * @param fieldErrors карта ошибок по полям
     */
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super("VALIDATION_ERROR", message, fieldErrors);
        this.fieldErrors = new HashMap<>(fieldErrors);
    }
    
    /**
     * Добавить ошибку валидации для поля
     * @param fieldName название поля
     * @param errorMessage сообщение об ошибке
     */
    public void addFieldError(String fieldName, String errorMessage) {
        fieldErrors.put(fieldName, errorMessage);
    }
    
    /**
     * Получить все ошибки валидации по полям
     * @return карта ошибок по полям
     */
    public Map<String, String> getFieldErrors() {
        return new HashMap<>(fieldErrors);
    }
    
    /**
     * Проверить, есть ли ошибки валидации
     * @return true если есть ошибки валидации
     */
    public boolean hasErrors() {
        return !fieldErrors.isEmpty();
    }
    
    /**
     * Получить количество ошибок валидации
     * @return количество ошибок
     */
    public int getErrorCount() {
        return fieldErrors.size();
    }
    
    /**
     * Получить ошибку для конкретного поля
     * @param fieldName название поля
     * @return сообщение об ошибке или null если ошибки нет
     */
    public String getFieldError(String fieldName) {
        return fieldErrors.get(fieldName);
    }
    
    /**
     * Получить форматированное сообщение со всеми ошибками полей
     * @return детальное сообщение об ошибках валидации
     */
    @Override
    public String getDetailedMessage() {
        if (fieldErrors.isEmpty()) {
            return super.getDetailedMessage();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDetailedMessage());
        sb.append(" Ошибки полей: ");
        
        boolean first = true;
        for (Map.Entry<String, String> entry : fieldErrors.entrySet()) {
            if (!first) sb.append("; ");
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
            first = false;
        }
        
        return sb.toString();
    }
} 