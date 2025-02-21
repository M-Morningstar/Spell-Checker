package processingTest;

import processing.TextProcessor;
import processing.TextProcessor.ProcessedWord;
import processing.TextProcessor.PuncAfterWord;
import java.util.List;

/**
 * This class tests the TextProcessor class to ensure correct functionality.
 * It checks various scenarios, including basic text processing, ignoring HTML tags,
 * detecting sentence endings, handling empty text, and managing edge cases.
 */
public class TextProcessorTest {

    /**
     * Main method to run all test cases.
     */
    public static void main(String[] args) {
        testBasicTextProcessing();
        testHTMLTagIgnoredProcessing();
        testSentenceEndDetection();
        testEmptyTextProcessing();
        testEdgeCases();
    }

    /**
     * Tests basic text processing functionality.
     */
    private static void testBasicTextProcessing() {
        TextProcessor textProcessor = new TextProcessor();
        List<ProcessedWord> processedWords = textProcessor.processText("Hello world! This is a test.");

        // Expected results
        String[] expectedWords = {"Hello", "world", "This", "is", "a", "test"};
        PuncAfterWord[] expectedPunctuation = {null, PuncAfterWord.EXCLAM_MARK, null, null, null, PuncAfterWord.PERIOD};
        boolean[] expectedCapAfter = {false, true, false, false, false, true};

        // Checking the processed words
        boolean testPassed = verifyResults(processedWords, expectedWords, expectedPunctuation, expectedCapAfter);
        printTestResult("testBasicTextProcessing", testPassed);
    }

    /**
     * Tests the processing of text while ignoring HTML tags.
     */
    private static void testHTMLTagIgnoredProcessing() {
        TextProcessor textProcessor = new TextProcessor();
        List<ProcessedWord> processedWords = textProcessor.processTextIgnoringTags("<p>Hello</p> world!");

        // Expected results (assuming HTML tags are ignored)
        String[] expectedWords = {"world"};
        PuncAfterWord[] expectedPunctuation = {PuncAfterWord.EXCLAM_MARK};
        boolean[] expectedCapAfter = {true};

        // Checking the processed words
        boolean testPassed = verifyResults(processedWords, expectedWords, expectedPunctuation, expectedCapAfter);
        printTestResult("testHTMLTagIgnoredProcessing", testPassed);
    }

    /**
     * Tests the detection of sentence-ending punctuation.
     */
    private static void testSentenceEndDetection() {
        TextProcessor textProcessor = new TextProcessor();
        List<ProcessedWord> processedWords = textProcessor.processText("End. Start? Continue!");

        // Expected results
        String[] expectedWords = {"End", "Start", "Continue"};
        PuncAfterWord[] expectedPunctuation = {PuncAfterWord.PERIOD, PuncAfterWord.QUES_MARK, PuncAfterWord.EXCLAM_MARK};
        boolean[] expectedCapAfter = {true, true, true};

        // Checking the processed words
        boolean testPassed = verifyResults(processedWords, expectedWords, expectedPunctuation, expectedCapAfter);
        printTestResult("testSentenceEndDetection", testPassed);
    }

    /**
     * Tests processing of empty text.
     */
    private static void testEmptyTextProcessing() {
        TextProcessor textProcessor = new TextProcessor();
        List<ProcessedWord> processedWords = textProcessor.processText("");

        boolean testPassed = processedWords.isEmpty();
        printTestResult("testEmptyTextProcessing", testPassed);
    }

    /**
     * Tests edge cases, such as ignoring numbers and handling commas.
     */
    private static void testEdgeCases() {
        TextProcessor textProcessor = new TextProcessor();
        List<ProcessedWord> processedWords = textProcessor.processText("999 hello, world!");

        // Expected results (assuming "999" is ignored)
        String[] expectedWords = {"hello", "world"};
        PuncAfterWord[] expectedPunctuation = {PuncAfterWord.COMMA, PuncAfterWord.EXCLAM_MARK};
        boolean[] expectedCapAfter = {false, true};

        // Checking the processed words
        boolean testPassed = verifyResults(processedWords, expectedWords, expectedPunctuation, expectedCapAfter);
        printTestResult("testEdgeCases", testPassed);
    }

    /**
     * Helper method to print the test results.
     *
     * @param testName The name of the test.
     * @param testPassed A boolean indicating if the test passed.
     */
    private static void printTestResult(String testName, boolean testPassed) {
        if (testPassed) {
            System.out.println(testName + ": Success");
        } else {
            System.err.println(testName + ": Failed");
        }
    }

    /**
     * Verifies the processed words against the expected results.
     *
     * @param processedWords The list of processed words.
     * @param expectedWords Array of expected word strings.
     * @param expectedPunctuation Array of expected punctuation types.
     * @param expectedCapAfter Array of booleans indicating if capitalization is required after the word.
     * @return boolean indicating if the test passed or failed.
     */
    private static boolean verifyResults(List<ProcessedWord> processedWords, String[] expectedWords, PuncAfterWord[] expectedPunctuation, boolean[] expectedCapAfter) {
        if (processedWords.size() != expectedWords.length) {
            return false;
        }
        for (int i = 0; i < processedWords.size(); i++) {
            ProcessedWord pw = processedWords.get(i);
            if (!pw.getWord().equals(expectedWords[i]) || 
                pw.getPuncAfterWord() != expectedPunctuation[i] ||
                pw.getPuncRequiresCapAfter() != expectedCapAfter[i]) {
                return false;
            }
        }
        return true;
    }
}
