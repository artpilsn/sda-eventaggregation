package pl.sdacademy.eventaggregation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Pole nie może być puste")
    @Length(min = 1, max = 15)
    private String firstName;

    @NotNull(message = "Pole nie może być puste")
    @Length(min = 1, max = 15)
    private String lastName;

    @NotNull(message = "Pole nie może być puste")
    @Length(min = 1, max = 50)
    private String username;

    @NotNull(message = "Pole nie może być puste")
    @Length(min = 8, max = 30, message = "Długość hasła powinna mieć od 8 do 30 znaków")
    private String password;

    @Column(name = "user_email")
    @Email(message = "Email should have proper format")
    private String email;

    private Role NORMAL_USER;
}