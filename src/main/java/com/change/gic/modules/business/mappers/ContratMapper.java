package com.change.gic.modules.business.mappers;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.info.ContratInfo;
import com.change.gic.modules.core.mappers.EntityMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ContratMapper extends EntityMapper<ContratInfo, Contrat> {
}
