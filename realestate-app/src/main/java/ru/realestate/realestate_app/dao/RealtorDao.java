package ru.realestate.realestate_app.dao;

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

/**
 * DAO класс для работы с риелторами
 * Обеспечивает доступ к данным риелторов в базе данных
 */
@Repository
public class RealtorDao {

    private final JdbcTemplate jdbcTemplate;
    private final RealtorRowMapper realtorRowMapper;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     * @param realtorRowMapper маппер для преобразования строк результата в объекты Realtor
     */
    public RealtorDao(JdbcTemplate jdbcTemplate, RealtorRowMapper realtorRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.realtorRowMapper = realtorRowMapper;
    }

    /**
     * Получить всех риелторов, отсортированных по фамилии и имени
     * @return список всех риелторов
     */
    public List<Realtor> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM realtors ORDER BY last_name, first_name",
            realtorRowMapper
        );
    }

    /**
     * Найти риелтора по уникальному идентификатору
     * @param id идентификатор риелтора
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     */
    public Realtor findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM realtors WHERE id_realtor = ?",
            realtorRowMapper,
            id
        );
    }

    /**
     * Сохранить нового риелтора в базе данных
     * @param realtor объект риелтора для сохранения
     * @return идентификатор созданного риелтора
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
     * Обновить данные существующего риелтора
     * @param id идентификатор риелтора для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
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
     * Удалить риелтора по идентификатору
     * @param id идентификатор риелтора для удаления
     * @return true если удаление прошло успешно, false если риелтор не найден
     */
    public boolean deleteById(Long id) {
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM realtors WHERE id_realtor = ?",
            id
        );
        
        return deletedRows > 0;
    }

    /**
     * Найти риелторов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список риелторов с указанной фамилией
     */
    public List<Realtor> findByLastName(String lastName) {
        return jdbcTemplate.query(
            "SELECT * FROM realtors WHERE last_name ILIKE ? ORDER BY first_name",
            realtorRowMapper,
            "%" + lastName + "%"
        );
    }

    /**
     * Найти риелторов с опытом работы больше указанного значения
     * @param minExperience минимальный опыт работы в годах
     * @return список риелторов с подходящим опытом, отсортированный по убыванию опыта
     */
    public List<Realtor> findByExperienceGreaterThan(int minExperience) {
        return jdbcTemplate.query(
            "SELECT * FROM realtors WHERE experience_years >= ? ORDER BY experience_years DESC",
            realtorRowMapper,
            minExperience
        );
    }

    /**
     * Найти риелтора по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     */
    public Realtor findByPhone(String phone) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM realtors WHERE phone = ?",
            realtorRowMapper,
            phone
        );
    }

    /**
     * Найти риелтора по адресу электронной почты (точное совпадение)
     * @param email адрес электронной почты
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     */
    public Realtor findByEmail(String email) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM realtors WHERE email = ?",
            realtorRowMapper,
            email
        );
    }

    /**
     * Получить общее количество риелторов в базе данных
     * @return количество риелторов
     */
    public int getCount() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM realtors",
            Integer.class
        );
        return count != null ? count : 0;
    }
} 