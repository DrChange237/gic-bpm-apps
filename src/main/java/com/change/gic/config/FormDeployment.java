package com.change.gic.config;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormDeployment {

    private final RepositoryService repositoryService;
    private final ResourceLoader resourceLoader;


    @EventListener(ApplicationReadyEvent.class)
    public void deployForms() {

        try {
            Resource[] resources =
                    ResourcePatternUtils
                            .getResourcePatternResolver(resourceLoader)
                            .getResources("classpath:*.form");

            if (resources.length == 0) {
                System.out.println("⚠️ Aucun fichier .form trouvé");
                return;
            }

            DeploymentBuilder deployment = repositoryService
                    .createDeployment()
                    .name("camunda-forms-auto");

            for (Resource resource : resources) {
                deployment.addInputStream(
                        resource.getFilename(),
                        resource.getInputStream()
                );
            }

            deployment.deploy();
            System.out.println("✅ Déploiement des forms terminé");

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du déploiement des forms", e);
        }
    }
}
