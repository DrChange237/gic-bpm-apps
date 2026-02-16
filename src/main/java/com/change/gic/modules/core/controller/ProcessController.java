package com.change.gic.modules.core.controller;

import com.change.gic.modules.core.dto.camunda.ProcessStartRequest;
import com.change.gic.modules.core.dto.camunda.ProcessStartResponse;
import com.change.gic.modules.core.info.ModuleInfo;
import com.change.gic.modules.core.info.ProcessInfo;
import com.change.gic.modules.core.service.faces.AuthService;
import com.change.gic.modules.core.service.faces.CamundaService;
import com.change.gic.modules.core.service.faces.ProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gic/process")
@RequiredArgsConstructor
@Tag(name = "Opération sur les Processus")
public class ProcessController {

    private final ProcessService processService;

    /**
     * Endpoint pour démarrer un processus depuis le formulaire frontend
     */
    @PostMapping("/start")
    public ResponseEntity<ProcessStartResponse> startProcess(
            @RequestBody ProcessStartRequest request) {

        ProcessStartResponse response = processService.startProcessWithInitiator(
                request.getProcessDefinitionKey(),
                request.getFormData(),
                request.getBusinessKey()
        );

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("")
    @Operation(summary = "Liste des Processus d'un Module")
    public ResponseEntity<List<ProcessInfo>> processes(
            @RequestParam("moduleId") String module
    ) {
        return ResponseEntity.ok(processService.processListByModule(module));
    }

    @GetMapping("/all")
    @Operation(summary = "Liste des Processus")
    public ResponseEntity<List<ProcessInfo>> findAllProcesses(
    ) {
        return ResponseEntity.ok(processService.processList());
    }

}
