package ru.realestate.realestate_app.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.Property;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PropertyRowMapper implements RowMapper<Property> {
    
    @Override
    public Property mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Property property = new Property();
        
        // Основные поля
        property.setIdProperty(rs.getLong("id_property"));
        property.setArea(rs.getBigDecimal("area"));
        property.setCost(rs.getBigDecimal("cost"));
        property.setDescription(rs.getString("description"));
        property.setPostalCode(rs.getString("postal_code"));
        property.setHouseNumber(rs.getString("house_number"));
        property.setHouseLetter(rs.getString("house_letter"));
        property.setBuildingNumber(rs.getString("building_number"));
        property.setApartmentNumber(rs.getString("apartment_number"));
        
        // Внешние ключи
        property.setIdPropertyType(rs.getInt("id_property_type"));
        property.setIdCountry(rs.getInt("id_country"));
        property.setIdRegion(rs.getInt("id_region"));
        property.setIdCity(rs.getInt("id_city"));
        property.setIdDistrict(rs.getInt("id_district"));
        property.setIdStreet(rs.getInt("id_street"));
        
        return property;
    }
} 