package ru.realestate.realestate_app.exception;

/**
 * Базовое исключение для приложения недвижимости
 * Содержит детальную информацию об ошибке для логирования и отладки
 */
public class RealEstateException extends RuntimeException {
    
    // Код ошибки для категоризации
    private final String errorCode;
    
    // Дополнительные данные для диагностики
    private final Object[] errorDetails;
    
    /**
     * Конструктор с сообщением об ошибке
     * @param message сообщение об ошибке
     */
    public RealEstateException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
        this.errorDetails = new Object[0];
    }
    
    /**
     * Конструктор с сообщением и причиной
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public RealEstateException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
        this.errorDetails = new Object[0];
    }
    
    /**
     * Конструктор с кодом ошибки и сообщением
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     */
    public RealEstateException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = new Object[0];
    }
    
    /**
     * Конструктор с кодом ошибки, сообщением и деталями
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     * @param errorDetails дополнительные детали ошибки
     */
    public RealEstateException(String errorCode, String message, Object... errorDetails) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }
    
    /**
     * Конструктор с кодом ошибки, сообщением, причиной и деталями
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     * @param cause причина исключения
     * @param errorDetails дополнительные детали ошибки
     */
    public RealEstateException(String errorCode, String message, Throwable cause, Object... errorDetails) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }
    
    /**
     * Получить код ошибки
     * @return код ошибки
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Получить детали ошибки
     * @return массив деталей ошибки
     */
    public Object[] getErrorDetails() {
        return errorDetails;
    }
    
    /**
     * Получить форматированное сообщение с деталями
     * @return полное сообщение об ошибке
     */
    public String getDetailedMessage() {
        if (errorDetails.length == 0) {
            return String.format("[%s] %s", errorCode, getMessage());
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s", errorCode, getMessage()));
        sb.append(" Детали: ");
        
        for (int i = 0; i < errorDetails.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(errorDetails[i]);
        }
        
        return sb.toString();
    }
} 