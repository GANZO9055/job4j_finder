package org.finder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Finder {

    private static void checkArgs(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Number of arguments does not match");
        }
    }

    private static String convertMaskToRegex(String paramOne) {
        return paramOne.replace("?", "\\S{1}")
                .replace(".", "\\.")
                .replace("*", "\\S*");
    }

    public static Predicate<Path> searchPath(String name, String type) {
        if (type.equals("name")) {
            return path -> path.getFileName().toString().equals(name);
        }
        if (type.equals("mask")) {
            return path -> path.getFileName().toString()
                    .matches(convertMaskToRegex(name));
        }
        if (type.equals("regex")) {
            Pattern pattern = Pattern.compile(name);
            return path -> pattern.matcher(path.getFileName().toString()).matches();
        }
        throw new IllegalArgumentException("Invalid type.");
    }

    public static List<Path> searchFiles(String directory, Predicate<Path> predicate) {
        Path path = Paths.get(directory);
        SearchFiles searchFiles = new SearchFiles(predicate);

        try {
            Files.walkFileTree(path, searchFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchFiles.getPaths();
    }

    public static void writeFiles(List<Path> files, String writeToFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(writeToFile))) {
            for (Path file : files) {
                writer.write(file.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        checkArgs(args);

        ArgsName argsName = ArgsName.of(args);
        String directory = argsName.get("d");
        String name = argsName.get("n");
        String type = argsName.get("t");
        String path = argsName.get("o");

        Predicate<Path> predicate = searchPath(name, type);
        List<Path> list = searchFiles(directory, predicate);

        writeFiles(list, path);
    }
}