package ru.realestate.realestate_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.Realtor;
import ru.realestate.realestate_app.service.RealtorService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для работы с риелторами агентства недвижимости
 * 
 * Этот контроллер предоставляет HTTP API для выполнения операций CRUD над риелторами,
 * включая специализированные поиски:
 * - по фамилии
 * - по опыту работы
 * - по номеру телефона
 * - по адресу электронной почты
 * 
 */
@RestController
@RequestMapping("/api/realtors")
public class RealtorController {

    private final RealtorService realtorService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param realtorService сервис для работы с риелторами
     */
    public RealtorController(RealtorService realtorService) {
        this.realtorService = realtorService;
    }

    /**
     * Получить список всех риелторов
     * 
     * HTTP метод: GET
     * URL: /api/realtors
     * 
     * Возвращает всех риелторов, отсортированных по фамилии и имени в алфавитном порядке
     * 
     * @return ResponseEntity со списком всех риелторов и HTTP статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Realtor>> getAllRealtors() {
        List<Realtor> realtors = realtorService.findAll();
        return ResponseEntity.ok(realtors);
    }

    /**
     * Получить риелтора по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/realtors/{id} (например: /api/realtors/123)
     *  
     * @param id идентификатор риелтора
     * @return ResponseEntity с данными риелтора и HTTP статусом 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Realtor> getRealtorById(@PathVariable Long id) {
        Realtor realtor = realtorService.findById(id);
        return ResponseEntity.ok(realtor);
    }

    /**
     * Создать нового риелтора
     * 
     * HTTP метод: POST
     * URL: /api/realtors
     * Content-Type: application/json
     * 
     * Пример JSON для создания риелтора:
     * {
     *   "firstName": "Иван",
     *   "lastName": "Иванов",
     *   "middleName": "Иванович",
     *   "phone": "+79161234567",
     *   "email": "ivan.ivanov@agency.com",
     *   "experience": 5,
     *   "commission": 3.5
     * }
     * 
     * Важно: При создании риелтора проверяется уникальность email и телефона
     * 
     * @param realtor данные нового риелтора из JSON в теле запроса
     * @return ResponseEntity с ID созданного риелтора и HTTP статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRealtor(@Valid @RequestBody Realtor realtor) {
        Long realtorId = realtorService.save(realtor);
        Map<String, Object> response = Map.of(
            "id", realtorId,
            "message", "Риелтор успешно создан"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновить данные существующего риелтора
     * 
     * HTTP метод: PUT
     * URL: /api/realtors/{id} (например: /api/realtors/123)
     * Content-Type: application/json
     * 
     * Пример JSON для обновления опыта работы и комиссии:
     * {
     *   "experience": 7,
     *   "commission": 4.0
     * }
     * 
     * Важно: При обновлении также проверяется уникальность email и телефона
     * 
     * @param id идентификатор риелтора для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return ResponseEntity с сообщением об успешном обновлении и HTTP статусом 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRealtor(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> updates) {
        
        boolean updated = realtorService.update(id, updates);
        
        if (updated) {
            Map<String, String> response = Map.of(
                "message", "Данные риелтора успешно обновлены"
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
     * Удалить риелтора по идентификатору
     * 
     * HTTP метод: DELETE
     * URL: /api/realtors/{id} (например: /api/realtors/123)
     * 
     * Если у риелтора есть связанные сделки, сервис выбросит BusinessRuleException
     * Это защищает от удаления риелторов, которые проводили сделки (сохраняет целостность данных)
     * 
     * @param id идентификатор риелтора для удаления
     * @return ResponseEntity с сообщением об успешном удалении и HTTP статусом 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRealtor(@PathVariable Long id) {
        boolean deleted = realtorService.deleteById(id);
        
        if (deleted) {
            Map<String, String> response = Map.of(
                "message", "Риелтор успешно удален"
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = Map.of(
                "message", "Риелтор не найден"
            );
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Найти риелторов по фамилии
     * 
     * HTTP метод: GET
     * URL: /api/realtors/search/by-lastname?lastName=Иванов
     * 
     * Поиск выполняется по частичному совпадению (LIKE %lastName%)
     * Результаты сортируются по фамилии и имени
     * 
     * @param lastName фамилия для поиска
     * @return ResponseEntity со списком найденных риелторов
     */
    @GetMapping("/search/by-lastname")
    public ResponseEntity<List<Realtor>> getRealtorsByLastName(@RequestParam String lastName) {
        List<Realtor> realtors = realtorService.findByLastName(lastName);
        return ResponseEntity.ok(realtors);
    }

    /**
     * Найти риелторов с опытом работы больше указанного значения
     * 
     * HTTP метод: GET
     * URL: /api/realtors/search/by-experience?minExperience=5
     * 
     * Поиск выполняется по условию experience > minExperience
     * Результаты сортируются по убыванию опыта (самые опытные первые)
     * 
     * @param minExperience минимальный опыт работы в годах
     * @return ResponseEntity со списком риелторов с подходящим опытом
     */
    @GetMapping("/search/by-experience")
    public ResponseEntity<List<Realtor>> getRealtorsByExperience(@RequestParam int minExperience) {
        List<Realtor> realtors = realtorService.findByExperienceGreaterThan(minExperience);
        return ResponseEntity.ok(realtors);
    }

    /**
     * Найти риелтора по номеру телефона
     * 
     * HTTP метод: GET
     * URL: /api/realtors/search/by-phone?phone=+79161234567
     * 
     * Поиск выполняется по точному совпадению номера телефона
     * Возвращает конкретного риелтора (телефон уникален)
     * 
     * @param phone номер телефона для поиска
     * @return ResponseEntity с найденным риелтором
     */
    @GetMapping("/search/by-phone")
    public ResponseEntity<Realtor> getRealtorByPhone(@RequestParam String phone) {
        Realtor realtor = realtorService.findByPhone(phone);
        return ResponseEntity.ok(realtor);
    }

    /**
     * Найти риелтора по адресу электронной почты
     * 
     * HTTP метод: GET
     * URL: /api/realtors/search/by-email?email=ivan.ivanov@agency.com
     * 
     * Поиск выполняется по точному совпадению email адреса
     * Возвращает конкретного риелтора (email уникален)
     * 
     * @param email адрес электронной почты для поиска
     * @return ResponseEntity с найденным риелтором
     */
    @GetMapping("/search/by-email")
    public ResponseEntity<Realtor> getRealtorByEmail(@RequestParam String email) {
        Realtor realtor = realtorService.findByEmail(email);
        return ResponseEntity.ok(realtor);
    }

    /**
     * Получить общее количество риелторов в базе данных
     * 
     * HTTP метод: GET
     * URL: /api/realtors/count
     * 
     * @return ResponseEntity с количеством риелторов
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getRealtorsCount() {
        int count = realtorService.getCount();
        
        Map<String, Integer> response = Map.of(
            "count", count
        );
        
        return ResponseEntity.ok(response);
    }
} 