package account.dto;

import account.annotation.DateValid;
import account.annotation.EmployeeExist;
import account.annotation.PaymentUnique;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Getter
@Setter
@JsonPropertyOrder({"employee","period","salary"})
@PaymentUnique
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {

    @NotBlank(message = "Employee's email mustn't be empty")
    @EmployeeExist
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String employee;

    @DateValid(pattern = "dd-MM-yyyy")
    private String period;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastname;

    @Min(value = 0, message = "Employee's salary must be non-negative!")
    @NotNull(message = "Employee's salary must be written!")
    private String salary;

}



