package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.realestate.realestate_app.service.reference.DealTypeService;
import ru.realestate.realestate_app.service.reference.PropertyTypeService;
import ru.realestate.realestate_app.service.reference.GeographyService;
import org.springframework.web.bind.annotation.RequestParam;
import ru.realestate.realestate_app.model.dto.RegionWithDetailsDto;
import ru.realestate.realestate_app.model.dto.CityWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DistrictWithDetailsDto;
import ru.realestate.realestate_app.model.dto.StreetWithDetailsDto;

import java.util.List;

/**
 * Веб-контроллер для отображения справочных таблиц
 * Обрабатывает запросы к /reference/*
 */
@Controller
@RequestMapping("/reference")
public class ReferenceWebController {

    private final DealTypeService dealTypeService;
    private final PropertyTypeService propertyTypeService;
    private final GeographyService geographyService;

    public ReferenceWebController(DealTypeService dealTypeService,
                                  PropertyTypeService propertyTypeService,
                                  GeographyService geographyService) {
        this.dealTypeService = dealTypeService;
        this.propertyTypeService = propertyTypeService;
        this.geographyService = geographyService;
    }

    /**
     * Отображает список типов сделок
     */
    @GetMapping("/deal-types")
    public String getDealTypes(Model model) {
        model.addAttribute("items", dealTypeService.findAll());
        model.addAttribute("pageTitle", "Типы сделок");
        model.addAttribute("headers", List.of("ID", "Название"));
        // Для универсального шаблона указываем, какие поля отображать
        model.addAttribute("fields", List.of("idDealType", "dealTypeName"));
        return "reference-view";
    }

    /**
     * Отображает список типов недвижимости
     */
    @GetMapping("/property-types")
    public String getPropertyTypes(Model model) {
        model.addAttribute("items", propertyTypeService.findAll());
        model.addAttribute("pageTitle", "Типы недвижимости");
        model.addAttribute("headers", List.of("ID", "Название"));
        model.addAttribute("fields", List.of("idPropertyType", "propertyTypeName"));
        return "reference-view";
    }

    /**
     * Отображает список стран
     */
    @GetMapping("/countries")
    public String getCountries(Model model) {
        model.addAttribute("items", geographyService.findAllCountries());
        model.addAttribute("pageTitle", "Страны");
        model.addAttribute("headers", List.of("ID", "Название"));
        model.addAttribute("fields", List.of("idCountry", "countryName"));
        return "reference-view";
    }

    /**
     * Отображает список регионов
     */
    @GetMapping("/regions")
    public String getRegions(Model model,
                             @RequestParam(required = false) String regionName,
                             @RequestParam(required = false) String regionCode,
                             @RequestParam(required = false) Long countryId) {
        // 1. Проверяем, присутствует ли хотя бы один параметр поиска.
        boolean hasSearchParams = (regionName != null && !regionName.isEmpty()) ||
                                  (regionCode != null && !regionCode.isEmpty()) ||
                                  (countryId != null);

        List<RegionWithDetailsDto> regions;
        if (hasSearchParams) {
            // 2. Вызываем новый метод сервиса для сложного поиска.
            regions = geographyService.searchRegionsWithDetails(regionName, regionCode, countryId);
        } else {
            // 3. Если параметры не предоставлены, получаем все регионы.
            regions = geographyService.findAllRegionsWithDetails();
        }
        
        model.addAttribute("items", regions);
        model.addAttribute("pageTitle", "Регионы");
        model.addAttribute("headers", List.of("ID", "Название", "Код", "Страна"));
        model.addAttribute("fields", List.of("regionId", "regionName", "regionCode", "countryName"));
        // Добавляем параметры поиска обратно в модель, чтобы сохранить их в форме после отправки.
        model.addAttribute("regionName", regionName);
        model.addAttribute("regionCode", regionCode);
        model.addAttribute("countryId", countryId);
        // Добавляем список всех стран для селекта в форме поиска
        model.addAttribute("countries", geographyService.findAllCountries());
        model.addAttribute("allRegions", geographyService.findAllRegionsWithDetails());
        return "reference-view";
    }

