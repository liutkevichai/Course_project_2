package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        model.addAttribute("pageTitle", "Клиенты");
        return "clients"; // Это будет преобразовано в clients.html
    }
}