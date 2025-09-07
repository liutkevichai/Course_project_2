package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.realestate.realestate_app.service.reference.DealTypeService;
import ru.realestate.realestate_app.service.reference.PropertyTypeService;
import ru.realestate.realestate_app.service.reference.GeographyService;

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
    public String getRegions(Model model) {
        model.addAttribute("items", geographyService.findAllRegionsWithDetails());
        model.addAttribute("pageTitle", "Регионы");
        model.addAttribute("headers", List.of("ID", "Название", "Код", "Страна"));
        model.addAttribute("fields", List.of("regionId", "regionName", "regionCode", "countryName"));
        return "reference-view";
    }

    /**
     * Отображает список городов
     */
    @GetMapping("/cities")
    public String getCities(Model model) {
        model.addAttribute("items", geographyService.findAllCitiesWithDetails());
        model.addAttribute("pageTitle", "Города");
        model.addAttribute("headers", List.of("ID", "Название", "Регион", "Страна"));
        model.addAttribute("fields", List.of("cityId", "cityName", "regionName", "countryName"));
        return "reference-view";
    }

    /**
     * Отображает список районов
     */
    @GetMapping("/districts")
    public String getDistricts(Model model) {
        model.addAttribute("items", geographyService.findAllDistrictsWithDetails());
        model.addAttribute("pageTitle", "Районы");
        model.addAttribute("headers", List.of("ID", "Название", "Город", "Регион"));
        model.addAttribute("fields", List.of("districtId", "districtName", "cityName", "regionName"));
        return "reference-view";
    }

    /**
     * Отображает список улиц
     */
    @GetMapping("/streets")
    public String getStreets(Model model) {
        model.addAttribute("items", geographyService.findAllStreetsWithDetails());
        model.addAttribute("pageTitle", "Улицы");
        model.addAttribute("headers", List.of("ID", "Название", "Город", "Регион"));
        model.addAttribute("fields", List.of("streetId", "streetName", "cityName", "regionName"));
        return "reference-view";
    }
}