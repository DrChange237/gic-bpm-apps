package com.change.gic.modules.business.init;

import com.change.gic.modules.business.entity.Program;
import com.change.gic.modules.business.entity.TestLang;
import com.change.gic.modules.business.repository.TestLangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TestLangInit implements CommandLineRunner {

    private final TestLangRepository testLangRepository;

    @Override
    public void run(String... args) throws Exception {

        List<TestLang> tests = Stream.of(

                new TestLang("TCF", "Test de Connaissance Française"),
                new TestLang("TEF", "Test d'Evaluation Français"),
                new TestLang("IELTS", "International English Language Testing System")

                ).filter(test -> !testLangRepository.existsBySlug(test.getSlug())).collect(Collectors.toList());
        testLangRepository.saveAll(tests);

    }
}
