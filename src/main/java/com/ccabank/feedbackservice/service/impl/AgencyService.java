package com.ccabank.feedbackservice.service.impl;

import com.ccabank.feedbackservice.constant.AppError;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.AgencyDto;
import com.ccabank.feedbackservice.dto.feedback.AgencyRestDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionChoiceDto;
import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.mappers.AgencyMapper;
import com.ccabank.feedbackservice.openfeign.EntityRestClient;
import com.ccabank.feedbackservice.openfeign.UserRestClient;
import com.ccabank.feedbackservice.repository.AgencyRepository;
import com.ccabank.feedbackservice.service.faces.IAgencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;


@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class AgencyService implements IAgencyService {

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    AgencyMapper agencyMapper;

    @Autowired
    EntityRestClient entityRestClient;

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);


    @Override
    public AppServiceResult<AgencyDto> findAgencyByAgencyCode(String agencyCode) {
        try {
            Agency agency = agencyRepository.findAgencyByAgencyCode(agencyCode);
            if (agency == null) {
                logger.warn(FEEDBACK_DETAIL_SERVICE, "findAgencyByAgencyCode",
                        "Feedback not exist!, Cannot further process!");
                return new AppServiceResult<AgencyDto>(false, AppError.Validattion.errorCode(),
                        "Feedback not exist!", null);
            }
            return new AppServiceResult<AgencyDto>(true, 0, "Succeed!", agencyMapper.toDto(agency));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE, "getFeedbackByAgencyCode : Exception ", e.getMessage());
            return new AppServiceResult<AgencyDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<List<QuestionChoiceDto>> getAllAgencies() {
        try {
            List<AgencyRestDto> agencies = entityRestClient.getAgencies();

            List<QuestionChoiceDto> choices = new ArrayList<>();

            for (AgencyRestDto agency : agencies) {
                QuestionChoiceDto choiceDto = new QuestionChoiceDto();
                choiceDto.setLabel(agency.getName());
                choiceDto.setValue(agency.getName());
                choices.add(choiceDto);
            }

            return new AppServiceResult<List<QuestionChoiceDto>>(true, 0, "Succeed!", choices);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE, "getAllAgencies : Exception ", e.getMessage());
            return new AppServiceResult<List<QuestionChoiceDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }
}
