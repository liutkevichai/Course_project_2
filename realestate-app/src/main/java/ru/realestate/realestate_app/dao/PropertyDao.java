package ru.realestate.realestate_app.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.PropertyRowMapper;
import ru.realestate.realestate_app.model.Property;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.lang.StringBuilder;

/**
 * DAO класс для работы с объектами недвижимости
 * Обеспечивает доступ к данным недвижимости в базе данных
 */
@Repository
public class PropertyDao {

    private final JdbcTemplate jdbcTemplate;
    private final PropertyRowMapper propertyRowMapper;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     * @param propertyRowMapper маппер для преобразования строк результата в объекты Property
     */
    public PropertyDao(JdbcTemplate jdbcTemplate, PropertyRowMapper propertyRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.propertyRowMapper = propertyRowMapper;
    }

    /**
     * Получить все объекты недвижимости, отсортированные по идентификатору
     * @return список всех объектов недвижимости
     */
    public List<Property> findAll() {
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
     */
    public Property findById(Long id) {
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
     */
    @SuppressWarnings({ "null" })
    public Long save(Property property) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO properties (area, cost, description, postal_code, house_number, " +
                "house_letter, building_number, apartment_number, id_property_type, id_country, " +
                "id_region, id_city, id_district, id_street) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            
            ps.setBigDecimal(1, property.getArea());
            ps.setBigDecimal(2, property.getCost());
            ps.setString(3, property.getDescription());
            ps.setString(4, property.getPostalCode());
            ps.setString(5, property.getHouseNumber());
            ps.setString(6, property.getHouseLetter());
            ps.setString(7, property.getBuildingNumber());
            ps.setString(8, property.getApartmentNumber());
            ps.setInt(9, property.getIdPropertyType());
            ps.setInt(10, property.getIdCountry());
            ps.setInt(11, property.getIdRegion());
            ps.setInt(12, property.getIdCity());
            ps.setInt(13, property.getIdDistrict());
            ps.setInt(14, property.getIdStreet());
            
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    /**
     * Обновить данные существующего объекта недвижимости
     * @param id идентификатор объекта недвижимости для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем, что есть поля для обновления
        if (updates == null || updates.isEmpty()) {
            return false;
        }
        
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
        return updatedRows > 0;
    }

    /**
     * Удалить объект недвижимости по идентификатору
     * @param id идентификатор объекта недвижимости для удаления
     * @return true если удаление прошло успешно, false если объект не найден
     */
    public boolean deleteById(Long id) {
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM properties WHERE id_property = ?",
            id
        );
        
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
    public List<Property> findByCityId(Integer cityId) {
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
    public List<Property> findByPropertyTypeId(Integer propertyTypeId) {
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
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM properties",
            Integer.class
        );
        return count != null ? count : 0;
    }
} 