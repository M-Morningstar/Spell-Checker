package dictionaryTest;

import java.io.File;
import java.io.IOException;
import dictionary.UserDictionary;

public class UserDictionaryTest {

    private static final String TEST_DICTIONARY_PATH = System.getProperty("user.home") + File.separator + ".mySpellCheckerTest" + File.separator + "test_userdictionary.txt";

    public static void main(String[] args) {
        testAddWord();
        testRemoveWord();
        testIsInDictionary();
        testResetDictionary();
        testSaveAndLoadUserDictionary();
    }

    private static void testAddWord() {
        try {
            UserDictionary userDict = new UserDictionary();
            userDict.addWord("testword");
            if (userDict.isInDictionary("testword")) {
                System.out.println("testAddWord: Success");
            } else {
                System.err.println("testAddWord: Failed, word not added.");
            }
            userDict.resetDictionary(); // Cleanup
        } catch (IOException e) {
            System.err.println("testAddWord: Exception - " + e.getMessage());
        }
    }

    private static void testRemoveWord() {
        try {
            UserDictionary userDict = new UserDictionary();
            userDict.addWord("testword");
            userDict.removeWord("testword");
            if (!userDict.isInDictionary("testword")) {
                System.out.println("testRemoveWord: Success");
            } else {
                System.err.println("testRemoveWord: Failed, word not removed.");
            }
            userDict.resetDictionary(); // Cleanup
        } catch (IOException e) {
            System.err.println("testRemoveWord: Exception - " + e.getMessage());
        }
    }

    private static void testIsInDictionary() {
        try {
            UserDictionary userDict = new UserDictionary();
            userDict.addWord("testword");
            if (userDict.isInDictionary("testword")) {
                System.out.println("testIsInDictionary: Success");
            } else {
                System.err.println("testIsInDictionary: Failed, word not found.");
            }
            userDict.resetDictionary(); // Cleanup
        } catch (IOException e) {
            System.err.println("testIsInDictionary: Exception - " + e.getMessage());
        }
    }

    private static void testResetDictionary() {
        try {
            UserDictionary userDict = new UserDictionary();
            userDict.addWord("testword");
            userDict.resetDictionary();
            if (!userDict.isInDictionary("testword")) {
                System.out.println("testResetDictionary: Success");
            } else {
                System.err.println("testResetDictionary: Failed, dictionary not reset.");
            }
        } catch (IOException e) {
            System.err.println("testResetDictionary: Exception - " + e.getMessage());
        }
    }

    private static void testSaveAndLoadUserDictionary() {
        try {
            UserDictionary userDict = new UserDictionary();
            userDict.addWord("testword");
            userDict.saveUserDictionary();

            UserDictionary newUserDict = new UserDictionary();
            if (newUserDict.isInDictionary("testword")) {
                System.out.println("testSaveAndLoadUserDictionary: Success");
            } else {
                System.err.println("testSaveAndLoadUserDictionary: Failed, word not saved/loaded.");
            }
            newUserDict.resetDictionary(); // Cleanup
        } catch (IOException e) {
            System.err.println("testSaveAndLoadUserDictionary: Exception - " + e.getMessage());
        }
    }
}
