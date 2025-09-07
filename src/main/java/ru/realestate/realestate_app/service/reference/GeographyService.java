package ru.realestate.realestate_app.service.reference;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.reference.GeographyDao;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.geography.*;
import ru.realestate.realestate_app.model.dto.RegionWithDetailsDto;
import ru.realestate.realestate_app.model.dto.CityWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DistrictWithDetailsDto;
import ru.realestate.realestate_app.model.dto.StreetWithDetailsDto;

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
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Country> findAllCountries() {
        try {
            return geographyDao.findAllCountries();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Country", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех стран");
            throw re;
        }
    }

    /**
     * Найти страну по уникальному идентификатору
     * @param id идентификатор страны
     * @return объект страны
     * @throws EntityNotFoundException если страна не найдена
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Country findCountryById(Long id) {
        try {
            return geographyDao.findCountryById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Country", id);
            ExceptionHandler.logException(re, "Ошибка при поиске страны по id: " + id);
            throw re;
        }
    }

    /**
     * Найти страну по названию (точное совпадение)
     * @param name название страны
     * @return объект страны
     * @throws EntityNotFoundException если страна не найдена
     * @throws ValidationException если название не указано
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Country findCountryByName(String name) {
        try {
            return geographyDao.findCountryByName(name);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Country", null);
            ExceptionHandler.logException(re, "Ошибка при поиске страны по названию: " + name);
            throw re;
        }
    }

    // ========== РЕГИОНЫ ==========

    /**
     * Получить все регионы, отсортированные по названию
     * @return список всех регионов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Region> findAllRegions() {
        try {
            return geographyDao.findAllRegions();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех регионов");
            throw re;
        }
    }

    /**
     * Найти регион по уникальному идентификатору
     * @param id идентификатор региона
     * @return объект региона
     * @throws EntityNotFoundException если регион не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Region findRegionById(Long id) {
        try {
            return geographyDao.findRegionById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", id);
            ExceptionHandler.logException(re, "Ошибка при поиске региона по id: " + id);
            throw re;
        }
    }

    /**
     * Найти регионы по коду (может быть несколько регионов с одинаковым кодом в разных странах)
     * @param code код региона
     * @return список регионов с указанным кодом, отсортированный по названию
     * @throws ValidationException если код не указан
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Region> findRegionByCode(String code) {
        try {
            return geographyDao.findRegionByCode(code);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", null);
            ExceptionHandler.logException(re, "Ошибка при поиске регионов по коду: " + code);
            throw re;
        }
    }

    /**
     * Найти регионы по стране
     * @param countryId идентификатор страны
     * @return список регионов указанной страны, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Region> findRegionsByCountry(Long countryId) {
        try {
            return geographyDao.findRegionsByCountry(countryId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", null);
            ExceptionHandler.logException(re, "Ошибка при поиске регионов по стране с id: " + countryId);
            throw re;
        }
    }

    /**
     * Найти регион по названию и стране (для точного поиска)
     * @param regionName название региона
     * @param countryId идентификатор страны
     * @return объект региона
     * @throws EntityNotFoundException если регион не найден
     * @throws ValidationException если параметры не указаны
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Region findRegionByNameAndCountry(String regionName, Long countryId) {
        try {
            return geographyDao.findRegionByNameAndCountry(regionName, countryId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", null);
            ExceptionHandler.logException(re, "Ошибка при поиске региона по названию и стране: " + regionName + ", countryId: " + countryId);
            throw re;
        }
    }

    // ========== ГОРОДА ==========

    /**
     * Получить все города, отсортированные по названию
     * @return список всех городов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<City> findAllCities() {
        try {
            return geographyDao.findAllCities();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех городов");
            throw re;
        }
    }

    /**
     * Найти город по уникальному идентификатору
     * @param id идентификатор города
     * @return объект города
     * @throws EntityNotFoundException если город не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public City findCityById(Long id) {
        try {
            return geographyDao.findCityById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", id);
            ExceptionHandler.logException(re, "Ошибка при поиске города по id: " + id);
            throw re;
        }
    }

    /**
     * Найти города по региону
     * @param regionId идентификатор региона
     * @return список городов указанного региона, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<City> findCitiesByRegion(Long regionId) {
        try {
            return geographyDao.findCitiesByRegion(regionId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при поиске городов по региону с id: " + regionId);
            throw re;
        }
    }

    /**
     * Найти города по стране (через связь с регионами)
     * @param countryId идентификатор страны
     * @return список городов указанной страны, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<City> findCitiesByCountry(Long countryId) {
        try {
            return geographyDao.findCitiesByCountry(countryId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при поиске городов по стране с id: " + countryId);
            throw re;
        }
    }

    /**
     * Найти город по названию и региону (для точного поиска)
     * @param cityName название города
     * @param regionId идентификатор региона
     * @return объект города
     * @throws EntityNotFoundException если город не найден
     * @throws ValidationException если параметры не указаны
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public City findCityByNameAndRegion(String cityName, Long regionId) {
        try {
            return geographyDao.findCityByNameAndRegion(cityName, regionId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при поиске города по названию и региону: " + cityName + ", regionId: " + regionId);
            throw re;
        }
    }

    /**
     * Поиск городов по частичному совпадению названия (регистронезависимый)
     * @param cityNamePattern паттерн для поиска
     * @return список городов, названия которых содержат указанный паттерн, отсортированный по названию
     * @throws ValidationException если паттерн не указан
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<City> findCitiesByNamePattern(String cityNamePattern) {
        try {
            return geographyDao.findCitiesByNamePattern(cityNamePattern);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при поиске городов по паттерну названия: " + cityNamePattern);
            throw re;
        }
    }

    // ========== РАЙОНЫ ==========

    /**
     * Получить все районы, отсортированные по названию
     * @return список всех районов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<District> findAllDistricts() {
        try {
            return geographyDao.findAllDistricts();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех районов");
            throw re;
        }
    }

    /**
     * Найти район по уникальному идентификатору
     * @param id идентификатор района
     * @return объект района
     * @throws EntityNotFoundException если район не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public District findDistrictById(Long id) {
        try {
            return geographyDao.findDistrictById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", id);
            ExceptionHandler.logException(re, "Ошибка при поиске района по id: " + id);
            throw re;
        }
    }

    /**
     * Найти районы по городу
     * @param cityId идентификатор города
     * @return список районов указанного города, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<District> findDistrictsByCity(Long cityId) {
        try {
            return geographyDao.findDistrictsByCity(cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при поиске районов по городу с id: " + cityId);
            throw re;
        }
    }

    /**
     * Найти районы по региону (через связь с городами)
     * @param regionId идентификатор региона
     * @return список районов всех городов указанного региона, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<District> findDistrictsByRegion(Long regionId) {
        try {
            return geographyDao.findDistrictsByRegion(regionId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при поиске районов по региону с id: " + regionId);
            throw re;
        }
    }

    /**
     * Найти районы по стране (через связь с городами и регионами)
     * @param countryId идентификатор страны
     * @return список районов всех городов указанной страны, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<District> findDistrictsByCountry(Long countryId) {
        try {
            return geographyDao.findDistrictsByCountry(countryId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при поиске районов по стране с id: " + countryId);
            throw re;
        }
    }

    /**
     * Найти район по названию и городу (для точного поиска)
     * @param districtName название района
     * @param cityId идентификатор города
     * @return объект района
     * @throws EntityNotFoundException если район не найден
     * @throws ValidationException если параметры не указаны
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public District findDistrictByNameAndCity(String districtName, Long cityId) {
        try {
            return geographyDao.findDistrictByNameAndCity(districtName, cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при поиске района по названию и городу: " + districtName + ", cityId: " + cityId);
            throw re;
        }
    }

    // ========== УЛИЦЫ ==========

    /**
     * Получить все улицы, отсортированные по названию
     * @return список всех улиц
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Street> findAllStreets() {
        try {
            return geographyDao.findAllStreets();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех улиц");
            throw re;
        }
    }

    /**
     * Найти улицу по уникальному идентификатору
     * @param id идентификатор улицы
     * @return объект улицы
     * @throws EntityNotFoundException если улица не найдена
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Street findStreetById(Long id) {
        try {
            return geographyDao.findStreetById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", id);
            ExceptionHandler.logException(re, "Ошибка при поиске улицы по id: " + id);
            throw re;
        }
    }

    /**
     * Найти улицы по городу
     * @param cityId идентификатор города
     * @return список улиц указанного города, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Street> findStreetsByCity(Long cityId) {
        try {
            return geographyDao.findStreetsByCity(cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", null);
            ExceptionHandler.logException(re, "Ошибка при поиске улиц по городу с id: " + cityId);
            throw re;
        }
    }

    /**
     * Найти улицу по названию и городу (для точного поиска)
     * @param streetName название улицы
     * @param cityId идентификатор города
     * @return объект улицы
     * @throws EntityNotFoundException если улица не найдена
     * @throws ValidationException если параметры не указаны
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Street findStreetByNameAndCity(String streetName, Long cityId) {
        try {
            return geographyDao.findStreetByNameAndCity(streetName, cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", null);
            ExceptionHandler.logException(re, "Ошибка при поиске улицы по названию и городу: " + streetName + ", cityId: " + cityId);
            throw re;
        }
    }

    /**
     * Поиск улиц по частичному совпадению названия (регистронезависимый)
     * @param streetNamePattern паттерн для поиска
     * @return список улиц, названия которых содержат указанный паттерн, отсортированный по названию
     * @throws ValidationException если паттерн не указан
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Street> findStreetsByNamePattern(String streetNamePattern) {
        try {
            return geographyDao.findStreetsByNamePattern(streetNamePattern);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", null);
            ExceptionHandler.logException(re, "Ошибка при поиске улиц по шаблону названия: " + streetNamePattern);
            throw re;
        }
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С DTO ==========

    // ========== РЕГИОНЫ С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все регионы с детальной информацией (включая данные о стране)
     * Использует JOIN запросы для оптимизации производительности
     * @return список всех регионов с полной информацией, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<RegionWithDetailsDto> findAllRegionsWithDetails() {
        try {
            return geographyDao.findAllRegionsWithDetails();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех регионов с детальной информацией");
            throw re;
        }
    }

    /**
     * Найти регион по идентификатору с детальной информацией
     * Включает полную информацию о стране
     * @param id идентификатор региона
     * @return объект региона с детальной информацией
     * @throws EntityNotFoundException если регион не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public RegionWithDetailsDto findRegionByIdWithDetails(Long id) {
        try {
            return geographyDao.findRegionByIdWithDetails(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", id);
            ExceptionHandler.logException(re, "Ошибка при поиске региона с детальной информацией по id: " + id);
            throw re;
        }
    }

    /**
     * Найти регионы по стране с детальной информацией
     * @param countryId идентификатор страны
     * @return список регионов с полной информацией указанной страны, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<RegionWithDetailsDto> findRegionsByCountryWithDetails(Long countryId) {
        try {
            return geographyDao.findRegionsByCountryWithDetails(countryId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Region", null);
            ExceptionHandler.logException(re, "Ошибка при поиске регионов с детальной информацией по стране с id: " + countryId);
            throw re;
        }
    }

    // ========== ГОРОДА С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все города с детальной информацией (включая регион и страну)
     * Использует JOIN запросы для оптимизации производительности
     * @return список всех городов с полной географической иерархией, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<CityWithDetailsDto> findAllCitiesWithDetails() {
        try {
            return geographyDao.findAllCitiesWithDetails();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех городов с детальной информацией");
            throw re;
        }
    }

    /**
     * Найти город по идентификатору с детальной информацией
     * Включает полную географическую иерархию (регион и страна)
     * @param id идентификатор города
     * @return объект города с детальной информацией
     * @throws EntityNotFoundException если город не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public CityWithDetailsDto findCityByIdWithDetails(Long id) {
        try {
            return geographyDao.findCityByIdWithDetails(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", id);
            ExceptionHandler.logException(re, "Ошибка при поиске города с детальной информацией по id: " + id);
            throw re;
        }
    }

    /**
     * Найти города по региону с детальной информацией
     * @param regionId идентификатор региона
     * @return список городов с полной информацией указанного региона, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<CityWithDetailsDto> findCitiesByRegionWithDetails(Long regionId) {
        try {
            return geographyDao.findCitiesByRegionWithDetails(regionId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "City", null);
            ExceptionHandler.logException(re, "Ошибка при поиске городов с детальной информацией по региону с id: " + regionId);
            throw re;
        }
    }

    // ========== РАЙОНЫ С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все районы с детальной информацией (включая город, регион и страну)
     * Использует JOIN запросы для оптимизации производительности
     * @return список всех районов с полной географической иерархией, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DistrictWithDetailsDto> findAllDistrictsWithDetails() {
        try {
            return geographyDao.findAllDistrictsWithDetails();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех районов с детальной информацией");
            throw re;
        }
    }

    /**
     * Найти район по идентификатору с детальной информацией
     * Включает полную географическую иерархию (город, регион и страна)
     * @param id идентификатор района
     * @return объект района с детальной информацией
     * @throws EntityNotFoundException если район не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public DistrictWithDetailsDto findDistrictByIdWithDetails(Long id) {
        try {
            return geographyDao.findDistrictByIdWithDetails(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", id);
            ExceptionHandler.logException(re, "Ошибка при поиске района с детальной информацией по id: " + id);
            throw re;
        }
    }

    /**
     * Найти районы по городу с детальной информацией
     * @param cityId идентификатор города
     * @return список районов с полной информацией указанного города, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DistrictWithDetailsDto> findDistrictsByCityWithDetails(Long cityId) {
        try {
            return geographyDao.findDistrictsByCityWithDetails(cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "District", null);
            ExceptionHandler.logException(re, "Ошибка при поиске районов с детальной информацией по городу с id: " + cityId);
            throw re;
        }
    }

    // ========== УЛИЦЫ С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все улицы с детальной информацией (включая город, регион и страну)
     * Использует JOIN запросы для оптимизации производительности
     * @return список всех улиц с полной географической иерархией, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<StreetWithDetailsDto> findAllStreetsWithDetails() {
        try {
            return geographyDao.findAllStreetsWithDetails();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех улиц с детальной информацией");
            throw re;
        }
    }

    /**
     * Найти улицу по идентификатору с детальной информацией
     * Включает полную географическую иерархию (город, регион и страна)
     * @param id идентификатор улицы
     * @return объект улицы с детальной информацией
     * @throws EntityNotFoundException если улица не найдена
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public StreetWithDetailsDto findStreetByIdWithDetails(Long id) {
        try {
            return geographyDao.findStreetByIdWithDetails(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", id);
            ExceptionHandler.logException(re, "Ошибка при поиске улицы с детальной информацией по id: " + id);
            throw re;
        }
    }

    /**
     * Найти улицы по городу с детальной информацией
     * @param cityId идентификатор города
     * @return список улиц с полной информацией указанного города, отсортированный по названию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<StreetWithDetailsDto> findStreetsByCityWithDetails(Long cityId) {
        try {
            return geographyDao.findStreetsByCityWithDetails(cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Street", null);
            ExceptionHandler.logException(re, "Ошибка при поиске улиц с детальной информацией по городу с id: " + cityId);
            throw re;
        }
    }
} 