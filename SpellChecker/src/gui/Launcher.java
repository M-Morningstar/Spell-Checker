package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Launches the program's GUI (contains main method)
 */
public class Launcher {

    // Variables
    public static JFrame frame;
    public static JPanel cards;

    // Main Method
    public static void main(String[] args) throws IOException {
        EditorCard.ignoreAllList = new ArrayList<>();
        createAndShowGUI();
    }

    // Creates and shows GUI (separate from main method for privacy reasons)
    public static void createAndShowGUI() throws IOException {
        setUpJFrame();
        setUpCards();
        frame.add(cards);

        // supposed to set focus to fileUploadButton (not working atm)
        //SwingUtilities.invokeLater(() -> {HomeCard.fileUploadButton.requestFocusInWindow();});

        frame.setVisible(true);
    }

    // Helper method to set up the JFrame
    private static void setUpJFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // terminates program upon closing GUI window
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null); // centers GUI on user's screen
        frame.setJMenuBar(MenuBar.createMenuBar());
    }

    // Helper method to set up the JPanel (has Card Layout) stored in the JFrame
    private static void setUpCards() throws IOException {
        cards = new JPanel(new CardLayout());
        cards.add(HomeCard.createHomeCard(), "HOME_CARD");
        // Note, editor card created after user hits enter button in home card screen
    }

    public static void resetVariables() {
        frame = null;
        cards = null;
    }
}
