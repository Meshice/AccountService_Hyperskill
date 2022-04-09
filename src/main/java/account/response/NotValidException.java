package account.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
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
