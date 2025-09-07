package ru.realestate.realestate_app.model.dto;

/**
 * DTO для отображения улицы с полной географической информацией
 * 
 * Этот класс содержит информацию об улице, включая:
 * - Основные данные улицы (название)
 * - Полную информацию о городе, регионе и стране
 */
public class StreetWithDetailsDto {
    
    /**
     * Уникальный идентификатор улицы
     */
    private Long streetId;
    
    /**
     * Название улицы
     */
    private String streetName;
    
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
    public StreetWithDetailsDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param streetId идентификатор улицы
     * @param streetName название улицы
     * @param cityId идентификатор города
     * @param cityName название города
     * @param regionId идентификатор региона
     * @param regionName название региона
     * @param countryId идентификатор страны
     * @param countryName название страны
     */
    public StreetWithDetailsDto(Long streetId, String streetName,
                               Long cityId, String cityName,
                               Long regionId, String regionName,
                               Long countryId, String countryName) {
        this.streetId = streetId;
        this.streetName = streetName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.regionId = regionId;
        this.regionName = regionName;
        this.countryId = countryId;
        this.countryName = countryName;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить полное название улицы
     * 
     * @return название в формате "ул. Тверская (г. Москва, Московская обл.)"
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (streetName != null) {
            // Добавляем сокращение если его нет
            if (!hasStreetPrefix(streetName)) {
                fullName.append("ул. ");
            }
            fullName.append(streetName);
        }
        
        // Добавляем географическую информацию в скобках
        StringBuilder locationInfo = new StringBuilder();
        
        if (cityName != null) {
            locationInfo.append("г. ").append(cityName);
        }
        
        if (regionName != null) {
            if (!locationInfo.isEmpty()) locationInfo.append(", ");
            
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            locationInfo.append(regionShort);
        }
        
        if (!locationInfo.isEmpty()) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append("(").append(locationInfo).append(")");
        }
        
        return fullName.toString();
    }
    
    /**
     * Получить краткое название улицы
     * 
     * @return название в формате "ул. Тверская"
     */
    public String getShortName() {
        if (streetName == null) return "Улица не указана";
        
        if (hasStreetPrefix(streetName)) {
            return streetName;
        }
        
        return "ул. " + streetName;
    }
    
    /**
     * Получить название улицы без префикса
     * 
     * @return название без "ул.", "пр.", "пер." и т.д.
     */
    public String getNameWithoutPrefix() {
        if (streetName == null) return "Улица не указана";
        
        String name = streetName.trim();
        
        // Убираем префиксы
        String[] prefixes = {"ул.", "улица", "пр.", "проспект", "пер.", "переулок", 
                           "б-р", "бульвар", "наб.", "набережная", "пл.", "площадь"};
        
        for (String prefix : prefixes) {
            if (name.toLowerCase().startsWith(prefix.toLowerCase())) {
                name = name.substring(prefix.length()).trim();
                break;
            }
        }
        
        return name;
    }
    
    /**
     * Получить тип улицы (улица, проспект, переулок и т.д.)
     * 
     * @return тип улицы или "улица" по умолчанию
     */
    public String getStreetType() {
        if (streetName == null) return "улица";
        
        String name = streetName.toLowerCase().trim();
        
        if (name.startsWith("пр.") || name.startsWith("проспект")) return "проспект";
        if (name.startsWith("пер.") || name.startsWith("переулок")) return "переулок";
        if (name.startsWith("б-р") || name.startsWith("бульвар")) return "бульвар";
        if (name.startsWith("наб.") || name.startsWith("набережная")) return "набережная";
        if (name.startsWith("пл.") || name.startsWith("площадь")) return "площадь";
        
        return "улица";
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
            if (!cityInfo.isEmpty()) cityInfo.append(" ");
            
            // Сокращаем название региона
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            cityInfo.append("(").append(regionShort).append(")");
        }
        
        return !cityInfo.isEmpty() ? cityInfo.toString() : "Город не указан";
    }
    
    /**
     * Получить полный иерархический путь
     * 
     * @return путь в формате "Россия / Московская область / Москва / ул. Тверская"
     */
    public String getHierarchyPath() {
        StringBuilder path = new StringBuilder();
        
        if (countryName != null) {
            path.append(countryName);
        }
        
        if (regionName != null) {
            if (!path.isEmpty()) path.append(" / ");
            path.append(regionName);
        }
        
        if (cityName != null) {
            if (!path.isEmpty()) path.append(" / ");
            path.append(cityName);
        }
        
        if (streetName != null) {
            if (!path.isEmpty()) path.append(" / ");
            path.append(getShortName());
        }
        
        return path.toString();
    }
    
    /**
     * Получить краткий иерархический путь
     * 
     * @return путь в формате "Москва / ул. Тверская"
     */
    public String getShortHierarchyPath() {
        StringBuilder path = new StringBuilder();
        
        if (cityName != null) {
            path.append(cityName);
        }
        
        if (streetName != null) {
            if (!path.isEmpty()) path.append(" / ");
            path.append(getShortName());
        }
        
        return path.toString();
    }
    
    /**
     * Получить название для карты или навигации
     * 
     * @return название в формате "ул. Тверская, Москва"
     */
    public String getMapDisplayName() {
        StringBuilder mapName = new StringBuilder();
        
        if (streetName != null) {
            mapName.append(getShortName());
        }
        
        if (cityName != null) {
            if (!mapName.isEmpty()) mapName.append(", ");
            mapName.append(cityName);
        }
        
        return mapName.toString();
    }
    
    /**
     * Получить данные для автодополнения адреса
     * 
     * @return строка для поиска и автодополнения
     */
    public String getSearchDisplayName() {
        StringBuilder searchName = new StringBuilder();
        
        if (streetName != null) {
            searchName.append(streetName);
        }
        
        if (cityName != null) {
            if (!searchName.isEmpty()) searchName.append(", ");
            searchName.append(cityName);
        }
        
        if (regionName != null && !regionName.equals(cityName)) {
            if (!searchName.isEmpty()) searchName.append(", ");
            
            // Сокращаем название региона для экономии места
            String regionShort = regionName;
            if (regionName.contains("область")) {
                regionShort = regionName.replace("область", "обл.");
            } else if (regionName.contains("республика")) {
                regionShort = regionName.replace("республика", "респ.");
            }
            
            searchName.append(regionShort);
        }
        
        return searchName.toString();
    }
    
    /**
     * Вспомогательный метод для проверки наличия префикса в названии улицы
     * 
     * @param name название улицы
     * @return true если есть префикс (ул., пр., пер. и т.д.)
     */
    private boolean hasStreetPrefix(String name) {
        if (name == null) return false;
        
        String lowerName = name.toLowerCase().trim();
        String[] prefixes = {"ул.", "улица", "пр.", "проспект", "пер.", "переулок", 
                           "б-р", "бульвар", "наб.", "набережная", "пл.", "площадь"};
        
        for (String prefix : prefixes) {
            if (lowerName.startsWith(prefix)) {
                return true;
            }
        }
        
        return false;
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getStreetId() {
        return streetId;
    }
    
    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }
    
    public String getStreetName() {
        return streetName;
    }
    
    public void setStreetName(String streetName) {
        this.streetName = streetName;
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