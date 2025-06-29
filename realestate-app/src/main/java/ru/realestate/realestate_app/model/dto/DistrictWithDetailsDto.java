package ru.realestate.realestate_app.model.dto;

/**
 * DTO для отображения района с полной географической информацией
 * 
 * Этот класс содержит информацию о районе, включая:
 * - Основные данные района (название)
 * - Полную информацию о городе, регионе и стране
 */
public class DistrictWithDetailsDto {
    
    /**
     * Уникальный идентификатор района
     */
    private Long districtId;
    
    /**
     * Название района
     */
    private String districtName;
    
    /**
     * Идентификатор города
     */
    private Long cityId;
    
    /**
     * Название города
     */
    private String cityName;
    
    /**
     * Идентификатор региона
     */
    private Long regionId;
    
    /**
     * Название региона
     */
    private String regionName;
    
    /**
     * Идентификатор страны
     */
    private Long countryId;
    
    /**
     * Название страны
     */
    private String countryName;
    
    // ========== КОНСТРУКТОРЫ ==========
    
    /**
     * Конструктор по умолчанию
     */
    public DistrictWithDetailsDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param districtId идентификатор района
     * @param districtName название района
     * @param cityId идентификатор города
     * @param cityName название города
     * @param regionId идентификатор региона
     * @param regionName название региона
     * @param countryId идентификатор страны
     * @param countryName название страны
     */
    public DistrictWithDetailsDto(Long districtId, String districtName,
                                 Long cityId, String cityName,
                                 Long regionId, String regionName,
                                 Long countryId, String countryName) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.regionId = regionId;
        this.regionName = regionName;
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить полное название района
     * 
     * @return название в формате "Центральный р-н (г. Москва, Московская обл.)"
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (districtName != null) {
            fullName.append(districtName);
            if (!districtName.toLowerCase().contains("район")) {
                fullName.append(" р-н");
            }
        }
        
        // Добавляем географическую информацию в скобках
        StringBuilder locationInfo = new StringBuilder();
        
        if (cityName != null) {
            locationInfo.append("г. ").append(cityName);
        }
        
        if (regionName != null) {
            if (locationInfo.length() > 0) locationInfo.append(", ");
            
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            locationInfo.append(regionShort);
        }
        
        if (locationInfo.length() > 0) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append("(").append(locationInfo).append(")");
        }
        
        return fullName.toString();
    }
    
    /**
     * Получить краткое название района
     * 
     * @return название в формате "Центральный р-н"
     */
    public String getShortName() {
        if (districtName == null) return "Район не указан";
        
        if (districtName.toLowerCase().contains("район")) {
            return districtName;
        }
        
        return districtName + " р-н";
    }
    
    /**
     * Получить информацию о городе
     * 
     * @return город в формате "г. Москва (Московская обл.)"
     */
    public String getCityInfo() {
        StringBuilder cityInfo = new StringBuilder();
        
        if (cityName != null) {
            cityInfo.append("г. ").append(cityName);
        }
        
        if (regionName != null) {
            if (cityInfo.length() > 0) cityInfo.append(" ");
            
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            cityInfo.append("(").append(regionShort).append(")");
        }
        
        return cityInfo.length() > 0 ? cityInfo.toString() : "Город не указан";
    }
    
    /**
     * Получить полный иерархический путь
     * 
     * @return путь в формате "Россия / Московская область / Москва / Центральный район"
     */
    public String getHierarchyPath() {
        StringBuilder path = new StringBuilder();
        
        if (countryName != null) {
            path.append(countryName);
        }
        
        if (regionName != null) {
            if (path.length() > 0) path.append(" / ");
            path.append(regionName);
        }
        
        if (cityName != null) {
            if (path.length() > 0) path.append(" / ");
            path.append(cityName);
        }
        
        if (districtName != null) {
            if (path.length() > 0) path.append(" / ");
            path.append(districtName);
            if (!districtName.toLowerCase().contains("район")) {
                path.append(" район");
            }
        }
        
        return path.toString();
    }
    
    /**
     * Получить краткий иерархический путь
     * 
     * @return путь в формате "Москва / Центральный р-н"
     */
    public String getShortHierarchyPath() {
        StringBuilder path = new StringBuilder();
        
        if (cityName != null) {
            path.append(cityName);
        }
        
        if (districtName != null) {
            if (path.length() > 0) path.append(" / ");
            path.append(districtName);
            if (!districtName.toLowerCase().contains("район")) {
                path.append(" р-н");
            }
        }
        
        return path.toString();
    }
    
    /**
     * Получить название для карты или навигации
     * 
     * @return название в формате "Центральный район, Москва"
     */
    public String getMapDisplayName() {
        StringBuilder mapName = new StringBuilder();
        
        if (districtName != null) {
            mapName.append(districtName);
            if (!districtName.toLowerCase().contains("район")) {
                mapName.append(" район");
            }
        }
        
        if (cityName != null) {
            if (mapName.length() > 0) mapName.append(", ");
            mapName.append(cityName);
        }
        
        return mapName.toString();
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getDistrictId() {
        return districtId;
    }
    
    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
    
    public String getDistrictName() {
        return districtName;
    }
    
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
    
    public Long getCityId() {
        return cityId;
    }
    
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public Long getRegionId() {
        return regionId;
    }
    
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    
    public String getRegionName() {
        return regionName;
    }
    
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    
    public Long getCountryId() {
        return countryId;
    }
    
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
} 