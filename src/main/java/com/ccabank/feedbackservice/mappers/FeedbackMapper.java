package com.ccabank.feedbackservice.mappers;

import com.ccabank.feedbackservice.dto.country.FeedbackDto;
import com.ccabank.feedbackservice.entity.Feedback;
import org.mapstruct.Mapper;

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
public interface FeedbackMapper extends EntityMapper<FeedbackDto, Feedback> {
}
