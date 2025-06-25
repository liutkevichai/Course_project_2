package ru.realestate.realestate_app.model.geography;

public class City {
    private Long idCity;
    private String cityName;
    
    // Конструкторы
    public City() {}
    
    public City(Long idCity, String cityName) {
        this.idCity = idCity;
        this.cityName = cityName;
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
    
    @Override
    public String toString() {
        return "City{" +
                "idCity=" + idCity +
                ", cityName='" + cityName + '\'' +
                '}';
    }
} 