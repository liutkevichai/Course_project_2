package ru.realestate.realestate_app.model.dto;

/**
 * DTO для отображения города с полной географической информацией
 * 
 * Этот класс содержит информацию о городе, включая:
 * - Основные данные города (название)
 * - Полную информацию о регионе и стране
 */
public class CityWithDetailsDto {
    
    /**
     * Уникальный идентификатор города
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
     * Код региона
     */
    private String regionCode;
    
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
    public CityWithDetailsDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param cityId идентификатор города
     * @param cityName название города
     * @param regionId идентификатор региона
     * @param regionName название региона
     * @param regionCode код региона
     * @param countryId идентификатор страны
     * @param countryName название страны
     */
    public CityWithDetailsDto(Long cityId, String cityName, 
                             Long regionId, String regionName, String regionCode,
                             Long countryId, String countryName) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить полное название города
     * 
     * @return название в формате "г. Москва (Московская обл., Россия)"
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (cityName != null) {
            fullName.append("г. ").append(cityName);
        }
        
        // Добавляем регион
        if (regionName != null) {
            if (fullName.length() > 0) fullName.append(" (");
            
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            fullName.append(regionShort);
            
            // Добавляем страну
            if (countryName != null) {
                fullName.append(", ").append(countryName);
            }
            
            fullName.append(")");
        }
        
        return fullName.toString();
    }
    
    /**
     * Получить краткое название города
     * 
     * @return название в формате "г. Москва"
     */
    public String getShortName() {
        if (cityName == null) return "Город не указан";
        
        return "г. " + cityName;
    }
    
    /**
     * Получить информацию о регионе
     * 
     * @return регион в формате "Московская обл. (Россия)"
     */
    public String getRegionInfo() {
        StringBuilder regionInfo = new StringBuilder();
        
        if (regionName != null) {
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            regionInfo.append(regionShort);
        }
        
        if (countryName != null) {
            if (regionInfo.length() > 0) regionInfo.append(" ");
            regionInfo.append("(").append(countryName).append(")");
        }
        
        return regionInfo.length() > 0 ? regionInfo.toString() : "Регион не указан";
    }
    
    /**
     * Получить полный иерархический путь
     * 
     * @return путь в формате "Россия / Московская область / Москва"
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
        
        return path.toString();
    }
    
    /**
     * Получить краткий иерархический путь
     * 
     * @return путь в формате "Московская обл. / Москва"
     */
    public String getShortHierarchyPath() {
        StringBuilder path = new StringBuilder();
        
        if (regionName != null) {
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            path.append(regionShort);
        }
        
        if (cityName != null) {
            if (path.length() > 0) path.append(" / ");
            path.append(cityName);
        }
        
        return path.toString();
    }
    
    /**
     * Проверить, является ли город столицей региона
     * (Упрощенная проверка по названию)
     * 
     * @return true если название города похоже на название региона
     */
    public boolean isRegionalCapital() {
        if (cityName == null || regionName == null) return false;
        
        // Упрощенная проверка: если название города содержится в названии региона
        String cityLower = cityName.toLowerCase();
        String regionLower = regionName.toLowerCase();
        
        return regionLower.contains(cityLower) || cityLower.contains(regionLower.split("\\s+")[0]);
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
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
    
    public String getRegionCode() {
        return regionCode;
    }
    
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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