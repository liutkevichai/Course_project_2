package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        // Fetch data for the main table and for dropdowns in forms
        model.addAttribute("properties", propertyService.findAllForTable()); // Use a DTO if possible
        model.addAttribute("propertyTypes", propertyTypeService.findAll());
        model.addAttribute("cities", geographyService.findAllCities()); // Example
        model.addAttribute("pageTitle", "Недвижимость");
        return "properties";
    }
}