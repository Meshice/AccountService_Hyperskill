package account.dto;

import account.annotation.EmployeeExist;
import account.annotation.PaymentUnique;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
@JsonPropertyOrder({"employee","period","salary"})
@PaymentUnique
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto implements DtoMarker {

    @NotBlank(message = "Employee's email mustn't be empty")
    @EmployeeExist
    private String employee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-yyyy")
//    @Pattern(regexp = "(0[1-9]|1[0-2])-\\d\\d\\d\\d",message = "Payment's period must have format:'MM-yyyy'")
    private Date period;

    @Min(value = 0, message = "Employee's salary must be non-negative!")
    @NotNull(message = "Employee's salary must be written!")
    private int salary;

}



