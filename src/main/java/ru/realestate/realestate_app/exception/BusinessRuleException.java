package ru.realestate.realestate_app.exception;

/**
 * Исключение для нарушений бизнес-правил приложения
 * Используется когда операция не может быть выполнена из-за бизнес-логики
 */
public class BusinessRuleException extends RealEstateException {
    
    // Название бизнес-правила, которое было нарушено
    private final String ruleName;
    
    // Дополнительная информация о контексте нарушения правила
    private final String context;
    
    /**
     * Конструктор с названием правила и сообщением
     * @param ruleName название бизнес-правила
     * @param message сообщение об ошибке
     */
    public BusinessRuleException(String ruleName, String message) {
        super("BUSINESS_RULE_VIOLATION", 
              String.format("Нарушение бизнес-правила '%s': %s", ruleName, message),
              ruleName, message);
        this.ruleName = ruleName;
        this.context = null;
    }
    
    /**
     * Конструктор с названием правила, контекстом и сообщением
     * @param ruleName название бизнес-правила
     * @param context контекст нарушения правила
     * @param message сообщение об ошибке
     */
    public BusinessRuleException(String ruleName, String context, String message) {
        super("BUSINESS_RULE_VIOLATION", 
              String.format("Нарушение бизнес-правила '%s' в контексте '%s': %s", 
                           ruleName, context, message),
              ruleName, context, message);
        this.ruleName = ruleName;
        this.context = context;
    }
    
    /**
     * Конструктор с названием правила, сообщением и причиной
     * @param ruleName название бизнес-правила
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public BusinessRuleException(String ruleName, String message, Throwable cause) {
        super("BUSINESS_RULE_VIOLATION", 
              String.format("Нарушение бизнес-правила '%s': %s", ruleName, message),
              cause, ruleName, message);
        this.ruleName = ruleName;
        this.context = null;
    }
    
    /**
     * Конструктор с полной информацией о нарушении правила
     * @param ruleName название бизнес-правила
     * @param context контекст нарушения правила
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public BusinessRuleException(String ruleName, String context, String message, Throwable cause) {
        super("BUSINESS_RULE_VIOLATION", 
              String.format("Нарушение бизнес-правила '%s' в контексте '%s': %s", 
                           ruleName, context, message),
              cause, ruleName, context, message);
        this.ruleName = ruleName;
        this.context = context;
    }
    
    /**
     * Получить название бизнес-правила
     * @return название правила
     */
    public String getRuleName() {
        return ruleName;
    }
    
    /**
     * Получить контекст нарушения правила
     * @return контекст или null если не указан
     */
    public String getContext() {
        return context;
    }
    
    /**
     * Проверить, указан ли контекст
     * @return true если контекст указан
     */
    public boolean hasContext() {
        return context != null;
    }
} 