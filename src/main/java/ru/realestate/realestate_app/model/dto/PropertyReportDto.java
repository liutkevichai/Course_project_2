package ru.realestate.realestate_app.model.dto;

import com.opencsv.bean.CsvBindByName;
import ru.realestate.realestate_app.service.CsvExportService;

import java.math.BigDecimal;

/**
 * DTO для отчета по недвижимости
 */
public class PropertyReportDto {

    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "ПЛОЩАДЬ, М2")
    private String area;

    @CsvBindByName(column = "СТОИМОСТЬ, РУБ.")
    private String cost;

    @CsvBindByName(column = "ОПИСАНИЕ")
    private String description;

    @CsvBindByName(column = "ТИП НЕДВИЖИМОСТИ")
    private String propertyTypeName;

    @CsvBindByName(column = "ПОЧТОВЫЙ ИНДЕКС")
    private String postalCode;

    @CsvBindByName(column = "НОМЕР ДОМА")
    private String houseNumber;

    @CsvBindByName(column = "ЛИТЕРА ДОМА")
    private String houseLetter;

    @CsvBindByName(column = "НОМЕР КОРПУСА")
    private String buildingNumber;

    @CsvBindByName(column = "НОМЕР КВАРТИРЫ")
    private String apartmentNumber;

    @CsvBindByName(column = "УЛИЦА")
    private String streetName;

    @CsvBindByName(column = "РАЙОН")
    private String districtName;

    @CsvBindByName(column = "ГОРОД")
    private String cityName;

    @CsvBindByName(column = "КОД РЕГИОНА")
    private String regionCode;

    @CsvBindByName(column = "РЕГИОН")
    private String regionName;

    @CsvBindByName(column = "СТРАНА")
    private String countryName;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setArea(BigDecimal area) {
        if (area != null) {
            this.area = new CsvExportService().formatBigDecimal(area);
        } else {
            this.area = null;
        }
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setCost(BigDecimal cost) {
        if (cost != null) {
            this.cost = new CsvExportService().formatBigDecimal(cost);
        } else {
            this.cost = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseLetter() {
        return houseLetter;
    }

    public void setHouseLetter(String houseLetter) {
        this.houseLetter = houseLetter;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}