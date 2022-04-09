package account.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    @Size(min = 12, message = "Password must be longer than 12!")
    @NotBlank
    @JsonProperty(namespace = "new_password")
    private String newPassword;

}
