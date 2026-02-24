package com.change.gic.modules.business.mappers;

import com.change.gic.modules.business.entity.MoneyMovement;
import com.change.gic.modules.business.info.MoneyMovementInfo;
import com.change.gic.modules.core.mappers.EntityMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MoneyMovementMapper extends EntityMapper<MoneyMovementInfo, MoneyMovement> {
}
