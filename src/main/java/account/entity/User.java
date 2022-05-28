package account.entity;

import lombok.*;
import account.entity.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements EntityMarker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Transient
    private List<String> roles;
    @Column(name = "attempt")
    private int attemptsForLogging = 5;
    @Column
    private boolean locked = false;

}
