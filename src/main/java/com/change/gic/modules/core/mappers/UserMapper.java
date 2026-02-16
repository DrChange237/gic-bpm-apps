package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.AppUser;
import com.change.gic.modules.core.info.UserInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper extends EntityMapper<UserInfo, AppUser> {
}
