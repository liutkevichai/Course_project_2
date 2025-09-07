package ru.realestate.realestate_app.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * DTO для детального просмотра сделки с полной информацией о связанных сущностях
 * 
 * Этот класс содержит всю информацию о сделке, включая:
 * - Основные данные сделки (дата, стоимость)
 * - Полную информацию о клиенте (имя, контакты)
 * - Полную информацию о риелторе (имя, контакты, опыт)
 * - Полную информацию об объекте недвижимости (адрес, характеристики)
 * - Тип сделки (продажа, аренда и т.д.)
 */
public class DealWithDetailsDto {
    
    // ========== ОСНОВНЫЕ ДАННЫЕ СДЕЛКИ ==========
    
    /**
     * Уникальный идентификатор сделки
     */
    private Long dealId;
    
    /**
     * Дата совершения сделки
     */
    private LocalDate dealDate;
    
    /**
     * Стоимость сделки в рублях
     */
    private BigDecimal dealCost;
    
    // ========== ИНФОРМАЦИЯ О КЛИЕНТЕ ==========
    
    /**
     * Идентификатор клиента
     */
    private Long clientId;
    
    /**
     * Имя клиента
     */
    private String clientFirstName;
    
    /**
     * Фамилия клиента
     */
    private String clientLastName;
    
    /**
     * Отчество клиента (может быть null)
     */
    private String clientMiddleName;
    
    /**
     * Номер телефона клиента
     */
    private String clientPhone;
    
    /**
     * Email клиента (может быть null)
     */
    private String clientEmail;
    
    // ========== ИНФОРМАЦИЯ О РИЕЛТОРЕ ==========
    
    /**
     * Идентификатор риелтора
     */
    private Long realtorId;
    
    /**
     * Имя риелтора
     */
    private String realtorFirstName;
    
    /**
     * Фамилия риелтора
     */
    private String realtorLastName;
    
    /**
     * Отчество риелтора (может быть null)
     */
    private String realtorMiddleName;
    
    /**
     * Номер телефона риелтора
     */
    private String realtorPhone;
    
    /**
     * Email риелтора (может быть null)
     */
    private String realtorEmail;
    
    /**
     * Опыт работы риелтора в годах
     */
    private Integer realtorExperience;
    
    // ========== ИНФОРМАЦИЯ ОБ ОБЪЕКТЕ НЕДВИЖИМОСТИ ==========
    
    /**
     * Идентификатор объекта недвижимости
     */
    private Long propertyId;
    
    /**
     * Площадь объекта в квадратных метрах
     */
    private BigDecimal propertyArea;
    
    /**
     * Стоимость объекта недвижимости
     */
    private BigDecimal propertyCost;
    
    /**
     * Описание объекта недвижимости
     */
    private String propertyDescription;
    
    /**
     * Почтовый индекс
     */
    private String propertyPostalCode;
    
    /**
     * Номер дома
     */
    private String propertyHouseNumber;
    
    /**
     * Литера дома (может быть null)
     */
    private String propertyHouseLetter;
    
    /**
     * Номер корпуса (может быть null)
     */
    private String propertyBuildingNumber;
    
    /**
     * Номер квартиры (может быть null)
     */
    private String propertyApartmentNumber;
    
    // ========== ГЕОГРАФИЧЕСКАЯ ИНФОРМАЦИЯ ==========
    
    /**
     * Название страны
     */
    private String countryName;
    
    /**
     * Название региона
     */
    private String regionName;
    
    /**
     * Название города
     */
    private String cityName;
    
    /**
     * Название района
     */
    private String districtName;
    
    /**
     * Название улицы
     */
    private String streetName;
    
    // ========== ТИПЫ ==========
    
    /**
     * Название типа недвижимости (квартира, дом, офис и т.д.)
     */
    private String propertyTypeName;
    
    /**
     * Название типа сделки (продажа, аренда и т.д.)
     */
    private String dealTypeName;
    
    // ========== КОНСТРУКТОРЫ ==========
    
    /**
     * Конструктор по умолчанию
     */
    public DealWithDetailsDto() {}
    
