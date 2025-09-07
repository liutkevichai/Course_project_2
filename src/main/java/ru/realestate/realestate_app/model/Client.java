package ru.realestate.realestate_app.model;

import jakarta.validation.constraints.*;

public class Client {
    private Long idClient;
    
    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\s-']+$", message = "Имя может содержать только буквы, пробелы, дефисы и апострофы")
    private String firstName;
    
    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\s-']+$", message = "Фамилия может содержать только буквы, пробелы, дефисы и апострофы")
    private String lastName;
    
    @Size(max = 100, message = "Отчество не может превышать 100 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\s-']*$", message = "Отчество может содержать только буквы, пробелы, дефисы и апострофы")
    private String middleName;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Некорректный формат телефона. Используйте формат: +7XXXXXXXXXX или 8XXXXXXXXXX")
    private String phone;
    
    @Email(message = "Некорректный формат email адреса")
    @Size(max = 255, message = "Email не может превышать 255 символов")
    private String email;
    
    // Конструкторы
    public Client() {}
    
    public Client(Long idClient, String firstName, String lastName, String middleName, 
                 String phone, String email) {
        this.idClient = idClient;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
    }
    
    // Геттеры и сеттеры
    public Long getIdClient() {
        return idClient;
    }
    
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Метод для получения полного имени
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (lastName != null) fullName.append(lastName);
        if (firstName != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(firstName);
        }
        if (middleName != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middleName);
        }
        return fullName.toString();
    }
    
    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
} 