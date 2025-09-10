package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import ru.realestate.realestate_app.model.dto.PaymentReportDto;
import ru.realestate.realestate_app.service.CsvExportService;
import ru.realestate.realestate_app.service.PaymentService;
import ru.realestate.realestate_app.service.DealService;
import ru.realestate.realestate_app.model.Payment;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@Controller
@RequestMapping("/payments")
public class PaymentWebController {

    private final PaymentService paymentService;
    private final DealService dealService;

    private final CsvExportService csvExportService;

    public PaymentWebController(PaymentService paymentService, DealService dealService, CsvExportService csvExportService) {
        this.paymentService = paymentService;
        this.dealService = dealService;
        this.csvExportService = csvExportService;
    }

    @GetMapping
    public String getPaymentsPage(Model model,
                               @RequestParam(required = false) Long dealId,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        boolean hasSearchParams = dealId != null || startDate != null || endDate != null;

        List<PaymentTableDto> payments;
        if (hasSearchParams) {
            payments = paymentService.searchPayments(dealId, startDate, endDate);
        } else {
            payments = paymentService.findAllWithDetails();
        }

        model.addAttribute("payments", payments);
        model.addAttribute("deals", dealService.findAllForTable());
        model.addAttribute("newPayment", new Payment());
        model.addAttribute("pageTitle", "Платежи");


        return "payments";
    }
                
    @PostMapping("/add")
    public String addPayment(@ModelAttribute Payment payment) {
        paymentService.save(payment);
        return "redirect:/payments";
    }
    
    @PostMapping("/update/{id}")
    public String updatePayment(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        // Создаем объект Payment с обновленными данными
        Payment paymentDetails = new Payment();
        
        // Устанавливаем значения из updates
        if (updates.containsKey("paymentDate")) {
            paymentDetails.setPaymentDate(LocalDate.parse((String) updates.get("paymentDate")));
        }
        if (updates.containsKey("amount")) {
            paymentDetails.setAmount(BigDecimal.valueOf(((Number) updates.get("amount")).doubleValue()));
        }
        if (updates.containsKey("idDeal")) {
            paymentDetails.setIdDeal(((Number) updates.get("idDeal")).longValue());
        }
        
        paymentService.update(id, paymentDetails);
        return "redirect:/payments";
    }
    
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generatePaymentReport() {
        try {
            // Получаем все платежи для отчета
            List<PaymentReportDto> payments = paymentService.findAllForReport();

            // Экспортируем данные в CSV
            byte[] csvData = csvExportService.exportToCsv(payments, PaymentReportDto.class);

            // Формируем имя файла с текущей датой
            String fileName = "payments_report_" + java.time.LocalDate.now() + ".csv";

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