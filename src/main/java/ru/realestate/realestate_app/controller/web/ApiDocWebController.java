package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.realestate.realestate_app.service.ApiDocService;

/**
 * Веб-контроллер для отображения документации API
 */
@Controller
@RequestMapping("/api-docs")
public class ApiDocWebController {
    
    private final ApiDocService apiDocService;
    
    public ApiDocWebController(ApiDocService apiDocService) {
        this.apiDocService = apiDocService;
    }
    
    /**
     * Обрабатывает GET-запросы по пути /api-docs
     *
     * @param model модель для передачи данных в представление
     * @return имя представления api-docs
     */
    @GetMapping
    public String getApiDocs(Model model) {
        String apiEndpointsHtml = apiDocService.getApiEndpointsHtml();
        model.addAttribute("apiEndpointsHtml", apiEndpointsHtml);
        return "api-docs";
    }
}