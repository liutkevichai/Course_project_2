package ru.realestate.realestate_app.model.geography;

public class Street {
    private Long idStreet;
    private String streetName;
    private Long idCity;
    
    public Street() {}
    
    public Street(Long idStreet, String streetName) {
        this.idStreet = idStreet;
        this.streetName = streetName;
    }
    
    public Street(Long idStreet, String streetName, Long idCity) {
        this.idStreet = idStreet;
        this.streetName = streetName;
        this.idCity = idCity;
    }
    
    // Геттеры и сеттеры
    public Long getIdStreet() {
        return idStreet;
    }
    
    public void setIdStreet(Long idStreet) {
        this.idStreet = idStreet;
    }
    
    public String getStreetName() {
        return streetName;
    }
    
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    
    public Long getIdCity() {
        return idCity;
    }
    
    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }
    
    @Override
    public String toString() {
        return "Street{" +
                "idStreet=" + idStreet +
                ", streetName='" + streetName + '\'' +
                ", idCity=" + idCity +
                '}';
    }
} 