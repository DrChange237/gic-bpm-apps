package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.memo.ApprovalDto;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import java.util.List;

public interface MapService {
    List<ApprovalDto> mapTaskToApprovalDto(List<HistoricTaskInstance> historics);
}
