package com.ccabank.memoservice.util.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class FileReader {

    // Injecter le chemin du fichier .txt à l'aide de @Value
    @Value("classpath:signature.txt")
    private Resource dataFile;

    public String readDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataFile.getInputStream()))) {
            // Lire tout le contenu du fichier en une seule chaîne de caractères
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier", e);
        }
    }
}
