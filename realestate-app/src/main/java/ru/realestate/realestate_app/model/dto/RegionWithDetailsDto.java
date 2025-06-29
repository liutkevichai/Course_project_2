package ru.realestate.realestate_app.model.dto;

/**
 * DTO для отображения региона с полной информацией о стране
 * 
 * Этот класс содержит информацию о регионе, включая:
 * - Основные данные региона (название, код)
 * - Полную информацию о родительской стране
 */
public class RegionWithDetailsDto {
    
    /**
     * Уникальный идентификатор региона
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
    public RegionWithDetailsDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param regionId идентификатор региона
     * @param regionName название региона
     * @param regionCode код региона
     * @param countryId идентификатор страны
     * @param countryName название страны
     */
    public RegionWithDetailsDto(Long regionId, String regionName, String regionCode, 
                               Long countryId, String countryName) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить полное название региона
     * 
     * @return название в формате "Московская область (Россия)" или "Московская область"
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (regionName != null) {
            fullName.append(regionName);
        }
        
        if (countryName != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append("(").append(countryName).append(")");
        }
        
        return fullName.toString();
    }
    
    /**
     * Получить название региона с сокращением
     * 
     * @return название с сокращением, например "Московская обл." или "Краснодарский край"
     */
    public String getRegionNameShort() {
        if (regionName == null) return "Регион не указан";
        
        String name = regionName;
        
        // Добавляем сокращения для типичных названий регионов
        if (name.contains("область")) {
            name = name.replace("область", "обл.");
        } else if (name.contains("республика")) {
            name = name.replace("республика", "респ.");
        }
        
        return name;
    }
    
    /**
     * Получить код региона с форматированием
     * 
     * @return код в формате "77 (Москва)" или просто код если нет названия
     */
    public String getRegionCodeFormatted() {
        if (regionCode == null) return "Код не указан";
        
        StringBuilder formatted = new StringBuilder(regionCode);
        
        if (regionName != null) {
            formatted.append(" (").append(regionName).append(")");
        }
        
        return formatted.toString();
    }
    
    /**
     * Получить иерархический путь
     * 
     * @return путь в формате "Россия / Московская область"
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
        
        return path.toString();
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
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