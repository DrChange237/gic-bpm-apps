package com.ccabank.memoservice.process.general.service;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.RequestStatus;
import com.ccabank.memoservice.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public Request confirmRequest(String processInstanceId){
        Request request = requestRepository.findByInstanceId(processInstanceId);
        request.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(request);
        return request;
    }

    public Request rejectRequest(String processInstanceId){
        Request request = requestRepository.findByInstanceId(processInstanceId);
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        return request;
    }
}
