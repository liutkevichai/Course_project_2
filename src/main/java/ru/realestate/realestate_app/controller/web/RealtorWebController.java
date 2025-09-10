package ru.realestate.realestate_app.controller.web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
import ru.realestate.realestate_app.service.CsvExportService;

import java.util.List;

@Controller
@RequestMapping("/realtors")
public class RealtorWebController {

    private final RealtorService realtorService;
    private final CsvExportService csvExportService;

    public RealtorWebController(RealtorService realtorService, CsvExportService csvExportService) {
        this.realtorService = realtorService;
        this.csvExportService = csvExportService;
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
    
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteRealtor(@PathVariable Long id) {
        realtorService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateRealtorReport() {
        try {
            // Получаем всех риелторов
            List<Realtor> realtors = realtorService.findAll();
            
            // Экспортируем риелторов в CSV
            byte[] csvData = csvExportService.exportToCsv(realtors, Realtor.class);
            
            // Формируем имя файла с текущей датой
            String fileName = "realtors_report_" + java.time.LocalDate.now() + ".csv";

            // Возвращаем CSV-файл с правильными заголовками
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .header("Content-Type", "text/csv; charset=utf-8")
                    .body(csvData);
        } catch (Exception _) {
            // В случае ошибки возвращаем пустой массив
            return ResponseEntity.internalServerError().body(new byte[0]);
        }
    }
}