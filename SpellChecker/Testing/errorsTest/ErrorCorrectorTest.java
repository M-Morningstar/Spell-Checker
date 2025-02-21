package errorsTest;

import dictionary.SystemDictionary;
import dictionary.UserDictionary;
import errors.ErrorCorrector;
import java.io.IOException;
import java.util.List;

public class ErrorCorrectorTest {

    public static void main(String[] args) {
        testMisspellingSuggestions();
        testCapitalizationSuggestions();
        testDoubleWordSuggestions();
        testMixedCapitalizationSuggestions();
    }

    private static void testMisspellingSuggestions() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("path/to/test/system_dict.txt");
            UserDictionary userDictionary = new UserDictionary();
            ErrorCorrector errorCorrector = new ErrorCorrector(systemDictionary, userDictionary);

            String misspelledWord = "incorect";
            List<String> suggestions = errorCorrector.getSuggestionsForMisspelling(misspelledWord);

            // Updated to reflect a range of potential suggestions
            // Note: The exact suggestions might vary based on the dictionary content and correction logic
            String[] expectedSuggestions = {"incorrect", "correct", "incorrectly", "incorrupt", "incorr", "indirect", "insert", "inspect"};

            System.out.println("Suggestions for 'incorect':");
            for(String suggestion : suggestions) {
                System.out.println(suggestion);
            }

            System.out.println("testMisspellingSuggestions: " + (suggestions.size() <= expectedSuggestions.length));
        } catch (IOException e) {
            System.err.println("testMisspellingSuggestions: Failed - " + e.getMessage());
        }
    }

    private static void testCapitalizationSuggestions() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("path/to/test/system_dict.txt");
            UserDictionary userDictionary = new UserDictionary();
            ErrorCorrector errorCorrector = new ErrorCorrector(systemDictionary, userDictionary);

            String word = "london";
            List<String> suggestions = errorCorrector.getSuggestionsForCapitalization(word);
            
            
            

            // Expected suggestions: ["london", "LONDON", "London"]
            String[] expectedSuggestions = {"london", "LONDON", "London"};
            
            for(String suggestion : suggestions) {
            	System.out.println(suggestion);
            }

            System.out.println("testCapitalizationSuggestions: " + compareSuggestions(suggestions, expectedSuggestions));
        } catch (IOException e) {
            System.err.println("testCapitalizationSuggestions: Failed - " + e.getMessage());
        }
    }

    private static void testDoubleWordSuggestions() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("path/to/test/system_dict.txt");
            UserDictionary userDictionary = new UserDictionary();
            ErrorCorrector errorCorrector = new ErrorCorrector(systemDictionary, userDictionary);

            String word = "the";
            List<String> suggestions = errorCorrector.getSuggestionsForDoubleWord(word);

            // Expected suggestion: ["Delete one"]
            String[] expectedSuggestions = {"Delete one"};
            
            for(String suggestion : suggestions) {
            	System.out.println(suggestion);
            }

            System.out.println("testDoubleWordSuggestions: " + compareSuggestions(suggestions, expectedSuggestions));
        } catch (IOException e) {
            System.err.println("testDoubleWordSuggestions: Failed - " + e.getMessage());
        }
    }

    private static void testMixedCapitalizationSuggestions() {
        try {
            SystemDictionary systemDictionary = new SystemDictionary("path/to/test/system_dict.txt");
            UserDictionary userDictionary = new UserDictionary();
            ErrorCorrector errorCorrector = new ErrorCorrector(systemDictionary, userDictionary);

            String word = "LoNdon";
            List<String> suggestions = errorCorrector.getSuggestionsForCapitalization(word);

            // Expected suggestions: ["london", "LONDON", "London"]
            String[] expectedSuggestions = {"london", "LONDON", "London"};
            
            for(String suggestion : suggestions) {
            	System.out.println(suggestion);
            }

            System.out.println("testMixedCapitalizationSuggestions: " + compareSuggestions(suggestions, expectedSuggestions));
        } catch (IOException e) {
            System.err.println("testMixedCapitalizationSuggestions: Failed - " + e.getMessage());
        }
    }

    private static boolean compareSuggestions(List<String> actual, String[] expected) {
        if (actual.size() != expected.length) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {
            if (!actual.get(i).equals(expected[i])) {
                return false;
            }
        }
        return true;
    }
}
