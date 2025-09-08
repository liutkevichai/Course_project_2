package ru.realestate.realestate_app.dao.reference;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.realestate.realestate_app.mapper.dto.RegionWithDetailsRowMapper;
import ru.realestate.realestate_app.mapper.dto.CityWithDetailsRowMapper;
import ru.realestate.realestate_app.mapper.dto.DistrictWithDetailsRowMapper;
import ru.realestate.realestate_app.mapper.dto.StreetWithDetailsRowMapper;
import ru.realestate.realestate_app.model.geography.*;
import ru.realestate.realestate_app.model.dto.RegionWithDetailsDto;
import ru.realestate.realestate_app.model.dto.CityWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DistrictWithDetailsDto;
import ru.realestate.realestate_app.model.dto.StreetWithDetailsDto;

import java.util.List;
import java.util.ArrayList;

/**
 * DAO класс для работы с географическими справочниками
 * Обеспечивает доступ к данным о странах, регионах, городах, районах и улицах
 */
@Repository
public class GeographyDao {

    private final JdbcTemplate jdbcTemplate;
    private final RegionWithDetailsRowMapper regionWithDetailsRowMapper;
    private final CityWithDetailsRowMapper cityWithDetailsRowMapper;
    private final DistrictWithDetailsRowMapper districtWithDetailsRowMapper;
    private final StreetWithDetailsRowMapper streetWithDetailsRowMapper;

    /**
     * Конструктор DAO с инжекцией зависимостей
     * @param jdbcTemplate шаблон для выполнения SQL запросов
     * @param regionWithDetailsRowMapper маппер для RegionWithDetailsDto
     * @param cityWithDetailsRowMapper маппер для CityWithDetailsDto
     * @param districtWithDetailsRowMapper маппер для DistrictWithDetailsDto
     * @param streetWithDetailsRowMapper маппер для StreetWithDetailsDto
     */
    public GeographyDao(JdbcTemplate jdbcTemplate,
                       RegionWithDetailsRowMapper regionWithDetailsRowMapper,
                       CityWithDetailsRowMapper cityWithDetailsRowMapper,
                       DistrictWithDetailsRowMapper districtWithDetailsRowMapper,
                       StreetWithDetailsRowMapper streetWithDetailsRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.regionWithDetailsRowMapper = regionWithDetailsRowMapper;
        this.cityWithDetailsRowMapper = cityWithDetailsRowMapper;
        this.districtWithDetailsRowMapper = districtWithDetailsRowMapper;
        this.streetWithDetailsRowMapper = streetWithDetailsRowMapper;
    }

    // ========== СТРАНЫ ==========
    
