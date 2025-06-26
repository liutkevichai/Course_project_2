package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;
import ru.realestate.realestate_app.dao.PropertyTypeDao;
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
     */
    public List<PropertyType> findAll() {
        return propertyTypeDao.findAll();
    }

    /**
     * Найти тип недвижимости по уникальному идентификатору
     * @param id идентификатор типа недвижимости
     * @return объект типа недвижимости
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
     */
    public PropertyType findById(Long id) {
        return propertyTypeDao.findById(id);
    }

    /**
     * Найти тип недвижимости по названию (точное совпадение)
     * @param name название типа недвижимости
     * @return объект типа недвижимости
     * @throws org.springframework.dao.EmptyResultDataAccessException если тип не найден
     */
    public PropertyType findByName(String name) {
        return propertyTypeDao.findByName(name);
    }
} 