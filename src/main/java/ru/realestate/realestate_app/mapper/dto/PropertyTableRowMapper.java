package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.PropertyTableDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в PropertyTableDto
 * 
 * Используется для получения компактной информации об объектах недвижимости
 * для табличного отображения в каталогах и списках.
 */
@Component
public class PropertyTableRowMapper implements RowMapper<PropertyTableDto> {
    
    /**
     * Преобразует строку ResultSet в объект PropertyTableDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект PropertyTableDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public PropertyTableDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PropertyTableDto dto = new PropertyTableDto();
        
        // Основные данные недвижимости
        dto.setPropertyId(rs.getLong("property_id"));
        dto.setArea(rs.getBigDecimal("area"));
        dto.setCost(rs.getBigDecimal("cost"));
        dto.setShortDescription(rs.getString("short_description"));
        dto.setHouseNumber(rs.getString("house_number"));
        dto.setHouseLetter(rs.getString("house_letter"));
        dto.setBuildingNumber(rs.getString("building_number"));
        dto.setApartmentNumber(rs.getString("apartment_number"));
        
        // Тип недвижимости
        dto.setPropertyTypeName(rs.getString("property_type_name"));
        
        // Географические данные для адреса
        dto.setCityName(rs.getString("city_name"));
        dto.setDistrictName(rs.getString("district_name"));
        dto.setStreetName(rs.getString("street_name"));
        
        return dto;
    }
} 