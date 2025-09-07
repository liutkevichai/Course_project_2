package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.PropertyWithDetailsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в PropertyWithDetailsDto
 * 
 * Используется для получения детальной информации об объекте недвижимости
 * с включением всех связанных географических данных и типа недвижимости.
 * Предназначен для выполнения оптимизированных JOIN запросов.
 */
@Component
public class PropertyWithDetailsRowMapper implements RowMapper<PropertyWithDetailsDto> {
    
    /**
     * Преобразует строку ResultSet в объект PropertyWithDetailsDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект PropertyWithDetailsDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public PropertyWithDetailsDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PropertyWithDetailsDto dto = new PropertyWithDetailsDto();
        
        // Основные данные недвижимости
        dto.setPropertyId(rs.getLong("property_id"));
        dto.setArea(rs.getBigDecimal("area"));
        dto.setCost(rs.getBigDecimal("cost"));
        dto.setDescription(rs.getString("description"));
        dto.setPostalCode(rs.getString("postal_code"));
        dto.setHouseNumber(rs.getString("house_number"));
        dto.setHouseLetter(rs.getString("house_letter"));
        dto.setBuildingNumber(rs.getString("building_number"));
        dto.setApartmentNumber(rs.getString("apartment_number"));
        
        // Тип недвижимости
        dto.setPropertyTypeId(rs.getLong("property_type_id"));
        dto.setPropertyTypeName(rs.getString("property_type_name"));
        
        // Географические данные - страна
        dto.setCountryId(rs.getLong("country_id"));
        dto.setCountryName(rs.getString("country_name"));
        
        // Географические данные - регион
        dto.setRegionId(rs.getLong("region_id"));
        dto.setRegionName(rs.getString("region_name"));
        dto.setRegionCode(rs.getString("region_code"));
        
        // Географические данные - город
        dto.setCityId(rs.getLong("city_id"));
        dto.setCityName(rs.getString("city_name"));
        
        // Географические данные - район
        dto.setDistrictId(rs.getLong("district_id"));
        dto.setDistrictName(rs.getString("district_name"));
        
        // Географические данные - улица
        dto.setStreetId(rs.getLong("street_id"));
        dto.setStreetName(rs.getString("street_name"));
        
        return dto;
    }
} 