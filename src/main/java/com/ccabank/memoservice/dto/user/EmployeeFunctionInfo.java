package com.ccabank.memoservice.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class EmployeeFunctionInfo {

    private FunctionInfo function;

    private Date startDate;

    private Date endDate;

}
