package ru.realestate.realestate_app.mapper.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.dto.DealReportDto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper для преобразования результата JOIN запроса в DealReportDto
 */
@Component
public class DealReportRowMapper implements RowMapper<DealReportDto> {

    /**
     * Преобразует строку ResultSet в объект DealReportDto
     *
     * @param rs     строка результата SQL запроса
     * @param rowNum номер строки (не используется)
     * @return заполненный объект DealReportDto
     * @throws SQLException если произошла ошибка при чтении данных из ResultSet
     */
    @Override
    public DealReportDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        DealReportDto dto = new DealReportDto();

        dto.setId(rs.getLong("id_deal"));
        dto.setDealDate(rs.getDate("deal_date").toLocalDate());
        dto.setDealCost(rs.getBigDecimal("deal_cost"));
        dto.setPropertyAddress(rs.getString("property_address"));
        dto.setRealtorFullName(rs.getString("realtor_full_name"));
        dto.setClientFullName(rs.getString("client_full_name"));
        dto.setDealTypeName(rs.getString("deal_type_name"));

        return dto;
    }
}