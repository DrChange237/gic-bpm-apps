package com.change.gic.modules.business.init;

import com.change.gic.modules.core.repository.TestNotationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestNotationInit implements CommandLineRunner {

    private final TestNotationRepository TestNotationRepository;

    @Override
    public void run(String... args) throws Exception {

    }
}
