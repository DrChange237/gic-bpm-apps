package com.ccabank.paperless.openfeign;


import com.ccabank.paperless.dto.reporting.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "REPORTING-SERVICE")
public interface ReportingRestClient {

    @PostMapping(path = "/api/reporting/vacation/sheet")
    ByteArrayResource vacation(@RequestBody VacationForm form);

    @PostMapping(path = "/api/reporting/vacation/decision")
    ByteArrayResource vacationDecision(@RequestBody VacationDecision form);

    @PostMapping(path = "/api/reporting/service/resumption")
    ByteArrayResource resumption(@RequestBody ResumptionForm form);

    @PostMapping(path = "/api/reporting/mission/order")
    ByteArrayResource mission(@RequestBody MissionForm form);

    @PostMapping(path = "/api/reporting/absence/authorization")
    ByteArrayResource absence(@RequestBody AbsenceForm form);

    @PostMapping(path = "/api/reporting/memo/sheet")
    ByteArrayResource memo(@RequestBody MemoForm form);

    @PostMapping(path = "/api/reporting/work/sheet")
    ByteArrayResource workform(@RequestBody WorkForm form);

    @PostMapping(path = "/api/reporting/purchase/sheet")
    ByteArrayResource purchase(@RequestBody PurchaseForm form);

    @PostMapping(path = "/api/reporting/handover/sheet/v2")
    ByteArrayResource handover(@RequestBody HandOverForm form);

    @PostMapping(path = "/api/reporting/interim/letter")
    ByteArrayResource interim(@RequestBody InterimForm form);
}
