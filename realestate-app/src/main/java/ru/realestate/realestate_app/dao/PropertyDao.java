package ru.realestate.realestate_app.dao;

import org.springframework.beans.factory.annotation.Autowired;
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

@Repository
public class PropertyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PropertyRowMapper propertyRowMapper;

    /**
     * Получить все объекты недвижимости
     */
    public List<Property> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM properties ORDER BY id_property",
            propertyRowMapper
        );
    }

    /**
     * Найти недвижимость по ID
     */
    public Property findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM properties WHERE id_property = ?",
            propertyRowMapper,
            id
        );
    }

    /**
     * Сохранить новую недвижимость
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
     * Обновление недвижимости
     * @param id ID недвижимости для обновления
     * @param updates Map с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно
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
     * Удалить недвижимость по ID
     */
    public boolean deleteById(Long id) {
        int deletedRows = jdbcTemplate.update(
            "DELETE FROM properties WHERE id_property = ?",
            id
        );
        
        return deletedRows > 0;
    }

    /**
     * Найти недвижимость по ценовому диапазону
     */
    public List<Property> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return jdbcTemplate.query(
            "SELECT * FROM properties WHERE cost BETWEEN ? AND ? ORDER BY cost",
            propertyRowMapper,
            minPrice, maxPrice
        );
    }

    /**
     * Найти недвижимость по городу
     */
    public List<Property> findByCityId(Integer cityId) {
        return jdbcTemplate.query(
            "SELECT * FROM properties WHERE id_city = ? ORDER BY cost",
            propertyRowMapper,
            cityId
        );
    }

    /**
     * Найти недвижимость по типу
     */
    public List<Property> findByPropertyTypeId(Integer propertyTypeId) {
        return jdbcTemplate.query(
            "SELECT * FROM properties WHERE id_property_type = ? ORDER BY cost",
            propertyRowMapper,
            propertyTypeId
        );
    }

    /**
     * Получить количество объектов недвижимости
     */
    public int getCount() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM properties",
            Integer.class
        );
        return count != null ? count : 0;
    }
} 