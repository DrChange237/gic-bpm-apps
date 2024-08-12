package com.ccabank.memoservice.openfeign;


import com.ccabank.memoservice.dto.reporting.*;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(value = "reportingRestClient", url = "https://developer.ccabank-app.com/sandbox", configuration = FeignClientProperties.FeignClientConfiguration.class)
@FeignClient(name = "REPORTING-SERVICE")
public interface ReportingRestClient {

    @PostMapping(path = "/api/reporting/vacation/sheet")
    ByteArrayResource vacation(@RequestBody VacationForm form);

    @PostMapping(path = "/api/reporting/service/resumption")
    ByteArrayResource resumption(@RequestBody ResumptionForm form);

    @PostMapping(path = "/api/reporting/mission/order")
    ByteArrayResource mission(@RequestBody MissionForm form);

    @PostMapping(path = "/api/reporting/absence/authorization")
    ByteArrayResource absence(@RequestBody AbsenceForm form);

    @PostMapping(path = "/api/reporting/absence/authorization")
    ByteArrayResource memo(@RequestBody MemoForm form);


}
