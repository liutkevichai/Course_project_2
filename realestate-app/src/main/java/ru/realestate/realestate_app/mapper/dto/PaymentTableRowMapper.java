package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в PaymentTableDto
 * 
 * Используется для получения компактной информации о платежах для табличного отображения.
 * Включает только основные данные необходимые для списков и таблиц.
 */
@Component
public class PaymentTableRowMapper implements RowMapper<PaymentTableDto> {
    
    /**
     * Преобразует строку ResultSet в объект PaymentTableDto
     * 
     * @param rs строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект PaymentTableDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public PaymentTableDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PaymentTableDto dto = new PaymentTableDto();
        
        // Основные данные платежа
        dto.setIdPayment(rs.getLong("id_payment"));
        dto.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        dto.setAmount(rs.getBigDecimal("amount"));
        
        // Данные сделки
        dto.setIdDeal(rs.getLong("id_deal"));
        dto.setDealDate(rs.getDate("deal_date").toLocalDate());
        
        // Данные клиента (полное имя)
        dto.setClientFio(rs.getString("client_fio"));
        
        // Адрес недвижимости (краткий)
        dto.setPropertyAddress(rs.getString("property_address"));
        
        return dto;
    }
}