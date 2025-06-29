package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.StreetWithDetailsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в StreetWithDetailsDto
 * 
 * Используется для получения информации об улице с включением
 * полной географической иерархии (район, город, регион, страна).
 */
@Component
public class StreetWithDetailsRowMapper implements RowMapper<StreetWithDetailsDto> {
    
    /**
     * Преобразует строку ResultSet в объект StreetWithDetailsDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект StreetWithDetailsDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public StreetWithDetailsDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        StreetWithDetailsDto dto = new StreetWithDetailsDto();
        
        // Основные данные улицы
        dto.setStreetId(rs.getLong("street_id"));
        dto.setStreetName(rs.getString("street_name"));
        
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