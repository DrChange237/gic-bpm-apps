package com.ccabank.feedbackservice.controller.country;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.HttpResponse;
import com.ccabank.feedbackservice.dto.HttpResponseError;
import com.ccabank.feedbackservice.dto.HttpResponseSuccess;
import com.ccabank.feedbackservice.dto.country.FeedbackDto;
import com.ccabank.feedbackservice.service.impl.FeedbackService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @GetMapping("/getFeedbackByStaff")
    public ResponseEntity<HttpResponse> getFeedbackByStaff(@Valid @RequestParam(value = "name") String username) {
        AppServiceResult<FeedbackDto> result = feedbackService.getFeedbackByStaffUsername(username);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<FeedbackDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }


}
