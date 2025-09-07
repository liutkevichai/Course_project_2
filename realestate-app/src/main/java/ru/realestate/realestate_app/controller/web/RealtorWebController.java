package ru.realestate.realestate_app.controller.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String getRealtorsPage(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer minExperience,
            Model model) {
        model.addAttribute("realtors", realtorService.searchRealtors(lastName, email, phone, minExperience));
        model.addAttribute("newRealtor", new Realtor());
        model.addAttribute("pageTitle", "Риелторы");
        return "realtors";
    }
    
    @PostMapping("/add")
    public String addRealtor(@ModelAttribute Realtor realtor) {
        realtorService.save(realtor);
        return "redirect:/realtors";
    }
    
    @PostMapping("/update/{id}")
    public String updateRealtor(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        realtorService.update(id, updates);
        return "redirect:/realtors";
    }
}