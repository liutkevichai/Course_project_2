package ru.realestate.realestate_app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.PropertyRowMapper;
import ru.realestate.realestate_app.mapper.dto.PropertyWithDetailsRowMapper;
import ru.realestate.realestate_app.mapper.dto.PropertyTableRowMapper;
import ru.realestate.realestate_app.model.Property;
import ru.realestate.realestate_app.model.dto.PropertyWithDetailsDto;
import ru.realestate.realestate_app.model.dto.PropertyTableDto;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * DAO класс для работы с объектами недвижимости
 * Обеспечивает доступ к данным недвижимости в базе данных
 */
@Repository
public class PropertyDao {

    // Логгер для записи событий и ошибок
    private static final Logger logger = LoggerFactory.getLogger(PropertyDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final PropertyRowMapper propertyRowMapper;
    private final PropertyWithDetailsRowMapper propertyWithDetailsRowMapper;
    private final PropertyTableRowMapper propertyTableRowMapper;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     * @param propertyRowMapper маппер для преобразования строк результата в объекты Property
     * @param propertyWithDetailsRowMapper маппер для PropertyWithDetailsDto
     * @param propertyTableRowMapper маппер для PropertyTableDto
     */
    public PropertyDao(JdbcTemplate jdbcTemplate, PropertyRowMapper propertyRowMapper,
                      PropertyWithDetailsRowMapper propertyWithDetailsRowMapper,
                      PropertyTableRowMapper propertyTableRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.propertyRowMapper = propertyRowMapper;
        this.propertyWithDetailsRowMapper = propertyWithDetailsRowMapper;
        this.propertyTableRowMapper = propertyTableRowMapper;
    }

    /**
     * Получить все объекты недвижимости, отсортированные по идентификатору
     * @return список всех объектов недвижимости
     */
    public List<Property> findAll() {
        logger.debug("Получение списка всех объектов недвижимости");
        return jdbcTemplate.query(
            "SELECT * FROM properties ORDER BY id_property",
            propertyRowMapper
        );
    }

    /**
     * Найти объект недвижимости по уникальному идентификатору
     * @param id идентификатор объекта недвижимости
     * @return объект недвижимости
     * @throws org.springframework.dao.EmptyResultDataAccessException если объект не найден
     * @throws IllegalArgumentException если id равен null
     */
    public Property findById(Long id) {
        // Валидация входного параметра
        if (id == null) {
            logger.error("Попытка поиска объекта недвижимости с null id");
            throw new IllegalArgumentException("Идентификатор объекта недвижимости не может быть null");
        }
        
        logger.debug("Поиск объекта недвижимости по id: {}", id);
        return jdbcTemplate.queryForObject(
            "SELECT * FROM properties WHERE id_property = ?",
            propertyRowMapper,
            id
        );
    }

    /**
     * Сохранить новый объект недвижимости в базе данных
     * @param property объект недвижимости для сохранения
     * @return идентификатор созданного объекта недвижимости
     * @throws IllegalArgumentException если данные объекта некорректны
     * @throws DataIntegrityViolationException если связанные сущности не существуют
     */
    @SuppressWarnings({ "null" })
    public Long save(Property property) {
        // Валидация входного объекта
        validatePropertyForSave(property);
        
        // Проверка существования связанных сущностей
        validateRelatedEntities(property);
        
        logger.debug("Сохранение нового объекта недвижимости: тип={}, стоимость={}", 
                    property.getIdPropertyType(), property.getCost());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO properties (area, cost, description, postal_code, house_number, " +
                "house_letter, building_number, apartment_number, id_property_type, id_country, " +
                "id_region, id_city, id_district, id_street) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new String[]{"id_property"}
            );
            
            ps.setBigDecimal(1, property.getArea());
            ps.setBigDecimal(2, property.getCost());
            ps.setString(3, property.getDescription());
            ps.setString(4, property.getPostalCode());
            ps.setString(5, property.getHouseNumber());
            ps.setString(6, property.getHouseLetter());
            ps.setString(7, property.getBuildingNumber());
            ps.setString(8, property.getApartmentNumber());
            ps.setLong(9, property.getIdPropertyType());
            ps.setLong(10, property.getIdCountry());
            ps.setLong(11, property.getIdRegion());
            ps.setLong(12, property.getIdCity());
            ps.setLong(13, property.getIdDistrict());
            ps.setLong(14, property.getIdStreet());
            
            return ps;
        }, keyHolder);
        
        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        if (generatedId == null) {
            logger.error("Не удалось получить сгенерированный id для объекта недвижимости");
            throw new DataIntegrityViolationException("Не удалось создать объект недвижимости в базе данных");
        }
        
