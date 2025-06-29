package ru.realestate.realestate_app.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.realestate.realestate_app.model.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PaymentRowMapper implements RowMapper<Payment> {
    
    @Override
    public Payment mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();
        
        payment.setIdPayment(rs.getLong("id_payment"));
        
        // Обработка даты (payment_date NOT NULL в БД)
        java.sql.Date paymentDate = rs.getDate("payment_date");
        if (paymentDate == null) {
            throw new SQLException("Дата оплаты не может быть null для записи " + rowNum);
        }
        payment.setPaymentDate(paymentDate.toLocalDate());
        
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setIdDeal(rs.getLong("id_deal"));
        
        return payment;
    }
} 