package ru.realestate.realestate_app.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * DTO для табличного отображения платежей
 * 
 * Компактный класс, содержащий только основную информацию о платеже
 * для отображения в таблицах и списках. Включает читабельные названия
 * вместо ID и предварительно отформатированные значения.
 */
public class PaymentTableDto {
    
    /**
     * Уникальный идентификатор платежа
     */
    private Long idPayment;
    
    /**
     * Дата совершения платежа
     */
    private LocalDate paymentDate;
    
    /**
     * Сумма платежа в рублях
     */
    private BigDecimal amount;
    
    /**
     * Уникальный идентификатор сделки
     */
    private Long idDeal;
    
    /**
     * Дата совершения сделки
     */
    private LocalDate dealDate;
    
    /**
     * Полное имя клиента (Фамилия Имя Отчество)
     */
    private String clientFio;
    
    /**
     * Краткий адрес объекта недвижимости
     */
    private String propertyAddress;
    
    // ========== КОНСТРУКТОРЫ ==========
    
    /**
     * Конструктор по умолчанию
     */
    public PaymentTableDto() {}
    
    /**
     * Конструктор для создания DTO из данных JOIN запроса
     * 
     * @param idPayment идентификатор платежа
     * @param paymentDate дата платежа
     * @param amount сумма платежа
     * @param idDeal идентификатор сделки
     * @param dealDate дата сделки
     * @param clientFio полное имя клиента
     * @param propertyAddress адрес недвижимости
     */
    public PaymentTableDto(Long idPayment, LocalDate paymentDate, BigDecimal amount,
                           Long idDeal, LocalDate dealDate, String clientFio, 
                           String propertyAddress) {
        this.idPayment = idPayment;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.idDeal = idDeal;
        this.dealDate = dealDate;
        this.clientFio = clientFio;
        this.propertyAddress = propertyAddress;
    }
    
    // ========== МЕТОДЫ ФОРМАТИРОВАНИЯ ==========
    
    /**
     * Получить отформатированную сумму платежа
     * 
     * @return сумма в формате "5 000 ₽"
     */
    public String getAmountFormatted() {
        if (amount == null) return "Не указана";
        
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.of("ru", "RU"));
        return formatter.format(amount) + " ₽";
    }
    
    /**
     * Получить отформатированную дату платежа
     * 
     * @return дата в формате "15.01.2024"
     */
    public String getPaymentDateFormatted() {
        if (paymentDate == null) return "Не указана";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return paymentDate.format(formatter);
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
     * Получить краткое имя клиента (Фамилия И. О.)
     * 
     * @return краткое имя для компактного отображения
     */
    public String getClientFioShort() {
        if (clientFio == null || clientFio.trim().isEmpty()) return "Не указан";
        
        String[] parts = clientFio.trim().split("\\s+");
        if (parts.length >= 3) {
            return String.format("%s %s. %s.", parts[0], parts[1].charAt(0), parts[2].charAt(0));
        } else if (parts.length == 2) {
            return String.format("%s %s.", parts[0], parts[1].charAt(0));
        }
        return clientFio;
    }
    
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========
    
    public Long getIdPayment() {
        return idPayment;
    }
    
    public void setIdPayment(Long idPayment) {
        this.idPayment = idPayment;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Long getIdDeal() {
        return idDeal;
    }
    
    public void setIdDeal(Long idDeal) {
        this.idDeal = idDeal;
    }
    
    public LocalDate getDealDate() {
        return dealDate;
    }
    
    public void setDealDate(LocalDate dealDate) {
        this.dealDate = dealDate;
    }
    
    public String getClientFio() {
        return clientFio;
    }
    
    public void setClientFio(String clientFio) {
        this.clientFio = clientFio;
    }
    
    public String getPropertyAddress() {
        return propertyAddress;
    }
    
    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }
}