package com.ccabank.feedbackservice.service.impl;

import com.ccabank.feedbackservice.constant.AppError;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.EvaluationItem;
import com.ccabank.feedbackservice.dto.feedback.EvaluationPeriodStaffDto;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionDto;
import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.mappers.FeedbackMapper;
import com.ccabank.feedbackservice.repository.AgencyRepository;
import com.ccabank.feedbackservice.repository.FeedbackRepository;
import com.ccabank.feedbackservice.service.faces.IFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;


@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class FeedbackService implements IFeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AgencyRepository agencyRepository;


    @Override
    public AppServiceResult<List<FeedbackDto>> getAllFeedback() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();
            return getConvertedResult(feedbacks, "getAllFeedback ");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }



    @Override
    public AppServiceResult<FeedbackDto> getFeedbackById(String id) {
        try {
            Feedback feedback = feedbackRepository.findById(Long.parseLong(id)).orElse(null);
            if (feedback == null) {
                logger.warn(FEEDBACK_DETAIL_SERVICE, "getFeedbackById",
                        "Feedback not exist!, Cannot further process!");
                return new AppServiceResult<FeedbackDto>(false, AppError.Validattion.errorCode(),
                        "Feedback not exist!", null);
            }
            return new AppServiceResult<FeedbackDto>(true, 0, "Succeed!", feedbackMapper.toDto(feedback));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE, "getFeedbackById : Exception ", e.getMessage());
            return new AppServiceResult<FeedbackDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<List<FeedbackDto>> getFeedbackByStaffAndCreatedAt(String staff, LocalDate startAt, LocalDate endAt) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaffUsernameAndCreatedAtBetween(staff, startAt.atStartOfDay(), endAt.atStartOfDay());

            return getConvertedResult(feedbacks, "getFeedbackByStaffAndCreatedAt ");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public Page<Feedback> findRank(LocalDateTime startAt, LocalDateTime endAt, String property, int limit) {
        Pageable pageable = PageRequest.of(1, limit);
        try {
            Page<Feedback> feedbacks = feedbackRepository.findRank(pageable, startAt, endAt, property);
            return feedbacks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AppServiceResult<List<FeedbackDto>> getFeedbackByStaffUsername(String staffUsername) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaff(staffUsername);
            return getConvertedResult(feedbacks, "getFeedbackByStaffUsername");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<FeedbackDto> addFeedback(FeedbackDto feedbackDto) {
        try {
            logger.info(FEEDBACK_DETAIL_SERVICE + "addFeedback : methode invocation");
            Feedback feedback = feedbackMapper.toEntity(feedbackDto);
            feedback.setCreatedAt(LocalDateTime.now());
            for (Answer answer : feedback.getAnswerCollection()) {
                answer.setFeedback(feedback);
            }
            feedback = feedbackRepository.save(feedback);
            FeedbackDto dto = feedbackMapper.toDto(feedback);
            return new AppServiceResult<FeedbackDto>(true, 0, "Succeed!", dto );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<FeedbackDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<FeedbackDto>> getFilterFeedback(String property) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();

            //List<Feedback> feeds =  feedbacks.stream().filter(entity ->  entity.getProfessionalism() == property).collect(Collectors.toList()) ;

            return getConvertedResult(feedbacks, "getFilterFeedback ");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<EvaluationPeriodStaffDto> getEvaluationStaff(String staffUsername, LocalDate startAt, LocalDate endAt) {

        List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaffUsernameAndCreatedAtBetween(staffUsername, startAt.atStartOfDay(), endAt.atStartOfDay());

        logger.error(FEEDBACK_DETAIL_SERVICE + " Feedbacks retrieve" + String.valueOf(feedbacks.size()), "" );


        EvaluationPeriodStaffDto evaluation = new EvaluationPeriodStaffDto();
        evaluation.setUsername(staffUsername);
        evaluation.setStartAt(startAt);
        evaluation.setEndAt(endAt);

        List<EvaluationItem> evaluations = evaluation.getEvaluations();

        List<QuestionDto> questionDtos = questionService.getAllQuestions("fr");

        for(QuestionDto questionDto: questionDtos){

            if(!questionDto.getType().equals("1-5")){
                continue;
            }

            EvaluationItem item = new EvaluationItem();
            item.setElement(questionDto.getProperty());
            item.setLabel(questionDto.getLabel());
            int score = 0;
            int total = 0;
            int count = 0;
            float percentage = 0;


            for (Feedback feedback : feedbacks) {
                    // Itérer sur chaque question et calculer le pourcentage
                    for (Answer answer : feedback.getAnswerCollection()) {

                        if(!questionDto.getProperty().equals(answer.getQuestion())){
                            continue;
                        }

                        score =  score + Integer.parseInt(answer.getAnswer());
                        count = count + 1;
                        total = total + 5;
                    }
                    if(total > 0){
                        percentage = ((float) score /total) * 100;
                    }
            }
            item.setPourcent(percentage);
            item.setCount(count);
            evaluations.add(item);
            evaluation.setEvaluations(evaluations);
        }

        return new AppServiceResult<EvaluationPeriodStaffDto>(true, 0, "Succeed!", evaluation);
    }

    @Override
    public AppServiceResult<EvaluationPeriodStaffDto> getEvaluationAgency(String agencyCode, LocalDate startAt, LocalDate endAt) {

        Agency agency = agencyRepository.findAgencyByAgencyCode(agencyCode);

        List<Feedback> feedbacks = feedbackRepository.findFeedbackByAgencyAndCreatedAtBetween(agency, startAt.atStartOfDay(), endAt.atStartOfDay());

        EvaluationPeriodStaffDto evaluation = new EvaluationPeriodStaffDto();
        evaluation.setUsername(agencyCode);
        evaluation.setStartAt(startAt);
        evaluation.setEndAt(endAt);

        List<EvaluationItem> evaluations = evaluation.getEvaluations();

        List<QuestionDto> questionDtos = questionService.getAllQuestions("fr");

        for(QuestionDto questionDto: questionDtos){

            if(!questionDto.getType().equals("1-5")){
                continue;
            }

            EvaluationItem item = new EvaluationItem();
            item.setElement(questionDto.getProperty());
            item.setLabel(questionDto.getLabel());
            int score = 0;
            int total = 0;
            int count = 0;
            float percentage = 0;


            for (Feedback feedback : feedbacks) {
                // Itérer sur chaque question et calculer le pourcentage
                for (Answer answer : feedback.getAnswerCollection()) {

                    if(!questionDto.getProperty().equals(answer.getQuestion())){
                        continue;
                    }

                    score =  score + Integer.parseInt(answer.getAnswer());
                    count = count + 1;
                    total = total + 5;
                }
                if(total > 0){
                    percentage = ((float) score /total) * 100;
                }
            }
            item.setPourcent(percentage);
            item.setCount(count);
            evaluations.add(item);
            evaluation.setEvaluations(evaluations);
        }

        return new AppServiceResult<EvaluationPeriodStaffDto>(true, 0, "Succeed!", evaluation);
    }

    private float calculatePercentage(int value, int max) {
        return (float) value / max * 100;
    }


    private AppServiceResult<List<FeedbackDto>> getConvertedResult(List<Feedback> feedbacks, String functionName) {
        if (feedbacks == null) {
            logger.warn(FEEDBACK_DETAIL_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Validattion.errorCode(),
                    "Feedback not exist!", null);
        }
        List<FeedbackDto> result =  new ArrayList<FeedbackDto>();
        if (feedbacks.size() > 0) {
            for (Feedback feedback : feedbacks) {
                result.add(feedbackMapper.toDto(feedback));
            }
        }
        return new AppServiceResult<List<FeedbackDto>>(true, 0, "Succeed!", result);
    }
}
