package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.PaymentReportDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в PaymentReportDto
 */
@Component
public class PaymentReportRowMapper implements RowMapper<PaymentReportDto> {

    /**
     * Преобразует строку ResultSet в объект PaymentReportDto
     *
     * @param rs     строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект PaymentReportDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public PaymentReportDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        PaymentReportDto dto = new PaymentReportDto();

        dto.setId(rs.getLong("id_payment"));
        dto.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        dto.setAmount(rs.getBigDecimal("amount"));
        dto.setClientFullName(rs.getString("client_full_name"));
        dto.setDealTypeName(rs.getString("deal_type_name"));
        dto.setDealCost(rs.getBigDecimal("deal_cost"));

        return dto;
    }
}