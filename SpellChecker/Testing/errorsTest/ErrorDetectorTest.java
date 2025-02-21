package errorsTest;

import dictionary.SystemDictionary;
import dictionary.UserDictionary;
import errors.ErrorDetector;
import processing.TextProcessor.ProcessedWord;
import processing.TextProcessor.PuncAfterWord;
import processing.ProblemWord;
import processing.ProblemWord.ProblemType;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

/**
 * This class contains test methods for the ErrorDetector class.
 */
public class ErrorDetectorTest {

    /**
     * Main method to run various test scenarios for the ErrorDetector class.
     * 
     * @param args Command line arguments (not used in this context).
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        testDetectMisspellings();
        testDetectCapitalizationErrors();
        testDetectDoubleWords();
        testEmptyInput();
        testMixedCapitalization();
    }

    /**
     * Tests the detection of misspelled words using the ErrorDetector class.
     * 
     * @throws IOException If an I/O error occurs.
     */
    private static void testDetectMisspellings() throws IOException {
        SystemDictionary systemDict = new SystemDictionary("words_alpha.txt");
        UserDictionary userDict = new UserDictionary();
        ErrorDetector errorDetector = new ErrorDetector(systemDict, userDict);

        List<ProcessedWord> words = Arrays.asList(
            new ProcessedWord("cat", null, false),
            new ProcessedWord("dgo", null, false) // misspelled word
        );

        // Check if any misspelling is detected
        boolean misspellingDetected = errorDetector.detectErrors(words).stream()
            .anyMatch(p -> p.getProblemType() == ProblemType.MISSPELLING);

        printTestResult("testDetectMisspellings", misspellingDetected);
    }

    /**
     * Tests the detection of capitalization errors using the ErrorDetector class.
     * 
     * @throws IOException If an I/O error occurs.
     */
    private static void testDetectCapitalizationErrors() throws IOException {
        SystemDictionary systemDict = new SystemDictionary("words_alpha.txt");
        UserDictionary userDict = new UserDictionary();
        ErrorDetector errorDetector = new ErrorDetector(systemDict, userDict);

        List<ProcessedWord> words = Arrays.asList(
            new ProcessedWord("This", PuncAfterWord.PERIOD, true),
            new ProcessedWord("is", null, false),
            new ProcessedWord("apple", null, false) // capitalization error
        );

        // Check if any capitalization error is detected
        boolean capitalizationErrorDetected = errorDetector.detectErrors(words).stream()
            .anyMatch(p -> p.getProblemType() == ProblemType.CAPITALIZATION);

        printTestResult("testDetectCapitalizationErrors", capitalizationErrorDetected);
    }

    /**
     * Tests the detection of double words using the ErrorDetector class.
     * 
     * @throws IOException If an I/O error occurs.
     */
    private static void testDetectDoubleWords() throws IOException {
        SystemDictionary systemDict = new SystemDictionary("words_alpha.txt");
        UserDictionary userDict = new UserDictionary();
        ErrorDetector errorDetector = new ErrorDetector(systemDict, userDict);

        List<ProcessedWord> words = Arrays.asList(
            new ProcessedWord("the", null, false),
            new ProcessedWord("the", null, false) // double word
        );

        // Check if any double word is detected
        boolean doubleWordDetected = errorDetector.detectErrors(words).stream()
            .anyMatch(p -> p.getProblemType() == ProblemType.DOUBLE_WORD);

        printTestResult("testDetectDoubleWords", doubleWordDetected);
    }

    /**
     * Tests the handling of empty input by the ErrorDetector class.
     * 
     * @throws IOException If an I/O error occurs.
     */
    private static void testEmptyInput() throws IOException {
        SystemDictionary systemDict = new SystemDictionary("words_alpha.txt");
        UserDictionary userDict = new UserDictionary();
        ErrorDetector errorDetector = new ErrorDetector(systemDict, userDict);

        // Detect errors in an empty list of words
        List<ProblemWord> detectedProblems = errorDetector.detectErrors(Arrays.asList());
        boolean testPassed = detectedProblems.isEmpty();
        printTestResult("testEmptyInput", testPassed);
    }

    /**
     * Tests the detection of mixed capitalization errors using the ErrorDetector class.
     * 
     * @throws IOException If an I/O error occurs.
     */
    private static void testMixedCapitalization() throws IOException {
        SystemDictionary systemDict = new SystemDictionary("words_alpha.txt");
        UserDictionary userDict = new UserDictionary();
        ErrorDetector errorDetector = new ErrorDetector(systemDict, userDict);

        List<ProcessedWord> words = Arrays.asList(
            new ProcessedWord("mIxEd", null, false) // mixed capitalization
        );

        // Check if any mixed capitalization error is detected
        boolean mixedCapitalizationDetected = errorDetector.detectErrors(words).stream()
            .anyMatch(p -> p.getProblemType() == ProblemType.CAPITALIZATION);

        printTestResult("testMixedCapitalization", mixedCapitalizationDetected);
    }

    /**
     * Prints the result of a test, indicating success or failure.
     * 
     * @param testName The name of the test.
     * @param testPassed A boolean indicating whether the test passed or failed.
     */
    private static void printTestResult(String testName, boolean testPassed) {
        if (testPassed) {
            System.out.println(testName + ": Success");
        } else {
            System.err.println(testName + ": Failed");
        }
    }
}
