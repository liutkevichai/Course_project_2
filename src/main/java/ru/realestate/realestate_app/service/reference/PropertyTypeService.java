package ru.realestate.realestate_app.service.reference;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.reference.PropertyTypeDao;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.PropertyType;

import java.util.List;

/**
 * Сервис для работы с типами недвижимости
 * Содержит бизнес-логику для операций с типами недвижимости
 * Делегирует выполнение операций с базой данных в PropertyTypeDao
 */
@Service
public class PropertyTypeService {

    // DAO для работы с данными типов недвижимости
    private final PropertyTypeDao propertyTypeDao;

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param propertyTypeDao DAO для работы с данными типов недвижимости
     */
    public PropertyTypeService(PropertyTypeDao propertyTypeDao) {
        this.propertyTypeDao = propertyTypeDao;
    }

    /**
     * Получить все типы недвижимости, отсортированные по названию
     * @return список всех типов недвижимости
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<PropertyType> findAll() {
        try {
            return propertyTypeDao.findAll();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "PropertyType", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех типов недвижимости");
            throw re;
        }
    }

    /**
     * Найти тип недвижимости по уникальному идентификатору
     * @param id идентификатор типа недвижимости
     * @return объект типа недвижимости
     * @throws EntityNotFoundException если тип недвижимости не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public PropertyType findById(Long id) {
        try {
            return propertyTypeDao.findById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "PropertyType", id);
            ExceptionHandler.logException(re, "Ошибка при поиске типа недвижимости по id: " + id);
            throw re;
        }
    }

    /**
     * Найти тип недвижимости по названию (точное совпадение)
     * @param name название типа недвижимости
     * @return объект типа недвижимости
     * @throws EntityNotFoundException если тип недвижимости не найден
     * @throws ValidationException если название не указано
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public PropertyType findByName(String name) {
        try {
            return propertyTypeDao.findByName(name);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "PropertyType", null);
            ExceptionHandler.logException(re, "Ошибка при поиске типа недвижимости по названию: " + name);
            throw re;
        }
    }
} 