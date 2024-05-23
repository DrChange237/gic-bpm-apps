package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.country.FeedbackDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IFeedbackService {
    AppServiceResult<List<FeedbackDto>> getAllFeedback();

    AppServiceResult<FeedbackDto> getFeedbackById(String id);

    AppServiceResult<List<FeedbackDto>> getFeedbackByStaffAndCreatedAt(String staff, LocalDateTime startAt, LocalDateTime endAt);

    AppServiceResult<FeedbackDto> getFeedbackByStaffUsername(String staffUsername);

    AppServiceResult<FeedbackDto> addFeedback(FeedbackDto feedbackDto);


}
