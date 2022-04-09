package account.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleChangeRequest {

    private String user;
    private String role;
    @Pattern(regexp = "GRANT|REMOVE", message = "Use only GRAND and REMOVE operations!")
    private String operation;

}
