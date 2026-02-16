package com.change.gic.modules.core.service.impl;

import com.change.gic.modules.core.dto.auth.AuthDto;
import com.change.gic.modules.core.dto.auth.ChangePasswordDto;
import com.change.gic.modules.core.entity.AppUser;
import com.change.gic.modules.core.entity.UserSession;
import com.change.gic.modules.core.info.UserSessionInfo;
import com.change.gic.modules.core.mappers.UserSessionMapper;
import com.change.gic.modules.core.repository.AppUserRepository;
import com.change.gic.modules.core.repository.UserSessionRepository;
import com.change.gic.modules.core.service.faces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final  AuthenticationManager authenticationManager;
    private final UserSessionMapper userSessionMapper;
    private final UserSessionRepository sessionRepository;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdentityService camundaIdentityService;

    @Override
    public void changePassword(String username, ChangePasswordDto req) {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Vérifier ancien mot de passe
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        // 2️⃣ Vérifier confirmation
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // 3️⃣ Encoder et sauvegarder
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        // 4️⃣ (Optionnel) Synchroniser avec Camunda
        if (camundaIdentityService != null) {
            User camundaUser =
                    camundaIdentityService.createUserQuery()
                            .userId(username)
                            .singleResult();

            if (camundaUser != null) {
                camundaUser.setPassword(req.getNewPassword());
                camundaIdentityService.saveUser(camundaUser);
            }
        }
    }


    @Override
    public UserSessionInfo login(AuthDto authDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getUsername(), authDto.getPassword()
                )
        );
        String token = jwtService.generateToken(authDto.getUsername());
        UserSession session = new UserSession();
        session.setToken(token);
        AppUser user = userRepository.findByUsername(authDto.getUsername()).orElseThrow(null);
        log.info(user.getEmail());
        session.setUser(user);
        session.setDate(LocalDateTime.now());
        sessionRepository.save(session);
        return userSessionMapper.toDto(session);
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // aucun utilisateur connecté
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        log.info(principal == null ? "null" : principal.toString());
        return principal.getUsername();
    }

    /**
     * Déconnexion avec invalidation de session et nettoyage des cookies
     */
    @Override
    public void performFullLogout(HttpServletRequest request,
                                  HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            String username = auth.getName();
            log.info("Déconnexion complète avec nettoyage pour: {}", username);

            // Configuration du handler de logout
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.setInvalidateHttpSession(true);
            logoutHandler.setClearAuthentication(true);

            // Exécution du logout
            logoutHandler.logout(request, response, auth);

            log.info("Déconnexion réussie pour: {}", username);
        }
    }

}
