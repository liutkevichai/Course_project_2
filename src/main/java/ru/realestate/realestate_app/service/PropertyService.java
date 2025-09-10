package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.DealDao;
import ru.realestate.realestate_app.dao.PropertyDao;
import ru.realestate.realestate_app.exception.BusinessRuleException;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.Property;
import ru.realestate.realestate_app.model.dto.PropertyWithDetailsDto;
import ru.realestate.realestate_app.model.dto.PropertyTableDto;
import ru.realestate.realestate_app.model.dto.PropertyReportDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с объектами недвижимости
 * Содержит бизнес-логику для операций с объектами недвижимости
 * Делегирует выполнение операций с базой данных в PropertyDao
 */
@Service
public class PropertyService {
    
    private final PropertyDao propertyDao;
    private final DealDao dealDao; // Добавляем зависимость для проверок

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param propertyDao DAO для работы с данными объектов недвижимости
     * @param dealDao DAO для работы с данными сделок
     */
    public PropertyService(PropertyDao propertyDao, DealDao dealDao) {
        this.propertyDao = propertyDao;
        this.dealDao = dealDao;
    }

    /**
     * Получить все объекты недвижимости, отсортированные по стоимости в убывающем порядке
     * @return список всех объектов недвижимости
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Property> findAll() {
        try {
            return propertyDao.findAll();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех объектов недвижимости");
            throw re;
        }
    }

    /**
     * Найти объект недвижимости по уникальному идентификатору
     * @param id идентификатор объекта недвижимости
     * @return объект недвижимости
     * @throws EntityNotFoundException если объект недвижимости не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Property findById(Long id) {
        try {
            return propertyDao.findById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", id);
            ExceptionHandler.logException(re, "Ошибка при поиске объекта недвижимости по id: " + id);
            throw re;
        }
    }

    /**
     * Сохранить новый объект недвижимости в базе данных
     * @param property объект недвижимости для сохранения
     * @return идентификатор созданного объекта недвижимости
     * @throws ValidationException если данные объекта недвижимости не прошли валидацию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Long save(Property property) {
        // Валидация входных данных
        validateProperty(property);
        
        try {
            return propertyDao.save(property);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "INSERT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при сохранении объекта недвижимости");
            throw re;
        }
    }

    /**
     * Обновить данные существующего объекта недвижимости
     * @param id идентификатор объекта недвижимости для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws EntityNotFoundException если объект недвижимости не найден
     * @throws ValidationException если данные не прошли валидацию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем существование объекта недвижимости
        findById(id);
        
        // Валидация обновлений
        validateUpdates(updates);
        
        try {
            return propertyDao.update(id, updates);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "UPDATE", "Property", id);
            ExceptionHandler.logException(re, "Ошибка при обновлении объекта недвижимости с id: " + id);
            throw re;
        }
    }

    /**
     * Удалить объект недвижимости по идентификатору
     * Проверяет, что у объекта недвижимости нет связанных сделок
     * @param id идентификатор объекта недвижимости для удаления
     * @return true если удаление прошло успешно, false если объект недвижимости не найден
     * @throws BusinessRuleException если у объекта недвижимости есть связанные сделки
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean deleteById(Long id) {
        // Проверяем, есть ли связанные сделки
        if (hasRelatedDeals(id)) {
            throw new BusinessRuleException(
                "PROPERTY_HAS_RELATED_DEALS",
                String.format("Нельзя удалить объект недвижимости с ID %d, так как у него есть связанные сделки", id)
            );
        }
        
        try {
            return propertyDao.deleteById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "DELETE", "Property", id);
            ExceptionHandler.logException(re, "Ошибка при удалении объекта недвижимости с id: " + id);
            throw re;
        }
    }

    /**
     * Найти объекты недвижимости по ценовому диапазону
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список объектов недвижимости в указанном ценовом диапазоне, отсортированный по цене
     * @throws ValidationException если ценовой диапазон указан некорректно
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Property> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            return propertyDao.findByPriceRange(minPrice, maxPrice);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости по ценовому диапазону: " + minPrice + " - " + maxPrice);
            throw re;
        }
    }

    /**
     * Найти объекты недвижимости по городу
     * @param cityId идентификатор города
     * @return список объектов недвижимости в указанном городе, отсортированный по цене
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Property> findByCityId(Long cityId) {
        try {
            return propertyDao.findByCityId(cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости по городу с id: " + cityId);
            throw re;
        }
    }

    /**
     * Найти объекты недвижимости по типу
     * @param propertyTypeId идентификатор типа недвижимости
     * @return список объектов недвижимости указанного типа, отсортированный по цене
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Property> findByPropertyTypeId(Long propertyTypeId) {
        try {
            return propertyDao.findByPropertyTypeId(propertyTypeId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости по типу с id: " + propertyTypeId);
            throw re;
        }
    }

    /**
     * Получить общее количество объектов недвижимости в базе данных
     * @return количество объектов недвижимости
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public int getCount() {
        try {
            return propertyDao.getCount();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при подсчете количества объектов недвижимости");
            throw re;
        }
    }

    /**
     * Проверить, есть ли связанные сделки для объекта недвижимости
     * @param propertyId идентификатор объекта недвижимости
     * @return true если есть связанные сделки, false если нет
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean hasRelatedDeals(Long propertyId) {
        try {
            return !dealDao.findByPropertyId(propertyId).isEmpty();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", propertyId);
            ExceptionHandler.logException(re, "Ошибка при проверке связанных сделок для объекта недвижимости с id: " + propertyId);
            throw re;
        }
    }

    /**
     * Валидация данных объекта недвижимости
     * @param property объект недвижимости для валидации
     * @throws ValidationException если данные не прошли валидацию
     */
    private void validateProperty(Property property) {
        if (property == null) {
            throw new ValidationException("property", "Объект недвижимости не может быть null");
        }
        // Остальная валидация выполняется в DAO
    }

