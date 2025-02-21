package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class provides file read and write utilities.
 */
public class FileHandler {
    
    /**
     * Reads the content of a file and returns it as a String.
     *
     * @param filePath The path to the file to be read.
     * @return A String containing the contents of the file.
     * @throws IOException If an I/O error occurs while reading from the file.
     */
    public String readFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Append each line of the file to the StringBuilder, adding a newline character
                // after each one to preserve the line breaks in the original file.
                contentBuilder.append(line).append("\n");
            }
        }
        
        // Convert the StringBuilder to a String and return it.
        return contentBuilder.toString();
    }
    
    /**
     * Writes the given content to a file at the specified path. If the file already exists, it
     * will be overwritten.
     *
     * @param content  The content to write to the file.
     * @param filePath The path to the file to be written.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void writeFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the content String to the file.
            writer.write(content);
        }
    }
    
}
