package ru.realestate.realestate_app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.realestate.realestate_app.mapper.ClientRowMapper;
import ru.realestate.realestate_app.model.Client;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * DAO класс для работы с клиентами
 * Обеспечивает доступ к данным клиентов в базе данных
 */
@Repository
public class ClientDao {

    private static final Logger logger = LoggerFactory.getLogger(ClientDao.class);
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

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
        logger.debug("Получение списка всех клиентов");
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
     * @throws IllegalArgumentException если id равен null
     */
    public Client findById(Long id) {
        if (id == null) {
            logger.error("Попытка поиска клиента с null id");
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }
        
        logger.debug("Поиск клиента по id: {}", id);
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
     * @throws IllegalArgumentException если данные клиента некорректны
     * @throws DataIntegrityViolationException если нарушена уникальность email или телефона
     */
    @SuppressWarnings({ "null" })
    public Long save(Client client) {
        validateClientForSave(client);
        
        // Проверка уникальности email
        if (client.getEmail() != null && existsByEmail(client.getEmail())) {
            logger.error("Попытка сохранения клиента с уже существующим email: {}", client.getEmail());
            throw new DataIntegrityViolationException("Клиент с таким email уже существует: " + client.getEmail());
        }
        
        // Проверка уникальности телефона
        if (client.getPhone() != null && existsByPhone(client.getPhone())) {
            logger.error("Попытка сохранения клиента с уже существующим телефоном: {}", client.getPhone());
            throw new DataIntegrityViolationException("Клиент с таким телефоном уже существует: " + client.getPhone());
        }
        
        logger.debug("Сохранение нового клиента: {} {}", client.getLastName(), client.getFirstName());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO clients (first_name, last_name, middle_name, phone, email) " +
                "VALUES (?, ?, ?, ?, ?)",
                new String[]{"id_client"}
            );
            
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getMiddleName());
            ps.setString(4, client.getPhone());
            ps.setString(5, client.getEmail());
            
