package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.EvaluationPeriodStaffDto;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.entity.Feedback;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IFeedbackService {
    AppServiceResult<List<FeedbackDto>> getAllFeedback();

    AppServiceResult<FeedbackDto> getFeedbackById(String id);

    AppServiceResult<List<FeedbackDto>> getFeedbackByStaffAndCreatedAt(String staff, LocalDate startAt, LocalDate endAt);

    Page<Feedback> findRank(LocalDateTime startAt, LocalDateTime endAt, String property, int limit);


    AppServiceResult<List<FeedbackDto>> getFeedbackByStaffUsername(String staffUsername);

    AppServiceResult<FeedbackDto> addFeedback(FeedbackDto feedbackDto);

    AppServiceResult<List<FeedbackDto>> getFilterFeedback(String property);

    AppServiceResult<EvaluationPeriodStaffDto> getEvaluationStaff(String staffUsername, LocalDate startAt, LocalDate endAt);

    AppServiceResult<EvaluationPeriodStaffDto> getEvaluationAgency(String agencyCode, LocalDate startAt, LocalDate endAt);





}
