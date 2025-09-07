package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.DealWithDetailsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в DealWithDetailsDto
 * 
 * Используется для получения детальной информации о сделке с включением
 * всех связанных данных (клиент, риелтор, недвижимость, географическая информация).
 * Предназначен для выполнения оптимизированных JOIN запросов вместо множественных SELECT.
 */
@Component
public class DealWithDetailsRowMapper implements RowMapper<DealWithDetailsDto> {
    
    /**
     * Преобразует строку ResultSet в объект DealWithDetailsDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект DealWithDetailsDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public DealWithDetailsDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        DealWithDetailsDto dto = new DealWithDetailsDto();
        
        // Основные данные сделки
        dto.setDealId(rs.getLong("deal_id"));
        dto.setDealDate(rs.getDate("deal_date").toLocalDate());
        dto.setDealCost(rs.getBigDecimal("deal_cost"));
        
        // Данные клиента
        dto.setClientId(rs.getLong("client_id"));
        dto.setClientFirstName(rs.getString("client_first_name"));
        dto.setClientLastName(rs.getString("client_last_name"));
        dto.setClientMiddleName(rs.getString("client_middle_name"));
        dto.setClientPhone(rs.getString("client_phone"));
        dto.setClientEmail(rs.getString("client_email"));
        
        // Данные риелтора
        dto.setRealtorId(rs.getLong("realtor_id"));
        dto.setRealtorFirstName(rs.getString("realtor_first_name"));
        dto.setRealtorLastName(rs.getString("realtor_last_name"));
        dto.setRealtorMiddleName(rs.getString("realtor_middle_name"));
        dto.setRealtorPhone(rs.getString("realtor_phone"));
        dto.setRealtorEmail(rs.getString("realtor_email"));
        dto.setRealtorExperience(rs.getInt("realtor_experience"));
        
        // Данные недвижимости
        dto.setPropertyId(rs.getLong("property_id"));
        dto.setPropertyArea(rs.getBigDecimal("property_area"));
        dto.setPropertyCost(rs.getBigDecimal("property_cost"));
        dto.setPropertyDescription(rs.getString("property_description"));
        dto.setPropertyPostalCode(rs.getString("property_postal_code"));
        dto.setPropertyHouseNumber(rs.getString("property_house_number"));
        dto.setPropertyHouseLetter(rs.getString("property_house_letter"));
        dto.setPropertyBuildingNumber(rs.getString("property_building_number"));
        dto.setPropertyApartmentNumber(rs.getString("property_apartment_number"));
        
        // Географические данные (только названия)
        dto.setCountryName(rs.getString("country_name"));
        dto.setRegionName(rs.getString("region_name"));
        dto.setCityName(rs.getString("city_name"));
        dto.setDistrictName(rs.getString("district_name"));
        dto.setStreetName(rs.getString("street_name"));
        
        // Справочные данные (только названия)
        dto.setPropertyTypeName(rs.getString("property_type_name"));
        dto.setDealTypeName(rs.getString("deal_type_name"));
        
        return dto;
    }
} 