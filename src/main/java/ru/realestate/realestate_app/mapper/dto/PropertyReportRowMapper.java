package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.PropertyReportDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в PropertyReportDto
 */
@Component
public class PropertyReportRowMapper implements RowMapper<PropertyReportDto> {
    
    /**
     * Преобразует строку ResultSet в объект PropertyReportDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект PropertyReportDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public PropertyReportDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PropertyReportDto dto = new PropertyReportDto();
        
        // Основные данные недвижимости
        dto.setId(rs.getLong("property_id"));
        dto.setArea(rs.getBigDecimal("area"));
        dto.setCost(rs.getBigDecimal("cost"));
        dto.setDescription(rs.getString("description"));
        dto.setPropertyTypeName(rs.getString("property_type_name"));
        dto.setPostalCode(rs.getString("postal_code"));
        dto.setHouseNumber(rs.getString("house_number"));
        dto.setHouseLetter(rs.getString("house_letter"));
        dto.setBuildingNumber(rs.getString("building_number"));
        dto.setApartmentNumber(rs.getString("apartment_number"));
        dto.setStreetName(rs.getString("street_name"));
        dto.setDistrictName(rs.getString("district_name"));
        dto.setCityName(rs.getString("city_name"));
        dto.setRegionCode(rs.getString("region_code"));
        dto.setRegionName(rs.getString("region_name"));
        dto.setCountryName(rs.getString("country_name"));
        
        return dto;
    }
}