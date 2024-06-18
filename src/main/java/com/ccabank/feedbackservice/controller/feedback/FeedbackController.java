package com.ccabank.feedbackservice.controller.feedback;

import com.ccabank.feedbackservice.domain.AppBaseResult;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.HttpResponse;
import com.ccabank.feedbackservice.dto.HttpResponseError;
import com.ccabank.feedbackservice.dto.HttpResponseSuccess;
import com.ccabank.feedbackservice.dto.feedback.*;
import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.service.impl.AgencyService;
import com.ccabank.feedbackservice.service.impl.FeedbackService;
import com.ccabank.feedbackservice.service.impl.QuestionService;
import io.swagger.annotations.Api;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.controller.country
 * <p>
 * @date: 08/08/2023
 * @time: 11:24
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Api(tags = "Feedback")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AgencyService agencyService;


    @GetMapping("/allFeedback")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> getAllFeedback() {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getAllFeedback();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/feedbackDetails")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> feedbackDetails(@RequestParam(value = "id") String id) {
        AppServiceResult<FeedbackDto> result = feedbackService.getFeedbackById(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<FeedbackDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    //@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    //@PreAuthorize(Authority.FeedBack.ADD_FEEDBACK)
    @PostMapping("/addFeedback")
    @CrossOrigin()
    public ResponseEntity<HttpResponse> addFeedback(@RequestBody FeedbackDto feedbackDto) {
        AppBaseResult result = feedbackService.addFeedback(feedbackDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Feedback successfully added"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }


    @GetMapping("/getFeedbackByStaffAndCreatedAt")
    //@CrossOrigin
    public ResponseEntity<HttpResponse> getFeedbackByStaffAndCreatedAt(@RequestParam(value = "staff") String staff, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByStaffAndCreatedAt(staff, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getFeedbackByAgencyAndCreatedAt")
    //@CrossOrigin
    public ResponseEntity<HttpResponse> getFeedbackByAgencyAndCreatedAt(@RequestParam(value = "agencyCode") String agencyCode, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByAgencyAndCreatedAt(agencyCode, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }


    @GetMapping("/exportFeedbackByStaffAndCreatedAt")
    //@CrossOrigin
    public ResponseEntity exportFeedbackByStaffAndCreatedAt(@RequestParam(value = "staff") String staff, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByStaffAndCreatedAt(staff, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));

        byte[] excelBytes = feedbackService.exportExcelFeedbacks(result.getData());

        // Configurer l'en-tête HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export_Customer_Feedback_" + staff + ".xlsx");
        headers.setContentLength(excelBytes.length);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

    }

    @GetMapping("/exportFeedbackByAgencyAndCreatedAt")
    //@CrossOrigin
    public ResponseEntity exportFeedbackByAgencyAndCreatedAt(@RequestParam(value = "agencyCode") String agencyCode, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByAgencyAndCreatedAt(agencyCode, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));

        byte[] excelBytes = feedbackService.exportExcelFeedbacks(result.getData());

        // Configurer l'en-tête HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export_Customer_Feedback_" + agencyCode + ".xlsx");
        headers.setContentLength(excelBytes.length);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

    }

    @GetMapping("/getStaffEvaluation")
    //@CrossOrigin
    public ResponseEntity<HttpResponse> getStaffEvaluation(@RequestParam(value = "staffUsername") String staffUsername, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<EvaluationPeriodStaffDto> result = feedbackService.getEvaluationStaff(staffUsername, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        return ResponseEntity.ok(new HttpResponseSuccess<EvaluationPeriodStaffDto>(result.getData()));
    }

    @GetMapping("/exportStaffEvaluation")
    //@CrossOrigin
    public ResponseEntity exportStaffEvaluation(@RequestParam(value = "staffUsername") String staffUsername, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<EvaluationPeriodStaffDto> result = feedbackService.getEvaluationStaff(staffUsername, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        byte[] excelBytes = feedbackService.exportExcelEvaluationFeedbacks(result.getData());
        // Configurer l'en-tête HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export_Customer_Feedback_" + result.getData().getUsername() + ".xlsx");
        headers.setContentLength(excelBytes.length);
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/exportAgencyEvaluation")
    //@CrossOrigin
    public ResponseEntity exportAgencyEvaluation(@RequestParam(value = "agencyCode") String agencyCode, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<EvaluationPeriodStaffDto> result = feedbackService.getEvaluationAgency(agencyCode, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        byte[] excelBytes = feedbackService.exportExcelEvaluationFeedbacks(result.getData());
        // Configurer l'en-tête HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export_Customer_Feedback_" + result.getData().getUsername() + ".xlsx");
        headers.setContentLength(excelBytes.length);
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/getAgencyEvaluation")
    //@CrossOrigin
    public ResponseEntity<HttpResponse> getAgencyEvaluation(@RequestParam(value = "agencyCode") String agencyCode, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<EvaluationPeriodStaffDto> result = feedbackService.getEvaluationAgency(agencyCode, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        return ResponseEntity.ok(new HttpResponseSuccess<EvaluationPeriodStaffDto>(result.getData()));
    }

    public  LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

}
