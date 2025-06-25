package ru.realestate.realestate_app.model.geography;

public class District {
    private Long idDistrict;
    private String districtName;
    
    // Конструкторы
    public District() {}
    
    public District(Long idDistrict, String districtName) {
        this.idDistrict = idDistrict;
        this.districtName = districtName;
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
    
    @Override
    public String toString() {
        return "District{" +
                "idDistrict=" + idDistrict +
                ", districtName='" + districtName + '\'' +
                '}';
    }
} 