            return ps;
        }, keyHolder);
        
        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        if (generatedId == null) {
            logger.error("Не удалось получить сгенерированный id для клиента");
            throw new DataIntegrityViolationException("Не удалось создать клиента в базе данных");
        }
        
        logger.info("Клиент успешно сохранен с id: {}", generatedId);
        return generatedId;
    }

    /**
     * Обновить данные существующего клиента
     * @param id идентификатор клиента для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws IllegalArgumentException если id равен null или данные некорректны
     * @throws DataIntegrityViolationException если нарушена уникальность email или телефона
     */
    public boolean update(Long id, Map<String, Object> updates) {
        if (id == null) {
            logger.error("Попытка обновления клиента с null id");
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }
        
        if (updates == null || updates.isEmpty()) {
            logger.debug("Нет данных для обновления клиента с id: {}", id);
            return false;
        }
        
        validateClientUpdates(updates);
        
        // Проверка уникальности email при обновлении
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            if (newEmail != null && existsByEmail(newEmail, id)) {
                logger.error("Попытка обновления клиента с уже существующим email: {}", newEmail);
                throw new DataIntegrityViolationException("Клиент с таким email уже существует: " + newEmail);
            }
        }
        
        // Проверка уникальности телефона при обновлении
        if (updates.containsKey("phone")) {
            String newPhone = (String) updates.get("phone");
            if (newPhone != null && existsByPhone(newPhone, id)) {
                logger.error("Попытка обновления клиента с уже существующим телефоном: {}", newPhone);
                throw new DataIntegrityViolationException("Клиент с таким телефоном уже существует: " + newPhone);
            }
        }
        
        logger.debug("Обновление клиента с id: {}", id);
        
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
        
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.setLength(sql.length() - 2);
        }
        
        sql.append(" WHERE id_client = ?");
        params.add(id);
        
        int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
        
        if (updatedRows > 0) {
            logger.info("Клиент с id {} успешно обновлен", id);
        } else {
            logger.warn("Клиент с id {} не найден для обновления", id);
        }
        
        return updatedRows > 0;
    }

    /**
     * Удалить клиента по идентификатору
     * @param id идентификатор клиента для удаления
     * @return true если удаление прошло успешно, false если клиент не найден
     * @throws IllegalArgumentException если id равен null
     */
    public boolean deleteById(Long id) {
        if (id == null) {
            logger.error("Попытка удаления клиента с null id");
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }
        
        logger.debug("Удаление клиента с id: {}", id);
        
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM clients WHERE id_client = ?",
            id
        );
        
        if (deletedRows > 0) {
            logger.info("Клиент с id {} успешно удален", id);
        } else {
            logger.warn("Клиент с id {} не найден для удаления", id);
        }
        
        return deletedRows > 0;
    }

    /**
     * Найти клиентов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список клиентов с указанной фамилией
     * @throws IllegalArgumentException если lastName равен null или пустой
     */
    public List<Client> findByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            logger.error("Попытка поиска клиентов с пустой фамилией");
            throw new IllegalArgumentException("Фамилия для поиска не может быть пустой");
        }
        
        logger.debug("Поиск клиентов по фамилии: {}", lastName);
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
     * @throws IllegalArgumentException если phone равен null или пустой
     */
    public Client findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            logger.error("Попытка поиска клиента с пустым номером телефона");
            throw new IllegalArgumentException("Номер телефона не может быть пустым");
        }
        
        logger.debug("Поиск клиента по телефону: {}", phone);
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
     * @throws IllegalArgumentException если email равен null или пустой
     */
    public Client findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.error("Попытка поиска клиента с пустым email");
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        
        logger.debug("Поиск клиента по email: {}", email);
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
        logger.debug("Получение общего количества клиентов");
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM clients",
            Integer.class
        );
        return count != null ? count : 0;
    }

    /**
     * Проверить существование клиента с указанным email
     * @param email адрес электронной почты
     * @return true если клиент с таким email существует
     */
    private boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Проверить существование клиента с указанным email, исключая клиента с заданным id
     * @param email адрес электронной почты
     * @param excludeId идентификатор клиента для исключения из проверки
     * @return true если клиент с таким email существует
     */
    private boolean existsByEmail(String email, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ? AND id_client != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, excludeId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование клиента с указанным телефоном
     * @param phone номер телефона
     * @return true если клиент с таким телефоном существует
     */
    private boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM clients WHERE phone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone);
        return count != null && count > 0;
    }

    /**
     * Проверить существование клиента с указанным телефоном, исключая клиента с заданным id
     * @param phone номер телефона
     * @param excludeId идентификатор клиента для исключения из проверки
     * @return true если клиент с таким телефоном существует
     */
    private boolean existsByPhone(String phone, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM clients WHERE phone = ? AND id_client != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone, excludeId);
        return count != null && count > 0;
    }

    /**
     * Валидация объекта клиента для сохранения
     * @param client объект клиента для валидации
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateClientForSave(Client client) {
        if (client == null) {
            logger.error("Попытка сохранения null клиента");
            throw new IllegalArgumentException("Клиент не может быть null");
        }
        
        if (client.getLastName() == null || client.getLastName().trim().isEmpty()) {
            logger.error("Попытка сохранения клиента с пустой фамилией");
            throw new IllegalArgumentException("Фамилия клиента обязательна");
        }
        
        if (client.getFirstName() == null || client.getFirstName().trim().isEmpty()) {
            logger.error("Попытка сохранения клиента с пустым именем");
            throw new IllegalArgumentException("Имя клиента обязательно");
        }
        
        // Валидация email если он указан
        if (client.getEmail() != null && !client.getEmail().trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(client.getEmail()).matches()) {
                logger.error("Попытка сохранения клиента с некорректным email: {}", client.getEmail());
                throw new IllegalArgumentException("Некорректный формат email: " + client.getEmail());
            }
        }
        
        // Валидация телефона если он указан
        if (client.getPhone() != null && !client.getPhone().trim().isEmpty()) {
            String phone = client.getPhone().replaceAll("[^0-9+]", "");
            if (phone.length() < 10) {
                logger.error("Попытка сохранения клиента с некорректным телефоном: {}", client.getPhone());
                throw new IllegalArgumentException("Некорректный формат телефона: " + client.getPhone());
            }
        }
    }

    /**
     * Валидация обновляемых данных клиента
     * @param updates карта с полями для обновления
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateClientUpdates(Map<String, Object> updates) {
        // Валидация фамилии
        if (updates.containsKey("lastName")) {
            String lastName = (String) updates.get("lastName");
            if (lastName == null || lastName.trim().isEmpty()) {
                logger.error("Попытка обновления клиента с пустой фамилией");
                throw new IllegalArgumentException("Фамилия клиента не может быть пустой");
            }
        }
        
        // Валидация имени
        if (updates.containsKey("firstName")) {
            String firstName = (String) updates.get("firstName");
            if (firstName == null || firstName.trim().isEmpty()) {
                logger.error("Попытка обновления клиента с пустым именем");
                throw new IllegalArgumentException("Имя клиента не может быть пустым");
            }
        }
        
        // Валидация email
        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            if (email != null && !email.trim().isEmpty()) {
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    logger.error("Попытка обновления клиента с некорректным email: {}", email);
                    throw new IllegalArgumentException("Некорректный формат email: " + email);
                }
            }
        }
        
        // Валидация телефона
        if (updates.containsKey("phone")) {
            String phone = (String) updates.get("phone");
            if (phone != null && !phone.trim().isEmpty()) {
                String cleanPhone = phone.replaceAll("[^0-9+]", "");
                if (cleanPhone.length() < 10) {
                    logger.error("Попытка обновления клиента с некорректным телефоном: {}", phone);
                    throw new IllegalArgumentException("Некорректный формат телефона: " + phone);
                }
            }
        }
    }
} 