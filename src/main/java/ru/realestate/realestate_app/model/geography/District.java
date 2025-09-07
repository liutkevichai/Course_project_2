package ru.realestate.realestate_app.model.geography;

public class District {
    private Long idDistrict;
    private String districtName;
    private Long idCity;
    
    public District() {}
    
    public District(Long idDistrict, String districtName) {
        this.idDistrict = idDistrict;
        this.districtName = districtName;
    }
    
    public District(Long idDistrict, String districtName, Long idCity) {
        this.idDistrict = idDistrict;
        this.districtName = districtName;
        this.idCity = idCity;
    }
    
    // Геттеры и сеттеры
    public Long getIdDistrict() {
        return idDistrict;
    }
    
    public void setIdDistrict(Long idDistrict) {
        this.idDistrict = idDistrict;
    }
    
    public String getDistrictName() {
        return districtName;
    }
    
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
    
    public Long getIdCity() {
        return idCity;
    }
    
    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }
    
    @Override
    public String toString() {
        return "District{" +
                "idDistrict=" + idDistrict +
                ", districtName='" + districtName + '\'' +
                ", idCity=" + idCity +
                '}';
    }
} 