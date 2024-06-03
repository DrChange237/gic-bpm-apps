package com.ccabank.feedbackservice.controller.feedback;

import com.ccabank.feedbackservice.domain.AppBaseResult;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.HttpResponse;
import com.ccabank.feedbackservice.dto.HttpResponseError;
import com.ccabank.feedbackservice.dto.HttpResponseSuccess;
import com.ccabank.feedbackservice.dto.feedback.EvaluationPeriodStaffDto;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionDto;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.service.impl.FeedbackService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/allFeedback")
    @CrossOrigin()
    public ResponseEntity<HttpResponse> getAllFeedback() {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getAllFeedback();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/feedbackDetails")
    @CrossOrigin()
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

    @GetMapping("/getFeedbackByStaff")
    @CrossOrigin
    public ResponseEntity<HttpResponse> getFeedbackByStaff(@RequestParam(value = "name") String username) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByStaffUsername(username);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }



    @GetMapping("/getFeedbackByStaffAndCreatedAt")
    @CrossOrigin
    public ResponseEntity<HttpResponse> getFeedbackByStaffAndCreatedAt(@RequestParam(value = "staff") String staff, @RequestParam(value = "startAt") LocalDate startAt,  @RequestParam(value = "endAt") LocalDate endAt ) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByStaffAndCreatedAt(staff, startAt, endAt);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getEvaluation")
    @CrossOrigin
    public ResponseEntity<HttpResponse> getEvaluation(@RequestParam(value = "staffUsername") String staffUsername, @RequestParam(value = "startAt") String startAt,  @RequestParam(value = "endAt") String endAt ) {
        AppServiceResult<EvaluationPeriodStaffDto> result = feedbackService.getEvaluationStaff(staffUsername, convertStringToLocalDate(startAt), convertStringToLocalDate(endAt));
        return ResponseEntity.ok(new HttpResponseSuccess<AppServiceResult<EvaluationPeriodStaffDto>>(result));
    }

    public  LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

}
