package com.change.gic.modules.business.service.impl;

import com.change.gic.modules.business.entity.MoneyMovement;
import com.change.gic.modules.business.info.MoneyMovementInfo;
import com.change.gic.modules.business.mappers.MoneyMovementMapper;
import com.change.gic.modules.business.repository.MoneyMovementRepository;
import com.change.gic.modules.business.service.faces.MoneyMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoneyMovementServiceImpl implements MoneyMovementService {

    private final MoneyMovementRepository moneyMovementRepository;
    private final MoneyMovementMapper moneyMovementMapper;

    @Override
    public List<MoneyMovementInfo> findMovementByReference(String reference){
        List<MoneyMovement> moneyMovement = moneyMovementRepository.findByReference(reference);
        return moneyMovementMapper.toDto(moneyMovement);
    }
}
