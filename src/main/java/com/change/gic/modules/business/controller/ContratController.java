package com.change.gic.modules.business.controller;

import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.info.ContratInfo;
import com.change.gic.modules.business.info.InscriptionInfo;
import com.change.gic.modules.business.info.MoneyMovementInfo;
import com.change.gic.modules.business.service.faces.ContratService;
import com.change.gic.modules.business.service.faces.MoneyMovementService;
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
@RequestMapping("/gic/business/contrat")
@Tag(name = "Operations sur les Contrats")
public class ContratController {

    private final ContratService contratService;
    private final MoneyMovementService moneyMovementService;

    @Operation(summary = "Rechercher un Contrat")
    @GetMapping("")
    public ResponseEntity<List<ContratInfo>> searchContrat(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean archived
    ){
        return ResponseEntity.ok(contratService.search(search, archived));
    }

    @Operation(summary = "Archiver un Contrat")
    @GetMapping("/archived")
    public ResponseEntity<?> archiverContrat(
            @RequestParam(required = true) String reference
    ){
        contratService.archived(reference);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Rechercher les versements sur un dossier")
    @GetMapping("/money")
    public ResponseEntity<List<MoneyMovementInfo>> searchMovement(
            @RequestParam(required = true) String reference
    ){
        return ResponseEntity.ok(moneyMovementService.findMovementByReference(reference));
    }
}
