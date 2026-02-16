package com.change.gic.modules.core.controller;

import com.change.gic.modules.core.dto.camunda.CompleteTask;
import com.change.gic.modules.core.dto.camunda.TaskDto;
import com.change.gic.modules.core.info.ProcessInfo;
import com.change.gic.modules.core.info.TaskDetailsResponse;
import com.change.gic.modules.core.service.faces.TaskContextService;
import com.change.gic.modules.core.service.faces.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gic/tasks")
@RequiredArgsConstructor
@Tag(name = "Opération sur les Tasks")
public class TaskController {

    private final TaskService taskService;

    private final TaskContextService taskContextService;


    @GetMapping("/my")
    @Operation(summary = "Liste des Taches de l'utilisateur Connectée")
    public ResponseEntity<List<TaskDto>> mytasks(
    ) {
        return ResponseEntity.ok(taskService.getMyTasks());
    }

    @GetMapping("/all")
    @Operation(summary = "Liste des Taches ")
    public ResponseEntity<List<TaskDto>> list(
    ) {
        return ResponseEntity.ok(taskService.getTasks());
    }

    @GetMapping("/bykey")
    @Operation(summary = "Liste des Taches pour une business Key ")
    public ResponseEntity<List<TaskDto>> bykey(
            @RequestParam(required = false) String businessKey
    ) {
        return ResponseEntity.ok(taskService.getActiveTasksByBusinessKey(businessKey));
    }

    @GetMapping("/full")
    public TaskDetailsResponse getTaskFullContext(@RequestParam String taskId) {
        return taskContextService.getTaskFullContext(taskId);
    }

    @GetMapping("/claim")
    @Operation(summary = "Reclamé une tâche")
    public ResponseEntity<?> claim(
            @RequestParam("taskId") String taskId
    ) {
        taskService.claim(taskId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/unclaim")
    @Operation(summary = "Libéré une tâche")
    public ResponseEntity<?> unclaim(
            @RequestParam("taskId") String taskId
    ) {
        taskService.unClaim(taskId);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/complete")
    @Operation(summary = "Compléter une tâche")
    public ResponseEntity<?> complete(
            @RequestBody CompleteTask completeTask
            ) {
        taskService.completeTask(completeTask);
        return ResponseEntity.ok(true);
    }



}
