package processing;

import java.util.ArrayList;
import java.util.List;

public class TextProcessor {

    public List<ProcessedWord> processText(String text) {
        return tokenizeText(text, false);
    }

    public List<ProcessedWord> processTextIgnoringTags(String text) {
        return tokenizeText(text, true);
    }

    private List<ProcessedWord> tokenizeText(String text, boolean ignoreTags) {
        List<ProcessedWord> processedWords = new ArrayList<>();
    	if(text.isEmpty()) {
    		return processedWords;
    	}
        String[] wordsWithPunctuation = text.split("\\s+"); // Split text by spaces

        for (String wordWithPunctuation : wordsWithPunctuation) {
            // Skip HTML/XML tags if ignoreTags is true
            if (ignoreTags && wordWithPunctuation.matches("<[^>]*>.*</[^>]*>")) {
                continue;
            }

            PuncAfterWord punc = puncAfterWord(wordWithPunctuation);
            Boolean bool = doesPuncRequireCapAfterIt(punc);

            // Remove leading and trailing punctuation
            String word = wordWithPunctuation.replaceAll("^[^\\w]+|[^\\w]+$", "");

            // Skip strings that consist only of numbers
            if (word.matches("^\\d+$")) {
                continue;
            }

            processedWords.add(new ProcessedWord(word, punc, bool));
        }

        return processedWords;
    }

    public enum PuncAfterWord {
        PERIOD, // Indicates a period after the word
        EXCLAM_MARK, // Indicates an exclamation mark after the word
        QUES_MARK, // Indicates a question mark after the word
        COMMA, // Indicates a comma after the word
        COLON, // Indicates a colon after the word
        SEMICOLON, // Indicates a semicolon after the word
    }

    private static boolean doesPuncRequireCapAfterIt(PuncAfterWord punc) {
        if (punc == null) {
            return false;
        } else if (punc.equals(PuncAfterWord.PERIOD) || punc.equals(PuncAfterWord.EXCLAM_MARK) || punc.equals(PuncAfterWord.QUES_MARK)) {
            return true;
        } else {
            return false;
        }
    }

    private PuncAfterWord puncAfterWord(String word) {
        if (word.endsWith(".")) {
            return PuncAfterWord.PERIOD;
        } else if (word.endsWith("!")) {
            return PuncAfterWord.EXCLAM_MARK;
        } else if (word.endsWith("?")) {
            return PuncAfterWord.QUES_MARK;
        } else if (word.endsWith(",")) {
            return PuncAfterWord.COMMA;
        } else if (word.endsWith(":")) {
            return PuncAfterWord.COLON;
        } else if (word.endsWith(";")) {
            return PuncAfterWord.SEMICOLON;
        } else {
            return null;
        }
    }

    public static class ProcessedWord {
        private String word;
        private PuncAfterWord puncAfterWord;
        private boolean puncRequiresCapAfter;

        public ProcessedWord(String word, PuncAfterWord punc, boolean capReq) {
            this.word = word;
            this.puncAfterWord = punc;
            this.puncRequiresCapAfter = capReq;
        }

        // Standard getters
        public String getWord() { return word; }
        public PuncAfterWord getPuncAfterWord() { return puncAfterWord; }
        public boolean getPuncRequiresCapAfter() { return puncRequiresCapAfter; }
    }
}
