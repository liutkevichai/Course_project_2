package ru.realestate.realestate_app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.DealRowMapper;
import ru.realestate.realestate_app.mapper.dto.DealWithDetailsRowMapper;
import ru.realestate.realestate_app.mapper.dto.DealTableRowMapper;
import ru.realestate.realestate_app.model.Deal;
import ru.realestate.realestate_app.model.dto.DealWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DealTableDto;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * DAO класс для работы с сделками
 * Обеспечивает доступ к данным сделок в базе данных
 */
@Repository
public class DealDao {

    private static final Logger logger = LoggerFactory.getLogger(DealDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final DealRowMapper dealRowMapper;
    private final DealWithDetailsRowMapper dealWithDetailsRowMapper;
    private final DealTableRowMapper dealTableRowMapper;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     * @param dealRowMapper маппер для преобразования строк результата в объекты Deal
     * @param dealWithDetailsRowMapper маппер для DealWithDetailsDto
     * @param dealTableRowMapper маппер для DealTableDto
     */
    public DealDao(JdbcTemplate jdbcTemplate, DealRowMapper dealRowMapper, 
                   DealWithDetailsRowMapper dealWithDetailsRowMapper, 
                   DealTableRowMapper dealTableRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dealRowMapper = dealRowMapper;
        this.dealWithDetailsRowMapper = dealWithDetailsRowMapper;
        this.dealTableRowMapper = dealTableRowMapper;
    }

    /**
     * Получить все сделки, отсортированные по дате в убывающем порядке
     * @return список всех сделок
     */
    public List<Deal> findAll() {
        logger.debug("Получение списка всех сделок");
        return jdbcTemplate.query(
            "SELECT * FROM deals ORDER BY deal_date DESC",
            dealRowMapper
        );
    }

    /**
     * Найти сделку по уникальному идентификатору
     * @param id идентификатор сделки
     * @return объект сделки
     * @throws org.springframework.dao.EmptyResultDataAccessException если сделка не найдена
     * @throws IllegalArgumentException если id равен null
     */
    public Deal findById(Long id) {
        if (id == null) {
            logger.error("Попытка поиска сделки с null id");
            throw new IllegalArgumentException("Идентификатор сделки не может быть null");
        }
        
        logger.debug("Поиск сделки по id: {}", id);
        return jdbcTemplate.queryForObject(
            "SELECT * FROM deals WHERE id_deal = ?",
            dealRowMapper,
            id
        );
    }

    /**
     * Сохранить новую сделку в базе данных
     * @param deal объект сделки для сохранения
     * @return идентификатор созданной сделки
     * @throws IllegalArgumentException если данные сделки некорректны
     * @throws DataIntegrityViolationException если связанные сущности не существуют
     */
    @SuppressWarnings({ "null" })
    public Long save(Deal deal) {
        validateDealForSave(deal);
        
        // Проверка существования связанных сущностей
        validateRelatedEntities(deal);
        
        logger.debug("Сохранение новой сделки: дата={}, стоимость={}", 
                    deal.getDealDate(), deal.getDealCost());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO deals (deal_date, deal_cost, id_property, id_realtor, id_client, id_deal_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            ps.setDate(1, java.sql.Date.valueOf(deal.getDealDate()));
            ps.setBigDecimal(2, deal.getDealCost());
            ps.setLong(3, deal.getIdProperty());
            ps.setLong(4, deal.getIdRealtor());
            ps.setLong(5, deal.getIdClient());
            ps.setLong(6, deal.getIdDealType());
            
            return ps;
        }, keyHolder);
        
        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        if (generatedId == null) {
            logger.error("Не удалось получить сгенерированный id для сделки");
            throw new DataIntegrityViolationException("Не удалось создать сделку в базе данных");
        }
        
        logger.info("Сделка успешно сохранена с id: {}", generatedId);
        return generatedId;
    }

