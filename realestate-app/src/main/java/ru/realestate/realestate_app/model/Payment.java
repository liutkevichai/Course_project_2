package ru.realestate.realestate_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private Long idPayment;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private Integer idDeal;
    
    // Конструкторы
    public Payment() {}
    
    public Payment(Long idPayment, LocalDate paymentDate, BigDecimal amount, Integer idDeal) {
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
    
    public Integer getIdDeal() {
        return idDeal;
    }
    
    public void setIdDeal(Integer idDeal) {
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