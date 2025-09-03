package ru.realestate.realestate_app.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.PaymentRowMapper;
import ru.realestate.realestate_app.mapper.dto.PaymentTableRowMapper;
import ru.realestate.realestate_app.model.Payment;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Класс для доступа к данным о платежах в базе данных
 */
@Repository
public class PaymentDao {
    private final JdbcTemplate jdbcTemplate;
    private final PaymentRowMapper paymentRowMapper;
    private final PaymentTableRowMapper paymentTableRowMapper;

    public PaymentDao(JdbcTemplate jdbcTemplate, PaymentRowMapper paymentRowMapper, PaymentTableRowMapper paymentTableRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.paymentRowMapper = paymentRowMapper;
        this.paymentTableRowMapper = paymentTableRowMapper;
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments";
        return jdbcTemplate.query(sql, paymentRowMapper);
    }

    public Optional<Payment> findById(Long id) {
        String sql = "SELECT * FROM payments WHERE id_payment = ?";
        List<Payment> payments = jdbcTemplate.query(sql, paymentRowMapper, id);
        return payments.stream().findFirst();
    }

    public Payment save(Payment payment) {
        String sql = "INSERT INTO payments (payment_date, amount, id_deal) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_payment"});
            ps.setObject(1, payment.getPaymentDate());
            ps.setBigDecimal(2, payment.getAmount());
            ps.setLong(3, payment.getIdDeal());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            payment.setIdPayment(keyHolder.getKey().longValue());
        }
        return payment;
    }

    public Payment update(Payment payment) {
        String sql = "UPDATE payments SET payment_date = ?, amount = ?, id_deal = ? WHERE id_payment = ?";
        jdbcTemplate.update(sql,
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getIdDeal(),
                payment.getIdPayment());
        return payment;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM payments WHERE id_payment = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Payment> findByDealId(Long dealId) {
        String sql = "SELECT * FROM payments WHERE id_deal = ?";
        return jdbcTemplate.query(sql, paymentRowMapper, dealId);
    }
    
    /**
     * Получить все платежи с детальной информацией для табличного отображения
     * @return список всех платежей с дополнительной информацией
     */
    public List<PaymentTableDto> findAllWithDetails() {
        String sql = """
            SELECT
                p.id_payment,
                p.payment_date,
                p.amount,
                d.id_deal,
                d.deal_date,
                -- Клиент (полное имя)
                CONCAT(c.last_name, ' ', c.first_name,
                       CASE WHEN c.middle_name IS NOT NULL THEN CONCAT(' ', c.middle_name) ELSE '' END) as client_fio,
                -- Адрес недвижимости (краткий)
                CONCAT(street.street_name, ', ', prop.house_number,
                       CASE WHEN prop.apartment_number IS NOT NULL THEN CONCAT('-', prop.apartment_number) ELSE '' END) as property_address
            FROM payments p
            JOIN deals d ON p.id_deal = d.id_deal
            JOIN clients c ON d.id_client = c.id_client
            JOIN properties prop ON d.id_property = prop.id_property
            JOIN streets street ON prop.id_street = street.id_street
            ORDER BY p.payment_date DESC
            """;
        return jdbcTemplate.query(sql, paymentTableRowMapper);
    }
}