package ru.realestate.realestate_app.exception;

/**
 * Исключение для ошибок базы данных
 * Содержит детальную информацию о SQL операциях и причинах ошибок
 */
public class DatabaseException extends RealEstateException {
    
    // Тип операции с базой данных
    private final String operationType;
    
    // SQL запрос, который вызвал ошибку (если доступен)
    private final String sqlQuery;
    
    // Параметры SQL запроса (если доступны)
    private final Object[] sqlParameters;
    
    /**
     * Конструктор с типом операции и сообщением
     * @param operationType тип операции (SELECT, INSERT, UPDATE, DELETE)
     * @param message сообщение об ошибке
     */
    public DatabaseException(String operationType, String message) {
        super("DATABASE_ERROR", 
              String.format("Ошибка базы данных при выполнении %s: %s", operationType, message),
              operationType, message);
        this.operationType = operationType;
        this.sqlQuery = null;
        this.sqlParameters = new Object[0];
    }
    
    /**
     * Конструктор с типом операции, сообщением и причиной
     * @param operationType тип операции
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public DatabaseException(String operationType, String message, Throwable cause) {
        super("DATABASE_ERROR", 
              String.format("Ошибка базы данных при выполнении %s: %s", operationType, message),
              cause, operationType, message);
        this.operationType = operationType;
        this.sqlQuery = null;
        this.sqlParameters = new Object[0];
    }
    
    /**
     * Конструктор с типом операции, SQL запросом и сообщением
     * @param operationType тип операции
     * @param sqlQuery SQL запрос
     * @param message сообщение об ошибке
     */
    public DatabaseException(String operationType, String sqlQuery, String message) {
        super("DATABASE_ERROR", 
              String.format("Ошибка базы данных при выполнении %s: %s. SQL: %s", 
                           operationType, message, sqlQuery),
              operationType, sqlQuery, message);
        this.operationType = operationType;
        this.sqlQuery = sqlQuery;
        this.sqlParameters = new Object[0];
    }
    
    /**
     * Конструктор с полной информацией об ошибке
     * @param operationType тип операции
     * @param sqlQuery SQL запрос
     * @param sqlParameters параметры SQL запроса
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public DatabaseException(String operationType, String sqlQuery, Object[] sqlParameters, 
                           String message, Throwable cause) {
        super("DATABASE_ERROR", 
              String.format("Ошибка базы данных при выполнении %s: %s. SQL: %s", 
                           operationType, message, sqlQuery),
              cause, operationType, sqlQuery, sqlParameters, message);
        this.operationType = operationType;
        this.sqlQuery = sqlQuery;
        this.sqlParameters = sqlParameters != null ? sqlParameters.clone() : new Object[0];
    }
    
    /**
     * Получить тип операции с базой данных
     * @return тип операции
     */
    public String getOperationType() {
        return operationType;
    }
    
    /**
     * Получить SQL запрос, который вызвал ошибку
     * @return SQL запрос или null если недоступен
     */
    public String getSqlQuery() {
        return sqlQuery;
    }
    
    /**
     * Получить параметры SQL запроса
     * @return массив параметров SQL запроса
     */
    public Object[] getSqlParameters() {
        return sqlParameters.clone();
    }
    
    /**
     * Проверить, доступен ли SQL запрос
     * @return true если SQL запрос доступен
     */
    public boolean hasSqlQuery() {
        return sqlQuery != null;
    }
    
    /**
     * Проверить, доступны ли параметры SQL запроса
     * @return true если параметры доступны
     */
    public boolean hasSqlParameters() {
        return sqlParameters.length > 0;
    }
    
    /**
     * Получить форматированное сообщение с SQL информацией
     * @return детальное сообщение об ошибке базы данных
     */
    @Override
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDetailedMessage());
        
        if (hasSqlQuery()) {
            sb.append(" SQL запрос: ").append(sqlQuery);
            
            if (hasSqlParameters()) {
                sb.append(" Параметры: [");
                for (int i = 0; i < sqlParameters.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(sqlParameters[i]);
                }
                sb.append("]");
            }
        }
        
        return sb.toString();
    }
} 