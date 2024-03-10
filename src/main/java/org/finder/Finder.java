package org.finder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Finder {
    private static String path;
    private static List<String> param = List.of("d", "n", "t", "o");
    private static List<String> valuesFile = List.of("name", "mask", "regex");

    private static void checkArgs(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Number of arguments does not match");
        }
    }

    public static void writeFiles(List<Path> files) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Path file : files) {
                writer.write(file.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        checkArgs(args);

        List<Path> list = new ArrayList<>();

        writeFiles(list);
    }
}