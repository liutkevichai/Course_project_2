package ru.realestate.realestate_app.model;

import jakarta.validation.constraints.*;
import com.opencsv.bean.CsvBindByName;

public class Realtor {
    @CsvBindByName(column = "ID")
    private Long idRealtor;
    
    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\s-']+$", message = "Имя может содержать только буквы, пробелы, дефисы и апострофы")
    @CsvBindByName(column = "Имя")
    private String firstName;
    
    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\s-']+$", message = "Фамилия может содержать только буквы, пробелы, дефисы и апострофы")
    @CsvBindByName(column = "Фамилия")
    private String lastName;
    
    @Size(max = 100, message = "Отчество не может превышать 100 символов")
    @Pattern(regexp = "^[а-яёА-ЯЁa-zA-Z\\s-']*$", message = "Отчество может содержать только буквы, пробелы, дефисы и апострофы")
    @CsvBindByName(column = "Отчество")
    private String middleName;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Некорректный формат телефона. Используйте формат: +7XXXXXXXXXX или 8XXXXXXXXXX")
    @CsvBindByName(column = "Телефон")
    private String phone;
    
    @Email(message = "Некорректный формат email адреса")
    @Size(max = 255, message = "Email не может превышать 255 символов")
    @CsvBindByName(column = "Email")
    private String email;
    
    @NotNull(message = "Опыт работы обязателен для заполнения")
    @Min(value = 0, message = "Опыт работы не может быть отрицательным")
    @Max(value = 50, message = "Опыт работы не может превышать 50 лет")
    @CsvBindByName(column = "Опыт работы (лет)")
    private Integer experienceYears;
    
    // Конструкторы
    public Realtor() {}
    
    public Realtor(Long idRealtor, String firstName, String lastName, String middleName, 
                  String phone, String email, Integer experienceYears) {
        this.idRealtor = idRealtor;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
        this.experienceYears = experienceYears;
    }
    
    // Геттеры и сеттеры
    public Long getIdRealtor() {
        return idRealtor;
    }
    
    public void setIdRealtor(Long idRealtor) {
        this.idRealtor = idRealtor;
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
    
    public Integer getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    // Метод для получения полного имени
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (lastName != null) fullName.append(lastName);
        if (firstName != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(firstName);
        }
        if (middleName != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(middleName);
        }
        return fullName.toString();
    }
    
    @Override
    public String toString() {
        return "Realtor{" +
                "idRealtor=" + idRealtor +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", experienceYears=" + experienceYears +
                '}';
    }
}