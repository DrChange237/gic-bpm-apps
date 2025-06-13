package com.ccabank.paperless.process.general.service;

import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

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
