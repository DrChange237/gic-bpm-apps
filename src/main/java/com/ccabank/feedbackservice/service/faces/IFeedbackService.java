package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.country.FeedbackDto;

import java.util.List;

public interface IFeedbackService {
    AppServiceResult<List<FeedbackDto>> getAllFeedback();

    AppServiceResult<FeedbackDto> getFeedbackById(String id);

    AppServiceResult<FeedbackDto> getFeedbackByStaffUsername(String staffUsername);

}
