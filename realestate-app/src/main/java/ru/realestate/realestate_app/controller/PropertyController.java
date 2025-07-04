package ru.realestate.realestate_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.Property;
import ru.realestate.realestate_app.model.dto.PropertyWithDetailsDto;
import ru.realestate.realestate_app.model.dto.PropertyTableDto;
import ru.realestate.realestate_app.service.PropertyService;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для работы с объектами недвижимости
 * 
 * Этот контроллер предоставляет HTTP API для выполнения операций CRUD 
 * над объектами недвижимости, включая поиск по различным критериям:
 * - по ценовому диапазону
 * - по городу
 * - по типу недвижимости
 * 
*/
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param propertyService сервис для работы с объектами недвижимости
     */
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /**
     * Получить список всех объектов недвижимости
     * 
     * HTTP метод: GET
     * URL: /api/properties
     * 
     * Возвращает все объекты недвижимости, отсортированные по стоимости в убывающем порядке
     * 
     * @return ResponseEntity со списком всех объектов недвижимости и HTTP статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.findAll();
        return ResponseEntity.ok(properties);
    }

    /**
     * Получить объект недвижимости по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/properties/{id} (например: /api/properties/123)
     * 
     * @param id идентификатор объекта недвижимости
     * @return ResponseEntity с данными объекта недвижимости и HTTP статусом 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.findById(id);
        return ResponseEntity.ok(property);
    }

    /**
     * Создать новый объект недвижимости
     * 
     * HTTP метод: POST
     * URL: /api/properties
     * Content-Type: application/json
     * 
     * 
     * Пример JSON для создания объекта недвижимости:
     * {
     *   "price": 5000000.00,
     *   "area": 85.5,
     *   "roomsCount": 3,
     *   "address": "ул. Ленина, д. 10, кв. 25",
     *   "propertyTypeId": 1,
     *   "cityId": 1
     * }
     * 
     * @param property данные нового объекта недвижимости из JSON в теле запроса
     * @return ResponseEntity с ID созданного объекта и HTTP статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProperty(@Valid @RequestBody Property property) {
        Long propertyId = propertyService.save(property);
        Map<String, Object> response = Map.of(
            "id", propertyId,
            "message", "Объект недвижимости успешно создан"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновить данные существующего объекта недвижимости
     * 
     * HTTP метод: PUT
     * URL: /api/properties/{id} (например: /api/properties/123)
     * Content-Type: application/json
     * 
     * Пример JSON для обновления цены и количества комнат:
     * {
     *   "price": 5500000.00,
     *   "roomsCount": 4
     * }
     * 
     * @param id идентификатор объекта недвижимости для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return ResponseEntity с сообщением об успешном обновлении и HTTP статусом 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProperty(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> updates) {
        
        boolean updated = propertyService.update(id, updates);
        
        if (updated) {
            Map<String, String> response = Map.of(
                "message", "Данные объекта недвижимости успешно обновлены"
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = Map.of(
                "message", "Нет данных для обновления"
            );
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Удалить объект недвижимости по идентификатору
     * 
     * HTTP метод: DELETE
     * URL: /api/properties/{id} (например: /api/properties/123)
     * 
     * Если у объекта недвижимости есть связанные сделки, 
     * сервис выбросит BusinessRuleException
     * 
     * @param id идентификатор объекта недвижимости для удаления
     * @return ResponseEntity с сообщением об успешном удалении и HTTP статусом 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProperty(@PathVariable Long id) {
        boolean deleted = propertyService.deleteById(id);
        
        if (deleted) {
            Map<String, String> response = Map.of(
                "message", "Объект недвижимости успешно удален"
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = Map.of(
                "message", "Объект недвижимости не найден"
            );
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Найти объекты недвижимости по ценовому диапазону
     * 
     * HTTP метод: GET
     * URL: /api/properties/search/by-price-range?minPrice=1000000&maxPrice=10000000
     * 
     * Результаты сортируются по цене в возрастающем порядке
     * 
     * @param minPrice минимальная цена (включительно)
     * @param maxPrice максимальная цена (включительно)
     * @return ResponseEntity со списком объектов недвижимости в указанном ценовом диапазоне
     */
    @GetMapping("/search/by-price-range")
    public ResponseEntity<List<Property>> getPropertiesByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        
        List<Property> properties = propertyService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(properties);
    }

    /**
     * Найти объекты недвижимости по городу
     * 
     * HTTP метод: GET
     * URL: /api/properties/search/by-city/{cityId} (например: /api/properties/search/by-city/1)
     * 
     * Результаты сортируются по цене в возрастающем порядке
     * 
     * @param cityId идентификатор города
     * @return ResponseEntity со списком объектов недвижимости в указанном городе
     */
    @GetMapping("/search/by-city/{cityId}")
    public ResponseEntity<List<Property>> getPropertiesByCityId(@PathVariable Long cityId) {
        List<Property> properties = propertyService.findByCityId(cityId);
        return ResponseEntity.ok(properties);
    }

    /**
     * Найти объекты недвижимости по типу
     * 
     * HTTP метод: GET
     * URL: /api/properties/search/by-type/{propertyTypeId} 
     * (например: /api/properties/search/by-type/1 для поиска квартир)
     * 
     * @PathVariable Long propertyTypeId - извлекает значение {propertyTypeId} из URL
     * Результаты сортируются по цене в возрастающем порядке
     * 
     * @param propertyTypeId идентификатор типа недвижимости
     * @return ResponseEntity со списком объектов недвижимости указанного типа
     */
    @GetMapping("/search/by-type/{propertyTypeId}")
    public ResponseEntity<List<Property>> getPropertiesByPropertyTypeId(@PathVariable Long propertyTypeId) {
        List<Property> properties = propertyService.findByPropertyTypeId(propertyTypeId);
        return ResponseEntity.ok(properties);
    }

    /**
     * Получить общее количество объектов недвижимости в базе данных
     * 
     * HTTP метод: GET
     * URL: /api/properties/count
     * 
     * Возвращает статистическую информацию о количестве объектов недвижимости
     * 
     * @return ResponseEntity с количеством объектов недвижимости
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getPropertiesCount() {
        int count = propertyService.getCount();
        Map<String, Integer> response = Map.of(
            "totalProperties", count
        );
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINTS ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все объекты недвижимости с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/properties/with-details
     * 
     * Возвращает все объекты недвижимости с полной географической информацией.
     * Использует JOIN запросы для оптимизации производительности (один запрос вместо N+1)
     * 
     * @return ResponseEntity со списком объектов недвижимости с детальной информацией
     */
    @GetMapping("/with-details")
    public ResponseEntity<List<PropertyWithDetailsDto>> getAllPropertiesWithDetails() {
        List<PropertyWithDetailsDto> properties = propertyService.findAllWithDetails();
        return ResponseEntity.ok(properties);
    }

    /**
     * Получить объект недвижимости с детальной информацией по идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/properties/{id}/with-details (например: /api/properties/123/with-details)
     * 
     * @param id идентификатор объекта недвижимости
     * @return ResponseEntity с детальной информацией об объекте недвижимости
     */
    @GetMapping("/{id}/with-details")
    public ResponseEntity<PropertyWithDetailsDto> getPropertyByIdWithDetails(@PathVariable Long id) {
        PropertyWithDetailsDto property = propertyService.findByIdWithDetails(id);
        return ResponseEntity.ok(property);
    }

    /**
     * Получить все объекты недвижимости в табличном формате
     * 
     * HTTP метод: GET
     * URL: /api/properties/for-table
     * 
     * Возвращает компактное представление объектов недвижимости для отображения в каталогах.
     * Содержит основную информацию в удобном для пользователя формате
     * 
     * @return ResponseEntity со списком объектов недвижимости в табличном формате
     */
    @GetMapping("/for-table")
    public ResponseEntity<List<PropertyTableDto>> getAllPropertiesForTable() {
        List<PropertyTableDto> properties = propertyService.findAllForTable();
        return ResponseEntity.ok(properties);
    }

    /**
     * Найти объекты недвижимости по ценовому диапазону с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/properties/search/by-price-range-with-details?minPrice=1000000&maxPrice=10000000
     * 
     * @param minPrice минимальная цена (включительно)
     * @param maxPrice максимальная цена (включительно)
     * @return ResponseEntity со списком объектов недвижимости с детальной информацией в указанном ценовом диапазоне
     */
    @GetMapping("/search/by-price-range-with-details")
    public ResponseEntity<List<PropertyWithDetailsDto>> getPropertiesByPriceRangeWithDetails(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        
        List<PropertyWithDetailsDto> properties = propertyService.findByPriceRangeWithDetails(minPrice, maxPrice);
        return ResponseEntity.ok(properties);
    }

    /**
     * Найти объекты недвижимости по городу с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/properties/search/by-city/{cityId}/with-details (например: /api/properties/search/by-city/1/with-details)
     * 
     * @param cityId идентификатор города
     * @return ResponseEntity со списком объектов недвижимости с детальной информацией в указанном городе
     */
    @GetMapping("/search/by-city/{cityId}/with-details")
    public ResponseEntity<List<PropertyWithDetailsDto>> getPropertiesByCityIdWithDetails(@PathVariable Long cityId) {
        List<PropertyWithDetailsDto> properties = propertyService.findByCityIdWithDetails(cityId);
        return ResponseEntity.ok(properties);
    }

    /**
     * Найти объекты недвижимости по типу с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/properties/search/by-type/{propertyTypeId}/with-details (например: /api/properties/search/by-type/1/with-details)
     * 
     * @param propertyTypeId идентификатор типа недвижимости
     * @return ResponseEntity со списком объектов недвижимости с детальной информацией указанного типа
     */
    @GetMapping("/search/by-type/{propertyTypeId}/with-details")
    public ResponseEntity<List<PropertyWithDetailsDto>> getPropertiesByPropertyTypeIdWithDetails(@PathVariable Long propertyTypeId) {
        List<PropertyWithDetailsDto> properties = propertyService.findByPropertyTypeIdWithDetails(propertyTypeId);
        return ResponseEntity.ok(properties);
    }
} 