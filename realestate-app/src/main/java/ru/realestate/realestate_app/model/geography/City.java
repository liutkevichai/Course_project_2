package ru.realestate.realestate_app.model.geography;

public class City {
    private Long idCity;
    private String cityName;
    private Long idRegion;
    
    public City() {}
    
    public City(Long idCity, String cityName) {
        this.idCity = idCity;
        this.cityName = cityName;
    }
    
    public City(Long idCity, String cityName, Long idRegion) {
        this.idCity = idCity;
        this.cityName = cityName;
        this.idRegion = idRegion;
    }
    
    // Геттеры и сеттеры
    public Long getIdCity() {
        return idCity;
    }
    
    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public Long getIdRegion() {
        return idRegion;
    }
    
    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }
    
    @Override
    public String toString() {
        return "City{" +
                "idCity=" + idCity +
                ", cityName='" + cityName + '\'' +
                ", idRegion=" + idRegion +
                '}';
    }
} 