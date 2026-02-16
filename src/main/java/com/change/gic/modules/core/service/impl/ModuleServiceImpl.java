package com.change.gic.modules.core.service.impl;

import com.change.gic.modules.core.info.ModuleInfo;
import com.change.gic.modules.core.entity.Module;
import com.change.gic.modules.core.mappers.ModuleMapper;
import com.change.gic.modules.core.repository.ModuleRepository;
import com.change.gic.modules.core.service.faces.ModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Override
    public List<ModuleInfo> getModules(){
        List<Module> modules = moduleRepository.findAllByOrderByPositionAsc();
        return moduleMapper.toDto(modules);
    }


}
