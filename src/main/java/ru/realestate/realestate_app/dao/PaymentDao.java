package ru.realestate.realestate_app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.mapper.PaymentRowMapper;
import ru.realestate.realestate_app.mapper.dto.PaymentTableRowMapper;
import ru.realestate.realestate_app.model.Payment;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс для доступа к данным о платежах в базе данных
 */
@Repository
public class PaymentDao {
    private static final Logger logger = LoggerFactory.getLogger(PaymentDao.class);
    
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
        Number key = keyHolder.getKey();
        Long generatedId = null;
        if (key != null) {
            generatedId = key.longValue();
        }
        if (generatedId == null) {
            logger.error("Не удалось получить сгенерированный id для платежа");
            throw new DatabaseException("INSERT", "Не удалось создать платеж в базе данных");
        }
        payment.setIdPayment(generatedId);
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
                CONCAT(city.city_name, ', ', street.street_name, ', ', prop.house_number,
                       CASE WHEN prop.apartment_number IS NOT NULL THEN CONCAT('-', prop.apartment_number) ELSE '' END) as property_address
            FROM payments p
            JOIN deals d ON p.id_deal = d.id_deal
            JOIN clients c ON d.id_client = c.id_client
            JOIN properties prop ON d.id_property = prop.id_property
            JOIN streets street ON prop.id_street = street.id_street
            JOIN cities city ON prop.id_city = city.id_city
            ORDER BY p.payment_date DESC
            """;
        return jdbcTemplate.query(sql, paymentTableRowMapper);
    }

    /**
     * Осуществляет поиск платежей по заданным критериям с помощью динамического SQL-запроса.
     *
     * @param dealId     ID сделки для фильтрации (может быть null).
     * @param startDate  Начальная дата для поиска (может быть null).
     * @param endDate    Конечная дата для поиска (может быть null).
     * @return Список отфильтрованных платежей в формате PaymentTableDto.
     */
    public List<PaymentTableDto> searchPayments(Long dealId, LocalDate startDate, LocalDate endDate) {
        // Базовый SQL-запрос, который мы будем расширять
        String baseSql = """
            SELECT
                p.id_payment,
                p.payment_date,
                p.amount,
                d.id_deal,
                d.deal_date,
                CONCAT(c.last_name, ' ', c.first_name, CASE WHEN c.middle_name IS NOT NULL THEN CONCAT(' ', c.middle_name) ELSE '' END) as client_fio,
                CONCAT(city.city_name, ', ', street.street_name, ', ', prop.house_number, CASE WHEN prop.apartment_number IS NOT NULL THEN CONCAT('-', prop.apartment_number) ELSE '' END) as property_address
            FROM payments p
            JOIN deals d ON p.id_deal = d.id_deal
            JOIN clients c ON d.id_client = c.id_client
            JOIN properties prop ON d.id_property = prop.id_property
            JOIN streets street ON prop.id_street = street.id_street
            JOIN cities city ON prop.id_city = city.id_city
            """;

        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // Динамически добавляем условия в WHERE
        if (dealId != null) {
            whereClause.append("p.id_deal = ?");
            params.add(dealId);
        }
        if (startDate != null) {
            if (!whereClause.isEmpty()) whereClause.append(" AND ");
            whereClause.append("p.payment_date >= ?");
            params.add(startDate);
        }
        if (endDate != null) {
            if (!whereClause.isEmpty()) whereClause.append(" AND ");
            whereClause.append("p.payment_date < ?");
            params.add(endDate.plusDays(1));
        }

        String finalSql = baseSql;
        if (!whereClause.isEmpty()) {
            finalSql += " WHERE " + whereClause.toString();
        }
        finalSql += " ORDER BY p.payment_date DESC";

        return jdbcTemplate.query(finalSql, paymentTableRowMapper, params.toArray());
    }
}