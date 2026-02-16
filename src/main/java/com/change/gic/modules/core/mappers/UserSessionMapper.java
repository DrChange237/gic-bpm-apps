package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.UserSession;
import com.change.gic.modules.core.info.UserSessionInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserSessionMapper extends EntityMapper<UserSessionInfo, UserSession> {
}
