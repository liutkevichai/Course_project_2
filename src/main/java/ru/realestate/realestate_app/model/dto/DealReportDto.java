package ru.realestate.realestate_app.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import ru.realestate.realestate_app.service.CsvExportService;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для отчета по сделкам
 */
public class DealReportDto {

    @CsvBindByName(column = "ID СДЕЛКИ")
    private Long id;

    @CsvDate("dd.MM.yyyy")
    @CsvBindByName(column = "ДАТА СДЕЛКИ")
    private LocalDate dealDate;

    @CsvBindByName(column = "СТОИМОСТЬ СДЕЛКИ, РУБ.")
    private String dealCost;

    @CsvBindByName(column = "АДРЕС НЕДВИЖИМОСТИ")
    private String propertyAddress;

    @CsvBindByName(column = "ФИО РИЕЛТОРА")
    private String realtorFullName;

    @CsvBindByName(column = "ФИО КЛИЕНТА")
    private String clientFullName;

    @CsvBindByName(column = "ТИП СДЕЛКИ")
    private String dealTypeName;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDealDate() {
        return dealDate;
    }

    public void setDealDate(LocalDate dealDate) {
        this.dealDate = dealDate;
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

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getRealtorFullName() {
        return realtorFullName;
    }

    public void setRealtorFullName(String realtorFullName) {
        this.realtorFullName = realtorFullName;
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
}