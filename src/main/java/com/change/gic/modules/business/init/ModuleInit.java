package com.change.gic.modules.business.init;

import com.change.gic.modules.core.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ModuleInit  implements CommandLineRunner {

    private final ModuleRepository moduleRepository;

    @Override
    public void run(String... args) throws Exception {

    }
}
