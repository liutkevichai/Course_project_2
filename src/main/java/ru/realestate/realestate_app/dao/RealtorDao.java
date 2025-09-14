package ru.realestate.realestate_app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.RealtorRowMapper;
import ru.realestate.realestate_app.model.Realtor;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * DAO класс для работы с риелторами
 * Обеспечивает доступ к данным риелторов в базе данных
 */
@Repository
public class RealtorDao {

    // Логгер для записи событий и ошибок
    private static final Logger logger = LoggerFactory.getLogger(RealtorDao.class);
    
    // Регулярное выражение для валидации email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

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
        logger.debug("Получение списка всех риелторов");
        return jdbcTemplate.query(
            "SELECT * FROM realtors ORDER BY id_realtor",
            realtorRowMapper
        );
    }

    /**
     * Найти риелтора по уникальному идентификатору
     * @param id идентификатор риелтора
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     * @throws IllegalArgumentException если id равен null
     */
    public Realtor findById(Long id) {
        // Валидация входного параметра
        if (id == null) {
            logger.error("Попытка поиска риелтора с null id");
            throw new IllegalArgumentException("Идентификатор риелтора не может быть null");
        }
        
        logger.debug("Поиск риелтора по id: {}", id);
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
     * @throws IllegalArgumentException если данные риелтора некорректны
     * @throws DataIntegrityViolationException если нарушена уникальность email или телефона
     */
    @SuppressWarnings({ "null" })
    public Long save(Realtor realtor) {
        // Валидация входного объекта
        validateRealtorForSave(realtor);
        
        // Проверка уникальности email
        if (realtor.getEmail() != null && existsByEmail(realtor.getEmail())) {
            logger.error("Попытка сохранения риелтора с уже существующим email: {}", realtor.getEmail());
            throw new DataIntegrityViolationException("Риелтор с таким email уже существует: " + realtor.getEmail());
        }
        
        // Проверка уникальности телефона
        if (realtor.getPhone() != null && existsByPhone(realtor.getPhone())) {
            logger.error("Попытка сохранения риелтора с уже существующим телефоном: {}", realtor.getPhone());
            throw new DataIntegrityViolationException("Риелтор с таким телефоном уже существует: " + realtor.getPhone());
        }
        
        logger.debug("Сохранение нового риелтора: {} {}", realtor.getLastName(), realtor.getFirstName());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO realtors (first_name, last_name, middle_name, phone, email, experience_years) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
                new String[]{"id_realtor"}
            );
            
            ps.setString(1, realtor.getFirstName());
            ps.setString(2, realtor.getLastName());
            ps.setString(3, realtor.getMiddleName());
            ps.setString(4, realtor.getPhone());
            ps.setString(5, realtor.getEmail());
            ps.setInt(6, realtor.getExperienceYears());
            
            return ps;
        }, keyHolder);
        
        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        if (generatedId == null) {
            logger.error("Не удалось получить сгенерированный id для риелтора");
            throw new DataIntegrityViolationException("Не удалось создать риелтора в базе данных");
        }
        
        logger.info("Риелтор успешно сохранен с id: {}", generatedId);
        return generatedId;
    }

    /**
     * Обновить данные существующего риелтора
     * @param id идентификатор риелтора для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws IllegalArgumentException если id равен null или данные некорректны
     * @throws DataIntegrityViolationException если нарушена уникальность email или телефона
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Валидация входных параметров
        if (id == null) {
            logger.error("Попытка обновления риелтора с null id");
            throw new IllegalArgumentException("Идентификатор риелтора не может быть null");
        }
        
        // Проверяем, что есть поля для обновления
        if (updates == null || updates.isEmpty()) {
            logger.debug("Нет данных для обновления риелтора с id: {}", id);
            return false;
        }
        
        // Валидация обновляемых данных
        validateRealtorUpdates(updates);
        
        // Проверка уникальности email при обновлении
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            if (newEmail != null && existsByEmail(newEmail, id)) {
                logger.error("Попытка обновления риелтора с уже существующим email: {}", newEmail);
                throw new DataIntegrityViolationException("Риелтор с таким email уже существует: " + newEmail);
            }
        }
        
        // Проверка уникальности телефона при обновлении
        if (updates.containsKey("phone")) {
            String newPhone = (String) updates.get("phone");
            if (newPhone != null && existsByPhone(newPhone, id)) {
                logger.error("Попытка обновления риелтора с уже существующим телефоном: {}", newPhone);
                throw new DataIntegrityViolationException("Риелтор с таким телефоном уже существует: " + newPhone);
            }
        }
        
        logger.debug("Обновление риелтора с id: {}", id);
        
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
            params.add(Integer.parseInt(updates.get("experienceYears").toString()));
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
        
        if (updatedRows > 0) {
            logger.info("Риелтор с id {} успешно обновлен", id);
        } else {
            logger.warn("Риелтор с id {} не найден для обновления", id);
        }
        
        return updatedRows > 0;
    }

    /**
     * Удалить риелтора по идентификатору
     * @param id идентификатор риелтора для удаления
     * @return true если удаление прошло успешно, false если риелтор не найден
     * @throws IllegalArgumentException если id равен null
     */
    public boolean deleteById(Long id) {
        // Валидация входного параметра
        if (id == null) {
            logger.error("Попытка удаления риелтора с null id");
            throw new IllegalArgumentException("Идентификатор риелтора не может быть null");
        }
        
        logger.debug("Удаление риелтора с id: {}", id);
        
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM realtors WHERE id_realtor = ?",
            id
        );
        
        if (deletedRows > 0) {
            logger.info("Риелтор с id {} успешно удален", id);
        } else {
            logger.warn("Риелтор с id {} не найден для удаления", id);
        }
        
        return deletedRows > 0;
    }

    /**
     * Найти риелторов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список риелторов с указанной фамилией
     * @throws IllegalArgumentException если lastName равен null или пустой
     */
    public List<Realtor> findByLastName(String lastName) {
        // Валидация входного параметра
        if (lastName == null || lastName.trim().isEmpty()) {
            logger.error("Попытка поиска риелторов с пустой фамилией");
            throw new IllegalArgumentException("Фамилия для поиска не может быть пустой");
        }
        
        logger.debug("Поиск риелторов по фамилии: {}", lastName);
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
     * @throws IllegalArgumentException если minExperience отрицательный
     */
    public List<Realtor> findByExperienceGreaterThan(int minExperience) {
        // Валидация входного параметра
        if (minExperience < 0) {
            logger.error("Попытка поиска риелторов с отрицательным опытом: {}", minExperience);
            throw new IllegalArgumentException("Минимальный опыт работы не может быть отрицательным");
        }
        
        logger.debug("Поиск риелторов с опытом больше {} лет", minExperience);
        return jdbcTemplate.query(
            "SELECT * FROM realtors WHERE experience_years >= ? ORDER BY experience_years DESC",
            realtorRowMapper,
            minExperience
        );
    }

    /**
     * Поиск риелторов по заданным критериям
     *
     * @param lastName фамилия риелтора (частичное совпадение)
     * @param email email риелтора (точное совпадение)
     * @param phone телефон риелтора (точное совпадение)
     * @param minExperience минимальный опыт работы риелтора
     * @return список риелторов, соответствующих всем указанным критериям
     */
    public List<Realtor> search(String lastName, String email, String phone, Integer minExperience) {
        StringBuilder sql = new StringBuilder("SELECT * FROM realtors WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Добавляем условия поиска только для непустых параметров
        if (lastName != null && !lastName.trim().isEmpty()) {
            sql.append(" AND last_name ILIKE ?");
            params.add("%" + lastName.trim() + "%");
        }

        if (email != null && !email.trim().isEmpty()) {
            sql.append(" AND email = ?");
            params.add(email.trim());
        }

        if (phone != null && !phone.trim().isEmpty()) {
            sql.append(" AND phone = ?");
            params.add(phone.trim());
        }

        if (minExperience != null) {
            sql.append(" AND experience_years >= ?");
            params.add(minExperience);
        }

        sql.append(" ORDER BY id_realtor");

        return jdbcTemplate.query(sql.toString(), realtorRowMapper, params.toArray());
    }

    /**
     * Найти риелтора по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     * @throws IllegalArgumentException если phone равен null или пустой
     */
    public Realtor findByPhone(String phone) {
        // Валидация входного параметра
        if (phone == null || phone.trim().isEmpty()) {
            logger.error("Попытка поиска риелтора с пустым номером телефона");
            throw new IllegalArgumentException("Номер телефона не может быть пустым");
        }
        
        logger.debug("Поиск риелтора по телефону: {}", phone);
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
     * @throws IllegalArgumentException если email равен null или пустой
     */
    public Realtor findByEmail(String email) {
        // Валидация входного параметра
        if (email == null || email.trim().isEmpty()) {
            logger.error("Попытка поиска риелтора с пустым email");
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        
        logger.debug("Поиск риелтора по email: {}", email);
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
        logger.debug("Получение общего количества риелторов");
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM realtors",
            Integer.class
        );
        return count != null ? count : 0;
    }

    /**
     * Проверить существование риелтора с указанным email
     * @param email адрес электронной почты
     * @return true если риелтор с таким email существует
     */
    private boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM realtors WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Проверить существование риелтора с указанным email, исключая риелтора с заданным id
     * @param email адрес электронной почты
     * @param excludeId идентификатор риелтора для исключения из проверки
     * @return true если риелтор с таким email существует
     */
    private boolean existsByEmail(String email, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM realtors WHERE email = ? AND id_realtor != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, excludeId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование риелтора с указанным телефоном
     * @param phone номер телефона
     * @return true если риелтор с таким телефоном существует
     */
    private boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM realtors WHERE phone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone);
        return count != null && count > 0;
    }

    /**
     * Проверить существование риелтора с указанным телефоном, исключая риелтора с заданным id
     * @param phone номер телефона
     * @param excludeId идентификатор риелтора для исключения из проверки
     * @return true если риелтор с таким телефоном существует
     */
    private boolean existsByPhone(String phone, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM realtors WHERE phone = ? AND id_realtor != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone, excludeId);
        return count != null && count > 0;
    }

    /**
     * Валидация объекта риелтора для сохранения
     * @param realtor объект риелтора для валидации
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateRealtorForSave(Realtor realtor) {
        if (realtor == null) {
            logger.error("Попытка сохранения null риелтора");
            throw new IllegalArgumentException("Риелтор не может быть null");
        }
        
        if (realtor.getLastName() == null || realtor.getLastName().trim().isEmpty()) {
            logger.error("Попытка сохранения риелтора с пустой фамилией");
            throw new IllegalArgumentException("Фамилия риелтора обязательна");
        }
        
        if (realtor.getFirstName() == null || realtor.getFirstName().trim().isEmpty()) {
            logger.error("Попытка сохранения риелтора с пустым именем");
            throw new IllegalArgumentException("Имя риелтора обязательно");
        }
        
        // Валидация опыта работы
        if (realtor.getExperienceYears() < 0) {
            logger.error("Попытка сохранения риелтора с отрицательным опытом: {}", realtor.getExperienceYears());
            throw new IllegalArgumentException("Опыт работы не может быть отрицательным");
        }
        
        if (realtor.getExperienceYears() > 100) {
            logger.error("Попытка сохранения риелтора с некорректным опытом: {}", realtor.getExperienceYears());
            throw new IllegalArgumentException("Опыт работы не может превышать 100 лет");
        }
        
        // Валидация email если он указан
        if (realtor.getEmail() != null && !realtor.getEmail().trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(realtor.getEmail()).matches()) {
                logger.error("Попытка сохранения риелтора с некорректным email: {}", realtor.getEmail());
                throw new IllegalArgumentException("Некорректный формат email: " + realtor.getEmail());
            }
        }
        
        // Валидация телефона если он указан
        if (realtor.getPhone() != null && !realtor.getPhone().trim().isEmpty()) {
            String phone = realtor.getPhone().replaceAll("[^0-9+]", "");
            if (phone.length() < 10) {
                logger.error("Попытка сохранения риелтора с некорректным телефоном: {}", realtor.getPhone());
                throw new IllegalArgumentException("Некорректный формат телефона: " + realtor.getPhone());
            }
        }
    }

    /**
     * Валидация обновляемых данных риелтора
     * @param updates карта с полями для обновления
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateRealtorUpdates(Map<String, Object> updates) {
        // Валидация фамилии
        if (updates.containsKey("lastName")) {
            String lastName = (String) updates.get("lastName");
            if (lastName == null || lastName.trim().isEmpty()) {
                logger.error("Попытка обновления риелтора с пустой фамилией");
                throw new IllegalArgumentException("Фамилия риелтора не может быть пустой");
            }
        }
        
        // Валидация имени
        if (updates.containsKey("firstName")) {
            String firstName = (String) updates.get("firstName");
            if (firstName == null || firstName.trim().isEmpty()) {
                logger.error("Попытка обновления риелтора с пустым именем");
                throw new IllegalArgumentException("Имя риелтора не может быть пустым");
            }
        }
        
        // Валидация опыта работы
        if (updates.containsKey("experienceYears")) {
            Object experienceYearsObj = updates.get("experienceYears");
            if (experienceYearsObj == null || experienceYearsObj.toString().isEmpty()) {
                logger.error("Попытка обновления риелтора с пустым значением опыта работы");
                throw new IllegalArgumentException("Опыт работы не может быть пустым");
            }
            Integer experienceYears;
            try {
                experienceYears = Integer.parseInt(experienceYearsObj.toString());
            } catch (NumberFormatException e) {
                logger.error("Попытка обновления риелтора с некорректным форматом опыта работы: {}", experienceYearsObj);
                throw new IllegalArgumentException("Некорректный формат опыта работы. Ожидается числовое значение.");
            }
            if (experienceYears < 0) {
                logger.error("Попытка обновления риелтора с отрицательным опытом: {}", experienceYears);
                throw new IllegalArgumentException("Опыт работы не может быть отрицательным");
            }

            if (experienceYears > 100) {
                logger.error("Попытка обновления риелтора с некорректным опытом: {}", experienceYears);
                throw new IllegalArgumentException("Опыт работы не может превышать 100 лет");
            }
        }
        
        // Валидация email
        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            if (email != null && !email.trim().isEmpty()) {
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    logger.error("Попытка обновления риелтора с некорректным email: {}", email);
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
                    logger.error("Попытка обновления риелтора с некорректным телефоном: {}", phone);
                    throw new IllegalArgumentException("Некорректный формат телефона: " + phone);
                }
            }
        }
    }
} 