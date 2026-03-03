package com.change.gic.modules.business.process.express.listener;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.SelectionArrima;
import com.change.gic.modules.business.entity.SelectionExpress;
import com.change.gic.modules.business.enumeration.SelectionArrimaStatus;
import com.change.gic.modules.business.enumeration.SelectionExpressStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.SelectionExpressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreatedProfilExpressListener implements ExecutionListener {

    private final SelectionExpressRepository selectionExpressRepository;
    private final ContratRepository contratRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String reference = delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);
        SelectionExpress selectionExpress = new SelectionExpress();
        String numero = delegateExecution.getVariable("id_express").toString();
        Long points = Long.parseLong(delegateExecution.getVariable("points").toString());
        selectionExpress.setNumero(numero);
        selectionExpress.setPoints(BigDecimal.valueOf(points));
        selectionExpress.setContract(contrat);
        selectionExpress.setStatus(SelectionExpressStatus.PROFIL_CREATED);
        selectionExpressRepository.save(selectionExpress);
    }
}
