package com.change.gic.modules.business.service.impl;

import com.change.gic.modules.business.entity.*;
import com.change.gic.modules.business.enumeration.EquivalenceStatus;
import com.change.gic.modules.business.enumeration.SelectionArrimaStatus;
import com.change.gic.modules.business.info.ContratInfo;
import com.change.gic.modules.business.mappers.ContratMapper;
import com.change.gic.modules.business.repository.*;
import com.change.gic.modules.business.service.faces.ContratService;
import com.change.gic.modules.business.specification.ContratSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratServiceImpl implements ContratService {

    private final ContratRepository contratRepository;
    private final ContratMapper contratMapper;

    private final EquivalenceRepository equivalenceRepository;
    private final TestExamRepository testExamRepository;
    private final MoneyMovementRepository moneyMovementRepository;
    private final SelectionArrimaRepository selectionArrimaRepository;
    private final SelectionExpressRepository selectionExpressRepository;
    private final PermanentResidentRepository permanentResidentRepository;



    @Override
    public void archived(String reference){
        Contrat contrat = contratRepository.findByReference(reference);
        contrat.setArchived(true);
        contratRepository.save(contrat);
    }

    @Override
    public List<ContratInfo> search(String search, Boolean archived) {
        Specification<Contrat> spec = Specification.where(ContratSpecifications.archived(archived));
        if (search != null && !search.isEmpty()) {
            spec = spec.and(ContratSpecifications.withDynamicQuery(search));
        }
        List<Contrat> contrats = contratRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));
        List<ContratInfo> contratInfos = contratMapper.toDto(contrats);
        return contratInfos.stream().map(x -> mapInfo(x, contratMapper.toEntity(x))).collect(Collectors.toList());
    }

    private ContratInfo mapInfo(ContratInfo contratInfo, Contrat contrat) {

        Optional<Equivalence> equivalence = equivalenceRepository.findByContract(contrat);
        if(contrat.getEquivalence()) {
            if (equivalence.isPresent()) {
                if(equivalence.get().getStatus() != null) {
                    contratInfo.setEquivalenceStatus(equivalence.get().getStatus().name());
                }
                if(equivalence.get().getDiplomaStatus() != null) {
                    contratInfo.setDiplomaStatus(equivalence.get().getDiplomaStatus().name());
                }
                //}
            }
        }else{
            contratInfo.setEquivalenceStatus(EquivalenceStatus.COMPLETED.name());
        }

        if(contrat.getTestLang()){
            Optional<TestExam> testExam = testExamRepository.findByContract(contrat);
            if (testExam.isPresent()) {
                contratInfo.setTestExamStatus(testExam.get().getStatus().name());
            }
        }else {
            contratInfo.setTestExamStatus(TestExamStatus.SUCCESS.name());
        }


        Optional<SelectionExpress> selectionExpressOptional = selectionExpressRepository.findByContract(contrat);
        if (selectionExpressOptional.isPresent()) {
            contratInfo.setSelectionStatus(selectionExpressOptional.get().getStatus().name());
        }

        if(contratInfo.getSelectionStatus() != "SUCCESS"){
            Optional<SelectionArrima> selectionArrimaOptional = selectionArrimaRepository.findByContract(contrat);
            if (selectionArrimaOptional.isPresent()) {
                contratInfo.setSelectionStatus(contratInfo.getSelectionStatus() + " - " + selectionArrimaOptional.get().getStatus().name());
                if(selectionArrimaOptional.get().getStatus().equals(SelectionArrimaStatus.SUCCESS)){
                    contratInfo.setSelectionStatus(selectionArrimaOptional.get().getStatus().name());
                }
            }
        }


        Optional<PermanentResident> permanentResidentOptional = permanentResidentRepository.findByContract(contrat);
        if (permanentResidentOptional.isPresent()) {
            contratInfo.setPermanentStatus(permanentResidentOptional.get().getStatus().name());
        }

        List<MoneyMovement> moneyMovements = moneyMovementRepository.findByReferenceAndFees(contrat.getReference(), true);
        BigDecimal allMoney = BigDecimal.ZERO;
        for (MoneyMovement moneyMovement : moneyMovements) {
            allMoney = allMoney.add(moneyMovement.getAmount());
        }
        contratInfo.setRestToPay(contratInfo.getTotalAmount().subtract(allMoney));

        return contratInfo;

    }

}
