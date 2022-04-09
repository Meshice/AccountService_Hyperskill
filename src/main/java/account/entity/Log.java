package account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@AllArgsConstructor
public class Log implements EntityMarker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String date;
    @Column
    private String action;
    @Column
    private String subject;
    @Column
    private String object;
    @Column
    private String path;

    public Log() {
        date = LocalDateTime.now().toString();
    }

}
