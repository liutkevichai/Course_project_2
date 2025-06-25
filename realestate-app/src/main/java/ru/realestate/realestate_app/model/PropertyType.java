package ru.realestate.realestate_app.model;

public class PropertyType {
    private Long idPropertyType;
    private String propertyTypeName;
    
    // Конструкторы
    public PropertyType() {}
    
    public PropertyType(Long idPropertyType, String propertyTypeName) {
        this.idPropertyType = idPropertyType;
        this.propertyTypeName = propertyTypeName;
    }
    
    // Геттеры и сеттеры
    public Long getIdPropertyType() {
        return idPropertyType;
    }
    
    public void setIdPropertyType(Long idPropertyType) {
        this.idPropertyType = idPropertyType;
    }
    
    public String getPropertyTypeName() {
        return propertyTypeName;
    }
    
    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }
    
    @Override
    public String toString() {
        return "PropertyType{" +
                "idPropertyType=" + idPropertyType +
                ", propertyTypeName='" + propertyTypeName + '\'' +
                '}';
    }
} 