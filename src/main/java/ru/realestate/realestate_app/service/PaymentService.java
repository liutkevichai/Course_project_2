package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;
import ru.realestate.realestate_app.dao.PaymentDao;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.Payment;
import ru.realestate.realestate_app.model.dto.PaymentTableDto;

import java.time.LocalDate;
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

    public List<Payment> findAll() {
        return paymentDao.findAll();
    }

    public Payment findById(Long id) {
        return paymentDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment", id));
    }

    public List<Payment> findByDealId(Long dealId) {
        return paymentDao.findByDealId(dealId);
    }

    public Payment save(Payment payment) {
        // Здесь могут быть бизнес-правила, например, проверка, что сумма платежей не превышает сумму сделки
        return paymentDao.save(payment);
    }

    public Payment update(Long id, Payment paymentDetails) {
        Payment payment = findById(id);
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setAmount(paymentDetails.getAmount());
        payment.setIdDeal(paymentDetails.getIdDeal());
        return paymentDao.update(payment);
    }

    public void deleteById(Long id) {
        // Проверяем, существует ли платеж перед удалением
        findById(id);
        paymentDao.delete(id);
    }
    
    /**
     * Получить все платежи с детальной информацией для табличного отображения
     * @return список всех платежей с дополнительной информацией
     */
    public List<PaymentTableDto> findAllWithDetails() {
        return paymentDao.findAllWithDetails();
    }

    /**
     * Осуществляет поиск платежей по заданным критериям.
     *
     * @param dealId     ID сделки для фильтрации.
     * @param startDate  Начальная дата для поиска.
     * @param endDate    Конечная дата для поиска.
     * @return Список найденных платежей в формате PaymentTableDto.
     */
    public List<PaymentTableDto> searchPayments(Long dealId, LocalDate startDate, LocalDate endDate) {
        try {
            return paymentDao.searchPayments(dealId, startDate, endDate);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Payment", null);
            ExceptionHandler.logException(re, "Ошибка при поиске платежей");
            throw re;
        }
    }
}