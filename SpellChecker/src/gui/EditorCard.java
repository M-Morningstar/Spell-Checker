package gui;

import dictionary.SystemDictionary;
import dictionary.UserDictionary;
import errors.ErrorCorrector;
import errors.ErrorDetector;
import processing.ProblemWord;
import processing.TextProcessor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper class containing methods to create editor window (EditorCard)
 * EditorCard is stored in the JPanel (cards) that is stored in the JFrame (frame)
 */
public class EditorCard {

    // Variables
    private static JPanel editorCard;
    private static String strToSpellCheck;
    private static List<TextProcessor.ProcessedWord> processedWords;
    private static List<ProblemWord> problemWords;
    private static JEditorPane textArea;
    private static JTextField manualFixTextField;
    private static JButton manualFixButton;
    private static JButton sug1;
    private static JButton sug2;
    private static JButton sug3;
    private static JButton sug4;
    private static JButton sug5;
    private static JButton delWord;
    private static JButton ignoreOnce;
    private static JButton ignoreAll;
    private static JButton addToDictionary;
    private static int problemWordCounter = 0;
    private static JTextArea metricsCounts;
    private static JTextArea metricsErrors;
    private static JTextArea metricsCorrections;
    private static int numMisspellInitial;
    private static int numMiscapInitial;
    private static int numDoubInitial;
    private static int numManCor;
    private static int numSugCor;
    private static int numDelCor;
    private static int numIgnCor;
    public static boolean ignTags;
    private static UserDictionary userDic;
    public static List<ProblemWord> ignoreAllList;

    // Main helper method to create an editorCard (returns the JPanel storing the editor card)
    public static JPanel createEditorCard() throws IOException {
        editorCard = new JPanel(new BorderLayout());
        processText();

        // if there aren't any problem words, prompt the user and return them to a new home card
        if (problemWords.isEmpty()) {
            String msg = "No errors detected. Please choose something else to spell check";
            String ttl = "No errors detected";
            JOptionPane.showMessageDialog(null, msg, ttl, JOptionPane.INFORMATION_MESSAGE);
            return HomeCard.createHomeCard();

            // if there are problem words, continue on creating the editor card
        } else {
            highlightProblemWords();
            createNewString();
            addNorthBorder();
            addCenterBorder();
            addEastBorder();
            addSouthBorder();
            countTotalErrors();
            getSuggestions();
            manualFixButtonFunctionality();
            suggestionButtonFunctionality();
            delWordFunctionality();
            ignoreOnceFunctionality();
            ignoreAllFunctionality();
            updateCountMetrics();
            updateErrorsMetrics();
            updateCorrectionsMetrics();
            addToPersonalDict();
            remFullIgnoreAllList();
            return editorCard;
        }
    }

    // Helper method to process the inputted text and identify problem words
    public static void processText() throws IOException {
        // Process text
        TextProcessor textProcessor = new TextProcessor();
        if (ignTags) {
            processedWords = textProcessor.processTextIgnoringTags(strToSpellCheck);
        } else {
            processedWords = textProcessor.processText(strToSpellCheck);
        }

        // Find problem words
        SystemDictionary sysDic = new SystemDictionary("words_alpha.txt");
        UserDictionary userDic = new UserDictionary();
        ErrorDetector errorDetector = new ErrorDetector(sysDic, userDic);
        problemWords = errorDetector.detectErrors(processedWords);
    }

