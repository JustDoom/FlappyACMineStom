package com.justdoom.flappyanticheat;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    public static CommentedConfigurationNode root;

    public Config() {


        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("./myproject.yml")) // Set where we will load and save to
                .build();

        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }
    }

    public static boolean doesFileExist(String filename) {
        Path path = Path.of(filename);
        if (!Files.exists(path)) {
            return false;
        }
        return true;
    }

    public static void createFile(String filename) throws IOException {
        Path path = Path.of(filename);
        File file = new File(String.valueOf(path));
        file.createNewFile();
    }

    public static void createDirectory(String path) throws IOException {
        Files.createDirectory(Path.of(path));
    }
}