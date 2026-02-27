package com.change.gic.modules.business.init;

import com.change.gic.modules.core.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessInit  implements CommandLineRunner {

    private final ProcessRepository processRepository;

    @Override
    public void run(String... args) throws Exception {

    }
}
