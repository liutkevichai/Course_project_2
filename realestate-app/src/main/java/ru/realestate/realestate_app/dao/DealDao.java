package ru.realestate.realestate_app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.DealRowMapper;
import ru.realestate.realestate_app.model.Deal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.lang.StringBuilder;

@Repository
public class DealDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DealRowMapper dealRowMapper;

    /**
     * Получить все сделки
     */
    public List<Deal> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM deals ORDER BY deal_date DESC",
            dealRowMapper
        );
    }

    /**
     * Найти сделку по ID
     */
    public Deal findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM deals WHERE id_deal = ?",
            dealRowMapper,
            id
        );
    }

    /**
     * Сохранить новую сделку
     */
    @SuppressWarnings({ "null" })
    public Long save(Deal deal) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO deals (deal_date, deal_cost, id_property, id_realtor, id_client, id_deal_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            ps.setDate(1, java.sql.Date.valueOf(deal.getDealDate()));
            ps.setBigDecimal(2, deal.getDealCost());
            ps.setInt(3, deal.getIdProperty());
            ps.setInt(4, deal.getIdRealtor());
            ps.setInt(5, deal.getIdClient());
            ps.setInt(6, deal.getIdDealType());
            
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    /**
     * Обновление сделки
     * @param id ID сделки для обновления
     * @param updates Map с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем, что есть поля для обновления
        if (updates == null || updates.isEmpty()) {
            return false;
        }
        
        // Строим динамический SQL запрос
        StringBuilder sql = new StringBuilder("UPDATE deals SET ");
        List<Object> params = new ArrayList<>();
        
        // Добавляем поля для обновления
        if (updates.containsKey("dealDate")) {
            sql.append("deal_date = ?, ");
            params.add(updates.get("dealDate"));
        }
        if (updates.containsKey("dealCost")) {
            sql.append("deal_cost = ?, ");
            params.add(updates.get("dealCost"));
        }
        if (updates.containsKey("idDealType")) {
            sql.append("id_deal_type = ?, ");
            params.add(updates.get("idDealType"));
        }
        if (updates.containsKey("idProperty")) {
            sql.append("id_property = ?, ");
            params.add(updates.get("idProperty"));
        }
        if (updates.containsKey("idClient")) {
            sql.append("id_client = ?, ");
            params.add(updates.get("idClient"));
        }
        if (updates.containsKey("idRealtor")) {
            sql.append("id_realtor = ?, ");
            params.add(updates.get("idRealtor"));
        }
        
        // Убираем последнюю запятую и пробел
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.setLength(sql.length() - 2);
        }
        
        // Добавляем условие WHERE
        sql.append(" WHERE id_deal = ?");
        params.add(id);
        
        // Выполняем обновление
        int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
        return updatedRows > 0;
    }

    /**
     * Удалить сделку по ID
     */
    public boolean deleteById(Long id) {
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM deals WHERE id_deal = ?",
            id
        );
        
        return deletedRows > 0;
    }

    /**
     * Найти сделки по дате
     */
    public List<Deal> findByDate(LocalDate date) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE deal_date = ? ORDER BY deal_cost DESC",
            dealRowMapper,
            java.sql.Date.valueOf(date)
        );
    }

    /**
     * Найти сделки по диапазону дат
     */
    public List<Deal> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE deal_date BETWEEN ? AND ? ORDER BY deal_date DESC",
            dealRowMapper,
            java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)
        );
    }

    /**
     * Найти сделки по риелтору
     */
    public List<Deal> findByRealtorId(Integer realtorId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_realtor = ? ORDER BY deal_date DESC",
            dealRowMapper,
            realtorId
        );
    }

    /**
     * Найти сделки по клиенту
     */
    public List<Deal> findByClientId(Integer clientId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_client = ? ORDER BY deal_date DESC",
            dealRowMapper,
            clientId
        );
    }

    /**
     * Найти сделки по недвижимости
     */
    public List<Deal> findByPropertyId(Integer propertyId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_property = ? ORDER BY deal_date DESC",
            dealRowMapper,
            propertyId
        );
    }

    /**
     * Найти сделки по типу сделки
     */
    public List<Deal> findByDealTypeId(Integer dealTypeId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_deal_type = ? ORDER BY deal_date DESC",
            dealRowMapper,
            dealTypeId
        );
    }

    /**
     * Найти сделки по ценовому диапазону
     */
    public List<Deal> findByCostRange(BigDecimal minCost, BigDecimal maxCost) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE deal_cost BETWEEN ? AND ? ORDER BY deal_cost DESC",
            dealRowMapper,
            minCost, maxCost
        );
    }

    /**
     * Получить общую сумму сделок
     */
    public BigDecimal getTotalDealsAmount() {
        BigDecimal total = jdbcTemplate.queryForObject(
            "SELECT SUM(deal_cost) FROM deals",
            BigDecimal.class
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Получить количество сделок
     */
    public int getCount() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM deals",
            Integer.class
        );
        return count != null ? count : 0;
    }
} 