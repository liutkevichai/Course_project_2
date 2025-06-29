package ru.realestate.realestate_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.realestate.realestate_app.model.Client;
import ru.realestate.realestate_app.service.ClientService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для работы с клиентами агентства недвижимости
 * 
 * Этот контроллер предоставляет HTTP API для выполнения операций CRUD над клиентами.
 * 
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    /**
     * Конструктор контроллера с внедрением зависимости
     * 
     * @param clientService сервис для работы с клиентами
     */
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Получить список всех клиентов
     * 
     * HTTP метод: GET
     * URL: /api/clients
     * 
     * @return ResponseEntity со списком всех клиентов и HTTP статусом 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }

    /**
     * Получить клиента по уникальному идентификатору
     * 
     * HTTP метод: GET
     * URL: /api/clients/{id} (например: /api/clients/123)
     * 
     * @param id идентификатор клиента
     * @return ResponseEntity с данными клиента и HTTP статусом 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    /**
     * Создать нового клиента
     * 
     * HTTP метод: POST
     * URL: /api/clients
     * Content-Type: application/json
     * 
     * @param client данные нового клиента из JSON в теле запроса
     * @return ResponseEntity с ID созданного клиента и HTTP статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createClient(@Valid @RequestBody Client client) {
        Long clientId = clientService.save(client);
        
        // Создается ответ с информацией о созданном клиенте
        Map<String, Object> response = Map.of(
            "id", clientId,
            "message", "Клиент успешно создан"
        );
        
        // HttpStatus.CREATED означает, что ресурс был успешно создан
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновить данные существующего клиента
     * 
     * HTTP метод: PUT
     * URL: /api/clients/{id} (например: /api/clients/123)
     * Content-Type: application/json
     * 
     * @param id идентификатор клиента для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return ResponseEntity с сообщением об успешном обновлении и HTTP статусом 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateClient(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> updates) {
        
        boolean updated = clientService.update(id, updates);
        
        if (updated) {
            Map<String, String> response = Map.of(
                "message", "Данные клиента успешно обновлены"
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
     * Удалить клиента по идентификатору
     * 
     * HTTP метод: DELETE
     * URL: /api/clients/{id} (например: /api/clients/123)
     * 
     * @param id идентификатор клиента для удаления
     * @return ResponseEntity с сообщением об успешном удалении и HTTP статусом 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteClient(@PathVariable Long id) {
        boolean deleted = clientService.deleteById(id);
        
        if (deleted) {
            Map<String, String> response = Map.of(
                "message", "Клиент успешно удален"
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = Map.of(
                "message", "Клиент не найден"
            );
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Найти клиентов по фамилии
     * 
     * HTTP метод: GET
     * URL: /api/clients/search/by-lastname?lastName=Иванов
     * 
     * Поиск выполняется по частичному совпадению (LIKE %lastName%)
     * 
     * @param lastName фамилия для поиска
     * @return ResponseEntity со списком найденных клиентов
     */
    @GetMapping("/search/by-lastname")
    public ResponseEntity<List<Client>> getClientsByLastName(@RequestParam String lastName) {
        List<Client> clients = clientService.findByLastName(lastName);
        return ResponseEntity.ok(clients);
    }

    /**
     * Найти клиента по номеру телефона
     * 
     * HTTP метод: GET
     * URL: /api/clients/search/by-phone?phone=+79161234567
     * 
     * Поиск выполняется по точному совпадению номера телефона
     * 
     * @param phone номер телефона для поиска
     * @return ResponseEntity с найденным клиентом
     */
    @GetMapping("/search/by-phone")
    public ResponseEntity<Client> getClientByPhone(@RequestParam String phone) {
        Client client = clientService.findByPhone(phone);
        return ResponseEntity.ok(client);
    }

    /**
     * Найти клиента по адресу электронной почты
     * 
     * HTTP метод: GET
     * URL: /api/clients/search/by-email?email=client@example.com
     * 
     * Поиск выполняется по точному совпадению email адреса
     * 
     * @param email адрес электронной почты для поиска
     * @return ResponseEntity с найденным клиентом
     */
    @GetMapping("/search/by-email")
    public ResponseEntity<Client> getClientByEmail(@RequestParam String email) {
        Client client = clientService.findByEmail(email);
        return ResponseEntity.ok(client);
    }

    /**
     * Получить общее количество клиентов в базе данных
     * 
     * HTTP метод: GET
     * URL: /api/clients/count
     * 
     * @return ResponseEntity с количеством клиентов
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getClientsCount() {
        int count = clientService.getCount();
        
        Map<String, Integer> response = Map.of(
            "count", count
        );
        
        return ResponseEntity.ok(response);
    }
} 