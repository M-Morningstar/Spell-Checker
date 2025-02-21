package dictionaryTest;

import java.io.IOException;
import dictionary.SystemDictionary;

/**
 * This class contains test methods for the SystemDictionary class.
 */
public class SystemDictionaryTest {

    /**
     * Main method to run various test scenarios for the SystemDictionary class.
     * 
     * @param args Command line arguments (not used in this context).
     */
    public static void main(String[] args) {
        testDictionaryLoading();
        testWordCheck();
        testInvalidWordCheck();
        stressTestWordCheck();
    }

    /**
     * Tests the loading of the dictionary file by creating a SystemDictionary instance.
     */
    private static void testDictionaryLoading() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("words_alpha.txt");
            System.out.println("testDictionaryLoading: Success");
        } catch (IOException e) {
            System.err.println("testDictionaryLoading: Failed to load dictionary - " + e.getMessage());
        }
    }

    /**
     * Tests the isInDictionary method by checking a valid word.
     */
    private static void testWordCheck() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("words_alpha.txt");
            String testWord = "example"; // Choose a word you are sure is in the dictionary
            if (systemDictionary.isInDictionary(testWord)) {
                System.out.println("testWordCheck: Success");
            } else {
                System.err.println("testWordCheck: Failed, valid word not recognized.");
            }
        } catch (IOException e) {
            System.err.println("testWordCheck: Failed to load dictionary for word check - " + e.getMessage());
        }
    }
    
    /**
     * Tests the isInDictionary method by checking an invalid word.
     */
    private static void testInvalidWordCheck() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("words_alpha.txt");
            String testWord = "invalidwordxyz"; // Assumed invalid word
            if (!systemDictionary.isInDictionary(testWord)) {
                System.out.println("testInvalidWordCheck: Success");
            } else {
                System.err.println("testInvalidWordCheck: Failed, invalid word recognized as valid.");
            }
        } catch (IOException e) {
            System.err.println("testInvalidWordCheck: Failed to load dictionary for invalid word check - " + e.getMessage());
        }
    }

    /**
     * Conducts a stress test by checking a large number of words against the dictionary.
     */
    private static void stressTestWordCheck() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("words_alpha.txt");
            boolean testPassed = true;
            for (int i = 0; i < 100000; i++) { // Stress test with a large number of checks
                String testWord = "test" + i;
                if (!systemDictionary.isInDictionary(testWord)) {
                    continue;
                } else {
                    System.err.println("stressTestWordCheck: Failed, invalid word recognized as valid.");
                    testPassed = false;
                    break;
                }
            }
            if (testPassed) {
                System.out.println("stressTestWordCheck: Success");
            }
        } catch (IOException e) {
            System.err.println("stressTestWordCheck: Failed to load dictionary for stress test - " + e.getMessage());
        }
    }
}
