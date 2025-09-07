package ru.realestate.realestate_app.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;

/**
 * Утилитный класс для обработки и преобразования исключений
 * Преобразует Spring Data исключения в специфичные исключения приложения
 */
public final class ExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private ExceptionHandler() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Обработать исключение базы данных и преобразовать в специфичное исключение приложения
     * @param e исходное исключение
     * @param operationType тип операции (SELECT, INSERT, UPDATE, DELETE)
     * @param entityType тип сущности (Client, Property, Deal и т.д.)
     * @param entityId идентификатор сущности (если применимо)
     * @return преобразованное исключение приложения
     */
    public static RealEstateException handleDatabaseException(Exception e, String operationType, 
                                                           String entityType, Object entityId) {
        logger.debug("Обработка исключения базы данных: {} для {} с id {}", 
                    e.getClass().getSimpleName(), entityType, entityId);
        
        // Обработка случая, когда сущность не найдена
        if (e instanceof EmptyResultDataAccessException) {
            logger.warn("Сущность {} с id {} не найдена", entityType, entityId);
            return new EntityNotFoundException(entityType, entityId);
        }
        
        // Обработка нарушений целостности данных
        if (e instanceof DataIntegrityViolationException) {
            logger.error("Нарушение целостности данных при выполнении {} для {}: {}", 
                        operationType, entityType, e.getMessage());
            return new DatabaseException(operationType, 
                                       "Нарушение целостности данных: " + e.getMessage(), e);
        }
        
        // Обработка ошибок SQL синтаксиса
        if (e instanceof BadSqlGrammarException) {
            logger.error("Ошибка SQL синтаксиса при выполнении {} для {}: {}", 
                        operationType, entityType, e.getMessage());
            return new DatabaseException(operationType, 
                                       "Ошибка SQL синтаксиса: " + e.getMessage(), e);
        }
        
        // Обработка ошибок соединения с базой данных
        if (e instanceof CannotGetJdbcConnectionException) {
            logger.error("Ошибка соединения с базой данных при выполнении {} для {}: {}", 
                        operationType, entityType, e.getMessage());
            return new DatabaseException(operationType, 
                                       "Ошибка соединения с базой данных: " + e.getMessage(), e);
        }
        
        // Обработка общих ошибок доступа к данным
        if (e instanceof DataAccessException) {
            logger.error("Ошибка доступа к данным при выполнении {} для {}: {}", 
                        operationType, entityType, e.getMessage());
            return new DatabaseException(operationType, 
                                       "Ошибка доступа к данным: " + e.getMessage(), e);
        }
        
        // Обработка неизвестных исключений
        logger.error("Неизвестная ошибка базы данных при выполнении {} для {}: {}", 
                    operationType, entityType, e.getMessage(), e);
        return new DatabaseException(operationType, 
                                   "Неизвестная ошибка базы данных: " + e.getMessage(), e);
    }
    
    /**
     * Обработать исключение валидации
     * @param e исходное исключение валидации
     * @param entityType тип сущности
     * @return преобразованное исключение валидации
     */
    public static ValidationException handleValidationException(Exception e, String entityType) {
        logger.debug("Обработка исключения валидации для {}: {}", entityType, e.getMessage());
        
        if (e instanceof IllegalArgumentException) {
            logger.warn("Ошибка валидации для {}: {}", entityType, e.getMessage());
            return new ValidationException("Общие данные", e.getMessage());
        }
        
        // Если это кастомное исключение валидации, возвращаем как есть
        if (e instanceof ValidationException) {
            return (ValidationException) e;
        }
        
        // Общее исключение валидации для других типов исключений 
        logger.error("Неизвестная ошибка валидации для {}: {}", entityType, e.getMessage());
        return new ValidationException("Неизвестная ошибка валидации: " + e.getMessage());
    }
    
    /**
     * Создать исключение для случая, когда связанная сущность не найдена
     * @param relatedEntityType тип связанной сущности
     * @param relatedEntityId идентификатор связанной сущности
     * @param mainEntityType тип основной сущности
     * @return исключение с детальной информацией
     */
    public static DatabaseException createRelatedEntityNotFoundException(String relatedEntityType, 
                                                                        Object relatedEntityId, 
                                                                        String mainEntityType) {
        String message = String.format("Связанная сущность %s с id %s не найдена для %s", 
                                      relatedEntityType, relatedEntityId, mainEntityType);
        logger.error(message);
        return new DatabaseException("VALIDATION", 
                                   String.format("Связанная сущность не существует: %s", message));
    }
    
    /**
     * Создать исключение для случая нарушения уникальности
     * @param fieldName название поля, которое должно быть уникальным
     * @param fieldValue значение поля
     * @param entityType тип сущности
     * @return исключение с детальной информацией
     */
    public static DatabaseException createUniquenessViolationException(String fieldName, 
                                                                       Object fieldValue, 
                                                                       String entityType) {
        String message = String.format("%s с %s '%s' уже существует", 
                                      entityType, fieldName, fieldValue);
        logger.warn(message);
        return new DatabaseException("INSERT", 
                                   String.format("Нарушение уникальности: %s", message));
    }
    
    /**
     * Логировать исключение с детальной информацией
     * @param e исключение для логирования
     * @param context контекст операции
     */
    public static void logException(Exception e, String context) {
        if (e instanceof RealEstateException) {
            RealEstateException re = (RealEstateException) e;
            logger.error("{}: {}", context, re.getDetailedMessage());
        } else {
            logger.error("{}: {}", context, e.getMessage(), e);
        }
    }
    
    /**
     * Проверить, является ли исключение критическим (требует немедленного внимания)
     * @param e исключение для проверки
     * @return true если исключение критическое
     */
    public static boolean isCriticalException(Exception e) {
        return e instanceof DatabaseException && 
               "DATABASE_ERROR".equals(((DatabaseException) e).getErrorCode());
    }
    
    /**
     * Получить пользовательское сообщение для исключения
     * @param e исключение
     * @return понятное пользователю сообщение
     */
    public static String getUserFriendlyMessage(Exception e) {
        if (e instanceof EntityNotFoundException) {
            return "Запрашиваемая информация не найдена";
        }
        
        if (e instanceof ValidationException) {
            return "Проверьте правильность введенных данных";
        }
        
        if (e instanceof DatabaseException) {
            DatabaseException de = (DatabaseException) e;
            if (de.getOperationType().equals("INSERT")) {
                return "Не удалось сохранить данные. Возможно, такая запись уже существует";
            }
            if (de.getOperationType().equals("UPDATE")) {
                return "Не удалось обновить данные";
            }
            if (de.getOperationType().equals("DELETE")) {
                return "Не удалось удалить данные";
            }
            return "Произошла ошибка при работе с базой данных";
        }
        
        return "Произошла непредвиденная ошибка";
    }
} 