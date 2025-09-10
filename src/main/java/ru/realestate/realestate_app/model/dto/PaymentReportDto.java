package ru.realestate.realestate_app.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import ru.realestate.realestate_app.service.CsvExportService;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для отчета по платежам
 */
public class PaymentReportDto {

    @CsvBindByName(column = "ID ПЛАТЕЖА")
    private Long id;

    @CsvDate("dd.MM.yyyy")
    @CsvBindByName(column = "ДАТА ПЛАТЕЖА")
    private LocalDate paymentDate;

    @CsvBindByName(column = "СУММА ПЛАТЕЖА, РУБ.")
    private String amount;

    @CsvBindByName(column = "ФИО КЛИЕНТА")
    private String clientFullName;

    @CsvBindByName(column = "ТИП СДЕЛКИ")
    private String dealTypeName;

    @CsvBindByName(column = "СТОИМОСТЬ СДЕЛКИ, РУБ.")
    private String dealCost;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = new CsvExportService().formatBigDecimal(amount);
        } else {
            this.amount = null;
        }
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public String getDealTypeName() {
        return dealTypeName;
    }

    public void setDealTypeName(String dealTypeName) {
        this.dealTypeName = dealTypeName;
    }

    public String getDealCost() {
        return dealCost;
    }

    public void setDealCost(String dealCost) {
        this.dealCost = dealCost;
    }

    public void setDealCost(BigDecimal dealCost) {
        if (dealCost != null) {
            this.dealCost = new CsvExportService().formatBigDecimal(dealCost);
        } else {
            this.dealCost = null;
        }
    }
}