package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.RegionWithDetailsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в RegionWithDetailsDto
 * 
 * Используется для получения информации о регионе с включением
 * данных о стране. Предназначен для выполнения JOIN запросов.
 */
@Component
public class RegionWithDetailsRowMapper implements RowMapper<RegionWithDetailsDto> {
    
    /**
     * Преобразует строку ResultSet в объект RegionWithDetailsDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект RegionWithDetailsDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public RegionWithDetailsDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        RegionWithDetailsDto dto = new RegionWithDetailsDto();
        
        // Основные данные региона
        dto.setRegionId(rs.getLong("region_id"));
        dto.setRegionName(rs.getString("region_name"));
        dto.setRegionCode(rs.getString("region_code"));
        
        // Данные страны
        dto.setCountryId(rs.getLong("country_id"));
        dto.setCountryName(rs.getString("country_name"));
        
        return dto;
    }
} 