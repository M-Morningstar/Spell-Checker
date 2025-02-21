/**
 * @Author Mert Osoydan, Joshua Shaji, Joshua Jackson
 * Class contains method to suggest correction to words errors
 */
package errors;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import processing.ProblemWord;
import dictionary.SystemDictionary;
import dictionary.UserDictionary;

public class ErrorCorrector {
    private SystemDictionary systemDictionary;
    private UserDictionary userDictionary;
    private final int maxDistance = 3; // Maximum Levenshtein distance for suggestions

    /**
     * Constructor to initialize the dictionary
     * @param systemDict
     * @param userDict 
     */
    public ErrorCorrector(SystemDictionary systemDict, UserDictionary userDict) {
        this.systemDictionary = systemDict;
        this.userDictionary = userDict;
    }

    /**
     * return a list of suggestion of corrected words for the given misspelled word
     * @param misspelledWord
     */
    public List<String> getSuggestionsForMisspelling(String misspelledWord) {
        PriorityQueue<Suggestion> suggestions = new PriorityQueue<>();

        // Combine both dictionaries for suggestions
        List<String> combinedDictionary = new ArrayList<>();
        combinedDictionary.addAll(systemDictionary.getWords());
        combinedDictionary.addAll(userDictionary.getWords());

        for (String word : combinedDictionary) {
            if (Math.abs(word.length() - misspelledWord.length()) > maxDistance) continue; // Word length filtering

            int distance = calculateLevenshteinDistance(misspelledWord, word, maxDistance);
            if (distance <= maxDistance) {
                suggestions.add(new Suggestion(word, distance));
                if (suggestions.size() > 5) {
                    suggestions.poll(); // Keep only the top 5 closest matches
                }
            }
        }

        return extractTopSuggestions(suggestions);
    }
    
    /**
     * method returns a list of suggestion to correct a miscapitalized word
     * @param word
     */
    public List<String> getSuggestionsForCapitalization(String word) {
        List<String> suggestions = new ArrayList<>();

        // Add different capitalization forms as suggestions
        suggestions.add(word.toLowerCase());
        suggestions.add(word.toUpperCase());
        suggestions.add(capitalize(word));

        return suggestions;
    }

    /**
     * return the suggestion for double word
     * @param word
     */
    public List<String> getSuggestionsForDoubleWord(String word) {
        // For double words, the suggestion is typically to delete one
        return List.of("Delete one");
    }

    /**
     * @param word1
     * @param word2
     * @param maxDistance
     * 
     */
    private int calculateLevenshteinDistance(String word1, String word2, int maxDistance) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[] prevRow = new int[len2 + 1];
        int[] currentRow = new int[len2 + 1];
        int[] temp;

        for (int i = 0; i <= len2; i++) {
            prevRow[i] = i;
        }

        for (int i = 1; i <= len1; i++) {
            currentRow[0] = i;
            int minCost = currentRow[0];

            for (int j = 1; j <= len2; j++) {
                int cost = (word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1;
                currentRow[j] = Math.min(Math.min(currentRow[j - 1] + 1, prevRow[j] + 1), prevRow[j - 1] + cost);
                
                minCost = Math.min(minCost, currentRow[j]);
            }

            // Early termination check
            if (minCost > maxDistance) {
                return maxDistance + 1;
            }

            // Swap the rows for the next iteration
            temp = prevRow;
            prevRow = currentRow;
            currentRow = temp;
        }

        return prevRow[len2];
    }

    /**
     * extract the top most corrected words from the suggestion lists
     * @param suggestions
     */
    private List<String> extractTopSuggestions(PriorityQueue<Suggestion> suggestions) {
        List<String> topSuggestions = new ArrayList<>();
        while (!suggestions.isEmpty()) {
            topSuggestions.add(0, suggestions.poll().word); // Reverse order to get closest matches first
        }
        return topSuggestions;
    }

    /**
     * nested class represent a word suggestion
     */
    private static class Suggestion implements Comparable<Suggestion> {
        private String word;
        private int distance;

        /**
         * @param word
         * @param distance
         * Constructor initialize the suggested corrected word and its distance to the error word
         */
        public Suggestion(String word, int distance) {
            this.word = word;
            this.distance = distance;
        }

        /**
         * @param other
         * compare the distance of this suggested word with other suggested word
         */
        @Override
        public int compareTo(Suggestion other) {
            return Integer.compare(this.distance, other.distance);
        }

        // Getter methods
        public String getWord() {
            return word;
        }

        public int getDistance() {
            return distance;
        }
    }
    
    // Helper method to capitalize the first letter of a word
    private String capitalize(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

}