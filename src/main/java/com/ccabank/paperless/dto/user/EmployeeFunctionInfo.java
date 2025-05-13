package com.ccabank.paperless.dto.user;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeFunctionInfo {

    private FunctionInfo function;

    private Date startDate;

    private Date endDate;

}
