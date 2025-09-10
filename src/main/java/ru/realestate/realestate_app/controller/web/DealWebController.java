package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import ru.realestate.realestate_app.model.dto.DealReportDto;
import ru.realestate.realestate_app.service.CsvExportService;
import ru.realestate.realestate_app.model.Deal;
import ru.realestate.realestate_app.service.DealService;
import ru.realestate.realestate_app.service.ClientService;
import ru.realestate.realestate_app.service.RealtorService;
import ru.realestate.realestate_app.service.PropertyService;
import ru.realestate.realestate_app.service.reference.DealTypeService;
import java.util.List;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/deals")
public class DealWebController {

    private final DealService dealService;
    private final ClientService clientService;
    private final RealtorService realtorService;
    private final PropertyService propertyService;
    private final DealTypeService dealTypeService;

    private final CsvExportService csvExportService;

    public DealWebController(DealService dealService, ClientService clientService,
                             RealtorService realtorService, PropertyService propertyService,
                             DealTypeService dealTypeService, CsvExportService csvExportService) {
        this.dealService = dealService;
        this.clientService = clientService;
        this.realtorService = realtorService;
        this.propertyService = propertyService;
        this.dealTypeService = dealTypeService;
        this.csvExportService = csvExportService;
    }

    @GetMapping
    public String getDealsPage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long realtorId,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long dealTypeId,
            Model model) {
        
        // Определяем, были ли предоставлены параметры поиска
        boolean hasSearchParameters = startDate != null || endDate != null || realtorId != null || clientId != null || dealTypeId != null;
        
        if (hasSearchParameters) {
            // Если параметры поиска есть, вызываем searchDeals
            model.addAttribute("deals", dealService.searchDeals(startDate, endDate, realtorId, clientId, dealTypeId));
        } else {
            // Если параметров нет, вызываем findAllForTable как раньше
            model.addAttribute("deals", dealService.findAllForTable());
        }
        
        // Добавляем параметры поиска обратно в модель
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("realtorId", realtorId);
        model.addAttribute("clientId", clientId);
        model.addAttribute("dealTypeId", dealTypeId);
        
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

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateDeal(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        dealService.update(id, updates);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        dealService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateDealReport() {
        try {
            // Получаем все сделки для отчета
            List<DealReportDto> deals = dealService.findAllForReport();

            // Экспортируем данные в CSV
            byte[] csvData = csvExportService.exportToCsv(deals, DealReportDto.class);

            // Формируем имя файла с текущей датой
            String fileName = "deals_report_" + java.time.LocalDate.now() + ".csv";

            // Возвращаем ответ с CSV данными
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .header("Content-Type", "text/csv; charset=utf-8")
                    .body(csvData);
        } catch (Exception _) {
            // В случае ошибки возвращаем пустой массив байтов
            return ResponseEntity.internalServerError().body(new byte[0]);
        }
    }
}