    /**
     * Полный конструктор для создания DTO из данных JOIN запроса
     * 
     * @param dealId идентификатор сделки
     * @param dealDate дата сделки
     * @param dealCost стоимость сделки
     * @param clientId идентификатор клиента
     * @param clientFirstName имя клиента
     * @param clientLastName фамилия клиента
     * @param clientMiddleName отчество клиента
     * @param clientPhone телефон клиента
     * @param clientEmail email клиента
     * @param realtorId идентификатор риелтора
     * @param realtorFirstName имя риелтора
     * @param realtorLastName фамилия риелтора
     * @param realtorMiddleName отчество риелтора
     * @param realtorPhone телефон риелтора
     * @param realtorEmail email риелтора
     * @param realtorExperience опыт риелтора
     * @param propertyId идентификатор недвижимости
     * @param propertyArea площадь недвижимости
     * @param propertyCost стоимость недвижимости
     * @param propertyDescription описание недвижимости
     * @param propertyPostalCode почтовый индекс
     * @param propertyHouseNumber номер дома
     * @param propertyHouseLetter литера дома
     * @param propertyBuildingNumber номер корпуса
     * @param propertyApartmentNumber номер квартиры
     * @param countryName название страны
     * @param regionName название региона
     * @param cityName название города
     * @param districtName название района
     * @param streetName название улицы
     * @param propertyTypeName тип недвижимости
     * @param dealTypeName тип сделки
     */
    public DealWithDetailsDto(Long dealId, LocalDate dealDate, BigDecimal dealCost,
                             Long clientId, String clientFirstName, String clientLastName, String clientMiddleName, 
                             String clientPhone, String clientEmail,
                             Long realtorId, String realtorFirstName, String realtorLastName, String realtorMiddleName,
                             String realtorPhone, String realtorEmail, Integer realtorExperience,
                             Long propertyId, BigDecimal propertyArea, BigDecimal propertyCost, String propertyDescription,
                             String propertyPostalCode, String propertyHouseNumber, String propertyHouseLetter,
                             String propertyBuildingNumber, String propertyApartmentNumber,
                             String countryName, String regionName, String cityName, String districtName, String streetName,
                             String propertyTypeName, String dealTypeName) {
        this.dealId = dealId;
        this.dealDate = dealDate;
        this.dealCost = dealCost;
        this.clientId = clientId;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientMiddleName = clientMiddleName;
        this.clientPhone = clientPhone;
        this.clientEmail = clientEmail;
        this.realtorId = realtorId;
        this.realtorFirstName = realtorFirstName;
        this.realtorLastName = realtorLastName;
        this.realtorMiddleName = realtorMiddleName;
        this.realtorPhone = realtorPhone;
        this.realtorEmail = realtorEmail;
        this.realtorExperience = realtorExperience;
        this.propertyId = propertyId;
        this.propertyArea = propertyArea;
        this.propertyCost = propertyCost;
        this.propertyDescription = propertyDescription;
        this.propertyPostalCode = propertyPostalCode;
        this.propertyHouseNumber = propertyHouseNumber;
        this.propertyHouseLetter = propertyHouseLetter;
        this.propertyBuildingNumber = propertyBuildingNumber;
        this.propertyApartmentNumber = propertyApartmentNumber;
        this.countryName = countryName;
        this.regionName = regionName;
        this.cityName = cityName;
        this.districtName = districtName;
        this.streetName = streetName;
        this.propertyTypeName = propertyTypeName;
        this.dealTypeName = dealTypeName;
    }
    
    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить полное имя клиента
     * 
     * @return полное имя в формате "Фамилия Имя Отчество"
     */
    public String getClientFullName() {
        StringBuilder fullName = new StringBuilder();
        if (clientLastName != null) fullName.append(clientLastName);
        if (clientFirstName != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(clientFirstName);
        }
        if (clientMiddleName != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(clientMiddleName);
        }
        return fullName.toString();
    }
    
    /**
     * Получить полное имя риелтора
     * 
     * @return полное имя в формате "Фамилия Имя Отчество"
     */
    public String getRealtorFullName() {
        StringBuilder fullName = new StringBuilder();
        if (realtorLastName != null) fullName.append(realtorLastName);
        if (realtorFirstName != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(realtorFirstName);
        }
        if (realtorMiddleName != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(realtorMiddleName);
        }
        return fullName.toString();
    }
    
    /**
     * Получить полный адрес объекта недвижимости
     * 
     * @return адрес в формате "г. Город, ул. Улица, д. 10, кв. 25"
     */
    public String getPropertyFullAddress() {
        StringBuilder address = new StringBuilder();
        
        // Добавляем город
        if (cityName != null) {
            address.append("г. ").append(cityName);
        }
        
        // Добавляем улицу
        if (streetName != null) {
            if (!address.isEmpty()) address.append(", ");
            address.append("ул. ").append(streetName);
        }
        
        // Добавляем номер дома
        if (propertyHouseNumber != null) {
            if (!address.isEmpty()) address.append(", ");
            address.append("д. ").append(propertyHouseNumber);
            
            // Добавляем литеру дома если есть
            if (propertyHouseLetter != null) {
                address.append(propertyHouseLetter);
            }
        }
        
        // Добавляем корпус если есть
        if (propertyBuildingNumber != null) {
            address.append(", корп. ").append(propertyBuildingNumber);
        }
        
        // Добавляем квартиру если есть
        if (propertyApartmentNumber != null) {
            address.append(", кв. ").append(propertyApartmentNumber);
        }
        
        return address.toString();
    }
    
