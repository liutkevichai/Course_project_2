package ru.realestate.realestate_app.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.DealDao;
import ru.realestate.realestate_app.dao.RealtorDao;
import ru.realestate.realestate_app.exception.BusinessRuleException;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.Realtor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для работы с риелторами
 * Содержит бизнес-логику для операций с риелторами
 * Делегирует выполнение операций с базой данных в RealtorDao
 */
@Service
public class RealtorService {
    
    private final RealtorDao realtorDao;
    private final DealDao dealDao; // Добавляем зависимость для проверок

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param realtorDao DAO для работы с данными риелторов
     * @param dealDao DAO для работы с данными сделок
     */
    public RealtorService(RealtorDao realtorDao, DealDao dealDao) {
        this.realtorDao = realtorDao;
        this.dealDao = dealDao;
    }

    /**
     * Получить всех риелторов, отсортированных по фамилии и имени
     * @return список всех риелторов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Realtor> findAll() {
        try {
            return realtorDao.findAll();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех риелторов");
            throw re;
        }
    }

    /**
     * Найти риелтора по уникальному идентификатору
     * @param id идентификатор риелтора
     * @return объект риелтора
     * @throws EntityNotFoundException если риелтор не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Realtor findById(Long id) {
        try {
            return realtorDao.findById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", id);
            ExceptionHandler.logException(re, "Ошибка при поиске риелтора по id: " + id);
            throw re;
        }
    }

    /**
     * Сохранить нового риелтора в базе данных
     * @param realtor объект риелтора для сохранения
     * @return идентификатор созданного риелтора
     * @throws ValidationException если данные риелтора не прошли валидацию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Long save(Realtor realtor) {
        // Валидация входных данных
        validateRealtor(realtor);
        
        // Проверка уникальности email и телефона
        validateUniqueness(realtor);
        
        try {
            return realtorDao.save(realtor);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "INSERT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при сохранении риелтора");
            throw re;
        }
    }

    /**
     * Обновить данные существующего риелтора
     * @param id идентификатор риелтора для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws EntityNotFoundException если риелтор не найден
     * @throws ValidationException если данные не прошли валидацию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем существование риелтора
        findById(id);
        
        // Валидация обновлений
        validateUpdates(updates);
        
        // Проверка уникальности email и телефона при обновлении
        validateUniquenessOnUpdate(id, updates);
        
        try {
            return realtorDao.update(id, updates);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "UPDATE", "Realtor", id);
            ExceptionHandler.logException(re, "Ошибка при обновлении риелтора с id: " + id);
            throw re;
        }
    }

    /**
     * Удалить риелтора по идентификатору
     * Проверяет, что у риелтора нет связанных сделок
     * @param id идентификатор риелтора для удаления
     * @return true если удаление прошло успешно, false если риелтор не найден
     * @throws BusinessRuleException если у риелтора есть связанные сделки
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean deleteById(Long id) {
        // Проверяем, есть ли связанные сделки
        if (hasRelatedDeals(id)) {
            throw new BusinessRuleException(
                "REALTOR_HAS_RELATED_DEALS",
                String.format("Нельзя удалить риелтора с ID %d, так как у него есть связанные сделки", id)
            );
        }
        
        try {
            return realtorDao.deleteById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "DELETE", "Realtor", id);
            ExceptionHandler.logException(re, "Ошибка при удалении риелтора с id: " + id);
            throw re;
        }
    }

    /**
     * Найти риелторов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список риелторов с указанной фамилией
     * @throws ValidationException если фамилия не указана
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Realtor> findByLastName(String lastName) {
        try {
            return realtorDao.findByLastName(lastName);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при поиске риелторов по фамилии: " + lastName);
            throw re;
        }
    }

    /**
     * Найти риелторов с опытом работы больше указанного значения
     * @param minExperience минимальный опыт работы в годах
     * @return список риелторов с подходящим опытом, отсортированный по убыванию опыта
     * @throws ValidationException если минимальный опыт указан некорректно
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Realtor> findByExperienceGreaterThan(int minExperience) {
        try {
            return realtorDao.findByExperienceGreaterThan(minExperience);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при поиске риелторов по опыту работы больше: " + minExperience);
            throw re;
        }
    }

    /**
     * Найти риелтора по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект риелтора
     * @throws EntityNotFoundException если риелтор не найден
     * @throws ValidationException если номер телефона не указан
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Realtor findByPhone(String phone) {
        try {
            return realtorDao.findByPhone(phone);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при поиске риелтора по телефону: " + phone);
            throw re;
        }
    }

    /**
     * Найти риелтора по адресу электронной почты (точное совпадение)
     * @param email адрес электронной почты
     * @return объект риелтора
     * @throws EntityNotFoundException если риелтор не найден
     * @throws ValidationException если email не указан или неверного формата
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Realtor findByEmail(String email) {
        try {
            return realtorDao.findByEmail(email);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при поиске риелтора по email: " + email);
            throw re;
        }
    }

    /**
     * Получить общее количество риелторов в базе данных
     * @return количество риелторов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public int getCount() {
        try {
            return realtorDao.getCount();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при получении количества риелторов");
            throw re;
        }
    }

    /**
     * Проверить, есть ли связанные сделки для риелтора
     * @param realtorId идентификатор риелтора
     * @return true если есть связанные сделки, false если нет
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean hasRelatedDeals(Long realtorId) {
        try {
            return !dealDao.findByRealtorId(realtorId).isEmpty();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", realtorId);
            ExceptionHandler.logException(re, "Ошибка при проверке связанных сделок для риелтора с id: " + realtorId);
            throw re;
        }
    }

    /**
     * Валидация данных риелтора
     * @param realtor объект риелтора для валидации
     * @throws ValidationException если данные не прошли валидацию
     */
    private void validateRealtor(Realtor realtor) {
        if (realtor == null) {
            throw new ValidationException("realtor", "Объект риелтора не может быть null");
        }
        // Остальная валидация выполняется в DAO
    }

