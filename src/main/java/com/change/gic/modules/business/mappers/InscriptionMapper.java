package com.change.gic.modules.business.mappers;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.info.InscriptionInfo;
import com.change.gic.modules.core.mappers.EntityMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface InscriptionMapper extends EntityMapper<InscriptionInfo, Inscription> {
}
