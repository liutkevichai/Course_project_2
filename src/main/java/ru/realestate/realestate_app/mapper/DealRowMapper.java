package ru.realestate.realestate_app.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ru.realestate.realestate_app.model.Deal;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DealRowMapper implements RowMapper<Deal> {
    
    @Override
    public Deal mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Deal deal = new Deal();
        
        deal.setIdDeal(rs.getLong("id_deal"));
        
        // Обработка даты (deal_date NOT NULL в БД)
        java.sql.Date dealDate = rs.getDate("deal_date");
        if (dealDate == null) {
            throw new SQLException("Дата сделки не может быть null для записи " + rowNum);
        }
        deal.setDealDate(dealDate.toLocalDate());
        
        deal.setDealCost(rs.getBigDecimal("deal_cost"));
        deal.setIdProperty(rs.getLong("id_property"));
        deal.setIdRealtor(rs.getLong("id_realtor"));
        deal.setIdClient(rs.getLong("id_client"));
        deal.setIdDealType(rs.getLong("id_deal_type"));
        
        return deal;
    }
} 