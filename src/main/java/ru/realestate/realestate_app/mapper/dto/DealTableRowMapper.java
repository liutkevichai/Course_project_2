package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.DealTableDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в DealTableDto
 * 
 * Используется для получения компактной информации о сделках для табличного отображения.
 * Включает только основные данные необходимые для списков и таблиц.
 */
@Component
public class DealTableRowMapper implements RowMapper<DealTableDto> {
    
    /**
     * Преобразует строку ResultSet в объект DealTableDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект DealTableDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public DealTableDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        DealTableDto dto = new DealTableDto();
        
        // Основные данные сделки
        dto.setDealId(rs.getLong("deal_id"));
        dto.setDealDate(rs.getDate("deal_date").toLocalDate());
        dto.setDealCost(rs.getBigDecimal("deal_cost"));
        
        // Данные клиента (полное имя)
        dto.setClientName(rs.getString("client_name"));
        dto.setClientPhone(rs.getString("client_phone"));
        
        // Данные риелтора (полное имя)
        dto.setRealtorName(rs.getString("realtor_name"));
        
        // Адрес недвижимости (краткий)
        dto.setPropertyAddress(rs.getString("property_address"));
        
        // Справочные данные
        dto.setPropertyTypeName(rs.getString("property_type_name"));
        dto.setDealTypeName(rs.getString("deal_type_name"));
        
        return dto;
    }
} 