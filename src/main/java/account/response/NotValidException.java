package account.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotValidException {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public NotValidException(HttpStatus httpStatus, String path, String message) {
        timestamp = LocalDateTime.now().toString();
        status = httpStatus.value();
        error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
