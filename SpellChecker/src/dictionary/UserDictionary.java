/**
 * @Author Mert Osoydan, Joshua Shaji
 * Represents an user dictionary used in a spell checker application.
 * It loads a list of user's words from the user's input and helps the spell checker
 * ignore those words later
 */
package dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class UserDictionary {
    private Set<String> customWords; //Set to store the words
    private String filePath; //file path to save the user dictionary on

    /**
     * Constructor initialize local path for the dictionary
     * @throws IOException
     */
    public UserDictionary() throws IOException {
        this.filePath = System.getProperty("user.home") + File.separator + ".mySpellChecker" + File.separator + "userdictionary.txt";
        this.customWords = new HashSet<>();
        loadUserDictionary();
    }

    /**
     * 
     * @return all the words in the user dictionary
     */
    public Set<String> getWords() {
        return Collections.unmodifiableSet(customWords);
    }

    /**
     * add a word to user dictionary
     * @param word
     * @throws IOException
     */
    public void addWord(String word) throws IOException {
        customWords.add(word.toLowerCase());
        saveUserDictionary();
    }

    /**
     * remove word from the user dictionary
     * @param word
     */
    public void removeWord(String word) {
        customWords.remove(word.toLowerCase());
    }

    /**
     * check if the word is in the user dictionary 
     * @param word
     * @return boolean
     */
    public boolean isInDictionary(String word) {
    	if (customWords.isEmpty()) {
    		return false;
    	}
    	
        return customWords.contains(word.toLowerCase());
    }

    /**
     * clear the dictionary
     */
    public void resetDictionary() {
        customWords.clear();
    }

    /**
     * Save the dictionary in the local path
     * @throws IOException
     */
    public void saveUserDictionary() throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Ensure directory exists
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String word : customWords) {
                writer.write(word);
                writer.newLine();
            }
        }
    }

    /**
     * Load up the user dictionary
     */
    private void loadUserDictionary() {
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    customWords.add(line.trim().toLowerCase());
                }
            } catch (IOException e) {
                System.err.println("Error reading the user dictionary file: " + filePath);
            }
        }
    }
}
