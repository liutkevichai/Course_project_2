package ru.realestate.realestate_app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.model.PropertyType;

import java.util.List;

@Repository
public class PropertyTypeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Получить все типы недвижимости
     */
    public List<PropertyType> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM property_types ORDER BY property_type_name",
            (rs, _) -> new PropertyType(
                rs.getLong("id_property_type"),
                rs.getString("property_type_name")
            )
        );
    }

    /**
     * Найти тип недвижимости по ID
     */
    public PropertyType findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM property_types WHERE id_property_type = ?",
            (rs, _) -> new PropertyType(
                rs.getLong("id_property_type"),
                rs.getString("property_type_name")
            ),
            id
        );
    }

    /**
     * Найти тип недвижимости по названию
     */
    public PropertyType findByName(String name) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM property_types WHERE property_type_name = ?",
            (rs, _) -> new PropertyType(
                rs.getLong("id_property_type"),
                rs.getString("property_type_name")
            ),
            name
        );
    }
} 