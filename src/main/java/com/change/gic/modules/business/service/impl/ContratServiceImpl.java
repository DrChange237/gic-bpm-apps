package com.change.gic.modules.business.service.impl;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.info.ContratInfo;
import com.change.gic.modules.business.mappers.ContratMapper;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.service.faces.ContratService;
import com.change.gic.modules.business.specification.ContratSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratServiceImpl implements ContratService {

    private final ContratRepository contratRepository;
    private final ContratMapper contratMapper;

    @Override
    public List<ContratInfo> search(String search) {
        Specification<Contrat> spec = Specification.where(null);
        spec = spec.and(ContratSpecifications.withDynamicQuery(search));
        List<Contrat> contrats = contratRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));
        return contratMapper.toDto(contrats);
    }

}
