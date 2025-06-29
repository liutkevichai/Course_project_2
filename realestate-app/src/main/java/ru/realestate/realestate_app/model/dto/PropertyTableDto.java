package ru.realestate.realestate_app.model.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * DTO для табличного отображения недвижимости
 * 
 * Компактный класс, содержащий только основную информацию об объекте недвижимости
 * для отображения в таблицах и каталогах. Включает читабельные названия
 * вместо ID и предварительно отформатированные значения.
 */
public class PropertyTableDto {
    
    /**
     * Уникальный идентификатор объекта недвижимости
     */
    private Long propertyId;
    
    /**
     * Тип недвижимости (квартира, дом, офис и т.д.)
     */
    private String propertyTypeName;
    
    /**
     * Площадь объекта в квадратных метрах
     */
    private BigDecimal area;
    
    /**
     * Стоимость объекта недвижимости
     */
    private BigDecimal cost;
    
    /**
     * Краткое описание объекта (первые 100 символов)
     */
    private String shortDescription;
    
    /**
     * Город расположения
     */
    private String cityName;
    
    /**
     * Район города
     */
    private String districtName;
    
    /**
     * Название улицы
     */
    private String streetName;
    
    /**
     * Номер дома
     */
    private String houseNumber;
    
    /**
     * Номер квартиры (может быть null для домов)
     */
    private String apartmentNumber;
    
    // ========== КОНСТРУКТОРЫ ==========
    
    /**
     * Конструктор по умолчанию
     */
    public PropertyTableDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param propertyId идентификатор недвижимости
     * @param propertyTypeName тип недвижимости
     * @param area площадь
     * @param cost стоимость
     * @param shortDescription краткое описание
     * @param cityName название города
     * @param districtName название района
     * @param streetName название улицы
     * @param houseNumber номер дома
     * @param apartmentNumber номер квартиры
     */
    public PropertyTableDto(Long propertyId, String propertyTypeName, BigDecimal area, BigDecimal cost,
                           String shortDescription, String cityName, String districtName, 
                           String streetName, String houseNumber, String apartmentNumber) {
        this.propertyId = propertyId;
        this.propertyTypeName = propertyTypeName;
        this.area = area;
        this.cost = cost;
        this.shortDescription = shortDescription;
        this.cityName = cityName;
        this.districtName = districtName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить краткий адрес для таблицы
     * 
     * @return адрес в формате "ул. Тверская, 10-25" или "ул. Тверская, 10"
     */
    public String getTableAddress() {
        StringBuilder address = new StringBuilder();
        
        // Добавляем улицу
        if (streetName != null) {
            address.append("ул. ").append(streetName);
        }
        
        // Добавляем номер дома
        if (houseNumber != null) {
            if (address.length() > 0) address.append(", ");
            address.append(houseNumber);
            
            // Добавляем квартиру через дефис для компактности
            if (apartmentNumber != null) {
                address.append("-").append(apartmentNumber);
            }
        }
        
        return address.length() > 0 ? address.toString() : "Адрес не указан";
    }
    
    /**
     * Получить информацию о местоположении
     * 
     * @return местоположение в формате "г. Москва, Центральный р-н"
     */
    public String getLocationInfo() {
        StringBuilder location = new StringBuilder();
        
        if (cityName != null) {
            location.append("г. ").append(cityName);
        }
        
        if (districtName != null) {
            if (location.length() > 0) location.append(", ");
            location.append(districtName).append(" р-н");
        }
        
        return location.length() > 0 ? location.toString() : "Местоположение не указано";
    }
    
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
        if (area == null) return "Не указана";
        
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
        
        return cost.divide(area, 0, RoundingMode.HALF_UP);
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
     * Получить краткую информацию об объекте
     * 
     * @return информация в формате "Квартира, 85.5 м², 5 000 000 ₽"
     */
    public String getPropertySummary() {
        StringBuilder summary = new StringBuilder();
        
        if (propertyTypeName != null) {
            summary.append(propertyTypeName);
        }
        
        if (area != null) {
            if (summary.length() > 0) summary.append(", ");
            summary.append(getAreaFormatted());
        }
        
        if (cost != null) {
            if (summary.length() > 0) summary.append(", ");
            summary.append(getCostFormatted());
        }
        
        return summary.toString();
    }
    
    /**
     * Получить очень краткое описание (первые 50 символов)
     * 
     * @return краткое описание с многоточием если обрезано
     */
    public String getVeryShortDescription() {
        if (shortDescription == null || shortDescription.trim().isEmpty()) {
            return "Описание отсутствует";
        }
        
        String description = shortDescription.trim();
        if (description.length() <= 50) {
            return description;
        }
        
        return description.substring(0, 47) + "...";
    }
    
    /**
     * Получить CSS класс для типа недвижимости (для стилизации в веб-интерфейсе)
     * 
     * @return CSS класс в зависимости от типа недвижимости
     */
    public String getPropertyTypeClass() {
        if (propertyTypeName == null) return "property-unknown";
        
        return switch (propertyTypeName.toLowerCase()) {
            case "квартира" -> "property-apartment";
            case "дом" -> "property-house";
            case "офис" -> "property-office";
            case "склад" -> "property-warehouse";
            case "магазин" -> "property-shop";
            case "гараж" -> "property-garage";
            case "участок" -> "property-land";
            default -> "property-other";
        };
    }
    
    /**
     * Получить цветовую категорию цены (для визуализации)
     * 
     * @return категория цены: "low", "medium", "high", "premium"
     */
    public String getPriceCategory() {
        if (cost == null) return "unknown";
        
        long costLong = cost.longValue();
        
        if (costLong < 3_000_000) return "low";           // до 3 млн
        if (costLong < 7_000_000) return "medium";        // 3-7 млн
        if (costLong < 15_000_000) return "high";         // 7-15 млн
        return "premium";                                  // свыше 15 млн
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getPropertyId() {
        return propertyId;
    }
    
    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
    
    public String getPropertyTypeName() {
        return propertyTypeName;
    }
    
    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
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
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public String getDistrictName() {
        return districtName;
    }
    
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
    
    public String getStreetName() {
        return streetName;
    }
    
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    
    public String getHouseNumber() {
        return houseNumber;
    }
    
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    
    public String getApartmentNumber() {
        return apartmentNumber;
    }
    
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
} 