    /**
     * Отображает список городов
     */
    @GetMapping("/cities")
    public String getCities(Model model,
                           @RequestParam(required = false) String cityName,
                           @RequestParam(required = false) Long regionId,
                           @RequestParam(required = false) Long countryId) {
        // 1. Проверяем, присутствует ли хотя бы один параметр поиска.
        boolean hasSearchParams = (cityName != null && !cityName.isEmpty()) ||
                                  (regionId != null) ||
                                  (countryId != null);

        List<CityWithDetailsDto> cities;
        if (hasSearchParams) {
            // 2. Вызываем новый метод сервиса для сложного поиска.
            cities = geographyService.searchCitiesWithDetails(cityName, regionId, countryId);
        } else {
            // 3. Если параметры не предоставлены, получаем все города.
            cities = geographyService.findAllCitiesWithDetails();
        }
        
        model.addAttribute("items", cities);
        model.addAttribute("pageTitle", "Города");
        model.addAttribute("headers", List.of("ID", "Название", "Регион", "Страна"));
        model.addAttribute("fields", List.of("cityId", "cityName", "regionName", "countryName"));
        // Добавляем параметры поиска обратно в модель, чтобы сохранить их в форме после отправки.
        model.addAttribute("cityName", cityName);
        model.addAttribute("regionId", regionId);
        model.addAttribute("countryId", countryId);
        // Добавляем список всех регионов и стран для селектов в форме поиска
        model.addAttribute("allRegions", geographyService.findAllRegionsWithDetails());
        model.addAttribute("countries", geographyService.findAllCountries());
        return "reference-view";
    }

    /**
     * Отображает список районов
     */
    @GetMapping("/districts")
    public String getDistricts(Model model,
                              @RequestParam(required = false) String districtName,
                              @RequestParam(required = false) Long cityId,
                              @RequestParam(required = false) Long regionId) {
        // 1. Проверяем, присутствует ли хотя бы один параметр поиска.
        boolean hasSearchParams = (districtName != null && !districtName.isEmpty()) ||
                                  (cityId != null) ||
                                  (regionId != null);

        List<DistrictWithDetailsDto> districts;
        if (hasSearchParams) {
            // 2. Вызываем новый метод сервиса для сложного поиска.
            districts = geographyService.searchDistrictsWithDetails(districtName, cityId, regionId);
        } else {
            // 3. Если параметры не предоставлены, получаем все районы.
            districts = geographyService.findAllDistrictsWithDetails();
        }
        
        model.addAttribute("items", districts);
        model.addAttribute("pageTitle", "Районы");
        model.addAttribute("headers", List.of("ID", "Название", "Город", "Регион"));
        model.addAttribute("fields", List.of("districtId", "districtName", "cityName", "regionName"));
        // Добавляем параметры поиска обратно в модель, чтобы сохранить их в форме после отправки.
        model.addAttribute("districtName", districtName);
        model.addAttribute("cityId", cityId);
        model.addAttribute("regionId", regionId);
        // Добавляем список всех городов и регионов для селектов в форме поиска
        model.addAttribute("allCities", geographyService.findAllCitiesWithDetails());
        model.addAttribute("allRegions", geographyService.findAllRegionsWithDetails());
        return "reference-view";
    }

    /**
     * Отображает список улиц
     */
    @GetMapping("/streets")
    public String getStreets(Model model,
                           @RequestParam(required = false) String streetName,
                           @RequestParam(required = false) Long cityId,
                           @RequestParam(required = false) Long regionId) {
        // 1. Проверяем, присутствует ли хотя бы один параметр поиска.
        boolean hasSearchParams = (streetName != null && !streetName.isEmpty()) ||
                                  (cityId != null) ||
                                  (regionId != null);

        List<StreetWithDetailsDto> streets;
        if (hasSearchParams) {
            // 2. Вызываем новый метод сервиса для сложного поиска.
            streets = geographyService.searchStreetsWithDetails(streetName, cityId, regionId);
        } else {
            // 3. Если параметры не предоставлены, получаем все улицы.
            streets = geographyService.findAllStreetsWithDetails();
        }
        
        model.addAttribute("items", streets);
        model.addAttribute("pageTitle", "Улицы");
        model.addAttribute("headers", List.of("ID", "Название", "Город", "Регион"));
        model.addAttribute("fields", List.of("streetId", "streetName", "cityName", "regionName"));
        // Добавляем параметры поиска обратно в модель, чтобы сохранить их в форме после отправки.
        model.addAttribute("streetName", streetName);
        model.addAttribute("cityId", cityId);
        model.addAttribute("regionId", regionId);
        // Добавляем список всех городов и регионов для селектов в форме поиска
        model.addAttribute("allCities", geographyService.findAllCitiesWithDetails());
        model.addAttribute("allRegions", geographyService.findAllRegionsWithDetails());
        return "reference-view";
    }
}