    /**
     * Получить все страны, отсортированные по названию
     * @return список всех стран
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
     * Найти страну по уникальному идентификатору
     * @param id идентификатор страны
     * @return объект страны
     * @throws org.springframework.dao.EmptyResultDataAccessException если страна не найдена
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

    /**
     * Найти страну по названию (точное совпадение)
     * @param name название страны
     * @return объект страны
     * @throws org.springframework.dao.EmptyResultDataAccessException если страна не найдена
     */
    public Country findCountryByName(String name) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM countries WHERE country_name = ?",
            (rs, _) -> new Country(
                rs.getLong("id_country"),
                rs.getString("country_name")
            ),
            name
        );
    }

    // ========== РЕГИОНЫ ==========
    
    /**
     * Получить все регионы, отсортированные по названию
     * @return список всех регионов
     */
    public List<Region> findAllRegions() {
        return jdbcTemplate.query(
            "SELECT * FROM regions ORDER BY name",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getLong("id_country")
            )
        );
    }

    /**
     * Найти регион по уникальному идентификатору
     * @param id идентификатор региона
     * @return объект региона
     * @throws org.springframework.dao.EmptyResultDataAccessException если регион не найден
     */
    public Region findRegionById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM regions WHERE id_region = ?",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getLong("id_country")
            ),
            id
        );
    }

    /**
     * Найти регионы по коду (может быть несколько регионов с одинаковым кодом в разных странах)
     * @param code код региона
     * @return список регионов с указанным кодом, отсортированный по названию
     */
    public List<Region> findRegionByCode(String code) {
        return jdbcTemplate.query(
            "SELECT * FROM regions WHERE code = ? ORDER BY name",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getLong("id_country")
            ), code
        );
    }

    /**
     * Найти регионы по стране
     * @param countryId идентификатор страны
     * @return список регионов указанной страны, отсортированный по названию
     */
    public List<Region> findRegionsByCountry(Long countryId) {
        return jdbcTemplate.query(
            "SELECT * FROM regions WHERE id_country = ? ORDER BY name",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getLong("id_country")
            ),
            countryId
        );
    }

    /**
     * Найти регион по названию и стране (для точного поиска)
     * @param regionName название региона
     * @param countryId идентификатор страны
     * @return объект региона
     * @throws org.springframework.dao.EmptyResultDataAccessException если регион не найден
     */
    public Region findRegionByNameAndCountry(String regionName, Long countryId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM regions WHERE name = ? AND id_country = ?",
            (rs, _) -> new Region(
                rs.getLong("id_region"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getLong("id_country")
            ),
            regionName, countryId
        );
    }

    // ========== ГОРОДА ==========
    
    /**
     * Получить все города, отсортированные по названию
     * @return список всех городов
     */
    public List<City> findAllCities() {
        return jdbcTemplate.query(
            "SELECT * FROM cities ORDER BY city_name",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name"),
                rs.getLong("id_region")
            )
        );
    }

    /**
     * Найти город по уникальному идентификатору
     * @param id идентификатор города
     * @return объект города
     * @throws org.springframework.dao.EmptyResultDataAccessException если город не найден
     */
    public City findCityById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM cities WHERE id_city = ?",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name"),
                rs.getLong("id_region")
            ),
            id
        );
    }

    /**
     * Найти города по региону
     * @param regionId идентификатор региона
     * @return список городов указанного региона, отсортированный по названию
     */
    public List<City> findCitiesByRegion(Long regionId) {
        return jdbcTemplate.query(
            "SELECT * FROM cities WHERE id_region = ? ORDER BY city_name",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name"),
                rs.getLong("id_region")
            ),
            regionId
        );
    }

    /**
     * Найти города по стране (через связь с регионами)
     * @param countryId идентификатор страны
     * @return список городов указанной страны, отсортированный по названию
     */
    public List<City> findCitiesByCountry(Long countryId) {
        return jdbcTemplate.query(
            "SELECT c.* FROM cities c " +
            "INNER JOIN regions r ON c.id_region = r.id_region " +
            "WHERE r.id_country = ? ORDER BY c.city_name",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name"),
                rs.getLong("id_region")
            ),
            countryId
        );
    }

    /**
     * Найти город по названию и региону (для точного поиска)
     * @param cityName название города
     * @param regionId идентификатор региона
     * @return объект города
     * @throws org.springframework.dao.EmptyResultDataAccessException если город не найден
     */
    public City findCityByNameAndRegion(String cityName, Long regionId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM cities WHERE city_name = ? AND id_region = ?",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name"),
                rs.getLong("id_region")
            ),
            cityName, regionId
        );
    }

    /**
     * Поиск городов по частичному совпадению названия (регистронезависимый)
     * @param cityNamePattern паттерн для поиска
     * @return список городов, названия которых содержат указанный паттерн, отсортированный по названию
     */
    public List<City> findCitiesByNamePattern(String cityNamePattern) {
        return jdbcTemplate.query(
            "SELECT * FROM cities WHERE city_name ILIKE ? ORDER BY city_name",
            (rs, _) -> new City(
                rs.getLong("id_city"),
                rs.getString("city_name"),
                rs.getLong("id_region")
            ),
            "%" + cityNamePattern + "%"
        );
    }

    // ========== РАЙОНЫ ==========
    
    /**
     * Получить все районы, отсортированные по названию
     * @return список всех районов
     */
    public List<District> findAllDistricts() {
        return jdbcTemplate.query(
            "SELECT * FROM districts ORDER BY district_name",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name"),
                rs.getLong("id_city")
            )
        );
    }

    /**
     * Найти район по уникальному идентификатору
     * @param id идентификатор района
     * @return объект района
     * @throws org.springframework.dao.EmptyResultDataAccessException если район не найден
     */
    public District findDistrictById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM districts WHERE id_district = ?",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name"),
                rs.getLong("id_city")
            ),
            id
        );
    }

    /**
     * Найти районы по городу
     * @param cityId идентификатор города
     * @return список районов указанного города, отсортированный по названию
     */
    public List<District> findDistrictsByCity(Long cityId) {
        return jdbcTemplate.query(
            "SELECT * FROM districts WHERE id_city = ? ORDER BY district_name",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name"),
                rs.getLong("id_city")
            ),
            cityId
        );
    }

    /**
     * Найти районы по региону (через связь с городами)
     * @param regionId идентификатор региона
     * @return список районов всех городов указанного региона, отсортированный по названию
     */
    public List<District> findDistrictsByRegion(Long regionId) {
        return jdbcTemplate.query(
            "SELECT d.* FROM districts d " +
            "INNER JOIN cities c ON d.id_city = c.id_city " +
            "WHERE c.id_region = ? ORDER BY d.district_name",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name"),
                rs.getLong("id_city")
            ),
            regionId
        );
    }

    /**
     * Найти районы по стране (через связь с городами и регионами)
     * @param countryId идентификатор страны
     * @return список районов всех городов указанной страны, отсортированный по названию
     */
    public List<District> findDistrictsByCountry(Long countryId) {
        return jdbcTemplate.query(
            "SELECT d.* FROM districts d " +
            "INNER JOIN cities c ON d.id_city = c.id_city " +
            "INNER JOIN regions r ON c.id_region = r.id_region " +
            "WHERE r.id_country = ? ORDER BY d.district_name",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name"),
                rs.getLong("id_city")
            ),
            countryId
        );
    }

    /**
     * Найти район по названию и городу (для точного поиска)
     * @param districtName название района
     * @param cityId идентификатор города
     * @return объект района
     * @throws org.springframework.dao.EmptyResultDataAccessException если район не найден
     */
    public District findDistrictByNameAndCity(String districtName, Long cityId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM districts WHERE district_name = ? AND id_city = ?",
            (rs, _) -> new District(
                rs.getLong("id_district"),
                rs.getString("district_name"),
                rs.getLong("id_city")
            ),
            districtName, cityId
        );
    }

    // ========== УЛИЦЫ ==========
    
    /**
     * Получить все улицы, отсортированные по названию
     * @return список всех улиц
     */
    public List<Street> findAllStreets() {
        return jdbcTemplate.query(
            "SELECT * FROM streets ORDER BY street_name",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name"),
                rs.getLong("id_city")
            )
        );
    }

    /**
     * Найти улицу по уникальному идентификатору
     * @param id идентификатор улицы
     * @return объект улицы
     * @throws org.springframework.dao.EmptyResultDataAccessException если улица не найдена
     */
    public Street findStreetById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM streets WHERE id_street = ?",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name"),
                rs.getLong("id_city")
            ),
            id
        );
    }

    /**
     * Найти улицы по городу
     * @param cityId идентификатор города
     * @return список улиц указанного города, отсортированный по названию
     */
    public List<Street> findStreetsByCity(Long cityId) {
        return jdbcTemplate.query(
            "SELECT * FROM streets WHERE id_city = ? ORDER BY street_name",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name"),
                rs.getLong("id_city")
            ),
            cityId
        );
    }

    /**
     * Найти улицу по названию и городу (для точного поиска)
     * @param streetName название улицы
     * @param cityId идентификатор города
     * @return объект улицы
     * @throws org.springframework.dao.EmptyResultDataAccessException если улица не найдена
     */
    public Street findStreetByNameAndCity(String streetName, Long cityId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM streets WHERE street_name = ? AND id_city = ?",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name"),
                rs.getLong("id_city")
            ),
            streetName, cityId
        );
    }

    /**
     * Поиск улиц по частичному совпадению названия (регистронезависимый)
     * @param streetNamePattern паттерн для поиска
     * @return список улиц, названия которых содержат указанный паттерн, отсортированный по названию
     */
    public List<Street> findStreetsByNamePattern(String streetNamePattern) {
        return jdbcTemplate.query(
            "SELECT * FROM streets WHERE street_name ILIKE ? ORDER BY street_name",
            (rs, _) -> new Street(
                rs.getLong("id_street"),
                rs.getString("street_name"),
                rs.getLong("id_city")
            ),
            "%" + streetNamePattern + "%"
        );
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все регионы с детальной информацией (включая страну)
     * @return список всех регионов с информацией о стране
     */
    public List<RegionWithDetailsDto> findAllRegionsWithDetails() {
        String sql = """
            SELECT 
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                c.id_country as country_id,
                c.country_name
            FROM regions r
            JOIN countries c ON r.id_country = c.id_country
            ORDER BY r.name
            """;
        return jdbcTemplate.query(sql, regionWithDetailsRowMapper);
    }

    /**
     * Найти регион с детальной информацией по идентификатору
     * @param id идентификатор региона
     * @return регион с информацией о стране
     * @throws org.springframework.dao.EmptyResultDataAccessException если регион не найден
     */
    public RegionWithDetailsDto findRegionByIdWithDetails(Long id) {
        String sql = """
            SELECT 
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                c.id_country as country_id,
                c.country_name
            FROM regions r
            JOIN countries c ON r.id_country = c.id_country
            WHERE r.id_region = ?
            """;
        return jdbcTemplate.queryForObject(sql, regionWithDetailsRowMapper, id);
    }

    /**
     * Найти регионы по стране с детальной информацией
     * @param countryId идентификатор страны
     * @return список регионов с информацией о стране
     */
    public List<RegionWithDetailsDto> findRegionsByCountryWithDetails(Long countryId) {
        String sql = """
            SELECT 
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                c.id_country as country_id,
                c.country_name
            FROM regions r
            JOIN countries c ON r.id_country = c.id_country
            WHERE r.id_country = ?
            ORDER BY r.name
            """;
        return jdbcTemplate.query(sql, regionWithDetailsRowMapper, countryId);
    }

    /**
     * Получить все города с детальной информацией (включая регион и страну)
     * @return список всех городов с полной географической информацией
     */
    public List<CityWithDetailsDto> findAllCitiesWithDetails() {
        String sql = """
            SELECT 
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                country.id_country as country_id,
                country.country_name
            FROM cities c
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            ORDER BY c.city_name
            """;
        return jdbcTemplate.query(sql, cityWithDetailsRowMapper);
    }

    /**
     * Найти город с детальной информацией по идентификатору
     * @param id идентификатор города
     * @return город с полной географической информацией
     * @throws org.springframework.dao.EmptyResultDataAccessException если город не найден
     */
    public CityWithDetailsDto findCityByIdWithDetails(Long id) {
        String sql = """
            SELECT 
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                country.id_country as country_id,
                country.country_name
            FROM cities c
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE c.id_city = ?
            """;
        return jdbcTemplate.queryForObject(sql, cityWithDetailsRowMapper, id);
    }

    /**
     * Найти города по региону с детальной информацией
     * @param regionId идентификатор региона
     * @return список городов с полной географической информацией
     */
    public List<CityWithDetailsDto> findCitiesByRegionWithDetails(Long regionId) {
        String sql = """
            SELECT 
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                country.id_country as country_id,
                country.country_name
            FROM cities c
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE c.id_region = ?
            ORDER BY c.city_name
            """;
        return jdbcTemplate.query(sql, cityWithDetailsRowMapper, regionId);
    }

    /**
     * Получить все районы с детальной информацией (включая город, регион и страну)
     * @return список всех районов с полной географической информацией
     */
    public List<DistrictWithDetailsDto> findAllDistrictsWithDetails() {
        String sql = """
            SELECT 
                d.id_district as district_id,
                d.district_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM districts d
            JOIN cities c ON d.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            ORDER BY d.district_name
            """;
        return jdbcTemplate.query(sql, districtWithDetailsRowMapper);
    }

    /**
     * Найти район с детальной информацией по идентификатору
     * @param id идентификатор района
     * @return район с полной географической информацией
     * @throws org.springframework.dao.EmptyResultDataAccessException если район не найден
     */
    public DistrictWithDetailsDto findDistrictByIdWithDetails(Long id) {
        String sql = """
            SELECT 
                d.id_district as district_id,
                d.district_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM districts d
            JOIN cities c ON d.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE d.id_district = ?
            """;
        return jdbcTemplate.queryForObject(sql, districtWithDetailsRowMapper, id);
    }

    /**
     * Найти районы по городу с детальной информацией
     * @param cityId идентификатор города
     * @return список районов с полной географической информацией
     */
    public List<DistrictWithDetailsDto> findDistrictsByCityWithDetails(Long cityId) {
        String sql = """
            SELECT 
                d.id_district as district_id,
                d.district_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM districts d
            JOIN cities c ON d.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE d.id_city = ?
            ORDER BY d.district_name
            """;
        return jdbcTemplate.query(sql, districtWithDetailsRowMapper, cityId);
    }

    /**
     * Получить все улицы с детальной информацией (включая город, регион и страну)
     * @return список всех улиц с полной географической информацией
     */
    public List<StreetWithDetailsDto> findAllStreetsWithDetails() {
        String sql = """
            SELECT 
                s.id_street as street_id,
                s.street_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM streets s
            JOIN cities c ON s.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            ORDER BY s.street_name
            """;
        return jdbcTemplate.query(sql, streetWithDetailsRowMapper);
    }

    /**
     * Найти улицу с детальной информацией по идентификатору
     * @param id идентификатор улицы
     * @return улица с полной географической информацией
     * @throws org.springframework.dao.EmptyResultDataAccessException если улица не найдена
     */
    public StreetWithDetailsDto findStreetByIdWithDetails(Long id) {
        String sql = """
            SELECT 
                s.id_street as street_id,
                s.street_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM streets s
            JOIN cities c ON s.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE s.id_street = ?
            """;
        return jdbcTemplate.queryForObject(sql, streetWithDetailsRowMapper, id);
    }

    /**
     * Найти улицы по городу с детальной информацией
     * @param cityId идентификатор города
     * @return список улиц с полной географической информацией
     */
    public List<StreetWithDetailsDto> findStreetsByCityWithDetails(Long cityId) {
        String sql = """
            SELECT 
                s.id_street as street_id,
                s.street_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM streets s
            JOIN cities c ON s.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE s.id_city = ?
            ORDER BY s.street_name
            """;
        return jdbcTemplate.query(sql, streetWithDetailsRowMapper, cityId);
    }
    
    /**
     * Поиск регионов с детальной информацией по нескольким критериям
     *
     * @param regionNamePattern паттерн названия региона (может быть null)
     * @param regionCode код региона (может быть null)
     * @param countryId идентификатор страны (может быть null)
     * @return список регионов, соответствующих критериям поиска
     */
    public List<RegionWithDetailsDto> searchRegionsWithDetails(String regionNamePattern, String regionCode, Long countryId) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                c.id_country as country_id,
                c.country_name
            FROM regions r
            JOIN countries c ON r.id_country = c.id_country
            WHERE 1=1
            """);
            
        // Добавляем условия поиска в зависимости от переданных параметров
        if (regionNamePattern != null && !regionNamePattern.isEmpty()) {
            sql.append(" AND r.name ILIKE ?");
        }
        
        if (regionCode != null && !regionCode.isEmpty()) {
            sql.append(" AND r.code ILIKE ?");
        }
        
        if (countryId != null) {
            sql.append(" AND r.id_country = ?");
        }
        
        sql.append(" ORDER BY r.name");
        
        // Подготавливаем параметры для запроса
        List<Object> params = new ArrayList<>();
        if (regionNamePattern != null && !regionNamePattern.isEmpty()) {
            params.add("%" + regionNamePattern + "%");
        }
        
        if (regionCode != null && !regionCode.isEmpty()) {
            params.add("%" + regionCode + "%");
        }
        
        if (countryId != null) {
            params.add(countryId);
        }
        
        return jdbcTemplate.query(sql.toString(), regionWithDetailsRowMapper, params.toArray());
    }
    
    /**
     * Поиск городов с детальной информацией по нескольким критериям
     *
     * @param cityNamePattern паттерн названия города (может быть null)
     * @param regionId идентификатор региона (может быть null)
     * @param countryId идентификатор страны (может быть null)
     * @return список городов, соответствующих критериям поиска
     */
    public List<CityWithDetailsDto> searchCitiesWithDetails(String cityNamePattern, Long regionId, Long countryId) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                r.code as region_code,
                country.id_country as country_id,
                country.country_name
            FROM cities c
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE 1=1
            """);
            
        // Добавляем условия поиска в зависимости от переданных параметров
        if (cityNamePattern != null && !cityNamePattern.isEmpty()) {
            sql.append(" AND c.city_name ILIKE ?");
        }
        
        if (regionId != null) {
            sql.append(" AND r.id_region = ?");
        }
        
        if (countryId != null) {
            sql.append(" AND country.id_country = ?");
        }
        
        sql.append(" ORDER BY c.city_name");
        
        // Подготавливаем параметры для запроса
        List<Object> params = new ArrayList<>();
        if (cityNamePattern != null && !cityNamePattern.isEmpty()) {
            params.add("%" + cityNamePattern + "%");
        }
        
        if (regionId != null) {
            params.add(regionId);
        }
        
        if (countryId != null) {
            params.add(countryId);
        }
        
        return jdbcTemplate.query(sql.toString(), cityWithDetailsRowMapper, params.toArray());
    }
    
    /**
     * Поиск районов с детальной информацией по нескольким критериям
     *
     * @param districtNamePattern паттерн названия района (может быть null)
     * @param cityId идентификатор города (может быть null)
     * @param regionId идентификатор региона (может быть null)
     * @return список районов, соответствующих критериям поиска
     */
    public List<DistrictWithDetailsDto> searchDistrictsWithDetails(String districtNamePattern, Long cityId, Long regionId) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                d.id_district as district_id,
                d.district_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM districts d
            JOIN cities c ON d.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE 1=1
            """);
            
        // Добавляем условия поиска в зависимости от переданных параметров
        if (districtNamePattern != null && !districtNamePattern.isEmpty()) {
            sql.append(" AND d.district_name ILIKE ?");
        }
        
        if (cityId != null) {
            sql.append(" AND c.id_city = ?");
        }
        
        if (regionId != null) {
            sql.append(" AND r.id_region = ?");
        }
        
        sql.append(" ORDER BY d.district_name");
        
        // Подготавливаем параметры для запроса
        List<Object> params = new ArrayList<>();
        if (districtNamePattern != null && !districtNamePattern.isEmpty()) {
            params.add("%" + districtNamePattern + "%");
        }
        
        if (cityId != null) {
            params.add(cityId);
        }
        
        if (regionId != null) {
            params.add(regionId);
        }
        
        return jdbcTemplate.query(sql.toString(), districtWithDetailsRowMapper, params.toArray());
    }
    
    /**
     * Поиск улиц с детальной информацией по нескольким критериям
     *
     * @param streetNamePattern паттерн названия улицы (может быть null)
     * @param cityId идентификатор города (может быть null)
     * @param regionId идентификатор региона (может быть null)
     * @return список улиц, соответствующих критериям поиска
     */
    public List<StreetWithDetailsDto> searchStreetsWithDetails(String streetNamePattern, Long cityId, Long regionId) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                s.id_street as street_id,
                s.street_name,
                c.id_city as city_id,
                c.city_name,
                r.id_region as region_id,
                r.name as region_name,
                country.id_country as country_id,
                country.country_name
            FROM streets s
            JOIN cities c ON s.id_city = c.id_city
            JOIN regions r ON c.id_region = r.id_region
            JOIN countries country ON r.id_country = country.id_country
            WHERE 1=1
            """);
            
        // Добавляем условия поиска в зависимости от переданных параметров
        if (streetNamePattern != null && !streetNamePattern.isEmpty()) {
            sql.append(" AND s.street_name ILIKE ?");
        }
        
        if (cityId != null) {
            sql.append(" AND c.id_city = ?");
        }
        
        if (regionId != null) {
            sql.append(" AND r.id_region = ?");
        }
        
        sql.append(" ORDER BY s.street_name");
        
        // Подготавливаем параметры для запроса
        List<Object> params = new ArrayList<>();
        if (streetNamePattern != null && !streetNamePattern.isEmpty()) {
            params.add("%" + streetNamePattern + "%");
        }
        
        if (cityId != null) {
            params.add(cityId);
        }
        
        if (regionId != null) {
            params.add(regionId);
        }
        
        return jdbcTemplate.query(sql.toString(), streetWithDetailsRowMapper, params.toArray());
    }
    
}