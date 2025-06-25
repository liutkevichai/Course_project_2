package ru.realestate.realestate_app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.RealtorRowMapper;
import ru.realestate.realestate_app.model.Realtor;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.lang.StringBuilder;

@Repository
public class RealtorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RealtorRowMapper realtorRowMapper;

    /**
     * Получить всех риелторов
     */
    public List<Realtor> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM realtors ORDER BY last_name, first_name",
            realtorRowMapper
        );
    }

    /**
     * Найти риелтора по ID
     */
    public Realtor findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM realtors WHERE id_realtor = ?",
            realtorRowMapper,
            id
        );
    }

    /**
     * Сохранить нового риелтора
     */
    @SuppressWarnings({ "null" })
    public Long save(Realtor realtor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO realtors (first_name, last_name, middle_name, phone, email, experience_years) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            ps.setString(1, realtor.getFirstName());
            ps.setString(2, realtor.getLastName());
            ps.setString(3, realtor.getMiddleName());
            ps.setString(4, realtor.getPhone());
            ps.setString(5, realtor.getEmail());
            ps.setInt(6, realtor.getExperienceYears());
            
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    /**
     * Обновление риелтора
     * @param id ID риелтора для обновления
     * @param updates Map с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем, что есть поля для обновления
        if (updates == null || updates.isEmpty()) {
            return false;
        }
        
        // Строим динамический SQL запрос
        StringBuilder sql = new StringBuilder("UPDATE realtors SET ");
        List<Object> params = new ArrayList<>();
        
        // Добавляем поля для обновления
        if (updates.containsKey("firstName")) {
            sql.append("first_name = ?, ");
            params.add(updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            sql.append("last_name = ?, ");
            params.add(updates.get("lastName"));
        }
        if (updates.containsKey("middleName")) {
            sql.append("middle_name = ?, ");
            params.add(updates.get("middleName"));
        }
        if (updates.containsKey("phone")) {
            sql.append("phone = ?, ");
            params.add(updates.get("phone"));
        }
        if (updates.containsKey("email")) {
            sql.append("email = ?, ");
            params.add(updates.get("email"));
        }
        if (updates.containsKey("experienceYears")) {
            sql.append("experience_years = ?, ");
            params.add(updates.get("experienceYears"));
        }
        
        // Убираем последнюю запятую и пробел
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.setLength(sql.length() - 2);
        }
        
        // Добавляем условие WHERE
        sql.append(" WHERE id_realtor = ?");
        params.add(id);
        
        // Выполняем обновление
        int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
        return updatedRows > 0;
    }

    /**
     * Удалить риелтора по ID
     */
    public boolean deleteById(Long id) {
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM realtors WHERE id_realtor = ?",
            id
        );
        
        return deletedRows > 0;
    }

    /**
     * Найти риелторов по фамилии
     */
    public List<Realtor> findByLastName(String lastName) {
        return jdbcTemplate.query(
            "SELECT * FROM realtors WHERE last_name ILIKE ? ORDER BY first_name",
            realtorRowMapper,
            "%" + lastName + "%"
        );
    }

    /**
     * Найти риелторов с опытом больше указанного
     */
    public List<Realtor> findByExperienceGreaterThan(int minExperience) {
        return jdbcTemplate.query(
            "SELECT * FROM realtors WHERE experience_years >= ? ORDER BY experience_years DESC",
            realtorRowMapper,
            minExperience
        );
    }

    /**
     * Найти риелтора по телефону
     */
    public Realtor findByPhone(String phone) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM realtors WHERE phone = ?",
            realtorRowMapper,
            phone
        );
    }

    /**
     * Найти риелтора по email
     */
    public Realtor findByEmail(String email) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM realtors WHERE email = ?",
            realtorRowMapper,
            email
        );
    }

    /**
     * Получить количество риелторов
     */
    public int getCount() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM realtors",
            Integer.class
        );
        return count != null ? count : 0;
    }
} 