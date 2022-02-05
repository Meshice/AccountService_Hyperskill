package account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "users")
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
    private int attemptsForLogging;
    @JsonIgnore
    @Column
    private boolean locked = false;

    public User() {
        this.attemptsForLogging = 5;
    }

    public User(String email, String name, String lastname, String password) {
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
    }

    public int getAttemptsForLogging() {
        return attemptsForLogging;
    }

    public void setAttemptsForLogging(int attemptsForLogging) {
        this.attemptsForLogging = attemptsForLogging;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", roles=" + roles +
                ", attemptsForLogging=" + attemptsForLogging +
                ", locked=" + locked +
                '}';
    }
}
