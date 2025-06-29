package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.CityWithDetailsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в CityWithDetailsDto
 * 
 * Используется для получения информации о городе с включением
 * данных о регионе и стране. Предназначен для выполнения JOIN запросов.
 */
@Component
public class CityWithDetailsRowMapper implements RowMapper<CityWithDetailsDto> {
    
    /**
     * Преобразует строку ResultSet в объект CityWithDetailsDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект CityWithDetailsDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public CityWithDetailsDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        CityWithDetailsDto dto = new CityWithDetailsDto();
        
        // Основные данные города
        dto.setCityId(rs.getLong("city_id"));
        dto.setCityName(rs.getString("city_name"));
        
        // Данные региона
        dto.setRegionId(rs.getLong("region_id"));
        dto.setRegionName(rs.getString("region_name"));
        dto.setRegionCode(rs.getString("region_code"));
        
        // Данные страны
        dto.setCountryId(rs.getLong("country_id"));
        dto.setCountryName(rs.getString("country_name"));
        
        return dto;
    }
} 