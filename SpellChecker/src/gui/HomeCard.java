package gui;

import io.FileHandler;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Helper class containing methods to create home window (HomeCard)
 * HomeCard is stored in the JPanel (cards) that is stored in the JFrame (frame)
 */
public class HomeCard {

    // Variables
    private static JPanel homeCard;
    private static JButton fileUploadButton;
    private static JTextArea inputTextArea;
    private static JLabel uploadedFileLabel;
    private static JButton enterButton;
    public static String selectedFilePath;
    private static JButton garbageBin;

    // Main helper method to create a homeCard (returns the JPanel storing the home card)
    public static JPanel createHomeCard() {
        homeCard = new JPanel(new BorderLayout());
        addNorthBorder();
        addCenterBorder();
        addSouthBorder();
        controlFileInputButtonBasedOnInputTextArea();
        fileUploadButtonFunctionality();
        enterButtonFunctionality();
        addPromptText();
        garbageBinFunctionality();



        return homeCard;
    }

    // Helper method to configure north border of the home card
    // Contains the title
    private static void addNorthBorder() {
        JPanel northBorder = new JPanel();

        // Title
        JLabel titleLabel = new JLabel("Spell Checker");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        northBorder.add(titleLabel);

        homeCard.add(northBorder, BorderLayout.NORTH); // add north border to home card
    }

    // Helper method to configure center border of the home card
    // Contains the file upload button
    // Contains the area to manually enter (or paste) text
    private static void addCenterBorder() {
        JPanel centerBorder = new JPanel();
        centerBorder.setLayout(new BoxLayout(centerBorder, BoxLayout.Y_AXIS)); // allows buttons to be stacked vertically

        // File upload button
        fileUploadButton = new JButton("Upload File Here");
        fileUploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerBorder.add(Box.createVerticalGlue()); // centers 3 items vertically
        centerBorder.add(fileUploadButton);
        centerBorder.add(Box.createRigidArea(new Dimension(0, 10))); // adds buffer space between fileUploadButton and orText


        // or text
        JLabel orText = new JLabel("OR Enter / Paste Text Here:");
        orText.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerBorder.add(orText);
        centerBorder.add(Box.createRigidArea(new Dimension(0, 10))); // adds buffer space between orText and inputTextArea

        // Area to manually enter (or paste) text
        inputTextArea = new JTextArea();
        inputTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerBorder.add(formatTextArea(inputTextArea));
        centerBorder.add(Box.createVerticalGlue()); // centers 3 items vertically

        homeCard.add(centerBorder, BorderLayout.CENTER); // add center border to home card
    }

    // Helper method to configure south border of the home card
    // Contains the enter button
    // Contains a label to identify the selected file
    private static void addSouthBorder() {
        JPanel southBorder = new JPanel();
        southBorder.setLayout(new BoxLayout(southBorder, BoxLayout.Y_AXIS)); // box layout used to add items vertically

        // Enter button
        enterButton = new JButton("ENTER");
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        southBorder.add(enterButton);

        // Inputted file label
        uploadedFileLabel = new JLabel(); // left blank, will only appear if file is added - see fileUploadButtonFunctionality() method
        uploadedFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadedFileLabel.setForeground(Color.blue);
        southBorder.add(uploadedFileLabel);

        // Garbage bin (remove inputted file)
        garbageBin = new JButton("X");
        garbageBin.setVisible(false); // will make visible only after file has been inputted
        garbageBin.setAlignmentX(Component.CENTER_ALIGNMENT);
        southBorder.add(garbageBin);

        southBorder.add(Box.createVerticalStrut(20)); // add spacer between bottom of screen and enter button

        homeCard.add(southBorder, BorderLayout.SOUTH); // add south border to home card
    }

    // Helper method to configure/format the area for the user to manually input (or paste) text
    private static JScrollPane formatTextArea(JTextArea tArea) {
        tArea.setLineWrap(true); // ensures text wraps when reaching horizontal border
        tArea.setWrapStyleWord(true); // ensures text wrapping wraps full words (instead of breaking them up)
        tArea.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(tArea); // automatically adds vertical slider to text area
        scrollPane.setPreferredSize(new Dimension(300, 200));
        scrollPane.setMaximumSize(new Dimension(300, 150 ));

        return scrollPane;
    }

