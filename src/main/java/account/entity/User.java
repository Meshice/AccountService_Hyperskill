package account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email")
    @NotEmpty(message = "Email is empty")
    @Pattern(regexp = ".+@acme\\.com$", message = "Wrong email")
    private String email;
    @Column(name = "name")
    @NotEmpty(message = "Name is empty")
    private String name;
    @Column(name = "lastname")
    @NotEmpty(message = "Lastname is empty")
    private String lastname;
    @Column(name = "password")
    @Size(min = 12, message = "Password must be longer than 12!")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonIgnore
    @Column(name = "role")
    private String role;
    @Transient
    private List<String> roles;
    @JsonIgnore
    @Column(name = "attempt")
    private int attemptsForLogging = 5;
    @JsonIgnore
    @Column
    private boolean locked = false;
}
