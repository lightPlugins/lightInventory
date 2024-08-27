package io.lightplugins.inventory.util.manager;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MultiFileManager {

    private final String directoryPath;
    @Getter
    private List<File> files;

    public MultiFileManager(String directoryPath) throws IOException {
        this.directoryPath = directoryPath;
        loadYmlFiles();
    }

    private void loadYmlFiles() throws IOException {
        files = new ArrayList<>();
        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        Files.walkFileTree(directory, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".yml")) {
                    files.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void reload() throws IOException {
        loadYmlFiles();
    }

    public String getFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }
}