    // Helper method to highlight problem words (using HTML formatting)
    private static void highlightProblemWords() {
        String color;
        for (int i = 0; i < problemWords.size(); i++) {
            // string to store inputted text with highlighted colours (HTML formatted)
            StringBuilder htmlBuilder = new StringBuilder();

            // if problem word is identified as a misspelled word, colour it red
            if(problemWords.get(i).getProblemType().equals(ProblemWord.ProblemType.MISSPELLING)) {
                color = "BD4545"; // hex code for red colour
                String word = processedWords.get(problemWords.get(i).getPosition()).getWord(); // misspelled word
                TextProcessor.PuncAfterWord punc = processedWords.get(problemWords.get(i).getPosition()).getPuncAfterWord(); // punc after word
                boolean boo = processedWords.get(problemWords.get(i).getPosition()).getPuncRequiresCapAfter(); // if misspelled word requires a capital after

                // colour word red using HTML tags
                htmlBuilder.append("<span style=\"color: " + color + "\">");
                htmlBuilder.append(word);
                htmlBuilder.append("</span>");

                // create a new ProcessedWord using the HTML-formatted word and assign it to the processedWords list
                TextProcessor.ProcessedWord colouredWord = new TextProcessor.ProcessedWord(htmlBuilder.toString(), punc, boo);
                processedWords.set(problemWords.get(i).getPosition(), colouredWord);

                // if problem word is identified as a mis-capitalization, colour it pink
            } else if (problemWords.get(i).getProblemType().equals(ProblemWord.ProblemType.CAPITALIZATION)) {
                color = "D937E7"; // hex code for orange
                String word = processedWords.get(problemWords.get(i).getPosition()).getWord(); // incorrectly capitalized word
                TextProcessor.PuncAfterWord punc = processedWords.get(problemWords.get(i).getPosition()).getPuncAfterWord(); // punc after word
                boolean boo = processedWords.get(problemWords.get(i).getPosition()).getPuncRequiresCapAfter(); // if misspelled word requires a capital after

                // colour word orange using HTML tags
                htmlBuilder.append("<span style=\"color: " + color + "\">");
                htmlBuilder.append(word);
                htmlBuilder.append("</span>");

                // create a new ProcessedWord using the HTML-formatted word and assign it to the processedWords list
                TextProcessor.ProcessedWord colouredWord = new TextProcessor.ProcessedWord(htmlBuilder.toString(), punc, boo);
                processedWords.set(problemWords.get(i).getPosition(), colouredWord);

                // if problem word is identified as a double word, colour it blue
            } else {
                color = "D937E7"; // hex code for blue
                String word = processedWords.get(problemWords.get(i).getPosition()).getWord(); // doubled word
                TextProcessor.PuncAfterWord punc = processedWords.get(problemWords.get(i).getPosition()).getPuncAfterWord(); // punc after word
                boolean boo = processedWords.get(problemWords.get(i).getPosition()).getPuncRequiresCapAfter(); // if misspelled word requires a capital after

                // colour word orange using HTML tags
                htmlBuilder.append("<span style=\"color: " + color + "\">");
                htmlBuilder.append(word);
                htmlBuilder.append("</span>");

                // create a new ProcessedWord using the HTML-formatted word and assign it to the processedWords list
                TextProcessor.ProcessedWord colouredWord = new TextProcessor.ProcessedWord(htmlBuilder.toString(), punc, boo);
                processedWords.set(problemWords.get(i).getPosition(), colouredWord);
            }
        }
    }

    // Helper method to update string to HTML format
    private static void createNewString() {
        // string to hold formatted string
        StringBuilder editedString = new StringBuilder();

        // add opening HTML tag
        editedString.append("<html><body>");

        // loop through all processedWords, adding them and their associated punctuation if needed
        for (int i = 0; i < processedWords.size(); i++) {
            // add the word
            editedString.append(processedWords.get(i).getWord());

            // if there is no punctuation after the word, just add a space
            if (processedWords.get(i).getPuncAfterWord() == null) {
                editedString.append(" ");
            } else if (processedWords.get(i).getPuncAfterWord().equals(TextProcessor.PuncAfterWord.PERIOD)) {
                editedString.append(". ");
            } else if (processedWords.get(i).getPuncAfterWord().equals(TextProcessor.PuncAfterWord.EXCLAM_MARK)) {
                editedString.append("! ");
            } else if (processedWords.get(i).getPuncAfterWord().equals(TextProcessor.PuncAfterWord.QUES_MARK)) {
                editedString.append("? ");
            } else if (processedWords.get(i).getPuncAfterWord().equals(TextProcessor.PuncAfterWord.COMMA)) {
                editedString.append(", ");
            } else if (processedWords.get(i).getPuncAfterWord().equals(TextProcessor.PuncAfterWord.COLON)) {
                editedString.append(": ");
            } else if (processedWords.get(i).getPuncAfterWord().equals(TextProcessor.PuncAfterWord.SEMICOLON)) {
                editedString.append("; ");
            }
        }
        editedString.append("</body></html>");
        strToSpellCheck = editedString.toString();
    }

    // Helper method to configure north border of the home card
    // Contains the title
    private static void addNorthBorder() {
        JPanel northBorder = new JPanel();

        // Title
        JLabel titleLabel = new JLabel("Editing in progress...");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        northBorder.add(titleLabel);

        editorCard.add(northBorder, BorderLayout.NORTH);
    }