        logger.info("Объект недвижимости успешно сохранен с id: {}", generatedId);
        return generatedId;
    }

    /**
     * Обновить данные существующего объекта недвижимости
     * @param id идентификатор объекта недвижимости для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws IllegalArgumentException если id равен null или данные некорректны
     * @throws DataIntegrityViolationException если связанные сущности не существуют
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Валидация входных параметров
        if (id == null) {
            logger.error("Попытка обновления объекта недвижимости с null id");
            throw new IllegalArgumentException("Идентификатор объекта недвижимости не может быть null");
        }
        
        // Проверяем, что есть поля для обновления
        if (updates == null || updates.isEmpty()) {
            logger.debug("Нет данных для обновления объекта недвижимости с id: {}", id);
            return false;
        }
        
        // Валидация обновляемых данных
        validatePropertyUpdates(updates);
        
        // Проверка существования связанных сущностей при обновлении
        validateRelatedEntitiesForUpdate(updates);
        
        logger.debug("Обновление объекта недвижимости с id: {}", id);
        
        // Строим динамический SQL запрос
        StringBuilder sql = new StringBuilder("UPDATE properties SET ");
        List<Object> params = new ArrayList<>();
        
        // Добавляем поля для обновления
        if (updates.containsKey("area")) {
            sql.append("area = ?, ");
            params.add(updates.get("area"));
        }
        if (updates.containsKey("cost")) {
            sql.append("cost = ?, ");
            params.add(updates.get("cost"));
        }
        if (updates.containsKey("description")) {
            sql.append("description = ?, ");
            params.add(updates.get("description"));
        }
        if (updates.containsKey("postalCode")) {
            sql.append("postal_code = ?, ");
            params.add(updates.get("postalCode"));
        }
        if (updates.containsKey("houseNumber")) {
            sql.append("house_number = ?, ");
            params.add(updates.get("houseNumber"));
        }
        if (updates.containsKey("houseLetter")) {
            sql.append("house_letter = ?, ");
            params.add(updates.get("houseLetter"));
        }
        if (updates.containsKey("buildingNumber")) {
            sql.append("building_number = ?, ");
            params.add(updates.get("buildingNumber"));
        }
        if (updates.containsKey("apartmentNumber")) {
            sql.append("apartment_number = ?, ");
            params.add(updates.get("apartmentNumber"));
        }
        if (updates.containsKey("idPropertyType")) {
            sql.append("id_property_type = ?, ");
            params.add(updates.get("idPropertyType"));
        }
        if (updates.containsKey("idCountry")) {
            sql.append("id_country = ?, ");
            params.add(updates.get("idCountry"));
        }
        if (updates.containsKey("idRegion")) {
            sql.append("id_region = ?, ");
            params.add(updates.get("idRegion"));
        }
        if (updates.containsKey("idCity")) {
            sql.append("id_city = ?, ");
            params.add(updates.get("idCity"));
        }
        if (updates.containsKey("idDistrict")) {
            sql.append("id_district = ?, ");
            params.add(updates.get("idDistrict"));
        }
        if (updates.containsKey("idStreet")) {
            sql.append("id_street = ?, ");
            params.add(updates.get("idStreet"));
        }
        
        // Убираем последнюю запятую и пробел
        if (sql.charAt(sql.length() - 2) == ',') {
            sql.setLength(sql.length() - 2);
        }
        
        // Добавляем условие WHERE
        sql.append(" WHERE id_property = ?");
        params.add(id);
        
        // Выполняем обновление
        int updatedRows = jdbcTemplate.update(sql.toString(), params.toArray());
        
        if (updatedRows > 0) {
            logger.info("Объект недвижимости с id {} успешно обновлен", id);
        } else {
            logger.warn("Объект недвижимости с id {} не найден для обновления", id);
        }
        
        return updatedRows > 0;
    }

    /**
     * Удалить объект недвижимости по идентификатору
     * @param id идентификатор объекта недвижимости для удаления
     * @return true если удаление прошло успешно, false если объект не найден
     * @throws IllegalArgumentException если id равен null
     */
    public boolean deleteById(Long id) {
        // Валидация входного параметра
        if (id == null) {
            logger.error("Попытка удаления объекта недвижимости с null id");
            throw new IllegalArgumentException("Идентификатор объекта недвижимости не может быть null");
        }
        
        logger.debug("Удаление объекта недвижимости с id: {}", id);
        
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM properties WHERE id_property = ?",
            id
        );
        
        if (deletedRows > 0) {
            logger.info("Объект недвижимости с id {} успешно удален", id);
        } else {
            logger.warn("Объект недвижимости с id {} не найден для удаления", id);
        }
        
        return deletedRows > 0;
    }

    /**
     * Найти объекты недвижимости в указанном ценовом диапазоне
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список объектов недвижимости в ценовом диапазоне, отсортированный по цене
     */
    public List<Property> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return jdbcTemplate.query(
            "SELECT * FROM properties WHERE cost BETWEEN ? AND ? ORDER BY cost",
            propertyRowMapper,
            minPrice, maxPrice
        );
    }

    /**
     * Найти объекты недвижимости по городу
     * @param cityId идентификатор города
     * @return список объектов недвижимости в указанном городе, отсортированный по цене
     */
    public List<Property> findByCityId(Long cityId) {
        return jdbcTemplate.query(
            "SELECT * FROM properties WHERE id_city = ? ORDER BY cost",
            propertyRowMapper,
            cityId
        );
    }

    /**
     * Найти объекты недвижимости по типу
     * @param propertyTypeId идентификатор типа недвижимости
     * @return список объектов недвижимости указанного типа, отсортированный по цене
     */
    public List<Property> findByPropertyTypeId(Long propertyTypeId) {
        return jdbcTemplate.query(
            "SELECT * FROM properties WHERE id_property_type = ? ORDER BY cost",
            propertyRowMapper,
            propertyTypeId
        );
    }

    /**
     * Получить общее количество объектов недвижимости в базе данных
     * @return количество объектов недвижимости
     */
    public int getCount() {
        logger.debug("Получение общего количества объектов недвижимости");
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM properties",
            Integer.class
        );
        return count != null ? count : 0;
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все объекты недвижимости с детальной информацией (JOIN запрос)
     * @return список всех объектов недвижимости с полной географической информацией
     */
    public List<PropertyWithDetailsDto> findAllWithDetails() {
        logger.debug("Получение списка всех объектов недвижимости с детальной информацией");
        String sql = """
            SELECT 
                p.id_property as property_id,
                p.area,
                p.cost,
                p.description,
                p.postal_code,
                p.house_number,
                p.house_letter,
                p.building_number,
                p.apartment_number,
                -- Тип недвижимости
                pt.id_property_type as property_type_id,
                pt.property_type_name,
                -- География
                country.id_country as country_id,
                country.country_name,
                region.id_region as region_id,
                region.name as region_name,
                region.code as region_code,
                city.id_city as city_id,
                city.city_name,
                district.id_district as district_id,
                district.district_name,
                street.id_street as street_id,
                street.street_name
            FROM properties p
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            ORDER BY p.cost DESC
            """;
        return jdbcTemplate.query(sql, propertyWithDetailsRowMapper);
    }

    /**
     * Найти объект недвижимости с детальной информацией по идентификатору
     * @param id идентификатор объекта недвижимости
     * @return объект недвижимости с полной географической информацией
     * @throws org.springframework.dao.EmptyResultDataAccessException если объект не найден
     */
    public PropertyWithDetailsDto findByIdWithDetails(Long id) {
        if (id == null) {
            logger.error("Попытка поиска объекта недвижимости с детальной информацией с null id");
            throw new IllegalArgumentException("Идентификатор объекта недвижимости не может быть null");
        }
        
        logger.debug("Поиск объекта недвижимости с детальной информацией по id: {}", id);
        String sql = """
            SELECT 
                p.id_property as property_id,
                p.area,
                p.cost,
                p.description,
                p.postal_code,
                p.house_number,
                p.house_letter,
                p.building_number,
                p.apartment_number,
                -- Тип недвижимости
                pt.id_property_type as property_type_id,
                pt.property_type_name,
                -- География
                country.id_country as country_id,
                country.country_name,
                region.id_region as region_id,
                region.name as region_name,
                region.code as region_code,
                city.id_city as city_id,
                city.city_name,
                district.id_district as district_id,
                district.district_name,
                street.id_street as street_id,
                street.street_name
            FROM properties p
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            WHERE p.id_property = ?
            """;
        return jdbcTemplate.queryForObject(sql, propertyWithDetailsRowMapper, id);
    }

    /**
     * Получить все объекты недвижимости для табличного отображения (компактная информация)
     * @return список объектов недвижимости с компактной информацией
     */
    public List<PropertyTableDto> findAllForTable() {
        logger.debug("Получение списка всех объектов недвижимости для табличного отображения");
        String sql = """
            SELECT 
                p.id_property as property_id,
                p.area,
                p.cost,
                SUBSTRING(p.description, 1, 100) as short_description,
                p.house_number,
                p.house_letter,
                p.building_number,
                p.apartment_number,
                -- Тип недвижимости
                pt.property_type_name,
                -- География для адреса
                city.city_name,
                district.district_name,
                street.street_name
            FROM properties p
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            ORDER BY p.cost DESC
            """;
        return jdbcTemplate.query(sql, propertyTableRowMapper);
    }

    /**
     * Найти объекты недвижимости по ценовому диапазону с детальной информацией
     * @param minPrice минимальная цена (включительно)
     * @param maxPrice максимальная цена (включительно)
     * @return список объектов недвижимости с полной информацией
     */
    public List<PropertyWithDetailsDto> findByPriceRangeWithDetails(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Поиск объектов недвижимости с детальной информацией по ценовому диапазону: {} - {}", minPrice, maxPrice);
        String sql = """
            SELECT 
                p.id_property as property_id,
                p.area,
                p.cost,
                p.description,
                p.postal_code,
                p.house_number,
                p.house_letter,
                p.building_number,
                p.apartment_number,
                -- Тип недвижимости
                pt.id_property_type as property_type_id,
                pt.property_type_name,
                -- География
                country.id_country as country_id,
                country.country_name,
                region.id_region as region_id,
                region.name as region_name,
                region.code as region_code,
                city.id_city as city_id,
                city.city_name,
                district.id_district as district_id,
                district.district_name,
                street.id_street as street_id,
                street.street_name
            FROM properties p
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            WHERE p.cost BETWEEN ? AND ?
            ORDER BY p.cost
            """;
        return jdbcTemplate.query(sql, propertyWithDetailsRowMapper, minPrice, maxPrice);
    }

    /**
     * Найти объекты недвижимости по городу с детальной информацией
     * @param cityId идентификатор города
     * @return список объектов недвижимости с полной информацией
     */
    public List<PropertyWithDetailsDto> findByCityIdWithDetails(Long cityId) {
        if (cityId == null) {
            logger.error("Попытка поиска объектов недвижимости с null id города");
            throw new IllegalArgumentException("Идентификатор города не может быть null");
        }
        
        logger.debug("Поиск объектов недвижимости с детальной информацией по городу: {}", cityId);
        String sql = """
            SELECT 
                p.id_property as property_id,
                p.area,
                p.cost,
                p.description,
                p.postal_code,
                p.house_number,
                p.house_letter,
                p.building_number,
                p.apartment_number,
                -- Тип недвижимости
                pt.id_property_type as property_type_id,
                pt.property_type_name,
                -- География
                country.id_country as country_id,
                country.country_name,
                region.id_region as region_id,
                region.name as region_name,
                region.code as region_code,
                city.id_city as city_id,
                city.city_name,
                district.id_district as district_id,
                district.district_name,
                street.id_street as street_id,
                street.street_name
            FROM properties p
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            WHERE p.id_city = ?
            ORDER BY p.cost
            """;
        return jdbcTemplate.query(sql, propertyWithDetailsRowMapper, cityId);
    }

    /**
     * Найти объекты недвижимости по типу с детальной информацией
     * @param propertyTypeId идентификатор типа недвижимости
     * @return список объектов недвижимости с полной информацией
     */
    public List<PropertyWithDetailsDto> findByPropertyTypeIdWithDetails(Long propertyTypeId) {
        if (propertyTypeId == null) {
            logger.error("Попытка поиска объектов недвижимости с null id типа");
            throw new IllegalArgumentException("Идентификатор типа недвижимости не может быть null");
        }
        
        logger.debug("Поиск объектов недвижимости с детальной информацией по типу: {}", propertyTypeId);
        String sql = """
            SELECT 
                p.id_property as property_id,
                p.area,
                p.cost,
                p.description,
                p.postal_code,
                p.house_number,
                p.house_letter,
                p.building_number,
                p.apartment_number,
                -- Тип недвижимости
                pt.id_property_type as property_type_id,
                pt.property_type_name,
                -- География
                country.id_country as country_id,
                country.country_name,
                region.id_region as region_id,
                region.name as region_name,
                region.code as region_code,
                city.id_city as city_id,
                city.city_name,
                district.id_district as district_id,
                district.district_name,
                street.id_street as street_id,
                street.street_name
            FROM properties p
            JOIN property_types pt ON p.id_property_type = pt.id_property_type
            JOIN countries country ON p.id_country = country.id_country
            JOIN regions region ON p.id_region = region.id_region
            JOIN cities city ON p.id_city = city.id_city
            JOIN districts district ON p.id_district = district.id_district
            JOIN streets street ON p.id_street = street.id_street
            WHERE p.id_property_type = ?
            ORDER BY p.cost
            """;
        return jdbcTemplate.query(sql, propertyWithDetailsRowMapper, propertyTypeId);
    }

    /**
     * Валидация объекта недвижимости для сохранения
     * @param property объект недвижимости для валидации
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validatePropertyForSave(Property property) {
        if (property == null) {
            logger.error("Попытка сохранения null объекта недвижимости");
            throw new IllegalArgumentException("Объект недвижимости не может быть null");
        }
        
        // Проверка обязательных полей
        if (property.getArea() == null || property.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Попытка сохранения объекта недвижимости с некорректной площадью: {}", property.getArea());
            throw new IllegalArgumentException("Площадь объекта недвижимости должна быть больше нуля");
        }
        
        if (property.getCost() == null || property.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Попытка сохранения объекта недвижимости с некорректной стоимостью: {}", property.getCost());
            throw new IllegalArgumentException("Стоимость объекта недвижимости должна быть больше нуля");
        }
        
        // Проверка географических идентификаторов
        if (property.getIdCountry() == null) {
            logger.error("Попытка сохранения объекта недвижимости без указания страны");
            throw new IllegalArgumentException("Идентификатор страны обязателен");
        }
        
        if (property.getIdRegion() == null) {
            logger.error("Попытка сохранения объекта недвижимости без указания региона");
            throw new IllegalArgumentException("Идентификатор региона обязателен");
        }
        
        if (property.getIdCity() == null) {
            logger.error("Попытка сохранения объекта недвижимости без указания города");
            throw new IllegalArgumentException("Идентификатор города обязателен");
        }
        
        if (property.getIdStreet() == null) {
            logger.error("Попытка сохранения объекта недвижимости без указания улицы");
            throw new IllegalArgumentException("Идентификатор улицы обязателен");
        }
        
        if (property.getIdPropertyType() == null) {
            logger.error("Попытка сохранения объекта недвижимости без указания типа");
            throw new IllegalArgumentException("Идентификатор типа недвижимости обязателен");
        }
        
        // Проверка номера дома
        if (property.getHouseNumber() == null || property.getHouseNumber().trim().isEmpty()) {
            logger.error("Попытка сохранения объекта недвижимости без номера дома");
            throw new IllegalArgumentException("Номер дома обязателен");
        }
    }

    /**
     * Валидация обновляемых данных объекта недвижимости
     * @param updates карта с полями для обновления
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validatePropertyUpdates(Map<String, Object> updates) {
        // Валидация площади
        if (updates.containsKey("area")) {
            BigDecimal area = (BigDecimal) updates.get("area");
            if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Попытка обновления объекта недвижимости с некорректной площадью: {}", area);
                throw new IllegalArgumentException("Площадь объекта недвижимости должна быть больше нуля");
            }
        }
        
        // Валидация стоимости
        if (updates.containsKey("cost")) {
            BigDecimal cost = (BigDecimal) updates.get("cost");
            if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Попытка обновления объекта недвижимости с некорректной стоимостью: {}", cost);
                throw new IllegalArgumentException("Стоимость объекта недвижимости должна быть больше нуля");
            }
        }
        
        // Валидация номера дома
        if (updates.containsKey("houseNumber")) {
            String houseNumber = (String) updates.get("houseNumber");
            if (houseNumber == null || houseNumber.trim().isEmpty()) {
                logger.error("Попытка обновления объекта недвижимости с пустым номером дома");
                throw new IllegalArgumentException("Номер дома не может быть пустым");
            }
        }
    }

    /**
     * Проверка существования связанных сущностей для объекта недвижимости
     * @param property объект недвижимости для проверки
     * @throws DataIntegrityViolationException если связанная сущность не существует
     */
    private void validateRelatedEntities(Property property) {
        // Проверка типа недвижимости
        if (!propertyTypeExists(property.getIdPropertyType())) {
            logger.error("Попытка сохранения объекта недвижимости с несуществующим типом: {}", property.getIdPropertyType());
            throw new DataIntegrityViolationException("Тип недвижимости с id " + property.getIdPropertyType() + " не найден");
        }
        
        // Проверка страны
        if (!countryExists(property.getIdCountry())) {
            logger.error("Попытка сохранения объекта недвижимости с несуществующей страной: {}", property.getIdCountry());
            throw new DataIntegrityViolationException("Страна с id " + property.getIdCountry() + " не найдена");
        }
        
        // Проверка региона
        if (!regionExists(property.getIdRegion())) {
            logger.error("Попытка сохранения объекта недвижимости с несуществующим регионом: {}", property.getIdRegion());
            throw new DataIntegrityViolationException("Регион с id " + property.getIdRegion() + " не найден");
        }
        
        // Проверка города
        if (!cityExists(property.getIdCity())) {
            logger.error("Попытка сохранения объекта недвижимости с несуществующим городом: {}", property.getIdCity());
            throw new DataIntegrityViolationException("Город с id " + property.getIdCity() + " не найден");
        }
        
        // Проверка района (если указан)
        if (property.getIdDistrict() != null && !districtExists(property.getIdDistrict())) {
            logger.error("Попытка сохранения объекта недвижимости с несуществующим районом: {}", property.getIdDistrict());
            throw new DataIntegrityViolationException("Район с id " + property.getIdDistrict() + " не найден");
        }
        
        // Проверка улицы
        if (!streetExists(property.getIdStreet())) {
            logger.error("Попытка сохранения объекта недвижимости с несуществующей улицей: {}", property.getIdStreet());
            throw new DataIntegrityViolationException("Улица с id " + property.getIdStreet() + " не найдена");
        }
    }

    /**
     * Проверка существования связанных сущностей при обновлении
     * @param updates карта с полями для обновления
     * @throws DataIntegrityViolationException если связанная сущность не существует
     */
    private void validateRelatedEntitiesForUpdate(Map<String, Object> updates) {
        // Проверка типа недвижимости
        if (updates.containsKey("idPropertyType")) {
            Long propertyTypeId = (Long) updates.get("idPropertyType");
            if (!propertyTypeExists(propertyTypeId)) {
                logger.error("Попытка обновления объекта недвижимости с несуществующим типом: {}", propertyTypeId);
                throw new DataIntegrityViolationException("Тип недвижимости с id " + propertyTypeId + " не найден");
            }
        }
        
        // Проверка страны
        if (updates.containsKey("idCountry")) {
            Long countryId = (Long) updates.get("idCountry");
            if (!countryExists(countryId)) {
                logger.error("Попытка обновления объекта недвижимости с несуществующей страной: {}", countryId);
                throw new DataIntegrityViolationException("Страна с id " + countryId + " не найдена");
            }
        }
        
        // Проверка региона
        if (updates.containsKey("idRegion")) {
            Long regionId = (Long) updates.get("idRegion");
            if (!regionExists(regionId)) {
                logger.error("Попытка обновления объекта недвижимости с несуществующим регионом: {}", regionId);
                throw new DataIntegrityViolationException("Регион с id " + regionId + " не найден");
            }
        }
        
        // Проверка города
        if (updates.containsKey("idCity")) {
            Long cityId = (Long) updates.get("idCity");
            if (!cityExists(cityId)) {
                logger.error("Попытка обновления объекта недвижимости с несуществующим городом: {}", cityId);
                throw new DataIntegrityViolationException("Город с id " + cityId + " не найден");
            }
        }
        
        // Проверка района
        if (updates.containsKey("idDistrict")) {
            Long districtId = (Long) updates.get("idDistrict");
            if (districtId != null && !districtExists(districtId)) {
                logger.error("Попытка обновления объекта недвижимости с несуществующим районом: {}", districtId);
                throw new DataIntegrityViolationException("Район с id " + districtId + " не найден");
            }
        }
        
        // Проверка улицы
        if (updates.containsKey("idStreet")) {
            Long streetId = (Long) updates.get("idStreet");
            if (!streetExists(streetId)) {
                logger.error("Попытка обновления объекта недвижимости с несуществующей улицей: {}", streetId);
                throw new DataIntegrityViolationException("Улица с id " + streetId + " не найдена");
            }
        }
    }

    /**
     * Проверить существование типа недвижимости
     * @param propertyTypeId идентификатор типа недвижимости
     * @return true если тип недвижимости существует
     */
    private boolean propertyTypeExists(Long propertyTypeId) {
        String sql = "SELECT COUNT(*) FROM property_types WHERE id_property_type = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, propertyTypeId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование страны
     * @param countryId идентификатор страны
     * @return true если страна существует
     */
    private boolean countryExists(Long countryId) {
        String sql = "SELECT COUNT(*) FROM countries WHERE id_country = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, countryId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование региона
     * @param regionId идентификатор региона
     * @return true если регион существует
     */
    private boolean regionExists(Long regionId) {
        String sql = "SELECT COUNT(*) FROM regions WHERE id_region = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, regionId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование города
     * @param cityId идентификатор города
     * @return true если город существует
     */
    private boolean cityExists(Long cityId) {
        String sql = "SELECT COUNT(*) FROM cities WHERE id_city = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cityId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование района
     * @param districtId идентификатор района
     * @return true если район существует
     */
    private boolean districtExists(Long districtId) {
        String sql = "SELECT COUNT(*) FROM districts WHERE id_district = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, districtId);
        return count != null && count > 0;
    }

    /**
     * Проверить существование улицы
     * @param streetId идентификатор улицы
     * @return true если улица существует
     */
    private boolean streetExists(Long streetId) {
        String sql = "SELECT COUNT(*) FROM streets WHERE id_street = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, streetId);
        return count != null && count > 0;
    }
} 