    /**
     * Получить отформатированную стоимость сделки
     * 
     * @return стоимость в формате "5 000 000 ₽"
     */
    public String getDealCostFormatted() {
        if (dealCost == null) return "Не указана";
        
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.of("ru", "RU"));
        return formatter.format(dealCost) + " ₽";
    }
    
    /**
     * Получить отформатированную дату сделки
     * 
     * @return дата в формате "15 января 2024"
     */
    public String getDealDateFormatted() {
        if (dealDate == null) return "Не указана";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.of("ru", "RU"));
        return dealDate.format(formatter);
    }
    
    /**
     * Получить отформатированную площадь недвижимости
     * 
     * @return площадь в формате "85.5 м²"
     */
    public String getPropertyAreaFormatted() {
        if (propertyArea == null) return "Не указана";
        
        return propertyArea + " м²";
    }
    

    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getDealId() { return dealId; }
    public void setDealId(Long dealId) { this.dealId = dealId; }
    
    public LocalDate getDealDate() { return dealDate; }
    public void setDealDate(LocalDate dealDate) { this.dealDate = dealDate; }
    
    public BigDecimal getDealCost() { return dealCost; }
    public void setDealCost(BigDecimal dealCost) { this.dealCost = dealCost; }
    
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public String getClientFirstName() { return clientFirstName; }
    public void setClientFirstName(String clientFirstName) { this.clientFirstName = clientFirstName; }
    
    public String getClientLastName() { return clientLastName; }
    public void setClientLastName(String clientLastName) { this.clientLastName = clientLastName; }
    
    public String getClientMiddleName() { return clientMiddleName; }
    public void setClientMiddleName(String clientMiddleName) { this.clientMiddleName = clientMiddleName; }
    
    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }
    
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    
    public Long getRealtorId() { return realtorId; }
    public void setRealtorId(Long realtorId) { this.realtorId = realtorId; }
    
    public String getRealtorFirstName() { return realtorFirstName; }
    public void setRealtorFirstName(String realtorFirstName) { this.realtorFirstName = realtorFirstName; }
    
    public String getRealtorLastName() { return realtorLastName; }
    public void setRealtorLastName(String realtorLastName) { this.realtorLastName = realtorLastName; }
    
    public String getRealtorMiddleName() { return realtorMiddleName; }
    public void setRealtorMiddleName(String realtorMiddleName) { this.realtorMiddleName = realtorMiddleName; }
    
    public String getRealtorPhone() { return realtorPhone; }
    public void setRealtorPhone(String realtorPhone) { this.realtorPhone = realtorPhone; }
    
    public String getRealtorEmail() { return realtorEmail; }
    public void setRealtorEmail(String realtorEmail) { this.realtorEmail = realtorEmail; }
    
    public Integer getRealtorExperience() { return realtorExperience; }
    public void setRealtorExperience(Integer realtorExperience) { this.realtorExperience = realtorExperience; }
    
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    
    public BigDecimal getPropertyArea() { return propertyArea; }
    public void setPropertyArea(BigDecimal propertyArea) { this.propertyArea = propertyArea; }
    
    public BigDecimal getPropertyCost() { return propertyCost; }
    public void setPropertyCost(BigDecimal propertyCost) { this.propertyCost = propertyCost; }
    
    public String getPropertyDescription() { return propertyDescription; }
    public void setPropertyDescription(String propertyDescription) { this.propertyDescription = propertyDescription; }
    
    public String getPropertyPostalCode() { return propertyPostalCode; }
    public void setPropertyPostalCode(String propertyPostalCode) { this.propertyPostalCode = propertyPostalCode; }
    
    public String getPropertyHouseNumber() { return propertyHouseNumber; }
    public void setPropertyHouseNumber(String propertyHouseNumber) { this.propertyHouseNumber = propertyHouseNumber; }
    
    public String getPropertyHouseLetter() { return propertyHouseLetter; }
    public void setPropertyHouseLetter(String propertyHouseLetter) { this.propertyHouseLetter = propertyHouseLetter; }
    
    public String getPropertyBuildingNumber() { return propertyBuildingNumber; }
    public void setPropertyBuildingNumber(String propertyBuildingNumber) { this.propertyBuildingNumber = propertyBuildingNumber; }
    
    public String getPropertyApartmentNumber() { return propertyApartmentNumber; }
    public void setPropertyApartmentNumber(String propertyApartmentNumber) { this.propertyApartmentNumber = propertyApartmentNumber; }
    
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }
    
    public String getStreetName() { return streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }
    
    public String getPropertyTypeName() { return propertyTypeName; }
    public void setPropertyTypeName(String propertyTypeName) { this.propertyTypeName = propertyTypeName; }
    
    public String getDealTypeName() { return dealTypeName; }
    public void setDealTypeName(String dealTypeName) { this.dealTypeName = dealTypeName; }
} 