package com.change.gic.modules.business.init;

import com.change.gic.modules.business.entity.ContratTermGroup;
import com.change.gic.modules.business.entity.Program;
import com.change.gic.modules.business.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProgramInit implements CommandLineRunner {

    private final ProgramRepository programRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Program> programs = Stream.of(

                new Program("ARRIMA", "Programme de Sélection ARRIMA (QUEBEC)"),
                new Program("EXPRESS", "Programme de Sélection Entrée Express (FEDERAL)")

        ).filter(program -> !programRepository.existsByName(program.getName())).collect(Collectors.toList());
        programRepository.saveAll(programs);
    }
}
