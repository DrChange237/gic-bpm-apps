package com.ccabank.feedbackservice.controller.feedback;

import com.ccabank.feedbackservice.domain.AppBaseResult;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.HttpResponse;
import com.ccabank.feedbackservice.dto.HttpResponseError;
import com.ccabank.feedbackservice.dto.HttpResponseSuccess;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.service.impl.FeedbackService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<HttpResponse> getAllFeedback() {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getAllFeedback();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/feedbackDetails")
    public ResponseEntity<HttpResponse> countryDetails(@Valid @RequestParam(value = "id") String id) {
        AppServiceResult<FeedbackDto> result = feedbackService.getFeedbackById(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<FeedbackDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    //@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    //@PreAuthorize(Authority.FeedBack.ADD_FEEDBACK)
    @PostMapping("/addFeedback")
    public ResponseEntity<HttpResponse> addFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        AppBaseResult result = feedbackService.addFeedback(feedbackDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Feedback successfully added"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getFeedbackByStaff")
    public ResponseEntity<HttpResponse> getFeedbackByStaff(@Valid @RequestParam(value = "name") String username) {
        AppServiceResult<FeedbackDto> result = feedbackService.getFeedbackByStaffUsername(username);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<FeedbackDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }



    @GetMapping("/getFeedbackByStaffAndCreatedAt")
    public ResponseEntity<HttpResponse> getFeedbackByStaffAndCreatedAt(@Valid @RequestParam(value = "staff") String staff, @RequestParam(value = "startAt") LocalDateTime startAt,  @RequestParam(value = "endAt") LocalDateTime endAt ) {
        AppServiceResult<List<FeedbackDto>> result = feedbackService.getFeedbackByStaffAndCreatedAt(staff, startAt, endAt);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<FeedbackDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getRank")
    public ResponseEntity<HttpResponse> getRank(@Valid @RequestParam(value = "startAt") LocalDateTime startAt,  @RequestParam(value = "endAt") LocalDateTime endAt, @RequestParam(value="property") String property, @RequestParam(value="size") int size ) {
        Page<Feedback> result = feedbackService.findRank(startAt, endAt, property, size);
        return ResponseEntity.ok(new HttpResponseSuccess<Page<Feedback>>(result));
    }




}
