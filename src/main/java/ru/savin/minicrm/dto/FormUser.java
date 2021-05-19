package ru.savin.minicrm.dto;

import ru.savin.minicrm.validation.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@FieldMatch.List({@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must " +
        "match")
})
public class FormUser {

    @NotBlank(message = "is required")
    @Size(min = 3, message = "is required")
    private String userName;

    @NotBlank(message = "is required")
    @Size(min = 3, max = 20, message = "the field must be between 3-20 characters")
    private String password;

    @NotBlank(message = "is required")
    @Size(min = 3, max = 20, message = "the field must be between 3-20 characters")
    private String matchingPassword;

    @NotBlank(message = "is required")
    @Size(min = 2, max = 30, message = "the field must be between 2-30 characters")
    private String firstName;

    @NotBlank(message = "is required")
    @Size(min = 2, max = 30, message = "the field must be between 2-30 characters")
    private String lastName;

    @Email(message = "email must be valid")
    @NotBlank(message = "is required")
    @Size(min = 3, message = "is required")
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "FormUser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