    /**
     * Расширенный поиск риелторов по нескольким критериям
     * @param lastName фамилия риелтора (может быть null)
     * @param email email риелтора (может быть null)
     * @param phone телефон риелтора (может быть null)
     * @param experience опыт работы риелтора (может быть null)
     * @return список риелторов, соответствующих критериям поиска
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Realtor> searchRealtors(String lastName, String email, String phone, Integer experience) {
        try {
            return realtorDao.search(lastName, email, phone, experience);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Realtor", null);
            ExceptionHandler.logException(re, "Ошибка при расширенном поиске риелторов");
            throw re;
        }
    }

    /**
     * Валидация уникальности email и телефона
     * @param realtor объект риелтора для проверки
     * @throws ValidationException если email или телефон уже существуют
     */
    private void validateUniqueness(Realtor realtor) {
        try {
            // Проверяем уникальность email
            Realtor existingRealtorByEmail = realtorDao.findByEmail(realtor.getEmail());
            if (existingRealtorByEmail != null) {
                throw new ValidationException("email", "Риелтор с таким email уже существует");
            }
        } catch (EmptyResultDataAccessException e) {
            // Email уникален, продолжаем
        }
        
        try {
            // Проверяем уникальность телефона
            Realtor existingRealtorByPhone = realtorDao.findByPhone(realtor.getPhone());
            if (existingRealtorByPhone != null) {
                throw new ValidationException("phone", "Риелтор с таким номером телефона уже существует");
            }
        } catch (EmptyResultDataAccessException e) {
            // Телефон уникален, продолжаем
        }
    }

    /**
     * Валидация уникальности email и телефона при обновлении
     * @param realtorId идентификатор обновляемого риелтора
     * @param updates карта обновлений
     * @throws ValidationException если email или телефон уже существуют у другого риелтора
     */
    private void validateUniquenessOnUpdate(Long realtorId, Map<String, Object> updates) {
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            try {
                Realtor existingRealtor = realtorDao.findByEmail(newEmail);
                if (existingRealtor != null && !existingRealtor.getIdRealtor().equals(realtorId)) {
                    throw new ValidationException("email", "Риелтор с таким email уже существует");
                }
            } catch (EmptyResultDataAccessException e) {
                // Email уникален, продолжаем
            }
        }
        
        if (updates.containsKey("phone")) {
            String newPhone = (String) updates.get("phone");
            try {
                Realtor existingRealtor = realtorDao.findByPhone(newPhone);
                if (existingRealtor != null && !existingRealtor.getIdRealtor().equals(realtorId)) {
                    throw new ValidationException("phone", "Риелтор с таким номером телефона уже существует");
                }
            } catch (EmptyResultDataAccessException e) {
                // Телефон уникален, продолжаем
            }
        }
    }

    /**
     * Валидация обновлений риелтора
     * @param updates карта обновлений
     * @throws ValidationException если обновления не прошли валидацию
     */
    private void validateUpdates(Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            throw new ValidationException("updates", "Данные для обновления не могут быть пустыми");
        }
        // Остальная валидация выполняется в DAO
    }

} 