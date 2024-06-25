package com.ccabank.feedbackservice.util.pdf;


import com.ccabank.feedbackservice.dto.feedback.EvaluationPeriodStaffDto;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;

@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class PdfGenerator {

    public void generateReport(List<FeedbackDto> feedbackDtos, EvaluationPeriodStaffDto evaluation){

              

    }

}
