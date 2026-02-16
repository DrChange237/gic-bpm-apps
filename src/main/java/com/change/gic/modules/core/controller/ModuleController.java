package com.change.gic.modules.core.controller;

import com.change.gic.modules.core.info.ModuleInfo;
import com.change.gic.modules.core.service.faces.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gic/module")
@RequiredArgsConstructor
@Tag(name = "Opération sur les Modules")
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("")
    @Operation(summary = "Liste des Modules")
    public ResponseEntity<List<ModuleInfo>> moduleList() {
        return ResponseEntity.ok(moduleService.getModules());
    }
}
