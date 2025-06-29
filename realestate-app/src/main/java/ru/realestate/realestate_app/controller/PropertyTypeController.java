package ru.realestate.realestate_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.PropertyType;
import ru.realestate.realestate_app.service.reference.PropertyTypeService;

import java.util.List;

/**
 * REST контроллер для работы с типами недвижимости
 * 
 * Этот контроллер предоставляет HTTP API для работы со справочником типов недвижимости.
 * 
 */
@RestController
@RequestMapping("/api/property-types")
public class PropertyTypeController {

    private final PropertyTypeService propertyTypeService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param propertyTypeService сервис для работы с типами недвижимости
     */
    public PropertyTypeController(PropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    /**
     * Получить список всех типов недвижимости
     * 
     * HTTP метод: GET
     * URL: /api/property-types
     * 
     * Возвращает все типы недвижимости, отсортированные по названию в алфавитном порядке.
     * 
     * Пример ответа:
     * [
     *   {"id": 1, "name": "Квартира"},
     *   {"id": 2, "name": "Дом"},
     *   {"id": 3, "name": "Коммерческая недвижимость"}
     * ]
     * 
     * @return ResponseEntity со списком всех типов недвижимости и HTTP статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<PropertyType>> getAllPropertyTypes() {
        List<PropertyType> propertyTypes = propertyTypeService.findAll();
        return ResponseEntity.ok(propertyTypes);
    }

    /**
     * Получить тип недвижимости по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/property-types/{id} (например: /api/property-types/1)
     * 
     * Пример ответа:
     * {"id": 1, "name": "Квартира"}
     * 
     * @param id идентификатор типа недвижимости
     * @return ResponseEntity с данными типа недвижимости и HTTP статусом 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyType> getPropertyTypeById(@PathVariable Long id) {
        PropertyType propertyType = propertyTypeService.findById(id);
        return ResponseEntity.ok(propertyType);
    }

    /**
     * Найти тип недвижимости по названию
     * 
     * HTTP метод: GET
     * URL: /api/property-types/search/by-name?name=Квартира
     * 
     * Поиск выполняется по точному совпадению названия (case-sensitive)
     * 
     * Примеры использования:
     * GET /api/property-types/search/by-name?name=Квартира
     * GET /api/property-types/search/by-name?name=Дом
     * GET /api/property-types/search/by-name?name=Коммерческая недвижимость
     * 
     * Пример ответа:
     * {"id": 1, "name": "Квартира"}
     * 
     * @param name название типа недвижимости для поиска
     * @return ResponseEntity с найденным типом недвижимости и HTTP статусом 200 (OK)
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<PropertyType> getPropertyTypeByName(@RequestParam String name) {
        PropertyType propertyType = propertyTypeService.findByName(name);
        return ResponseEntity.ok(propertyType);
    }
} 