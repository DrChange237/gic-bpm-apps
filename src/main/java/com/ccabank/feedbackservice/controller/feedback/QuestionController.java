package com.ccabank.feedbackservice.controller.feedback;

import com.ccabank.feedbackservice.domain.AppBaseResult;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.HttpResponse;
import com.ccabank.feedbackservice.dto.HttpResponseError;
import com.ccabank.feedbackservice.dto.HttpResponseSuccess;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionDto;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.service.impl.FeedbackService;
import com.ccabank.feedbackservice.service.impl.QuestionService;
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
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/allQuestions")
    public ResponseEntity<HttpResponse> getAllQuestions(@RequestParam(value = "lang") String lang) {

        try{
            List<QuestionDto> questions = questionService.getAllQuestions(lang);
            return ResponseEntity.ok(new HttpResponseSuccess<List<QuestionDto>>(questions));

        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }

}
