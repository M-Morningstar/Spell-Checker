/**
 * @Author Mert Osoydan, Joshua Shaji
 * Represents a system dictionary used in a spell checker application.
 * It loads a predefined list of correctly spelled words from a file and provides
 * functionality to check if a word is in this dictionary.
 */
package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SystemDictionary {
    
    private final Set<String> words; // A set of words that are in the system dictionary

    /**
     * Constructor for SystemDictionary that initializes the dictionary by loading words
     * from a file at the given resource path.
     *
     * @param filePath The path to the resource file where the system dictionary is stored.
     * @throws IOException If there is an issue accessing the resource file.
     */
    public SystemDictionary(String filePath) throws IOException {
        this.words = new HashSet<>();
        loadDictionary(filePath);
    }
    
    /**
     * Loads the system dictionary from a resource file.
     *
     * @param filePath The path to the resource file to load.
     * @throws IOException If there is an issue reading from the resource file.
     */
    private void loadDictionary(String filePath) throws IOException{
        // Access the resource file using the class loader
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("Dictionary file not found in resources.");
        }
        // Read each line from the resource file and add it to the words set
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        }
    }

    /**
     * Checks if a word is in the system dictionary.
     *
     * @param word The word to check.
     * @return True if the word is in the dictionary, false otherwise.
     */
    public boolean isInDictionary(String word) {
        return words.contains(word.toLowerCase());
    }
    
    /**
     * Returns an unmodifiable set of words in the system dictionary.
     * 
     * @return An unmodifiable Set of words.
     */
    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }
}
