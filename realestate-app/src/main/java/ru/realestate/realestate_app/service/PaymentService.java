package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;
import ru.realestate.realestate_app.dao.PaymentDao;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.model.Payment;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;

import java.util.List;

/**
 * Сервис для управления платежами.
 * Обеспечивает бизнес-логику и взаимодействие с {@link PaymentDao}.
 */
@Service
public class PaymentService {
    private final PaymentDao paymentDao;

    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public List<Payment> getAllPayments() {
        return paymentDao.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment", id));
    }

    public List<Payment> getPaymentsByDealId(Long dealId) {
        return paymentDao.findByDealId(dealId);
    }

    public Payment createPayment(Payment payment) {
        // Здесь могут быть бизнес-правила, например, проверка, что сумма платежей не превышает сумму сделки
        return paymentDao.save(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = getPaymentById(id);
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setAmount(paymentDetails.getAmount());
        payment.setIdDeal(paymentDetails.getIdDeal());
        return paymentDao.update(payment);
    }

    public void deletePayment(Long id) {
        // Проверяем, существует ли платеж перед удалением
        getPaymentById(id);
        paymentDao.delete(id);
    }
    
    /**
     * Получить все платежи с детальной информацией для табличного отображения
     * @return список всех платежей с дополнительной информацией
     */
    public List<PaymentTableDto> findAllWithDetails() {
        return paymentDao.findAllWithDetails();
    }
}