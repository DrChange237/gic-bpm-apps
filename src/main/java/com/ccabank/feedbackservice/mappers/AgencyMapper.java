package com.ccabank.feedbackservice.mappers;

import com.ccabank.feedbackservice.dto.feedback.AgencyDto;
import com.ccabank.feedbackservice.dto.feedback.AnswerDto;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.mappers
 * <p>
 * @date: 08/08/2023
 * @time: 11:48
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Mapper(componentModel = "spring")
public interface AgencyMapper extends EntityMapper<AgencyDto, Agency> {

}


