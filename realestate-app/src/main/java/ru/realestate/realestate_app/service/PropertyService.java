package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.PropertyDao;
import ru.realestate.realestate_app.model.Property;

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

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param propertyDao DAO для работы с данными объектов недвижимости
     */
    public PropertyService(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    /**
     * Получить все объекты недвижимости, отсортированные по идентификатору
     * @return список всех объектов недвижимости
     */
    public List<Property> findAll() {
        return propertyDao.findAll();
    }

    /**
     * Найти объект недвижимости по уникальному идентификатору
     * @param id идентификатор объекта недвижимости
     * @return объект недвижимости
     * @throws org.springframework.dao.EmptyResultDataAccessException если объект не найден
     */
    public Property findById(Long id) {
        return propertyDao.findById(id);
    }

    /**
     * Сохранить новый объект недвижимости в базе данных
     * @param property объект недвижимости для сохранения
     * @return идентификатор созданного объекта недвижимости
     */
    public Long save(Property property) {
        return propertyDao.save(property);
    }

    /**
     * Обновить данные существующего объекта недвижимости
     * @param id идентификатор объекта недвижимости для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     */
    public boolean update(Long id, Map<String, Object> updates) {
        return propertyDao.update(id, updates);
    }

    /**
     * Удалить объект недвижимости по идентификатору
     * @param id идентификатор объекта недвижимости для удаления
     * @return true если удаление прошло успешно, false если объект не найден
     */
    public boolean deleteById(Long id) {
        return propertyDao.deleteById(id);
    }

    /**
     * Найти объекты недвижимости в указанном ценовом диапазоне
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список объектов недвижимости в ценовом диапазоне, отсортированный по цене
     */
    public List<Property> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyDao.findByPriceRange(minPrice, maxPrice);
    }

    /**
     * Найти объекты недвижимости по городу
     * @param cityId идентификатор города
     * @return список объектов недвижимости в указанном городе, отсортированный по цене
     */
    public List<Property> findByCityId(Integer cityId) {
        return propertyDao.findByCityId(cityId);
    }

    /**
     * Найти объекты недвижимости по типу
     * @param propertyTypeId идентификатор типа недвижимости
     * @return список объектов недвижимости указанного типа, отсортированный по цене
     */
    public List<Property> findByPropertyTypeId(Integer propertyTypeId) {
        return propertyDao.findByPropertyTypeId(propertyTypeId);
    }

    /**
     * Получить общее количество объектов недвижимости в базе данных
     * @return количество объектов недвижимости
     */
    public int getCount() {
        return propertyDao.getCount();
    }
} 