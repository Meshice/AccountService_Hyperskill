package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id","email","name","lastname", "roles"})
public class UserDto implements DtoMarker {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @Pattern(regexp = "\\w+@\\w+.\\w+", message = "User's email must have pattern:'name@mail.com'!")
    private String email;

    @NotBlank(message = "User's name must be not blank!")
    private String name;

    @NotBlank(message = "User's lastname must be not blank!")
    private String lastname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, message = "User's password must have 8 or more characters!")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> roles;

}
