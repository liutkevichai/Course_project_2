package ru.realestate.realestate_app.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.ClientDao;
import ru.realestate.realestate_app.dao.DealDao;
import ru.realestate.realestate_app.exception.BusinessRuleException;
import ru.realestate.realestate_app.exception.DatabaseException;
import ru.realestate.realestate_app.exception.EntityNotFoundException;
import ru.realestate.realestate_app.exception.RealEstateException;
import ru.realestate.realestate_app.exception.ValidationException;
import ru.realestate.realestate_app.exception.handler.ExceptionHandler;
import ru.realestate.realestate_app.model.Client;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с клиентами
 * Содержит бизнес-логику для операций с клиентами агентства недвижимости
 * Делегирует выполнение операций с базой данных в ClientDao
 */
@Service
public class ClientService {

    private final ClientDao clientDao;
    private final DealDao dealDao; // Добавляем зависимость для проверок

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param clientDao DAO для работы с данными клиентов
     * @param dealDao DAO для работы с данными сделок
     */
    public ClientService(ClientDao clientDao, DealDao dealDao) {
        this.clientDao = clientDao;
        this.dealDao = dealDao;
    }

    /**
     * Получить всех клиентов, отсортированных по фамилии и имени
     * @return список всех клиентов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Client> findAll() {
        try {
            return clientDao.findAll();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Client", null);
            ExceptionHandler.logException(re, "Ошибка при получении списка всех клиентов");
            throw re;
        }
    }

    /**
     * Найти клиента по уникальному идентификатору
     * @param id идентификатор клиента
     * @return объект клиента
     * @throws EntityNotFoundException если клиент не найден
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Client findById(Long id) {
        try {
            return clientDao.findById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Client", id);
            ExceptionHandler.logException(re, "Ошибка при поиске клиента по id: " + id);
            throw re;
        }
    }

    /**
     * Сохранить нового клиента в базе данных
     * @param client объект клиента для сохранения
     * @return идентификатор созданного клиента или null в случае ошибки
     * @throws ValidationException если данные клиента не прошли валидацию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Long save(Client client) {
        // Валидация входных данных
        validateClient(client);
        
        // Проверка уникальности email и телефона
        validateUniqueness(client);
        
        try {
            return clientDao.save(client);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "INSERT", "Client", null);
            ExceptionHandler.logException(re, "Ошибка при сохранении клиента");
            throw re;
        }
    }

    /**
     * Обновить данные существующего клиента
     * @param id идентификатор клиента для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     * @throws EntityNotFoundException если клиент не найден
     * @throws ValidationException если данные не прошли валидацию
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean update(Long id, Map<String, Object> updates) {
        // Проверяем существование клиента
        findById(id);
        
        // Валидация обновлений
        validateUpdates(updates);
        
        // Проверка уникальности email и телефона при обновлении
        validateUniquenessOnUpdate(id, updates);
        
        try {
            return clientDao.update(id, updates);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "UPDATE", "Client", id);
            ExceptionHandler.logException(re, "Ошибка при обновлении клиента с id: " + id);
            throw re;
        }
    }

    /**
     * Удалить клиента по идентификатору
     * Проверяет, что у клиента нет связанных сделок
     * @param id идентификатор клиента для удаления
     * @return true если удаление прошло успешно, false если клиент не найден
     * @throws BusinessRuleException если у клиента есть связанные сделки
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean deleteById(Long id) {
        // Проверяем, есть ли связанные сделки
        if (hasRelatedDeals(id.intValue())) {
            throw new BusinessRuleException(
                "CLIENT_HAS_RELATED_DEALS",
                String.format("Нельзя удалить клиента с ID %d, так как у него есть связанные сделки", id)
            );
        }
        
        try {
            return clientDao.deleteById(id);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "DELETE", "Client", id);
            ExceptionHandler.logException(re, "Ошибка при удалении клиента с id: " + id);
            throw re;
        }
    }

    /**
     * Найти клиентов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список клиентов с указанной фамилией
     * @throws ValidationException если фамилия не указана
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public List<Client> findByLastName(String lastName) {
        try {
            return clientDao.findByLastName(lastName);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Client", null);
            ExceptionHandler.logException(re, "Ошибка при поиске клиентов по фамилии: " + lastName);
            throw re;
        }
    }

    /**
     * Найти клиента по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект клиента
     * @throws EntityNotFoundException если клиент не найден
     * @throws ValidationException если номер телефона не указан
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Client findByPhone(String phone) {
        try {
            return clientDao.findByPhone(phone);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Client", null);
            ExceptionHandler.logException(re, "Ошибка при поиске клиента по телефону: " + phone);
            throw re;
        }
    }

    /**
     * Найти клиента по адресу электронной почты (точное совпадение)
     * @param email адрес электронной почты
     * @return объект клиента
     * @throws EntityNotFoundException если клиент не найден
     * @throws ValidationException если email не указан или неверного формата
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public Client findByEmail(String email) {
        try {
            return clientDao.findByEmail(email);
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Client", null);
            ExceptionHandler.logException(re, "Ошибка при поиске клиента по email: " + email);
            throw re;
        }
    }

    /**
     * Получить общее количество клиентов в базе данных
     * @return количество клиентов
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public int getCount() {
        try {
            return clientDao.getCount();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Client", null);
            ExceptionHandler.logException(re, "Ошибка при получении количества клиентов");
            throw re;
        }
    }

    /**
     * Проверить, есть ли связанные сделки для клиента
     * @param clientId идентификатор клиента
     * @return true если есть связанные сделки, false если нет
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public boolean hasRelatedDeals(Integer clientId) {
        try {
            return !dealDao.findByClientId(clientId).isEmpty();
        } catch (Exception e) {
            RealEstateException re = ExceptionHandler.handleDatabaseException(e, "SELECT", "Deal", clientId);
            ExceptionHandler.logException(re, "Ошибка при проверке связанных сделок для клиента с id: " + clientId);
            throw re;
        }
    }

    /**
     * Валидация данных клиента
     * @param client объект клиента для валидации
     * @throws ValidationException если данные не прошли валидацию
     */
    private void validateClient(Client client) {
        if (client == null) {
            throw new ValidationException("client", "Объект клиента не может быть null");
        }
        // Остальная валидация выполняется в DAO
    }

