package com.change.gic.modules.business.process.contract.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EvaluateContractListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        log.info("Evaluate contract listener");

        List<String> term_prep_doc = (List<String>) delegateExecution.getVariable("term_prep_doc");
        boolean ifPreDoc = term_prep_doc != null && term_prep_doc.size() > 0;
        delegateExecution.setVariable("ifPreDoc", ifPreDoc);
        boolean ifEquivalence = false;
        boolean ifTest = false;
        if(term_prep_doc.contains("prep_doc_2")){
            ifEquivalence = true;
        }
        if(term_prep_doc.contains("prep_doc_4")){
            ifTest = true;
        }
        delegateExecution.setVariable("ifEquivalence", ifEquivalence);
        delegateExecution.setVariable("ifTest", ifTest);

    }

}