    // Helper method to disable file input button when text is added to inputTextArea
    private static void controlFileInputButtonBasedOnInputTextArea() {
        String promptText = "Input or paste text here:   ";
        inputTextArea.getDocument().addDocumentListener(new DocumentListener() {
            // if there's text in the box and it's not the prompt text in light gray, disable the file upload button
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!inputTextArea.getText().equals(promptText) && !inputTextArea.getForeground().equals(Color.lightGray)) {
                    fileUploadButton.setEnabled(false);
                }
            }

            // if you remove all the text in the inputText area, enable the file upload button
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (inputTextArea.getText().isEmpty()) {
                    fileUploadButton.setEnabled(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Style change (e.g., font size) - not relevant for this case
            }
        });
    }

    // Helper method to add clickable functionality to file upload button
    private static void fileUploadButtonFunctionality() {
        fileUploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                uploadedFileLabel.setText("Selected File: " + selectedFilePath);
                inputTextArea.setText("***Remove inputted file to add text***");
                inputTextArea.setEnabled(false);
                garbageBin.setVisible(true);
                fileUploadButton.setEnabled(false);
            }
        });
    }

    // Helper method to add functionality to enter button
    private static void enterButtonFunctionality() {
        enterButton.addActionListener(e -> {
            // if enter button is hit and a file was inputted
            if (!uploadedFileLabel.getText().isEmpty()) {
                // set the string to spell check (in EditorCard class) to text from inputted file
                FileHandler fileHandler = new FileHandler();
                try {
                    EditorCard.setStrToSpellCheck(fileHandler.readFile(selectedFilePath));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // create and show editorCard()
                try {
                    Launcher.cards.add(EditorCard.createEditorCard(), "EDITOR_CARD");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                CardLayout cl = (CardLayout) (Launcher.cards.getLayout());
                cl.show(Launcher.cards, "EDITOR_CARD");
            }

            // if enter button is hit and text was entered in the text area (and it isn't the light gray prompt text)
            else if (!inputTextArea.getText().isEmpty() && !inputTextArea.getForeground().equals(Color.lightGray)) {
                // set the string to spell check (in EditorCard class) to inputted text
                EditorCard.setStrToSpellCheck(inputTextArea.getText());

                // create and show editorCard()
                try {
                    Launcher.cards.add(EditorCard.createEditorCard(), "EDITOR_CARD");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                CardLayout cl = (CardLayout) (Launcher.cards.getLayout());
                cl.show(Launcher.cards, "EDITOR_CARD");
            }

            // if enter button is hit and no file was inputted AND no text was entered (or there is just light gray prompt text)
            else {
                // prompt user to select a file or input text to text area before hitting enter button
                String msg = "Select a file or enter/paste text into the text area above before hitting ENTER.";
                String ttl = "Nothing to spell check :(";
                JOptionPane.showMessageDialog(null, msg, ttl, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // Helper method to add gray-ed out prompt text to the text area
    // Text should disappear upon clicking onto the text area
    private static void addPromptText() {
        // base prompt text (gray-ed out)
        String promptText = "Input or paste text here:   ";
        inputTextArea.setText(promptText);
        inputTextArea.setForeground(Color.lightGray);

        // if the text area is clicked on when the prompt is in it, remove prompt and change text colour to black
        inputTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputTextArea.getText().equals(promptText) && inputTextArea.getForeground().equals(Color.lightGray)) {
                    inputTextArea.setText("");
                    inputTextArea.setForeground(Color.black);
                }
            }
        });

        // if the text area is clicked off of, remove focus from the text area
        // if this happens when there is no text inputted, add the prompt back
        homeCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (inputTextArea.isFocusOwner()) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                }
                if (inputTextArea.getText().isEmpty()) {
                    inputTextArea.setText(promptText);
                    inputTextArea.setForeground(Color.lightGray);
                }
            }
        });
    }

    private static void garbageBinFunctionality() {
        garbageBin.addActionListener(e -> {
            uploadedFileLabel.setText("");
            garbageBin.setVisible(false);
            fileUploadButton.setEnabled(true);
            inputTextArea.setEnabled(true);
            addPromptText();
        });
    }

    public static void resetVariables() {
        homeCard=null;
        fileUploadButton=null;
        inputTextArea=null;
        uploadedFileLabel=null;
        enterButton=null;
        selectedFilePath=null;
        garbageBin=null;
    }

}