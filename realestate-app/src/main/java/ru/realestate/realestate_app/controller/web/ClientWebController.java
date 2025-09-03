package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.realestate.realestate_app.model.Client;
import ru.realestate.realestate_app.service.ClientService;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientWebController {

    private final ClientService clientService;

    public ClientWebController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getClientsPage(Model model) {
        // Получаем список всех клиентов
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("newClient", new Client()); // Передаем пустой объект для формы добавления
        model.addAttribute("pageTitle", "Клиенты");
        return "clients";
    }
    
    @PostMapping("/add")
    public String addClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients"; // Перенаправляем на страницу списка клиентов
    }
}