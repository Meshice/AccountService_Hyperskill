package account.request;

import account.annotation.EmployeeExist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentRequest {

    @Column
    @NotBlank(message = "Employee is null or empty")
    @EmployeeExist
    private String employee;
    @Column
    @Pattern(regexp = "(0[1-9]|1[0-2])-\\d\\d\\d\\d",message = "Wrong date!")
    private String period;
    @Column
    @Min(value = 0,message = "Salary must be non negative!")
    @NotNull(message = "Salary is null")
    private Long salary;

}
