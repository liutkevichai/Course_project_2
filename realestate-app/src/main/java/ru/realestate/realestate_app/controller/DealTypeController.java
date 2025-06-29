package ru.realestate.realestate_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.DealType;
import ru.realestate.realestate_app.service.reference.DealTypeService;

import java.util.List;

/**
 * REST контроллер для работы с типами сделок
 * 
 * Этот контроллер предоставляет HTTP API для работы со справочником типов сделок.
 * Типы сделок - это справочные данные (например: "Продажа", "Аренда", "Обмен"),
 * поэтому контроллер предоставляет только операции чтения (GET).
 * 
 */
@RestController
@RequestMapping("/api/deal-types")
public class DealTypeController {

    private final DealTypeService dealTypeService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param dealTypeService сервис для работы с типами сделок
     */
    public DealTypeController(DealTypeService dealTypeService) {
        this.dealTypeService = dealTypeService;
    }

    /**
     * Получить список всех типов сделок
     * 
     * HTTP метод: GET
     * URL: /api/deal-types
     * 
     * Возвращает все типы сделок, отсортированные по названию в алфавитном порядке.
     * 
     * @return ResponseEntity со списком всех типов сделок и HTTP статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<DealType>> getAllDealTypes() {
        List<DealType> dealTypes = dealTypeService.findAll();
        return ResponseEntity.ok(dealTypes);
    }

    /**
     * Получить тип сделки по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/deal-types/{id} (например: /api/deal-types/1)
     * 
     * @param id идентификатор типа сделки
     * @return ResponseEntity с данными типа сделки и HTTP статусом 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<DealType> getDealTypeById(@PathVariable Long id) {
        DealType dealType = dealTypeService.findById(id);
        return ResponseEntity.ok(dealType);
    }

    /**
     * Найти тип сделки по названию
     * 
     * HTTP метод: GET
     * URL: /api/deal-types/search/by-name?name=Продажа
     * 
     * Поиск выполняется по точному совпадению названия (case-sensitive)
     * 
     * Пример использования:
     * GET /api/deal-types/search/by-name?name=Продажа
     * GET /api/deal-types/search/by-name?name=Аренда
     * 
     * @param name название типа сделки для поиска
     * @return ResponseEntity с найденным типом сделки и HTTP статусом 200 (OK)
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<DealType> getDealTypeByName(@RequestParam String name) {
        // Ищем тип сделки по названию через сервис
        DealType dealType = dealTypeService.findByName(name);
        
        // Возвращаем найденный тип сделки
        return ResponseEntity.ok(dealType);
    }
} 