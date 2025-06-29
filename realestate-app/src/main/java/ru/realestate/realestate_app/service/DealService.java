package ru.realestate.realestate_app.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.DealDao;
import ru.realestate.realestate_app.dao.PropertyDao;
import ru.realestate.realestate_app.exception.BusinessRuleException;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.Deal;
import ru.realestate.realestate_app.model.Property;
import ru.realestate.realestate_app.model.dto.DealWithDetailsDto;
import ru.realestate.realestate_app.model.dto.DealTableDto;

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
    private final PropertyDao propertyDao; // Добавляем зависимость для проверок

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param dealDao DAO для работы с данными сделок
     * @param propertyDao DAO для работы с данными объектов недвижимости
     */
    public DealService(DealDao dealDao, PropertyDao propertyDao) {
        this.dealDao = dealDao;
        this.propertyDao = propertyDao;
    }

    /**
     * Получить все сделки, отсортированные по дате в убывающем порядке
     * @return список всех сделок
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findAll() {
        try {
            return dealDao.findAll();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех сделок");
            throw re;
        }
    }

    /**
     * Найти сделку по уникальному идентификатору
     * @param id идентификатор сделки
     * @return объект сделки
     * @throws EntityNotFoundException если сделка не найдена
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Deal findById(Long id) {
        try {
            return dealDao.findById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", id);
            ExceptionHandler.logException(re, "Ошибка при поиске сделки по id: " + id);
            throw re;
        }
    }

    /**
     * Сохранить новую сделку в базе данных
     * Выполняет бизнес-проверки перед сохранением
     * @param deal объект сделки для сохранения
     * @return идентификатор созданной сделки
     * @throws ValidationException если данные сделки не прошли валидацию
     * @throws BusinessRuleException если нарушены бизнес-правила
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Long save(Deal deal) {
        // Валидация входных данных
        validateDeal(deal);
        
        // Бизнес-проверки
        validateBusinessRules(deal);
        
        try {
            return dealDao.save(deal);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "INSERT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при сохранении сделки");
            throw re;
        }
    }

    /**
     * Обновить данные существующей сделки
     * @param id идентификатор сделки для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws EntityNotFoundException если сделка не найдена
     * @throws ValidationException если данные не прошли валидацию
     * @throws BusinessRuleException если нарушены бизнес-правила
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем существование сделки
        findById(id);
        
        // Валидация обновлений
        validateUpdates(updates);
        
        // Бизнес-проверки для обновлений
        validateUpdateBusinessRules(id, updates);
        
        try {
            return dealDao.update(id, updates);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "UPDATE", "Deal", id);
            ExceptionHandler.logException(re, "Ошибка при обновлении сделки с id: " + id);
            throw re;
        }
    }

    /**
     * Удалить сделку по идентификатору
     * @param id идентификатор сделки для удаления
     * @return true если удаление прошло успешно, false если сделка не найдена
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean deleteById(Long id) {
        try {
            return dealDao.deleteById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "DELETE", "Deal", id);
            ExceptionHandler.logException(re, "Ошибка при удалении сделки с id: " + id);
            throw re;
        }
    }

    /**
     * Найти сделки по конкретной дате
     * @param date дата совершения сделки
     * @return список сделок, совершенных в указанную дату, отсортированный по убыванию стоимости
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByDate(LocalDate date) {
        try {
            return dealDao.findByDate(date);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по дате: " + date);
            throw re;
        }
    }

    /**
     * Найти сделки в указанном диапазоне дат
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return список сделок в указанном диапазоне дат, отсортированный по убыванию даты
     * @throws ValidationException если диапазон дат указан некорректно
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return dealDao.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по диапазону дат: " + startDate + " - " + endDate);
            throw re;
        }
    }

    /**
     * Найти сделки конкретного риелтора
     * @param realtorId идентификатор риелтора
     * @return список сделок указанного риелтора, отсортированный по убыванию даты
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByRealtorId(Long realtorId) {
        try {
            return dealDao.findByRealtorId(realtorId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по риелтору с id: " + realtorId);
            throw re;
        }
    }

    /**
     * Найти сделки конкретного клиента
     * @param clientId идентификатор клиента
     * @return список сделок указанного клиента, отсортированный по убыванию даты
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByClientId(Long clientId) {
        try {
            return dealDao.findByClientId(clientId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по клиенту с id: " + clientId);
            throw re;
        }
    }

    /**
     * Найти сделки по конкретному объекту недвижимости
     * @param propertyId идентификатор объекта недвижимости
     * @return список сделок по указанному объекту недвижимости, отсортированный по убыванию даты
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByPropertyId(Long propertyId) {
        try {
            return dealDao.findByPropertyId(propertyId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по объекту недвижимости с id: " + propertyId);
            throw re;
        }
    }

    /**
     * Найти сделки по типу сделки
     * @param dealTypeId идентификатор типа сделки
     * @return список сделок указанного типа, отсортированный по убыванию даты
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByDealTypeId(Long dealTypeId) {
        try {
            return dealDao.findByDealTypeId(dealTypeId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по типу с id: " + dealTypeId);
            throw re;
        }
    }

    /**
     * Найти сделки в указанном ценовом диапазоне
     * @param minCost минимальная стоимость сделки
     * @param maxCost максимальная стоимость сделки
     * @return список сделок в ценовом диапазоне, отсортированный по убыванию стоимости
     * @throws ValidationException если ценовой диапазон указан некорректно
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Deal> findByCostRange(BigDecimal minCost, BigDecimal maxCost) {
        try {
            return dealDao.findByCostRange(minCost, maxCost);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок по ценовому диапазону: " + minCost + " - " + maxCost);
            throw re;
        }
    }

    /**
     * Получить общую сумму всех сделок
     * @return общая сумма сделок или 0 если сделок нет
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public BigDecimal getTotalDealsAmount() {
        try {
            return dealDao.getTotalDealsAmount();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при получении общей суммы сделок");
            throw re;
        }
    }

    /**
     * Получить общее количество сделок в базе данных
     * @return количество сделок
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public int getCount() {
        try {
            return dealDao.getCount();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при подсчете количества сделок");
            throw re;
        }
    }

    /**
     * Валидация данных сделки
     * @param deal объект сделки для валидации
     * @throws ValidationException если данные не прошли валидацию
     */
    private void validateDeal(Deal deal) {
        if (deal == null) {
            throw new ValidationException("deal", "Объект сделки не может быть null");
        }
        // Остальная валидация выполняется в DAO
    }

    /**
     * Валидация бизнес-правил для сделки
     * @param deal объект сделки для проверки
     * @throws BusinessRuleException если нарушены бизнес-правила
     */
    private void validateBusinessRules(Deal deal) {
        try {
            // Проверяем существование объекта недвижимости
            Property property = propertyDao.findById(deal.getIdProperty());
            
            // Проверка: стоимость сделки ≤ стоимость объекта недвижимости
            if (deal.getDealCost().compareTo(property.getCost()) > 0) {
                throw new BusinessRuleException(
                    "DEAL_COST_EXCEEDS_PROPERTY_COST",
                    String.format("Стоимость сделки (%.2f) не может превышать стоимость объекта недвижимости (%.2f)", 
                                 deal.getDealCost(), property.getCost())
                );
            }
            
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Property", deal.getIdProperty());
        } catch (BusinessRuleException e) {
            throw e; // Перебрасываем бизнес-исключения как есть
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", deal.getIdProperty());
            ExceptionHandler.logException(re, "Ошибка при проверке бизнес-правил для сделки");
            throw re;
        }
    }

    /**
     * Валидация обновлений сделки
     * @param updates карта обновлений
     * @throws ValidationException если обновления не прошли валидацию
     */
    private void validateUpdates(Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            throw new ValidationException("updates", "Данные для обновления не могут быть пустыми");
        }
        // Остальная валидация выполняется в DAO
    }

    /**
     * Валидация бизнес-правил для обновлений сделки
     * @param dealId идентификатор сделки
     * @param updates карта обновлений
     * @throws BusinessRuleException если нарушены бизнес-правила
     */
    private void validateUpdateBusinessRules(Long dealId, Map<String, Object> updates) {
        try {
            // Получаем текущую сделку
            Deal currentDeal = dealDao.findById(dealId);
            
            // Проверяем обновление стоимости
            if (updates.containsKey("dealCost")) {
                BigDecimal newCost = (BigDecimal) updates.get("dealCost");
                Property property = propertyDao.findById(currentDeal.getIdProperty());
                
                if (newCost.compareTo(property.getCost()) > 0) {
                    throw new BusinessRuleException(
                        "DEAL_COST_EXCEEDS_PROPERTY_COST",
                        String.format("Стоимость сделки (%.2f) не может превышать стоимость объекта недвижимости (%.2f)", 
                                     newCost, property.getCost())
                    );
                }
            }
            
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Deal", dealId);
        } catch (BusinessRuleException e) {
            throw e; // Перебрасываем бизнес-исключения как есть
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", dealId);
            ExceptionHandler.logException(re, "Ошибка при проверке бизнес-правил для обновления сделки с id: " + dealId);
            throw re;
        }
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все сделки с детальной информацией (включая данные клиента, риелтора и недвижимости)
     * Использует JOIN запросы для оптимизации производительности
     * @return список всех сделок с полной информацией, отсортированный по дате в убывающем порядке
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealWithDetailsDto> findAllWithDetails() {
        try {
            return dealDao.findAllWithDetails();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех сделок с детальной информацией");
            throw re;
        }
    }

    /**
     * Найти сделку по идентификатору с детальной информацией
     * Включает полную информацию о клиенте, риелторе и объекте недвижимости
     * @param id идентификатор сделки
     * @return объект сделки с детальной информацией
     * @throws EntityNotFoundException если сделка не найдена
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public DealWithDetailsDto findByIdWithDetails(Long id) {
        try {
            return dealDao.findByIdWithDetails(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", id);
            ExceptionHandler.logException(re, "Ошибка при поиске сделки с детальной информацией по id: " + id);
            throw re;
        }
    }

    /**
     * Получить все сделки в табличном формате для отображения в списках
     * Компактное представление с основной информацией
     * @return список сделок в табличном формате, отсортированный по дате в убывающем порядке
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealTableDto> findAllForTable() {
        try {
            return dealDao.findAllForTable();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех сделок в табличном формате");
            throw re;
        }
    }

    /**
     * Найти сделки по дате с детальной информацией
     * @param date дата совершения сделки
     * @return список сделок с полной информацией, отсортированный по убыванию стоимости
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealWithDetailsDto> findByDateWithDetails(LocalDate date) {
        try {
            return dealDao.findByDateWithDetails(date);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок с детальной информацией по дате: " + date);
            throw re;
        }
    }

    /**
     * Найти сделки в диапазоне дат с детальной информацией
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return список сделок с полной информацией в указанном диапазоне дат, отсортированный по убыванию даты
     * @throws ValidationException если диапазон дат указан некорректно
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealWithDetailsDto> findByDateRangeWithDetails(LocalDate startDate, LocalDate endDate) {
        try {
            return dealDao.findByDateRangeWithDetails(startDate, endDate);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок с детальной информацией по диапазону дат: " + startDate + " - " + endDate);
            throw re;
        }
    }

    /**
     * Найти сделки конкретного риелтора с детальной информацией
     * @param realtorId идентификатор риелтора
     * @return список сделок с полной информацией указанного риелтора, отсортированный по убыванию даты
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealWithDetailsDto> findByRealtorIdWithDetails(Long realtorId) {
        try {
            return dealDao.findByRealtorIdWithDetails(realtorId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок с детальной информацией по риелтору с id: " + realtorId);
            throw re;
        }
    }

    /**
     * Найти сделки конкретного клиента с детальной информацией
     * @param clientId идентификатор клиента
     * @return список сделок с полной информацией указанного клиента, отсортированный по убыванию даты
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealWithDetailsDto> findByClientIdWithDetails(Long clientId) {
        try {
            return dealDao.findByClientIdWithDetails(clientId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", null);
            ExceptionHandler.logException(re, "Ошибка при поиске сделок с детальной информацией по клиенту с id: " + clientId);
            throw re;
        }
    }
}
