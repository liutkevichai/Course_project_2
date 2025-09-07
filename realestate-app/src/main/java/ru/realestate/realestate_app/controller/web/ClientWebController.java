package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.realestate.realestate_app.model.Client;
import ru.realestate.realestate_app.service.ClientService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clients")
public class ClientWebController {

    private final ClientService clientService;

    public ClientWebController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getClientsPage(Model model,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) String phone) {
        // 1. Проверяем, присутствует ли хотя бы один параметр поиска.
        boolean hasSearchParams = (lastName != null && !lastName.isEmpty()) ||
                                  (email != null && !email.isEmpty()) ||
                                  (phone != null && !phone.isEmpty());

        List<Client> clients;
        if (hasSearchParams) {
            // 2. Вызываем новый метод сервиса для сложного поиска.
            clients = clientService.searchClients(lastName, email, phone);
        } else {
            // 3. Если параметры не предоставлены, получаем всех клиентов.
            clients = clientService.findAll();
        }

        model.addAttribute("clients", clients);
        model.addAttribute("newClient", new Client()); // Передаем пустой объект для формы добавления
        model.addAttribute("pageTitle", "Клиенты");
        // Добавляем параметры поиска обратно в модель, чтобы сохранить их в форме после отправки.
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        return "clients";
    }
    
    @PostMapping("/add")
    public String addClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients"; // Перенаправляем на страницу списка клиентов
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        clientService.update(id, updates);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}