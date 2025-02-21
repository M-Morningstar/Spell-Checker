package gui;

import io.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EndCard {

    // Variables
    private static JPanel endCard;
    private static JButton saveButton;
    private static JButton saveAsButton;
    private static JButton discardButton;
    private static JButton newSpellCheckButton;
    private static JButton closeApplicationButton;

    // Main helper method to create an endCard (returns the JPanel storing the end card)
    public static JPanel createEndCard() throws IOException {
        endCard = new JPanel(new BorderLayout());
        addNorthBorder();
        addCenterBorder();
        addEastBorder();
        addSouthBorder();
        saveButtonFunc();
        saveAsButtonFunc();
        discardButtonFunc();
        newSpellCheckButtonFunc();
        closeApplicationButtonFunc();

        return endCard;
    }

    // Helper method to create north border of end card
    // Contains title
    private static void addNorthBorder() {
        JPanel northBorder = new JPanel();
        JLabel titleLabel = new JLabel("Spell Checking Complete");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        northBorder.add(titleLabel);

        endCard.add(northBorder, BorderLayout.NORTH);
    }

    // Helper method to create center border of end card
    // Contains finalized spell-checked document
    private static void addCenterBorder() {
        JPanel centerBorder = new JPanel();
        JEditorPane textArea = new JEditorPane();
        textArea.setContentType("text/html");
        textArea.setText(EditorCard.getStrToSpellCheck());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        centerBorder.add(scrollPane);

        endCard.add(centerBorder, BorderLayout.CENTER);
    }

    // Helper method to create east border of end card
    // Contains buttons to save, save as, delete, spell check a new document, and close application
    private static void addEastBorder() {
        JPanel eastBorder = new JPanel(new BorderLayout());

        // add title to north part of east border of the end card
        JPanel nBorder = new JPanel();
        JLabel optionsTitle = new JLabel("Options:");
        nBorder.add(optionsTitle);
        eastBorder.add(nBorder, BorderLayout.NORTH);

        // add save, save as, and discard buttons to the center part of the east border of the end card
        JPanel cBorder = new JPanel();
        cBorder.setLayout(new BoxLayout(cBorder, BoxLayout.Y_AXIS)); // allows buttons to be stacked vertically
        saveButton = new JButton("Save");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveAsButton = new JButton("Save As...");
        saveAsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        discardButton = new JButton("Discard Changes");
        discardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newSpellCheckButton = new JButton("Perform Another Spell Check");
        newSpellCheckButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newSpellCheckButton.setEnabled(false); // set to false as user must first save, save as, or discard changes
        closeApplicationButton = new JButton("Close Spell Checker");
        closeApplicationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeApplicationButton.setEnabled(false); // set to false as user must first save, save as, or discard changes

        cBorder.add(Box.createVerticalGlue());
        cBorder.add(saveButton);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(saveAsButton);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(discardButton);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(newSpellCheckButton);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(closeApplicationButton);
        cBorder.add(Box.createVerticalGlue());
        eastBorder.add(cBorder, BorderLayout.CENTER);

        // add east border to card layout
        endCard.add(eastBorder, BorderLayout.EAST);
    }

    // Helper method to create south border of end card
    // Contains the metrics summary
    private static void addSouthBorder() {
        JPanel southBorder = new JPanel();

        endCard.add(southBorder, BorderLayout.SOUTH);
    }

    // Helper method to add functionality to the save button
    // Once saved, you can not save it again or discard it (but you can still save as another file)
    // Also enable the new spell check and close application buttons after it has been hit
    private static void saveButtonFunc() {
        saveButton.addActionListener(e -> {
            try {
                FileHandler.writeFile(EditorCard.getStrToSpellCheck(), HomeCard.selectedFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            saveButton.setEnabled(false); // if you save it, you can't save it again
            discardButton.setEnabled(false); // if you save it, you can no longer discard it
            newSpellCheckButton.setEnabled(true);
            closeApplicationButton.setEnabled(true);
        });
    }

    // Helper method to add functionality to the save as button
    // Once saved as a file, you can still save it to the original path, save as again, but cannot discard changes
    // Also enable the new spell check and close application buttons after it has been hit (and successfully saved)
    private static void saveAsButtonFunc() {
        saveAsButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();

                try {
                    FileHandler.writeFile(EditorCard.getStrToSpellCheck(), filePath);
                    HomeCard.selectedFilePath = filePath; // Update the selectedFilePath
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                discardButton.setEnabled(false);
                newSpellCheckButton.setEnabled(true);
                closeApplicationButton.setEnabled(true);
            }
        });
    }

    // Helper method to add functionality to the discard button
    // As the spell-checked text was never saved to the system, hitting this just disables the 3 buttons
    // Also enable the new spell check and close application buttons after it has been hit
    private static void discardButtonFunc() {
        discardButton.addActionListener(e -> {
            saveButton.setEnabled(false);
            saveAsButton.setEnabled(false);
            discardButton.setEnabled(false);
            newSpellCheckButton.setEnabled(true);
            closeApplicationButton.setEnabled(true);
        });
    }

    // Helper method to add functionality to the Spell Check Another File button
    private static void newSpellCheckButtonFunc() {
        newSpellCheckButton.addActionListener(e -> {
            // restart the application
            Launcher.frame.dispose();
            Launcher.resetVariables();
            HomeCard.resetVariables();
            EditorCard.resetVariables();
            try {
                Launcher.createAndShowGUI();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // Helper method to add functionality to the Close Application button
    private static void closeApplicationButtonFunc() {
        closeApplicationButton.addActionListener(e -> {
            System.exit(0); // terminate Java application
        });
    }

}
