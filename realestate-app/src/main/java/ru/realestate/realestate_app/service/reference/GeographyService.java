package ru.realestate.realestate_app.service.reference;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.reference.GeographyDao;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
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
            ExceptionHandler.logException(re, "Ошибка при поиске улиц по паттерну названия: " + streetNamePattern);
            throw re;
        }
    }
} 