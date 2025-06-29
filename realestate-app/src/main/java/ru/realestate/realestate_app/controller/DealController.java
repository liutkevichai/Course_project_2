package ru.realestate.realestate_app.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.Deal;
import ru.realestate.realestate_app.model.dto.DealWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DealTableDto;
import ru.realestate.realestate_app.service.DealService;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для работы со сделками недвижимости
 * 
 * Этот контроллер предоставляет HTTP API для выполнения операций CRUD со сделками,
 * включая расширенные возможности поиска:
 * - по дате и диапазону дат
 * - по риелтору, клиенту, объекту недвижимости
 * - по типу сделки
 * - по стоимости
 * - статистические данные (общая сумма сделок, количество)
 * 
 */
@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param dealService сервис для работы со сделками
     */
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    /**
     * Получить список всех сделок
     * 
     * HTTP метод: GET
     * URL: /api/deals
     * 
     * Возвращает все сделки, отсортированные по дате в убывающем порядке
     * (самые новые сделки в начале списка)
     * 
     * @return ResponseEntity со списком всех сделок и HTTP статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {
        List<Deal> deals = dealService.findAll();
        return ResponseEntity.ok(deals);
    }

    /**
     * Получить сделку по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/deals/{id} (например: /api/deals/123)
     * 
     * 
     * @param id идентификатор сделки
     * @return ResponseEntity с данными сделки и HTTP статусом 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Deal> getDealById(@PathVariable Long id) {
        Deal deal = dealService.findById(id);
        return ResponseEntity.ok(deal);
    }

    /**
     * Создать новую сделку
     * 
     * HTTP метод: POST
     * URL: /api/deals
     * Content-Type: application/json
     * 
     * Пример JSON для создания сделки:
     * {
     *   "dealDate": "2024-01-15",
     *   "cost": 5000000.00,
     *   "dealTypeId": 1,
     *   "clientId": 1,
     *   "realtorId": 1,
     *   "propertyId": 1
     * }
     * 
     * Важно: При создании сделки выполняются бизнес-проверки:
     * - проверка существования связанных объектов (клиент, риелтор, объект недвижимости)
     * - проверка соответствия стоимости сделки стоимости объекта недвижимости
     * - другие бизнес-правила, определенные в сервисе
     * 
     * @param deal данные новой сделки из JSON в теле запроса
     * @return ResponseEntity с ID созданной сделки и HTTP статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDeal(@Valid @RequestBody Deal deal) {
        Long dealId = dealService.save(deal);
        Map<String, Object> response = Map.of(
            "id", dealId,
            "message", "Сделка успешно создана"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновить данные существующей сделки
     * 
     * HTTP метод: PUT
     * URL: /api/deals/{id} (например: /api/deals/123)
     * Content-Type: application/json
     * 
     * Пример JSON для обновления стоимости сделки:
     * {
     *   "cost": 5200000.00
     * }
     * 
     * Важно: При обновлении сделки также выполняются бизнес-проверки
     * 
     * @param id идентификатор сделки для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return ResponseEntity с сообщением об успешном обновлении и HTTP статусом 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateDeal(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> updates) {
            
        boolean updated = dealService.update(id, updates);
        
        if (updated) {
            Map<String, String> response = Map.of(
                "message", "Данные сделки успешно обновлены"
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
     * Удалить сделку по идентификатору
     * 
     * HTTP метод: DELETE
     * URL: /api/deals/{id} (например: /api/deals/123)
     * 
     * В отличие от клиентов, риелторов и объектов недвижимости,
     * сделки можно удалять без дополнительных проверок на связанные записи
     * 
     * @param id идентификатор сделки для удаления
     * @return ResponseEntity с сообщением об успешном удалении и HTTP статусом 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDeal(@PathVariable Long id) {
        boolean deleted = dealService.deleteById(id);
        
        if (deleted) {
            Map<String, String> response = Map.of(
                "message", "Сделка успешно удалена"
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = Map.of(
                "message", "Сделка не найдена"
            );
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Найти сделки по конкретной дате
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-date?date=2024-01-15
     * 
     * Поиск выполняется по точному совпадению даты
     * Результаты сортируются по убыванию стоимости
     * 
     * @param date дата совершения сделки в формате YYYY-MM-DD
     * @return ResponseEntity со списком сделок, совершенных в указанную дату
     */
    @GetMapping("/search/by-date")
    public ResponseEntity<List<Deal>> getDealsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Deal> deals = dealService.findByDate(date);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки в указанном диапазоне дат
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-date-range?startDate=2024-01-01&endDate=2024-01-31
     * 
     * Поиск выполняется включительно для обеих дат (startDate <= dealDate <= endDate)
     * Результаты сортируются по убыванию даты (самые новые первые)
     * 
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return ResponseEntity со списком сделок в указанном диапазоне дат
     */
    @GetMapping("/search/by-date-range")
    public ResponseEntity<List<Deal>> getDealsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Deal> deals = dealService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки конкретного риелтора
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-realtor/{realtorId} (например: /api/deals/search/by-realtor/1)
     * 
     * Результаты сортируются по убыванию даты
     * 
     * @param realtorId идентификатор риелтора
     * @return ResponseEntity со списком сделок указанного риелтора
     */
    @GetMapping("/search/by-realtor/{realtorId}")
    public ResponseEntity<List<Deal>> getDealsByRealtorId(@PathVariable Long realtorId) {
        List<Deal> deals = dealService.findByRealtorId(realtorId);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки конкретного клиента
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-client/{clientId} (например: /api/deals/search/by-client/1)
     * 
     * Результаты сортируются по убыванию даты
     * 
     * @param clientId идентификатор клиента
     * @return ResponseEntity со списком сделок указанного клиента
     */
    @GetMapping("/search/by-client/{clientId}")
    public ResponseEntity<List<Deal>> getDealsByClientId(@PathVariable Long clientId) {
        List<Deal> deals = dealService.findByClientId(clientId);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки по конкретному объекту недвижимости
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-property/{propertyId} (например: /api/deals/search/by-property/1)
     * 
     * Результаты сортируются по убыванию даты
     * 
     * @param propertyId идентификатор объекта недвижимости
     * @return ResponseEntity со списком сделок по указанному объекту недвижимости
     */
    @GetMapping("/search/by-property/{propertyId}")
    public ResponseEntity<List<Deal>> getDealsByPropertyId(@PathVariable Long propertyId) {
        List<Deal> deals = dealService.findByPropertyId(propertyId);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки по типу
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-type/{dealTypeId} (например: /api/deals/search/by-type/1)
     * 
     * Результаты сортируются по убыванию даты
     * 
     * @param dealTypeId идентификатор типа сделки
     * @return ResponseEntity со списком сделок указанного типа
     */
    @GetMapping("/search/by-type/{dealTypeId}")
    public ResponseEntity<List<Deal>> getDealsByDealTypeId(@PathVariable Long dealTypeId) {
        List<Deal> deals = dealService.findByDealTypeId(dealTypeId);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки по диапазону стоимости
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-cost-range?minCost=1000000&maxCost=10000000
     * 
     * Поиск выполняется включительно для обеих границ (minCost <= cost <= maxCost)
     * Результаты сортируются по убыванию стоимости
     * @param minCost минимальная стоимость сделки (включительно)
     * @param maxCost максимальная стоимость сделки (включительно)
     * @return ResponseEntity со списком сделок в указанном диапазоне стоимости
     */
    @GetMapping("/search/by-cost-range")
    public ResponseEntity<List<Deal>> getDealsByCostRange(
            @RequestParam BigDecimal minCost, 
            @RequestParam BigDecimal maxCost) {
        
        List<Deal> deals = dealService.findByCostRange(minCost, maxCost);
        return ResponseEntity.ok(deals);
    }

    /**
     * Получить общую сумму всех сделок
     * 
     * HTTP метод: GET
     * URL: /api/deals/total-amount
     * 
     * @return ResponseEntity с общей суммой всех сделок
     */
    @GetMapping("/total-amount")
    public ResponseEntity<Map<String, BigDecimal>> getTotalDealsAmount() {
        BigDecimal totalAmount = dealService.getTotalDealsAmount();
        
        Map<String, BigDecimal> response = Map.of(
            "totalAmount", totalAmount
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Получить общее количество сделок в базе данных
     * 
     * HTTP метод: GET
     * URL: /api/deals/count
     * 
     * Возвращает статистическую информацию о количестве сделок
     * 
     * @return ResponseEntity с количеством сделок
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getDealsCount() {
        int count = dealService.getCount();
        Map<String, Integer> response = Map.of(
            "totalDeals", count
        );
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINTS ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все сделки с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/deals/with-details
     * 
     * Возвращает все сделки с полной информацией о клиенте, риелторе и объекте недвижимости.
     * Использует JOIN запросы для оптимизации производительности (один запрос вместо N+1)
     * 
     * @return ResponseEntity со списком сделок с детальной информацией
     */
    @GetMapping("/with-details")
    public ResponseEntity<List<DealWithDetailsDto>> getAllDealsWithDetails() {
        List<DealWithDetailsDto> deals = dealService.findAllWithDetails();
        return ResponseEntity.ok(deals);
    }

    /**
     * Получить сделку с детальной информацией по идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/deals/{id}/with-details (например: /api/deals/123/with-details)
     * 
     * @param id идентификатор сделки
     * @return ResponseEntity с детальной информацией о сделке
     */
    @GetMapping("/{id}/with-details")
    public ResponseEntity<DealWithDetailsDto> getDealByIdWithDetails(@PathVariable Long id) {
        DealWithDetailsDto deal = dealService.findByIdWithDetails(id);
        return ResponseEntity.ok(deal);
    }

    /**
     * Получить все сделки в табличном формате
     * 
     * HTTP метод: GET
     * URL: /api/deals/for-table
     * 
     * Возвращает компактное представление сделок для отображения в таблицах.
     * Содержит основную информацию в удобном для пользователя формате
     * 
     * @return ResponseEntity со списком сделок в табличном формате
     */
    @GetMapping("/for-table")
    public ResponseEntity<List<DealTableDto>> getAllDealsForTable() {
        List<DealTableDto> deals = dealService.findAllForTable();
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки по дате с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-date-with-details?date=2024-01-15
     * 
     * @param date дата совершения сделки в формате YYYY-MM-DD
     * @return ResponseEntity со списком сделок с детальной информацией, совершенных в указанную дату
     */
    @GetMapping("/search/by-date-with-details")
    public ResponseEntity<List<DealWithDetailsDto>> getDealsByDateWithDetails(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<DealWithDetailsDto> deals = dealService.findByDateWithDetails(date);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки в диапазоне дат с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-date-range-with-details?startDate=2024-01-01&endDate=2024-01-31
     * 
     * @param startDate начальная дата диапазона в формате YYYY-MM-DD
     * @param endDate конечная дата диапазона в формате YYYY-MM-DD
     * @return ResponseEntity со списком сделок с детальной информацией в указанном диапазоне дат
     */
    @GetMapping("/search/by-date-range-with-details")
    public ResponseEntity<List<DealWithDetailsDto>> getDealsByDateRangeWithDetails(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<DealWithDetailsDto> deals = dealService.findByDateRangeWithDetails(startDate, endDate);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки риелтора с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-realtor/{realtorId}/with-details (например: /api/deals/search/by-realtor/1/with-details)
     * 
     * @param realtorId идентификатор риелтора
     * @return ResponseEntity со списком сделок с детальной информацией указанного риелтора
     */
    @GetMapping("/search/by-realtor/{realtorId}/with-details")
    public ResponseEntity<List<DealWithDetailsDto>> getDealsByRealtorIdWithDetails(@PathVariable Long realtorId) {
        List<DealWithDetailsDto> deals = dealService.findByRealtorIdWithDetails(realtorId);
        return ResponseEntity.ok(deals);
    }

    /**
     * Найти сделки клиента с детальной информацией
     * 
     * HTTP метод: GET
     * URL: /api/deals/search/by-client/{clientId}/with-details (например: /api/deals/search/by-client/1/with-details)
     * 
     * @param clientId идентификатор клиента
     * @return ResponseEntity со списком сделок с детальной информацией указанного клиента
     */
    @GetMapping("/search/by-client/{clientId}/with-details")
    public ResponseEntity<List<DealWithDetailsDto>> getDealsByClientIdWithDetails(@PathVariable Long clientId) {
        List<DealWithDetailsDto> deals = dealService.findByClientIdWithDetails(clientId);
        return ResponseEntity.ok(deals);
    }
} 