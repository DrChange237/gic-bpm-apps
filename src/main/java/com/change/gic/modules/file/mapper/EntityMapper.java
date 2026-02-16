package com.change.gic.modules.file.mapper;

public interface EntityMapper <D, E>{

    E toEntity(D dto);

    D toDto(E entity);
}