    // Helper method to configure center border of the home card
    // Contains the text being edited
    private static void addCenterBorder() {
        JPanel centerBorder = new JPanel();
        centerBorder.add(formatTextBlock());
        editorCard.add(centerBorder, BorderLayout.CENTER);
    }

    // Helper method to configure east border of the home card
    // Contains the problem editor (provides suggestions, a manual input, and an ignore)
    private static void addEastBorder() {
        JPanel eastBorder = new JPanel(new BorderLayout());

        // add manual fix box to the north part of the east border of the editor card
        JPanel nBorder = new JPanel();
        JLabel manualFixLabel = new JLabel("Manual Fix:");
        manualFixTextField = new JTextField(problemWords.get(problemWordCounter).getWord(),20);
        manualFixButton = new JButton("ENTER");
        nBorder.add(manualFixLabel);
        nBorder.add(manualFixTextField);
        nBorder.add(manualFixButton);
        eastBorder.add(nBorder, BorderLayout.NORTH);

        // add suggestion boxes to the center part of the east border of the editor card
        JPanel cBorder = new JPanel();
        cBorder.setLayout(new BoxLayout(cBorder, BoxLayout.Y_AXIS)); // allows buttons to be stacked vertically
        sug1 = new JButton("N/A");
        sug1.setAlignmentX(Component.CENTER_ALIGNMENT);
        sug2 = new JButton("N/A");
        sug2.setAlignmentX(Component.CENTER_ALIGNMENT);
        sug3 = new JButton("N/A");
        sug3.setAlignmentX(Component.CENTER_ALIGNMENT);
        sug4 = new JButton("N/A");
        sug4.setAlignmentX(Component.CENTER_ALIGNMENT);
        sug5 = new JButton("N/A");
        sug5.setAlignmentX(Component.CENTER_ALIGNMENT);

        cBorder.add(Box.createVerticalGlue());
        cBorder.add(sug1);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(sug2);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(sug3);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(sug4);
        cBorder.add(Box.createVerticalGlue());
        cBorder.add(sug5);
        cBorder.add(Box.createVerticalGlue());
        eastBorder.add(cBorder, BorderLayout.CENTER);

        // add ignore boxes to the south part of the east border of the editor card
        JPanel sBorder = new JPanel();
        delWord = new JButton("Delete Word");
        ignoreOnce = new JButton("Ignore Once");
        ignoreAll = new JButton("Ignore All");
        addToDictionary = new JButton("Add to dictionary");
        
        sBorder.add(delWord);
        sBorder.add(ignoreOnce);
        sBorder.add(ignoreAll);
        sBorder.add(addToDictionary);
        eastBorder.add(sBorder, BorderLayout.SOUTH);

        editorCard.add(eastBorder, BorderLayout.EAST);
    }

    // Helper method to configure south border of the home card
    // Contains the metrics
    private static void addSouthBorder() {
        JPanel southBorder = new JPanel(new BorderLayout());
        JLabel metricsPlaceholder = new JLabel("METRICS");
        southBorder.add(metricsPlaceholder, BorderLayout.NORTH);

        JPanel metricsPanel = new JPanel(new GridLayout(1, 3));

        metricsCounts = new JTextArea();
        metricsErrors = new JTextArea();
        metricsCorrections = new JTextArea();

        metricsPanel.add(new JScrollPane(metricsCounts));
        metricsPanel.add(new JScrollPane(metricsErrors));
        metricsPanel.add(new JScrollPane(metricsCorrections));

        southBorder.add(metricsPanel, BorderLayout.CENTER);

        editorCard.add(southBorder, BorderLayout.SOUTH);
    }



    // Helper method to count the initial total number of errors (categorized by type)
    private static void countTotalErrors() {
        for (int i = 0; i < problemWords.size(); i++) {
            if (problemWords.get(i).getProblemType().equals(ProblemWord.ProblemType.MISSPELLING)) {
                numMisspellInitial++;
            } else if (problemWords.get(i).getProblemType().equals(ProblemWord.ProblemType.CAPITALIZATION)) {
                numMiscapInitial++;
            } else {
                numDoubInitial++;
            }
        }
    }

    // Helper method to configure an HTML-formatted text block to show user the problem words in context
    private static JScrollPane formatTextBlock() {
        textArea = new JEditorPane();
        textArea.setContentType("text/html");
        textArea.setText(strToSpellCheck);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        return scrollPane;
    }

