package com.ccabank.feedbackservice.service.impl;

import com.ccabank.feedbackservice.service.faces.IFormService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;


@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class FormService implements IFormService {

}
