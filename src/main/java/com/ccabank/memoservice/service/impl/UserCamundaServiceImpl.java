package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.entity.camunda.MemberShip;
import com.ccabank.memoservice.entity.camunda.UserCamunda;
import com.ccabank.memoservice.repository.MemberShipRepository;
import com.ccabank.memoservice.repository.UserCamundaRepository;
import com.ccabank.memoservice.service.faces.UserCamundaService;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserCamundaServiceImpl implements UserCamundaService {

    @Autowired
    private UserCamundaRepository userCamundaRepository;

    @Autowired
    private MemberShipRepository memberShipRepository;

    @Override
    public UserCamunda newUser(String userName) {
        UserCamunda user = new UserCamunda();
        user.setUserName(userName);
        user.setEmail(userName + "@cca-bank.com");
        String[] fullname = userName.split("\\.");
        if(fullname.length > 1) {
            user.setFirstName(fullname[0].toUpperCase());
            user.setLastName(fullname[1].toUpperCase());
        }
        String userId = userName.replace(".", "");
        //User userEntity = this.mapToEntity(user);
        userCamundaRepository.save(user);
        return user;
    }

    @Override
    public void newMembership(String userId, String groupId) {
        System.out.println("-----------------------------------------------------Dans le fonction: " + userId);

        UserCamunda user = this.getUser(userId);
        if (user == null) {
            System.out.println("-----------------------------------------------------New User: " + userId);
            this.newUser(userId);
        }
        MemberShip memberShip = new MemberShip();
        memberShip.setUserId(userId);
        memberShip.setGroupId(groupId);
        System.out.println("-----------------------------------------------------New membership: " + memberShip);
        memberShipRepository.save(memberShip);
    }


    @Override
    public UserCamunda getUser(String userName) {
        Optional<UserCamunda> user = userCamundaRepository.findByUserName(userName);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public UserCamunda getUserById(String Id) {
        UserCamunda user = userCamundaRepository.getOne(Id);
        return user;
    }



}
