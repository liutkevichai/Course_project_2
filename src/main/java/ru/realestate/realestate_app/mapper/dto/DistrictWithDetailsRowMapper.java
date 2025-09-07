package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.DistrictWithDetailsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в DistrictWithDetailsDto
 * 
 * Используется для получения информации о районе с включением
 * полной географической иерархии (город, регион, страна).
 */
@Component
public class DistrictWithDetailsRowMapper implements RowMapper<DistrictWithDetailsDto> {
    
    /**
     * Преобразует строку ResultSet в объект DistrictWithDetailsDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект DistrictWithDetailsDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public DistrictWithDetailsDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        DistrictWithDetailsDto dto = new DistrictWithDetailsDto();
        
        // Основные данные района
        dto.setDistrictId(rs.getLong("district_id"));
        dto.setDistrictName(rs.getString("district_name"));
        
        // Данные города
        dto.setCityId(rs.getLong("city_id"));
        dto.setCityName(rs.getString("city_name"));
        
        // Данные региона
        dto.setRegionId(rs.getLong("region_id"));
        dto.setRegionName(rs.getString("region_name"));
        
        // Данные страны
        dto.setCountryId(rs.getLong("country_id"));
        dto.setCountryName(rs.getString("country_name"));
        
        return dto;
    }
} 