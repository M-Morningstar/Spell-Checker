package ioTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import io.FileHandler;

/**
 * This class contains test methods for the FileHandler class.
 */
public class FileHandlerTest {

    /**
     * Main method to run various test scenarios for the FileHandler class.
     * 
     * @param args Command line arguments (not used in this context).
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        testWriteFile();
        testReadFile();
        testFileNotFound();
        testEmptyFile();
        testLargeFile();
    }

    /**
     * Tests the writeFile method of the FileHandler class.
     */
    private static void testWriteFile() {
        FileHandler fileHandler = new FileHandler();
        String filename = "testWriteFile.txt";
        String content = "Hello, World!";
        
        try {
            fileHandler.writeFile(content, filename);
            System.out.println("testWriteFile: Success");
        } catch (IOException e) {
            System.err.println("testWriteFile: IOException was thrown when trying to write.");
            e.printStackTrace();
        } finally {
            // Clean up: delete the test file
            new File(filename).delete();
        }
    }

    /**
     * Tests the readFile method of the FileHandler class.
     */
    private static void testReadFile() {
        FileHandler fileHandler = new FileHandler();
        String filename = "testReadFile.txt";
        String content = "Hello, World!";
        
        try {
            fileHandler.writeFile(content, filename);
            String readContent = fileHandler.readFile(filename);
            if (content.equals(readContent.trim())) {
                System.out.println("testReadFile: Success");
            } else {
                System.err.println("testReadFile: Content mismatch.");
            }
        } catch (IOException e) {
            System.err.println("testReadFile: IOException was thrown.");
            e.printStackTrace();
        } finally {
            // Clean up: delete the test file
            new File(filename).delete();
        }
    }

    /**
     * Tests the scenario when the file to read is not found.
     * 
     * @throws IOException If an I/O error occurs.
     */
    private static void testFileNotFound() throws IOException {
        FileHandler fileHandler = new FileHandler();
        String filename = "nonExistentFile.txt";
        
        try {
            fileHandler.readFile(filename);
            System.err.println("testFileNotFound: Failed, expected IOException for non-existent file.");
        } catch (FileNotFoundException e) {
            System.out.println("testFileNotFound: Success, IOException was thrown as expected.");
        }
    }
    
    /**
     * Tests the writeFile and readFile methods with an empty file.
     */
    private static void testEmptyFile() {
        FileHandler fileHandler = new FileHandler();
        String filename = "testEmptyFile.txt";
        String content = "";

        try {
            fileHandler.writeFile(content, filename);
            String readContent = fileHandler.readFile(filename);
            if ("".equals(readContent.trim())) {
                System.out.println("testEmptyFile: Success");
            } else {
                System.err.println("testEmptyFile: Failed, content is not empty.");
            }
        } catch (IOException e) {
            System.err.println("testEmptyFile: IOException was thrown.");
            e.printStackTrace();
        } finally {
            new File(filename).delete();
        }
    }
    
    /**
     * Tests the writeFile and readFile methods with a large file.
     */
    private static void testLargeFile() {
        FileHandler fileHandler = new FileHandler();
        String filename = "testLargeFile.txt";
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeContent.append("Line ").append(i).append("\n");
        }

        try {
            fileHandler.writeFile(largeContent.toString(), filename);
            String readContent = fileHandler.readFile(filename);
            if (largeContent.toString().equals(readContent)) {
                System.out.println("testLargeFile: Success");
            } else {
                System.err.println("testLargeFile: Failed, content mismatch.");
            }
        } catch (IOException e) {
            System.err.println("testLargeFile: IOException was thrown.");
            e.printStackTrace();
        } finally {
            new File(filename).delete();
        }
    }
}
