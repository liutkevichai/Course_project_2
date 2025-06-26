package ru.realestate.realestate_app.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class Property {
    private Long idProperty;
    
    @NotNull(message = "Площадь обязательна для заполнения")
    @DecimalMin(value = "0.01", message = "Площадь должна быть больше 0")
    @DecimalMax(value = "10000.00", message = "Площадь не может превышать 10000 кв.м")
    @Digits(integer = 5, fraction = 2, message = "Площадь должна содержать не более 5 целых и 2 дробных цифр")
    private BigDecimal area;
    
    @NotNull(message = "Стоимость обязательна для заполнения")
    @DecimalMin(value = "0.01", message = "Стоимость должна быть больше 0")
    @DecimalMax(value = "999999999.9999", message = "Стоимость не может превышать 999,999,999.9999")
    @Digits(integer = 9, fraction = 4, message = "Стоимость должна содержать не более 9 целых и 4 дробных цифр")
    private BigDecimal cost;
    
    @Size(max = 1000, message = "Описание не может превышать 1000 символов")
    private String description;
    
    @Size(max = 20, message = "Почтовый индекс не может превышать 20 символов")
    @Pattern(regexp = "^\\d{6}$", message = "Почтовый индекс должен содержать 6 цифр")
    private String postalCode;
    
    @NotBlank(message = "Номер дома обязателен для заполнения")
    @Size(max = 10, message = "Номер дома не может превышать 10 символов")
    @Pattern(regexp = "^[0-9]+[а-яёА-ЯЁa-zA-Z]?$", message = "Номер дома должен содержать цифры и может содержать букву")
    private String houseNumber;
    
    @Size(max = 5, message = "Литера дома не может превышать 5 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z]*$", message = "Литера дома может содержать только буквы")
    private String houseLetter;
    
    @Size(max = 10, message = "Номер корпуса не может превышать 10 символов")
    @Pattern(regexp = "^[0-9]*$", message = "Номер корпуса может содержать только цифры")
    private String buildingNumber;
    
    @Size(max = 10, message = "Номер квартиры не может превышать 10 символов")
    @Pattern(regexp = "^[0-9]*$", message = "Номер квартиры может содержать только цифры")
    private String apartmentNumber;
    
    // Внешние ключи
    @NotNull(message = "Тип недвижимости обязателен для заполнения")
    @Positive(message = "ID типа недвижимости должен быть положительным")
    private Integer idPropertyType;
    
    @NotNull(message = "Страна обязательна для заполнения")
    @Positive(message = "ID страны должен быть положительным")
    private Integer idCountry;
    
    @NotNull(message = "Регион обязателен для заполнения")
    @Positive(message = "ID региона должен быть положительным")
    private Integer idRegion;
    
    @NotNull(message = "Город обязателен для заполнения")
    @Positive(message = "ID города должен быть положительным")
    private Integer idCity;
    
    @NotNull(message = "Район обязателен для заполнения")
    @Positive(message = "ID района должен быть положительным")
    private Integer idDistrict;
    
    @NotNull(message = "Улица обязательна для заполнения")
    @Positive(message = "ID улицы должен быть положительным")
    private Integer idStreet;
    
    // Конструкторы
    public Property() {}
    
    public Property(Long idProperty, BigDecimal area, BigDecimal cost, String description,
                   String postalCode, String houseNumber, String houseLetter, 
                   String buildingNumber, String apartmentNumber) {
        this.idProperty = idProperty;
        this.area = area;
        this.cost = cost;
        this.description = description;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.houseLetter = houseLetter;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }
    
    // Геттеры и сеттеры
    public Long getIdProperty() {
        return idProperty;
    }
    
    public void setIdProperty(Long idProperty) {
        this.idProperty = idProperty;
    }
    
    public BigDecimal getArea() {
        return area;
    }
    
    public void setArea(BigDecimal area) {
        this.area = area;
    }
    
    public BigDecimal getCost() {
        return cost;
    }
    
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getHouseNumber() {
        return houseNumber;
    }
    
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    
    public String getHouseLetter() {
        return houseLetter;
    }
    
    public void setHouseLetter(String houseLetter) {
        this.houseLetter = houseLetter;
    }
    
    public String getBuildingNumber() {
        return buildingNumber;
    }
    
    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }
    
    public String getApartmentNumber() {
        return apartmentNumber;
    }
    
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
    
    public Integer getIdPropertyType() {
        return idPropertyType;
    }
    
    public void setIdPropertyType(Integer idPropertyType) {
        this.idPropertyType = idPropertyType;
    }
    
    public Integer getIdCountry() {
        return idCountry;
    }
    
    public void setIdCountry(Integer idCountry) {
        this.idCountry = idCountry;
    }
    
    public Integer getIdRegion() {
        return idRegion;
    }
    
    public void setIdRegion(Integer idRegion) {
        this.idRegion = idRegion;
    }
    
    public Integer getIdCity() {
        return idCity;
    }
    
    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }
    
    public Integer getIdDistrict() {
        return idDistrict;
    }
    
    public void setIdDistrict(Integer idDistrict) {
        this.idDistrict = idDistrict;
    }
    
    public Integer getIdStreet() {
        return idStreet;
    }
    
    public void setIdStreet(Integer idStreet) {
        this.idStreet = idStreet;
    }
    
    @Override
    public String toString() {
        return "Property{" +
                "idProperty=" + idProperty +
                ", area=" + area +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", houseLetter='" + houseLetter + '\'' +
                ", buildingNumber='" + buildingNumber + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                ", idPropertyType=" + idPropertyType +
                ", idCountry=" + idCountry +
                ", idRegion=" + idRegion +
                ", idCity=" + idCity +
                ", idDistrict=" + idDistrict +
                ", idStreet=" + idStreet +
                '}';
    }
}