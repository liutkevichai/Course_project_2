package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;
import ru.realestate.realestate_app.dao.DealTypeDao;
import ru.realestate.realestate_app.model.DealType;

import java.util.List;

/**
 * Сервис для работы с типами сделок
 * Содержит бизнес-логику для операций с типами сделок
 * Делегирует выполнение операций с базой данных в DealTypeDao
 */
@Service
public class DealTypeService {

    // DAO для работы с данными типов сделок
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
     */
    public List<DealType> findAll() {
        return dealTypeDao.findAll();
    }

    /**
     * Найти тип сделки по уникальному идентификатору
     * @param id идентификатор типа сделки
     * @return объект типа сделки
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
     */
    public DealType findById(Long id) {
        return dealTypeDao.findById(id);
    }

    /**
     * Найти тип сделки по названию (точное совпадение)
     * @param name название типа сделки
     * @return объект типа сделки
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
     */
    public DealType findByName(String name) {
        return dealTypeDao.findByName(name);
    }
} 