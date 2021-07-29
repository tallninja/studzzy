package application.models;

import application.controllers.UserController;
import application.controllers.utils.Password;

import java.sql.Date;
import java.util.UUID;

public class User {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String registrationNumber;
    private String university;
    private Date startSemDate;
    private Date endSemDate;
    private String email;
    private String password;

    public User(UUID userId, String firstName, String lastName, String registrationNumber, String university, Date startSemDate, Date endSemDate, String email, String password) {
        this.setUserId(userId);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setRegistrationNumber(registrationNumber);
        this.setUniversity(university);
        this.setStartSemDate(startSemDate);
        this.setEndSemDate(endSemDate);
        this.setEmail(email);
        this.setPassword(password);
    }

    public User(String firstName, String lastName, String registrationNumber, String university, Date startSemDate, Date endSemDate, String email, String password) {
        this.setUserId(UUID.randomUUID());
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setRegistrationNumber(registrationNumber);
        this.setUniversity(university);
        this.setStartSemDate(startSemDate);
        this.setEndSemDate(endSemDate);
        this.setEmail(email);
        this.setPassword(password);
    }

    public void save() {
        this.setPassword(Password.hash(this.getPassword()));
        UserController.saveUser(this);
    }

    public void update(String firstName, String lastName, String registrationNumber, String university, Date startSemDate, Date endSemDate) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setRegistrationNumber(registrationNumber);
        this.setUniversity(university);
        this.setStartSemDate(startSemDate);
        this.setEndSemDate(endSemDate);
        UserController.editUser(this);
    }

    public void delete() {
        UserController.deleteUser(this);
    }

    @Override
    public String toString() {
        return String.format("User{'firstName': %s, 'lastName': %s, 'registrationNumber': %s, 'university': %s, 'startSemDate': %s, 'endSemDate': %s, 'email': %s, 'password': %s}",
                this.getFirstName(), this.getLastName(), this.getRegistrationNumber(), this.getUniversity(), this.getStartSemDate(), this.getEndSemDate(), this.getEmail(), this.getPassword());
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Date getStartSemDate() {
        return startSemDate;
    }

    public void setStartSemDate(Date startSemDate) {
        this.startSemDate = startSemDate;
    }

    public Date getEndSemDate() {
        return endSemDate;
    }

    public void setEndSemDate(Date endSemDate) {
        this.endSemDate = endSemDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
