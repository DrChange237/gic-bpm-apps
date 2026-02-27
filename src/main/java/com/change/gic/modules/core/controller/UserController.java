package com.change.gic.modules.core.controller;

import com.change.gic.modules.core.dto.auth.RegisterUserDto;
import com.change.gic.modules.core.info.UserInfo;
import com.change.gic.modules.core.service.faces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gic/user")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Enregistrer un utilisateur")
    public ResponseEntity<UserInfo> register(@RequestBody RegisterUserDto req) {
        return ResponseEntity.ok(userService.createUser(req));
    }

    @GetMapping("")
    @Operation(summary = "Enregistrer un utilisateur")
    public ResponseEntity<List<UserInfo>> list() {
        return ResponseEntity.ok(userService.getUsers());
    }

}
