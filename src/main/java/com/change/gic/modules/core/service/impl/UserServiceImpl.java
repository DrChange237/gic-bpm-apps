package com.change.gic.modules.core.service.impl;

import com.change.gic.modules.business.entity.Agency;
import com.change.gic.modules.business.repository.AgencyRepository;
import com.change.gic.modules.core.dto.auth.RegisterUserDto;
import com.change.gic.modules.core.entity.AppUser;
import com.change.gic.modules.core.entity.Role;
import com.change.gic.modules.core.info.UserInfo;
import com.change.gic.modules.core.mappers.UserMapper;
import com.change.gic.modules.core.repository.AppUserRepository;
import com.change.gic.modules.core.repository.RoleRepository;
import com.change.gic.modules.core.service.faces.UserService;
import com.change.gic.modules.core.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AgencyRepository agencyRepository;
    private final RoleRepository roleRepository;
    private final IdentityService identityService;

    @Override
    public List<UserInfo> getUsers(){
        List<AppUser> users = userRepository.findAll();
        return  userMapper.toDto(users);
    }

    @Override
    public void activateUser(String username){
        AppUser user = userRepository.findByUsername(username).orElse(null);
        if(user != null){
            user.setEnabled(true);
        }
    }

    @Override
    @Transactional
    public UserInfo createUser(RegisterUserDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username déjà utilisé");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        Agency agency = agencyRepository.findById(request.getAgencyId()).orElse(null);
        if (agency == null) {
            throw new IllegalArgumentException("Agency n'existe pas");
        }

        Role role = roleRepository.findBySlug(request.getRole());
        AppUser user = new AppUser();
        user.setAgency(agency);
        user.setRole(role);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());


        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);

        // ---- 2️⃣ Création utilisateur Camunda ----
        org.camunda.bpm.engine.identity.User camundaUser = identityService.newUser(request.getUsername());
        camundaUser.setEmail(request.getEmail());
        camundaUser.setPassword(request.getPassword()); // stocké en clair dans Camunda DB, Spring hashé dans ton app
        identityService.saveUser(camundaUser);

        // ---- 3️⃣ Assigner les groupes Camunda selon les rôles ----
        String groupId = role.getSlug(); // ex: ROLE_ADMIN, ROLE_USER
        // Vérifie si le groupe existe sinon le créer
        if (identityService.createGroupQuery().groupId(groupId).singleResult() == null) {
            org.camunda.bpm.engine.identity.Group group = identityService.newGroup(groupId);
            group.setName(groupId);
            group.setType("ROLE"); // type générique
            identityService.saveGroup(group);
        }
        // Assigner l’utilisateur au groupe
        identityService.createMembership(request.getUsername(), groupId);

        return userMapper.toDto(user);
    }
}
