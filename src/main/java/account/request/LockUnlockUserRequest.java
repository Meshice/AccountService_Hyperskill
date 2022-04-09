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
public class LockUnlockUserRequest {

    private String user;
    @Pattern(regexp = "LOCK|UNLOCK", message = "Only LOCK or UNLOCK")
    private String operation;

}
