package ru.realestate.realestate_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Deal {
    private Long idDeal;
    private LocalDate dealDate;
    private BigDecimal dealCost;
    private Integer idProperty;
    private Integer idRealtor;
    private Integer idClient;
    private Integer idDealType;
    
    // Конструкторы
    public Deal() {}
    
    public Deal(Long idDeal, LocalDate dealDate, BigDecimal dealCost, 
               Integer idProperty, Integer idRealtor, Integer idClient, Integer idDealType) {
        this.idDeal = idDeal;
        this.dealDate = dealDate;
        this.dealCost = dealCost;
        this.idProperty = idProperty;
        this.idRealtor = idRealtor;
        this.idClient = idClient;
        this.idDealType = idDealType;
    }
    
    // Геттеры и сеттеры
    public Long getIdDeal() {
        return idDeal;
    }
    
    public void setIdDeal(Long idDeal) {
        this.idDeal = idDeal;
    }
    
    public LocalDate getDealDate() {
        return dealDate;
    }
    
    public void setDealDate(LocalDate dealDate) {
        this.dealDate = dealDate;
    }
    
    public BigDecimal getDealCost() {
        return dealCost;
    }
    
    public void setDealCost(BigDecimal dealCost) {
        this.dealCost = dealCost;
    }
    
    public Integer getIdProperty() {
        return idProperty;
    }
    
    public void setIdProperty(Integer idProperty) {
        this.idProperty = idProperty;
    }
    
    public Integer getIdRealtor() {
        return idRealtor;
    }
    
    public void setIdRealtor(Integer idRealtor) {
        this.idRealtor = idRealtor;
    }
    
    public Integer getIdClient() {
        return idClient;
    }
    
    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }
    
    public Integer getIdDealType() {
        return idDealType;
    }
    
    public void setIdDealType(Integer idDealType) {
        this.idDealType = idDealType;
    }
    
    @Override
    public String toString() {
        return "Deal{" +
                "idDeal=" + idDeal +
                ", dealDate=" + dealDate +
                ", dealCost=" + dealCost +
                ", idProperty=" + idProperty +
                ", idRealtor=" + idRealtor +
                ", idClient=" + idClient +
                ", idDealType=" + idDealType +
                '}';
    }
} 