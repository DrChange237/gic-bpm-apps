package com.change.gic.modules.business.init;


import com.change.gic.modules.core.entity.ActivityUserTask;
import com.change.gic.modules.core.repository.ActivityUserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActivityInit implements CommandLineRunner {

    private final ActivityUserTaskRepository activityUserTaskRepository;

    @Override
    public void run(String... args) throws Exception {

        HashMap<String, String> consultation = new HashMap<>();
        consultation.put("firstName", "Noms du client");
        consultation.put("lastName", "Prénoms du client");
        consultation.put("diploma", "Dernier Diplôme");
        consultation.put("yearGraduation", "Année d'obtention");
        consultation.put("experience", "Expérience");


        List<ActivityUserTask> activityUserTasks = Stream.of(

                new ActivityUserTask("consultation", consultation)
        ).filter(activityUserTask -> !activityUserTaskRepository.existsByTaskDefinitionKey(activityUserTask.getTaskDefinitionKey())).collect(Collectors.toList());
        activityUserTaskRepository.saveAll(activityUserTasks);

    }

}
