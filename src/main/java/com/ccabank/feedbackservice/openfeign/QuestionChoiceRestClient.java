package com.ccabank.feedbackservice.openfeign;


import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.QuestionChoiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "FEEDBACK-SERVICE" , configuration = FeignClientProperties.FeignClientConfiguration.class)
//@FeignClient(value = "userRestClient", url = "http://localhost:6589/api", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface QuestionChoiceRestClient {
    @GetMapping(path = "/api/feedbacks/{path}")
    AppServiceResult<List<QuestionChoiceDto>> getChoices(@PathVariable("path") String path);
}
