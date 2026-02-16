package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.Role;
import com.change.gic.modules.core.info.RoleInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface RoleMapper extends EntityMapper<RoleInfo, Role>{

}
