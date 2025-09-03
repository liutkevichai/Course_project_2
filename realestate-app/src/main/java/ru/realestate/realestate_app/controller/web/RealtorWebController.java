package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.realestate.realestate_app.model.Realtor;
import ru.realestate.realestate_app.service.RealtorService;

@Controller
@RequestMapping("/realtors")
public class RealtorWebController {

    private final RealtorService realtorService;

    public RealtorWebController(RealtorService realtorService) {
        this.realtorService = realtorService;
    }

    @GetMapping
    public String getRealtorsPage(Model model) {
        model.addAttribute("realtors", realtorService.findAll());
        model.addAttribute("newRealtor", new Realtor());
        model.addAttribute("pageTitle", "Риелторы");
        return "realtors";
    }
    
    @PostMapping("/add")
    public String addRealtor(@ModelAttribute Realtor realtor) {
        realtorService.save(realtor);
        return "redirect:/realtors";
    }
}