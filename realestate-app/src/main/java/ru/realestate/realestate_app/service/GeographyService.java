package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.GeographyDao;
import ru.realestate.realestate_app.model.geography.*;

import java.util.List;

/**
 * Сервис для работы с географическими справочниками
 * Содержит бизнес-логику для операций со странами, регионами, городами, районами и улицами
 * Делегирует выполнение операций с базой данных в GeographyDao
 */
@Service
public class GeographyService {
    
    private final GeographyDao geographyDao;

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param geographyDao DAO для работы с географическими данными
     */
    public GeographyService(GeographyDao geographyDao) {
        this.geographyDao = geographyDao;
    }

    // ========== СТРАНЫ ==========

    /**
     * Получить все страны, отсортированные по названию
     * @return список всех стран
     */
    public List<Country> findAllCountries() {
        return geographyDao.findAllCountries();
    }

    /**
     * Найти страну по уникальному идентификатору
     * @param id идентификатор страны
     * @return объект страны
     * @throws org.springframework.dao.EmptyResultDataAccessException если страна не найдена
     */
    public Country findCountryById(Long id) {
        return geographyDao.findCountryById(id);
    }

    /**
     * Найти страну по названию (точное совпадение)
     * @param name название страны
     * @return объект страны
     * @throws org.springframework.dao.EmptyResultDataAccessException если страна не найдена
     */
    public Country findCountryByName(String name) {
        return geographyDao.findCountryByName(name);
    }

    // ========== РЕГИОНЫ ==========

    /**
     * Получить все регионы, отсортированные по названию
     * @return список всех регионов
     */
    public List<Region> findAllRegions() {
        return geographyDao.findAllRegions();
    }

    /**
     * Найти регион по уникальному идентификатору
     * @param id идентификатор региона
     * @return объект региона
     * @throws org.springframework.dao.EmptyResultDataAccessException если регион не найден
     */
    public Region findRegionById(Long id) {
        return geographyDao.findRegionById(id);
    }

    /**
     * Найти регионы по коду (может быть несколько регионов с одинаковым кодом в разных странах)
     * @param code код региона
     * @return список регионов с указанным кодом, отсортированный по названию
     */
    public List<Region> findRegionByCode(String code) {
        return geographyDao.findRegionByCode(code);
    }

    /**
     * Найти регионы по стране
     * @param countryId идентификатор страны
     * @return список регионов указанной страны, отсортированный по названию
     */
    public List<Region> findRegionsByCountry(Long countryId) {
        return geographyDao.findRegionsByCountry(countryId);
    }

    /**
     * Найти регион по названию и стране (для точного поиска)
     * @param regionName название региона
     * @param countryId идентификатор страны
     * @return объект региона
     * @throws org.springframework.dao.EmptyResultDataAccessException если регион не найден
     */
    public Region findRegionByNameAndCountry(String regionName, Long countryId) {
        return geographyDao.findRegionByNameAndCountry(regionName, countryId);
    }

    // ========== ГОРОДА ==========

    /**
     * Получить все города, отсортированные по названию
     * @return список всех городов
     */
    public List<City> findAllCities() {
        return geographyDao.findAllCities();
    }

    /**
     * Найти город по уникальному идентификатору
     * @param id идентификатор города
     * @return объект города
     * @throws org.springframework.dao.EmptyResultDataAccessException если город не найден
     */
    public City findCityById(Long id) {
        return geographyDao.findCityById(id);
    }

    /**
     * Найти города по региону
     * @param regionId идентификатор региона
     * @return список городов указанного региона, отсортированный по названию
     */
    public List<City> findCitiesByRegion(Long regionId) {
        return geographyDao.findCitiesByRegion(regionId);
    }

    /**
     * Найти города по стране (через связь с регионами)
     * @param countryId идентификатор страны
     * @return список городов указанной страны, отсортированный по названию
     */
    public List<City> findCitiesByCountry(Long countryId) {
        return geographyDao.findCitiesByCountry(countryId);
    }

