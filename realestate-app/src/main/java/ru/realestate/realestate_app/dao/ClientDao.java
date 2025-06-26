package ru.realestate.realestate_app.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.realestate.realestate_app.mapper.ClientRowMapper;
import ru.realestate.realestate_app.model.Client;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO класс для работы с клиентами
 * Обеспечивает доступ к данным клиентов в базе данных
 */
@Repository
public class ClientDao {

    private final JdbcTemplate jdbcTemplate;
    private final ClientRowMapper clientRowMapper;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     * @param clientRowMapper маппер для преобразования строк результата в объекты Client
     */
    public ClientDao(JdbcTemplate jdbcTemplate, ClientRowMapper clientRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.clientRowMapper = clientRowMapper;
    }

    /**
     * Получить всех клиентов, отсортированных по фамилии и имени
     * @return список всех клиентов
     */
    public List<Client> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM clients ORDER BY last_name, first_name",
            clientRowMapper
        );
    }

    /**
     * Найти клиента по уникальному идентификатору
     * @param id идентификатор клиента
     * @return объект клиента
     * @throws org.springframework.dao.EmptyResultDataAccessException если клиент не найден
     */
    public Client findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM clients WHERE id_client = ?",
            clientRowMapper,
            id
        );
    }

    /**
     * Сохранить нового клиента в базе данных
     * @param client объект клиента для сохранения
     * @return идентификатор созданного клиента или null в случае ошибки
     */
    @SuppressWarnings({ "null" })
    public Long save(Client client) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO clients (first_name, last_name, middle_name, phone, email) " +
                "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getMiddleName());
            ps.setString(4, client.getPhone());
            ps.setString(5, client.getEmail());
            
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    /**
     * Обновить данные существующего клиента
     * @param id идентификатор клиента для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     */
    public boolean update(Long id, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            return false;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE clients SET ");
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
        
        // Убираем последнюю запятую и пробел
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.setLength(sql.length() - 2);
        }
        
        // Добавляем условие WHERE
        sql.append(" WHERE id_client = ?");
        params.add(id);
        
        // Выполняем обновление
        int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
        return updatedRows > 0;
    }

    /**
     * Удалить клиента по идентификатору
     * @param id идентификатор клиента для удаления
     * @return true если удаление прошло успешно, false если клиент не найден
     */
    public boolean deleteById(Long id) {
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM clients WHERE id_client = ?",
            id
        );
        
        return deletedRows > 0;
    }

    /**
     * Найти клиентов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список клиентов с указанной фамилией
     */
    public List<Client> findByLastName(String lastName) {
        return jdbcTemplate.query(
            "SELECT * FROM clients WHERE last_name ILIKE ? ORDER BY first_name",
            clientRowMapper,
            "%" + lastName + "%"
        );
    }

    /**
     * Найти клиента по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект клиента
     * @throws org.springframework.dao.EmptyResultDataAccessException если клиент не найден
     */
    public Client findByPhone(String phone) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM clients WHERE phone = ?",
            clientRowMapper,
            phone
        );
    }

    /**
     * Найти клиента по адресу электронной почты (точное совпадение)
     * @param email адрес электронной почты
     * @return объект клиента
     * @throws org.springframework.dao.EmptyResultDataAccessException если клиент не найден
     */
    public Client findByEmail(String email) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM clients WHERE email = ?",
            clientRowMapper,
            email
        );
    }

    /**
     * Получить общее количество клиентов в базе данных
     * @return количество клиентов
     */
    public int getCount() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM clients",
            Integer.class
        );
        return count != null ? count : 0;
    }
} 