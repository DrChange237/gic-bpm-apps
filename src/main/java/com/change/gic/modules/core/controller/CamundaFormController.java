package com.change.gic.modules.core.controller;


import com.change.gic.modules.core.dto.camunda.form.CamundaFormResponseDto;
import com.change.gic.modules.core.service.faces.CamundaService;
import com.change.gic.modules.core.service.faces.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/gic/camunda/forms")
@Tag(name = "Camunda Forms", description = "API pour récupérer les formulaires Camunda")
public class CamundaFormController {

    @Autowired
    private CamundaService camundaFormService;

    @Autowired
    private TaskService taskService;

    /**
     * Récupère le formulaire de démarrage par clé de processus (dernière version)
     */
    @Operation(summary = "Récupère le formulaire de démarrage",
            description = "Retourne le formulaire Camunda (.form) associé au StartEvent du processus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulaire récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Processus ou formulaire non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CamundaFormResponseDto> getStartFormByProcessKey(
            @Parameter(description = "Clé du processus (ex: inscription-process)")
            @RequestParam String processKey) {

        CamundaFormResponseDto response = camundaFormService.getStartFormByProcessKey(processKey);

        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/start/task")
    public ResponseEntity<CamundaFormResponseDto> getTaskForm(@RequestParam String taskId) {
        try {
            CamundaFormResponseDto form = camundaFormService.getTaskForm(taskId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(form);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère le formulaire de démarrage par clé et version de processus
     */
    @Operation(summary = "Récupère le formulaire de démarrage avec version spécifique")
    @GetMapping(value = "/start/{processKey}/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CamundaFormResponseDto> getStartFormByProcessKeyAndVersion(
            @Parameter(description = "Clé du processus")
            @PathVariable String processKey,
            @Parameter(description = "Version du processus (optionnel, dernière version par défaut)")
            @RequestParam(required = false) Integer version) {

        CamundaFormResponseDto response = camundaFormService
                .getStartFormByProcessKeyAndVersion(processKey, version);

        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Récupère le formulaire de démarrage par ID de définition de processus
     */
    @Operation(summary = "Récupère le formulaire par ID de définition de processus")
    @GetMapping(value = "/start/definition/{processDefinitionId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CamundaFormResponseDto> getStartFormByProcessDefinitionId(
            @Parameter(description = "ID de la définition du processus")
            @PathVariable String processDefinitionId) {

        CamundaFormResponseDto response = camundaFormService
                .getStartFormByProcessDefinitionId(processDefinitionId);

        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Récupère uniquement le JSON brut du formulaire (sans métadonnées)
     */
    @Operation(summary = "Récupère le JSON brut du formulaire")
    @GetMapping(value = "/start/{processKey}/raw", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStartFormRawJson(
            @Parameter(description = "Clé du processus")
            @PathVariable String processKey) {
        try {
            String formJson = camundaFormService.getStartFormJsonByProcessKey(processKey);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(formJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Gestion globale des erreurs
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CamundaFormResponseDto> handleException(Exception e) {
        CamundaFormResponseDto errorResponse = CamundaFormResponseDto.builder()
                .success(false)
                .message("Internal server error: " + e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @GetMapping("/form-variables")
    @Operation(summary = "Recupérer les variables d'une tâche")
    public ResponseEntity<Map<String, Object>> getTaskFormVariables(@RequestParam String taskId) {
        return ResponseEntity.ok(taskService.getTaskFormVariables(taskId));
    }
}
