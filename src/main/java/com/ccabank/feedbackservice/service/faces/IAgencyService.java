package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.AgencyDto;

public interface IAgencyService {

    AppServiceResult<AgencyDto> findAgencyByAgencyCode(String agencyCode);

}
