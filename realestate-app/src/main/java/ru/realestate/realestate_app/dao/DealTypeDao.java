package ru.realestate.realestate_app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.model.DealType;

import java.util.List;

@Repository
public class DealTypeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Получить все типы сделок
     */
    public List<DealType> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM deal_types ORDER BY deal_type_name",
            (rs, _) -> new DealType(
                rs.getLong("id_deal_type"),
                rs.getString("deal_type_name")
            )
        );
    }

    /**
     * Найти тип сделки по ID
     */
    public DealType findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM deal_types WHERE id_deal_type = ?",
            (rs, _) -> new DealType(
                rs.getLong("id_deal_type"),
                rs.getString("deal_type_name")
            ),
            id
        );
    }

    /**
     * Найти тип сделки по названию
     */
    public DealType findByName(String name) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM deal_types WHERE deal_type_name = ?",
            (rs, _) -> new DealType(
                rs.getLong("id_deal_type"),
                rs.getString("deal_type_name")
            ),
            name
        );
    }
} 