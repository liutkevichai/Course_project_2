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

    /**
     * Литера дома (может быть null)
     */
    private String houseLetter;

    /**
     * Номер корпуса (может быть null)
     */
    private String buildingNumber;
    
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
                           String streetName, String houseNumber, String houseLetter, String buildingNumber, String apartmentNumber) {
        this.propertyId = propertyId;
        this.propertyTypeName = propertyTypeName;
        this.area = area;
        this.cost = cost;
        this.shortDescription = shortDescription;
        this.cityName = cityName;
        this.districtName = districtName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.houseLetter = houseLetter;
        this.buildingNumber = buildingNumber;
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
            if (!address.isEmpty()) address.append(", ");
            address.append(houseNumber);

            // Добавляем литеру дома если есть
            if (houseLetter != null) {
                address.append(houseLetter);
            }

            // Добавляем корпус если есть
            if (buildingNumber != null) {
                address.append("к").append(buildingNumber);
            }

            // Добавляем квартиру через дефис для компактности
            if (apartmentNumber != null) {
                address.append("-").append(apartmentNumber);
            }
        }
        
        return !address.isEmpty() ? address.toString() : "Адрес не указан";
    }
    
    /**
     * Получить информацию о местоположении
     * 
     * @return местоположение в формате "г. Москва, ул. Лесная, 4"
     */
    public String getAddressWithCity() {
        StringBuilder address = new StringBuilder();
        
        if (cityName != null) {
            address.append("г. ").append(cityName);
        }
        
        // Добавляем улицу
        if (streetName != null) {
            if (!address.isEmpty())
                address.append(", ");
            address.append("ул. ").append(streetName);
        }

        // Добавляем номер дома
        if (houseNumber != null) {
            if (!address.isEmpty())
                address.append(", ");
            address.append(houseNumber);

            // Добавляем литеру дома если есть
            if (houseLetter != null) {
                address.append(houseLetter);
            }

            // Добавляем корпус если есть
            if (buildingNumber != null) {
                address.append("к").append(buildingNumber);
            }

            // Добавляем квартиру через дефис для компактности
            if (apartmentNumber != null) {
                address.append("-").append(apartmentNumber);
            }
        }
        
        return !address.isEmpty() ? address.toString() : "Местоположение не указано";
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
            if (!summary.isEmpty()) summary.append(", ");
            summary.append(getAreaFormatted());
        }
        
        if (cost != null) {
            if (!summary.isEmpty()) summary.append(", ");
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
}