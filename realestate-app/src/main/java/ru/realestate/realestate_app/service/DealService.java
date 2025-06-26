package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.DealDao;
import ru.realestate.realestate_app.model.Deal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы со сделками
 * Содержит бизнес-логику для операций со сделками
 * Делегирует выполнение операций с базой данных в DealDao
 */
@Service
public class DealService {
    
    private final DealDao dealDao;

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param dealDao DAO для работы с данными сделок
     */
    public DealService(DealDao dealDao) {
        this.dealDao = dealDao;
    }

    /**
     * Получить все сделки, отсортированные по дате в убывающем порядке
     * @return список всех сделок
     */
    public List<Deal> findAll() {
        return dealDao.findAll();
    }

    /**
     * Найти сделку по уникальному идентификатору
     * @param id идентификатор сделки
     * @return объект сделки
     * @throws org.springframework.dao.EmptyResultDataAccessException если сделка не найдена
     */
    public Deal findById(Long id) {
        return dealDao.findById(id);
    }

    /**
     * Сохранить новую сделку в базе данных
     * @param deal объект сделки для сохранения
     * @return идентификатор созданной сделки
     */
    public Long save(Deal deal) {
        return dealDao.save(deal);
    }

    /**
     * Обновить данные существующей сделки
     * @param id идентификатор сделки для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     */
    public boolean update(Long id, Map<String, Object> updates) {
        return dealDao.update(id, updates);
    }

    /**
     * Удалить сделку по идентификатору
     * @param id идентификатор сделки для удаления
     * @return true если удаление прошло успешно, false если сделка не найдена
     */
    public boolean deleteById(Long id) {
        return dealDao.deleteById(id);
    }

    /**
     * Найти сделки по конкретной дате
     * @param date дата совершения сделки
     * @return список сделок, совершенных в указанную дату, отсортированный по убыванию стоимости
     */
    public List<Deal> findByDate(LocalDate date) {
        return dealDao.findByDate(date);
    }

    /**
     * Найти сделки в указанном диапазоне дат
     * @param startDate начальная дата периода (включительно)
     * @param endDate конечная дата периода (включительно)
     * @return список сделок в указанном диапазоне дат, отсортированный по убыванию даты
     */
    public List<Deal> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return dealDao.findByDateRange(startDate, endDate);
    }

    /**
     * Найти сделки конкретного риелтора
     * @param realtorId идентификатор риелтора
     * @return список сделок указанного риелтора, отсортированный по убыванию даты
     */
    public List<Deal> findByRealtorId(Integer realtorId) {
        return dealDao.findByRealtorId(realtorId);
    }

    /**
     * Найти сделки конкретного клиента
     * @param clientId идентификатор клиента
     * @return список сделок указанного клиента, отсортированный по убыванию даты
     */
    public List<Deal> findByClientId(Integer clientId) {
        return dealDao.findByClientId(clientId);
    }

    /**
     * Найти сделки по конкретному объекту недвижимости
     * @param propertyId идентификатор объекта недвижимости
     * @return список сделок по указанному объекту недвижимости, отсортированный по убыванию даты
     */
    public List<Deal> findByPropertyId(Integer propertyId) {
        return dealDao.findByPropertyId(propertyId);
    }

    /**
     * Найти сделки по типу сделки
     * @param dealTypeId идентификатор типа сделки
     * @return список сделок указанного типа, отсортированный по убыванию даты
     */
    public List<Deal> findByDealTypeId(Integer dealTypeId) {
        return dealDao.findByDealTypeId(dealTypeId);
    }

    /**
     * Найти сделки в указанном ценовом диапазоне
     * @param minCost минимальная стоимость сделки
     * @param maxCost максимальная стоимость сделки
     * @return список сделок в ценовом диапазоне, отсортированный по убыванию стоимости
     */
    public List<Deal> findByCostRange(BigDecimal minCost, BigDecimal maxCost) {
        return dealDao.findByCostRange(minCost, maxCost);
    }

    /**
     * Получить общую сумму всех сделок
     * @return общая сумма сделок или 0 если сделок нет
     */
    public BigDecimal getTotalDealsAmount() {
        return dealDao.getTotalDealsAmount();
    }

    /**
     * Получить общее количество сделок в базе данных
     * @return количество сделок
     */
    public int getCount() {
        return dealDao.getCount();
    }
}
