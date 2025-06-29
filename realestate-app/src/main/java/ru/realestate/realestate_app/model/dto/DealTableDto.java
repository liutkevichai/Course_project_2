package ru.realestate.realestate_app.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * DTO для табличного отображения сделок
 * 
 * Компактный класс, содержащий только основную информацию о сделке
 * для отображения в таблицах и списках. Включает читабельные названия
 * вместо ID и предварительно отформатированные значения.
 */
public class DealTableDto {
    
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
    
    /**
     * Полное имя клиента (Фамилия Имя)
     */
    private String clientName;
    
    /**
     * Телефон клиента для связи
     */
    private String clientPhone;
    
    /**
     * Полное имя риелтора (Фамилия Имя)
     */
    private String realtorName;
    
    /**
     * Краткий адрес объекта недвижимости (город, улица, дом)
     */
    private String propertyAddress;
    
    /**
     * Тип недвижимости (квартира, дом, офис и т.д.)
     */
    private String propertyTypeName;
    
    /**
     * Тип сделки (продажа, аренда и т.д.)
     */
    private String dealTypeName;
    
    // ========== КОНСТРУКТОРЫ ==========
    
    /**
     * Конструктор по умолчанию
     */
    public DealTableDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param dealId идентификатор сделки
     * @param dealDate дата сделки
     * @param dealCost стоимость сделки
     * @param clientName полное имя клиента
     * @param clientPhone телефон клиента
     * @param realtorName полное имя риелтора
     * @param propertyAddress адрес недвижимости
     * @param propertyTypeName тип недвижимости
     * @param dealTypeName тип сделки
     */
    public DealTableDto(Long dealId, LocalDate dealDate, BigDecimal dealCost,
                       String clientName, String clientPhone, String realtorName,
                       String propertyAddress, String propertyTypeName, String dealTypeName) {
        this.dealId = dealId;
        this.dealDate = dealDate;
        this.dealCost = dealCost;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.realtorName = realtorName;
        this.propertyAddress = propertyAddress;
        this.propertyTypeName = propertyTypeName;
        this.dealTypeName = dealTypeName;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
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
     * @return дата в формате "15.01.2024"
     */
    public String getDealDateFormatted() {
        if (dealDate == null) return "Не указана";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return dealDate.format(formatter);
    }
    
    /**
     * Получить краткое имя клиента (Фамилия И.)
     * 
     * @return краткое имя для компактного отображения
     */
    public String getClientNameShort() {
        if (clientName == null || clientName.trim().isEmpty()) return "Не указан";
        
        String[] parts = clientName.trim().split("\\s+");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1].charAt(0) + ".";
        }
        return clientName;
    }
    
    /**
     * Получить краткое имя риелтора (Фамилия И.)
     * 
     * @return краткое имя для компактного отображения
     */
    public String getRealtorNameShort() {
        if (realtorName == null || realtorName.trim().isEmpty()) return "Не указан";
        
        String[] parts = realtorName.trim().split("\\s+");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1].charAt(0) + ".";
        }
        return realtorName;
    }
    
    /**
     * Получить очень краткий адрес (только улица и дом)
     * 
     * @return адрес в формате "ул. Ленина, 10"
     */
    public String getPropertyAddressShort() {
        if (propertyAddress == null || propertyAddress.trim().isEmpty()) return "Адрес не указан";
        
        // Извлекаем только улицу и дом из полного адреса
        String[] parts = propertyAddress.split(",");
        StringBuilder shortAddress = new StringBuilder();
        
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("ул.") || part.startsWith("д.")) {
                if (shortAddress.length() > 0) shortAddress.append(", ");
                shortAddress.append(part);
            }
        }
        
        return shortAddress.length() > 0 ? shortAddress.toString() : propertyAddress;
    }
    
    /**
     * Получить CSS класс для статуса сделки (для стилизации в веб-интерфейсе)
     * 
     * @return CSS класс в зависимости от типа сделки
     */
    public String getDealTypeClass() {
        if (dealTypeName == null) return "deal-unknown";
        
        return switch (dealTypeName.toLowerCase()) {
            case "продажа" -> "deal-sale";
            case "аренда" -> "deal-rent";
            case "обмен" -> "deal-exchange";
            default -> "deal-other";
        };
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getDealId() {
        return dealId;
    }
    
    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }
    
    public LocalDate getDealDate() {
        return dealDate;
    }
    
    public void setDealDate(LocalDate dealDate) {
        this.dealDate = dealDate;
    }
    
    public BigDecimal getDealCost() {
        return dealCost;
    }
    
    public void setDealCost(BigDecimal dealCost) {
        this.dealCost = dealCost;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public String getClientPhone() {
        return clientPhone;
    }
    
    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
    
    public String getRealtorName() {
        return realtorName;
    }
    
    public void setRealtorName(String realtorName) {
        this.realtorName = realtorName;
    }
    
    public String getPropertyAddress() {
        return propertyAddress;
    }
    
    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }
    
    public String getPropertyTypeName() {
        return propertyTypeName;
    }
    
    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }
    
    public String getDealTypeName() {
        return dealTypeName;
    }
    
    public void setDealTypeName(String dealTypeName) {
        this.dealTypeName = dealTypeName;
    }
} 