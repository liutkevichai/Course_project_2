package ru.realestate.realestate_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.geography.*;
import ru.realestate.realestate_app.model.dto.RegionWithDetailsDto;
import ru.realestate.realestate_app.model.dto.CityWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DistrictWithDetailsDto;
import ru.realestate.realestate_app.model.dto.StreetWithDetailsDto;
import ru.realestate.realestate_app.service.reference.GeographyService;

import java.util.List;

/**
 * REST контроллер для работы с географическими справочниками
 * 
 * Контроллер предоставляет HTTP API для работы с географической иерархией:
 * - Страны (Country)
 * - Регионы/Области (Region) 
 * - Города (City)
 * - Районы (District)
 * - Улицы (Street)
 * 
 */
@RestController
@RequestMapping("/api/geography")
public class GeographyController {

    private final GeographyService geographyService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param geographyService сервис для работы с географическими данными
     */
    public GeographyController(GeographyService geographyService) {
        this.geographyService = geographyService;
    }

    // ========== СТРАНЫ ==========

    /**
     * Получить список всех стран
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries
     * 
     * Возвращает все страны, отсортированные по названию в алфавитном порядке
     * 
     * @return ResponseEntity со списком всех стран и HTTP статусом 200 (OK)
     */
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = geographyService.findAllCountries();
        return ResponseEntity.ok(countries);
    }

    /**
     * Получить страну по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries/{id} (например: /api/geography/countries/1)
     * 
     * @param id идентификатор страны
     * @return ResponseEntity с данными страны и HTTP статусом 200 (OK)
     */
    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        Country country = geographyService.findCountryById(id);
        return ResponseEntity.ok(country);
    }

    /**
     * Найти страну по названию
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries/search/by-name?name=Россия
     * 
     * @param name название страны для поиска
     * @return ResponseEntity с найденной страной
     */
    @GetMapping("/countries/search/by-name")
    public ResponseEntity<Country> getCountryByName(@RequestParam String name) {
        Country country = geographyService.findCountryByName(name);
        return ResponseEntity.ok(country);
    }

    // ========== РЕГИОНЫ ==========

    /**
     * Получить список всех регионов
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions
     * 
     * Возвращает все регионы всех стран, отсортированные по названию
     * 
     * @return ResponseEntity со списком всех регионов и HTTP статусом 200 (OK)
     */
    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> regions = geographyService.findAllRegions();
        return ResponseEntity.ok(regions);
    }

    /**
     * Получить регион по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/{id} (например: /api/geography/regions/1)
     * 
     * @param id идентификатор региона
     * @return ResponseEntity с данными региона и HTTP статусом 200 (OK)
     */
    @GetMapping("/regions/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        Region region = geographyService.findRegionById(id);
        return ResponseEntity.ok(region);
    }

    /**
     * Найти регионы по коду
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/search/by-code?code=77
     * 
     * Может вернуть несколько регионов, так как коды могут повторяться в разных странах
     * 
     * @param code код региона для поиска
     * @return ResponseEntity со списком найденных регионов
     */
    @GetMapping("/regions/search/by-code")
    public ResponseEntity<List<Region>> getRegionsByCode(@RequestParam String code) {
        List<Region> regions = geographyService.findRegionByCode(code);
        return ResponseEntity.ok(regions);
    }

    /**
     * Получить регионы конкретной страны
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries/{countryId}/regions
     * (например: /api/geography/countries/1/regions)
     * 
     * @param countryId идентификатор страны
     * @return ResponseEntity со списком регионов указанной страны
     */
    @GetMapping("/countries/{countryId}/regions")
    public ResponseEntity<List<Region>> getRegionsByCountry(@PathVariable Long countryId) {
        List<Region> regions = geographyService.findRegionsByCountry(countryId);
        return ResponseEntity.ok(regions);
    }

    /**
     * Найти регион по названию и стране (для точного поиска)
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/search/by-name-and-country?regionName=Москва&countryId=1
     * 
     * Используется для точного поиска региона, когда важно избежать дублирования
     * 
     * @param regionName название региона
     * @param countryId идентификатор страны
     * @return ResponseEntity с найденным регионом
     */
    @GetMapping("/regions/search/by-name-and-country")
    public ResponseEntity<Region> getRegionByNameAndCountry(
            @RequestParam String regionName, 
            @RequestParam Long countryId) {
        
        Region region = geographyService.findRegionByNameAndCountry(regionName, countryId);
        return ResponseEntity.ok(region);
    }

    // ========== ГОРОДЫ ==========

    /**
     * Получить список всех городов
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities
     * 
     * Возвращает все города всех регионов, отсортированные по названию.
     * 
     * @return ResponseEntity со списком всех городов и HTTP статусом 200 (OK)
     */
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = geographyService.findAllCities();
        return ResponseEntity.ok(cities);
    }

    /**
     * Получить город по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/{id} (например: /api/geography/cities/1)
     * 
     * @param id идентификатор города
     * @return ResponseEntity с данными города и HTTP статусом 200 (OK)
     */
    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        City city = geographyService.findCityById(id);
        return ResponseEntity.ok(city);
    }

    /**
     * Получить города конкретного региона
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/{regionId}/cities
     * (например: /api/geography/regions/1/cities)
     * 
     * @param regionId идентификатор региона
     * @return ResponseEntity со списком городов указанного региона
     */
    @GetMapping("/regions/{regionId}/cities")
    public ResponseEntity<List<City>> getCitiesByRegion(@PathVariable Long regionId) {
        List<City> cities = geographyService.findCitiesByRegion(regionId);
        return ResponseEntity.ok(cities);
    }

    /**
     * Получить города конкретной страны
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries/{countryId}/cities
     * (например: /api/geography/countries/1/cities)
     * 
     * @param countryId идентификатор страны
     * @return ResponseEntity со списком городов указанной страны
     */
    @GetMapping("/countries/{countryId}/cities")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable Long countryId) {
        List<City> cities = geographyService.findCitiesByCountry(countryId);
        return ResponseEntity.ok(cities);
    }

    /**
     * Найти город по названию и региону (для точного поиска)
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/search/by-name-and-region?cityName=Москва&regionId=1
     * 
     * @param cityName название города
     * @param regionId идентификатор региона
     * @return ResponseEntity с найденным городом
     */
    @GetMapping("/cities/search/by-name-and-region")
    public ResponseEntity<City> getCityByNameAndRegion(
            @RequestParam String cityName, 
            @RequestParam Long regionId) {
        
        City city = geographyService.findCityByNameAndRegion(cityName, regionId);
        return ResponseEntity.ok(city);
    }

    /**
     * Найти города по шаблону названия
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/search/by-name-pattern?pattern=Мос
     * 
     * Поиск выполняется по частичному совпадению (LIKE %pattern%)
     * 
     * @param pattern шаблон названия города для поиска
     * @return ResponseEntity со списком найденных городов
     */
    @GetMapping("/cities/search/by-name-pattern")
    public ResponseEntity<List<City>> getCitiesByNamePattern(@RequestParam String pattern) {
        List<City> cities = geographyService.findCitiesByNamePattern(pattern);
        return ResponseEntity.ok(cities);
    }

    // ========== РАЙОНЫ ==========

    /**
     * Получить список всех районов
     * 
     * HTTP метод: GET
     * URL: /api/geography/districts
     * 
     * @return ResponseEntity со списком всех районов и HTTP статусом 200 (OK)
     */
    @GetMapping("/districts")
    public ResponseEntity<List<District>> getAllDistricts() {
        List<District> districts = geographyService.findAllDistricts();
        return ResponseEntity.ok(districts);
    }

    /**
     * Получить район по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/districts/{id} (например: /api/geography/districts/1)
     * 
     * @param id идентификатор района
     * @return ResponseEntity с данными района и HTTP статусом 200 (OK)
     */
    @GetMapping("/districts/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable Long id) {
        District district = geographyService.findDistrictById(id);
        return ResponseEntity.ok(district);
    }

    /**
     * Получить районы конкретного города
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/{cityId}/districts
     * (например: /api/geography/cities/1/districts)
     * 
     * @param cityId идентификатор города
     * @return ResponseEntity со списком районов указанного города
     */
    @GetMapping("/cities/{cityId}/districts")
    public ResponseEntity<List<District>> getDistrictsByCity(@PathVariable Long cityId) {
        List<District> districts = geographyService.findDistrictsByCity(cityId);
        return ResponseEntity.ok(districts);
    }

    /**
     * Получить районы конкретного региона
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/{regionId}/districts
     * 
     * @param regionId идентификатор региона
     * @return ResponseEntity со списком районов указанного региона
     */
    @GetMapping("/regions/{regionId}/districts")
    public ResponseEntity<List<District>> getDistrictsByRegion(@PathVariable Long regionId) {
        List<District> districts = geographyService.findDistrictsByRegion(regionId);
        return ResponseEntity.ok(districts);
    }

    /**
     * Получить районы конкретной страны
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries/{countryId}/districts
     * 
     * @param countryId идентификатор страны
     * @return ResponseEntity со списком районов указанной страны
     */
    @GetMapping("/countries/{countryId}/districts")
    public ResponseEntity<List<District>> getDistrictsByCountry(@PathVariable Long countryId) {
        List<District> districts = geographyService.findDistrictsByCountry(countryId);
        return ResponseEntity.ok(districts);
    }

    /**
     * Найти район по названию и городу
     * 
     * HTTP метод: GET
     * URL: /api/geography/districts/search/by-name-and-city?districtName=Центральный&cityId=1
     * 
     * @param districtName название района
     * @param cityId идентификатор города
     * @return ResponseEntity с найденным районом
     */
    @GetMapping("/districts/search/by-name-and-city")
    public ResponseEntity<District> getDistrictByNameAndCity(
            @RequestParam String districtName, 
            @RequestParam Long cityId) {
        
        District district = geographyService.findDistrictByNameAndCity(districtName, cityId);
        return ResponseEntity.ok(district);
    }

    // ========== УЛИЦЫ ==========

    /**
     * Получить список всех улиц
     * 
     * HTTP метод: GET
     * URL: /api/geography/streets
     * 
     * @return ResponseEntity со списком всех улиц и HTTP статусом 200 (OK)
     */
    @GetMapping("/streets")
    public ResponseEntity<List<Street>> getAllStreets() {
        List<Street> streets = geographyService.findAllStreets();
        return ResponseEntity.ok(streets);
    }

    /**
     * Получить улицу по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/streets/{id} (например: /api/geography/streets/1)
     * 
     * @param id идентификатор улицы
     * @return ResponseEntity с данными улицы и HTTP статусом 200 (OK)
     */
    @GetMapping("/streets/{id}")
    public ResponseEntity<Street> getStreetById(@PathVariable Long id) {
        Street street = geographyService.findStreetById(id);
        return ResponseEntity.ok(street);
    }

    /**
     * Получить улицы конкретного города
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/{cityId}/streets
     * (например: /api/geography/cities/1/streets)
     * 
     * @param cityId идентификатор города
     * @return ResponseEntity со списком улиц указанного города
     */
    @GetMapping("/cities/{cityId}/streets")
    public ResponseEntity<List<Street>> getStreetsByCity(@PathVariable Long cityId) {
        List<Street> streets = geographyService.findStreetsByCity(cityId);
        return ResponseEntity.ok(streets);
    }

    /**
     * Найти улицу по названию и городу
     * 
     * HTTP метод: GET
     * URL: /api/geography/streets/search/by-name-and-city?streetName=Ленина&cityId=1
     * 
     * @param streetName название улицы
     * @param cityId идентификатор города
     * @return ResponseEntity с найденной улицей
     */
    @GetMapping("/streets/search/by-name-and-city")
    public ResponseEntity<Street> getStreetByNameAndCity(
            @RequestParam String streetName, 
            @RequestParam Long cityId) {
        
        Street street = geographyService.findStreetByNameAndCity(streetName, cityId);
        return ResponseEntity.ok(street);
    }

    /**
     * Найти улицы по шаблону названия
     * 
     * HTTP метод: GET
     * URL: /api/geography/streets/search/by-name-pattern?pattern=Лен
     * 
     * Поиск выполняется по частичному совпадению (LIKE %pattern%)
     * 
     * @param pattern шаблон названия улицы для поиска
     * @return ResponseEntity со списком найденных улиц
     */
    @GetMapping("/streets/search/by-name-pattern")
    public ResponseEntity<List<Street>> getStreetsByNamePattern(@RequestParam String pattern) {
        List<Street> streets = geographyService.findStreetsByNamePattern(pattern);
        return ResponseEntity.ok(streets);
    }

    // ========== ENDPOINTS ДЛЯ РАБОТЫ С DTO ==========

    // ========== РЕГИОНЫ С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все регионы с детальной информацией (включая данные о стране)
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/with-details
     * 
     * Возвращает регионы с полной информацией о стране.
     * Использует JOIN запросы для оптимизации производительности
     * 
     * @return ResponseEntity со списком регионов с детальной информацией
     */
    @GetMapping("/regions/with-details")
    public ResponseEntity<List<RegionWithDetailsDto>> getAllRegionsWithDetails() {
        List<RegionWithDetailsDto> regions = geographyService.findAllRegionsWithDetails();
        return ResponseEntity.ok(regions);
    }

    /**
     * Получить регион с детальной информацией по идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/{id}/with-details (например: /api/geography/regions/1/with-details)
     * 
     * @param id идентификатор региона
     * @return ResponseEntity с детальной информацией о регионе
     */
    @GetMapping("/regions/{id}/with-details")
    public ResponseEntity<RegionWithDetailsDto> getRegionByIdWithDetails(@PathVariable Long id) {
        RegionWithDetailsDto region = geographyService.findRegionByIdWithDetails(id);
        return ResponseEntity.ok(region);
    }

    /**
     * Получить регионы конкретной страны с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/geography/countries/{countryId}/regions/with-details
     * (например: /api/geography/countries/1/regions/with-details)
     * 
     * @param countryId идентификатор страны
     * @return ResponseEntity со списком регионов с детальной информацией указанной страны
     */
    @GetMapping("/countries/{countryId}/regions/with-details")
    public ResponseEntity<List<RegionWithDetailsDto>> getRegionsByCountryWithDetails(@PathVariable Long countryId) {
        List<RegionWithDetailsDto> regions = geographyService.findRegionsByCountryWithDetails(countryId);
        return ResponseEntity.ok(regions);
    }

    // ========== ГОРОДА С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все города с детальной информацией (включая регион и страну)
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/with-details
     * 
     * Возвращает города с полной географической иерархией.
     * Использует JOIN запросы для оптимизации производительности
     * 
     * @return ResponseEntity со списком городов с детальной информацией
     */
    @GetMapping("/cities/with-details")
    public ResponseEntity<List<CityWithDetailsDto>> getAllCitiesWithDetails() {
        List<CityWithDetailsDto> cities = geographyService.findAllCitiesWithDetails();
        return ResponseEntity.ok(cities);
    }

    /**
     * Получить город с детальной информацией по идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/{id}/with-details (например: /api/geography/cities/1/with-details)
     * 
     * @param id идентификатор города
     * @return ResponseEntity с детальной информацией о городе
     */
    @GetMapping("/cities/{id}/with-details")
    public ResponseEntity<CityWithDetailsDto> getCityByIdWithDetails(@PathVariable Long id) {
        CityWithDetailsDto city = geographyService.findCityByIdWithDetails(id);
        return ResponseEntity.ok(city);
    }

    /**
     * Получить города конкретного региона с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/geography/regions/{regionId}/cities/with-details
     * (например: /api/geography/regions/1/cities/with-details)
     * 
     * @param regionId идентификатор региона
     * @return ResponseEntity со списком городов с детальной информацией указанного региона
     */
    @GetMapping("/regions/{regionId}/cities/with-details")
    public ResponseEntity<List<CityWithDetailsDto>> getCitiesByRegionWithDetails(@PathVariable Long regionId) {
        List<CityWithDetailsDto> cities = geographyService.findCitiesByRegionWithDetails(regionId);
        return ResponseEntity.ok(cities);
    }

    // ========== РАЙОНЫ С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все районы с детальной информацией (включая город, регион и страну)
     * 
     * HTTP метод: GET
     * URL: /api/geography/districts/with-details
     * 
     * Возвращает районы с полной географической иерархией.
     * Использует JOIN запросы для оптимизации производительности
     * 
     * @return ResponseEntity со списком районов с детальной информацией
     */
    @GetMapping("/districts/with-details")
    public ResponseEntity<List<DistrictWithDetailsDto>> getAllDistrictsWithDetails() {
        List<DistrictWithDetailsDto> districts = geographyService.findAllDistrictsWithDetails();
        return ResponseEntity.ok(districts);
    }

    /**
     * Получить район с детальной информацией по идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/districts/{id}/with-details (например: /api/geography/districts/1/with-details)
     * 
     * @param id идентификатор района
     * @return ResponseEntity с детальной информацией о районе
     */
    @GetMapping("/districts/{id}/with-details")
    public ResponseEntity<DistrictWithDetailsDto> getDistrictByIdWithDetails(@PathVariable Long id) {
        DistrictWithDetailsDto district = geographyService.findDistrictByIdWithDetails(id);
        return ResponseEntity.ok(district);
    }

    /**
     * Получить районы конкретного города с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/{cityId}/districts/with-details
     * (например: /api/geography/cities/1/districts/with-details)
     * 
     * @param cityId идентификатор города
     * @return ResponseEntity со списком районов с детальной информацией указанного города
     */
    @GetMapping("/cities/{cityId}/districts/with-details")
    public ResponseEntity<List<DistrictWithDetailsDto>> getDistrictsByCityWithDetails(@PathVariable Long cityId) {
        List<DistrictWithDetailsDto> districts = geographyService.findDistrictsByCityWithDetails(cityId);
        return ResponseEntity.ok(districts);
    }

    // ========== УЛИЦЫ С ДЕТАЛЬНОЙ ИНФОРМАЦИЕЙ ==========

    /**
     * Получить все улицы с детальной информацией (включая город, регион и страну)
     * 
     * HTTP метод: GET
     * URL: /api/geography/streets/with-details
     * 
     * Возвращает улицы с полной географической иерархией.
     * Использует JOIN запросы для оптимизации производительности
     * 
     * @return ResponseEntity со списком улиц с детальной информацией
     */
    @GetMapping("/streets/with-details")
    public ResponseEntity<List<StreetWithDetailsDto>> getAllStreetsWithDetails() {
        List<StreetWithDetailsDto> streets = geographyService.findAllStreetsWithDetails();
        return ResponseEntity.ok(streets);
    }

    /**
     * Получить улицу с детальной информацией по идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/geography/streets/{id}/with-details (например: /api/geography/streets/1/with-details)
     * 
     * @param id идентификатор улицы
     * @return ResponseEntity с детальной информацией об улице
     */
    @GetMapping("/streets/{id}/with-details")
    public ResponseEntity<StreetWithDetailsDto> getStreetByIdWithDetails(@PathVariable Long id) {
        StreetWithDetailsDto street = geographyService.findStreetByIdWithDetails(id);
        return ResponseEntity.ok(street);
    }

    /**
     * Получить улицы конкретного города с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/geography/cities/{cityId}/streets/with-details
     * (например: /api/geography/cities/1/streets/with-details)
     * 
     * @param cityId идентификатор города
     * @return ResponseEntity со списком улиц с детальной информацией указанного города
     */
    @GetMapping("/cities/{cityId}/streets/with-details")
    public ResponseEntity<List<StreetWithDetailsDto>> getStreetsByCityWithDetails(@PathVariable Long cityId) {
        List<StreetWithDetailsDto> streets = geographyService.findStreetsByCityWithDetails(cityId);
        return ResponseEntity.ok(streets);
    }
} 