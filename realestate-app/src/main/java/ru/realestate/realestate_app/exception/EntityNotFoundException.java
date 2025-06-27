package ru.realestate.realestate_app.exception;

/**
 * Исключение, возникающее когда запрашиваемая сущность не найдена в базе данных
 * Используется для обработки случаев отсутствия данных
 */
public class EntityNotFoundException extends RealEstateException {
    
    // Тип сущности, которая не была найдена
    private final String entityType;
    
    // Идентификатор, по которому искали сущность
    private final Object entityId;
    
    /**
     * Конструктор с типом сущности и идентификатором
     * @param entityType тип сущности (например, "Client", "Property")
     * @param entityId идентификатор сущности
     */
    public EntityNotFoundException(String entityType, Object entityId) {
        super("ENTITY_NOT_FOUND", 
              String.format("%s с идентификатором %s не найден", entityType, entityId),
              entityType, entityId);
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    /**
     * Конструктор с типом сущности, идентификатором и дополнительным сообщением
     * @param entityType тип сущности
     * @param entityId идентификатор сущности
     * @param additionalMessage дополнительное сообщение
     */
    public EntityNotFoundException(String entityType, Object entityId, String additionalMessage) {
        super("ENTITY_NOT_FOUND", 
              String.format("%s с идентификатором %s не найден. %s", entityType, entityId, additionalMessage),
              entityType, entityId, additionalMessage);
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    /**
     * Конструктор с типом сущности, идентификатором и причиной
     * @param entityType тип сущности
     * @param entityId идентификатор сущности
     * @param cause причина исключения
     */
    public EntityNotFoundException(String entityType, Object entityId, Throwable cause) {
        super("ENTITY_NOT_FOUND", 
              String.format("%s с идентификатором %s не найден", entityType, entityId),
              cause, entityType, entityId);
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    /**
     * Получить тип сущности
     * @return тип сущности
     */
    public String getEntityType() {
        return entityType;
    }
    
    /**
     * Получить идентификатор сущности
     * @return идентификатор сущности
     */
    public Object getEntityId() {
        return entityId;
    }
} 