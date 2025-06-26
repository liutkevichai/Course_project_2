package ru.realestate.realestate_app.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Deal {
    private Long idDeal;
    
    @NotNull(message = "Дата сделки обязательна для заполнения")
    @PastOrPresent(message = "Дата сделки не может быть в будущем")
    private LocalDate dealDate;
    
    @NotNull(message = "Стоимость сделки обязательна для заполнения")
    @DecimalMin(value = "0.01", message = "Стоимость сделки должна быть больше 0")
    @DecimalMax(value = "999999999.9999", message = "Стоимость сделки не может превышать 999,999,999.9999")
    @Digits(integer = 9, fraction = 4, message = "Стоимость сделки должна содержать не более 9 целых и 4 дробных цифр")
    private BigDecimal dealCost;
    
    @NotNull(message = "Объект недвижимости обязателен для заполнения")
    @Positive(message = "ID объекта недвижимости должен быть положительным")
    private Integer idProperty;
    
    @NotNull(message = "Риелтор обязателен для заполнения")
    @Positive(message = "ID риелтора должен быть положительным")
    private Integer idRealtor;
    
    @NotNull(message = "Клиент обязателен для заполнения")
    @Positive(message = "ID клиента должен быть положительным")
    private Integer idClient;
    
    @NotNull(message = "Тип сделки обязателен для заполнения")
    @Positive(message = "ID типа сделки должен быть положительным")
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