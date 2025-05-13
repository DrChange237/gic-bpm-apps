package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.entity.camunda.UserCamunda;

public interface UserCamundaService {
    UserCamunda newUser(String userName);

    void newMembership(String userId, String groupId);

    UserCamunda getUser(String userName);

    UserCamunda getUserById(String Id);
}
