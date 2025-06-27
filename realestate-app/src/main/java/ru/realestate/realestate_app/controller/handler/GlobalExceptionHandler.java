package ru.realestate.realestate_app.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Глобальный обработчик исключений для всех контроллеров
 * Преобразует исключения приложения в HTTP ответы с понятными сообщениями
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Обработка ошибок валидации Spring Validation
     * @param e исключение MethodArgumentNotValidException
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 400 и деталями ошибок по полям
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleSpringValidationException(
            MethodArgumentNotValidException e, WebRequest request) {
        
        logger.warn("Ошибки валидации Spring Validation: {}", e.getMessage());
        
        // Получаем все ошибки валидации по полям
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        
        // Создаем карту ошибок по полям
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            // Используем сообщение из аннотации валидации (message = "...")
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        // Создаем ответ с ошибками
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Проверьте правильность введенных данных",
            "Ошибки валидации полей",
            request.getDescription(false)
        );
        
        // Добавляем детали ошибок по полям
        errorResponse.setFieldErrors(errors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Обработка исключений когда сущность не найдена
     * @param e исключение EntityNotFoundException
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 404
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException e, WebRequest request) {
        
        logger.warn("Сущность не найдена: {}", e.getDetailedMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Запрашиваемая информация не найдена",
            e.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Обработка исключений валидации
     * @param e исключение ValidationException
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 400
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException e, WebRequest request) {
        
        logger.warn("Ошибка валидации: {}", e.getDetailedMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Проверьте правильность введенных данных",
            e.getMessage(),
            request.getDescription(false)
        );
        
        // Добавляем детали ошибок валидации по полям
        if (e.hasErrors()) {
            errorResponse.setFieldErrors(e.getFieldErrors());
        }
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Обработка исключений базы данных
     * @param e исключение DatabaseException
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 500 или 400 в зависимости от типа ошибки
     */
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            DatabaseException e, WebRequest request) {
        
        logger.error("Ошибка базы данных: {}", e.getDetailedMessage());
        
        // Определяем HTTP статус в зависимости от типа операции
        HttpStatus status = determineHttpStatus(e);
        
        String userMessage = getUserFriendlyMessage(e);
        
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            userMessage,
            e.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, status);
    }
    
    /**
     * Обработка общих исключений приложения
     * @param e исключение RealEstateException
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 500
     */
    @ExceptionHandler(RealEstateException.class)
    public ResponseEntity<ErrorResponse> handleRealEstateException(
            RealEstateException e, WebRequest request) {
        
        logger.error("Ошибка приложения: {}", e.getDetailedMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Произошла непредвиденная ошибка",
            e.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Обработка общих исключений IllegalArgumentException
     * @param e исключение IllegalArgumentException
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e, WebRequest request) {
        
        logger.warn("Некорректные аргументы: {}", e.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Проверьте правильность введенных данных",
            e.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Обработка всех остальных исключений
     * @param e любое исключение
     * @param request HTTP запрос
     * @return HTTP ответ с кодом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception e, WebRequest request) {
        
        logger.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Произошла непредвиденная ошибка",
            "Внутренняя ошибка сервера",
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Обработчик для ошибок доступа к данным (включая ошибки соединения с БД)
     * @param e исключение доступа к данным
     * @return HTTP ответ с деталями ошибки
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        logger.error("Ошибка доступа к данным: {}", e.getMessage(), e);
        
        // Определяем тип ошибки для более точного сообщения
        String userMessage;
        if (e instanceof CannotGetJdbcConnectionException) {
            userMessage = "Сервис временно недоступен. Попробуйте позже.";
        } else if (e instanceof DataIntegrityViolationException) {
            userMessage = "Ошибка целостности данных. Проверьте введенную информацию.";
        } else if (e instanceof BadSqlGrammarException) {
            userMessage = "Внутренняя ошибка системы. Обратитесь к администратору.";
        } else {
            userMessage = "Ошибка при работе с базой данных. Попробуйте позже.";
        }
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            userMessage,
            "Ошибка доступа к данным: " + e.getMessage(),
            "N/A"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    /**
     * Определить HTTP статус для исключения базы данных
     * @param e исключение DatabaseException
     * @return соответствующий HTTP статус
     */
    private HttpStatus determineHttpStatus(DatabaseException e) {
        String operationType = e.getOperationType();
        
        // Для операций вставки с нарушением уникальности возвращаем 400
        if ("INSERT".equals(operationType) && e.getMessage().contains("уникальности")) {
            return HttpStatus.BAD_REQUEST;
        }
        
        // Для операций обновления/удаления несуществующих записей возвращаем 404
        if (("UPDATE".equals(operationType) || "DELETE".equals(operationType)) && 
            e.getMessage().contains("не найден")) {
            return HttpStatus.NOT_FOUND;
        }
        
        // Для остальных ошибок базы данных возвращаем 500
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * Получить понятное пользователю сообщение для исключения базы данных
     * @param e исключение DatabaseException
     * @return понятное сообщение
     */
    private String getUserFriendlyMessage(DatabaseException e) {
        String operationType = e.getOperationType();
        
        if ("INSERT".equals(operationType)) {
            if (e.getMessage().contains("уникальности")) {
                return "Не удалось сохранить данные. Возможно, такая запись уже существует";
            }
            return "Не удалось сохранить данные";
        }
        
        if ("UPDATE".equals(operationType)) {
            if (e.getMessage().contains("не найден")) {
                return "Запись не найдена для обновления";
            }
            return "Не удалось обновить данные";
        }
        
        if ("DELETE".equals(operationType)) {
            if (e.getMessage().contains("не найден")) {
                return "Запись не найдена для удаления";
            }
            return "Не удалось удалить данные";
        }
        
        return "Произошла ошибка при работе с базой данных";
    }
    
    /**
     * Класс для представления ошибки в HTTP ответе
     */
    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final String details;
        private final String path;
        private final LocalDateTime timestamp;
        private Map<String, String> fieldErrors;
        
        public ErrorResponse(int status, String message, String details, String path) {
            this.status = status;
            this.message = message;
            this.details = details;
            this.path = path;
            this.timestamp = LocalDateTime.now();
            this.fieldErrors = new HashMap<>();
        }
        
        // Геттеры
        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
        public String getPath() { return path; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, String> getFieldErrors() { return fieldErrors; }
        
        // Сеттер для ошибок полей
        public void setFieldErrors(Map<String, String> fieldErrors) {
            this.fieldErrors = new HashMap<>(fieldErrors);
        }
    }
} 