    /**
     * Найти город по названию и региону (для точного поиска)
     * @param cityName название города
     * @param regionId идентификатор региона
     * @return объект города
     * @throws org.springframework.dao.EmptyResultDataAccessException если город не найден
     */
    public City findCityByNameAndRegion(String cityName, Long regionId) {
        return geographyDao.findCityByNameAndRegion(cityName, regionId);
    }

    /**
     * Поиск городов по частичному совпадению названия (регистронезависимый)
     * @param cityNamePattern паттерн для поиска
     * @return список городов, названия которых содержат указанный паттерн, отсортированный по названию
     */
    public List<City> findCitiesByNamePattern(String cityNamePattern) {
        return geographyDao.findCitiesByNamePattern(cityNamePattern);
    }

    // ========== РАЙОНЫ ==========

    /**
     * Получить все районы, отсортированные по названию
     * @return список всех районов
     */
    public List<District> findAllDistricts() {
        return geographyDao.findAllDistricts();
    }

    /**
     * Найти район по уникальному идентификатору
     * @param id идентификатор района
     * @return объект района
     * @throws org.springframework.dao.EmptyResultDataAccessException если район не найден
     */
    public District findDistrictById(Long id) {
        return geographyDao.findDistrictById(id);
    }

    /**
     * Найти районы по городу
     * @param cityId идентификатор города
     * @return список районов указанного города, отсортированный по названию
     */
    public List<District> findDistrictsByCity(Long cityId) {
        return geographyDao.findDistrictsByCity(cityId);
    }

    /**
     * Найти районы по региону (через связь с городами)
     * @param regionId идентификатор региона
     * @return список районов всех городов указанного региона, отсортированный по названию
     */
    public List<District> findDistrictsByRegion(Long regionId) {
        return geographyDao.findDistrictsByRegion(regionId);
    }

    /**
     * Найти районы по стране (через связь с городами и регионами)
     * @param countryId идентификатор страны
     * @return список районов всех городов указанной страны, отсортированный по названию
     */
    public List<District> findDistrictsByCountry(Long countryId) {
        return geographyDao.findDistrictsByCountry(countryId);
    }

    /**
     * Найти район по названию и городу (для точного поиска)
     * @param districtName название района
     * @param cityId идентификатор города
     * @return объект района
     * @throws org.springframework.dao.EmptyResultDataAccessException если район не найден
     */
    public District findDistrictByNameAndCity(String districtName, Long cityId) {
        return geographyDao.findDistrictByNameAndCity(districtName, cityId);
    }

    // ========== УЛИЦЫ ==========

    /**
     * Получить все улицы, отсортированные по названию
     * @return список всех улиц
     */
    public List<Street> findAllStreets() {
        return geographyDao.findAllStreets();
    }

    /**
     * Найти улицу по уникальному идентификатору
     * @param id идентификатор улицы
     * @return объект улицы
     * @throws org.springframework.dao.EmptyResultDataAccessException если улица не найдена
     */
    public Street findStreetById(Long id) {
        return geographyDao.findStreetById(id);
    }

    /**
     * Найти улицы по городу
     * @param cityId идентификатор города
     * @return список улиц указанного города, отсортированный по названию
     */
    public List<Street> findStreetsByCity(Long cityId) {
        return geographyDao.findStreetsByCity(cityId);
    }

    /**
     * Найти улицу по названию и городу (для точного поиска)
     * @param streetName название улицы
     * @param cityId идентификатор города
     * @return объект улицы
     * @throws org.springframework.dao.EmptyResultDataAccessException если улица не найдена
     */
    public Street findStreetByNameAndCity(String streetName, Long cityId) {
        return geographyDao.findStreetByNameAndCity(streetName, cityId);
    }

    /**
     * Поиск улиц по частичному совпадению названия (регистронезависимый)
     * @param streetNamePattern паттерн для поиска
     * @return список улиц, названия которых содержат указанный паттерн, отсортированный по названию
     */
    public List<Street> findStreetsByNamePattern(String streetNamePattern) {
        return geographyDao.findStreetsByNamePattern(streetNamePattern);
    }
} 