    /**
     * Обновить данные существующей сделки
     * @param id идентификатор сделки для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws IllegalArgumentException если id равен null или данные некорректны
     * @throws DataIntegrityViolationException если связанные сущности не существуют
     */
    public boolean update(Long id, Map<String, Object> updates) {
        if (id == null) {
            logger.error("Попытка обновления сделки с null id");
            throw new IllegalArgumentException("Идентификатор сделки не может быть null");
        }
        
        if (updates == null || updates.isEmpty()) {
            logger.debug("Нет данных для обновления сделки с id: {}", id);
            return false;
        }
        
        validateDealUpdates(updates);
        
        // Проверка существования связанных сущностей при обновлении
        validateRelatedEntitiesForUpdate(updates);
        
        logger.debug("Обновление сделки с id: {}", id);
        
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
        
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.setLength(sql.length() - 2);
        }
        
        sql.append(" WHERE id_deal = ?");
        params.add(id);
        
        int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
        
        if (updatedRows > 0) {
            logger.info("Сделка с id {} успешно обновлена", id);
        } else {
            logger.warn("Сделка с id {} не найдена для обновления", id);
        }
        
        return updatedRows > 0;
    }

    /**
     * Удалить сделку по идентификатору
     * @param id идентификатор сделки для удаления
     * @return true если удаление прошло успешно, false если сделка не найдена
     * @throws IllegalArgumentException если id равен null
     */
    public boolean deleteById(Long id) {
        if (id == null) {
            logger.error("Попытка удаления сделки с null id");
            throw new IllegalArgumentException("Идентификатор сделки не может быть null");
        }
        
        logger.debug("Удаление сделки с id: {}", id);
        
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM deals WHERE id_deal = ?",
            id
        );
        
        if (deletedRows > 0) {
            logger.info("Сделка с id {} успешно удалена", id);
        } else {
            logger.warn("Сделка с id {} не найдена для удаления", id);
        }
        
        return deletedRows > 0;
    }

    /**
     * Найти сделки по конкретной дате
     * @param date дата совершения сделки
     * @return список сделок, совершенных в указанную дату, отсортированный по убыванию стоимости
     * @throws IllegalArgumentException если date равен null
     */
    public List<Deal> findByDate(LocalDate date) {
        if (date == null) {
            logger.error("Попытка поиска сделок с null датой");
            throw new IllegalArgumentException("Дата не может быть null");
        }
        
        logger.debug("Поиск сделок по дате: {}", date);
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE deal_date = ? ORDER BY deal_cost DESC",
            dealRowMapper,
            java.sql.Date.valueOf(date)
        );
    }

    /**
     * Найти сделки в указанном диапазоне дат
     * @param startDate начальная дата периода (включительно)
     * @param endDate конечная дата периода (включительно)
     * @return список сделок в указанном диапазоне дат, отсортированный по убыванию даты
     * @throws IllegalArgumentException если даты некорректны
     */
    public List<Deal> findByDateRange(LocalDate startDate, LocalDate endDate) {
        // Валидация входных параметров
        if (startDate == null) {
            logger.error("Попытка поиска сделок с null начальной датой");
            throw new IllegalArgumentException("Начальная дата не может быть null");
        }
        
        if (endDate == null) {
            logger.error("Попытка поиска сделок с null конечной датой");
            throw new IllegalArgumentException("Конечная дата не может быть null");
        }
        
        if (startDate.isAfter(endDate)) {
            logger.error("Попытка поиска сделок с некорректным диапазоном дат: {} - {}", startDate, endDate);
            throw new IllegalArgumentException("Начальная дата не может быть позже конечной");
        }
        
        logger.debug("Поиск сделок в диапазоне дат: {} - {}", startDate, endDate);
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE deal_date BETWEEN ? AND ? ORDER BY deal_date DESC",
            dealRowMapper,
            java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)
        );
    }

    /**
     * Найти сделки конкретного риелтора
     * @param realtorId идентификатор риелтора
     * @return список сделок указанного риелтора, отсортированный по убыванию даты
     * @throws IllegalArgumentException если realtorId равен null
     */
    public List<Deal> findByRealtorId(Long realtorId) {
        // Валидация входного параметра
        if (realtorId == null) {
            logger.error("Попытка поиска сделок с null id риелтора");
            throw new IllegalArgumentException("Идентификатор риелтора не может быть null");
        }
        
        logger.debug("Поиск сделок по id риелтора: {}", realtorId);
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_realtor = ? ORDER BY deal_date DESC",
            dealRowMapper,
            realtorId
        );
    }

    /**
     * Найти сделки конкретного клиента
     * @param clientId идентификатор клиента
     * @return список сделок указанного клиента, отсортированный по убыванию даты
     */
    public List<Deal> findByClientId(Long clientId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_client = ? ORDER BY deal_date DESC",
            dealRowMapper,
            clientId
        );
    }

    /**
     * Найти сделки по конкретному объекту недвижимости
     * @param propertyId идентификатор объекта недвижимости
     * @return список сделок по указанному объекту недвижимости, отсортированный по убыванию даты
     */
    public List<Deal> findByPropertyId(Long propertyId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_property = ? ORDER BY deal_date DESC",
            dealRowMapper,
            propertyId
        );
    }

    /**
     * Найти сделки по типу сделки
     * @param dealTypeId идентификатор типа сделки
     * @return список сделок указанного типа, отсортированный по убыванию даты
     */
    public List<Deal> findByDealTypeId(Long dealTypeId) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE id_deal_type = ? ORDER BY deal_date DESC",
            dealRowMapper,
            dealTypeId
        );
    }

    /**
     * Найти сделки в указанном ценовом диапазоне
     * @param minCost минимальная стоимость сделки
     * @param maxCost максимальная стоимость сделки
     * @return список сделок в ценовом диапазоне, отсортированный по убыванию стоимости
     */
    public List<Deal> findByCostRange(BigDecimal minCost, BigDecimal maxCost) {
        return jdbcTemplate.query(
            "SELECT * FROM deals WHERE deal_cost BETWEEN ? AND ? ORDER BY deal_cost DESC",
            dealRowMapper,
            minCost, maxCost
        );
    }

    /**
     * Получить общую сумму всех сделок
     * @return общая сумма сделок или 0 если сделок нет
     */
    public BigDecimal getTotalDealsAmount() {
        BigDecimal total = jdbcTemplate.queryForObject(
            "SELECT SUM(deal_cost) FROM deals",
            BigDecimal.class
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Получить общее количество сделок в базе данных
     * @return количество сделок
     */
    public int getCount() {
        logger.debug("Получение общего количества сделок");
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM deals",
            Integer.class
        );
        return count != null ? count : 0;
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все сделки с детальной информацией (JOIN запрос)
     * @return список всех сделок с полной информацией о связанных сущностях
     */
    public List<DealWithDetailsDto> findAllWithDetails() {
        logger.debug("Получение списка всех сделок с детальной информацией");
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент
                c.id_client as client_id,
                c.first_name as client_first_name,
                c.last_name as client_last_name,
                c.middle_name as client_middle_name,
                c.phone as client_phone,
                c.email as client_email,
                -- Риелтор
                r.id_realtor as realtor_id,
                r.first_name as realtor_first_name,
                r.last_name as realtor_last_name,
                r.middle_name as realtor_middle_name,
                r.phone as realtor_phone,
                r.email as realtor_email,
                r.experience_years as realtor_experience,
                -- Недвижимость
                p.id_property as property_id,
                p.area as property_area,
                p.cost as property_cost,
                p.description as property_description,
                p.postal_code as property_postal_code,
                p.house_number as property_house_number,
                p.house_letter as property_house_letter,
                p.building_number as property_building_number,
                p.apartment_number as property_apartment_number,
                -- География
                country.country_name,
                region.name as region_name,
                city.city_name,
                district.district_name,
                street.street_name,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            ORDER BY d.deal_date DESC
            """;
        return jdbcTemplate.query(sql, dealWithDetailsRowMapper);
    }

    /**
     * Найти сделку с детальной информацией по идентификатору
     * @param id идентификатор сделки
     * @return сделка с полной информацией о связанных сущностях
     * @throws org.springframework.dao.EmptyResultDataAccessException если сделка не найдена
     */
    public DealWithDetailsDto findByIdWithDetails(Long id) {
        if (id == null) {
            logger.error("Попытка поиска сделки с детальной информацией с null id");
            throw new IllegalArgumentException("Идентификатор сделки не может быть null");
        }
        
        logger.debug("Поиск сделки с детальной информацией по id: {}", id);
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент
                c.id_client as client_id,
                c.first_name as client_first_name,
                c.last_name as client_last_name,
                c.middle_name as client_middle_name,
                c.phone as client_phone,
                c.email as client_email,
                -- Риелтор
                r.id_realtor as realtor_id,
                r.first_name as realtor_first_name,
                r.last_name as realtor_last_name,
                r.middle_name as realtor_middle_name,
                r.phone as realtor_phone,
                r.email as realtor_email,
                r.experience_years as realtor_experience,
                -- Недвижимость
                p.id_property as property_id,
                p.area as property_area,
                p.cost as property_cost,
                p.description as property_description,
                p.postal_code as property_postal_code,
                p.house_number as property_house_number,
                p.house_letter as property_house_letter,
                p.building_number as property_building_number,
                p.apartment_number as property_apartment_number,
                -- География
                country.country_name,
                region.name as region_name,
                city.city_name,
                district.district_name,
                street.street_name,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            WHERE d.id_deal = ?
            """;
        return jdbcTemplate.queryForObject(sql, dealWithDetailsRowMapper, id);
    }

    /**
     * Получить все сделки для табличного отображения (компактная информация)
     * @return список сделок с компактной информацией
     */
    public List<DealTableDto> findAllForTable() {
        logger.debug("Получение списка всех сделок для табличного отображения");
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент (полное имя)
                CONCAT(c.last_name, ' ', c.first_name, 
                       CASE WHEN c.middle_name IS NOT NULL THEN CONCAT(' ', c.middle_name) ELSE '' END) as client_name,
                c.phone as client_phone,
                -- Риелтор (полное имя)
                CONCAT(r.last_name, ' ', r.first_name,
                       CASE WHEN r.middle_name IS NOT NULL THEN CONCAT(' ', r.middle_name) ELSE '' END) as realtor_name,
                -- Адрес недвижимости (краткий)
                CONCAT(street.street_name, ', ', p.house_number,
                       CASE WHEN p.apartment_number IS NOT NULL THEN CONCAT('-', p.apartment_number) ELSE '' END) as property_address,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            ORDER BY d.deal_date DESC
            """;
        return jdbcTemplate.query(sql, dealTableRowMapper);
    }

    /**
     * Найти сделки по дате с детальной информацией
     * @param date дата сделки
     * @return список сделок с полной информацией
     */
    public List<DealWithDetailsDto> findByDateWithDetails(LocalDate date) {
        if (date == null) {
            logger.error("Попытка поиска сделок с null датой");
            throw new IllegalArgumentException("Дата не может быть null");
        }
        
        logger.debug("Поиск сделок с детальной информацией по дате: {}", date);
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент
                c.id_client as client_id,
                c.first_name as client_first_name,
                c.last_name as client_last_name,
                c.middle_name as client_middle_name,
                c.phone as client_phone,
                c.email as client_email,
                -- Риелтор
                r.id_realtor as realtor_id,
                r.first_name as realtor_first_name,
                r.last_name as realtor_last_name,
                r.middle_name as realtor_middle_name,
                r.phone as realtor_phone,
                r.email as realtor_email,
                r.experience_years as realtor_experience,
                -- Недвижимость
                p.id_property as property_id,
                p.area as property_area,
                p.cost as property_cost,
                p.description as property_description,
                p.postal_code as property_postal_code,
                p.house_number as property_house_number,
                p.house_letter as property_house_letter,
                p.building_number as property_building_number,
                p.apartment_number as property_apartment_number,
                -- География
                country.country_name,
                region.name as region_name,
                city.city_name,
                district.district_name,
                street.street_name,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            WHERE d.deal_date = ?
            ORDER BY d.deal_date DESC
            """;
        return jdbcTemplate.query(sql, dealWithDetailsRowMapper, date);
    }

    /**
     * Найти сделки по диапазону дат с детальной информацией
     * @param startDate начальная дата (включительно)
     * @param endDate конечная дата (включительно)
     * @return список сделок с полной информацией
     */
    public List<DealWithDetailsDto> findByDateRangeWithDetails(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            logger.error("Попытка поиска сделок с null датами: start={}, end={}", startDate, endDate);
            throw new IllegalArgumentException("Даты не могут быть null");
        }
        
        if (startDate.isAfter(endDate)) {
            logger.error("Некорректный диапазон дат: start={}, end={}", startDate, endDate);
            throw new IllegalArgumentException("Начальная дата не может быть позже конечной");
        }
        
        logger.debug("Поиск сделок с детальной информацией по диапазону дат: {} - {}", startDate, endDate);
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент
                c.id_client as client_id,
                c.first_name as client_first_name,
                c.last_name as client_last_name,
                c.middle_name as client_middle_name,
                c.phone as client_phone,
                c.email as client_email,
                -- Риелтор
                r.id_realtor as realtor_id,
                r.first_name as realtor_first_name,
                r.last_name as realtor_last_name,
                r.middle_name as realtor_middle_name,
                r.phone as realtor_phone,
                r.email as realtor_email,
                r.experience_years as realtor_experience,
                -- Недвижимость
                p.id_property as property_id,
                p.area as property_area,
                p.cost as property_cost,
                p.description as property_description,
                p.postal_code as property_postal_code,
                p.house_number as property_house_number,
                p.house_letter as property_house_letter,
                p.building_number as property_building_number,
                p.apartment_number as property_apartment_number,
                -- География
                country.country_name,
                region.name as region_name,
                city.city_name,
                district.district_name,
                street.street_name,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            WHERE d.deal_date BETWEEN ? AND ?
            ORDER BY d.deal_date DESC
            """;
        return jdbcTemplate.query(sql, dealWithDetailsRowMapper, startDate, endDate);
    }

    /**
     * Найти сделки по риелтору с детальной информацией
     * @param realtorId идентификатор риелтора
     * @return список сделок с полной информацией
     */
    public List<DealWithDetailsDto> findByRealtorIdWithDetails(Long realtorId) {
        if (realtorId == null) {
            logger.error("Попытка поиска сделок с null id риелтора");
            throw new IllegalArgumentException("Идентификатор риелтора не может быть null");
        }
        
        logger.debug("Поиск сделок с детальной информацией по риелтору: {}", realtorId);
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент
                c.id_client as client_id,
                c.first_name as client_first_name,
                c.last_name as client_last_name,
                c.middle_name as client_middle_name,
                c.phone as client_phone,
                c.email as client_email,
                -- Риелтор
                r.id_realtor as realtor_id,
                r.first_name as realtor_first_name,
                r.last_name as realtor_last_name,
                r.middle_name as realtor_middle_name,
                r.phone as realtor_phone,
                r.email as realtor_email,
                r.experience_years as realtor_experience,
                -- Недвижимость
                p.id_property as property_id,
                p.area as property_area,
                p.cost as property_cost,
                p.description as property_description,
                p.postal_code as property_postal_code,
                p.house_number as property_house_number,
                p.house_letter as property_house_letter,
                p.building_number as property_building_number,
                p.apartment_number as property_apartment_number,
                -- География
                country.country_name,
                region.name as region_name,
                city.city_name,
                district.district_name,
                street.street_name,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            WHERE d.id_realtor = ?
            ORDER BY d.deal_date DESC
            """;
        return jdbcTemplate.query(sql, dealWithDetailsRowMapper, realtorId);
    }

    /**
     * Найти сделки по клиенту с детальной информацией
     * @param clientId идентификатор клиента
     * @return список сделок с полной информацией
     */
    public List<DealWithDetailsDto> findByClientIdWithDetails(Long clientId) {
        if (clientId == null) {
            logger.error("Попытка поиска сделок с null id клиента");
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }
        
        logger.debug("Поиск сделок с детальной информацией по клиенту: {}", clientId);
        String sql = """
            SELECT 
                d.id_deal as deal_id,
                d.deal_date,
                d.deal_cost,
                -- Клиент
                c.id_client as client_id,
                c.first_name as client_first_name,
                c.last_name as client_last_name,
                c.middle_name as client_middle_name,
                c.phone as client_phone,
                c.email as client_email,
                -- Риелтор
                r.id_realtor as realtor_id,
                r.first_name as realtor_first_name,
                r.last_name as realtor_last_name,
                r.middle_name as realtor_middle_name,
                r.phone as realtor_phone,
                r.email as realtor_email,
                r.experience_years as realtor_experience,
                -- Недвижимость
                p.id_property as property_id,
                p.area as property_area,
                p.cost as property_cost,
                p.description as property_description,
                p.postal_code as property_postal_code,
                p.house_number as property_house_number,
                p.house_letter as property_house_letter,
                p.building_number as property_building_number,
                p.apartment_number as property_apartment_number,
                -- География
                country.country_name,
                region.name as region_name,
                city.city_name,
                district.district_name,
                street.street_name,
                -- Типы
                pt.property_type_name,
                dt.deal_type_name
            FROM deals d
            JOIN clients c ON d.id_client = c.id_client
            JOIN realtors r ON d.id_realtor = r.id_realtor
            JOIN properties p ON d.id_property = p.id_property
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN deal_types dt ON d.id_deal_type = dt.id_deal_type
            WHERE d.id_client = ?
            ORDER BY d.deal_date DESC
            """;
        return jdbcTemplate.query(sql, dealWithDetailsRowMapper, clientId);
    }

    /**
     * Валидация объекта сделки для сохранения
     * @param deal объект сделки для валидации
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateDealForSave(Deal deal) {
        if (deal == null) {
            logger.error("Попытка сохранения null сделки");
            throw new IllegalArgumentException("Сделка не может быть null");
        }
        
        // Проверка обязательных полей
        if (deal.getDealDate() == null) {
            logger.error("Попытка сохранения сделки с null датой");
            throw new IllegalArgumentException("Дата сделки обязательна");
        }
        
        // Проверка, что дата не в будущем
        if (deal.getDealDate().isAfter(LocalDate.now())) {
            logger.error("Попытка сохранения сделки с датой в будущем: {}", deal.getDealDate());
            throw new IllegalArgumentException("Дата сделки не может быть в будущем");
        }
        
        if (deal.getDealCost() == null || deal.getDealCost().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Попытка сохранения сделки с некорректной стоимостью: {}", deal.getDealCost());
            throw new IllegalArgumentException("Стоимость сделки должна быть больше нуля");
        }
        
        // Проверка связанных сущностей
        if (deal.getIdProperty() == null) {
            logger.error("Попытка сохранения сделки без указания объекта недвижимости");
            throw new IllegalArgumentException("Идентификатор объекта недвижимости обязателен");
        }
        
        if (deal.getIdRealtor() == null) {
            logger.error("Попытка сохранения сделки без указания риелтора");
            throw new IllegalArgumentException("Идентификатор риелтора обязателен");
        }
        
        if (deal.getIdClient() == null) {
            logger.error("Попытка сохранения сделки без указания клиента");
            throw new IllegalArgumentException("Идентификатор клиента обязателен");
        }
        
        if (deal.getIdDealType() == null) {
            logger.error("Попытка сохранения сделки без указания типа сделки");
            throw new IllegalArgumentException("Идентификатор типа сделки обязателен");
        }
    }

    /**
     * Валидация обновляемых данных сделки
     * @param updates карта с полями для обновления
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateDealUpdates(Map<String, Object> updates) {
        // Валидация даты
        if (updates.containsKey("dealDate")) {
            LocalDate dealDate = (LocalDate) updates.get("dealDate");
            if (dealDate == null) {
                logger.error("Попытка обновления сделки с null датой");
                throw new IllegalArgumentException("Дата сделки не может быть null");
            }
            
            if (dealDate.isAfter(LocalDate.now())) {
                logger.error("Попытка обновления сделки с датой в будущем: {}", dealDate);
                throw new IllegalArgumentException("Дата сделки не может быть в будущем");
            }
        }
        
        // Валидация стоимости
        if (updates.containsKey("dealCost")) {
            BigDecimal dealCost = (BigDecimal) updates.get("dealCost");
            if (dealCost == null || dealCost.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Попытка обновления сделки с некорректной стоимостью: {}", dealCost);
                throw new IllegalArgumentException("Стоимость сделки должна быть больше нуля");
            }
        }
    }

    /**
     * Проверка существования связанных сущностей для сделки
     * @param deal объект сделки для проверки
     * @throws DataIntegrityViolationException если связанная сущность не существует
     */
    private void validateRelatedEntities(Deal deal) {
        // Проверка объекта недвижимости
        if (!propertyExists(deal.getIdProperty())) {
            logger.error("Попытка сохранения сделки с несуществующим объектом недвижимости: {}", deal.getIdProperty());
            throw new DataIntegrityViolationException("Объект недвижимости с id " + deal.getIdProperty() + " не найден");
        }
        
        // Проверка риелтора
        if (!realtorExists(deal.getIdRealtor())) {
            logger.error("Попытка сохранения сделки с несуществующим риелтором: {}", deal.getIdRealtor());
            throw new DataIntegrityViolationException("Риелтор с id " + deal.getIdRealtor() + " не найден");
        }
        
        // Проверка клиента
        if (!clientExists(deal.getIdClient())) {
            logger.error("Попытка сохранения сделки с несуществующим клиентом: {}", deal.getIdClient());
            throw new DataIntegrityViolationException("Клиент с id " + deal.getIdClient() + " не найден");
        }
        
        // Проверка типа сделки
        if (!dealTypeExists(deal.getIdDealType())) {
            logger.error("Попытка сохранения сделки с несуществующим типом: {}", deal.getIdDealType());
            throw new DataIntegrityViolationException("Тип сделки с id " + deal.getIdDealType() + " не найден");
        }
    }

    /**
     * Проверка существования связанных сущностей при обновлении
     * @param updates карта с полями для обновления
     * @throws DataIntegrityViolationException если связанная сущность не существует
     */
    private void validateRelatedEntitiesForUpdate(Map<String, Object> updates) {
        // Проверка объекта недвижимости
        if (updates.containsKey("idProperty")) {
            Long propertyId = (Long) updates.get("idProperty");
            if (!propertyExists(propertyId)) {
                logger.error("Попытка обновления сделки с несуществующим объектом недвижимости: {}", propertyId);
                throw new DataIntegrityViolationException("Объект недвижимости с id " + propertyId + " не найден");
            }
        }
        
        // Проверка риелтора
        if (updates.containsKey("idRealtor")) {
            Long realtorId = (Long) updates.get("idRealtor");
            if (!realtorExists(realtorId)) {
                logger.error("Попытка обновления сделки с несуществующим риелтором: {}", realtorId);
                throw new DataIntegrityViolationException("Риелтор с id " + realtorId + " не найден");
            }
        }
        
        // Проверка клиента
        if (updates.containsKey("idClient")) {
            Long clientId = (Long) updates.get("idClient");
            if (!clientExists(clientId)) {
                logger.error("Попытка обновления сделки с несуществующим клиентом: {}", clientId);
                throw new DataIntegrityViolationException("Клиент с id " + clientId + " не найден");
            }
        }
        
        // Проверка типа сделки
        if (updates.containsKey("idDealType")) {
            Long dealTypeId = (Long) updates.get("idDealType");
            if (!dealTypeExists(dealTypeId)) {
                logger.error("Попытка обновления сделки с несуществующим типом: {}", dealTypeId);
                throw new DataIntegrityViolationException("Тип сделки с id " + dealTypeId + " не найден");
            }
        }
    }

    /**
     * Проверить существование объекта недвижимости
     * @param propertyId идентификатор объекта недвижимости
     * @return true если объект недвижимости существует
     */
    private boolean propertyExists(Long propertyId) {
        String sql = "SELECT COUNT(*) FROM properties WHERE id_property = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, propertyId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование риелтора
     * @param realtorId идентификатор риелтора
     * @return true если риелтор существует
     */
    private boolean realtorExists(Long realtorId) {
        String sql = "SELECT COUNT(*) FROM realtors WHERE id_realtor = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, realtorId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование клиента
     * @param clientId идентификатор клиента
     * @return true если клиент существует
     */
    private boolean clientExists(Long clientId) {
        String sql = "SELECT COUNT(*) FROM clients WHERE id_client = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, clientId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование типа сделки
     * @param dealTypeId идентификатор типа сделки
     * @return true если тип сделки существует
     */
    private boolean dealTypeExists(Long dealTypeId) {
        String sql = "SELECT COUNT(*) FROM deal_types WHERE id_deal_type = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dealTypeId);
        return count != null && count > 0;
    }
} 