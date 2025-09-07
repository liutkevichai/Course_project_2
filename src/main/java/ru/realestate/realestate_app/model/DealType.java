package ru.realestate.realestate_app.model;

public class DealType {
    private Long idDealType;
    private String dealTypeName;
    
    // Конструкторы
    public DealType() {}
    
    public DealType(Long idDealType, String dealTypeName) {
        this.idDealType = idDealType;
        this.dealTypeName = dealTypeName;
    }
    
    // Геттеры и сеттеры
    public Long getIdDealType() {
        return idDealType;
    }
    
    public void setIdDealType(Long idDealType) {
        this.idDealType = idDealType;
    }
    
    public String getDealTypeName() {
        return dealTypeName;
    }
    
    public void setDealTypeName(String dealTypeName) {
        this.dealTypeName = dealTypeName;
    }
    
    @Override
    public String toString() {
        return "DealType{" +
                "idDealType=" + idDealType +
                ", dealTypeName='" + dealTypeName + '\'' +
                '}';
    }
} 