    /**
     * Валидация обновлений объекта недвижимости
     * @param updates карта обновлений
     * @throws ValidationException если обновления не прошли валидацию
     */
    private void validateUpdates(Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            throw new ValidationException("updates", "Данные для обновления не могут быть пустыми");
        }
        // Остальная валидация выполняется в DAO
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С DTO ==========

    /**
     * Получить все объекты недвижимости с детальной информацией (включая географические данные)
     * Использует JOIN запросы для оптимизации производительности
     * @return список всех объектов недвижимости с полной информацией, отсортированный по стоимости в убывающем порядке
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyWithDetailsDto> findAllWithDetails() {
        try {
            return propertyDao.findAllWithDetails();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех объектов недвижимости с детальной информацией");
            throw re;
        }
    }

    /**
     * Найти объект недвижимости по идентификатору с детальной информацией
     * Включает полную географическую иерархию и информацию о типе недвижимости
     * @param id идентификатор объекта недвижимости
     * @return объект недвижимости с детальной информацией
     * @throws EntityNotFoundException если объект недвижимости не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public PropertyWithDetailsDto findByIdWithDetails(Long id) {
        try {
            return propertyDao.findByIdWithDetails(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", id);
            ExceptionHandler.logException(re, "Ошибка при поиске объекта недвижимости с детальной информацией по id: " + id);
            throw re;
        }
    }

    /**
     * Получить все объекты недвижимости в табличном формате для отображения в каталоге
     * Компактное представление с основной информацией
     * @return список объектов недвижимости в табличном формате, отсортированный по стоимости в убывающем порядке
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyTableDto> findAllForTable() {
        try {
            return propertyDao.findAllForTable();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех объектов недвижимости в табличном формате");
            throw re;
        }
    }

    /**
     * Найти объекты недвижимости по ценовому диапазону с детальной информацией
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список объектов недвижимости с полной информацией в указанном ценовом диапазоне, отсортированный по цене
     * @throws ValidationException если ценовой диапазон указан некорректно
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyWithDetailsDto> findByPriceRangeWithDetails(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            return propertyDao.findByPriceRangeWithDetails(minPrice, maxPrice);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости с детальной информацией по ценовому диапазону: " + minPrice + " - " + maxPrice);
            throw re;
        }
    }

    /**
     * Найти объекты недвижимости по городу с детальной информацией
     * @param cityId идентификатор города
     * @return список объектов недвижимости с полной информацией в указанном городе, отсортированный по цене
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyWithDetailsDto> findByCityIdWithDetails(Long cityId) {
        try {
            return propertyDao.findByCityIdWithDetails(cityId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости с детальной информацией по городу с id: " + cityId);
            throw re;
        }
    }

    /**
     * Найти объекты недвижимости по типу с детальной информацией
     * @param propertyTypeId идентификатор типа недвижимости
     * @return список объектов недвижимости с полной информацией указанного типа, отсортированный по цене
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyWithDetailsDto> findByPropertyTypeIdWithDetails(Long propertyTypeId) {
        try {
            return propertyDao.findByPropertyTypeIdWithDetails(propertyTypeId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости с детальной информацией по типу с id: " + propertyTypeId);
            throw re;
        }
    }

    /**
     * Поиск объектов недвижимости по заданным критериям
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @param cityId идентификатор города
     * @param propertyTypeId идентификатор типа недвижимости
     * @param districtId идентификатор района
     * @param streetId идентификатор улицы
     * @return список объектов недвижимости, соответствующих критериям поиска
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyTableDto> searchProperties(BigDecimal minPrice, BigDecimal maxPrice, Long cityId, Long propertyTypeId, Long districtId, Long streetId) {
        try {
            return propertyDao.search(minPrice, maxPrice, cityId, propertyTypeId, districtId, streetId);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при поиске объектов недвижимости по заданным критериям");
            throw re;
        }
    }
    
    /**
     * Получить все объекты недвижимости для отчета
     * @return список всех объектов недвижимости с полной информацией для отчета
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyReportDto> findAllForReport() {
        try {
            return propertyDao.findAllForReport();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Property", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех объектов недвижимости для отчета");
            throw re;
        }
    }
}