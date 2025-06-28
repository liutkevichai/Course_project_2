package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.DealTypeDao;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.DealType;

import java.util.List;

/**
 * Сервис для работы с типами сделок
 * Содержит бизнес-логику для операций с типами сделок
 * Делегирует выполнение операций с базой данных в DealTypeDao
 */
@Service
public class DealTypeService {
    
    private final DealTypeDao dealTypeDao;

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param dealTypeDao DAO для работы с данными типов сделок
     */
    public DealTypeService(DealTypeDao dealTypeDao) {
        this.dealTypeDao = dealTypeDao;
    }

    /**
     * Получить все типы сделок, отсортированные по названию
     * @return список всех типов сделок
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<DealType> findAll() {
        try {
            return dealTypeDao.findAll();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "DealType", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех типов сделок");
            throw re;
        }
    }

    /**
     * Найти тип сделки по уникальному идентификатору
     * @param id идентификатор типа сделки
     * @return объект типа сделки
     * @throws EntityNotFoundException если тип сделки не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public DealType findById(Long id) {
        try {
            return dealTypeDao.findById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "DealType", id);
            ExceptionHandler.logException(re, "Ошибка при поиске типа сделки по id: " + id);
            throw re;
        }
    }

    /**
     * Найти тип сделки по названию (точное совпадение)
     * @param name название типа сделки
     * @return объект типа сделки
     * @throws EntityNotFoundException если тип сделки не найден
     * @throws ValidationException если название не указано
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public DealType findByName(String name) {
        try {
            return dealTypeDao.findByName(name);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "DealType", null);
            ExceptionHandler.logException(re, "Ошибка при поиске типа сделки по названию: " + name);
            throw re;
        }
    }
} 