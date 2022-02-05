package account.request;

import account.annotation.EmployeeExist;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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

    public UpdatePaymentRequest() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
