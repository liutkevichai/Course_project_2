package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import ru.realestate.realestate_app.service.PaymentService;
import ru.realestate.realestate_app.service.DealService;
import ru.realestate.realestate_app.model.Payment;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentWebController {

    private final PaymentService paymentService;
    private final DealService dealService;

    public PaymentWebController(PaymentService paymentService, DealService dealService) {
        this.paymentService = paymentService;
        this.dealService = dealService;
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
        paymentService.createPayment(payment);
        return "redirect:/payments";
    }
}