package ru.realestate.realestate_app.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private Long idPayment;
    
    @NotNull(message = "Дата платежа обязательна для заполнения")
    @PastOrPresent(message = "Дата платежа не может быть в будущем")
    private LocalDate paymentDate;
    
    @NotNull(message = "Сумма платежа обязательна для заполнения")
    @DecimalMin(value = "0.01", message = "Сумма платежа должна быть больше 0")
    @DecimalMax(value = "999999999.9999", message = "Сумма платежа не может превышать 999,999,999.9999")
    @Digits(integer = 9, fraction = 4, message = "Сумма платежа должна содержать не более 9 целых и 4 дробных цифр")
    private BigDecimal amount;
    
    @NotNull(message = "Сделка обязательна для заполнения")
    @Positive(message = "ID сделки должен быть положительным")
    private Long idDeal;
    
    // Конструкторы
    public Payment() {}
    
    public Payment(Long idPayment, LocalDate paymentDate, BigDecimal amount, Long idDeal) {
        this.idPayment = idPayment;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.idDeal = idDeal;
    }
    
    // Геттеры и сеттеры
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
    
    @Override
    public String toString() {
        return "Payment{" +
                "idPayment=" + idPayment +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", idDeal=" + idDeal +
                '}';
    }
} 