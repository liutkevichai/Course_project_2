package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.realestate.realestate_app.model.Deal;
import ru.realestate.realestate_app.service.DealService;
import ru.realestate.realestate_app.service.ClientService;
import ru.realestate.realestate_app.service.RealtorService;
import ru.realestate.realestate_app.service.PropertyService;
import ru.realestate.realestate_app.service.reference.DealTypeService;

@Controller
@RequestMapping("/deals")
public class DealWebController {

    private final DealService dealService;
    private final ClientService clientService;
    private final RealtorService realtorService;
    private final PropertyService propertyService;
    private final DealTypeService dealTypeService;

    public DealWebController(DealService dealService, ClientService clientService,
                             RealtorService realtorService, PropertyService propertyService,
                             DealTypeService dealTypeService) {
        this.dealService = dealService;
        this.clientService = clientService;
        this.realtorService = realtorService;
        this.propertyService = propertyService;
        this.dealTypeService = dealTypeService;
    }

    @GetMapping
    public String getDealsPage(Model model) {
        model.addAttribute("deals", dealService.findAllForTable()); // Use a DTO
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("realtors", realtorService.findAll());
        model.addAttribute("properties", propertyService.findAllForTable());
        model.addAttribute("dealTypes", dealTypeService.findAll());
        model.addAttribute("pageTitle", "Сделки");
        model.addAttribute("newDeal", new Deal()); // Пустой объект для формы добавления
        return "deals";
    }

    /**
     * Обрабатывает POST-запрос на добавление новой сделки
     * @param deal объект сделки, заполненный из формы
     * @return перенаправление на страницу списка сделок
     */
    @PostMapping("/add")
    public String addDeal(@ModelAttribute Deal deal) {
        dealService.save(deal);
        return "redirect:/deals";
    }
}