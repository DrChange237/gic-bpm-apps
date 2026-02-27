package com.change.gic.modules.business.controller;


import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.info.InscriptionInfo;
import com.change.gic.modules.business.service.faces.InscriptionService;
import com.change.gic.modules.core.info.DocumentInfo;
import com.change.gic.modules.file.dto.FileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/gic/business/inscription")
@Tag(name = "Operations sur les Inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @Operation(summary = "Rechercher une Inscription")
    @GetMapping("")
    public ResponseEntity<List<InscriptionInfo>> searchInscription(
            @RequestParam(required = false) String search,
            HttpServletRequest request
    ){
        return ResponseEntity.ok(inscriptionService.search(search));
    }

    @Operation(summary = "Rechercher une Inscription")
    @GetMapping("/download")
    public ResponseEntity<FileDto> downloadDocument(
            @RequestParam(required = true) String reference,
            @RequestParam(required = true) String tag
    ){
        return ResponseEntity.ok(inscriptionService.downloadDocument(reference, tag));
    }
}
