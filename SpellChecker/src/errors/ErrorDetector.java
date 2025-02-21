package errors;

import java.util.ArrayList;
import java.util.List;
import processing.TextProcessor.ProcessedWord;
import dictionary.SystemDictionary;
import dictionary.UserDictionary;
import processing.ProblemWord;
import processing.ProblemWord.ProblemType;

/**
 * @Author Mert Osoydan, Joshua Shaji
 * The ErrorDetector class is responsible for identifying problems with words
 * in a given list of processed words. Problems include misspellings, capitalization errors,
 * and double words.
 */
public class ErrorDetector {
    private SystemDictionary systemDictionary;
    private UserDictionary userDictionary;

    /**
     * Constructs an ErrorDetector with references to a system dictionary and a user dictionary.
     * 
     * @param systemDict The system dictionary to check words against.
     * @param userDict   The user dictionary to check words against.
     */
    public ErrorDetector(SystemDictionary systemDict, UserDictionary userDict) {
        this.systemDictionary = systemDict;
        this.userDictionary = userDict;
    }

    /**
     * Detects errors in a list of processed words.
     * 
     * @param processedWords A list of processed words from the text processor.
     * @return A list of ProblemWord objects that contains any words with detected problems.
     */
    public List<ProblemWord> detectErrors(List<ProcessedWord> processedWords) {
    	List<ProblemWord> problemWords = new ArrayList<>();
        boolean isNewSentence = true; // Flag to indicate the start of a new sentence
        String previousWord = null;

        // Iterate over each processed word to detect any spelling mistakes.
        int positionCounter = 0;
        for (ProcessedWord processedWord : processedWords) {
            String word = processedWord.getWord();

            // Check for misspelling
            if (!systemDictionary.isInDictionary(word.toLowerCase()) && !userDictionary.isInDictionary(word.toLowerCase())) {
                problemWords.add(
                		new ProblemWord(word, positionCounter,ProblemType.MISSPELLING));
            }

            // Check for capitalization issues at the start of sentences
            if (shouldStartWithCapital(word, isNewSentence) && !startsWithCapital(word)) {
                problemWords.add(
                		new ProblemWord(word, positionCounter, ProblemType.CAPITALIZATION));
            }
            
            // Check for double words
            if (previousWord != null && word.equalsIgnoreCase(previousWord)) {
                problemWords.add(
                		new ProblemWord(word, positionCounter, ProblemType.DOUBLE_WORD));
            }

            // Update isNewSentence for next word
            isNewSentence = processedWord.getPuncRequiresCapAfter();
            previousWord = word;
            positionCounter++;
        }

        return problemWords;
    }
    
    private boolean shouldStartWithCapital(String word, boolean isNewSentence) {
        // The first word after a sentence-ending word should be capitalized.
        return isNewSentence;
    }

    
    private boolean startsWithCapital(String word) {
        return !word.isEmpty() && Character.isUpperCase(word.charAt(0));
    }
}