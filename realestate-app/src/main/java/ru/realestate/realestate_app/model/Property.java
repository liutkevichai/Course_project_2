package ru.realestate.realestate_app.model;

import java.math.BigDecimal;

public class Property {
    private Long idProperty;
    private BigDecimal area;
    private BigDecimal cost;
    private String description;
    private String postalCode;
    private String houseNumber;
    private String houseLetter;
    private String buildingNumber;
    private String apartmentNumber;
    
    // Внешние ключи
    private Integer idPropertyType;
    private Integer idCountry;
    private Integer idRegion;
    private Integer idCity;
    private Integer idDistrict;
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