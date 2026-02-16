package com.change.gic.modules.core.controller;


import com.change.gic.modules.core.dto.camunda.ProcessDefinitionDto;
import com.change.gic.modules.core.service.faces.ProcessDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gic/camunda")
@RequiredArgsConstructor
@Tag(name = "Opération Camunda")
public class CamundaController {

    private final ProcessDefinitionService processDefinitionService;

    @GetMapping("/process-definition")
    @Operation(summary = "Liste des Processus")
    public ResponseEntity<List<ProcessDefinitionDto>> processList() {
        return ResponseEntity.ok(processDefinitionService.getProcessDefinitions());
    }
}
