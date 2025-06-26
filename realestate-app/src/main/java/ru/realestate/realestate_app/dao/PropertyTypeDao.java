package ru.realestate.realestate_app.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.model.PropertyType;

import java.util.List;

/**
 * DAO класс для работы с типами недвижимости
 * Обеспечивает доступ к справочнику типов недвижимости в базе данных
 */
@Repository
public class PropertyTypeDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     */
    public PropertyTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получить все типы недвижимости, отсортированные по названию
     * @return список всех типов недвижимости
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
     * Найти тип недвижимости по уникальному идентификатору
     * @param id идентификатор типа недвижимости
     * @return объект типа недвижимости
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
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
     * Найти тип недвижимости по названию (точное совпадение)
     * @param name название типа недвижимости
     * @return объект типа недвижимости
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
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