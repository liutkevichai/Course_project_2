package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки корневых веб-запросов.
 */
@Controller
public class RootController {

    /**
     * Обрабатывает GET-запросы к корневому URL ("/").
     *
     * @return имя представления "index".
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}