    /**
     * Валидация уникальности email и телефона
     * @param client объект клиента для проверки
     * @throws ValidationException если email или телефон уже существуют
     */
    private void validateUniqueness(Client client) {
        try {
            // Проверяем уникальность email
            Client existingClientByEmail = clientDao.findByEmail(client.getEmail());
            if (existingClientByEmail != null) {
                throw new ValidationException("email", "Клиент с таким email уже существует");
            }
        } catch (EmptyResultDataAccessException e) {
            // Email уникален, продолжаем
        }
        
        try {
            // Проверяем уникальность телефона
            Client existingClientByPhone = clientDao.findByPhone(client.getPhone());
            if (existingClientByPhone != null) {
                throw new ValidationException("phone", "Клиент с таким номером телефона уже существует");
            }
        } catch (EmptyResultDataAccessException e) {
            // Телефон уникален, продолжаем
        }
    }

    /**
     * Валидация уникальности email и телефона при обновлении
     * @param clientId идентификатор обновляемого клиента
     * @param updates карта обновлений
     * @throws ValidationException если email или телефон уже существуют у другого клиента
     */
    private void validateUniquenessOnUpdate(Long clientId, Map<String, Object> updates) {
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            try {
                Client existingClient = clientDao.findByEmail(newEmail);
                if (existingClient != null && !existingClient.getIdClient().equals(clientId)) {
                    throw new ValidationException("email", "Клиент с таким email уже существует");
                }
            } catch (EmptyResultDataAccessException e) {
                // Email уникален, продолжаем
            }
        }
        
        if (updates.containsKey("phone")) {
            String newPhone = (String) updates.get("phone");
            try {
                Client existingClient = clientDao.findByPhone(newPhone);
                if (existingClient != null && !existingClient.getIdClient().equals(clientId)) {
                    throw new ValidationException("phone", "Клиент с таким номером телефона уже существует");
                }
            } catch (EmptyResultDataAccessException e) {
                // Телефон уникален, продолжаем
            }
        }
    }

    /**
     * Валидация обновлений клиента
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