    // Helper method to find suggestions for the current problem word and add up to five of them to the sug# buttons
    private static void getSuggestions() throws IOException {
        ProblemWord problem = problemWords.get(problemWordCounter);
        SystemDictionary sysDic = new SystemDictionary("words_alpha.txt");
        UserDictionary userDic = new UserDictionary();

        // Find suggestions
        ErrorCorrector errorCorrector = new ErrorCorrector(sysDic, userDic);
        List<String> suggestions;
        if (problem.getProblemType().equals(ProblemWord.ProblemType.MISSPELLING)) {
            suggestions = errorCorrector.getSuggestionsForMisspelling(problem.getWord());
        } else if (problem.getProblemType().equals(ProblemWord.ProblemType.CAPITALIZATION)) {
            suggestions = errorCorrector.getSuggestionsForCapitalization(problem.getWord());
        } else {
            suggestions = errorCorrector.getSuggestionsForDoubleWord(problem.getWord());
        }

        // Add suggestions to buttons
        if (suggestions.isEmpty()) {
            sug1.setText("N/A");
            sug2.setText("N/A");
            sug3.setText("N/A");
            sug4.setText("N/A");
            sug5.setText("N/A");
        } else if (suggestions.get(0).equals("Delete one")) {
            sug1.setText("N/A");
            sug2.setText("N/A");
            sug3.setText("N/A");
            sug4.setText("N/A");
            sug5.setText("N/A");
        } else if(suggestions.size() == 5) {
            sug1.setText(suggestions.get(0));
            sug2.setText(suggestions.get(1));
            sug3.setText(suggestions.get(2));
            sug4.setText(suggestions.get(3));
            sug5.setText(suggestions.get(4));
        } else if (suggestions.size() == 4) {
            sug1.setText(suggestions.get(0));
            sug2.setText(suggestions.get(1));
            sug3.setText(suggestions.get(2));
            sug4.setText(suggestions.get(3));
            sug5.setText("N/A");
        } else if (suggestions.size() == 3) {
            sug1.setText(suggestions.get(0));
            sug2.setText(suggestions.get(1));
            sug3.setText(suggestions.get(2));
            sug4.setText("N/A");
            sug5.setText("N/A");
        } else if (suggestions.size() == 2) {
            sug1.setText(suggestions.get(0));
            sug2.setText(suggestions.get(1));
            sug3.setText("N/A");
            sug4.setText("N/A");
            sug5.setText("N/A");
        } else if (suggestions.size() == 1) {
            sug1.setText(suggestions.get(0));
            sug2.setText("N/A");
            sug3.setText("N/A");
            sug4.setText("N/A");
            sug5.setText("N/A");
        } else { // suggestions.size().isEmpty()
            System.out.println("ERROR, suggestions part");
        }

        // Enable all suggestion boxes
        sug1.setEnabled(true);
        sug2.setEnabled(true);
        sug3.setEnabled(true);
        sug4.setEnabled(true);
        sug5.setEnabled(true);

        // Disable all suggestion boxes labelled as N/A
        if (sug1.getText().equals("N/A")) {
            sug1.setEnabled(false);
        }
        if (sug2.getText().equals("N/A")) {
            sug2.setEnabled(false);
        }
        if (sug3.getText().equals("N/A")) {
            sug3.setEnabled(false);
        }
        if (sug4.getText().equals("N/A")) {
            sug4.setEnabled(false);
        }
        if (sug5.getText().equals("N/A")) {
            sug5.setEnabled(false);
        }
    }

    // Helper method that adds functionality to the manual fix button
    private static void manualFixButtonFunctionality() {
        manualFixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the enter button beside the manual fix text box
                // find the index of the problem word in the processWords list
                int index = problemWords.get(problemWordCounter).getPosition();

                // obtain the string that the user desires to replace it with
                String manualFixText = manualFixTextField.getText();

                // create a new ProcessedWord to replace it
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(manualFixText, processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());

                // and replace it in the list
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numManCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numManCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // Helper method that adds functionality to the suggestion buttons
    private static void suggestionButtonFunctionality() {
        sug1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the sug1 button, the problem word will be replaced with the word in suggestion 1 box
                // replace the problem word with the sug1 word
                String replacementWord = sug1.getText();
                int index = problemWords.get(problemWordCounter).getPosition();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(replacementWord, processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }});

