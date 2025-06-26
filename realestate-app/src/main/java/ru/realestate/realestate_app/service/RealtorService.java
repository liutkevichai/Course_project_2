package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.RealtorDao;
import ru.realestate.realestate_app.model.Realtor;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с риелторами
 * Содержит бизнес-логику для операций с риелторами
 * Делегирует выполнение операций с базой данных в RealtorDao
 */
@Service
public class RealtorService {
    
    private final RealtorDao realtorDao;

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param realtorDao DAO для работы с данными риелторов
     */
    public RealtorService(RealtorDao realtorDao) {
        this.realtorDao = realtorDao;
    }

    /**
     * Получить всех риелторов, отсортированных по фамилии и имени
     * @return список всех риелторов
     */
    public List<Realtor> findAll() {
        return realtorDao.findAll();
    }

    /**
     * Найти риелтора по уникальному идентификатору
     * @param id идентификатор риелтора
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     */
    public Realtor findById(Long id) {
        return realtorDao.findById(id);
    }

    /**
     * Сохранить нового риелтора в базе данных
     * @param realtor объект риелтора для сохранения
     * @return идентификатор созданного риелтора
     */
    public Long save(Realtor realtor) {
        return realtorDao.save(realtor);
    }

    /**
     * Обновить данные существующего риелтора
     * @param id идентификатор риелтора для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     */
    public boolean update(Long id, Map<String, Object> updates) {
        return realtorDao.update(id, updates);
    }

    /**
     * Удалить риелтора по идентификатору
     * @param id идентификатор риелтора для удаления
     * @return true если удаление прошло успешно, false если риелтор не найден
     */
    public boolean deleteById(Long id) {
        return realtorDao.deleteById(id);
    }

    /**
     * Найти риелторов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список риелторов с указанной фамилией
     */
    public List<Realtor> findByLastName(String lastName) {
        return realtorDao.findByLastName(lastName);
    }

    /**
     * Найти риелторов с опытом работы больше указанного значения
     * @param minExperience минимальный опыт работы в годах
     * @return список риелторов с подходящим опытом, отсортированный по убыванию опыта
     */
    public List<Realtor> findByExperienceGreaterThan(int minExperience) {
        return realtorDao.findByExperienceGreaterThan(minExperience);
    }

    /**
     * Найти риелтора по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     */
    public Realtor findByPhone(String phone) {
        return realtorDao.findByPhone(phone);
    }

    /**
     * Найти риелтора по адресу электронной почты (точное совпадение)
     * @param email адрес электронной почты
     * @return объект риелтора
     * @throws org.springframework.dao.EmptyResultDataAccessException если риелтор не найден
     */
    public Realtor findByEmail(String email) {
        return realtorDao.findByEmail(email);
    }

    /**
     * Получить общее количество риелторов в базе данных
     * @return количество риелторов
     */
    public int getCount() {
        return realtorDao.getCount();
    }
} 