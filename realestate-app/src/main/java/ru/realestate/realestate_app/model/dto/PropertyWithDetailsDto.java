package ru.realestate.realestate_app.model.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * DTO для детального просмотра объекта недвижимости с полной информацией
 * 
 * Этот класс содержит всю информацию об объекте недвижимости, включая:
 * - Основные характеристики (площадь, стоимость, описание)
 * - Полный адрес с названиями (страна, регион, город, район, улица)
 * - Детали адреса (дом, корпус, квартира, индекс)
 * - Тип недвижимости
 */
public class PropertyWithDetailsDto {
    
    // ========== ОСНОВНЫЕ ДАННЫЕ НЕДВИЖИМОСТИ ==========
    
    /**
     * Уникальный идентификатор объекта недвижимости
     */
    private Long propertyId;
    
    /**
     * Площадь объекта в квадратных метрах
     */
    private BigDecimal area;
    
    /**
     * Стоимость объекта недвижимости
     */
    private BigDecimal cost;
    
    /**
     * Описание объекта недвижимости
     */
    private String description;
    
    // ========== АДРЕСНАЯ ИНФОРМАЦИЯ ==========
    
    /**
     * Почтовый индекс
     */
    private String postalCode;
    
    /**
     * Номер дома
     */
    private String houseNumber;
    
    /**
     * Литера дома (может быть null)
     */
    private String houseLetter;
    
    /**
     * Номер корпуса (может быть null)
     */
    private String buildingNumber;
    
    /**
     * Номер квартиры (может быть null - для частных домов)
     */
    private String apartmentNumber;
    
    // ========== ГЕОГРАФИЧЕСКАЯ ИНФОРМАЦИЯ ==========
    
    /**
     * Идентификатор страны
     */
    private Long countryId;
    
    /**
     * Название страны
     */
    private String countryName;
    
    /**
     * Идентификатор региона
     */
    private Long regionId;
    
    /**
     * Название региона
     */
    private String regionName;
    
    /**
     * Код региона (например, для субъектов РФ)
     */
    private String regionCode;
    
    /**
     * Идентификатор города
     */
    private Long cityId;
    
    /**
     * Название города
     */
    private String cityName;
    
    /**
     * Идентификатор района
     */
    private Long districtId;
    
    /**
     * Название района
     */
    private String districtName;
    
    /**
     * Идентификатор улицы
     */
    private Long streetId;
    
    /**
     * Название улицы
     */
    private String streetName;
    
    // ========== ТИП НЕДВИЖИМОСТИ ==========
    
    /**
     * Идентификатор типа недвижимости
     */
    private Long propertyTypeId;
    
    /**
     * Название типа недвижимости (квартира, дом, офис и т.д.)
     */
    private String propertyTypeName;
    
    // ========== КОНСТРУКТОРЫ ==========
    
    /**
     * Конструктор по умолчанию
     */
    public PropertyWithDetailsDto() {}
    