        sug2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the sug1 button, the problem word will be replaced with the word in suggestion 1 box
                // replace the problem word with the sug1 word
                String replacementWord = sug2.getText();
                int index = problemWords.get(problemWordCounter).getPosition();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(replacementWord,  processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        sug3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the sug1 button, the problem word will be replaced with the word in suggestion 1 box
                // replace the problem word with the sug1 word
                String replacementWord = sug3.getText();
                int index = problemWords.get(problemWordCounter).getPosition();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(replacementWord,  processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        sug4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the sug1 button, the problem word will be replaced with the word in suggestion 1 box
                // replace the problem word with the sug1 word
                String replacementWord = sug4.getText();
                int index = problemWords.get(problemWordCounter).getPosition();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(replacementWord,  processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        sug5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the sug1 button, the problem word will be replaced with the word in suggestion 1 box
                // replace the problem word with the sug1 word
                String replacementWord = sug5.getText();
                int index = problemWords.get(problemWordCounter).getPosition();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(replacementWord, processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numSugCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }

    private static void addToPersonalDict() {
        addToDictionary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent c) {
                try {
                    userDic.addWord(problemWords.get(problemWordCounter).getWord());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    // Helper method that adds functionality to the delete word button
    private static void delWordFunctionality() {
        delWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the delete word button, the problem word will simply be deleted
                int index = problemWords.get(problemWordCounter).getPosition();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord("",  processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numDelCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    updateCountMetrics();
                    updateErrorsMetrics();
                    numDelCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // Helper method that adds functionality to the ignore once button
    private static void ignoreOnceFunctionality() {
        ignoreOnce.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the ignore once button, the problem word will be ignored
                // do so by removing the html tagging around the word
                int index = problemWords.get(problemWordCounter).getPosition();
                String noHTMLword = problemWords.get(problemWordCounter).getWord();
                TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(noHTMLword,  processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                processedWords.set(index, replacementProcessedWord);

                // then create a new string and update the text area
                createNewString();
                textArea.setText(strToSpellCheck);

                // check if you've fixed all the problem words
                if (problemWordCounter+1 == problemWords.size()) {
                    // if so, update the metrics and exit to the end card
                    //updateCountMetrics(); // no need for ignore correction (as none of the counts change)
                    updateErrorsMetrics();
                    numIgnCor++;
                    updateCorrectionsMetrics();
                    exitEditorCard();
                } else {
                    // if not, increment the counter, update the metrics
                    problemWordCounter++;
                    //updateCountMetrics(); // no need for ignore correction (as none of the counts change)
                    updateErrorsMetrics();
                    numIgnCor++;
                    updateCorrectionsMetrics();

                    // and update the manual fix text field + suggestions boxes
                    manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                    try {
                        getSuggestions();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // Helper method that adds functionality to the ignore all button
    private static void ignoreAllFunctionality() {
        ignoreAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // upon hitting the ignore all button, the problem word will be added to the ignoreAllList
                ignoreAllList.add(new ProblemWord(problemWords.get(problemWordCounter).getWord(), -1, null));

                remFullIgnoreAllList();
            }
        });
    }

    // Helper method to check if you are done
    private static void exitEditorCard() {
        // if you are done, add and show an end screen card
        try {
            Launcher.cards.add(EndCard.createEndCard(), "END_CARD");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        CardLayout cl = (CardLayout) (Launcher.cards.getLayout());
        cl.show(Launcher.cards, "END_CARD");
    }

    // Helper method to update count metrics
    private static void updateCountMetrics() {
        String strWithoutHtml = strToSpellCheck.replaceAll("<[^>]*>", "");

        int numChar = strWithoutHtml.length();

        String remSpaces = strWithoutHtml.replaceAll("\\s", "");
        int numCharIgnoreSpaces = remSpaces.length();

        String[] lines = strWithoutHtml.split("\n");
        int numLines = lines.length;

        String[] words = strWithoutHtml.split("\\s+");
        int numWords = words.length;

        StringBuilder str = new StringBuilder();
        str.append(numChar);
        str.append(" characters (includes whitespace)\n");
        str.append(numCharIgnoreSpaces);
        str.append(" characters (excludes whitespace)\n");
        str.append(numLines);
        str.append(" lines\n");
        str.append(numWords);
        str.append(" words");

        metricsCounts.setText(str.toString());
    }

    // Helper method to update errors metrics
    private static void updateErrorsMetrics() {
        int numMisspellRem = 0;
        int numMiscapRem = 0;
        int numDoubRem = 0;

        for (int i = problemWordCounter; i < problemWords.size(); i++) {
            if (problemWords.get(i).getProblemType().equals(ProblemWord.ProblemType.MISSPELLING)) {
                numMisspellRem++;
            } else if (problemWords.get(i).getProblemType().equals(ProblemWord.ProblemType.CAPITALIZATION)) {
                numMiscapRem++;
            } else {
                numDoubRem++;
            }
        }

        StringBuilder str = new StringBuilder();
        str.append("Error Type: # remaining (# initial)\n");
        str.append("Misspellings: ");
        str.append(numMisspellRem);
        str.append(" (");
        str.append(numMisspellInitial);
        str.append(")\n");
        str.append("Miscapitalizations: ");
        str.append(numMiscapRem);
        str.append(" (");
        str.append(numMiscapInitial);
        str.append(")\n");
        str.append("Double Words: ");
        str.append(numDoubRem);
        str.append(" (");
        str.append(numDoubInitial);
        str.append(")\n");

        metricsErrors.setText(str.toString());
    }

    // Helper method to update corrections metrics
    private static void updateCorrectionsMetrics() {
        StringBuilder str = new StringBuilder();
        str.append("Correction Methods Used\n");
        str.append("Manual corrections: ");
        str.append(numManCor);
        str.append("\n");
        str.append("Suggested corrections: ");
        str.append(numSugCor);
        str.append("\n");
        str.append("Deletion corrections: ");
        str.append(numDelCor);
        str.append("\n");
        str.append("Ignored: ");
        str.append(numIgnCor);
        str.append("\n");

        metricsCorrections.setText(str.toString());
    }

    // Used to reset variables in EditorCard in the case that user wants to spell check another document in the EndCard
    public static void resetVariables() {
        editorCard=null;
        strToSpellCheck=null;
        processedWords=null;
        problemWords=null;
        textArea=null;
        manualFixTextField=null;
        manualFixButton=null;
        sug1=null;
        sug2=null;
        sug3=null;
        sug4=null;
        sug5=null;
        delWord=null;
        ignoreOnce=null;
        ignoreAll=null;
        problemWordCounter = 0;
        metricsCounts=null;
        metricsErrors=null;
        metricsCorrections=null;
        numMisspellInitial=0;
        numMiscapInitial=0;
        numDoubInitial=0;
        numManCor=0;
        numSugCor=0;
        numDelCor=0;
        int numIgnCor=0;
    }

    private static void remFullIgnoreAllList() {
        for (int i = 0; i < ignoreAllList.size(); i++) {
            for (int j = 0; j < problemWords.size(); j++) {
                // if it's in the ignoreAllList, ignore all occurrences of the problem
                if (ignoreAllList.get(i).getWord().equals(problemWords.get(j).getWord())) {
                    // if found, ignore once
                    int index = problemWords.get(j).getPosition();
                    String noHTMLword = problemWords.get(problemWordCounter).getWord();
                    TextProcessor.ProcessedWord replacementProcessedWord = new TextProcessor.ProcessedWord(noHTMLword,  processedWords.get(index).getPuncAfterWord(), processedWords.get(index).getPuncRequiresCapAfter());
                    processedWords.set(index, replacementProcessedWord);

                    // then create a new string and update the text area
                    createNewString();
                    textArea.setText(strToSpellCheck);

                    // check if you've fixed all the problem words
                    if (problemWordCounter+1 == problemWords.size()) {
                        // if so, update the metrics and exit to the end card
                        //updateCountMetrics(); // no need for ignore correction (as none of the counts change)
                        updateErrorsMetrics();
                        numIgnCor++;
                        updateCorrectionsMetrics();
                        exitEditorCard();
                    } else {
                        // if not, increment the counter, update the metrics
                        problemWordCounter++;
                        //updateCountMetrics(); // no need for ignore correction (as none of the counts change)
                        updateErrorsMetrics();
                        numIgnCor++;
                        updateCorrectionsMetrics();

                        // and update the manual fix text field + suggestions boxes
                        manualFixTextField.setText(problemWords.get(problemWordCounter).getWord());
                        try {
                            getSuggestions();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
    }

    // Setter and getter
    public static void setStrToSpellCheck(String str) { strToSpellCheck = str; }
    public static String getStrToSpellCheck() { return strToSpellCheck; }
}