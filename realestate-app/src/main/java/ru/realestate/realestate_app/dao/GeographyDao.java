package ru.realestate.realestate_app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.model.geography.*;

import java.util.List;

@Repository
public class GeographyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ========== СТРАНЫ ==========
    
    /**
     * Получить все страны
     */
    public List<Country> findAllCountries() {
        return jdbcTemplate.query(
            "SELECT * FROM countries ORDER BY country_name",
            (rs, _) -> new Country(
                rs.getLong("id_country"),
                rs.getString("country_name")
            )
        );
    }

    /**
     * Найти страну по ID
     */
    public Country findCountryById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM countries WHERE id_country = ?",
            (rs, _) -> new Country(
                rs.getLong("id_country"),
                rs.getString("country_name")
            ),
            id
        );
    }

    // ========== РЕГИОНЫ ==========
    
    /**
     * Получить все регионы
     */
    public List<Region> findAllRegions() {
        return jdbcTemplate.query(
            "SELECT * FROM regions ORDER BY name",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code")
            )
        );
    }

    /**
     * Найти регион по ID
     */
    public Region findRegionById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM regions WHERE id_region = ?",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code")
            ),
            id
        );
    }

    // ========== ГОРОДА ==========
    
    /**
     * Получить все города
     */
    public List<City> findAllCities() {
        return jdbcTemplate.query(
            "SELECT * FROM cities ORDER BY city_name",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name")
            )
        );
    }

    /**
     * Найти город по ID
     */
    public City findCityById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM cities WHERE id_city = ?",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name")
            ),
            id
        );
    }

    /**
     * Найти города по региону
     */
    public List<City> findCitiesByRegion(Long regionId) {
        return jdbcTemplate.query(
            "SELECT * FROM cities WHERE id_region = ? ORDER BY city_name",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name")
            ),
            regionId
        );
    }

    // ========== РАЙОНЫ ==========
    
    /**
     * Получить все районы
     */
    public List<District> findAllDistricts() {
        return jdbcTemplate.query(
            "SELECT * FROM districts ORDER BY district_name",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name")
            )
        );
    }

    /**
     * Найти район по ID
     */
    public District findDistrictById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM districts WHERE id_district = ?",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name")
            ),
            id
        );
    }

    /**
     * Найти районы по городу
     */
    public List<District> findDistrictsByCity(Long cityId) {
        return jdbcTemplate.query(
            "SELECT * FROM districts WHERE id_city = ? ORDER BY district_name",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name")
            ),
            cityId
        );
    }

    // ========== УЛИЦЫ ==========
    
    /**
     * Получить все улицы
     */
    public List<Street> findAllStreets() {
        return jdbcTemplate.query(
            "SELECT * FROM streets ORDER BY street_name",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name")
            )
        );
    }

    /**
     * Найти улицу по ID
     */
    public Street findStreetById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM streets WHERE id_street = ?",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name")
            ),
            id
        );
    }

    /**
     * Найти улицы по району
     */
    public List<Street> findStreetsByDistrict(Long districtId) {
        return jdbcTemplate.query(
            "SELECT * FROM streets WHERE id_district = ? ORDER BY street_name",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name")
            ),
            districtId
        );
    }
} 