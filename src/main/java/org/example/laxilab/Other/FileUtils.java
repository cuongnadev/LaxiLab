package org.example.laxilab.Other;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        java.util.Scanner scanner = new java.util.Scanner(file);
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return content.toString();
    }

    public static void writeFile(String content, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}
