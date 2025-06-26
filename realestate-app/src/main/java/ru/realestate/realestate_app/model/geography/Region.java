package ru.realestate.realestate_app.model.geography;

public class Region {
    private Long idRegion;
    private String name;
    private String code;
    private Long idCountry;
    
    public Region() {}
    
    public Region(Long idRegion, String name, String code) {
        this.idRegion = idRegion;
        this.name = name;
        this.code = code;
    }
    
    public Region(Long idRegion, String name, String code, Long idCountry) {
        this.idRegion = idRegion;
        this.name = name;
        this.code = code;
        this.idCountry = idCountry;
    }
    
    // Геттеры и сеттеры
    public Long getIdRegion() {
        return idRegion;
    }
    
    public void setIdRegion(Long idRegion) {
        this.idRegion = idRegion;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Long getIdCountry() {
        return idCountry;
    }
    
    public void setIdCountry(Long idCountry) {
        this.idCountry = idCountry;
    }
    
    @Override
    public String toString() {
        return "Region{" +
                "idRegion=" + idRegion +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", idCountry=" + idCountry +
                '}';
    }
} 