package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.entity.camunda.MemberShip;
import com.ccabank.paperless.entity.camunda.UserCamunda;
import com.ccabank.paperless.repository.MemberShipRepository;
import com.ccabank.paperless.repository.UserCamundaRepository;
import com.ccabank.paperless.service.faces.UserCamundaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCamundaServiceImpl implements UserCamundaService {

    private final UserCamundaRepository userCamundaRepository;

    private final MemberShipRepository memberShipRepository;

    @Override
    public UserCamunda newUser(String userName) {
        UserCamunda user = new UserCamunda();
        user.setUserName(userName);
        user.setEmail(userName + "@cca-bank.com");
        String[] fullName = userName.split("\\.");
        if(fullName.length > 1) {
            user.setFirstName(fullName[0].toUpperCase());
            user.setLastName(fullName[1].toUpperCase());
        }
        userCamundaRepository.save(user);
        return user;
    }

    @Override
    public void newMembership(String userId, String groupId) {
        log.info("-----------------------------------------------------Dans le fonction: {}", userId);

        UserCamunda user = this.getUser(userId);
        if (user == null) {
            log.info("-----------------------------------------------------New User: {}", userId);
            this.newUser(userId);
        }
        MemberShip memberShip = new MemberShip();
        memberShip.setUserId(userId);
        memberShip.setGroupId(groupId);
        log.info("-----------------------------------------------------New membership: {}", memberShip);
        memberShipRepository.save(memberShip);
    }


    @Override
    public UserCamunda getUser(String userName) {
        return userCamundaRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public UserCamunda getUserById(String Id) {
        return userCamundaRepository.findById(Id).orElse(null);
    }

}
