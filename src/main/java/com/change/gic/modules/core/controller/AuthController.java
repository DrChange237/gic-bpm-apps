package com.change.gic.modules.core.controller;

import com.change.gic.modules.core.dto.auth.AuthDto;
import com.change.gic.modules.core.info.UserSessionInfo;
import com.change.gic.modules.core.service.faces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/gic")
@RequiredArgsConstructor
@Tag(name = "Authentification")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authentification")
    public ResponseEntity<UserSessionInfo> login(@RequestBody AuthDto req) {
        return ResponseEntity.ok(authService.login(req));
    }

    /**
     * Endpoint de déconnexion simple
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            String username = authService.getCurrentUsername();
            authService.performFullLogout(request, response);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }
}
