package com.ccabank.feedbackservice.service.impl;

import com.ccabank.feedbackservice.constant.AppError;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionDto;
import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.mappers.FeedbackMapper;
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
                logger.warn(FEEDBACK_DETAIL_SERVICE, "getCountryById",
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
            List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaffUsernameAndCreatedAtBetween(staff, startAt, endAt);

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
    public Map<String, Map<String, Double>> getEvaluationStaff(String staffUsername, LocalDate startAt, LocalDate endAt) {

        List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaffUsernameAndCreatedAtBetween(staffUsername, startAt, endAt);

        Map<String, Map<String, Double>> statistics = new HashMap<>();

        for (Feedback feedback : feedbacks) {
            Map<String, Double> questionStats = statistics.getOrDefault(staffUsername, new HashMap<>());

            // Itérer sur chaque question et calculer le pourcentage
            for (Answer answer : feedback.getAnswerCollection()) {
                QuestionDto questionDto = questionService.getQuestion(answer.getQuestion());
                if(questionDto.getType() != "1-5"){
                    continue;
                }
                int questionScore = Integer.parseInt(answer.getAnswer());
                String questionName = answer.getQuestion();
                double percentage = calculatePercentage(questionScore, 5);
                questionStats.put(questionName, percentage);
            }

            statistics.put(staffUsername, questionStats);
        }

        return statistics;
    }

    private double calculatePercentage(int value, int max) {
        return (double) value / max * 100;
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
