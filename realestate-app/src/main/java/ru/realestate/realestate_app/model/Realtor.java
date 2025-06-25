package ru.realestate.realestate_app.model;

public class Realtor {
    private Long idRealtor;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
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