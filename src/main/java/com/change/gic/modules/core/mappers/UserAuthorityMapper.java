package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.UserAuthority;
import com.change.gic.modules.core.info.UserAuthorityInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserAuthorityMapper extends EntityMapper<UserAuthorityInfo, UserAuthority>  {

}
