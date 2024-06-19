package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.AgencyDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionChoiceDto;

import java.util.List;

public interface IAgencyService {

    AppServiceResult<AgencyDto> findAgencyByAgencyCode(String agencyCode);

    AppServiceResult<List<QuestionChoiceDto>> getAllAgencies();
}