    /**
     * Полный конструктор для создания DTO из данных JOIN запроса
     * 
     * @param propertyId идентификатор недвижимости
     * @param area площадь
     * @param cost стоимость
     * @param description описание
     * @param postalCode почтовый индекс
     * @param houseNumber номер дома
     * @param houseLetter литера дома
     * @param buildingNumber номер корпуса
     * @param apartmentNumber номер квартиры
     * @param countryId идентификатор страны
     * @param countryName название страны
     * @param regionId идентификатор региона
     * @param regionName название региона
     * @param regionCode код региона
     * @param cityId идентификатор города
     * @param cityName название города
     * @param districtId идентификатор района
     * @param districtName название района
     * @param streetId идентификатор улицы
     * @param streetName название улицы
     * @param propertyTypeId идентификатор типа недвижимости
     * @param propertyTypeName название типа недвижимости
     */
    public PropertyWithDetailsDto(Long propertyId, BigDecimal area, BigDecimal cost, String description,
                                 String postalCode, String houseNumber, String houseLetter, 
                                 String buildingNumber, String apartmentNumber,
                                 Long countryId, String countryName,
                                 Long regionId, String regionName, String regionCode,
                                 Long cityId, String cityName,
                                 Long districtId, String districtName,
                                 Long streetId, String streetName,
                                 Long propertyTypeId, String propertyTypeName) {
        this.propertyId = propertyId;
        this.area = area;
        this.cost = cost;
        this.description = description;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.houseLetter = houseLetter;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.countryId = countryId;
        this.countryName = countryName;
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.cityId = cityId;
        this.cityName = cityName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.streetId = streetId;
        this.streetName = streetName;
        this.propertyTypeId = propertyTypeId;
        this.propertyTypeName = propertyTypeName;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ АДРЕСА ==========
    
    /**
     * Получить полный адрес объекта недвижимости
     * 
     * @return адрес в формате "Россия, Московская обл., г. Москва, Центральный р-н, ул. Тверская, д. 10, кв. 25"
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        
        // Добавляем страну
        if (countryName != null) {
            address.append(countryName);
        }
        
        // Добавляем регион
        if (regionName != null) {
            if (address.length() > 0) address.append(", ");
            address.append(regionName);
            
            // Добавляем сокращение для регионов
            if (regionName.contains("область")) {
                address.append(" обл.");
            } else if (regionName.contains("край")) {
                address.append(" край");
            } else if (regionName.contains("республика")) {
                address.append(" респ.");
            }
        }
        
        // Добавляем город
        if (cityName != null) {
            if (address.length() > 0) address.append(", ");
            address.append("г. ").append(cityName);
        }
        
        // Добавляем район
        if (districtName != null) {
            if (address.length() > 0) address.append(", ");
            address.append(districtName).append(" р-н");
        }
        
        // Добавляем улицу
        if (streetName != null) {
            if (address.length() > 0) address.append(", ");
            address.append("ул. ").append(streetName);
        }
        
        // Добавляем номер дома
        if (houseNumber != null) {
            if (address.length() > 0) address.append(", ");
            address.append("д. ").append(houseNumber);
            
            // Добавляем литеру дома если есть
            if (houseLetter != null) {
                address.append(houseLetter);
            }
        }
        
        // Добавляем корпус если есть
        if (buildingNumber != null) {
            address.append(", корп. ").append(buildingNumber);
        }
        
        // Добавляем квартиру если есть
        if (apartmentNumber != null) {
            address.append(", кв. ").append(apartmentNumber);
        }
        
        return address.toString();
    }
    
    /**
     * Получить краткий адрес для списков
     * 
     * @return адрес в формате "г. Москва, ул. Тверская, д. 10"
     */
    public String getShortAddress() {
        StringBuilder address = new StringBuilder();
        
        // Добавляем город
        if (cityName != null) {
            address.append("г. ").append(cityName);
        }
        
        // Добавляем улицу
        if (streetName != null) {
            if (address.length() > 0) address.append(", ");
            address.append("ул. ").append(streetName);
        }
        
        // Добавляем номер дома
        if (houseNumber != null) {
            if (address.length() > 0) address.append(", ");
            address.append("д. ").append(houseNumber);
            
            // Добавляем литеру дома если есть
            if (houseLetter != null) {
                address.append(houseLetter);
            }
        }
        
        return address.toString();
    }
    
    /**
     * Получить адрес для почтовой отправки
     * 
     * @return адрес с индексом в начале
     */
    public String getPostalAddress() {
        StringBuilder address = new StringBuilder();
        
        // Добавляем индекс в начало
        if (postalCode != null) {
            address.append(postalCode).append(", ");
        }
        
        // Добавляем полный адрес
        address.append(getFullAddress());
        
        return address.toString();
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ДАННЫХ ==========
    
    /**
     * Получить отформатированную стоимость
     * 
     * @return стоимость в формате "5 000 000 ₽"
     */
    public String getCostFormatted() {
        if (cost == null) return "Цена не указана";
        
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.of("ru", "RU"));
        return formatter.format(cost) + " ₽";
    }
    
    /**
     * Получить отформатированную площадь
     * 
     * @return площадь в формате "85.5 м²"
     */
    public String getAreaFormatted() {
        if (area == null) return "Площадь не указана";
        
        return area + " м²";
    }
    
    /**
     * Получить стоимость за квадратный метр
     * 
     * @return стоимость за м² в рублях
     */
    public BigDecimal getCostPerSquareMeter() {
        if (cost == null || area == null || area.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return cost.divide(area, 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Получить отформатированную стоимость за квадратный метр
     * 
     * @return стоимость за м² в формате "58 824 ₽/м²"
     */
    public String getCostPerSquareMeterFormatted() {
        BigDecimal costPerM2 = getCostPerSquareMeter();
        if (costPerM2.compareTo(BigDecimal.ZERO) == 0) {
            return "Не рассчитано";
        }
        
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.of("ru", "RU"));
        return formatter.format(costPerM2) + " ₽/м²";
    }
    
    /**
     * Получить тип жилья для удобного отображения
     * 
     * @return тип с артиклем, например "Квартира", "Дом", "Офис"
     */
    public String getPropertyTypeFormatted() {
        if (propertyTypeName == null) return "Тип не указан";
        
        return switch (propertyTypeName.toLowerCase()) {
            case "квартира" -> "Квартира";
            case "дом" -> "Частный дом";
            case "офис" -> "Офисное помещение";
            case "склад" -> "Складское помещение";
            case "магазин" -> "Торговое помещение";
            case "гараж" -> "Гараж";
            case "участок" -> "Земельный участок";
            default -> propertyTypeName;
        };
    }
    
    /**
     * Получить иконку для типа недвижимости (для веб-интерфейса)
     * 
     * @return название иконки
     */
    public String getPropertyTypeIcon() {
        if (propertyTypeName == null) return "home";
        
        return switch (propertyTypeName.toLowerCase()) {
            case "квартира" -> "apartment";
            case "дом" -> "house";
            case "офис" -> "business";
            case "склад" -> "warehouse";
            case "магазин" -> "store";
            case "гараж" -> "garage";
            case "участок" -> "landscape";
            default -> "home";
        };
    }
    
    /**
     * Проверить, является ли недвижимость жилой
     * 
     * @return true для квартир и домов
     */
    public boolean isResidential() {
        if (propertyTypeName == null) return false;
        
        String type = propertyTypeName.toLowerCase();
        return type.equals("квартира") || type.equals("дом");
    }
    
    /**
     * Проверить, является ли недвижимость коммерческой
     * 
     * @return true для офисов, складов, магазинов
     */
    public boolean isCommercial() {
        if (propertyTypeName == null) return false;
        
        String type = propertyTypeName.toLowerCase();
        return type.equals("офис") || type.equals("склад") || type.equals("магазин");
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    
    public BigDecimal getArea() { return area; }
    public void setArea(BigDecimal area) { this.area = area; }
    
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    
    public String getHouseLetter() { return houseLetter; }
    public void setHouseLetter(String houseLetter) { this.houseLetter = houseLetter; }
    
    public String getBuildingNumber() { return buildingNumber; }
    public void setBuildingNumber(String buildingNumber) { this.buildingNumber = buildingNumber; }
    
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
    
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    
    public Long getRegionId() { return regionId; }
    public void setRegionId(Long regionId) { this.regionId = regionId; }
    
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public Long getDistrictId() { return districtId; }
    public void setDistrictId(Long districtId) { this.districtId = districtId; }
    
    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }
    
    public Long getStreetId() { return streetId; }
    public void setStreetId(Long streetId) { this.streetId = streetId; }
    
    public String getStreetName() { return streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }
    
    public Long getPropertyTypeId() { return propertyTypeId; }
    public void setPropertyTypeId(Long propertyTypeId) { this.propertyTypeId = propertyTypeId; }
    
    public String getPropertyTypeName() { return propertyTypeName; }
    public void setPropertyTypeName(String propertyTypeName) { this.propertyTypeName = propertyTypeName; }
} 