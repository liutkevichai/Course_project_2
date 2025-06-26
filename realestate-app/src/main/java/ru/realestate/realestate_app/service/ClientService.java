package ru.realestate.realestate_app.service;

import org.springframework.stereotype.Service;

import ru.realestate.realestate_app.dao.ClientDao;
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

    /**
     * Конструктор сервиса с инжекцией зависимостей
     * @param clientDao DAO для работы с данными клиентов
     */
    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    /**
     * Получить всех клиентов, отсортированных по фамилии и имени
     * @return список всех клиентов
     */
    public List<Client> findAll() {
        return clientDao.findAll();
    }

    /**
     * Найти клиента по уникальному идентификатору
     * @param id идентификатор клиента
     * @return объект клиента
     * @throws org.springframework.dao.EmptyResultDataAccessException если клиент не найден
     */
    public Client findById(Long id) {
        return clientDao.findById(id);
    }

    /**
     * Сохранить нового клиента в базе данных
     * @param client объект клиента для сохранения
     * @return идентификатор созданного клиента или null в случае ошибки
     */
    public Long save(Client client) {
        return clientDao.save(client);
    }

    /**
     * Обновить данные существующего клиента
     * @param id идентификатор клиента для обновления
     * @param updates карта с полями для обновления (ключ - название поля, значение - новое значение)
     * @return true если обновление прошло успешно, false если данных для обновления нет
     */
    public boolean update(Long id, Map<String, Object> updates) {
        return clientDao.update(id, updates);
    }

    /**
     * Удалить клиента по идентификатору
     * @param id идентификатор клиента для удаления
     * @return true если удаление прошло успешно, false если клиент не найден
     */
    public boolean deleteById(Long id) {
        return clientDao.deleteById(id);
    }

    /**
     * Найти клиентов по фамилии (поиск по частичному совпадению)
     * @param lastName фамилия для поиска
     * @return список клиентов с указанной фамилией
     */
    public List<Client> findByLastName(String lastName) {
        return clientDao.findByLastName(lastName);
    }

    /**
     * Найти клиента по номеру телефона (точное совпадение)
     * @param phone номер телефона
     * @return объект клиента
     * @throws org.springframework.dao.EmptyResultDataAccessException если клиент не найден
     */
    public Client findByPhone(String phone) {
        return clientDao.findByPhone(phone);
    }

    /**
     * Найти клиента по адресу электронной почты (точное совпадение)
     * @param email адрес электронной почты
     * @return объект клиента
     * @throws org.springframework.dao.EmptyResultDataAccessException если клиент не найден
     */
    public Client findByEmail(String email) {
        return clientDao.findByEmail(email);
    }

    /**
     * Получить общее количество клиентов в базе данных
     * @return количество клиентов
     */
    public int getCount() {
        return clientDao.getCount();
    }
}