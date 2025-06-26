package ru.realestate.realestate_app.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.model.DealType;

import java.util.List;

/**
 * DAO класс для работы с типами сделок
 * Обеспечивает доступ к справочнику типов сделок в базе данных
 */
@Repository
public class DealTypeDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     */
    public DealTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получить все типы сделок, отсортированные по названию
     * @return список всех типов сделок
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
     * Найти тип сделки по уникальному идентификатору
     * @param id идентификатор типа сделки
     * @return объект типа сделки
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
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
     * Найти тип сделки по названию (точное совпадение)
     * @param name название типа сделки
     * @return объект типа сделки
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
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