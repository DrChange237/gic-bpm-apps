package com.change.gic.modules.business.service.impl;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.info.InscriptionInfo;
import com.change.gic.modules.business.mappers.InscriptionMapper;
import com.change.gic.modules.business.repository.InscriptionRepository;
import com.change.gic.modules.business.service.faces.InscriptionService;
import com.change.gic.modules.business.specification.InscriptionSpecifications;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.info.DocumentInfo;
import com.change.gic.modules.core.mappers.DocumentMapper;
import com.change.gic.modules.core.repository.DocumentRepository;
import com.change.gic.modules.file.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final InscriptionMapper inscriptionMapper;

    @Override
    public List<InscriptionInfo> search(String search) {
        Specification<Inscription> spec = Specification.where(null);
        spec = spec.and(InscriptionSpecifications.withDynamicQuery(search));
        Pageable pageable = PageRequest.of(0, inscriptionRepository.findAll().size(), Sort.by(Sort.Direction.DESC, "creationDate"));
        List<Inscription> inscriptions = inscriptionRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));
        return inscriptionMapper.toDto(inscriptions);
    }

    @Override
    public FileDto downloadDocument(String reference, String tag) {
        Document document = documentRepository.findByBusinessKeyAndTag(reference, tag);
        DocumentInfo documentInfo =  documentMapper.toDto(document);
        return documentInfo.getFile();
    }

}
