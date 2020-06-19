package pl.sdacademy.eventaggregation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

/*
    @Id
    @GeneratedValue
    private Long id;
*/

    @NotNull(message = "Can't be empty")
    @Length(min = 1, max = 15, message = "1 to 15 characters required")
    private String firstName;

    @NotNull(message = "Can't be empty")
    @Length(min = 1, max = 15, message = "1 to 15 characters required")
    private String lastName;

    @Id
    @NotNull(message = "Can't be empty")
    @Length(min = 1, max = 50, message = "1 to 50 characters required")
    private String username;

    //validation moved to register.html
    @NotNull(message = "Can't be empty")
    private String password;

    @Column(name = "user_email")
    @Email(message = "Email should have proper format")
    private String email;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String firstName, String lastName, String username, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}