package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import ru.realestate.realestate_app.model.Property;
import ru.realestate.realestate_app.model.dto.PropertyReportDto;
import ru.realestate.realestate_app.model.dto.PropertyTableDto;
import ru.realestate.realestate_app.service.CsvExportService;
import ru.realestate.realestate_app.service.PropertyService;
import ru.realestate.realestate_app.service.reference.PropertyTypeService;
import ru.realestate.realestate_app.service.reference.GeographyService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/properties")
public class PropertyWebController {

    private final PropertyService propertyService;
    private final PropertyTypeService propertyTypeService;
    private final GeographyService geographyService;
    private final CsvExportService csvExportService;

    public PropertyWebController(PropertyService propertyService,
                                PropertyTypeService propertyTypeService,
                                GeographyService geographyService,
                                CsvExportService csvExportService) {
        this.propertyService = propertyService;
        this.propertyTypeService = propertyTypeService;
        this.geographyService = geographyService;
        this.csvExportService = csvExportService;
    }

    @GetMapping
    public String getPropertiesPage(Model model,
                                @RequestParam(required = false) BigDecimal minPrice,
                                @RequestParam(required = false) BigDecimal maxPrice,
                                @RequestParam(required = false) Long cityId,
                                @RequestParam(required = false) Long propertyTypeId,
                                @RequestParam(required = false) Long districtId,
                                @RequestParam(required = false) Long streetId) {
        
        // Логика поиска передается в сервис
        List<PropertyTableDto> properties = propertyService.searchProperties(minPrice, maxPrice, cityId, propertyTypeId, districtId, streetId);
        model.addAttribute("properties", properties);
        
        // Добавляем в модель все необходимое для рендеринга страницы, включая параметры поиска для формы
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("cityId", cityId);
        model.addAttribute("propertyTypeId", propertyTypeId);
        model.addAttribute("districtId", districtId);
        model.addAttribute("streetId", streetId);
        model.addAttribute("propertyTypes", propertyTypeService.findAll());
        model.addAttribute("citiesWithDetails", geographyService.findAllCitiesWithDetails());
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
    
    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateProperty(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        propertyService.update(id, updates);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generatePropertyReport() {
        try {
            // Получаем все объекты недвижимости для отчета
            List<PropertyReportDto> properties = propertyService.findAllForReport();
            
            // Экспортируем данные в CSV
            byte[] csvData = csvExportService.exportToCsv(properties, PropertyReportDto.class);
            
            // Формируем имя файла с текущей датой
            String fileName = "properties_report_" + java.time.LocalDate.now() + ".csv";
            
            // Возвращаем ответ с CSV данными
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .header("Content-Type", "text/csv; charset=utf-8")
                    .body(csvData);
        } catch (Exception _) {
            // В случае ошибки возвращаем пустой массив байтов
            return ResponseEntity.internalServerError().body(new byte[0]);
        }
    }
}