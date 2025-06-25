package ru.realestate.realestate_app.model.geography;

public class Street {
    private Long idStreet;
    private String streetName;
    
    // Конструкторы
    public Street() {}
    
    public Street(Long idStreet, String streetName) {
        this.idStreet = idStreet;
        this.streetName = streetName;
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
    
    @Override
    public String toString() {
        return "Street{" +
                "idStreet=" + idStreet +
                ", streetName='" + streetName + '\'' +
                '}';
    }
} 