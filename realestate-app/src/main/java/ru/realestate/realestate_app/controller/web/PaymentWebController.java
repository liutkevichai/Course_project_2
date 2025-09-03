package ru.realestate.realestate_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.realestate.realestate_app.service.PaymentService;
import ru.realestate.realestate_app.service.DealService;
import ru.realestate.realestate_app.model.Payment;

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
        public String getPaymentsPage(Model model) {
            model.addAttribute("payments", paymentService.findAllWithDetails());
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