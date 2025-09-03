package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.realestate.realestate_app.model.Property;
import ru.realestate.realestate_app.service.PropertyService;
import ru.realestate.realestate_app.service.reference.PropertyTypeService;
import ru.realestate.realestate_app.service.reference.GeographyService;


@Controller
@RequestMapping("/properties")
public class PropertyWebController {

    private final PropertyService propertyService;
    private final PropertyTypeService propertyTypeService;
    private final GeographyService geographyService;

    public PropertyWebController(PropertyService propertyService,
                                PropertyTypeService propertyTypeService,
                                GeographyService geographyService) {
        this.propertyService = propertyService;
        this.propertyTypeService = propertyTypeService;
        this.geographyService = geographyService;
    }

    @GetMapping
    public String getPropertiesPage(Model model) {
        model.addAttribute("properties", propertyService.findAllForTable());
        model.addAttribute("propertyTypes", propertyTypeService.findAll());
        model.addAttribute("cities", geographyService.findAllCities());
        model.addAttribute("regions", geographyService.findAllRegions());
        model.addAttribute("districts", geographyService.findAllDistricts());
        model.addAttribute("streets", geographyService.findAllStreets());
        model.addAttribute("countries", geographyService.findAllCountries());
        Property newProperty = new Property();
        newProperty.setIdRegion(null);
        newProperty.setIdDistrict(null);
        newProperty.setIdStreet(null);
        newProperty.setIdCountry(null);
        model.addAttribute("newProperty", newProperty); // Для формы добавления
        model.addAttribute("pageTitle", "Недвижимость");
        return "properties";
    }

    @PostMapping("/add")
    public String addProperty(@ModelAttribute Property property) {
        propertyService.save(property);
        return "redirect:/properties";
    }
}